package com.Jas.shop_backend.api.helper;

import java.util.Random;

public class VerificationCodeGenerator {

    public static String generateCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(900000);
        return String.valueOf(code);
    }

}
