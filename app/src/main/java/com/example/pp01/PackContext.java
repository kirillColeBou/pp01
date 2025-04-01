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

public class PackContext {
    public static String BASE_URL = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface PacksCallback {
        void onSuccess(List<Pack> packs);
        void onError(String error);
    }

    public static void getPack(PacksCallback callback) {
        new GetPacksTask(callback).execute(BASE_URL + "pack?select=*,categories(name),storehouse(name),product(name)");
    }

    public static void findPackById(String packId, PacksCallback callback) {
        String url = BASE_URL + "pack?select=*,categories(name),storehouse(name),product(name)&id_converted=eq." + packId;
        new GetPacksTask(callback).execute(url);
    }

    private static class GetPacksTask extends AsyncTask<String, Void, String> {
        private PacksCallback callback;
        private Exception exception;

        GetPacksTask(PacksCallback callback) {
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
                List<Pack> packs = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Pack pack = new Pack(
                            obj.getInt("id"),
                            obj.getString("name"),
                            obj.getInt("id_category"),
                            obj.getInt("count"),
                            obj.getInt("id_storehouse"),
                            obj.optString("QR", null),
                            obj.getInt("numeration_min"),
                            obj.getInt("numeration_max"),
                            obj.getInt("id_product"),
                            obj.getString("id_converted")
                    );
                    if (obj.has("categories")) pack.setCategoryName(obj.getJSONObject("categories").getString("name"));
                    if (obj.has("storehouse")) pack.setWarehouseName(obj.getJSONObject("storehouse").getString("name"));
                    if (obj.has("product")) pack.setProductName(obj.getJSONObject("product").getString("name"));
                    packs.add(pack);
                }
                callback.onSuccess(packs);
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }
}