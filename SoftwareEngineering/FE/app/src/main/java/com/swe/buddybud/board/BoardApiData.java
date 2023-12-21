package com.swe.buddybud.board;

import com.google.gson.annotations.SerializedName;

public class BoardApiData {

    @SerializedName("insertBoardResult")
    private boolean insertBoardResult;

    @SerializedName("updateBoardResult")
    private boolean updateBoardResult;

    @SerializedName("deleteBoardResult")
    private boolean deleteBoardResult;

    // 게시글 저장 성공여부
    public boolean getInsertBoardResult(){ return insertBoardResult; }

    // 게시글 수정 성공여부
    public boolean getUpdateBoardResult(){ return updateBoardResult; }

    // 게시글 삭제 성공여부
    public boolean getDeleteBoardResult(){ return deleteBoardResult; }
}
