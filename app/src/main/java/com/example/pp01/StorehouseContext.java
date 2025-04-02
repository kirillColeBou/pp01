package com.example.pp01;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorehouseContext {
    public static String BASE_URL = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface StorehousesCallback {
        void onSuccess(List<Storehouse> storehouses);
        void onError(String error);
    }

    public static void getStorehouses(StorehousesCallback callback) {
        new GetStorehousesTask(callback).execute(BASE_URL + "storehouse");
    }

    private static class GetStorehousesTask extends AsyncTask<String, Void, String> {
        private StorehousesCallback callback;
        private Exception exception;

        GetStorehousesTask(StorehousesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                Document doc = Jsoup.connect(urls[0])
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
                List<Storehouse> storehouses = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Storehouse storehouse = new Storehouse(
                            obj.getInt("id"),
                            obj.getString("name")
                    );
                    storehouses.add(storehouse);
                }
                callback.onSuccess(storehouses);
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }
}