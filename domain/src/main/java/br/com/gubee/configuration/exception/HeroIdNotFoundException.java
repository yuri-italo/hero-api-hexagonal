package br.com.gubee.configuration.exception;

import java.util.UUID;

public class HeroIdNotFoundException extends RuntimeException {
    public HeroIdNotFoundException(String message) {
        super(message);
    }

    public HeroIdNotFoundException(UUID id) {
        this(String.format("Hero ID not found: %s", id));
    }
}
