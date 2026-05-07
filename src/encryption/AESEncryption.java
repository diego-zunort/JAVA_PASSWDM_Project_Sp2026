package encryption;
import javax.crypto.Cipher; // cipher used for encryption/decryption operations
import javax.crypto.KeyGenerator;   // used to generate a 128 size security key
import javax.crypto.SecretKey;      // key type
import java.security.GeneralSecurityException;  // used to signal problems in the operations
import java.util.Base64;       

public class AESEncryption implements IEncryptable {

    public AESEncryption() {
                                    // constructor
    }

    static public SecretKey generateKey(){          // generates and returns a private key needed
        try {KeyGenerator keyGen = KeyGenerator.getInstance("AES");     //does AES
            keyGen.init(128);           // Size of key
            SecretKey secretKey = keyGen.generateKey();     // Generates the key and stores it
            return secretKey;                   // returns the key
        } catch (GeneralSecurityException e){    // if it fails, returns exception
            throw new IllegalStateException("Failed to generate AES key", e);
        }

    }

    @Override
    public String encrypt(String data, SecretKey secretKey) {   // encrypts the data provided using the secret key
        try{Cipher cipher = Cipher.getInstance("AES");      // uses the cipher with AES
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);    // Configures the cipher in encrypt mode using the key
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());    //
            return Base64.getEncoder().encodeToString(encryptedBytes);  // returns the encrypted string
        } catch (GeneralSecurityException e){   // if it fails, returns exception
            throw new IllegalStateException("Failed to encrypt", e);
        }
    }

    @Override
    public String decrypt(String data, SecretKey secretKey) {   // decrypts the data provided using the key
        try{Cipher cipher = Cipher.getInstance("AES");  // sets the cipher to AES
            cipher.init(Cipher.DECRYPT_MODE, secretKey);    // Configures the cipher in decrypt mode using the key
            byte[] decodeBytes = Base64.getDecoder().decode(data);  //
            byte[] decryptedBytes = cipher.doFinal(decodeBytes);    // 
            return new String(decryptedBytes);      // returns the decrypted password
        } catch (GeneralSecurityException e){   // if it fails, returns exception
            throw new IllegalStateException("Failed to decrypt", e);
        }
    }
}
