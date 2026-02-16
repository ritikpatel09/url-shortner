# 🚀 URL Shortener with QR Code Generation

A production-grade Spring Boot application that shortens long URLs and optionally generates a QR code.  

If `check == 2`, the application:
- Generates a QR code for the short URL
- Uploads the QR image to an external storage API
- Returns the public QR image URL in the response

---

## 📌 Features

- 🔗 Shorten long URLs
- 🔁 Redirect short URL to original URL
- 🧾 Optional QR code generation
- ☁ Upload QR image to external API (S3 / Storage Service)
- 💾 Database persistence
- 🧱 Clean layered architecture
- ⚡ Production-ready structure
- 🛡 Exception handling
- 📊 Extendable for analytics & tracking

---

## 🏗 System Architecture

url-shortner/
│
├── controller/
│ └── UrlController.java
│
├── service/
│ └── UrlService.java
│
├── repository/
│ └── UrlRepository.java
│
├── entity/
│ └── UrlMapping.java
│
├── dto/
│ ├── UrlRequest.java
│ └── UrlResponse.java
│
└── application.properties

