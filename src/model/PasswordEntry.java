package model;
import encryption.AESEncryption;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordEntry {

    private String siteName;
    private String username;
    private String password;
    private String category;
    private final AESEncryption aes = new AESEncryption();
    private String encodedKey;
    private transient SecretKey secretKey;

    public PasswordEntry(String siteName, String username, String password, String category) {
        this.siteName = siteName;
        this.username = username;
        this.secretKey = AESEncryption.generateKey();
        this.encodedKey = Base64.getEncoder().encodeToString(this.secretKey.getEncoded());
        this.password = aes.encrypt(password, this.secretKey);
        this.category = category;
    }

    public String getSiteName() { return siteName; }
    public String getUsername() { return username; }
    public String getCategory() { return category; }

    public String getPassword() {
        return aes.decrypt(password, getSecretKey());
    }

    public void setPassword(String newPwd) {
        SecretKey key = getSecretKey();
        this.password = aes.encrypt(newPwd, key);
    }

    private SecretKey getSecretKey() {
        if (secretKey != null) {
            return secretKey;
        }
        if (encodedKey == null) {
            secretKey = AESEncryption.generateKey();
            encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            return secretKey;
        }
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        secretKey = new SecretKeySpec(keyBytes, "AES");
        return secretKey;
    }
}
