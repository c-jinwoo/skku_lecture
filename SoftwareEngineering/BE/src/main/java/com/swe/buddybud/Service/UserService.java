package com.swe.buddybud.Service;

import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public interface UserService {

    Map<String, Object> userLogin(Map<String, String> fields);

    boolean userEmailDuplCheck(String email);

    boolean userIdDuplCheck(String id);

    void insertUserInfo(Map<String, String> fields);

    void updateUserInfo(Map<String, String> fields);
}
