package comqxe.example.user1.tri_post;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import comqxe.example.user1.tri_post.MusicService.MusicBinder;






public class MainActivity extends AppCompatActivity  {

    // Here we declared the variable where the songs will be stored (songID)
    private ArrayList <Song> songList;
    private ListView songView;

    // Instance variables to pull from MusicService Class
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

               // statement pulls in the songs
        songView = (ListView)findViewById(R.id.song_list);

        // array list is being instantiated
        songList = new ArrayList<Song>();

        // We are calling the method to obtain the information for the song

        getSongList();

        // display songs Alphabetically
        Collections.sort(songList, new Comparator<Song>(){

            public int compare(Song a, Song b){

                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
    }




    // connect to the MusicService Class to be controlled
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicBinder binder = (MusicBinder)service;

            // get MusicService
            musicSrv = binder.getService();

            // pass ArrayList
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            musicBound = false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //menu item selected
        switch (item.getItemId()) {

            case R.id.action_shuffle:

                //shuffle
                break;

            case R.id.action_end:

                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {

        super.onStart();

        if(playIntent == null){

            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }



    public void songPicked(View view){

        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
    }


    // Method pulls the audio information
    public void getSongList() {

        /*
        Here we are using the ContentResolver class
        to receive the URI. Cursor Class is used
        to Search for the files.
        */
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        // this is to confirm that the data is Correct for use of the Media Player
        if(musicCursor!= null && musicCursor.moveToFirst()){

            //IF Statement above is true then usage of the data is now being simplified into a variable
            // To be placed into a parameter later
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);

            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);

            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            //adding songs to list here
            // I am using a DoWhile Loop to input the data
            // The Variables are being passed as parameters
            do {
                long thisId = musicCursor.getLong(idColumn);

                String thisTitle = musicCursor.getString(titleColumn);

                String thisArtist = musicCursor.getString(artistColumn);

                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {

        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }


}
