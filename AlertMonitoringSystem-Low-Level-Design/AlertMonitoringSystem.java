import java.util.*;

enum SENSOR_TYPE {
    TEMPRATURE,
    PRESSURE
}

enum SENSOR_STATUS {
    ENABLED,
    DISABLED,
    NOT_WORKING
}

enum ALERT_TYPE {
    LOW_URGENCY,
    HIGH_URGENCY,
    NO_ALERT
}

enum ALERT_STATUS {
    PENDING,
    RESOLVED,
    OTHER
}

abstract class Sensor {
    SENSOR_STATUS sensorStatus;
    String sensorId;

    abstract public double getData();
    abstract public SENSOR_TYPE getSensorType();

    public void setSensorStatus(SENSOR_STATUS sensorStatus) {
        this.sensorStatus = sensorStatus;
    }

    public String getSensorId() {
        return this.sensorId;
    }
}

class TempratureSensor extends  Sensor {

    public TempratureSensor(String sensorId) {
        this.sensorId = sensorId;
        this.sensorStatus = SENSOR_STATUS.ENABLED;
    }

    public double getData() {
        if(this.sensorStatus == SENSOR_STATUS.ENABLED) {
            return 5.0;
        }
        return 0.0;
    }

    public SENSOR_TYPE getSensorType() {
        return SENSOR_TYPE.TEMPRATURE;
    }
}

class PressureSensor extends Sensor {

    public PressureSensor(String sensorId) {
        this.sensorId = sensorId;
        this.sensorStatus = SENSOR_STATUS.ENABLED;
    }

    public double getData() {
        if(this.sensorStatus == SENSOR_STATUS.ENABLED) {
            return 12.0;
        }
        return 0.0;
    }

    public SENSOR_TYPE getSensorType() {
        return SENSOR_TYPE.PRESSURE;
    }

    public void setSensorStatus(SENSOR_STATUS sensorStatus) {
        this.sensorStatus = sensorStatus;
    }
}

class Rule {
    private final String ruleId;
    private final String sensorId;
    private final String ruleName;
    private double lowThresholdValue;
    private double highThresholdValue;

    public Rule(String ruleId, String sensorId, String ruleName, double lowThresholdValue, double highThresholdValue) {
        this.ruleId = ruleId;
        this.sensorId = sensorId;
        this.ruleName = ruleName;
        this.lowThresholdValue = lowThresholdValue;
        this.highThresholdValue = highThresholdValue;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public double getLowThresholdValue() {
        return lowThresholdValue;
    }

    public double getHighThresholdValue() {
        return highThresholdValue;
    }

    public void setLowThreshold(double value) {
        this.lowThresholdValue = value;
    }

    public void setHighThreshold(double value) {
        this.highThresholdValue = value;
    }

    public ALERT_TYPE evaluateAlert(double currentValue) {
        if(currentValue >= lowThresholdValue && currentValue < highThresholdValue) return ALERT_TYPE.LOW_URGENCY;
        else if(currentValue >= lowThresholdValue && currentValue >= highThresholdValue) return ALERT_TYPE.HIGH_URGENCY;
        return ALERT_TYPE.NO_ALERT;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "ruleId='" + ruleId + '\'' +
                ", sensorId='" + sensorId + '\'' +
                ", ruleName='" + ruleName + '\'' +
                ", lowThresholdValue=" + lowThresholdValue +
                ", highThresholdValue=" + highThresholdValue +
                '}';
    }
}

class Machine {
    private String machineId;
    private String machineName;
    private List<Sensor> sensors;

    public Machine(String machineId, String machineName) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.sensors = new ArrayList<>();
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor) {
        if (sensors == null) {
            sensors = new ArrayList<>();
        }
        sensors.add(sensor);
    }

    public void removeSensor(String sensorId) {
        if (sensors != null) {
            sensors.removeIf(sensor -> sensor.getSensorId().equals(sensorId));
        }
    }

    @Override
    public String toString() {
        return "Machine{" +
                "machineId='" + machineId + '\'' +
                ", machineName='" + machineName + '\'' +
                ", sensors=" + sensors +
                '}';
    }
}

class Alert {
    private String alertId;
    private String sensorId;
    private ALERT_TYPE alertType;
    private Date alertReported;

    public Alert(String alertId, String sensorId, ALERT_TYPE alertType, Date alertReported) {
        this.alertId = alertId;
        this.sensorId = sensorId;
        this.alertType = alertType;
        this.alertReported = alertReported;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public ALERT_TYPE getAlertType() {
        return alertType;
    }

    public void setAlertType(ALERT_TYPE alertType) {
        this.alertType = alertType;
    }

    public Date getAlertReported() {
        return alertReported;
    }

    public void setAlertReported(Date alertReported) {
        this.alertReported = alertReported;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertId='" + alertId + '\'' +
                ", sensorId='" + sensorId + '\'' +
                ", alertType=" + alertType +
                ", alertReported=" + alertReported +
                '}';
    }
}

interface AlertNotifier {
    void send(Alert alert);
}

class ConsoleAlertNotifier implements AlertNotifier {
    public void send(Alert alert) {
        System.out.println("ALERT SENT:");
        System.out.println(alert);
    }
}

class EmailAlertNotifier implements AlertNotifier {
    public void send(Alert alert) {
        System.out.println("Sending email for alert: " + alert.getAlertId());
    }
}

class AlertManager {
    List<Machine> machines;
    Map<String, Rule> rules;
    AlertNotifier notifier;

    public AlertManager(AlertNotifier notifier) {
        this.machines = new ArrayList<>();
        this.rules = new HashMap<>();
        this.notifier = notifier;
    }

    public void createRule(String ruleName, String sensorId, double lowThresholdValue, double highThresholdValue) {
        String ruleId = "rule_" + UUID.randomUUID().toString();
        Rule rule = new Rule(ruleId, sensorId, ruleName, lowThresholdValue, highThresholdValue);
        rules.put(sensorId, rule);
    }

    public void evaluateAlerts() {
        for (Machine machine : machines) {
            for (Sensor sensor : machine.getSensors()) {

                String sensorId = sensor.getSensorId();
                double currentValue = sensor.getData();

                if (rules.containsKey(sensorId)) {
                    Rule rule = rules.get(sensorId);
                    ALERT_TYPE type = rule.evaluateAlert(currentValue);
                    if (type != ALERT_TYPE.NO_ALERT) {
                        createSendAlert(rule, type);
                    }
                }
            }
        }
    }

    public void createSendAlert(Rule rule, ALERT_TYPE alertType) {
        String alertId = UUID.randomUUID().toString();
        Alert alert = new Alert(alertId, rule.getSensorId(), alertType, new Date());
        notifier.send(alert);
    }
}


class AlertMonitoringSystem {
    public static void main(String[] args) {
        // Create sensors
        Sensor tempSensor = new TempratureSensor("temp_1");
        Sensor pressureSensor = new PressureSensor("press_1");

        // Create machine and attach sensors
        Machine machine = new Machine("m1", "Machine-1");
        machine.addSensor(tempSensor);
        machine.addSensor(pressureSensor);

        // Create alert manager with notifier
        AlertNotifier consoleNotifier = new ConsoleAlertNotifier();
        AlertManager manager = new AlertManager(consoleNotifier);
        manager.machines.add(machine);

        // Create rules
        manager.createRule("TempRule", "temp_1", 2.0, 8.0);
        manager.createRule("PressureRule", "press_1", 3.0, 9.0);

        // Evaluate alerts
        manager.evaluateAlerts();
    }
}
