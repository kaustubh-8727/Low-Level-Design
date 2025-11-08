import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

enum ConnectionState {
    IDLE,
    ACTIVE,
    CLOSED,
    ERROR
}

class Connection {

    private String connectionID;
    private ConnectionState connectionState;
    private Date createdTime;
    private Date lastUsedTime;
    private ConnectionCredentials connectionCredentials;

    public Connection(ConnectionCredentials connectionCredentials) {
        this.connectionCredentials = connectionCredentials;
        this.connectionState = ConnectionState.IDLE;
        this.createdTime = new Date();
        this.connectionID = UUID.randomUUID().toString();
    }

    public String getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(String connectionID) {
        this.connectionID = connectionID;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Date lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }

    public ConnectionCredentials getConnectionCredentials() {
        return connectionCredentials;
    }

    public void setConnectionCredentials(ConnectionCredentials connectionCredentials) {
        this.connectionCredentials = connectionCredentials;
    }

    public void close() {
        this.connectionState = ConnectionState.CLOSED;
    }
}

class ConnectionCredentials {
    private String userName;
    private String password;
    private String host;
    private int port;
    private String databaseName;

    public ConnectionCredentials(String userName, String password, String host, int port, String databaseName) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}

class ConnectionPoolConfig {

    private ConnectionCredentials connectionCredentials;
    private long minimumConnections;
    private long maximumConnections;
    private Double idleTimeout;
    private Double connectionRequestTimeout;
    private boolean lazyInitialization;
    private boolean testOnBorrow;
    private String validationQuery;

    public ConnectionPoolConfig() {
        this.minimumConnections = 5;
        this.maximumConnections = 200;
        this.idleTimeout = 5000.0; // milliseconds
        this.connectionRequestTimeout = 5000.0; // milliseconds
        this.testOnBorrow = false;
        this.validationQuery = "SELECT 1;";
        this.lazyInitialization = false;
    }

    // Getters and Setters
    public ConnectionCredentials getConnectionCredentials() {
        return connectionCredentials;
    }

    public void setConnectionCredentials(ConnectionCredentials connectionCredentials) {
        this.connectionCredentials = connectionCredentials;
    }

    public long getMinimumConnections() {
        return minimumConnections;
    }

    public void setMinimumConnections(long minimumConnections) {
        this.minimumConnections = minimumConnections;
    }

    public long getMaximumConnections() {
        return maximumConnections;
    }

    public void setMaximumConnections(long maximumConnections) {
        this.maximumConnections = maximumConnections;
    }

    public Double getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Double idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public Double getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Double connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public boolean isLazyInitialization() {
        return lazyInitialization;
    }

    public void setLazyInitialization(boolean lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
}

class ConnectionPoolConfigBuilder {

    private ConnectionCredentials connectionCredentials;
    private long minimumConnections;
    private long maximumConnections;
    private Double idleTimeout;
    private Double connectionRequestTimeout;
    private boolean lazyInitialization;
    private boolean testOnBorrow;
    private String validationQuery;

    public ConnectionPoolConfigBuilder setConnectionCredentials(ConnectionCredentials connectionCredentials) {
        this.connectionCredentials = connectionCredentials;
        return this;
    }

    public ConnectionPoolConfigBuilder setMinimumConnections(long minimumConnections) {
        this.minimumConnections = minimumConnections;
        return this;
    }

    public ConnectionPoolConfigBuilder setMaximumConnections(long maximumConnections) {
        this.maximumConnections = maximumConnections;
        return this;
    }

    public ConnectionPoolConfigBuilder setIdleTimeout(Double idleTimeout) {
        this.idleTimeout = idleTimeout;
        return this;
    }

    public ConnectionPoolConfigBuilder setConnectionRequestTimeout(Double connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public ConnectionPoolConfigBuilder setLazyInitialization(boolean lazyInitialization) {
        this.lazyInitialization = lazyInitialization;
        return this;
    }

    public ConnectionPoolConfigBuilder setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }

    public ConnectionPoolConfigBuilder setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
        return this;
    }

    public ConnectionPoolConfig build() {
        ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
        
        connectionPoolConfig.setConnectionCredentials(this.connectionCredentials);
        connectionPoolConfig.setMinimumConnections(this.minimumConnections);
        connectionPoolConfig.setMaximumConnections(this.maximumConnections);
        connectionPoolConfig.setIdleTimeout(this.idleTimeout);
        connectionPoolConfig.setConnectionRequestTimeout(this.connectionRequestTimeout);
        connectionPoolConfig.setLazyInitialization(this.lazyInitialization);
        connectionPoolConfig.setTestOnBorrow(this.testOnBorrow);
        connectionPoolConfig.setValidationQuery(this.validationQuery);

        return connectionPoolConfig;
    }
}

class ConnectionPoolService {

    private final ConnectionPoolConfig connectionPoolConfig;
    private final BlockingQueue<Connection> idleConnections = new LinkedBlockingQueue<>();
    private final Set<Connection> activeConnections = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicLong connectionCount = new AtomicLong(0);

