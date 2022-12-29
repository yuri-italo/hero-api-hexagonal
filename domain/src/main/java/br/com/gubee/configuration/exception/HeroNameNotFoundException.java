package br.com.gubee.configuration.exception;

public class HeroNameNotFoundException extends RuntimeException {
    public HeroNameNotFoundException(String message) {
        super(message);
    }
}
