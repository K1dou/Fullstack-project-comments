package com.kidou.comments_api.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RedirectUtil {

    public static String encode(String uri) {
        return Base64.getUrlEncoder().encodeToString(uri.getBytes());
    }

    public static String decode(String encoded) {
        return new String(Base64.getUrlDecoder().decode(encoded), StandardCharsets.UTF_8);
    }


}
