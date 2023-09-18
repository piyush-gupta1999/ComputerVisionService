package org.example;

import javafx.util.Pair;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AnimalDiseaseDetection {
    private final CognitiveApiWithImageStream cognitiveApiWithImageStream;
    private Map<String, String> suggestions;

    /*
     * Calculating dot product of 2 Image Vectors
     */
    private double getDot(JSONArray A, JSONArray B) {
        double ans = 0.0;
        for(int i=0; i < A.length();i++) {
            ans += A.getDouble(i) * B.getDouble(i);
        }
        return ans;
    }

    /*
     * Calculating cosine of 2 Image Vectors for similarity
     */
    private double calculateCosine(JSONArray modelData, JSONArray imageData) {
        double modelDataDot = Math.sqrt(getDot(modelData, modelData));
        double imageDataDot = Math.sqrt(getDot(imageData, imageData));
        return getDot(modelData, imageData) / (modelDataDot * imageDataDot);
    }

    /*
     * Initializing First AID Suggestions for Volunteer to do something before ambulance arrive
     */
    private void initFirstAidSuggestionSuggestions() throws IOException {
        suggestions = new HashMap<>();
        File f = new File(Constants.firstAIDSuggestionModel);
        if (f.exists()){
            InputStream is = Files.newInputStream(Paths.get(Constants.firstAIDSuggestionModel));
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonTxt);

            for(int i = 0 ; i< jsonArray.length() ; i++) {
                JSONObject diseasesJson = jsonArray.getJSONObject(i);
                String diseaseName = diseasesJson.getString("diseaseName");
                String firstAid = diseasesJson.getString("firstAid");
                suggestions.put(diseaseName, firstAid);
            }
        }
    }

    public AnimalDiseaseDetection(String imagePath) throws IOException {
        initFirstAidSuggestionSuggestions();
        this.cognitiveApiWithImageStream = new CognitiveApiWithImageStream(imagePath);
    }

    /*
     * After analysing Animal Image this method will print disease name with First AID suggestions
     */
    public void getAnimalDisease() throws IOException {
        List<Pair<String, Double>> result = new ArrayList<>();
        JSONArray testImageData = cognitiveApiWithImageStream.getImageVector();
        if(testImageData != null) {
            JSONArray modelData = cognitiveApiWithImageStream.getTrainedData();
            if(modelData != null) {
                for(int i = 0 ; i< modelData.length() ; i++) {
                    JSONArray diseasesJson = modelData.getJSONArray(i);
                    String diseaseName = diseasesJson.getString(0);
                    JSONArray diseaseVector = diseasesJson.getJSONArray(1);

                    double cosineValue = calculateCosine(diseaseVector, testImageData);
                    result.add(new Pair<>(diseaseName, cosineValue));
                }
                result.sort(new PairComparator());
                System.out.println("***************************** Total Pairs *****************************");
                result.forEach(stringDoublePair -> System.out.println("DiseaseName: " + stringDoublePair.getKey() + ", CosineValue: " + stringDoublePair.getValue()));
                System.out.println("***********************************************************************");

                System.out.println("\n\n**********************Top 5 Disease Pair********************************");
                Map<String, Integer> diseaseFrequencyMap = new HashMap<>();
                for(int i = 0 ; i < Math.min(result.size(), 5); i++) {
                    String disease = result.get(i).getKey();
                    System.out.println("Disease Name: " + disease + ", CosineValue: " + result.get(i).getValue());
                    diseaseFrequencyMap.put(disease, diseaseFrequencyMap.getOrDefault(disease, 0) + 1);
                }
                System.out.println("***********************************************************************");
                int maxFrequency = Collections.max(diseaseFrequencyMap.values());

                List<String> mostFrequentStrings = new ArrayList<>();
                for (Map.Entry<String, Integer> entry : diseaseFrequencyMap.entrySet()) {
                    if (entry.getValue() == maxFrequency) {
                        mostFrequentStrings.add(entry.getKey());
                    }
                }
                System.out.println("\n\n************************** Most Matched Disease ****************************");
                System.out.print("Disease Name: ");
                mostFrequentStrings.forEach(System.out::println);
                System.out.println("First Aid Suggestions for " + mostFrequentStrings.get(0));
                System.out.println(suggestions.get("Mange"));
                System.out.println("*********************************************************************************");
            } else {
                System.out.println("Model Vector is null.");
            }
        } else {
            System.out.println("Image Vector is null.");
        }

    }
}


