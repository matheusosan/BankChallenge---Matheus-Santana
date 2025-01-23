package br.com.compass.application.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptService implements ICriptografiaService{

    @Override
    public String criptografarSenha(String senhaPura) {
        return BCrypt.withDefaults().hashToString(8, senhaPura.toCharArray());
    }

    @Override
    public boolean verificarSenha(String senhaPura, String senhaHasheada) {
        BCrypt.Result resultado = BCrypt.verifyer().verify(senhaPura.toCharArray(), senhaHasheada);
        return resultado.verified;
    }
}
