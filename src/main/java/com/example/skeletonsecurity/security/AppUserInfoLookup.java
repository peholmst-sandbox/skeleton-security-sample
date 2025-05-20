package com.example.skeletonsecurity.security;

import com.example.skeletonsecurity.security.domain.UserId;

import java.util.Optional;

public interface AppUserInfoLookup {

    Optional<AppUserInfo> getUserInfo(UserId userId);
}
