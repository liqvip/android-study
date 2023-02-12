package cn.blogss.frame.okhttp;

import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPT {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    // Moshi
    private final Moshi moshi = new Moshi.Builder().build();
    private static String API_KEY = "";

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .build();

    public void completion(String prompt) throws IOException {
        CompletionRequest completionRequest = new CompletionRequest();
        completionRequest.setPrompt(prompt);

        String reqJson = moshi.adapter(CompletionRequest.class).toJson(completionRequest);
        System.out.println("reqJson: " + reqJson);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(MEDIA_TYPE_JSON, reqJson))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }

    public void imageGeneration(String prompt) throws IOException {
        ImageGenerations imageGenerations = new ImageGenerations();
        imageGenerations.setPrompt(prompt);

        String reqJson = moshi.adapter(ImageGenerations.class).toJson(imageGenerations);
        System.out.println("reqJson: " + reqJson);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .header("Authorization", "Bearer " + API_KEY)
                .post(RequestBody.create(MEDIA_TYPE_JSON, reqJson))
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }

    private static class CompletionRequest {
        private String model = "text-davinci-003";
        private String prompt;
        private Integer max_tokens = 256;
        private float temperature = 0.5f;
        private Integer top_p = 1;
        private Integer n = 1;
        private Boolean stream = false;
        private Boolean logprobs;
        private String stop;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public Integer getMax_tokens() {
            return max_tokens;
        }

        public void setMax_tokens(Integer max_tokens) {
            this.max_tokens = max_tokens;
        }

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public Integer getTop_p() {
            return top_p;
        }

        public void setTop_p(Integer top_p) {
            this.top_p = top_p;
        }

        public Integer getN() {
            return n;
        }

        public void setN(Integer n) {
            this.n = n;
        }

        public Boolean getStream() {
            return stream;
        }

        public void setStream(Boolean stream) {
            this.stream = stream;
        }

        public Boolean getLogprobs() {
            return logprobs;
        }

        public void setLogprobs(Boolean logprobs) {
            this.logprobs = logprobs;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }
    }

    private static class ImageGenerations {
        private String prompt;
        private int n = 2;
        private String size = "1024x1024";

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }


}
