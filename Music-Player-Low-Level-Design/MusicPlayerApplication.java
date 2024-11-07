import java.util.*;

enum SubscriptionType {
    MONTHLY,
    WEEKLY,
    YEARLY
}

enum SongPlayerStatus {
    PLAYING,
    PAUSE,
    SEEK,
    NOT_PLAYED
}

class Song {
    String songId;
    String songName;
    String singerName;
    String releasedDate;
    String albumName;
    
    public Song(String songName, String singerName, String releasedDate, String albumName) {
        this.songId = "1234";
        this.songName = songName;
        this.singerName = singerName;
        this.releasedDate = releasedDate;
        this.albumName = albumName;
    }
}

class Singer {
    String singerId;
    String singerName;
    List<Album> singerAlbum;
    
    public Singer(String singerName, List<Album> singerAlbum) {
        this.singerId = "1234";
        this.singerName = singerName;
        this.singerAlbum = singerAlbum;
    }
}

class Album {
    String albumId;
    String albumName;
    List<Song> albumSongs;
    
    public Album(String albumName, List<Song> albumSongs) {
        this.albumId = "1234";
        this.albumName = albumName;
        this.albumSongs = albumSongs;
    }
}

class MusicLibrary {
    private static MusicLibrary instance;
    List<Song> allSongs;
    List<Album> allAlbums;
    List<Singer> allSingers;
    
    private MusicLibrary() {
        allSongs = new ArrayList<>();
        allSingers = new ArrayList<>();
        allAlbums = new ArrayList<>();
    }

    public static synchronized MusicLibrary getInstance() {
        if (instance == null) {
            instance = new MusicLibrary();
        }
        return instance;
    }
    
    public void addSong(Song song) {
        allSongs.add(song);
    }
    
    public void addAlbum(Album album) {
        allAlbums.add(album);
    }
    
    public void addSingers(Singer singer) {
        allSingers.add(singer);
    }

    public List<Song> searchSongs(SongFilter songFilter) {
        Set<Song> uniqueFilteredSongs = new HashSet<>();  // Use a Set to ensure uniqueness
    
        for (Song song : allSongs) {
            boolean matches = false;
    
            // Check if the song matches the album name
            if (songFilter.getAlbumName() != null && song.albumName.equalsIgnoreCase(songFilter.getAlbumName())) {
                matches = true;
            }
    
            // Check if the song matches the singer name
            if (songFilter.getSingerName() != null && song.singerName.equalsIgnoreCase(songFilter.getSingerName())) {
                matches = true;
            }
    
            // Check if the song matches the song name
            if (songFilter.getSong() != null && song.songName.equalsIgnoreCase(songFilter.getSong())) {
                matches = true;
            }
    
            // Add song to filtered set if any criteria match
            if (matches) {
                uniqueFilteredSongs.add(song);
            }
        }
    
        return new ArrayList<>(uniqueFilteredSongs);
    }
}

class SongFilter {
    private String albumName;
    private String singerName;
    private String song;
    private String genericName;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }
}

class User {
    String userId;
    SubscriptionType subscriptionType;
    String userName;
    String emailId;
    List<Playlist> userPlaylist;
    SongHistory songHistory;
    
