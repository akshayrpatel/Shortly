package com.url.shortener.repository;

import com.url.shortener.model.UrlMapping;
import com.url.shortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    List<UrlMapping> findByShortUrl(String shortUrl);
    UrlMapping findByShortUrlAndUser_Username(String shortUrl, String username);
    List<UrlMapping> findByUser(User user);
}
