package com.example.pp01;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CategoryContext {
    public static String Url = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/categories?select=*";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface CategoriesCallback {
        void onSuccess(JSONArray categories);
        void onError(String error);
    }

    public static void getCategories(CategoriesCallback callback) {
        new GetCategoriesTask(callback).execute();
    }

    private static class GetCategoriesTask extends AsyncTask<Void, Void, String> {
        private CategoriesCallback callback;
        private Exception exception;

        GetCategoriesTask(CategoriesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(Url)
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
                callback.onSuccess(new JSONArray(result));
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }
}