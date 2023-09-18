package org.example;

public class Constants {

    // Azure Computer Vision Service Endpoint
    public static String cognitiveApiImageStreamURL = "https://cxcvapis.cognitiveservices.azure.com/computervision/retrieval:vectorizeImage?overload=stream&api-version=2023-02-01-preview";

    // Animal (stuck in tar) Image path
    public static String animalImagePath = "src/main/java/org/example/media/PuppyInTar.jpg";

    // Animal (paralysed) Image path
    public static String paralyzedAnimalImagePath = "src/main/java/org/example/media/ParalysedDog.jpg";

    // ML training sample data file path
    public static String animalAIModel = "src/main/java/org/example/model/DogModel.json";

    // First AID suggestion file path
    public static String firstAIDSuggestionModel = "src/main/java/org/example/model/FirstAidSuggestions.json";

    // Access Key to Access Azure Computer Vision API
    public static String accessKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static String animalImageURL = "https://picsum.photos/id/237/200/300";

}
