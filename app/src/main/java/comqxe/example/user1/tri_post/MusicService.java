package comqxe.example.user1.tri_post;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;


/*
this class is related to the Services call in the manifest
Here we will construct the call to play the songs when selected.
We are implementing the MediaPlayer Class here.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private final IBinder musicBind = new MusicBinder();

    /*
    A method call must be declared when implementing these classes
    int he Parent MusicPlayer class.
     */

    //media player
    private MediaPlayer player;

    // pulling the ArrayList from the Song Class
    private ArrayList<Song> songs;

    //current position
    private int songPosn;


    // Here we create the functionality of the buttons
    public void onCreate(){

        //create the service
        super.onCreate();

        //initialize position
        songPosn = 0;

        //create player
        player = new MediaPlayer();

        initMusicPlayer();
    }

    //sets player properties
    public void initMusicPlayer(){

        // Allows the app to continue while the user exit the app
        // this is referenced in Manifest Permission
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);


        // This prepares class as a listener for MusicPlayer Instance
        player.setOnPreparedListener(this);

        // Stage when the music has completed the playback
        player.setOnCompletionListener(this);

        // Informs when a song is done and when error is thrown
        player.setOnErrorListener(this);

    }


    // Method that plays the song
    public void playSong(){

        player.reset();

        //get song
        Song playSong = songs.get(songPosn);

        //get id
        long currSong = playSong.getID();

        //set uri
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);

        try{

            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){

            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public void setSong(int songIndex){

        songPosn = songIndex;
    }


    @Override
    public IBinder onBind(Intent intent) {

        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){

        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {

        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
    }

    public void setList(ArrayList<Song> theSongs){

        songs = theSongs;
    }

    public class MusicBinder extends Binder {

        MusicService getService() {

            return MusicService.this;
        }
    }

}
