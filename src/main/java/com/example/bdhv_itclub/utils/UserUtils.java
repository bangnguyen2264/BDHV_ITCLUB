package com.example.bdhv_itclub.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class UserUtils {
    public static String getResidentName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
            log.info("get connectedUser: {}", auth.getName());
        return auth != null ? auth.getName() : null;
    }
}