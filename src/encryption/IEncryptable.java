package encryption;

public interface IEncryptable {
    String encrypt(String data);
    String decrypt(String data);
}
