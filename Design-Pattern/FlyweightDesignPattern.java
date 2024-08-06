import java.util.HashMap;
import java.util.Map;

interface ITextDocument {
    
    public void draw(int row, int column);
}

class TextDocument implements ITextDocument {
    
    private char character;
    private String fontType;
    private int fontSize;
    private String color;
    private boolean bold;
    private boolean itallic;
    private boolean underline;
    
    public TextDocument(char character, String fontType, int fontSize, String color, boolean bold, boolean itallic, boolean underline) {
        this.character = character;
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.color = color;
        this.bold = bold;
        this.itallic = itallic;
        this.underline = underline;
    }
    
    public void draw(int row, int column) {
        System.out.println("character '" + character + "' is drawn at row : " + row + " and column : " + column);
    }
}

class TextDocumentFactory {
    
    private static Map<Character, ITextDocument> characterCache = new HashMap<>();
    
    public ITextDocument createLetter(char character) {
        
        if(characterCache.containsKey(character)) {
            return characterCache.get(character);
        }
        
        ITextDocument newLetter = new TextDocument(character, "Ariel", 12, "black", false, false, false);
        characterCache.put(character, newLetter);
        
        return newLetter;
    }
}

public class FlyweightDesignPattern {  
    public static void main(String args[]){
        
        // create a word "hello" in text document
        TextDocumentFactory createWord = new TextDocumentFactory();
        
        ITextDocument letterH = createWord.createLetter('h');
        letterH.draw(0,0);
        
        ITextDocument letterE = createWord.createLetter('e');
        letterE.draw(0,1);
        
        ITextDocument letterL = createWord.createLetter('l');
        letterL.draw(0,2);
        
        letterL = createWord.createLetter('l');
        letterL.draw(0,3);
        
        ITextDocument letterO = createWord.createLetter('o');
        letterO.draw(0,4);
    }
}
