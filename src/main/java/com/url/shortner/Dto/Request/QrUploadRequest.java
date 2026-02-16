package com.url.shortner.Dto.Request;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class QrUploadRequest {

    private String clientData;
    private String clientId;
    private String pathName;
    private String northVirginia;
    private byte[] qrBytes;
    private String shortCode;

    public QrUploadRequest(String clientData, String clientId, String pathName,
                           String northVirginia, byte[] qrBytes, String shortCode) {
        this.clientData = clientData;
        this.clientId = clientId;
        this.pathName = pathName;
        this.northVirginia = northVirginia;
        this.qrBytes = qrBytes;
        this.shortCode = shortCode;
    }

    /**
     * Converts this request into a MultiValueMap for RestTemplate
     */
    public MultiValueMap<String, Object> toMultiValueMap() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("clientData", clientData);
        body.add("clientId", clientId);
        body.add("pathName", pathName);
        body.add("northVirginia", northVirginia);

        // Attach QR image with filename
        body.add("list", new ByteArrayResource(qrBytes) {
            @Override
            public String getFilename() {
                return "qr_" + shortCode + ".png";
            }
        });

        return body;
    }
}