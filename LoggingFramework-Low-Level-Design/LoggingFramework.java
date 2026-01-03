import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5);

    private final int severity;

    LogLevel(int severity) {
        this.severity = severity;
    }

    public boolean isAtLeast(LogLevel other) {
        return this.severity >= other.severity;
    }
}

final class LogMessage {
    private final Instant timestamp;
    private final LogLevel level;
    private final String loggerName;
    private final String message;
    private final String threadName;
    private final Throwable throwable;

    public LogMessage(LogLevel level, String loggerName, String message, Throwable throwable) {
        this.timestamp = Instant.now();
        this.level = level;
        this.loggerName = loggerName;
        this.message = message;
        this.threadName = Thread.currentThread().getName();
        this.throwable = throwable;
    }

    public Instant getTimestamp() { return timestamp; }
    public LogLevel getLevel() { return level; }
    public String getLoggerName() { return loggerName; }
    public String getMessage() { return message; }
    public String getThreadName() { return threadName; }
    public Throwable getThrowable() { return throwable; }
}

interface LogFormatter {
    String format(LogMessage message);
}

class SimpleLogFormatter implements LogFormatter {

    @Override
    public String format(LogMessage msg) {
        return String.format(
            "%s [%s] %s - %s",
            msg.getTimestamp(),
            msg.getLevel(),
            msg.getLoggerName(),
            msg.getMessage()
        );
    }
}

interface Appender {
    void append(LogMessage message);
    void close();
}

class ConsoleAppender implements Appender {

    private final LogFormatter formatter;

    public ConsoleAppender(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void append(LogMessage message) {
        System.out.println(formatter.format(message));
        if (message.getThrowable() != null) {
            message.getThrowable().printStackTrace(System.out);
        }
    }

    @Override
    public void close() {}
}

class FileAppender implements Appender {

    private final LogFormatter formatter;

    public FileAppender(String filePath, LogFormatter formatter) throws IOException {
        this.formatter = formatter;
    }

    @Override
    public synchronized void append(LogMessage message) {
        System.out.println("appended to file");
    }

    @Override
    public void close() {}
}

interface LoggingStrategy {
    void log(LogMessage message, Iterable<Appender> appenders);
    void shutdown();
}

class SyncLoggingStrategy implements LoggingStrategy {

    @Override
    public void log(LogMessage message, Iterable<Appender> appenders) {
        for (Appender appender : appenders) {
            appender.append(message);
        }
    }

    @Override
    public void shutdown() {}
}

class AsyncLoggingStrategy implements LoggingStrategy {

    private final BlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();
    private volatile boolean running = true;

    public AsyncLoggingStrategy(Iterable<Appender> appenders) {
        Thread worker = new Thread(() -> {
            while (running || !queue.isEmpty()) {
                try {
                    LogMessage message = queue.take();
                    for (Appender appender : appenders) {
                        appender.append(message);
                    }
                } catch (InterruptedException ignored) {}
            }
        }, "async-logger-thread");
        worker.setDaemon(true);
        worker.start();
    }

    @Override
    public void log(LogMessage message, Iterable<Appender> appenders) {
        queue.offer(message);
    }

    @Override
    public void shutdown() {
        running = false;
    }
}

class Loggers {

    private final String name;
    private final LogLevel minLevel;
    private final List<Appender> appenders;
    private final LoggingStrategy strategy;

    Loggers(String name,
           LogLevel minLevel,
           List<Appender> appenders,
           LoggingStrategy strategy) {
        this.name = name;
        this.minLevel = minLevel;
        this.appenders = appenders;
        this.strategy = strategy;
    }

    private void log(LogLevel level, String message, Throwable t) {
        if (!level.isAtLeast(minLevel)) return;

        LogMessage logMessage = new LogMessage(level, name, message, t);
        strategy.log(logMessage, appenders);
    }

    public void debug(String msg) { log(LogLevel.DEBUG, msg, null); }
    public void info(String msg) { log(LogLevel.INFO, msg, null); }
    public void warn(String msg) { log(LogLevel.WARN, msg, null); }
    public void error(String msg) { log(LogLevel.ERROR, msg, null); }
    public void fatal(String msg) { log(LogLevel.FATAL, msg, null); }
}

class LoggersFactory {

    private static final LoggersFactory INSTANCE = new LoggersFactory();
    private final Map<String, Loggers> cache = new ConcurrentHashMap<>();

    private LoggersFactory() {}

    public static LoggersFactory getInstance() {
        return INSTANCE;
    }

    public Loggers getLogger(String name,
                            LogLevel level,
                            List<Appender> appenders,
                            boolean async) {

        return cache.computeIfAbsent(name, key -> {
            LoggingStrategy strategy =
                async ? new AsyncLoggingStrategy(appenders)
                      : new SyncLoggingStrategy();
            return new Loggers(key, level, appenders, strategy);
        });
    }
}

public class LoggingFramework {
    public static void main(String[] args) throws Exception {

        LogFormatter formatter = new SimpleLogFormatter();

        Appender console = new ConsoleAppender(formatter);
        Appender file = new FileAppender("app.log", formatter);

        Loggers logger = LoggersFactory.getInstance()
            .getLogger(
                "OrderService",
                LogLevel.INFO,
                List.of(console, file),
                true
            );

        logger.info("Order created");
        logger.error("Payment failed");

        Thread.sleep(2000);
    }
}
