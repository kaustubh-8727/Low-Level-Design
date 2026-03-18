import java.util.*;

abstract class Node {
    String name;
    Directory parent;

    public Node(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }
}

class File extends Node {
    String content;
    Date createdAt;
    Date updatedAt;
    long size;

    public File(String name, String content, Directory parent) {
        super(name, parent);
        this.content = content;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.size = content.getBytes().length;
    }
}

class Directory extends Node {
    Map<String, Node> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        this.children = new HashMap<>();
    }

    public void addNode(Node node) {
        children.put(node.name, node);
    }

    public Node getNode(String name) {
        return children.get(name);
    }
}

interface IFileSystem {
    void mkdir(String name);
    void createFile(String name, String content);
    void cd(String path);
    String pwd();
}

class FileSystem implements IFileSystem {

    private static volatile FileSystem instance;

    private Directory root;
    private Directory current;

    private FileSystem() {
        root = new Directory("", null);
        current = root;
    }

    public static FileSystem getInstance() {
        if (instance == null) {
            synchronized (FileSystem.class) {
                if (instance == null) {
                    instance = new FileSystem();
                }
            }
        }
        return instance;
    }

    public void mkdir(String name) {
        if (current.children.containsKey(name)) {
            throw new RuntimeException("Directory already exists");
        }

        Directory dir = new Directory(name, current);
        current.addNode(dir);
    }

    public void createFile(String name, String content) {
        if (current.children.containsKey(name)) {
            throw new RuntimeException("File already exists");
        }

        File file = new File(name, content, current);
        current.addNode(file);
    }

    public void cd(String path) {
        Directory temp = path.startsWith("/") ? root : current;

        String[] parts = path.split("/");

        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) continue;

            if (part.equals("..")) {
                if (temp.parent != null) temp = temp.parent;
            } 
            else if (part.equals("*")) {
                temp = handleWildcard(temp);
            } 
            else {
                Node node = temp.getNode(part);

                if (node == null || !(node instanceof Directory)) {
                    throw new RuntimeException("Invalid path: " + part);
                }

                temp = (Directory) node;
            }
        }

        current = temp;
    }

    private Directory handleWildcard(Directory dir) {

        for (Node node : dir.children.values()) {
            if (node instanceof Directory) {
                return (Directory) node;
            }
        }
        return dir;
    }

    public String pwd() {
        if (current == root) return "/";

        StringBuilder path = new StringBuilder();
        Directory temp = current;

        while (temp != root) {
            path.insert(0, "/" + temp.name);
            temp = temp.parent;
        }

        return path.toString();
    }
}

public class FileSystemAdvance {
    public static void main(String[] args) {
        FileSystem fs = FileSystem.getInstance();

        fs.mkdir("home");
        fs.cd("home");
        fs.mkdir("user");
        fs.cd("user");
        fs.createFile("file.txt", "hello");

        System.out.println(fs.pwd());

        fs.cd("..");
        System.out.println(fs.pwd());

        fs.cd("/home/user");
        System.out.println(fs.pwd());
    }
}