    public ConnectionPoolService(ConnectionPoolConfig connectionPoolConfig) throws Exception {
        validateConnectionPoolConfig(connectionPoolConfig);
        this.connectionPoolConfig = connectionPoolConfig;

        handleConnectionCreation();

        startCleanupTask();
    }

    private void validateConnectionPoolConfig(ConnectionPoolConfig config) throws Exception {
        if (config.getConnectionCredentials() == null)
            throw new Exception("Credentials not provided");
    }

    private void handleConnectionCreation() throws Exception {
        if (!connectionPoolConfig.isLazyInitialization()) {
            for (int i = 0; i < connectionPoolConfig.getMinimumConnections(); i++) {
                createConnection();
            }
        }
    }

    private void createConnection() throws Exception {
        if (connectionCount.get() >= connectionPoolConfig.getMaximumConnections()) {
            throw new Exception("Maximum connection limit reached");
        }

        Connection connection = new Connection(connectionPoolConfig.getConnectionCredentials());
        idleConnections.add(connection);
        connectionCount.incrementAndGet();
    }

    public Connection borrowConnection() throws Exception {
        Connection connection = idleConnections.poll();

        if (connection == null) {
            if (connectionCount.get() < connectionPoolConfig.getMaximumConnections()) {
                createConnection();
                connection = idleConnections.poll(2, TimeUnit.SECONDS);
            } else {
                connection = idleConnections.poll(connectionPoolConfig.getConnectionRequestTimeout().longValue(),
                                                  TimeUnit.MILLISECONDS);
                if (connection == null) {
                    throw new TimeoutException("No available connections within timeout period");
                }
            }
        }

        // Mark as active
        connection.setConnectionState(ConnectionState.ACTIVE);
        activeConnections.add(connection);

        if (connectionPoolConfig.isTestOnBorrow()) {
            validateConnection(connection);
        }

        return connection;
    }

    public void returnConnection(Connection connection) {
        if (connection != null) {
            activeConnections.remove(connection);
            connection.setConnectionState(ConnectionState.IDLE);
            idleConnections.add(connection);
        }
    }

    private void validateConnection(Connection connection) throws Exception {
        String validationQuery = connectionPoolConfig.getValidationQuery();
        if (validationQuery == null || validationQuery.isEmpty()) {
            throw new Exception("Invalid validation query");
        }
        // Example: Actually execute the validation query on DB (not implemented here)
    }

    private void startCleanupTask() {
        cleanupExecutor.scheduleAtFixedRate(() -> {
            try {
                cleanupIdleConnections();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    private void cleanupIdleConnections() throws Exception {
        long now = System.currentTimeMillis();

        for (Connection conn : idleConnections) {
            long idleTime = now - conn.getCreatedTime().getTime();
            if (idleTime > connectionPoolConfig.getIdleTimeout()) {
                deleteConnection(conn);
            }
        }
    }

    private void deleteConnection(Connection connection) throws Exception {
        if (connection != null && idleConnections.remove(connection)) {
            connection.close();
            connectionCount.decrementAndGet();
            System.out.println("Deleted idle connection: " + connection.getConnectionID());
        }
    }

    public void shutdown() {
        cleanupExecutor.shutdownNow();
        idleConnections.forEach(conn -> {
            try {
                conn.close();
            } catch (Exception ignored) {}
        });
        activeConnections.forEach(conn -> {
            try {
                conn.close();
            } catch (Exception ignored) {}
        });
        idleConnections.clear();
        activeConnections.clear();
    }
}

class ConnectionPool {
    public static void main(String[] args) {
        try {
            // Create Credentials
            ConnectionCredentials credentials = new ConnectionCredentials(
                    "admin", "password", "localhost", 3306, "testDB");

            // Build Pool Config
            ConnectionPoolConfig poolConfig = new ConnectionPoolConfigBuilder()
                    .setConnectionCredentials(credentials)
                    .setMinimumConnections(3)
                    .setMaximumConnections(10)
                    .setIdleTimeout(5000.0) // 5 seconds
                    .setConnectionRequestTimeout(2000.0)
                    .setLazyInitialization(false)
                    .setTestOnBorrow(false)
                    .setValidationQuery("SELECT 1;")
                    .build();

            // Create Connection Pool Service
            ConnectionPoolService poolService = new ConnectionPoolService(poolConfig);

            // Borrow and Return Connections Sequentially
            System.out.println("\n--- Borrowing connections sequentially ---");
            Connection c1 = poolService.borrowConnection();
            System.out.println("Borrowed connection: " + c1.getConnectionID() + " | State: " + c1.getConnectionState());

            Connection c2 = poolService.borrowConnection();
            System.out.println("Borrowed connection: " + c2.getConnectionID() + " | State: " + c2.getConnectionState());

            // Return one connection
            poolService.returnConnection(c1);
            System.out.println("Returned connection: " + c1.getConnectionID());

            // Wait to Trigger Idle Cleanup
            System.out.println("\n--- Waiting for idle connections to expire ---");
            Thread.sleep(15000); // wait 15 seconds for cleanup thread to remove old ones

            // Shutdown Pool
            poolService.shutdown();
            System.out.println("\nConnection Pool shutdown complete.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
