package com.swe.buddybud.board;

import com.google.gson.JsonObject;
import com.swe.buddybud.user.UserApiData;

import java.util.List;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BoardApiService {

    @GET("/board/all/{boardType}/{userId}")
    Call<List<Map<String, String>>> getBoardsList(@Path("boardType") String boardType, @Path("userId") Integer userId);

    @GET("/board/detail/{boardType}/{userId}/{boardId}")
    Call<JsonObject> getBoardDetails(@Path("boardType") String boardType, @Path("userId") Integer userId, @Path("boardId") Integer boardId);

    @PUT("/board/boardLike/{likeYN}/{userId}/{boardId}")
    Call<JsonObject> updateBoardLike(@Path("likeYN") String likeYN, @Path("userId") Integer userId, @Path("boardId") Integer boardId);

    @PUT("/board/commentLike/{likeYN}/{userId}/{commentId}")
    Call<JsonObject> updateCommentLike(@Path("likeYN") String likeYN, @Path("userId") int userId, @Path("commentId") int commentId);

    @PUT("/board/commentHate/{hateYN}/{userId}/{commentId}")
    Call<JsonObject> updateCommentHate(@Path("hateYN") String hateYN, @Path("userId") int userId, @Path("commentId") int commentId);

    @PUT("/board/scrap/{scrapYN}/{userId}/{boardId}")
    Call<JsonObject> updateScrap(@Path("scrapYN") String scrapYN, @Path("userId") Integer userId, @Path("boardId") Integer boardId);

    @POST("/board/comment")
    Call<JsonObject> insertComment(@Body RequestBody requestBody);

    @POST("/board")
    Call<BoardApiData> insertBoard(@Body RequestBody requestBody);

    @PUT("/board/{boardId}")
    Call<BoardApiData> updateBoard(@Path("boardId") Integer boardId, @Body RequestBody requestBody);

    @DELETE("/board/{boardId}")
    Call<BoardApiData> deleteBoard(@Path("boardId") Integer boardId);
}
