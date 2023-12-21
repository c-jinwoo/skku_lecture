package com.swe.buddybud.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface AccountService {
    Map<String, String> getUserInfo(Integer userId);

    List<Map<String, String>> getUserPostsList(Integer userId);

    List<Map<String, String>> getUserScrapsList(Integer userId);

    void updateUserInfo(Map<String, String> fields);
}
