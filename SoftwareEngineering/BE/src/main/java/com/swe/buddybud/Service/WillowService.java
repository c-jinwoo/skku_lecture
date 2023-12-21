package com.swe.buddybud.Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface WillowService {
    List<Map<String, Object>> getAllChat(Integer sender_no, Integer receiver_no);

    List<Map<String, Object>> getMyWillows(Integer user_no);

    void insertChat(Map<String, String> fields);

    int addChatRoom(Map<String, String> fields);

    void acceptWillow(Map<String, String> fields);

    void cancelAcceptWillow(Map<String, String> fields);

    int sendWillow(Map<String, String> fields);

    List<Map<String, String>> getSentWillowList(Integer userId);

    List<Map<String, String>> getReceivedWillowList(Integer userId);

    boolean deleteWillowRequest(Map<String, String> fields);
}
