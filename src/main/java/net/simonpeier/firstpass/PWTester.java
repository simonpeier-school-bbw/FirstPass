package net.simonpeier.firstpass;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PWTester {
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Cypher cypher = new Cypher();

        String salt = cypher.generateSalt();
        System.out.println("Salt: " + salt);

        System.out.println(cypher.hashPassword("asdf", salt));
    }
}
