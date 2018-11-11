package com.example.android.intouch_android.utils;

import java.util.HashMap;
import java.util.UUID;

public class AuthLoginPageParamsBuilder {
    HashMap<String, Object> params;

    public AuthLoginPageParamsBuilder() {
        params = new HashMap<>();
    }

    public AuthLoginPageParamsBuilder disableLogin() {
        params.put("allowLogin", false);
        return this;
    }

    public AuthLoginPageParamsBuilder prefillWithPlaceholderEmail() {
        params.put(
                "emailHint",
                UUID.randomUUID().toString().substring(0, 10) + "@intouch.gmail.com"
        );
        return this;
    }

    public AuthLoginPageParamsBuilder setEmailInstructionsOnSignup(String instructions) {
        params.put("databaseSignUpInstructions", instructions);
        return this;
    }

    public HashMap<String, Object> build() { return params; }
}
