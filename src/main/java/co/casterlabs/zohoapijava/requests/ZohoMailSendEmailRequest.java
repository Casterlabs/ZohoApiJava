package co.casterlabs.zohoapijava.requests;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.zohoapijava.HttpUtil;
import co.casterlabs.zohoapijava.ZohoApi;
import co.casterlabs.zohoapijava.ZohoAuth;
import co.casterlabs.zohoapijava.ZohoUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Response;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ZohoMailSendEmailRequest extends AuthenticatedWebRequest<Void, ZohoAuth> {
    private @NonNull String accountId;
    private @NonNull String fromAddress;
    private @NonNull String toAddress;
    private @Nullable String ccAddress;
    private @Nullable String bccAddress;

    private @NonNull String encoding = "UTF-8";
    private @NonNull String subject;
    private @NonNull @Setter(AccessLevel.NONE) String content;
    private @NonNull @Setter(AccessLevel.NONE) String mailFormat;

    public ZohoMailSendEmailRequest(@NonNull ZohoAuth auth) {
        super(auth);
    }

    public ZohoMailSendEmailRequest setContentsAsHtml(String html) {
        this.mailFormat = "html";
        this.content = html;

        return this;
    }

    public ZohoMailSendEmailRequest setContentsAsPlaintext(String text) {
        this.mailFormat = "plaintext";
        this.content = text;

        return this;
    }

    @Override
    protected Void execute() throws ApiException, ApiAuthException, IOException {
        if (ZohoUtil.verifyNotNull(this.accountId, this.fromAddress, this.toAddress, this.encoding, this.subject, this.content, this.mailFormat)) {
            String url = String.format("https://mail.zoho.com/api/accounts/%s/messages", this.accountId);

            JsonObject body = new JsonObject();

            body.addProperty("fromAddress", this.fromAddress);
            body.addProperty("toAddress", this.toAddress);

            body.addProperty("encoding", this.encoding);
            body.addProperty("subject", this.subject);
            body.addProperty("content", this.content);
            body.addProperty("mailFormat", this.mailFormat);

            if (this.ccAddress != null) {
                body.addProperty("ccAddress", this.ccAddress);
            }

            if (this.bccAddress != null) {
                body.addProperty("bccAddress", this.bccAddress);
            }

            try (Response response = HttpUtil.sendHttp(body.toString(), url, null, "application/json", this.auth)) {
                JsonObject json = ZohoApi.GSON.fromJson(response.body().string(), JsonObject.class);

                if (response.isSuccessful()) {
                    return null;
                } else {
                    throw new ApiException(json.toString());
                }
            }
        } else {
            throw new ApiException("Missing required parameter.");
        }
    }

}
