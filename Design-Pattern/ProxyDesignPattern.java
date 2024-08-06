class Employee {
    int ID;
    String name;
    String contactNumber;
    String address;
}

interface IEmployeeDao {
    
    public void addEmployee(Employee employee);
    
    public void deleteEmployee(String employeeID);
    
    public void getEmployee(String employeeID);
}

class EmployeeImpl implements IEmployeeDao {
    
    public void addEmployee(Employee employee) {
        System.out.println("employee is created successfully....");
    }
    
    public void deleteEmployee(String employeeID) {
        System.out.println("employee is deleted successfully....");
    }
    
    public void getEmployee(String employeeID) {
        System.out.println("employee records fetched successfully....");
    }
}

class EmployeeProxy {
    
    IEmployeeDao employee;
    
    public EmployeeProxy(IEmployeeDao employee) {
        this.employee = employee;
    }
    
    public void addEmployee(String client, Employee employeeDetails) throws Exception {
        if(client == "ADMIN") {
            employee.addEmployee(employeeDetails);
            return;
        }
        
        throw new Exception("user does not sufficient previliges to create....");
    }
    
    public void deleteEmployee(String client, String employeeID) throws Exception {
        if(client == "ADMIN") {
            employee.deleteEmployee(employeeID);
            return;
        }
        
        throw new Exception("user does not sufficient previliges to delete....");
    }
    
    public void getEmployee(String client, String employeeID) throws Exception {
        if(client == "ADMIN" || client == "USER") {
            employee.getEmployee(employeeID);
            return;
        }
        
        throw new Exception("user does not sufficient previliges fetch....");
    }
}

public class ProxyDesignPattern {  
    public static void main(String args[]){
        
        Employee employeeDetails = new Employee();
        IEmployeeDao employee = new EmployeeImpl();
        
        EmployeeProxy employeeProxy = new EmployeeProxy(employee);
        
        try {
            employeeProxy.addEmployee("ADMIN", employeeDetails);
            employeeProxy.getEmployee("USER", "1");
            employeeProxy.deleteEmployee("USER", "1");
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
}
