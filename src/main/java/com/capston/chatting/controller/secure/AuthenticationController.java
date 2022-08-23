package com.capston.chatting.controller.secure;

import com.capston.chatting.dto.member.AuthenticationCodeDto;
import com.capston.chatting.dto.member.SecretKeyAndBarcodeUrlDto;
import com.capston.chatting.service.otp.TOTPService;
import com.capston.chatting.service.otp.TOTPTokenGenerator;
import com.capston.chatting.service.otp.TOTPTokenValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final TOTPTokenValidation tokenValidation;
    private final TOTPTokenGenerator tokenGenerator;

    private final TOTPService totpService;

    @GetMapping("/2FA_authentication")
    public String FAAuthentication(Model model) {
        SecretKeyAndBarcodeUrlDto dto = generateSecurityKey();

        model.addAttribute("security", dto.getBarcodeUrl());
        model.addAttribute("secretKey", dto.getSecretKey());

//        HashMap<String, String> map = totpService.generate("name", "host");
//        String otpKey = map.get("encodeKey");
//        String url = map.get("url");
//
//        model.addAttribute("security", url);
//        model.addAttribute("secretKey", otpKey);

        return "fa_authentication/fa_authentication";
    }

    @PostMapping("/2FA_authentication")
    public String FAAuthenticationPost(@ModelAttribute("code")AuthenticationCodeDto dto, HttpSession session) {
        boolean isValidate = validAuthenticatorCode(dto.getCode(), dto.getSecretKey());

        if (isValidate) {
            session.setAttribute("isAuthentication", "OK");
            return "redirect:/";
        }
        else {
            return "redirect:/2FA_authentication";
        }

//        boolean check = totpService.checkCode(dto.getCode(), dto.getSecretKey());
//
//        if (check) {
//            return "redirect:/";
//        }
//        else {
//            return "redirect:/2FA_authentication";
//        }
    }

    private boolean validAuthenticatorCode(String code, String secretKey) {
        if (tokenValidation.validate(code, secretKey)) {
            log.info("Logged in successfully");
            return true;
        }
        else {
            log.error("Invalid 2FA code");
            return false;
        }
    }

    private SecretKeyAndBarcodeUrlDto generateSecurityKey() {
        SecretKeyAndBarcodeUrlDto dto = new SecretKeyAndBarcodeUrlDto();

        String secretKey = tokenGenerator.generateSecretKey();
        log.info("SecretKey : {}", secretKey);

        String account = "otptest@google.com";
        String issuer = "otpTest";
        String barcodeUrl = tokenGenerator.getGoogleAuthenticatorBarcode(secretKey, account, issuer);
        log.info(barcodeUrl);

        dto.setSecretKey(secretKey);
        dto.setBarcodeUrl(barcodeUrl);

        return dto;
    }
}
