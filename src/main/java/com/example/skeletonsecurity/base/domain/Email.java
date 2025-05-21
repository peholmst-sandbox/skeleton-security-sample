package com.example.skeletonsecurity.base.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Domain primitive representing an email address.
 */
public final class Email implements Serializable {

    private final String email;

    private Email(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }

    /**
     * Creates a new {@code Email} instance.
     *
     * @param email the email address (never {@code null}).
     * @return a new {@code Email} instance.
     * @throws IllegalArgumentException if the email is invalid.
     */
    public static Email of(String email) {
        return new Email(email);
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }

    /**
     * Checks if the given email address is valid.
     *
     * @param email the email address to check (never {@code null}).
     * @return {@code true} if the email address is valid, {@code false} otherwise.
     */
    public static boolean isValid(String email) {
        // Check length
        if (email.isEmpty() || email.length() > 320) {
            return false;
        }
        var parts = email.split("@");
        // Check the number of parts
        if (parts.length != 2) {
            return false;
        }
        // Validate parts
        return isValidLocalPart(parts[0]) && isValidDomainName(parts[1]);
    }

    private static boolean isValidLocalPart(String localPart) {
        // Check length
        if (localPart.isEmpty() || localPart.length() > 64) {
            return false;
        }
        // Check for invalid characters
        if (!localPart.matches("[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+")) {
            return false;
        }
        // Check for double dots
        if (localPart.contains("..")) {
            return false;
        }
        // Check for leading or trailing dots
        return !localPart.startsWith(".") && !localPart.endsWith(".");
    }

    private static boolean isValidDomainName(String domainName) {
        // Check length
        if (domainName.isEmpty() || domainName.length() > 253) {
            return false;
        }
        var labels = domainName.split("\\.", -1);
        for (var label : labels) {
            // Check label length
            if (label.isEmpty() || label.length() > 63) {
                return false;
            }
            // Check label characters (only ASCII letters, digits, and "-" are allowed)
            for (var c : label.toCharArray()) {
                if (!Character.isDigit(c) && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != '-') {
                    return false;
                }
            }
            // Check that the label does not start or end with a "-"
            if (label.charAt(0) == '-' || label.charAt(label.length() - 1) == '-') {
                return false;
            }
        }
        return true;
    }
}
