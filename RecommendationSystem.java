import java.util.*;

public class RecommendationSystem {

    // Sample user-item ratings matrix
    static double[][] ratings = {
        {5, 3, 0, 0, 2},  // User 1's ratings (Item 1, 2, 3, 4, 5)
        {4, 0, 0, 2, 3},  // User 2's ratings
        {0, 3, 0, 5, 4},  // User 3's ratings
        {0, 0, 4, 4, 0},  // User 4's ratings
        {5, 4, 3, 0, 0}   // User 5's ratings
    };

    public static void main(String[] args) {
        int userIndex = 0; // Assume we want recommendations for User 1
        List<Integer> recommendedItems = recommendItems(userIndex, ratings);
        System.out.println("Recommended items for User " + (userIndex + 1) + ": " + recommendedItems);
    }

    // Function to recommend items for a specific user
    public static List<Integer> recommendItems(int userIndex, double[][] ratingsMatrix) {
        // Step 1: Calculate similarity scores between users
        double[] userSimilarities = calculateUserSimilarities(userIndex, ratingsMatrix);

        // Step 2: Generate recommendations for the user
        Map<Integer, Double> itemScores = new HashMap<>();
        for (int i = 0; i < ratingsMatrix[userIndex].length; i++) {
            if (ratingsMatrix[userIndex][i] == 0) {  // Only recommend items that the user hasn't rated
                double score = 0;
                double similaritySum = 0;
                
                for (int j = 0; j < ratingsMatrix.length; j++) {
                    if (j != userIndex && ratingsMatrix[j][i] != 0) { // User j rated item i
                        double similarity = userSimilarities[j];
                        score += similarity * ratingsMatrix[j][i];
                        similaritySum += Math.abs(similarity);
                    }
                }

                if (similaritySum > 0) {
                    score /= similaritySum;
                }

                if (score > 0) {
                    itemScores.put(i, score);
                }
            }
        }

        // Sort items by score in descending order
        List<Map.Entry<Integer, Double>> sortedItems = new ArrayList<>(itemScores.entrySet());
        sortedItems.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Get the top recommended items
        List<Integer> recommendedItems = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : sortedItems) {
            recommendedItems.add(entry.getKey());
        }

        return recommendedItems;
    }

    // Function to calculate the similarity between a target user and other users
    public static double[] calculateUserSimilarities(int targetUserIndex, double[][] ratingsMatrix) {
        double[] similarities = new double[ratingsMatrix.length];
        for (int i = 0; i < ratingsMatrix.length; i++) {
            if (i != targetUserIndex) {
                similarities[i] = cosineSimilarity(ratingsMatrix[targetUserIndex], ratingsMatrix[i]);
            } else {
                similarities[i] = -1; // Avoid self-similarity
            }
        }
        return similarities;
    }

    // Cosine Similarity between two vectors (users' ratings)
    public static double cosineSimilarity(double[] user1, double[] user2) {
        double dotProduct = 0;
        double magnitudeUser1 = 0;
        double magnitudeUser2 = 0;
        
        for (int i = 0; i < user1.length; i++) {
            dotProduct += user1[i] * user2[i];
            magnitudeUser1 += Math.pow(user1[i], 2);
            magnitudeUser2 += Math.pow(user2[i], 2);
        }

        if (magnitudeUser1 == 0 || magnitudeUser2 == 0) {
            return 0;
        }
        
        return dotProduct / (Math.sqrt(magnitudeUser1) * Math.sqrt(magnitudeUser2));
    }
}
