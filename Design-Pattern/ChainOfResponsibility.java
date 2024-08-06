enum LogType {
    INFO,
    DEBUG,
    ERROR,
    WARN
}

interface  LogProcessor {
    public void log(LogType logtype, String message);
}

class InfoLogger implements LogProcessor {
    
    LogProcessor nextLogProcessor;
    
    public InfoLogger(LogProcessor logProcessor) {
        this.nextLogProcessor = logProcessor;
    }
    
    public void log(LogType logtype, String message) {
        
        if(logtype == LogType.INFO) {
            System.out.println("INFO : " + message);
            return;
        }
        
        nextLogProcessor.log(logtype, message);
    }
}

class DebugLogger implements LogProcessor {
    
    LogProcessor nextLogProcessor;
    
    public DebugLogger(LogProcessor logProcessor) {
        this.nextLogProcessor = logProcessor;
    }
    
    public void log(LogType logtype, String message) {
        
        if(logtype == LogType.DEBUG) {
            System.out.println("DEBUG : " + message);
            return;
        }
        
        nextLogProcessor.log(logtype, message);
    }
}

class ErrorLogger implements LogProcessor {
    
    LogProcessor nextLogProcessor;
    
    public ErrorLogger(LogProcessor logProcessor) {
        this.nextLogProcessor = logProcessor;
    }
    
    public void log(LogType logtype, String message) {
        
        if(logtype == LogType.ERROR) {
            System.out.println("ERROR : " + message);
            return;
        }
        
        nextLogProcessor.log(logtype, message);
    }
}

class WarnLogger implements LogProcessor {
    
    LogProcessor nextLogProcessor;
    
    public WarnLogger(LogProcessor logProcessor) {
        this.nextLogProcessor = logProcessor;
    }
    
    public void log(LogType logtype, String message) {
        
        if(logtype == LogType.WARN) {
            System.out.println("WARN : " + message);
            return;
        }
        
        System.out.println("invalid logger....");
    }
}

public class ChainOfResponsibility {
    public static void main(String args[]){
        
        LogProcessor warnLogger = new WarnLogger(null);
        LogProcessor errorLogger = new ErrorLogger(warnLogger);
        LogProcessor debugLogger = new DebugLogger(errorLogger);
        LogProcessor infoLogger = new InfoLogger(debugLogger);
        
        LogProcessor logger = infoLogger;
        
        logger.log(LogType.DEBUG, "can you debug my code");
        logger.log(LogType.INFO, "can you show info");
    }
}
