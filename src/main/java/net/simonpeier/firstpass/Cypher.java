package net.simonpeier.firstpass;

import net.simonpeier.firstpass.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Cypher {
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    // hashes password of given User using the provided salt
    public String hashPassword(User user, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String plain = user.getPassword();

        KeySpec spec = new PBEKeySpec(plain.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        String hash = Arrays.toString(factory.generateSecret(spec).getEncoded());

        return hash;
    }
}
