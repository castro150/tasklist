package com.example.app;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpRequest {
    OkHttpClient client = new OkHttpClient();

    public Call get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public Call uploadFile(String url, byte[] byteArray, Callback callback) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "android-file.png", RequestBody.create(MediaType.parse("image/png"), byteArray))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}
