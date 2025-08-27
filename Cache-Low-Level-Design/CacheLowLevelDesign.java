
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


interface CacheStorage<K,V> {

    public V readValue(K key);
    public void addValue(K key, V value);
    public void removeValue(K key);
    public boolean containsKey(K key);
    public int size();
    public int getCapacity();
}

class CacheStorageImpl<K,V> implements CacheStorage<K, V> {

    int capacity;
    Map<K,V> cacheMap;

    public CacheStorageImpl(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new ConcurrentHashMap<>(); 

    }

    public void addValue(K key, V value) {
        cacheMap.put(key, value);
    }

    public V readValue(K key) {
        if(cacheMap.containsKey(key)) {
            return cacheMap.get(key);
        }

        return null;
    }

    public void removeValue(K key) {
        if(cacheMap.containsKey(key)) {
            cacheMap.remove(key);
        }
    }

    public boolean containsKey(K key) {
        return cacheMap.containsKey(key);
    }

    public int size() {
        return cacheMap.size();
    }

    public int getCapacity() {
        return this.capacity;
    }
}

interface DatabaseStorage<K,V> {

    public V readValue(K key);
    public void addValue(K key, V value);
    public void removeValue(K key);
}

class DatabaseStorageImpl<K,V> implements DatabaseStorage<K, V> {

    public V readValue(K key) {
        return null;
    }

    public void addValue(K key, V value) {
    }

    public void removeValue(K key) {
    }
}

interface CacheEvicitionAlgorithm<K,V> {

    public void updateKey(K key);
    public K evictCache();
}

class LFUCacheEvictionAlgorithm<K,V> implements CacheEvicitionAlgorithm<K,V> {

    public void updateKey(K key) {
        
    }
    public K evictCache() {
        return null;
    }
}

class LRUCacheEvictionAlgorithm<K, V> implements CacheEvicitionAlgorithm<K, V> {
    Map<K, DoublyLinkedListNode<K>> keyNodeMap;
    DoublyLinkedListNode<K> head;
    DoublyLinkedListNode<K> tail;

    public LRUCacheEvictionAlgorithm() {
        this.keyNodeMap = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    // called when key is accessed or added
    public void updateKey(K key) {
        if (keyNodeMap.containsKey(key)) {
            DoublyLinkedListNode<K> node = keyNodeMap.get(key);
            detachNode(node);
            addNodeAtTail(node);
        } else {
            DoublyLinkedListNode<K> newNode = new DoublyLinkedListNode<>(key);
            addNodeAtTail(newNode);
            keyNodeMap.put(key, newNode);
        }
    }

    // evict least recently used (head)
    public K evictCache() {

        System.err.println("eviction process started");

        if (head == null) {
            return null;
        }

        K key = head.getValue();
        keyNodeMap.remove(key);
        removeNodeAtHead();

        return key;
    }

    private void detachNode(DoublyLinkedListNode<K> node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            // node was head
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            // node was tail
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
    }

    private void addNodeAtTail(DoublyLinkedListNode<K> node) {
        if (tail == null) {
            // first element
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    private void removeNodeAtHead() {
        if (head == null) return;

        if (head.next != null) {
            head = head.next;
            head.prev = null;
        } else {
            // only one element
            head = null;
            tail = null;
        }
    }
}

class DoublyLinkedListNode<K> {
    K key;
    DoublyLinkedListNode<K> prev;
    DoublyLinkedListNode<K> next;

    public DoublyLinkedListNode(K key) {
        this.key = key;
        this.prev = null;
        this.next = null;
    }

    public K getValue() {
        return key;
    }
}

class CacheEvicitionStrategy<K, V> {
    CacheEvicitionAlgorithm<K, V> cacheEvicitionAlgorithm;

    public CacheEvicitionStrategy(CacheEvicitionAlgorithm<K, V> cacheEvicitionAlgorithm) {
        this.cacheEvicitionAlgorithm = cacheEvicitionAlgorithm;
    }

    public CacheEvicitionAlgorithm<K, V> getCacheEvicitionAlgorithm() {
        return this.cacheEvicitionAlgorithm;
    }
}

class Cache<K,V> {
    CacheStorage<K,V> cacheStorage;
    DatabaseStorage<K,V> databaseStorage;
    CacheEvicitionStrategy<K, V> cacheEvicitionStrategy;

    public Cache(CacheStorage cacheStorage, DatabaseStorage databaseStorage, CacheEvicitionAlgorithm cacheEvicitionAlgorithm) {
        this.cacheStorage = cacheStorage;
        this.databaseStorage = databaseStorage;
        this.cacheEvicitionStrategy = new CacheEvicitionStrategy(cacheEvicitionAlgorithm);
    }

    public synchronized  void updateData(K key, V value) {

        if(cacheStorage.containsKey(key)) {
            cacheStorage.addValue(key, value);
            cacheEvicitionStrategy.getCacheEvicitionAlgorithm().updateKey(key);
            databaseStorage.addValue(key, value);
        }
        else {
            int cacheCurrentcapacity = cacheStorage.size();

            if(cacheCurrentcapacity == cacheStorage.getCapacity()) {
                K removeKey = cacheEvicitionStrategy.getCacheEvicitionAlgorithm().evictCache();
                cacheStorage.removeValue(removeKey);
            }

            cacheStorage.addValue(key, value);
            cacheEvicitionStrategy.getCacheEvicitionAlgorithm().updateKey(key);
            databaseStorage.addValue(key, value);
        }
    }

    public synchronized V getData(K key) {
        if (cacheStorage.containsKey(key)) {
            cacheEvicitionStrategy.getCacheEvicitionAlgorithm().updateKey(key);
            return cacheStorage.readValue(key);
        } else {
            V value = databaseStorage.readValue(key);
            if (value != null) {
                updateData(key, value);
            }
            return value;
        }
    }

}

// Main class with Use Case
public class CacheLowLevelDesign {
    public static void main(String[] args) {
        CacheEvicitionAlgorithm<Integer, String> lruEviction = new LRUCacheEvictionAlgorithm<>();
        CacheStorage<Integer, String> cacheStorage = new CacheStorageImpl<>(5);
        DatabaseStorage<Integer, String> databaseStorage = new DatabaseStorageImpl<>();

        Cache<Integer, String> cache = new Cache<>(cacheStorage, databaseStorage, lruEviction);

        cache.updateData(1, "red");
        cache.updateData(2, "blue");
        cache.updateData(3, "yellow");
        cache.updateData(4, "green");
        cache.updateData(5, "orange");

        String value_2 = cache.getData(2);
        String value_4 = cache.getData(4);
        String value_5 = cache.getData(5);

        System.out.println("value fetched for key - 2 from cache is " + value_2);
        System.out.println("value fetched for key - 4 from cache is " + value_4);
        System.out.println("value fetched for key - 5 from cache is " + value_5);

        cache.updateData(6, "purple");

        String value_6 = cache.getData(6);

        System.out.println("value fetched for key - 6 from cache is " + value_6);
        
    }
}
