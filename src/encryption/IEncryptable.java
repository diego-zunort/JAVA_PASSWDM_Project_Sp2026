package encryption;

import javax.crypto.SecretKey;

public interface IEncryptable {
    String encrypt(String data, SecretKey key);
    String decrypt(String data, SecretKey key);
}
