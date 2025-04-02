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
    private static final String BASE_URL = "https://nexmefaydmsqibfspxxg.supabase.co/rest/v1/";
    public static String Token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0MzAxMjc3OCwiZXhwIjoyMDU4NTg4Nzc4fQ.KkthFQJSVMlHSGTZKpdYQjQL-O_VOm9oG3EH2k2dr8U";
    public static String Secret = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5leG1lZmF5ZG1zcWliZnNweHhnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDMwMTI3NzgsImV4cCI6MjA1ODU4ODc3OH0.08L6ocU5pqpCT0rzuH0USw886LyNB4x15JSsQaH5fQU";

    public interface StorehousesCallback {
        void onSuccess(List<Storehouse> storehouses);
        void onError(String error);
    }

    public interface StorehouseOperationCallback {
        void onSuccess(Storehouse storehouse);
        void onError(String error);
    }

    public interface OperationCallback {
        void onSuccess();
        void onError(String error);
    }

    public static void getStorehouses(StorehousesCallback callback) {
        new StorehouseTask(callback).execute(BASE_URL + "storehouse", "GET", null);
    }

    public static void createStorehouse(String name, StorehouseOperationCallback callback) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            new StorehouseOperationTask(callback).execute(BASE_URL + "storehouse", "POST", json.toString());
        } catch (JSONException e) {
            callback.onError("Ошибка создания данных");
        }
    }

    public static void updateStorehouse(int id, String name, StorehouseOperationCallback callback) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            new StorehouseOperationTask(callback).execute(BASE_URL + "storehouse?id=eq." + id, "PATCH", json.toString());
        } catch (JSONException e) {
            callback.onError("Ошибка обновления данных");
        }
    }

    public static void deleteStorehouse(int id, OperationCallback callback) {
        new DeleteStorehouseTask(callback).execute(BASE_URL + "storehouse?id=eq." + id, "DELETE", null);
    }

    private static class StorehouseTask extends AsyncTask<String, Void, String> {
        private StorehousesCallback callback;
        private Exception exception;

        StorehouseTask(StorehousesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(params[0])
                        .header("Authorization", Token)
                        .header("apikey", Secret)
                        .header("Content-Type", "application/json")
                        .header("Prefer", "return=representation")
                        .ignoreContentType(true)
                        .method(org.jsoup.Connection.Method.valueOf(params[1]))
                        .requestBody(params[2])
                        .execute()
                        .parse();
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
                    storehouses.add(new Storehouse(
                            obj.getInt("id"),
                            obj.getString("name")
                    ));
                }
                callback.onSuccess(storehouses);
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }

    private static class StorehouseOperationTask extends AsyncTask<String, Void, String> {
        private StorehouseOperationCallback callback;
        private Exception exception;

        StorehouseOperationTask(StorehouseOperationCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Document doc = Jsoup.connect(params[0])
                        .header("Authorization", Token)
                        .header("apikey", Secret)
                        .header("Content-Type", "application/json")
                        .header("Prefer", "return=representation")
                        .ignoreContentType(true)
                        .method(org.jsoup.Connection.Method.valueOf(params[1]))
                        .requestBody(params[2])
                        .execute()
                        .parse();
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
                JSONObject obj = jsonArray.getJSONObject(0);
                callback.onSuccess(new Storehouse(
                        obj.getInt("id"),
                        obj.getString("name")
                ));
            } catch (JSONException e) {
                callback.onError("Ошибка обработки данных");
            }
        }
    }

    private static class DeleteStorehouseTask extends AsyncTask<String, Void, Boolean> {
        private OperationCallback callback;
        private Exception exception;

        DeleteStorehouseTask(OperationCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Jsoup.connect(params[0])
                        .header("Authorization", Token)
                        .header("apikey", Secret)
                        .header("Content-Type", "application/json")
                        .ignoreContentType(true)
                        .method(org.jsoup.Connection.Method.valueOf(params[1]))
                        .execute();
                return true;
            } catch (IOException e) {
                exception = e;
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (exception != null) {
                callback.onError(exception.getMessage());
            } else if (success) {
                callback.onSuccess();
            } else {
                callback.onError("Ошибка удаления");
            }
        }
    }
}