package com.swe.buddybud.Mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    List<Map<String, String>> getBoardsList(String boardType, Integer userId);

    Map<String, String> getBoard(String boardType, Integer userId, Integer boardId);

    void insertBoard(Map<String, String> fields);

    void updateBoard(Map<String, String> fields);

    void deleteBoard(Integer boardId);

    List<Map<String, String>> getComment(Integer boardId, Integer userId);

    void insertComment(Map<String, String> fields);

    int getLastInsertId();

    void updateParentCommentNo(Integer seq);

    void deleteBoardLike(Integer boardId, Integer userId);

    void insertBoardLike(Integer boardId, Integer userId);

    void deleteCommentLike(String tableType, Integer commentId, Integer userId);

    void insertCommentLike(String tableType, Integer commentId, Integer userId);

    void deleteScrap(Integer boardId, Integer userId);

    void insertScrap(Integer boardId, Integer userId);
}