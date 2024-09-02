import java.util.Collections;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

enum ElevatorStatus {
    MOVING,
    IDLE,
    MAINTENANCE
}

enum Direction {
    UP,
    DOWN
}

class Elevator {

    String elevatorId;
    int currentFloor;
    Direction direction;
    InternalButton internalButton;
    ElevatorStatus elevatorStatus;

    public Elevator() {
        this.elevatorId = "ELEV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.elevatorStatus = ElevatorStatus.IDLE;
        this.currentFloor = 0;
        this.internalButton = new InternalButton();
        System.out.println("Elevator created with ID: " + elevatorId + " at floor: " + currentFloor);
    }

    public void setInternalButton(InternalButton internalButton) {
        this.internalButton = internalButton;
    }

    public InternalButton getInternalButton() {
        return internalButton;
    }

    public String getElevatorId() {
        return elevatorId;
    }

    public Direction getCurrentDirection() {
        return direction;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorStatus getStatus() {
        return elevatorStatus;
    }

    public void setStatus(ElevatorStatus elevatorStatus) {
        this.elevatorStatus = elevatorStatus;
        System.out.println("Elevator " + elevatorId + " status set to: " + elevatorStatus);
    }

    public void moveElevator(int destinationFloor, Direction direction) {
        this.currentFloor = destinationFloor;
        this.direction = direction;
        System.out.println("Elevator " + elevatorId + " moving " + direction + " to floor: " + destinationFloor);
    }
}

class ElevatorController {

    Elevator elevator;
    PriorityQueue<Integer> queueForUp = new PriorityQueue<>();
    PriorityQueue<Integer> queueForDown = new PriorityQueue<>(Collections.reverseOrder());
    Queue<Integer> pendingRequests = new LinkedList<>();

    public ElevatorController(Elevator elevator) {
        this.elevator = elevator;
    }

    public void submitRequest(int destinationFloor) {
        Direction currentDirection = elevator.getCurrentDirection();
        int currentFloor = elevator.getCurrentFloor();
        Direction requestedDirection = currentFloor < destinationFloor ? Direction.UP : Direction.DOWN;

        if (requestedDirection == Direction.UP) {
            if (currentDirection == Direction.UP || elevator.getStatus() == ElevatorStatus.IDLE) {
                queueForUp.offer(destinationFloor);
            } else {
                pendingRequests.offer(destinationFloor);
            }
        } else if (requestedDirection == Direction.DOWN) {
            if (currentDirection == Direction.DOWN || elevator.getStatus() == ElevatorStatus.IDLE) {
                queueForDown.offer(destinationFloor);
            } else {
                pendingRequests.offer(destinationFloor);
            }
        }

        System.out.println("Request submitted for floor: " + destinationFloor + " (Direction: " + requestedDirection + ")");
        controlFlow();
    }

    public void controlFlow() {
        elevator.setStatus(ElevatorStatus.MOVING);

        while (!queueForUp.isEmpty()) {
            int nextFloor = queueForUp.poll();
            elevator.moveElevator(nextFloor, Direction.UP);
        }

        while (!queueForDown.isEmpty()) {
            int nextFloor = queueForDown.poll();
            elevator.moveElevator(nextFloor, Direction.DOWN);
        }

        while (!pendingRequests.isEmpty()) {
            submitRequest(pendingRequests.poll());
        }

        if (queueForUp.isEmpty() && queueForDown.isEmpty() && pendingRequests.isEmpty()) {
            elevator.setStatus(ElevatorStatus.IDLE);
            System.out.println("Elevator " + elevator.getElevatorId() + " is now IDLE at floor: " + elevator.getCurrentFloor());
        }
    }

    public String getElevatorId() {
        return elevator.getElevatorId();
    }
}


class InternalButton {

    InternalButtonController internalButtonController;

    public void setInternalButtonController(InternalButtonController internalButtonController) {
        this.internalButtonController = internalButtonController;
    }

    public void pressButton(int destinationFloor, String elevatorId) {
        System.out.println("Internal button pressed for floor: " + destinationFloor + " in Elevator ID: " + elevatorId);
        internalButtonController.handleRequest(destinationFloor, elevatorId);
    }
}

class ExternalButton {

    ExternalButtonController externalButtonController;

    public ExternalButton(ExternalButtonController externalButtonController) {
        this.externalButtonController = externalButtonController;
    }

    public void pressButton(int floorNumber) {
        System.out.println("External button pressed at floor: " + floorNumber);
        externalButtonController.handleRequest(floorNumber);
    }
}

class InternalButtonController {

    List<ElevatorController> elevatorControllerList = new ArrayList<>();

    public InternalButtonController(List<ElevatorController> elevatorControllerList) {
        this.elevatorControllerList = elevatorControllerList;
    }

