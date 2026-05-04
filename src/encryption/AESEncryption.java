package encryption;

public class AESEncryption implements IEncryptable {

    private String secretKey;

    public AESEncryption(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String encrypt(String data) {
        // TODO: implement AES encryption
        return "";
    }

    @Override
    public String decrypt(String data) {
        // TODO: implement AES decryption
        return "";
    }
}
