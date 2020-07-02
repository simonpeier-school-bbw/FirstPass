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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public Application secureData(Application application, String key, boolean encrypt) {
        return secureData(new ArrayList<>(Collections.singletonList(application)), key, encrypt).get(0);
    }

    public List<Application> secureData(List<Application> applications, String key, boolean encrypt) {
        Cipher cipher = getCipher(key, encrypt);

        for (Application application : applications) {
            if (cipher != null) {
                if (encrypt) {
                    try {
                        application.setName(Base64.encodeBase64String(cipher.doFinal(application.getName().getBytes())));
                        application.setUsername(Base64.encodeBase64String(cipher.doFinal(application.getUsername().getBytes())));
                        application.setDescription(Base64.encodeBase64String(cipher.doFinal(application.getDescription().getBytes())));
                        application.setPassword(Base64.encodeBase64String(cipher.doFinal(application.getPassword().getBytes())));
                    } catch (IllegalBlockSizeException | BadPaddingException e) {
                        System.out.println("Could not encrypt data: " + e.getClass());
                    }
                } else {
                    try {
                        application.setName(new String(cipher.doFinal(Base64.decodeBase64(application.getName().getBytes()))));
                        application.setUsername(new String(cipher.doFinal(Base64.decodeBase64(application.getUsername().getBytes()))));
                        application.setDescription(new String(cipher.doFinal(Base64.decodeBase64(application.getDescription().getBytes()))));
                        application.setPassword(new String(cipher.doFinal(Base64.decodeBase64(application.getPassword().getBytes()))));
                    } catch (IllegalBlockSizeException | BadPaddingException e) {
                        System.out.println("Could not decrypt data: " + e.getClass());
                    }
                }
            }
        }
        return applications;
    }

    private Cipher getCipher(String key, boolean encrypt) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.out.println("This encryption algorithm does not exist: " + e.getClass());
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        try {
            if (encrypt) {
                Objects.requireNonNull(cipher).init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } else {
                Objects.requireNonNull(cipher).init(Cipher.DECRYPT_MODE, secretKeySpec);
            }
        } catch (InvalidKeyException e) {
            System.out.println("Cipher key is invalid: " + e.getClass());
        }
        return cipher;
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
}
