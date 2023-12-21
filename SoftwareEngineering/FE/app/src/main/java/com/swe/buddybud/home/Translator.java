package com.swe.buddybud.home;

import android.os.AsyncTask;
import android.util.Log;

import com.swe.buddybud.user.LoginData;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {

    private final String clientId = "onlQfoFdsbh7csdsH84P"; // 클라이언트 아이디 값
    private final String clientSecret = "v_0_D1rGEo"; // 클라이언트 시크릿 값

    public interface TranslationCallback {
        void onTranslationDone(String translatedText);
        void onTranslationError(Exception e);
    }

    public String getUserLang() {
        String userLang = LoginData.getLoginUserLang();
        Log.e("logtemp","UserLang = "+userLang);
        if(userLang.equals("English")) return "en";
        if(userLang.equals("Korean")) return "ko";
        if(userLang.equals("Japanese")) return "ja";
        if(userLang.equals("Chinese")) return "zh-CH";
        return "en";
    }

    public void detectAndTranslate(final String sourceText, final TranslationCallback callback) {
        new AsyncTask<Void, Void, String>() {
            private Exception exception;

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // 1. 언어 감지
                    String userLang = getUserLang();
                    String detectedLanguage = detectLanguage(sourceText);
                    if (!userLang.equals(detectedLanguage)) {
                        // 2. 번역
                        return translate(sourceText, detectedLanguage, userLang);
                    } else {
                        return sourceText; // 이미 해당 언어인 경우 번역하지 않음
                    }
                } catch (Exception e) {
                    this.exception = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (exception != null) {
                    callback.onTranslationError(exception);
                } else {
                    callback.onTranslationDone(result);
                }
            }

            private String detectLanguage(String text) throws Exception {
                String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
                String query = URLEncoder.encode(text, "UTF-8");
                String postParams = "query=" + query;

                return postRequest(apiURL, postParams);
            }

            private String translate(String text, String sourceLang, String targetLang) throws Exception {
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                String postParams = "source=" + sourceLang + "&target=" + targetLang + "&text=" + URLEncoder.encode(text, "UTF-8");

                return postRequest(apiURL, postParams);
            }

            private String postRequest(String apiURL, String postParams) throws Exception {
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                con.setDoOutput(true);

                try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                    wr.writeBytes(postParams);
                    wr.flush();
                }

                int responseCode = con.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        responseCode == 200 ? con.getInputStream() : con.getErrorStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                if (apiURL.contains("detectLangs")) {
                    // 언어 감지 응답 처리
                    JSONObject responseObject = new JSONObject(response.toString());
                    return responseObject.getString("langCode");
                } else {
                    // 번역 응답 처리
                    JSONObject responseObject = new JSONObject(response.toString());
                    if (!responseObject.has("errorMessage")) {
                        return responseObject.getJSONObject("message")
                                .getJSONObject("result")
                                .getString("translatedText");
                    } else {
                        throw new Exception("Error in translation response: " + response);
                    }
                }
            }
        }.execute();
    }
}