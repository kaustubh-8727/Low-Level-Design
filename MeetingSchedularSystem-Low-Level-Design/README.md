# Meeting Scheduler System

## Overview

The Meeting Scheduler System is a Java-based application designed to facilitate the scheduling and management of meetings within an organization. It provides functionalities to manage users, meeting rooms, calendars, and notifications, ensuring that meetings are organized efficiently without conflicts.

## Components

### 1. User
- **Attributes**: `userName`, `emailId`, `contactNumber`
- **Responsibilities**:
  - Represents an individual participant in a meeting.
  - Receives notifications about scheduled or canceled meetings.

### 2. Meeting
- **Attributes**: `meetingId`, `meetingTopic`, `meetingRoomId`, `startTime`, `endTime`, `capacity`, `participants`
- **Responsibilities**:
  - Represents the details of a scheduled meeting.
  - Stores information such as topic, time, capacity, and participants.

### 3. Meeting Room
- **Attributes**: `meetingRoomId`, `capacity`, `meetingRoomAddress`
- **Responsibilities**:
  - Represents a physical location where meetings are held.
  - Stores capacity and location details.

### 4. Calendar
- **Responsibilities**:
  - Maintains a list of booked meetings for a specific meeting room.
  - Checks for availability of meeting slots based on requested intervals.

### 5. Notification
- **Responsibilities**:
  - Handles the communication of meeting details to participants.
  - Sends email notifications when meetings are scheduled or canceled.

### 6. Meeting Scheduler
- **Responsibilities**:
  - Manages the overall scheduling of meetings.
  - Interfaces with `MeetingRoomManager` to find available rooms and schedule meetings.
  - Ensures participants are notified of meeting details.

### 7. Meeting Room Manager
- **Responsibilities**:
  - Manages the creation, removal, and scheduling of meetings in meeting rooms.
  - Checks room availability based on requested intervals and room capacity.
  - Handles the association of meetings with rooms in the calendar.

### 8. Location
- **Attributes**: `towerName`, `meetingRoomName`, `floorNumber`
- **Responsibilities**:
  - Represents the physical address of a meeting room.

## Functionality

- **Meeting Scheduling**:
  - Users can schedule meetings by specifying the meeting details such as topic, time, and participants.
  - The system checks for available meeting rooms that meet the capacity requirements and schedules the meeting in a suitable room.

- **Conflict Management**:
  - The Calendar component ensures that no overlapping meetings are scheduled in the same room.

- **Notifications**:
  - Once a meeting is scheduled or canceled, all participants receive email notifications with the meeting details.

- **Meeting Cancellation**:
  - Users can cancel meetings, and the system will update the calendar and notify participants.

## Usage

1. **Create Users**: Initialize users with their respective details.
2. **Create Meeting Rooms**: Define meeting rooms with their capacity and location.
3. **Schedule Meetings**: Use the `MeetingScheduler` to book a meeting based on the participants, time, and room availability.
4. **Receive Notifications**: Participants will be notified via email about their meeting details.

## Future Enhancements

- **Recurring Meetings**: Add support for recurring meetings.
- **Advanced Notifications**: Integrate additional notification channels like SMS or in-app alerts.
- **Room Equipment Management**: Include features to manage and book meeting room equipment like projectors and conference call setups.

## Conclusion

The Meeting Scheduler System provides a structured and efficient way to manage meetings within an organization, ensuring that resources are utilized effectively and participants are kept informed. The design is modular, allowing for easy future enhancements and scalability.

## Sample Output

```
=========================================
          Meeting Notification           
=========================================
Sending email to: addy@gmail.com
-----------------------------------------
            Meeting Details              
-----------------------------------------
Meeting ID      : MEETING-F345ABB1
Topic           : product launch discussion
Meeting Room ID : ROOM-EA2B1E33
Start Time      : 4 PM
End Time        : 5 PM
Capacity        : 15
-----------------------------------------
            Participants List            
-----------------------------------------
- addy (addy@gmail.com)
- philip (philip@gmail.com)
=========================================




=========================================
          Meeting Notification           
=========================================
Sending email to: philip@gmail.com
-----------------------------------------
            Meeting Details              
-----------------------------------------
Meeting ID      : MEETING-F345ABB1
Topic           : product launch discussion
Meeting Room ID : ROOM-EA2B1E33
Start Time      : 4 PM
End Time        : 5 PM
Capacity        : 15
-----------------------------------------
            Participants List            
-----------------------------------------
- addy (addy@gmail.com)
- philip (philip@gmail.com)
=========================================
