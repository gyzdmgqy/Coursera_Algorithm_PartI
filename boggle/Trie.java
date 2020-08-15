/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:8/15/2020
 *  Description:This is the Trie class definition including its internal Node class.
 **************************************************************************** */

import java.util.HashMap;

public class Trie {
    private Node root;

    Trie() {
        root = new Node(' ', 0);
    }

    public Node getRoot() {
        return root;
    }

    public int searchScore(String key) {
        if (key.length() < 3) return 0;
        Node current = getRoot();
        for (char character : key.toCharArray()) {
            current = current.get(character);
            if (current == null) return 0;
        }
        return current.getScore();
    }

    public void put(String key) {
        Node currentNode = root;
        for (int i = 0; i < key.length(); ++i) {
            int score = 0;
            char currentChar = key.charAt(i);
            if (!currentNode.children.containsKey(currentChar)) {
                currentNode.insert(new Node(currentChar, score));
            }
            currentNode = currentNode.children.get(currentChar);
            if (i == key.length() - 1) {
                score = getScore(key);
                currentNode.setScore(score);
            }
        }
    }

    public static int getScore(String word) {
        int len = word.length();
        if (len > 2 && len <= 4) return 1;
        if (len == 5) return 2;
        if (len == 6) return 3;
        if (len == 7) return 5;
        return 11;
    }


    static class Node {
        private char c;
        private int score;
        private HashMap<Character, Node> children = new HashMap<>();

        Node(char key, int score) {
            this.c = key;
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        private void setScore(int score) {
            this.score = score;
        }

        private void insert(Node node) {
            if (children == null) children = new HashMap<>();
            if (children.containsKey(node.c)) return;
            children.put(node.c, node);
        }

        public Node get(char character) {
            if (children == null) return null;
            if (!children.containsKey(character)) return null;
            return children.get(character);
        }
    }
}
