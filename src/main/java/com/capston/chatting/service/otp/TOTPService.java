package com.capston.chatting.service.otp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

@Service
@Slf4j
public class TOTPService {

    public HashMap<String, String> generate(String userName, String hostName) {
        HashMap<String, String> map = new HashMap<>();
        byte[] buffer = new byte[5 + 5 * 5];
        new Random().nextBytes(buffer);
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, 10);
        byte[] bEncodeKey = codec.encode(secretKey);

        String encodeKey = new String(bEncodeKey);
        String url = getQRBarcodeUrl(userName, hostName, encodeKey);

        map.put("encodeKey", encodeKey);
        map.put("url", url);

        return map;
    }

    public boolean checkCode(String userCode, String otpKey) {
        long otpNum = Integer.parseInt(userCode);   //Google Authenticator 앱에 표시되는 6자리 숫자
        long wave = new Date().getTime() / 30000;   //Google Authenticator 의 주기는 30초
        boolean result = false;

        try {
            Base32 codec = new Base32();
            byte[] decodeKey = codec.decode(otpKey);
            int window = 3;
            for (int i = -window; i <= window ; ++i) {
                long hash = verifyCode(decodeKey, wave + i);
                if (hash == otpNum) result = true;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("[InvalidKeyException] or [NoSuchAlgorithmException] : {}", e.getMessage());
        }
        return result;
    }

    private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

    public static String getQRBarcodeUrl(String user, String host, String secret) {
        // QR 코드 주소 생성
        String format = "http://chart.apis/google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&chld=H|0";
        return String.format(format, user, host, secret);
    }
}
