package com.swe.buddybud.home;

import com.swe.buddybud.R;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static DataManager instance;
    private List<FeedData> feedList;
    
    // 초기 데이터 세팅
    private DataManager() {
        feedList = new ArrayList<>();
        List<CommentData> comment3 = new ArrayList<>();
        List<CommentData> comment2 = new ArrayList<>();
        List<CommentData> comment1 = new ArrayList<>();

//        comment3.add(new CommentData(1, R.drawable.logo_profile, "seven77", "23/11/14 17:34",
//                "오늘 기숙사 짐 빼는 날이잖아 ㅋㅋ", "Today is the day to unpack your dorm room haha!", 7, 0, 1,null));
//        comment3.add(new CommentData(2,R.drawable.profile_2, "Woosik12", "23/11/14 17:45",
//                "아 맞네 이걸 몰랐네;", "Oh right, I didn't know this;", 5, 1, 1,"seven77"));
//        comment3.add(new CommentData(3,R.drawable.logo_profile, "happy486", "23/11/14 17:46",
//                "君はばかだよ", "You're stupid", 0, 0, 1,"Woosik12"));
//        comment3.add(new CommentData(4,R.drawable.profile_2, "Woosik12", "23/11/14 17:47",
//                "ㅠㅠㅠ", "(crying)", 0, 0, 1,"happy486"));
//        comment3.add(new CommentData(5,R.drawable.profile_3, "s_k_you", "23/11/14 17:35",
//                "Where can I get the delivery box?", "Where can I get the delivery box?", 0, 0, 5,null));
//        comment3.add(new CommentData(6,R.drawable.logo_profile, "유저F", "23/11/14 17:36",
//                "多分郵便局に行かなければならないだろう", "You'll probably have to go to the post office.", 0, 0, 5,"s_k_you"));
//
//        comment2.add(new CommentData(1, R.drawable.profile_1, "스꾸", "23/11/14 17:34",
//                "저 관심 있어요!", "I'm interested!", 1, 0, 1,null));
//        comment2.add(new CommentData(2,R.drawable.logo_profile, "머라하지", "23/11/14 17:45",
//                "저도용 근데 라켓 없어도 되나요?", "Me too, but can I do it without a racket?", 0, 2, 1,"스꾸"));
//        comment2.add(new CommentData(3,R.drawable.logo_profile, "Joy", "23/11/14 17:50",
//                "as far as I know, Joa Tennis has racket rental service", "as far as I know, Joa Tennis has racket rental service", 5, 0, 1,"머라하지"));
//        comment2.add(new CommentData(4,R.drawable.logo_profile, "软锥", "23/11/14 17:41",
//                "这是我第一次打网球，可以吗？", "This is my first time playing tennis, is it okay?", 1, 1, 4,null));
//        comment2.add(new CommentData(5,R.drawable.logo_profile, "milililili", "23/11/14 17:41",
//                "当然！", "Certainly!", 2, 0, 4,"软锥"));
//
//        comment1.add(new CommentData(1,R.drawable.logo_profile, "软锥", "23/11/14 18:34",
//                "비밀 댓글입니다", "번역 test1", 15, 2, 1,null));
//
//        feedList.add(new FeedData(3, R.drawable.profile_2, "Woosik12", "23/11/14 17:18", "오늘 신관 앞에 사람 많은데",
//                "무슨 일임?","There's a crowd in front of shin-gwan", "What's going on?",  13, 6, comment3, null));
//        feedList.add(new FeedData(2, R.drawable.mililili, "mililili", "23/11/14 17:13", "Anyone who wants to play tennis?",
//                "any tennis newbies around? I'm looking to hit the court around campus. \uD83D\uDE06 DM me if you're up for some smashing fun \uD83D\uDE4C",
//                "Anyone who wants to play tennis?", "any tennis newbies around? I'm looking to hit the court around campus. \uD83D\uDE06 DM me if you're up for some smashing fun \uD83D\uDE4C", 16, 5, comment2, null));
//        feedList.add(new FeedData(1, R.drawable.profile_frog, "Frogy123", "23/11/14 16:33", "韓国料理のおすすめ",
//                "韓国に来たばかりでしたが、トッポッキは食べてみて、もう別の食べ物も挑戦してみたいです！恵化駅の近くで食べるのに大丈夫な韓国料理店がありますか？",
//                "Korean food recommendation", "I just came to Korea, but after trying Tteokbokki, I want to try other foods! Is there a good Korean restaurant to eat near Hyehwa Station?", 27, 1, comment1, null));
    }
    
    public static synchronized DataManager getInstance() {
        if(instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public List<FeedData> getFeedList() {
        return feedList;
    }

    // 피드 추가 - 게시글 작성 부분에서 사용
    public void addFeed(FeedData feed) {
        feedList.add(0, feed); // 리스트의 시작 부분에 피드 추가
    }

    // 댓글 위치 찾기
    private int findInsertIndexForReply(List<CommentData> comments, int replyToCommentId) {
        for (int i = comments.size() - 1; i >= 0; i--) {
            CommentData currentComment = comments.get(i);
            if (currentComment.getCommentId() == replyToCommentId || (currentComment.getReplyToCommentId() != 0 && currentComment.getReplyToCommentId() == replyToCommentId)) {
                return i + 1;
            }
        }
        return comments.size();
    }

    // 피드 댓글 달았을 경우
    public void updateFeedData(FeedData updatedFeed) {
        for (int i = 0; i < feedList.size(); i++) {
            if (feedList.get(i).getId() == updatedFeed.getId()) {
                feedList.set(i, updatedFeed);
                break;
            }
        }
    }

    // 검색 필터링
    public List<FeedData> filterFeeds(String query) {
        if (query == null || query.isEmpty()) {
            return feedList;
        }

        List<FeedData> filteredList = new ArrayList<>();
        for (FeedData feed : feedList) {
            if (feed.getTitle().toLowerCase().contains(query.toLowerCase()) || feed.getContent().toLowerCase().contains(query.toLowerCase()) ) {
                filteredList.add(feed);
            }
        }
        return filteredList;
    }
}
