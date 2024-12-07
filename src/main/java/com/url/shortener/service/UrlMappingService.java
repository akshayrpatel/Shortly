package com.url.shortener.service;

import ch.qos.logback.core.util.MD5Util;
import com.url.shortener.dto.UrlMappingRequest;
import com.url.shortener.dto.UrlMappingResponse;
import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import com.url.shortener.repository.UrlMappingRepository;
import io.jsonwebtoken.io.Encoders;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private UrlMappingResponse convert(UrlMapping urlMapping) {
        return new UrlMappingResponse(
                urlMapping.getOriginalUrl(),
                urlMapping.getShortUrl(),
                urlMapping.getClickCount(),
                urlMapping.getCreatedDate()
        );
    }

    private String generateShortUrl(String originalUrl) {
        /*
            1. Generate MD5 hash of 128 bit, 32 hexadecimal characters
            2. Use a portion of the hash, lets say first 8 characters, and encode in Base64
         */
        try {
            MD5Util md5Util = new MD5Util();
            byte[] md5Hash = md5Util.md5Hash(originalUrl);
            byte[] md5ShortHash = Arrays.copyOfRange(md5Hash, 0, 8);;

            BigInteger value = new BigInteger(1, md5ShortHash); // avoid negative values
            StringBuilder shortUrl = new StringBuilder();

            while (value.compareTo(BigInteger.ZERO) > 0) {
                BigInteger[] divmod = value.divideAndRemainder(BigInteger.valueOf(62));
                shortUrl.append(BASE62.charAt(divmod[1].intValue()));
                value = divmod[0];
            }
            return shortUrl.reverse().toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to generate shortUrl for originalUrl: " + originalUrl);
        }
    }

    public UrlMappingResponse createShortUrl(UrlMappingRequest urlMappingRequest, User user) {
        String originalUrl = urlMappingRequest.getOriginalUrl();
        String shortUrl = generateShortUrl(originalUrl);
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setUser(user);
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());
        return convert(urlMappingRepository.save(urlMapping));
    }

    public List<UrlMappingResponse> getUserUrls(User user) {
        List<UrlMapping> urls = urlMappingRepository.findByUser(user);
        return urls.stream().map(this::convert).toList();
    }

    public String getOriginalUrl(String shortUrl) {
        List<UrlMapping> urlMappings = urlMappingRepository
                .findByShortUrl(shortUrl);

        urlMappings.forEach(url -> {
            url.setClickCount(url.getClickCount() + 1);
        });

        // update clickCount and save to db
        urlMappingRepository.saveAll(urlMappings);

        return urlMappings.getFirst().getOriginalUrl();
    }
}
