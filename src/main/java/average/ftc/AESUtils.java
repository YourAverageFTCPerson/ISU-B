package average.ftc;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Optional;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final System.Logger LOGGER = System.getLogger(AESUtils.class.getName());

    public static final String ALGORITHM = "AES/GCM/NoPadding";

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static SecretKey createKey(final String algorithm, final int keysize, final Optional<Provider> provider, final Optional<SecureRandom> rng) throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator;
        if (provider.isPresent()) {
            keyGenerator = KeyGenerator.getInstance(algorithm, provider.get());
        } else {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        }

        if (rng.isPresent()) {
            keyGenerator.init(keysize, rng.get());
        } else {
            // not really needed for the Sun provider which handles null OK
            keyGenerator.init(keysize);
        }

        return keyGenerator.generateKey();
    }

    public static SecretKey getKeyFromPassword(char[] password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private AESUtils() {
        throw new UnsupportedOperationException();
    }

    public static final SecureRandom RANDOM;

    static {
        SecureRandom temp;
        try {
            temp = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(System.Logger.Level.WARNING, "No strong SecureRandoms");
            temp = new SecureRandom();
        }
        RANDOM = temp;
    }

    public record Encrypted(byte[] cipherText, byte[] iv) {
    }

    public static Encrypted encrypt(byte[] message, SecretKey sk) {
        byte[] iv = new byte[12];
        RANDOM.nextBytes(iv);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, sk, new GCMParameterSpec(128, iv));
            return new Encrypted(cipher.doFinal(message), iv);
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, (String) null, e);
            System.exit(1);
            throw new AssertionError();
        }
    }

    public static byte[] decrypt(Encrypted encrypted, SecretKey sk) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, sk, new GCMParameterSpec(128, encrypted.iv));
            return cipher.doFinal(encrypted.cipherText);
        } catch (Exception e) {
            LOGGER.log(System.Logger.Level.ERROR, (String) null, e);
            System.exit(1);
            throw new AssertionError();
        }
    }
}
