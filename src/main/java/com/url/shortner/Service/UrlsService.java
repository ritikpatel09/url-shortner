package com.url.shortner.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.url.shortner.Config.BaseApiResponse;
import com.url.shortner.Dto.Request.QrUploadRequest;
import com.url.shortner.Dto.Request.UrlsRequestDto;
import com.url.shortner.Dto.Response.UrlsResponseDto;
import com.url.shortner.Entity.Urls;
import com.url.shortner.Repo.UrlsRepo;
import com.url.shortner.Util.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UrlsService {

    private final UrlsRepo urlsRepo;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${upload.client-data}")
    private String clientData;

    @Value("${upload.client-id}")
    private String clientId;

    @Value("${upload.path-name}")
    private String pathName;

    @Value("${upload.north-virginia}")
    private String northVirginia;

    @Value("${upload.api-url}")
    private String uploadApi;

    @Value("${short.url.base}")
    private String SHORT_URL_BASE;

    @Value("${upload.api.url}")
    private String UPLOAD_API;


    public BaseApiResponse shortenUrl(UrlsRequestDto request) {

        if (!isValidUrl(request.getUrl())) {
            return ApiResponseHelper.failure("URL is not correct");
        }

        Optional<Urls> existingOpt = urlsRepo.findByOriginalUrl(request.getUrl());

        //  URL already exists
        if (existingOpt.isPresent()) {
            Urls existing = existingOpt.get();

            if (request.getCheck() == 2 && existing.getImageUrl() == null) {
                String imageUrl = generateAndUploadQr(existing.getShortUrl());
                existing.setImageUrl(imageUrl);
                urlsRepo.save(existing);
            }

            UrlsResponseDto dto = new UrlsResponseDto(
                    existing.getShortUrl(),
                    existing.getImageUrl()
            );

            return ApiResponseHelper.success(dto, "URL shortened successfully");
        }

        //  New URL
        String shortCode = generateShortCode();
        String shortUrl = shortCode;

        String imageUrl = null;
        if (request.getCheck() == 2) {
            imageUrl = generateAndUploadQr(shortUrl);
        }

        Urls entity = new Urls();
        entity.setOriginalUrl(request.getUrl());
        entity.setShortUrl(shortUrl);
        entity.setImageUrl(imageUrl);

        urlsRepo.save(entity);

        UrlsResponseDto dto = new UrlsResponseDto(SHORT_URL_BASE+shortUrl, imageUrl);

        return ApiResponseHelper.success(dto, "URL shortened successfully");
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            return uri.getScheme() != null && uri.getHost() != null;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateAndUploadQr(String shortUrl) {
        byte[] qrBytes = generateQrBytes(shortUrl);
        return uploadQrBytes(qrBytes, extractCode(shortUrl));
    }

    private byte[] generateQrBytes(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }

    public String uploadQrBytes(byte[] qrBytes, String shortCode) {
        QrUploadRequest request = new QrUploadRequest(
                clientData,
                clientId,
                pathName,
                northVirginia,
                qrBytes,
                shortCode
        );

        MultiValueMap<String, Object> body = request.toMultiValueMap();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

        // Using RestClient to call the external API
        String response = restClient
                .post()
                .uri(UPLOAD_API)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(String.class);

        return extractImageUrl(response);
    }


    private String extractImageUrl(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            return root.path("data").path("list").get(0).asText();
        } catch (Exception e) {
            return "Failed to parse upload response";
        }
    }

    private String generateShortCode() {
        String shortCode;
        boolean exists;

        do {

            shortCode = UUID.randomUUID().toString().substring(0, 8);

            // Check if this shortcode already exists in the DB
            exists = urlsRepo.findByShortUrl(shortCode).isPresent();

        } while (exists); // Repeat until unique

        return shortCode;
    }


    private String extractCode(String shortUrl) {
        return shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
    }



    //Get API
    public BaseApiResponse getOriginalUrl(String shortCode) {

        String shortUrl = shortCode;

        Optional<Urls> urlOpt = urlsRepo.findByShortUrl(shortUrl);
        if(urlOpt.isEmpty()){
             return ApiResponseHelper.failure(
                    "Short URL not found",
                    SHORT_URL_BASE+shortUrl);
        }

         Urls url = urlOpt.get();

        return ApiResponseHelper.success(
                url.getOriginalUrl(),
                "Original URL fetched successfully"
        );
    }
}
