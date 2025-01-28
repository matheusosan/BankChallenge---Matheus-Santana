package br.com.compass.application.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptService implements IEncryptionService {

    @Override
    public String encryptPassword(String purePassword) {
        return BCrypt.withDefaults().hashToString(8, purePassword.toCharArray());
    }

    @Override
    public boolean verifyPassword(String purePassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(purePassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
