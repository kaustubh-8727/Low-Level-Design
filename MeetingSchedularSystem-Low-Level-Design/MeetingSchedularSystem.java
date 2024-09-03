import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

class User {
    String userName;
    String emailId;
    String contactNumber;
    
    public User(String userName, String emailId, String contactNumber) {
        this.userName = userName;
        this.emailId = emailId;
        this.contactNumber = contactNumber;
    }
    
    public void sendEmail(Meeting meeting) {
        System.out.println("=========================================");
        System.out.println("          Meeting Notification           ");
        System.out.println("=========================================");
        System.out.println("Sending email to: " + emailId);
        System.out.println("-----------------------------------------");
        System.out.println("            Meeting Details              ");
        System.out.println("-----------------------------------------");
        System.out.println("Meeting ID      : " + meeting.getMeetingId());
        System.out.println("Topic           : " + meeting.getMeetingTopic());
        System.out.println("Meeting Room ID : " + meeting.getMeetingRoomId());
        System.out.println("Start Time      : " + meeting.getStartTime() + " PM");
        System.out.println("End Time        : " + meeting.getEndTime() + " PM");
        System.out.println("Capacity        : " + meeting.getCapacity());
        System.out.println("-----------------------------------------");
        System.out.println("            Participants List            ");
        System.out.println("-----------------------------------------");
        for (User participant : meeting.getParticipants()) {
            System.out.println("- " + participant.userName + " (" + participant.emailId + ")");
        }
        System.out.println("=========================================");
        System.out.println();
    }
}

class Meeting {

    private String meetingId;
    private String meetingTopic;
    private String meetingRoomId;
    private int startTime;
    private int endTime;
    private int capacity;
    private List<User> participants = new ArrayList<>();
    
    public Meeting() {
        this.meetingId = "MEETING-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public String getMeetingId() {
        return meetingId;
    }
    
    public String getMeetingRoomId() {
        return meetingRoomId;
    }

    public String getMeetingTopic() {
        return meetingTopic;
    }
    
    public void setMeetingRoomId(String meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public void setMeetingTopic(String meetingTopic) {
        this.meetingTopic = meetingTopic;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }
    
    public int getCapacity() {
        return capacity;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void addParticipant(User participant) {
        participants.add(participant);
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

class Interval {
    int startTime;
    int endTime;
    int capacity;
    
    public Interval(int startTime, int endTime, int capacity) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
    }
    
    public int getCapacity() {
        return capacity;
    }
    
    public int getStartTime() {
        return startTime;
    }
    
    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}

class MeetingRoom {
    
    String meetingRoomId;
    int capacity;
    Location meetingRoomAddress;
    
    public MeetingRoom(int capacity, Location meetingRoomAddress) {
        this.meetingRoomId = "ROOM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.capacity = capacity;
        this.meetingRoomAddress = meetingRoomAddress;
    }
    
    public String getMeetingRoomId() {
        return meetingRoomId;
    }
    
    public int getCapacity() {
        return capacity;
    }
}

class MeetingRoomManager {
    
    List<MeetingRoom> meetingRoomList = new ArrayList<>();
    Map<String, Calendar> meetingRoomCalenderMap = new HashMap<>();
    
    public void addMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoomList.add(meetingRoom);
        meetingRoomCalenderMap.put(meetingRoom.getMeetingRoomId(), new Calendar());
    }
    
    public void removeMeetingRoom(String meetingRoomId) {
        for(MeetingRoom meetingRoom : meetingRoomList) {
            if(meetingRoom.getMeetingRoomId() == meetingRoomId) {
                meetingRoomList.remove(meetingRoom);
            }
        }
    }
    
    public void addMeetingtoCalendar(Meeting meetingDO) {
        
        Calendar calendar = meetingRoomCalenderMap.get(meetingDO.getMeetingRoomId());
        calendar.addMeeting(meetingDO);
    }
    
    public void removeMeetingFromCalendar(Meeting meetingDO) {
        
        Calendar calendar = meetingRoomCalenderMap.get(meetingDO.getMeetingRoomId());
        calendar.removeMeeting(meetingDO);
    }
    
    public List<MeetingRoom> checkMeetingRoomAvailability(Interval requestedInterval) {
        
        List<MeetingRoom> availableMeetingRooms = new ArrayList<>();
        
        for(MeetingRoom meetingRoom : meetingRoomList) {
            
            if(meetingRoom.getCapacity() >= requestedInterval.getCapacity()) {
                
                Calendar calendar = meetingRoomCalenderMap.get(meetingRoom.getMeetingRoomId());
                
                if(calendar.checkMeetingSlotAvailability(requestedInterval)) {
                    availableMeetingRooms.add(meetingRoom);
                }
            }
        }
        
        return availableMeetingRooms;
    }
    
    public MeetingRoom getMeetingRoom(String meetingRoomId) {
        for(MeetingRoom meetingRoom : meetingRoomList) {
            if(meetingRoom.getMeetingRoomId() == meetingRoomId) {
                return meetingRoom;
            }
        }
        
        return null;
    }
}

class Calendar {
    
    List<Meeting> bookedMeetings = new ArrayList<>();
    
    public void addMeeting(Meeting meetingDO) {
        bookedMeetings.add(meetingDO);
    }
    
    public void removeMeeting(Meeting meeting) {
        bookedMeetings.remove(meeting);
    }
    
    public boolean checkMeetingSlotAvailability(Interval requestedInterval) {
        
        for(Meeting meeting : bookedMeetings) {
            
            if(requestedInterval.getStartTime() >= meeting.getStartTime() && requestedInterval.getEndTime() <= meeting.getEndTime() ||
                requestedInterval.getEndTime() >=  meeting.getStartTime() && requestedInterval.getEndTime() >= meeting.getEndTime()) {
                return false;
            }
        }
        
        return true;
    }
}

class MeetingSchedular {
    
    MeetingRoomManager meetingRoomManager;
    Notification notifier;
    List<Meeting> meetingHistoryList = new ArrayList<>();
    
    public MeetingSchedular() {
        meetingRoomManager = new MeetingRoomManager();
        notifier = new Notification();
    }
    
    public void setMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoomManager.addMeetingRoom(meetingRoom);
    }
    
    public List<MeetingRoom> getAvailableRooms(Interval requestedInterval) {
        return meetingRoomManager.checkMeetingRoomAvailability(requestedInterval);
    }
    
    public void scheduleMeeting(Meeting meetingDO) {
        
        List<User> partipicants = meetingDO.getParticipants();
        meetingRoomManager.addMeetingtoCalendar(meetingDO);
        notifier.notifyAllPartipicants(partipicants, meetingDO);
    }
    
    public void cancelMeeting(String meetingId) {
        Meeting meeting = getMeetingDetails(meetingId);
        List<User> partipicants = meeting.getParticipants();
        meetingRoomManager.removeMeetingFromCalendar(meeting);
        notifier.notifyAllPartipicants(partipicants, meeting);
    }
    
    public Meeting getMeetingDetails(String meetingId) {
        for(Meeting meeting : meetingHistoryList) {
            if(meeting.getMeetingId() == meetingId) {
                return meeting;
            }
        }
        return null;
    }
}

class Notification {
    
