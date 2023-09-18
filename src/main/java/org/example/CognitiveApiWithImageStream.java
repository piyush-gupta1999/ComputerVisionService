package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CognitiveApiWithImageStream {
    private final String imagePath;

    private static byte[] readImageToByteArray(String imagePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(imagePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }

    public CognitiveApiWithImageStream(String imagePath) {
        this.imagePath = imagePath;
    }

    /*
     * Read Trained Model data from a JSON file
     */
    public JSONArray getTrainedData() throws IOException {
        File f = new File(Constants.animalAIModel);
        if (f.exists()){
            InputStream is = Files.newInputStream(Paths.get(Constants.animalAIModel));
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonTxt);
            return json.getJSONArray("json_data");
        }
        return null;
    }

    /*
     * Using Azure Computer Vision Service, this method will return the Image Vector of size 1024
     */
    public JSONArray getImageVector() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(Constants.cognitiveApiImageStreamURL);

        try {
            byte[] imageBytes = readImageToByteArray(imagePath);

            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
            httpPost.setHeader(new BasicHeader("Ocp-Apim-Subscription-Key", Constants.accessKey));
            HttpEntity entity = new ByteArrayEntity(imageBytes);
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                JSONObject jsonResponse = new JSONObject(responseString);
                return jsonResponse.getJSONArray("vector");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
