package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class CognitiveApiWithImageURL {

    public static void main(String[] args) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://cxcvapis.cognitiveservices.azure.com/computervision/retrieval:vectorizeImage?api-version=2023-02-01-preview");

        String key = "70915561d6bc420fb651ed7c871b9982";

        // Create a JSON request body with the image URL
        String jsonRequestBody = "{\"url\":\"https://picsum.photos/id/237/200/300\"}";
        StringEntity entity = new StringEntity(jsonRequestBody, "UTF-8");
        entity.setContentType("application/json");
        httpPost.setHeader(new BasicHeader("Ocp-Apim-Subscription-Key", key));
        httpPost.setEntity(entity);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                JSONObject jsonResponse = new JSONObject(responseString);
                String modelVersion = jsonResponse.getString("modelVersion");
                JSONArray array = jsonResponse.getJSONArray("vector");
                System.out.println("Model Version: " + modelVersion);
                System.out.println("Vector: " + array.toString());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}