    public void notifyAllPartipicants(List<User> partipicants, Meeting meetingDO) {
        for(User user : partipicants) {
            user.sendEmail(meetingDO);
        }
    }
    
    // in future more features can be added here
}

class Location {
    String towerName;
    int floorNumber;
    String meetingRoomName;
    
    public Location(String towerName, String meetingRoomName, int floorNumber) {
        this.towerName = towerName;
        this.meetingRoomName = meetingRoomName;
        this.floorNumber = floorNumber;
    }
}

public class MeetingSchedularSystem {
	public static void main(String[] args) {
	    
		
		// create users
		User user1 = new User("alex", "alex@gmail.com", "9886858544");
		User user2 = new User("addy", "addy@gmail.com", "6554646455");
		User user3 = new User("philip", "philip@gmail.com", "265645435");
		
		// create meeting rooms
		MeetingRoom meetingRoom1 = new MeetingRoom(25, new Location("tower1", "skyer", 10));
		MeetingRoom meetingRoom2 = new MeetingRoom(40, new Location("tower2", "skyer", 12));
		
		// create meeting schedular
	    MeetingSchedular meetingSchedular = new MeetingSchedular();
	    meetingSchedular.setMeetingRoom(meetingRoom1);
	    meetingSchedular.setMeetingRoom(meetingRoom2);
		
		// create a meeting interval where start time - 4pm, end time - 5pm, partipicants - 15
		Interval requestedInterval = new Interval(4, 5, 15);
		
		// check for available meeting rooms
		List<MeetingRoom> availableMeetingRooms = meetingSchedular.getAvailableRooms(requestedInterval);
		
		// select a meeting roomfor meeting
		MeetingRoom selectedMeetingRoom = availableMeetingRooms.get(0);
		
		// create meeting
		Meeting meetingDO = new Meeting();
		meetingDO.setMeetingTopic("product launch discussion");
		meetingDO.setStartTime(4);
		meetingDO.setEndTime(5);
		meetingDO.setCapacity(15);
		meetingDO.setMeetingRoomId(selectedMeetingRoom.getMeetingRoomId());
		meetingDO.addParticipant(user2);
		meetingDO.addParticipant(user3);
		
		// book the meeting
		meetingSchedular.scheduleMeeting(meetingDO);
	}
}
