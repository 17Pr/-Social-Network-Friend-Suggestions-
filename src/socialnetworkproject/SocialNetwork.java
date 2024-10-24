package socialnetworkproject;
import java.util.*;

public class SocialNetwork {
	private Map<String, List<String>> graph;

    public SocialNetwork() {
        graph = new HashMap<>();
    }

    // Add a friendship (edge) between two users (nodes)
    public void addFriendship(String user1, String user2) {
        graph.computeIfAbsent(user1, k -> new ArrayList<>()).add(user2);
        graph.computeIfAbsent(user2, k -> new ArrayList<>()).add(user1);
    }
 // Suggest friends based on mutual friends using BFS
    public List<String> suggestFriends(String user) {
        if (!graph.containsKey(user)) {
            return Collections.emptyList();  // No suggestions if the user doesn't exist
        }

        Set<String> visited = new HashSet<>();
        Map<String, Integer> mutualFriends = new HashMap<>();  // Count mutual friends
        Queue<String> queue = new LinkedList<>();
        queue.add(user);
        visited.add(user);
        int level = 0;

        while (!queue.isEmpty() && level <= 2) {  // Explore up to friends-of-friends (2 levels)
            int size = queue.size();
            level++;
            
            for (int i = 0; i < size; i++) {
                String currentUser = queue.poll();
                for (String friend : graph.get(currentUser)) {
                    if (!visited.contains(friend)) {
                        if (level == 2) {  // If it's a friend's friend, increase mutual friend count
                            mutualFriends.put(friend, mutualFriends.getOrDefault(friend, 0) + 1);
                        } else {
                            queue.add(friend);
                        }
                        visited.add(friend);
                    }
                }
            }
        }

        // Sort suggestions based on number of mutual friends in descending order
        List<Map.Entry<String, Integer>> sortedSuggestions = new ArrayList<>(mutualFriends.entrySet());
        sortedSuggestions.sort((a, b) -> b.getValue() - a.getValue());

        // Return sorted suggestions (just user names)
        List<String> suggestions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedSuggestions) {
            suggestions.add(entry.getKey() + " (Mutual Friends: " + entry.getValue() + ")");
        }

        return suggestions;
    }
    
 // Display the social network (for visualization/debugging)
    public void displayNetwork() {
        for (String user : graph.keySet()) {
            System.out.println(user + ": " + graph.get(user));
        }
    }
    public static void main(String[] args) {
        // Create a social network
        SocialNetwork network = new SocialNetwork();
        
        // Add some friendships (sample data)
        network.addFriendship("Alice", "Bob");
        network.addFriendship("Alice", "Charlie");
        network.addFriendship("Bob", "David");
        network.addFriendship("Charlie", "David");
        network.addFriendship("Charlie", "Eve");
        network.addFriendship("David", "Frank");
        network.addFriendship("Eve", "Frank");
        network.addFriendship("Frank", "George");

        // Display the network
        System.out.println("Social Network Connections:");
        network.displayNetwork();

        // Suggest friends for a specific user
        String userToSuggestFor = "Alice";
        System.out.println("\nFriend suggestions for " + userToSuggestFor + ":");
        List<String> suggestions = network.suggestFriends(userToSuggestFor);

        if (!suggestions.isEmpty()) {
            for (String suggestion : suggestions) {
                System.out.println(suggestion);
            }
        } else {
            System.out.println("No new friends to suggest.");
        }
    }
}
