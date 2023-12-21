package com.swe.buddybud.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swe.buddybud.Service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping("/board/all/{boardType}/{userId}")
    public String getBoardsList(@PathVariable String boardType, @PathVariable Integer userId) {
        JsonArray jsonArray = new JsonArray();
        List<Map<String, String>> result = boardService.getBoardsList(boardType, userId);

        for (Map<String, String> board : result) {
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, String> entry : board.entrySet()) {
                jsonObject.addProperty(entry.getKey(), entry.getValue());
            }
            jsonArray.add(jsonObject);
        }

        return jsonArray.toString();
    }

    @GetMapping("/board/detail/{boardType}/{userId}/{boardId}")
    public String getBoard(@PathVariable String boardType, @PathVariable Integer userId, @PathVariable Integer boardId) {
        JsonObject jsonObject = new JsonObject();

        Map<String, String> result = boardService.getBoard(boardType, userId, boardId);

        if (boardType.equals("SNS")) {
            List<Map<String, String>> comments = boardService.getComment(boardId, userId);

            jsonObject.add("comments", new Gson().toJsonTree(comments));
        }

        for (Map.Entry<String, String> entry : result.entrySet()) {
            jsonObject.addProperty(entry.getKey(), entry.getValue());
        }
        return jsonObject.toString();
    }

    @PostMapping("/board")
    public String insertBoard(@RequestBody Map<String, String> fields) {
        JsonObject jsonObject = new JsonObject();
        boolean result = false;

        try {
            boardService.insertBoard(fields);
            result = true;
        }
        catch (Exception e) {
            result = false;
        }

        jsonObject.addProperty("insertBoardResult", result);

        return jsonObject.toString();
    }

    @PostMapping("/board/comment")
    public String insertComment(@RequestBody Map<String, String> fields) {
        JsonObject jsonObject = new JsonObject();
        boolean result = false;
        try {
            boardService.insertComment(fields);
            int seq = boardService.getLastInsertId();
            boardService.updateParentCommentNo(seq);
            result = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            result = false;
        }

        jsonObject.addProperty("insertCommentResult", result);

        return jsonObject.toString();
    }

    @PutMapping("/board/{boardId}")
    public String updateBoard(@PathVariable Integer boardId, @RequestBody Map<String, String> fields) {
        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        fields.put("boardId", boardId.toString());

        try {
            boardService.updateBoard(fields);
            result = true;
        }
        catch (Exception e) {
            result = false;
        }

        jsonObject.addProperty("updateBoardResult", result);

        return jsonObject.toString();
    }

    @PutMapping("/board/boardLike/{likeYN}/{userId}/{boardId}")
    public String updateBoardLike(@PathVariable String likeYN, @PathVariable Integer userId, @PathVariable Integer boardId) {
        JsonObject jsonObject = new JsonObject();
        String result = "failed";

        try {
            if (likeYN.equals("Y")) {
                boardService.insertBoardLike(boardId, userId);
                result = "insert success";
            } else if (likeYN.equals("N")) {
                boardService.deleteBoardLike(boardId, userId);
                result = "delete success";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "request failed";
        }

        jsonObject.addProperty("updateBoardLikeResult", result);

        return jsonObject.toString();
    }

    @PutMapping("/board/commentLike/{likeYN}/{userId}/{commentId}")
    public String updateCommentLike(@PathVariable String likeYN, @PathVariable Integer commentId, @PathVariable Integer userId) {
        JsonObject jsonObject = new JsonObject();

        String result = "failed";
        String tableType = "Like";

        try {
            if (likeYN.equals("Y")) {
                boardService.insertCommentLike(tableType, commentId, userId);
                result = "insert success";
            } else if (likeYN.equals("N")) {
                boardService.deleteCommentLike(tableType, commentId, userId);
                result = "delete success";
            }
        }
        catch (Exception e) {
            result = "request failed";
        }

        jsonObject.addProperty("updateCommentLikeResult", result);

        return jsonObject.toString();
    }

    @PutMapping("/board/commentHate/{hateYN}/{userId}/{commentId}")
    public String updateCommentHate(@PathVariable String hateYN, @PathVariable Integer commentId, @PathVariable Integer userId) {
        JsonObject jsonObject = new JsonObject();

        String result = "failed";
        String tableType = "Hate";

        try {
            if (hateYN.equals("Y")) {
                boardService.insertCommentLike(tableType, commentId, userId);
                result = "insert success";
            } else if (hateYN.equals("N")) {
                boardService.deleteCommentLike(tableType, commentId, userId);
                result = "delete success";
            }
        }
        catch (Exception e) {
            result = "request failed";
        }

        jsonObject.addProperty("updateCommentHateResult", result);

        return jsonObject.toString();
    }

    @PutMapping("/board/scrap/{scrapYN}/{userId}/{boardId}")
    public String updateScrap(@PathVariable String scrapYN, @PathVariable Integer userId, @PathVariable Integer boardId) {
        JsonObject jsonObject = new JsonObject();

        String result = "failed";

        try {
            if (scrapYN.equals("Y")) {
                boardService.insertScrap(boardId, userId);
                result = "insert success";
            } else if (scrapYN.equals("N")) {
                boardService.deleteScrap(boardId, userId);
                result = "delete success";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "request failed";
        }

        jsonObject.addProperty("updateScrapResult", result);

        return jsonObject.toString();
    }

    @DeleteMapping("/board/{boardId}")
    public String deleteBoard(@PathVariable Integer boardId) {
        JsonObject jsonObject = new JsonObject();

        boolean result = false;

        try {
            boardService.deleteBoard(boardId);
            result = true;
        }
        catch (Exception e) {
            result = false;
        }

        jsonObject.addProperty("deleteBoardResult", result);

        return jsonObject.toString();
    }
}