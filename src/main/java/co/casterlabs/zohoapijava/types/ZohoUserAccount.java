package co.casterlabs.zohoapijava.types;

import java.util.List;

import co.casterlabs.rakurai.json.annotating.JsonClass;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonClass(exposeAll = true)
public class ZohoUserAccount {
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
    private List<ZohoUserAccount.EmailAddress> emailAddress;
    private String mailboxStatus;
    private String basicStorage;
    private String encryptedZuid;
    private String lastClient;
    private long allowedStorage;
    private long usedStorage;
    private List<ZohoUserAccount.SendMailDetail> sendMailDetails;
    private float popFetchTime;
    private ZohoUserAccount.Address address;
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
    @JsonClass(exposeAll = true)
    public static class EmailAddress {
        private boolean isAlias;
        private boolean isPrimary;
        private String mailId;
        private boolean isConfirmed;

    }

    @Getter
    @ToString
    @JsonClass(exposeAll = true)
    public static class SendMailDetail {
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
    @JsonClass(exposeAll = true)
    public static class Address {
        private String country;
        private String streetAddr;
        private String city;
        private String postalCode;
        private String state;

    }

}