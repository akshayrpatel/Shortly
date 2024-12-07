package com.url.shortener.dto;

import lombok.Data;
import java.util.Set;

@Data
public class LoginRequest {
    private String username;
    private Set<String> role;
    private String password;
}
