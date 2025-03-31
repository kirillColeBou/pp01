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
import java.util.concurrent.TimeUnit;

public class PackContext {
    public static String Url = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/pack?select=*,categories(name),storehouse(name)";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface PacksCallback {
        void onSuccess(List<Pack> pack);
        void onError(String error);
    }

    public static void getPack(PacksCallback callback) {
        new GetPacksTask(callback).execute();
    }

    private static class GetPacksTask extends AsyncTask<Void, Void, String> {
        private PacksCallback callback;
        private Exception exception;

        GetPacksTask(PacksCallback callback) {
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
                JSONArray jsonArray = new JSONArray(result);
                List<Pack> packs = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Pack pack = new Pack(
                            obj.getString("id"),
                            obj.getString("name"),
                            obj.getInt("id_category"),
                            obj.getInt("count"),
                            obj.getInt("id_storehouse"),
                            obj.optString("QR", null));
                    if (obj.has("categories")) pack.setCategoryName(obj.getJSONObject("categories").getString("name"));
                    if (obj.has("storehouse")) pack.setWarehouseName(obj.getJSONObject("storehouse").getString("name"));
                    packs.add(pack);
                }
                callback.onSuccess(packs);
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }
}