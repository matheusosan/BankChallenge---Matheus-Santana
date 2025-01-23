package br.com.compass.application.security;

public interface ICriptografiaService {

    String criptografarSenha(String senhaPura);

    boolean verificarSenha(String senhaPura, String senhaHasheada);
}
