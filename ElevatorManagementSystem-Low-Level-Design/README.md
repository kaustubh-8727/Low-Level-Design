# Elevator Management System

## Overview

The Elevator Management System is designed to efficiently manage the movement of multiple elevators in a building. The system ensures that elevator requests are handled promptly and optimally, catering to both internal and external user demands. This implementation is built using Java and follows a low-level design that includes various components such as elevators, controllers, buttons, and floor management.

## Features

- **Elevator Management**: Handles multiple elevators, each with its own state and movement logic.
- **Request Handling**: Manages requests from internal (within the elevator) and external (on the floors) sources.
- **Direction and Prioritization**: Elevators move in the optimal direction based on current and future requests.
- **Strategies for Elevator Allocation**: Implements strategies like Odd-Even and Fixed Floor allocation to manage requests.
- **Idle Management**: Elevators return to an idle state when all requests are fulfilled.

## System Components

### 1. `Elevator`
- **Attributes**:
  - `elevatorId`: Unique identifier for each elevator.
  - `currentFloor`: Current floor where the elevator is located.
  - `direction`: The direction in which the elevator is moving (UP/DOWN).
  - `internalButton`: Internal button panel to handle requests inside the elevator.
  - `elevatorStatus`: Current status of the elevator (MOVING/IDLE/MAINTENANCE).
- **Methods**:
  - `moveElevator(int destinationFloor, Direction direction)`: Moves the elevator to the specified floor.
  - `setStatus(ElevatorStatus elevatorStatus)`: Updates the elevator's status.

### 2. `ElevatorController`
- **Attributes**:
  - `elevator`: The elevator instance controlled by this controller.
  - `queueForUp`: Priority queue to manage upward floor requests.
  - `queueForDown`: Priority queue to manage downward floor requests.
  - `pendingRequests`: Queue for handling requests that cannot be served immediately.
- **Methods**:
  - `submitRequest(int destinationFloor)`: Handles new floor requests and decides the elevator's direction.
  - `controlFlow()`: Manages the movement of the elevator based on the request queues.

### 3. `InternalButton`
- **Attributes**:
  - `internalButtonController`: Controller for handling internal requests.
- **Methods**:
  - `pressButton(int destinationFloor, String elevatorId)`: Handles button presses inside the elevator.

### 4. `ExternalButton`
- **Attributes**:
  - `externalButtonController`: Controller for handling external requests.
- **Methods**:
  - `pressButton(int floorNumber)`: Handles button presses on a floor.

### 5. `InternalButtonController`
- **Attributes**:
  - `elevatorControllerList`: List of elevator controllers managed by this controller.
- **Methods**:
  - `handleRequest(int destinationFloor, String elevatorId)`: Processes requests from internal buttons.

### 6. `ExternalButtonController` (Abstract)
- **Attributes**:
  - `elevatorControllerList`: List of elevator controllers managed by this controller.
- **Methods**:
  - `handleRequest(int destinationFloor)`: Processes requests from external buttons.
  - `getOptimizedElevatorController(int destinationFloor)`: Abstract method to be implemented by different strategies.

### 7. `OddEvenElevatorStrategy` & `FixedFloorElevatorStrategy`
- **Methods**:
  - `getOptimizedElevatorController(int destinationFloor)`: Implements the strategy for optimal elevator allocation based on destination floors.

### 8. `Floor`
- **Attributes**:
  - `floorNumber`: The floor number.
  - `externalButton`: External button for requesting an elevator.
- **Methods**:
  - `getExternalButton()`: Returns the external button associated with the floor.

### 9. `Building`
- **Attributes**:
  - `buildingName`: Name of the building.
  - `floors`: List of floors in the building.
- **Methods**:
  - `getFloor(int floorNumber)`: Retrieves a specific floor from the building.

## How It Works

1. **Initialization**: The system initializes with a set number of elevators and floors. Each elevator is assigned an ID, and external buttons are set up on each floor.
2. **Handling Requests**:
   - External requests are made via the floor buttons, which trigger the `ExternalButtonController`.
   - Internal requests are made inside the elevator via the internal buttons.
3. **Elevator Movement**: The `ElevatorController` handles the movement of the elevator, processing requests in priority order and adjusting the elevator's direction and status accordingly.
4. **Optimization Strategies**: Different strategies like Odd-Even and Fixed Floor can be applied to optimize the allocation of elevators to requests.

## Sample Output

```
Elevator created with ID: ELEV-F5FF7AAB at floor: 0
Elevator created with ID: ELEV-25499C61 at floor: 0

External button pressed at floor: 3
Elevator ELEV-F5FF7AAB is coming to floor: 3
Request submitted for floor: 3 (Direction: UP)
Elevator ELEV-F5FF7AAB status set to: MOVING
Elevator ELEV-F5FF7AAB moving UP to floor: 3
Elevator ELEV-F5FF7AAB status set to: IDLE
Elevator ELEV-F5FF7AAB is now IDLE at floor: 3

Internal button pressed for floor: 5 in Elevator ID: ELEV-F5FF7AAB
Request submitted for floor: 5 (Direction: UP)
Elevator ELEV-F5FF7AAB status set to: MOVING
Elevator ELEV-F5FF7AAB moving UP to floor: 5
Elevator ELEV-F5FF7AAB status set to: IDLE
Elevator ELEV-F5FF7AAB is now IDLE at floor: 5

Internal button pressed for floor: 1 in Elevator ID: ELEV-F5FF7AAB
Request submitted for floor: 1 (Direction: DOWN)
Elevator ELEV-F5FF7AAB status set to: MOVING
Elevator ELEV-F5FF7AAB moving DOWN to floor: 1
Elevator ELEV-F5FF7AAB status set to: IDLE
Elevator ELEV-F5FF7AAB is now IDLE at floor: 1
