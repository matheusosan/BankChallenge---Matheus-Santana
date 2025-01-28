package br.com.compass.application.security;

public interface IEncryptionService {

    String encryptPassword(String purePassword);

    boolean verifyPassword(String purePassword, String hashedPassword);
}
