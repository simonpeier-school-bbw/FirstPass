package net.simonpeier.firstpass.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.simonpeier.firstpass.model.Application;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 50000, 128);
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

    public List<Application> decryptData(String encryptedEntriesB64, String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] b64DecodedBytes = Base64.decodeBase64(encryptedEntriesB64);
            byte[] decryptedEntries = cipher.doFinal(b64DecodedBytes);
            String jsonString = new String(decryptedEntries);
            return objectMapper.readValue(jsonString, new TypeReference<>() {
            });

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptData(List<Application> applications, String key) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writeValueAsString(applications);
            byte[] byteStream = jsonString.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] bytes = cipher.doFinal(byteStream);
            return Base64.encodeBase64String(bytes);

        } catch (JsonProcessingException | BadPaddingException | NoSuchPaddingException | IllegalBlockSizeException
                | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
