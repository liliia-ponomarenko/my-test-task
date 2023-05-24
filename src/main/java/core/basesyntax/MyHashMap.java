package core.basesyntax;

//import java.util.HashMap;
import java.util.Objects;
import java.util.Random;


public class MyHashMap<K, V> implements MyMap<K, V> {
    public double EXPANSION_COEFFICIENT = 2;
    public double LOAD_FACTOR = 0.75;
    private Node<K, V>[] values;
    private static final int defaultCapacity = 16;
    private int size;
    private Integer threshold;

    public MyHashMap() {
        values = new Node[defaultCapacity];
        this.threshold = (int) (defaultCapacity * LOAD_FACTOR);
        size = 0;
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> nextNode;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node<K, V> getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node<K, V> nextNode) {
            this.nextNode = nextNode;
        }
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            Node<K, V>[] oldBucketStock = values;
            values = new Node[values.length * 2];
            for (int i = 0; i < oldBucketStock.length; i++) {
                while (oldBucketStock[i] != null) {
                    K k = oldBucketStock[i].key;
                    V v = oldBucketStock[i].value;
                    int index = getIndexFromHash(k);
                    if (values[index] == null) {
                        values[index] = new Node<>(k, v);
                    } else {
                        Node<K, V> lastBucketNode = values[index];
                        while (lastBucketNode.nextNode != null) {
                            lastBucketNode = lastBucketNode.nextNode;
                        }
                        lastBucketNode.nextNode = new Node<>(k, v);
                    }
                    oldBucketStock[i] = oldBucketStock[i].nextNode;
                }
            }
            this.threshold = (int) (values.length * LOAD_FACTOR);
        }
        int index = getIndexFromHash(key);
        if (values[index] == null) {
            values[index] = new Node<>(key, value);
        } else {
            Node<K, V> lastBucketNode = values[index];
            if (lastBucketNode.key == null && key == null) {
                lastBucketNode.value = value;
                return;
            } else if (key != null && key.equals(lastBucketNode.key)) {
                lastBucketNode.value = value;
                return;
            }
            while (lastBucketNode.nextNode != null) {
                if (Objects.equals(lastBucketNode.key, key)) {
                    lastBucketNode.value = value;
                    return;
                }
                lastBucketNode = lastBucketNode.nextNode;
            }
            lastBucketNode.nextNode = new Node<>(key, value);
        }
        size = size + 1;
    }



    @Override
    public V getValue(K key) {
        int index = getIndexFromHash(key);
        Node<K, V> returnNode = values[index];
        while (returnNode != null) {
            /* if (key == returnNode.key) {
                return returnNode.value;
            } */
            if (key == null && returnNode.key == null) {
                return returnNode.value;
            }
            if (key != null && key.equals(returnNode.key)) {
                return returnNode.value;
            }
            returnNode = returnNode.nextNode;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private int hash(int hashCode, int primaryNumber) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);

    }

    private int generateRandomPrimaryNumber() {
        int n = new Random().nextInt(100);
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return n;
        return 1;
    }

    public int getIndexFromHash(K key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(hash(key.hashCode(), generateRandomPrimaryNumber()) % values.length);
        }

    }
}