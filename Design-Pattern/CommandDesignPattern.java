import java.util.Stack;

class Document {
    
    int[][] textPage = new int[100][100];
    
    public void cut() {
        System.out.println("cut operation is performed");
    }
    
    public void copy() {
        System.out.println("copy operation is performed");
    }
    
    public void paste() {
        System.out.println("paste operation is performed");
    }
}

interface ICommand {
    
    public void execute();
    
    public void undo();
}

class CopyCommand implements ICommand {
    Document document;
    
    public CopyCommand(Document document) {
        this.document = document;
    }
    
    public void execute() {
        document.copy();
    }
    
    public void undo() {
        System.out.println("copy undo operation is performed");
    }
}

class CutCommand implements ICommand {
    Document document;
    
    public CutCommand(Document document) {
        this.document = document;
    }
    
    public void execute() {
        document.cut();
    }
    
    public void undo() {
        System.out.println("cut undo operation is performed");
    }
}

class PasteCommand implements ICommand {
    Document document;
    
    public PasteCommand(Document document) {
        this.document = document;
    }
    
    public void execute() {
        document.paste();
    }
    
    public void undo() {
        System.out.println("paste undo operation is performed");
    }
}

class MenuButton {
    
    ICommand command;
    Stack<ICommand> commandHistory = new Stack<>();
    
    public void setCommand(ICommand command) {
        this.command = command;
    }
    
    public void pressButton() {
        command.execute();
        commandHistory.add(command);
    }
    
    public void undo() {
        if(!commandHistory.empty()) {
            ICommand lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }
}

public class CommandDesignPattern {
	public static void main(String[] args) {
		
		Document document = new Document();
		
		ICommand copy = new CopyCommand(document);
		ICommand cut = new CutCommand(document);
		ICommand paste = new PasteCommand(document);
		
		MenuButton invoker = new MenuButton();
		
		invoker.setCommand(copy);
		invoker.pressButton();

		invoker.setCommand(paste);
		invoker.pressButton();
		
		
		invoker.undo();
		invoker.undo();
	}
}
