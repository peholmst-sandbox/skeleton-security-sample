package com.example.skeletonsecurity.security.domain;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Domain primitive for the user ID. In OIDC, this is the "subject", but I think more users are familiar with the
 * term "UserId".
 */
public final class UserId implements Serializable {

    private final String userId;

    private UserId(String userId) {
        // If the userId has a specific format, validate it here.
        this.userId = requireNonNull(userId);
    }

    public static UserId of(String userId) {
        return new UserId(userId);
    }

    @Override
    public String toString() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserId that = (UserId) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
