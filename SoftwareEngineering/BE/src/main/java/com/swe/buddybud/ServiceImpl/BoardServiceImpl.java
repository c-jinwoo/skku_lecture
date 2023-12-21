package com.swe.buddybud.ServiceImpl;

import com.swe.buddybud.Mapper.BoardMapper;
import com.swe.buddybud.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardMapper boardMapper;

    @Override
    public List<Map<String, String>> getBoardsList(String boardType, Integer userId) {
        return boardMapper.getBoardsList(boardType, userId);
    }

    @Override
    public Map<String, String> getBoard(String boardType, Integer userId, Integer boardId) {
        return boardMapper.getBoard(boardType, userId, boardId);
    }

    @Override
    public void insertBoard(Map<String, String> fields) {
        boardMapper.insertBoard(fields);
    }

    @Override
    public void updateBoard(Map<String, String> fields) {
        boardMapper.updateBoard(fields);
    }

    @Override
    public void deleteBoard(Integer boardId) {
        boardMapper.deleteBoard(boardId);
    }

    @Override
    public List<Map<String, String>> getComment(Integer boardId, Integer userId) {
        return boardMapper.getComment(boardId, userId);
    }

    @Override
    public void insertComment(Map<String, String> fields) {
        boardMapper.insertComment(fields);
    }

    @Override
    public int getLastInsertId() {
        return boardMapper.getLastInsertId();
    }

    @Override
    public void updateParentCommentNo(Integer seq) {
        boardMapper.updateParentCommentNo(seq);
    }
    @Override
    public void deleteBoardLike(Integer boardId, Integer userId) {
        boardMapper.deleteBoardLike(boardId, userId);
    }

    @Override
    public void insertBoardLike(Integer boardId, Integer userId) {
        boardMapper.insertBoardLike(boardId, userId);
    }

    @Override
    public void deleteCommentLike(String tableType, Integer commentId, Integer userId) {
        boardMapper.deleteCommentLike(tableType, commentId, userId);
    }

    @Override
    public void insertCommentLike(String tableType, Integer commentId, Integer userId) {
        boardMapper.insertCommentLike(tableType, commentId, userId);
    }

    @Override
    public void deleteScrap(Integer boardId, Integer userId) {
        boardMapper.deleteScrap(boardId, userId);
    }

    @Override
    public void insertScrap(Integer boardId, Integer userId) {
        boardMapper.insertScrap(boardId, userId);
    }
}