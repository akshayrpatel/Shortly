package com.url.shortener.dto;

import com.url.shortener.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String token;
}
