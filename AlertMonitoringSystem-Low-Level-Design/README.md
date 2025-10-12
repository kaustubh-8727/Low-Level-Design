# Alert Monitoring System (Java)

A modular **Alert Monitoring System** designed in Java to simulate how machines and their sensors (Temperature, Pressure, etc.) are monitored in real time.  
Each sensor is associated with a set of **rules** defining upper and lower thresholds.  
If sensor readings violate these thresholds, the system automatically **generates and sends alerts** to the user.

---

## System Overview

The system models a simplified version of a real-world IoT alerting workflow:

Machine → Sensors → Rules → AlertManager → AlertNotifier → User


1. **Machines** contain multiple sensors (Temperature, Pressure, etc.).
2. **Sensors** provide real-time readings (via `getData()`).
3. **Rules** define threshold limits for each sensor per machine.
4. **AlertManager** periodically evaluates sensors against rules.
5. **Alerts** are created and passed to an **AlertNotifier**, which can send them via console, email, webhook, etc.

---

## Class Design

### 1. `Sensor` (Abstract)
- Base class for all sensor types.
- Provides `getData()` and `getSensorType()`.
- Examples:
  - `TempratureSensor`
  - `PressureSensor`

### 2. `Rule`
- Defines high and low thresholds for a specific sensor.
- Evaluates sensor data to determine alert type (Low / High urgency).

### 3. `Machine`
- Represents a machine with multiple sensors attached.
- Supports adding/removing sensors dynamically.

### 4. `Alert`
- Represents a triggered event.
- Includes:
  - `alertId`
  - `sensorId`
  - `alertType`
  - `alertReported` timestamp

### 5. `AlertNotifier` (Interface)
- Abstraction for how alerts are sent.
- Implementations:
  - `ConsoleAlertNotifier` (prints to console)
  - Can be extended to `EmailAlertNotifier`, `SlackAlertNotifier`, etc.

### 6. `AlertManager`
- Central monitoring component.
- Responsibilities:
  - Evaluate sensor readings against their rules.
  - Generate alerts when thresholds are violated.
  - Send alerts via the chosen notifier.

---

## Design Patterns Used

| Pattern | Purpose |
|----------|----------|
| **Strategy Pattern** | Used in `AlertNotifier` to allow different alert delivery mechanisms (console, email, etc.) without changing core logic. |
| **Factory Pattern** *(optional)* | Can be added for creating sensors dynamically by type. |
| **Observer Pattern** *(conceptually)* | Alerts are triggered when sensor data changes — the system “observes” sensors. |

---

## Example Flow

```text
1. Create Machine → add sensors (Temperature, Pressure)
2. Define Rules → threshold values per sensor
3. AlertManager monitors sensors
4. If threshold violated → create Alert
5. AlertNotifier sends the alert (Console / Email / Webhook)
```

## Example Output

```
ALERT SENT:
Alert{alertId='05243d4f-7d36-44ac-8adc-f4880bec2ec5', sensorId='temp_1', alertType=LOW_URGENCY, alertReported=Sun Oct 12 22:34:52 IST 2025}

ALERT SENT:
Alert{alertId='c1e9224c-fdcf-486f-aa31-6b204b0f6f9a', sensorId='press_1', alertType=HIGH_URGENCY, alertReported=Sun Oct 12 22:34:52 IST 2025}
```
