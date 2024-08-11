class Node<K,V> {
    
    K key;
    V value;
    Node next;
    
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
}

class HashMaps<K,V> {
    private static final int  INITIAL_SIZE = 1<<4;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    Node[] hashTable;

    public HashMaps(){
        hashTable= new Node[INITIAL_SIZE];
    }
    
    public void put(K key, V value) {
        
        int hashCode = key.hashCode() % hashTable.length;
        Node node = hashTable[hashCode];
        
        if(node == null) {
            Node newNode = new Node(key, value);
            hashTable[hashCode] = newNode;
        } else {
            Node prevNode = hashTable[hashCode];
            
            while(node != null) {
                
                if(node.getKey() == key) {
                    node.setValue(value);
                    return;
                }
                
                prevNode = node;
                node = node.next;
            }
            
            prevNode.next = new Node(key, value);
        }
    }
    
    public V get(K key) {
        int hashCode = key.hashCode() % hashTable.length;
        Node node = hashTable[hashCode];
        
        while(node != null) {
            if(node.getKey() == key) {
                return (V)node.getValue();
            }
        }
        
        return null;
    }
}

public class HashMapDesign {
	public static void main(String[] args) {
		HashMaps<Integer, String> hashMap = new HashMaps<>();
		hashMap.put(1, "value 1");
        hashMap.put(2, "value 2");
        hashMap.put(4, "value 4");
        hashMap.put(5, "value 5");

        String value1 = hashMap.get(2);
        System.out.println(value1);
        
        hashMap.put(2, "value 2 updated");
        
        String value2 = hashMap.get(2);
        System.out.println(value2);
        
        String value3 = hashMap.get(6);
        System.out.println(value3);
	}
}
