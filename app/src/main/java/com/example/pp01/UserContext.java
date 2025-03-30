package com.example.pp01;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URLEncoder;

public class UserContext {
    public static String Url = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/users";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface Callback {
        void onSuccess(boolean userExists);
        void onError(String error);
    }

    public static void checkUserCredentials(String username, String password, String role, Callback callback) {
        new CheckUserTask(username, password, role, callback).execute();
    }

    private static class CheckUserTask extends AsyncTask<Void, Void, String> {
        private String username;
        private String password;
        private String role;
        private Callback callback;
        private Exception exception;

        CheckUserTask(String username, String password, String role, Callback callback) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String encodedUsername = URLEncoder.encode(username, "UTF-8");
                String encodedPassword = URLEncoder.encode(password, "UTF-8");
                String urlWithParams = Url + "?username=eq." + encodedUsername + "&password=eq." + encodedPassword;

                Document doc = Jsoup.connect(urlWithParams)
                        .header("Authorization", Token)
                        .header("apikey", Secret)
                        .ignoreContentType(true)
                        .get();

                return doc.body().text();
            } catch (IOException e) {
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (exception != null) {
                callback.onError(exception.getMessage());
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                callback.onSuccess(jsonArray.length() > 0);
            } catch (JSONException e) {
                callback.onError("Invalid server response");
            }
        }
    }
}