    public void handleRequest(int destinationFloor, String elevatorId) {
        ElevatorController elevatorController = getElevatorController(elevatorId);
        elevatorController.submitRequest(destinationFloor);
    }

    public ElevatorController getElevatorController(String elevatorId) {
        for (ElevatorController elevatorController : elevatorControllerList) {
            if (elevatorController.getElevatorId().equals(elevatorId)) {
                return elevatorController;
            }
        }

        return null;
    }
}

abstract class ExternalButtonController {

    List<ElevatorController> elevatorControllerList = new ArrayList<>();

    public ExternalButtonController(List<ElevatorController> elevatorControllerList) {
        this.elevatorControllerList = elevatorControllerList;
    }

    public void handleRequest(int destinationFloor) {
        ElevatorController selectedElevatorController = getOptimizedElevatorController(destinationFloor);
        if (selectedElevatorController != null) {
            System.out.println("Elevator " + selectedElevatorController.getElevatorId() + " is coming to floor: " + destinationFloor);
            selectedElevatorController.submitRequest(destinationFloor);
        } else {
            System.out.println("No elevators available to service the request at floor: " + destinationFloor);
        }
    }

    public abstract ElevatorController getOptimizedElevatorController(int destinationFloor);
}

class OddEvenElevatorStrategy extends ExternalButtonController {

    public OddEvenElevatorStrategy(List<ElevatorController> elevatorControllerList) {
        super(elevatorControllerList);
    }

    public ElevatorController getOptimizedElevatorController(int destinationFloor) {
        // logic to get nearest odd or even elevator based on destination floor
        return elevatorControllerList.get(0); // simplified for now
    }
}

class FixedFloorElevatorStrategy extends ExternalButtonController {

    public FixedFloorElevatorStrategy(List<ElevatorController> elevatorControllerList) {
        super(elevatorControllerList);
    }

    public ElevatorController getOptimizedElevatorController(int destinationFloor) {
        // logic to get nearest odd or even elevator based on destination floor
        return elevatorControllerList.get(0); // simplified for now
    }
}

class Floor {
    int floorNumber;
    ExternalButton externalButton;

    public Floor(int floorNumber, ExternalButton externalButton) {
        this.floorNumber = floorNumber;
        this.externalButton = externalButton;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public ExternalButton getExternalButton() {
        return externalButton;
    }
}

class Building {

    String buildingName;
    List<Floor> floors = new ArrayList<>();

    public Building(String buildingName, List<Floor> floors) {
        this.buildingName = buildingName;
        this.floors = floors;
    }

    public Floor getFloor(int floorNumber) {
        for (Floor floor : floors) {
            if (floor.getFloorNumber() == floorNumber) {
                return floor;
            }
        }
        return null;
    }
}

public class ElevatorManagementsystem {
    public static void main(String[] args) {

        // create elevators
        Elevator elevator1 = new Elevator();
        ElevatorController elevatorController1 = new ElevatorController(elevator1);
        Elevator elevator2 = new Elevator();
        ElevatorController elevatorController2 = new ElevatorController(elevator2);

        // create elevator list
        List<ElevatorController> elevatorControllerList = new ArrayList<>();
        elevatorControllerList.add(elevatorController1);
        elevatorControllerList.add(elevatorController2);

        // create elevator strategy
        ExternalButtonController oddEvenElevatorStrategy = new OddEvenElevatorStrategy(elevatorControllerList);

        // create internal elevator strategy
        InternalButtonController internalButtonController = new InternalButtonController(elevatorControllerList);

        // set internal button controller
        elevator1.getInternalButton().setInternalButtonController(internalButtonController);
        elevator2.getInternalButton().setInternalButtonController(internalButtonController);

        // create floors
        Floor floor1 = new Floor(1, new ExternalButton(oddEvenElevatorStrategy));
        Floor floor2 = new Floor(2, new ExternalButton(oddEvenElevatorStrategy));
        Floor floor3 = new Floor(3, new ExternalButton(oddEvenElevatorStrategy));
        Floor floor4 = new Floor(4, new ExternalButton(oddEvenElevatorStrategy));
        Floor floor5 = new Floor(5, new ExternalButton(oddEvenElevatorStrategy));

        // create list of floors
        List<Floor> floors = new ArrayList<>();
        floors.add(floor1);
        floors.add(floor2);
        floors.add(floor3);
        floors.add(floor4);
        floors.add(floor5);

        // create building
        Building building = new Building("Building-1", floors);

        // simulate elevator calling and moving
        Floor floor3Ref = building.getFloor(3);
        floor3Ref.getExternalButton().pressButton(3);

        elevator1.getInternalButton().pressButton(5, elevator1.getElevatorId());
        elevator1.getInternalButton().pressButton(1, elevator1.getElevatorId());
    }
}
