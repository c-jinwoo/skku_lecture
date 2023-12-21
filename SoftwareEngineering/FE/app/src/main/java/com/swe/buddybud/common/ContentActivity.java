package com.swe.buddybud.common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.swe.buddybud.R;
import com.swe.buddybud.account.AccountFragment;
import com.swe.buddybud.board.BoardApiService;
import com.swe.buddybud.board.BoardDetailFragment;
import com.swe.buddybud.board.BoardFragment;
import com.swe.buddybud.home.HomeFragment;
import com.swe.buddybud.user.LoginData;
import com.swe.buddybud.willow.WillowFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContentActivity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        chipNavigationBar = findViewById(R.id.ChipNavigationBar);
        chipNavigationBar.setItemSelected(R.id.action_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, new HomeFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_board:
                        fragment = new BoardFragment();
                        break;
                    case R.id.action_willow:
                        fragment = new WillowFragment();
                        break;
                    case R.id.action_account:
                        fragment = new AccountFragment();
                        break;
                }
                // 현재 Fragment가 BoardDetailFragment이면 좋아요 상태 업데이트 및 백스택에서 제거
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
                if (currentFragment instanceof BoardDetailFragment) {
                    BoardDetailFragment boardDetailFragment = (BoardDetailFragment) currentFragment;
                    // 좋아요 상태가 변경되었는지 확인
                    if (boardDetailFragment.isLikeStatusChanged()) {
                        updateLikeStatus(boardDetailFragment.getBoardId(), boardDetailFragment.getIsLiked());
                    }
                    // 스크랩 상태가 변경되었는지 확인
                    if (boardDetailFragment.isScrapStatusChanged()) {
                        updateScrapStatus(boardDetailFragment.getBoardId(), boardDetailFragment.getIsScrap());
                    }
                    // 백스택에서 제거
                    getSupportFragmentManager().popBackStack();
                }
                // 새로운 Fragment로 교체
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout, fragment).commit();
            }
        });
    }

    // 좋아요 상태 업데이트 API 호출
    private void updateLikeStatus(int boardId, boolean isLiked) {
        String likeYN = isLiked ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateBoardLike(likeYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("ContentActivity", "Board like status updated successfully");
                } else {
                    Log.e("ContentActivity", "Server error occurred while updating board like status");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ContentActivity", "Network error: " + t.getMessage());
            }
        });
    }

    // 스크랩 상태 업데이트 API 호출
    private void updateScrapStatus(int boardId, boolean isScrap) {
        String scrapYN = isScrap ? "Y" : "N";
        int userId = LoginData.getLoginUserNo();

        BoardApiService service = RetrofitClient.getService(BoardApiService.class);
        Call<JsonObject> call = service.updateScrap(scrapYN, userId, boardId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("ContentActivity", "Board scrap status updated successfully");
                } else {
                    Log.e("ContentActivity", "Server error occurred while updating board scrap status");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ContentActivity", "Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (currentFragment instanceof BoardDetailFragment) {
            BoardDetailFragment boardDetailFragment = (BoardDetailFragment) currentFragment;
            // 좋아요 상태가 변경되었는지 확인
            if (boardDetailFragment.isLikeStatusChanged()) {
                updateLikeStatus(boardDetailFragment.getBoardId(), boardDetailFragment.getIsLiked());
            }
            // 스크랩 상태가 변경되었는지 확인
            if (boardDetailFragment.isScrapStatusChanged()) {
                updateScrapStatus(boardDetailFragment.getBoardId(), boardDetailFragment.getIsScrap());
            }
            // 백스택에서 제거
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}