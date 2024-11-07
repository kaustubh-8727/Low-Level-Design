# Online Music Streaming Service Design

An in-depth Java implementation of an online music streaming service, similar to Spotify, built with key components like song management, music library, search services, user playlists, and recommendation systems. This service allows users to play, search, and organize their favorite music seamlessly.

## Overview

### Key Modules

1. **Song**: Represents the essential details of a song, such as title, singer, release date, and album.
2. **Music Library**: Manages all songs, albums, and singers, acting as a central repository for the service.
3. **Search Services**: Provides classes to search songs by category, title, singer, or album.
4. **User Management**: Manages user creation, registration, login, and subscription.
5. **Playlist**: Enables users to create, manage, and organize custom playlists.
6. **History**: Tracks song history for each user, storing recently played songs.
7. **Music Player**: Controls song playback with states like play, pause, and seek.
8. **Recommendation Service**: Recommends songs to users based on their history and popular songs.
9. **User Subscription Management**: Handles different subscription plans: Monthly, Weekly, and Yearly.

### Enum Classes
- **SubscriptionType**: Defines subscription durations.
- **SongPlayerStatus**: Specifies song statuses such as PLAYING, PAUSE, SEEK, or NOT_PLAYED.

## Detailed Class Descriptions

### 1. `Song`
Manages information about each song, including:
- `songId`: Unique identifier.
- `songName`: Song title.
- `singerName`: Artist name.
- `releasedDate`: Release date.
- `albumName`: Associated album name.

### 2. `Singer` and `Album`
- **Singer**: Manages each singer’s information, including their albums.
- **Album**: Contains album details and a list of songs within each album.

### 3. `MusicLibrary`
Singleton class that maintains:
- All songs, albums, and singers.
- `searchSongs`: Allows searching based on filters like album name, singer name, or song title.

### 4. `User`
Stores and manages user details:
- `userId`, `userName`, `emailId`, `subscriptionType`: Basic user information.
- `userPlaylist`: User-created playlists.
- `songHistory`: Tracks song history.
- Methods for creating and managing playlists, adding/removing songs from playlists, and updating subscriptions.

### 5. `UserManagement`
Singleton class that handles:
- User creation, subscription updates, and user removal.
- `getUser`, `createUser`, `updateSubscription`: Core functionalities for managing users.

### 6. `Playlist`
Handles a list of songs for each user’s playlist:
- `addSong` and `removeSong` methods to manage playlist content.

### 7. `SongHistory`
Manages song history with a predefined maximum history size.

### 8. `MusicPlayer`
Manages playback functions:
- `playMusic`, `pauseMusic`, `seekMusic`: Control song playback states.

### 9. `MusicRecommendationService`
Singleton service that recommends songs to users based on:
- User song history.
- Similar songs from the same artist or album.
- Popular or new releases.

### 10. `MusicStreamingFactory`
Factory class providing instances of `MusicRecommendationService`, `MusicLibrary`, and `UserManagement`.
