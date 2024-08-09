package com.random.siddhu.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;

@RestController
public class JwtDecoderController {
	
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @GetMapping("/decode")
    public ResponseEntity<?> decodeJwt(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT Token");
        }

        String token = jwtToken.substring(7);

        String[] split_string = token.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        String body = new String(base64Url.decode(base64EncodedBody));

        JSONObject jsonBody = new JSONObject(body);
        String sub = jsonBody.getString("sub");
        long exp = jsonBody.getLong("exp");
        long iat = jsonBody.getLong("iat");


        LocalDateTime expDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(exp), ZoneId.of("Asia/Kolkata"));
        LocalDateTime iatDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(iat), ZoneId.of("Asia/Kolkata"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String expFormatted = expDateTime.format(formatter);
        String iatFormatted = iatDateTime.format(formatter);

        String response = String.format("sub: %s ,exp: %s, iat: %s", sub, expFormatted, iatFormatted);
		return ResponseEntity.ok(response);

    }
}
