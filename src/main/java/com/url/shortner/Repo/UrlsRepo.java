package com.url.shortner.Repo;

import com.url.shortner.Entity.Urls;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlsRepo extends JpaRepository<Urls,Long> {


    Optional<Urls> findByOriginalUrl(String originalUrl);
    Optional<Urls> findByShortUrl(String shortUrl);



}
