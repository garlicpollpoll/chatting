package com.capston.chatting.service.otp;

import de.taimos.totp.TOTP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TOTPTokenValidation {


    public boolean validate(String inputCode, String secretKey) {
        String code = getTOTPCode(secretKey);
        return code.equals(inputCode);
    }

    public String getTOTPCode(String secretKey) {
        Base32 base = new Base32();
        byte[] bytes = base.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
}
