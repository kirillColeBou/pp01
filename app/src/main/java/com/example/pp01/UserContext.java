package com.example.pp01;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class UserContext {
    private static final String URL = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/users";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    private static final String SECRET = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface Callback {
        void onSuccess(boolean userExists);
        void onError(String error);
    }

    public static void checkUserCredentials(String username, String password, Callback callback) {
        new CheckUserTask(username, password, callback).execute();
    }

    private static class CheckUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String username;
        private final String password;
        private final Callback callback;
        private String error;

        CheckUserTask(String username, String password, Callback callback) {
            this.username = username;
            this.password = password;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                String url = URL + "?username=eq." + URLEncoder.encode(username, "UTF-8") +
                        "&password=eq." + URLEncoder.encode(password, "UTF-8");

                Document doc = Jsoup.connect(url)
                        .header("Authorization", TOKEN)
                        .header("apikey", SECRET)
                        .ignoreContentType(true)
                        .get();

                return new JSONArray(doc.body().text()).length() > 0;
            } catch (Exception e) {
                error = e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean userExists) {
            if (error != null) {
                callback.onError(error);
            } else {
                callback.onSuccess(userExists);
            }
        }
    }
}