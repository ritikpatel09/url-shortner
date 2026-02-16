package com.url.shortner.Controller;




import com.url.shortner.Config.BaseApiResponse;
import com.url.shortner.Dto.Request.UrlsRequestDto;
import com.url.shortner.Service.UrlsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlsService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<BaseApiResponse> shorten(
            @Valid @RequestBody UrlsRequestDto request) {
        BaseApiResponse dto = urlShortenerService.shortenUrl(request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("{shortCode}")
    public ResponseEntity<BaseApiResponse> redirect(@PathVariable String shortCode) {
        BaseApiResponse longUrl = urlShortenerService.getOriginalUrl(shortCode);
        return ResponseEntity.ok(longUrl);
    }
}