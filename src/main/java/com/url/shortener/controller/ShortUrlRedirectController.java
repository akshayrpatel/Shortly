package com.url.shortener.controller;

import com.url.shortener.service.UrlMappingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/r")
@AllArgsConstructor
public class ShortUrlRedirectController {

    private UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public void getOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = urlMappingService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            response.sendRedirect(originalUrl); // 302 redirect
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found");
        }
    }
}