    public User(String userName, String emailId, SubscriptionType subscriptionType) {
        this.userId = "1234";
        this.userName = userName;
        this.emailId = emailId;
        this.subscriptionType = subscriptionType;
        this.userPlaylist = new ArrayList<>();
        this.songHistory = new SongHistory(20);
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void createPlaylist(String playlistName) {
        Playlist playlist = new Playlist(playlistName);
    }
    
    public void addSongToPlaylist(String playlistId, Song song) {
        Playlist playlist = getPlaylist(playlistId);
        if(playlist != null) {
            playlist.addSong(song);
        }
    }
    
    public void removeSongToPlaylist(String playlistId, Song song) {
        Playlist playlist = getPlaylist(playlistId);
        if(playlist != null) {
            playlist.removeSong(song);
        }
    }
    
    public void removePlaylist(String playlistId) {
        Playlist removePlaylist = getPlaylist(playlistId);
        
        if(removePlaylist != null) {
            userPlaylist.remove(removePlaylist);
        }
    }

    public Playlist getPlaylist(String playlistId) {
        for(Playlist playlist : userPlaylist) {
            if(playlist.playlistId == playlistId) {
                return playlist;
            }
        }
        
        return null;
    }
    
    public void updateSongHistory(Song song) {
        songHistory.addSongToHistory(song);
    }
    
    public void updateSubscription(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}

class UserManagement {
    private static UserManagement instance;
    List<User> allUsers;
    
    private UserManagement() {
        allUsers = new ArrayList<>();
    }

    public static synchronized UserManagement getInstance() {
        if (instance == null) {
            instance = new UserManagement();
        }
        return instance;
    }
    
    public User createUser(String userName, String emailId, SubscriptionType subscriptionType) {
        User user = new User(userName, emailId, subscriptionType);
        allUsers.add(user);
        return user;
    }
    
    public void updateSubscription(String userId, SubscriptionType subscriptionType) {
        User user = getUser(userId);
        user.updateSubscription(subscriptionType);
    }

    public void removeUser(String userId) {
        User user = getUser(userId);
        allUsers.remove(user);
    }

    public User getUser(String userId) {
        for(User user : allUsers) {
            if(user.getUserId() == userId) {
                return user;
            }
        }
        
        return null;
    }
}

class Playlist {
    String playlistId;
    String playlistName;
    String creationDate;
    List<Song> playlistSongs;
    
    public Playlist(String playlistName) {
        this.playlistName = playlistName;
        this.creationDate = "";
        playlistSongs = new ArrayList<>();
    }
    
    public void addSong(Song song) {
        playlistSongs.add(song);
    }
    
    public void removeSong(Song song) {
        playlistSongs.remove(song);
    }
}

class SongHistory {
    Deque<Song> allPlayedSongs = new ArrayDeque<>();
    int historyKeepDate = 50;
    
    public SongHistory(int historyKeepDate) {
        this.historyKeepDate = historyKeepDate;
    }
    
    public void addSongToHistory(Song song) {
        allPlayedSongs.addFirst(song);
        
        if(allPlayedSongs.size() == historyKeepDate) {
            allPlayedSongs.removeLast();
        }
    }
    
    public void clearSongsHistory() {
        allPlayedSongs.clear();
    }
}

class MusicPlayer {
    Song song;
    SongPlayerStatus songPlayerStatus;
    
    public MusicPlayer(Song song) {
        this.song = song;
        songPlayerStatus = SongPlayerStatus.PLAYING;
    }
    
    public void playMusic() {
        songPlayerStatus = SongPlayerStatus.PLAYING;
    }
    
    public void pauseMusic() {
        songPlayerStatus = SongPlayerStatus.PAUSE;
    }
    
    public void seekMusic() {
        songPlayerStatus = SongPlayerStatus.SEEK;
    }
}

class MusicRecomendationService {
    private static MusicRecomendationService instance;
    private List<Song> recomendedSongs;
    private MusicLibrary musicLibrary;

    private MusicRecomendationService() {
        recomendedSongs = new ArrayList<>();
    }

    public static synchronized MusicRecomendationService getInstance() {
        if (instance == null) {
            instance = new MusicRecomendationService();
        }
        return instance;
    }

    public List<Song> recomendSongs(User user, MusicLibrary musicLibrary) {
        
        this.musicLibrary = musicLibrary;
        
        // Clear previous recommendations
        recomendedSongs.clear();
        
        // Get user song history
        Deque<Song> songHistory = user.songHistory.allPlayedSongs;
        
        // Recommendation based on user's play history
        for (Song song : songHistory) {
            recommendSimilarSongs(song);
        }

        // If the list is still empty, recommend popular songs or new releases
        if (recomendedSongs.isEmpty()) {
            recommendPopularSongs();
        }

        return new ArrayList<>(recomendedSongs);
    }

    private void recommendSimilarSongs(Song song) {
        // Fetch songs from the same album
        for (Album album : musicLibrary.allAlbums) {
            if (album.albumName.equals(song.albumName)) {
                for (Song similarSong : album.albumSongs) {
                    if (!recomendedSongs.contains(similarSong) && !similarSong.songId.equals(song.songId)) {
                        recomendedSongs.add(similarSong);
                    }
                }
            }
        }

        // Fetch songs from the same singer
        for (Singer singer : musicLibrary.allSingers) {
            if (singer.singerName.equals(song.singerName)) {
                for (Album album : singer.singerAlbum) {
                    for (Song similarSong : album.albumSongs) {
                        if (!recomendedSongs.contains(similarSong) && !similarSong.songId.equals(song.songId)) {
                            recomendedSongs.add(similarSong);
                        }
                    }
                }
            }
        }
    }

    private void recommendPopularSongs() {
        // This is a placeholder logic for popular songs
        // In a real scenario, you might sort by play count or use other metrics
        recomendedSongs.addAll(musicLibrary.allSongs.subList(0, Math.min(10, musicLibrary.allSongs.size())));
    }
}

class MusicStreamingFactory {
    MusicRecomendationService musicRecomendationService;
    MusicLibrary musicLibrary;
    UserManagement userManagement;
    
    public MusicRecomendationService getMusicRecomendationService() {
        return musicRecomendationService.getInstance();
    }
    
    public MusicLibrary getMusicLibrary() {
        return musicLibrary.getInstance();
    }
    
    public UserManagement getUserManagement() {
        return userManagement.getInstance();
    }
}

public class MusicPlayerApplication {
	public static void main(String[] args) {
		
		MusicStreamingFactory musicStreamingFactory = new MusicStreamingFactory();
		
		// create user
		User user = musicStreamingFactory.getUserManagement().createUser("user1", "user1@gmail.com", SubscriptionType.MONTHLY);
		
		// create songs
		Song song1 = new Song("song1", "singer1", "", "album1");
		Song song2 = new Song("song2", "singer1", "", "album1");
		Song song3 = new Song("song3", "singer2", "", "album2");
		Song song4 = new Song("song4", "singer2", "", "album2");
		
		// create album
		List<Song> album1Songs = new ArrayList<>();
		album1Songs.add(song1);
		album1Songs.add(song2);
		Album album1 = new Album("album1", album1Songs);
		
		List<Song> album2Songs = new ArrayList<>();
		album2Songs.add(song3);
		album2Songs.add(song4);
		Album album2 = new Album("album2", album2Songs);
		
		// create Singer
		List<Album> singer1Album = new ArrayList<>();
		singer1Album.add(album1);
		Singer singer1 = new Singer("singer4", singer1Album);
		
		List<Album> singer2Album = new ArrayList<>();
		singer2Album.add(album2);
		Singer singer2 = new Singer("singer4", singer2Album);
		
		musicStreamingFactory.getMusicLibrary().addSong(song1);
		musicStreamingFactory.getMusicLibrary().addSong(song2);
		musicStreamingFactory.getMusicLibrary().addSong(song3);
		musicStreamingFactory.getMusicLibrary().addSong(song4);
		
		musicStreamingFactory.getMusicLibrary().addAlbum(album1);
		musicStreamingFactory.getMusicLibrary().addAlbum(album2);
		
		musicStreamingFactory.getMusicLibrary().addSingers(singer1);
		musicStreamingFactory.getMusicLibrary().addSingers(singer2);
		
		// create song search filter
		SongFilter songFilter = new SongFilter();
		songFilter.setAlbumName("album1");
		songFilter.setSingerName("singer1");
		songFilter.setSong("song1");
		
		List<Song> allSearchedSongs = musicStreamingFactory.getMusicLibrary().searchSongs(songFilter);
		System.out.println("all searched songs are fetched : ");
		for(Song song : allSearchedSongs) {
		    System.out.println(song.songName);
		}
		
		// user selects song1
		user.updateSongHistory(allSearchedSongs.get(0));
		
		// user play the song
		MusicPlayer musicPlayer = new MusicPlayer(allSearchedSongs.get(0));
		
		// user pause the song
		musicPlayer.pauseMusic();
		
		List<Song> allRecomendedSongs = musicStreamingFactory.getMusicRecomendationService().recomendSongs(user, musicStreamingFactory.getMusicLibrary());
		System.out.println("\nall recomended songs are fetched : ");
		for(Song song : allRecomendedSongs) {
		    System.out.println(song.songName);
		}
		
	}
}
