package com.swe.buddybud.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swe.buddybud.Service.UserService;
import com.swe.buddybud.Mapper.UserMapper;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public Map<String, Object> userLogin(Map<String, String> fields){

        Map<String, Object> loginInfo = userMapper.userLogin(fields);
        
        // 유저 정보가 있는 경우
        if(loginInfo != null){
            String loginPW = fields.get("password");
            String dbPW = loginInfo.get("pw").toString();
            
            // 비번 맞은 경우
            if (loginPW.equals(dbPW)) {
                loginInfo.put("isUserLoginSucceed", true);
            }
            // 비번 틀린 경우
            else {
                loginInfo.put("isUserLoginSucceed", false);
            }
        }

        return loginInfo;
    }

    @Override
    public boolean userEmailDuplCheck(String email){
        int selected = userMapper.userEmailDuplCheck(email);

        return selected > 0;
    }

    @Override
    public boolean userIdDuplCheck(String id){
        int selected = userMapper.userIdDuplCheck(id);

        return selected > 0;
    }

    @Override
    public void insertUserInfo(Map<String, String> fields){
        userMapper.insertUserInfo(fields);
    }

    @Override
    public void updateUserInfo(Map<String, String> fields){
        userMapper.insertUserInfo(fields);
    }
}
