package com.swe.buddybud.ServiceImpl;

import com.swe.buddybud.Mapper.AccountMapper;
import com.swe.buddybud.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;


    @Override
    public Map<String, String> getUserInfo(Integer userId) {
        return accountMapper.getUserInfo(userId);
    }

    @Override
    public List<Map<String, String>> getUserPostsList(Integer userId) {
        return accountMapper.getUserPostsList(userId);
    }

    @Override
    public List<Map<String, String>> getUserScrapsList(Integer userId) {
        return accountMapper.getUserScrapsList(userId);
    }

    @Override
    public void updateUserInfo(Map<String, String> fields) {
        accountMapper.updateUserInfo(fields);
    }
}
