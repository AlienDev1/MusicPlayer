package comqxe.example.user1.tri_post;

public class Song {

    /*
    This Class is used to model the data that will be
    obtained for the songs track. Below are the variables to
    Hold Specified information. A constructor method will be
    declare to run instantiate the global variables.
     */

    // Variable to hold song information
    private long id;
    private String title;
    private String artist;

    // Constructor method
    public Song(long songID, String songTitle, String songArtist) {

        id = songID;
        title = songTitle;
        artist = songArtist;

        /*
        Here you will enter additional items that you may
        want to obtain about the track. Such as pictures etc...
         */
    }

    // Get Methods to pull song information

    // Pulls song subScript
    public long getID(){

        return id;
    }
    // Pulls song title
    public String getTitle(){

        return title;
    }
    // pulls artist of the song
    public String getArtist(){

        return artist;
    }


}
