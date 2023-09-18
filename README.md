# Brief on Azure Computer Vision API Service

1. We are using Azure Computer Vision API to convert Image Stream into Vector. 
2. Then we have a training sample data vectors of 30+ images for different animal disease.
3. Now we are calculating cosine similarity between training and test images.
4. Sorting the entire result based on cosine value.
5. Whichever disease is coming most frequently in top 5 values is the final disease of test image.
