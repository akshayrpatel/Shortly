package com.url.shortener.controller;

import com.url.shortener.dto.UrlMappingRequest;
import com.url.shortener.dto.UrlMappingResponse;
import com.url.shortener.model.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {

    private UserService userService;
    private UrlMappingService urlMappingService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<? extends UrlMappingResponse> createShortUrl(@RequestBody UrlMappingRequest urlMappingRequest, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        UrlMappingResponse urlMappingResponse = urlMappingService.createShortUrl(urlMappingRequest, user);
        return ResponseEntity.ok(urlMappingResponse);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<? extends List<UrlMappingResponse>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(urlMappingService.getUserUrls(user));
    }
}
