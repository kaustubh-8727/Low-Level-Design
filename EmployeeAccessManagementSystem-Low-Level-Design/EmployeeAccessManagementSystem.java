
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

enum Permission {
    READ,
    WRITE,
    ADMIN,
    NO_ACCESS
}

interface User {

    public Resource getResource(String resourceId);
}

class Employee implements User {

    private String userId;
    private String userName;
    private String contactNumber;
    private String address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Resource getResource(String resourceId) {
        return null;
    }
}

interface Resource {
    String getResourceId();
    String getResourceType();
}


class FileResource implements Resource {

    private String resourceId;
    private String resourceType;

    public String getResourceId() {
        return resourceId;
    }
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}

class DbResource implements Resource {

    private String resourceId;
    private String resourceType;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}

class ApiResource implements Resource {

    private String resourceId;
    private String resourceType;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}

class ResourceMapper {
    private final String resourceId;
    private final Set<Permission> permissions = new HashSet<>();

    public ResourceMapper(String resourceId) {
        this.resourceId = resourceId;
    }

    public void addPermission(Permission permission) { permissions.add(permission); }

    public void removePermission(Permission permission) { permissions.remove(permission); }

    public void clearPermissions() { permissions.clear(); }

    public boolean hasPermission(Permission permission) { return permissions.contains(permission); }

    public Set<Permission> getPermissions() { return Collections.unmodifiableSet(permissions); }

    public String getResourceId() { return resourceId; }
}

class ResourceManager {

    private final Map<String, Map<String, ResourceMapper>> resourceMap = new HashMap<>();

    public void grantAccess(String employeeId, String resourceId, Permission permission) {
        resourceMap.computeIfAbsent(employeeId, k -> new HashMap<>())
                   .computeIfAbsent(resourceId, k -> new ResourceMapper(resourceId))
                   .addPermission(permission);
    }

    public void revokeAccess(String employeeId, String resourceId, Permission permission) {
        Map<String, ResourceMapper> employeeResources = resourceMap.get(employeeId);
        if (employeeResources == null) return;

        ResourceMapper mapper = employeeResources.get(resourceId);
        if (mapper == null) return;

        if (permission == null) {
            mapper.clearPermissions();
        } else {
            mapper.removePermission(permission);
        }
    }

    public ResourceMapper retrieveAccess(String employeeId, String resourceId) {
        Map<String, ResourceMapper> employeeResources = resourceMap.get(employeeId);
        if (employeeResources == null) return null;

        return employeeResources.get(resourceId);
    }

    public List<String> retrieveResources(String employeeId) {
        Map<String, ResourceMapper> employeeResources = resourceMap.get(employeeId);
        if (employeeResources == null) return Collections.emptyList();

        List<String> accessibleResources = new ArrayList<>();
        for (ResourceMapper mapper : employeeResources.values()) {
            if (!mapper.getPermissions().isEmpty()) {
                accessibleResources.add(mapper.getResourceId());
            }
        }
        return accessibleResources;
    }
}

class EmployeeResourceProxy {
    private static EmployeeResourceProxy instance;
    private final ResourceManager resourceManager;

    private EmployeeResourceProxy(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public static EmployeeResourceProxy getInstance(ResourceManager resourceManager) {
        if (instance == null) {
            instance = new EmployeeResourceProxy(resourceManager);
        }
        return instance;
    }

    public Resource getResource(String employeeId, String resourceId) {
        ResourceMapper mapper = resourceManager.retrieveAccess(employeeId, resourceId);

        if (mapper != null && !mapper.getPermissions().isEmpty()) {
            // Example: return FileResource; can extend to DB/API dynamically
            return new FileResource();
        } else {
            System.out.println("Access Denied for employee: " + employeeId + ", resource: " + resourceId);
            return null;
        }
    }

    public boolean hasPermission(String employeeId, String resourceId, Permission permission) {
        ResourceMapper mapper = resourceManager.retrieveAccess(employeeId, resourceId);
        return mapper != null && mapper.hasPermission(permission);
    }

    public List<String> getAllAccessibleResources(String employeeId) {
        return resourceManager.retrieveResources(employeeId);
    }
}

// ----------------- Demo -----------------
public class EmployeeAccessManagementSystem {
    public static void main(String[] args) {
        ResourceManager resourceManager = new ResourceManager();

        // Employees
        Employee e1 = new Employee();
        e1.setUserId("e1");
        Employee e2 = new Employee();
        e2.setUserId("e2");

        // Grant access
        resourceManager.grantAccess("E1", "R1", Permission.READ);
        resourceManager.grantAccess("E1", "R1", Permission.WRITE);
        resourceManager.grantAccess("E2", "R2", Permission.ADMIN);

        // Singleton proxy
        EmployeeResourceProxy proxy = EmployeeResourceProxy.getInstance(resourceManager);

        // Access resources
        Resource r1 = proxy.getResource("E1", "R1"); // Allowed
        Resource r2 = proxy.getResource("E1", "R2"); // Denied

        System.out.println("E1 Accessible Resources: " + proxy.getAllAccessibleResources("E1"));
        System.out.println("E2 Accessible Resources: " + proxy.getAllAccessibleResources("E2"));

        // Check permission
        System.out.println("does E1 has WRITE on R1 " + proxy.hasPermission("E1", "R1", Permission.WRITE));
    }
}
