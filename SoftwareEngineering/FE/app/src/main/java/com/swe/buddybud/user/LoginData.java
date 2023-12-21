package com.swe.buddybud.user;

public class LoginData {

    private static boolean loginRemember = false;    // Login 화면 Remember Me 체크여부

    private static String loginEmail = "";           // Login 화면 이메일 저장용

    private static Integer loginUserNo = 0;          // 로그인한 유저 번호 저장용

    private static String loginUserId = "";          // 로그인한 유저 ID 저장용

    private static String loginUserLang = "";        // 로그인한 유저 언어 저장용

    private static String loginUserGender = "";      // 로그인한 유저 성별 저장용

    public static boolean isLoginRemember() {
        return loginRemember;
    }

    public static void setLoginRemember(boolean loginRemember) {
        LoginData.loginRemember = loginRemember;
    }

    public static String getLoginEmail() {
        return loginEmail;
    }

    public static void setLoginEmail(String loginEmail) {
        LoginData.loginEmail = loginEmail;
    }

    public static Integer getLoginUserNo() {
        return loginUserNo;
    }

    public static void setLoginUserNo(Integer loginUserNo) {
        LoginData.loginUserNo = loginUserNo;
    }

    public static String getLoginUserId() {
        return loginUserId;
    }

    public static void setLoginUserId(String loginUserId) {
        LoginData.loginUserId = loginUserId;
    }

    public static String getLoginUserLang() {
        return loginUserLang;
    }

    public static void setLoginUserLang(String loginUserLang) {
        LoginData.loginUserLang = loginUserLang;
    }

    public static String getLoginUserGender() {
        return loginUserGender;
    }

    public static void setLoginUserGender(String loginUserGender) {
        LoginData.loginUserGender = loginUserGender;
    }
}