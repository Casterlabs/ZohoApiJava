package co.casterlabs.zohoapijava.requests;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import co.casterlabs.apiutil.auth.ApiAuthException;
import co.casterlabs.apiutil.web.ApiException;
import co.casterlabs.apiutil.web.AuthenticatedWebRequest;
import co.casterlabs.zohoapijava.HttpUtil;
import co.casterlabs.zohoapijava.ZohoApi;
import co.casterlabs.zohoapijava.ZohoAuth;
import co.casterlabs.zohoapijava.requests.ZohoMailGetUserAccountDetailsRequest.ZohoUserAccount;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import okhttp3.Response;

public class ZohoMailGetUserAccountDetailsRequest extends AuthenticatedWebRequest<List<ZohoUserAccount>, ZohoAuth> {
    private static final Type LIST_TYPE = new TypeToken<List<ZohoUserAccount>>() {
    }.getType();

    public ZohoMailGetUserAccountDetailsRequest(@NonNull ZohoAuth auth) {
        super(auth);
    }

    @Override
    protected List<ZohoUserAccount> execute() throws ApiException, ApiAuthException, IOException {
        try (Response response = HttpUtil.sendHttpGet("https://mail.zoho.com/api/accounts", null, this.auth)) {
            JsonObject json = ZohoApi.GSON.fromJson(response.body().string(), JsonObject.class);

            if (response.isSuccessful()) {
                List<ZohoUserAccount> result = ZohoApi.GSON.fromJson(json.get("data"), LIST_TYPE);

                return result;
            } else {
                throw new ApiException(json.toString());
            }
        }
    }

    @Getter
    @ToString
    public static class ZohoUserAccount {
        private String lastName;
        private String country;
        private long lastLogin;
        private String accountDisplayName;
        private String role;
        private String gender;
        private boolean activeSyncEnabled;
        private String accountName;
        private String displayName;
        private String mobileNumber;
        private boolean isCustomAdmin;
        private boolean incomingBlocked;
        private String language;
        private boolean isLogoExist;
        private String type;
        private String URI;
        private String primaryEmailAddress;
        private boolean enabled;
        private long mailboxCreationTime;
        private String incomingUserName;
        private List<EmailAddress> emailAddress;
        private String mailboxStatus;
        private String basicStorage;
        private String encryptedZuid;
        private String lastClient;
        private long allowedStorage;
        private long usedStorage;
        private List<SendMailDetail> sendMailDetails;
        private float popFetchTime;
        private Address address;
        private long userExpiry;
        private boolean popAccessEnabled;
        private boolean spamcheckEnabled;
        private boolean imapAccessEnabled;
        private String timeZone;
        private long accountCreationTime;
        private long zuid;
        private String firstName;
        private String accountId;
        private long sequence;
        private boolean outgoingBlocked;
        private String mailboxAddress;
        private float lastPasswordReset;
        private boolean tfaEnabled;
        private String phoneNumer;
        private boolean status;

        @Getter
        @ToString
        public class EmailAddress {
            private boolean isAlias;
            private boolean isPrimary;
            private String mailId;
            private boolean isConfirmed;

        }

        @Getter
        @ToString
        public class SendMailDetail {
            private String sendMailId;
            private String displayName;
            private String serverName;
            private String signatureId;
            private int serverPort;
            private String userName;
            private String connectionType;
            private String mode;
            private boolean validated;
            private String fromAddress;
            private long smtpConnection;
            private boolean validationRequired;
            private long validationState;
            private boolean status;

        }

        @Getter
        @ToString
        public class Address {
            private String country;
            private String streetAddr;
            private String city;
            private String postalCode;
            private String state;

        }

    }

}
