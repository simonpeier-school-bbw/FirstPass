package net.simonpeier.firstpass.security;

import net.simonpeier.firstpass.model.Application;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;

public class Cypher {

    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];

        random.nextBytes(salt);

        return byteArrToString(salt);
    }

    // hashes password of given User using the provided salt
    public String hashPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        return byteArrToString(hash);
    }

    private String byteArrToString(byte[] byteArr) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArr) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public List<Application> secureData(List<Application> applications, String key, boolean encrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        }

        for (Application application : applications) {
            application.setName(Base64.encodeBase64String(cipher.doFinal(application.getName().getBytes())));
            application.setUsername(Base64.encodeBase64String(cipher.doFinal(application.getUsername().getBytes())));
            application.setDescription(Base64.encodeBase64String(cipher.doFinal(application.getDescription().getBytes())));
            application.setPassword(Base64.encodeBase64String(cipher.doFinal(application.getPassword().getBytes())));
        }

        return applications;
    }
}
