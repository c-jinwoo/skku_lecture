package com.swe.buddybud.Controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swe.buddybud.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/account/{userId}")
    public String getUserInfo(@PathVariable Integer userId) {
        JsonObject jsonObject = new JsonObject();

        Map<String, String> result = accountService.getUserInfo(userId);

        for (Map.Entry<String, String> entry : result.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }

        return jsonObject.toString();
    }

    @GetMapping("/account/{userId}/post")
    public String getUserPostsList(@PathVariable Integer userId) {
        JsonArray jsonArray = new JsonArray();
        List<Map<String, String>> result = accountService.getUserPostsList(userId);

        for (Map<String, String> post : result) {
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, String> entry : post.entrySet()) {
                jsonObject.addProperty(entry.getKey(), entry.getValue());
            }
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @GetMapping("/account/{userId}/scrap")
    public String getUserScrapsList(@PathVariable Integer userId) {
        JsonArray jsonArray = new JsonArray();
        List<Map<String, String>> result = accountService.getUserScrapsList(userId);

        for (Map<String, String> scrap : result) {
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, String> entry : scrap.entrySet()) {
                jsonObject.addProperty(entry.getKey(), entry.getValue());
            }
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @PutMapping("/account/{userId}")
    public String updateUserInfo(@PathVariable Integer userId, @RequestBody Map<String, String> fields) {
        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        fields.put("userId", userId.toString());

        try {
            accountService.updateUserInfo(fields);
            result = true;
        }
        catch (Exception e) {
            result = false;
        }

        jsonObject.addProperty("updateUserInfoResult", result);

        return jsonObject.toString();
    }
}
