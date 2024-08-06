import java.util.ArrayList;
import java.util.List;


interface FileSystem {
    void ls();
    List<String> findFile(String fileExtension);
}

class File implements FileSystem {
    String fileName;
    
    File(String fileName) {
        this.fileName = fileName;
    }
    
    public void ls() {
        System.out.println("file name is - " + fileName);
    }
    
    public List<String> findFile(String fileExtension) {
        List<String> fileFoundList = new ArrayList<>();
        
        if(this.hasSameExtension(fileName, fileExtension)) {
            fileFoundList.add(fileName);
        }
        
        return fileFoundList;
    }
    
    public static boolean hasSameExtension(String filename, String fileExtension) {
        
      int index = filename.lastIndexOf('.');
      if (index == -1) {
        return false;
      }
      
      String extension = filename.substring(index);
      
      return extension.equalsIgnoreCase(fileExtension);
    }
}

class Directory implements FileSystem {
    String directoryName;
    List<FileSystem> directoryList;
    
    Directory(String directoryName) {
        this.directoryName = directoryName;
        directoryList = new ArrayList<>();
    }
    
    public void add(FileSystem fileSystem) {
        directoryList.add(fileSystem);
    }
    
    public void ls() {
        System.out.println("directory name is - " + directoryName);
        for(FileSystem fileSystem : directoryList) {
            fileSystem.ls();
        }
    }
    
    public List<String> findFile(String fileExtension) {
        List<String> fileFoundList = new ArrayList<>();
        
        for(FileSystem fileSystem : directoryList) {
            
            List<String> foundList = fileSystem.findFile(fileExtension);
            
            if(!foundList.isEmpty()) fileFoundList.addAll(foundList);
        }
        
        return fileFoundList;
    }
}

public class CompositeDesignPattern {
	public static void main(String[] args) {
		Directory rootDirectory = new Directory("root");
		
		//create 2 files
		FileSystem file1 = new File("facebook.txt");
		FileSystem file2 = new File("amazon.xml");
		FileSystem file3 = new File("google.js");
		FileSystem file4 = new File("microsoft.txt");
		FileSystem file5 = new File("netflix.xml");
		FileSystem file6 = new File("apple.xml");
		
		// create 2 directories
		Directory directory1 = new Directory("company1");
		Directory directory2 = new Directory("company2");
		
		rootDirectory.add(directory1);
		rootDirectory.add(directory2);
		
		directory1.add(file1);
		directory1.add(file3);
		directory1.add(file4);
		directory1.add(file5);
		
		
		directory2.add(file2);
		directory2.add(file6);
		
		rootDirectory.ls();
		
		System.out.println("\nsearch all files with extension with .XML in a directory....\n");
		
		List<String> filesList = rootDirectory.findFile(".xml");
		for(String file : filesList) {
		    System.out.println(file);
		}
		
	}
}
