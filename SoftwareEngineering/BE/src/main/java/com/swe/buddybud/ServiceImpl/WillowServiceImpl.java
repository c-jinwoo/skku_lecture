package com.swe.buddybud.ServiceImpl;

import com.swe.buddybud.Service.WillowService;
import com.swe.buddybud.Mapper.WillowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WillowServiceImpl implements WillowService {

    @Autowired
    WillowMapper willowMapper;

    @Override
    public int sendWillow(Map<String, String> fields) {
        return willowMapper.sendWillow(fields);
    }

    @Override
    public List<Map<String, String>> getSentWillowList(Integer userId) {
        return willowMapper.getSentWillowList(userId);
    }

    @Override
    public List<Map<String, String>> getReceivedWillowList(Integer userId) {
        return willowMapper.getReceivedWillowList(userId);
    }

    @Override
    public List<Map<String, Object>> getAllChat(Integer sender_no, Integer receiver_no) {
        return willowMapper.getAllChat(sender_no, receiver_no);
    }

    @Override
    public List<Map<String, Object>> getMyWillows(Integer user_no) {
        return willowMapper.getMyWillows(user_no);
    }

    @Override
    public void insertChat(Map<String, String> fields) {willowMapper.insertChat(fields);
    }

    @Override
    public int addChatRoom(Map<String, String> fields) {
        return willowMapper.addChatRoom(fields);
    }

    @Override
    public void acceptWillow(Map<String, String> fields) {
        willowMapper.acceptWillow(fields);
    }

    @Override
    public void cancelAcceptWillow(Map<String, String> fields) {
        willowMapper.cancelAcceptWillow(fields);
    }

    @Override
    public boolean deleteWillowRequest(Map<String, String> fields) {
        int rowsAffected = willowMapper.deleteWillowRequest(fields);
        return rowsAffected > 0;
    }
}
