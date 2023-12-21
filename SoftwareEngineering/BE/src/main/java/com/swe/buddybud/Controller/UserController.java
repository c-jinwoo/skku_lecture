package com.swe.buddybud.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.*;

import com.swe.buddybud.Service.UserService;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public String userLogin(@RequestBody Map<String, String> fields){
        JsonObject jsonObject = new JsonObject();
        Map<String, Object> loginInfo = userService.userLogin(fields);

        if(loginInfo == null){
            jsonObject.addProperty("isUserLoginSucceed", false);
        }
        else{
            jsonObject.addProperty("isUserLoginSucceed", (Boolean) loginInfo.get("isUserLoginSucceed"));
            jsonObject.addProperty("loginUserNo", (Integer) loginInfo.get("seq"));
            jsonObject.addProperty("loginUserId", (String) loginInfo.get("id"));
            jsonObject.addProperty("loginUserLang", (String) loginInfo.get("lang"));
            jsonObject.addProperty("loginUserGender", (String) loginInfo.get("gender"));
        }

        return jsonObject.toString();
    }

    /* 회원가입 : 첫 번째 화면 이메일 중복 검사 */
    @GetMapping("/user/emailDuplCheck")
    public String userEmailDuplCheck(String email){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("isUserEmailDupl", userService.userEmailDuplCheck(email));

        return jsonObject.toString();
    }

    /* 회원가입 : 두 번째 화면 ID 중복 검사 */
    @GetMapping("/user/idDuplCheck")
    public String userIdDuplCheck(String id){
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("isUserIdDupl", userService.userIdDuplCheck(id));

        return jsonObject.toString();
    }

    @PostMapping("/user/insertUserInfo")
    public String insertUserInfo(@RequestBody Map<String, String> fields){
        JsonObject jsonObject = new JsonObject();
        boolean result = false;

        try{
            userService.insertUserInfo(fields);
            result = true;
        }
        catch(Exception e){
            result = false;
        }

        jsonObject.addProperty("inserUserInfoResult", result);

        return jsonObject.toString();
    }

    @PostMapping("/user/updateUserInfo")
    public String updateUserInfo(@RequestBody Map<String, String> fields){
        JsonObject jsonObject = new JsonObject();
        boolean result = false;

        try{
            userService.updateUserInfo(fields);
            result = true;
        }
        catch(Exception e){
            result = false;
        }

        jsonObject.addProperty("updateUserInfoResult", result);

        return jsonObject.toString();
    }
}
