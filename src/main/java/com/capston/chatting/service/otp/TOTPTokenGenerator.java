package com.capston.chatting.service.otp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

@Service
@Slf4j
public class TOTPTokenGenerator {

    private TOTPTokenGenerator() {

    }

    private static final String GOOGLE_URL = "https://www.google.com/chart?chs=200x200&chld=M10&cht=qr&chl=";

    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base = new Base32();
        return base.encodeToString(bytes);
    }

    public String getGoogleAuthenticatorBarcode(String secretKey, String account, String issuer) {
        try {
            log.info("URLEncoder.encode SecretKey : {}", URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20"));
            log.info("URLEncoder.encode issuer : {}", URLEncoder.encode(issuer, "UTF-8").replace("+", "%20"));
            String url =  GOOGLE_URL + "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
            log.info("URL : {}", url);
            return url;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
