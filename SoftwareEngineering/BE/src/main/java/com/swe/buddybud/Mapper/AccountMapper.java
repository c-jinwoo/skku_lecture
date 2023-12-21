package com.swe.buddybud.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountMapper {
    Map<String, String> getUserInfo(Integer userId);

    List<Map<String, String>> getUserPostsList(Integer userId);

    List<Map<String, String>> getUserScrapsList(Integer userId);

    void updateUserInfo(Map<String, String> fields);
}
