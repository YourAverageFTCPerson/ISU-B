import average.ftc.AESUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AESUtilsTest {
    @BeforeAll
    public static void beforeAll() throws ClassNotFoundException {
        Class.forName(AESUtils.class.getName());
    }

    @Test
    public void encrypt_encrypts() throws NoSuchAlgorithmException {
        SecretKey sk = AESUtils.createKey(AESUtils.ALGORITHM.substring(0, AESUtils.ALGORITHM.indexOf('/')), 128, Optional.empty(), Optional.of(AESUtils.RANDOM));
        assertEquals("Hello World!", new String(AESUtils.decrypt(AESUtils.encrypt("Hello World!".getBytes(), sk), sk)));
    }
}
