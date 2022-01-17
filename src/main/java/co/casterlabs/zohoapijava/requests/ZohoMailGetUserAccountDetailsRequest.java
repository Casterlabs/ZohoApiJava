package co.casterlabs.zohoapijava.requests;

import java.io.IOException;
import java.util.List;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.rakurai.json.Rson;
import co.casterlabs.rakurai.json.TypeToken;
import co.casterlabs.rakurai.json.element.JsonObject;
import co.casterlabs.zohoapijava.ZohoAuth;
import co.casterlabs.zohoapijava.ZohoHttpUtil;
import co.casterlabs.zohoapijava.types.ZohoUserAccount;
import lombok.NonNull;
import okhttp3.Response;

public class ZohoMailGetUserAccountDetailsRequest extends AuthenticatedWebRequest<List<ZohoUserAccount>, ZohoAuth> {
    private static final TypeToken<List<ZohoUserAccount>> LIST_TYPE = new TypeToken<List<ZohoUserAccount>>() {
    };

    public ZohoMailGetUserAccountDetailsRequest(@NonNull ZohoAuth auth) {
        super(auth);
    }

    @Override
    protected List<ZohoUserAccount> execute() throws ApiException, ApiAuthException, IOException {
        try (Response response = ZohoHttpUtil.sendHttpGet("https://mail.zoho.com/api/accounts", null, this.auth)) {
            JsonObject json = Rson.DEFAULT.fromJson(response.body().string(), JsonObject.class);

            if (response.isSuccessful()) {
                List<ZohoUserAccount> result = Rson.DEFAULT.fromJson(json.get("data"), LIST_TYPE);

                return result;
            } else {
                throw new ApiException(json.toString());
            }
        }
    }

}
