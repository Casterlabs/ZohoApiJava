package co.casterlabs.zohoapijava;

import java.io.IOException;

import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.auth.AuthProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import okhttp3.Request.Builder;
import okhttp3.Response;

@Getter
public class ZohoAuth implements AuthProvider, AutoCloseable {
    private @Setter boolean autoRefresh = true;

    private String refreshToken;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;

    private String accessToken;

    public ZohoAuth(@NonNull String refreshToken, @NonNull String clientId, @NonNull String clientSecret, @NonNull String redirectUri, @NonNull String scope) throws ApiAuthException {
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;

        this.refresh();
    }

    @Override
    public void refresh() throws ApiAuthException {
        String url = String.format(
            "https://accounts.zoho.com/oauth/v2/token" +
                "?refresh_token=%s" +
                "&client_id=%s" +
                "&client_secret=%s" +
                "&redirect_uri=%s" +
                "&scope=%s" +
                "&grant_type=refresh_token",
            this.refreshToken,
            this.clientId,
            this.clientSecret,
            this.redirectUri,
            this.scope
        );

        try (Response response = HttpUtil.sendHttp("{}", url, null, null, null)) {
            JsonObject json = ZohoApi.GSON.fromJson(response.body().string(), JsonObject.class);

            if (json.has("error")) {
                throw new ApiAuthException(json.get("error").getAsString());
            } else {
                this.accessToken = json.get("access_token").getAsString();

                long expiresInSeconds = json.get("expires_in").getAsLong();

                if (this.autoRefresh) {
                    ThreadHelper.executeAsyncLater(
                        "ZohoAuth Token Refresh Thread",
                        () -> {
                            if (this.autoRefresh) {
                                try {
                                    this.refresh();
                                } catch (ApiAuthException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        expiresInSeconds * 1000 // Secs -> Millis
                    );
                }
            }
        } catch (IOException e) {
            throw new ApiAuthException(e);
        }
    }

    @Override
    public void authenticateRequest(@NonNull Builder request) {
        request.header("Authorization", "Zoho-oauthtoken " + this.accessToken);
    }

    @Override
    public void close() throws IOException {
        this.autoRefresh = false;
    }

    public static ZohoAuth verifyOAuthCode(@NonNull String code, @NonNull String clientId, @NonNull String clientSecret, @NonNull String redirectUri, @NonNull String scope) throws ApiAuthException {
        String url = String.format(
            "https://accounts.zoho.com/oauth/v2/token" +
                "?code=%s" +
                "&client_id=%s" +
                "&client_secret=%s" +
                "&redirect_uri=%s" +
                "&scope=%s" +
                "&grant_type=authorization_code",
            code,
            clientId,
            clientSecret,
            redirectUri,
            scope
        );

        try (Response response = HttpUtil.sendHttp("{}", url, null, null, null)) {
            JsonObject json = ZohoApi.GSON.fromJson(response.body().string(), JsonObject.class);

            if (json.has("error")) {
                throw new ApiAuthException(json.get("error").getAsString());
            } else {
                String refreshToken = json.get("refresh_token").getAsString();

                return new ZohoAuth(refreshToken, clientId, clientSecret, redirectUri, scope);
            }
        } catch (IOException e) {
            throw new ApiAuthException(e);
        }
    }

    public static String getOAuthOfflineUrl(@NonNull String scope, @NonNull String clientId, @NonNull String redirectUri) {
        return String.format(
            "https://accounts.zoho.com/oauth/v2/auth" +
                "?scope=%s&client_id=%s" +
                "&response_type=code" +
                "&access_type=offline" +
                "&redirect_uri=%s",
            scope,
            clientId,
            redirectUri
        );
    }

}
