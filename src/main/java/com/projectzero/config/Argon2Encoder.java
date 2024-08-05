package com.projectzero.config;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Argon2Encoder implements PasswordEncoder {

    private final Argon2 argon2;

    public Argon2Encoder() {
        this.argon2 = Argon2Factory.create();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        // Use the newer method for hashing
        return argon2.hash(4, 65536, 1, rawPassword.toString().toCharArray());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // Use the newer method for verification
        return argon2.verify(encodedPassword, rawPassword.toString().toCharArray());
    }
}
