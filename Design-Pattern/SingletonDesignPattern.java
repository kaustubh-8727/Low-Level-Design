class Connection {
    
    private static Connection connection = null;
    
    private Connection() {
    }
    
    public static Connection getInstance() {
        
        if(connection == null) {
            synchronized (Connection.class) {
                if(connection == null) {
                    connection = new Connection();
                }
            }
        }
        
        return connection;
    }
    
}

class SingletonDesignPattern {
    public static void main(String args[]) {
        
        Connection connection1 = Connection.getInstance();
        
        // calling same instance
        Connection connection2 = Connection.getInstance();
        
        if(connection1 == connection2) {
            System.out.println("singleton design pattern....");
        }
        
    }
}
