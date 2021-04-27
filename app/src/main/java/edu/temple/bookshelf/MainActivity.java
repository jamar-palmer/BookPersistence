package edu.temple.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentInterface, ControlFragment.ControlFragmentInterface {

    // global variables
    BookList bookList;
    boolean container2present;
    BookDetailsFragment bookDetailsFragment;
    ControlFragment controlFragment;
    int placeholder=0;
    boolean contain3 = false;

    AudiobookService.MediaControlBinder audiobookService;
    AudiobookService.BookProgress bookProgress;
    boolean isConnected;

    String internalFile = "myaudiofile";
    File file;
    SharedPreferences preferences;
    int currentBookSave;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audiobookService = ((AudiobookService.MediaControlBinder) service);
            isConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnected= false;
        }
    };

    Handler audioHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if(isConnected) {
                bookProgress = (AudiobookService.BookProgress) msg.obj;
                if (bookProgress != null) {
                    // bookList.get(bookProgress.getBookId() -1).setDuration(bookProgress.getProgress());
                    controlFragment.setProgress(bookProgress.getProgress());
                }
            }
                return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookList = new BookList();

        file = new File(getFilesDir(), internalFile);
        preferences = getPreferences(MODE_PRIVATE);
        currentBookSave = preferences.getInt("currentBook", -1);

        Intent launchIntent = new Intent(MainActivity.this, edu.temple.audiobookplayer.AudiobookService.class);
        launchIntent.putExtra("song", 10);
        bindService(launchIntent, serviceConnection, BIND_AUTO_CREATE);

        //checks if activity is in portrait or landscape
        container2present = findViewById(R.id.container_2) != null;

        //if in landscape mode, 2nd fragment is instatiated and filled
        if(container2present){
            bookDetailsFragment = new BookDetailsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_2, bookDetailsFragment).commit();
        }

        if(!contain3) {
            controlFragment = new ControlFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container_3, controlFragment).addToBackStack(null).commit();
            contain3 = true;
        }
    }

    //storing of the file locally with id
    public void storeFile(int booknum){
        String downloadApi = "https://kamorris.com/lab/audlib/download.php?id=" + booknum;

        try {
            //Uri uri = Uri.parse(downloadApi);
            FileOutputStream outputStream = new FileOutputStream(file);
         //outputStream.write(uri.toString().getBytes());
            outputStream.write(downloadApi.getBytes());
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentBook", booknum);
        editor.apply();
        currentBookSave = booknum;
    }

    public void searchClicked(View view){
            Intent launchIntent = new Intent(MainActivity.this, BookSearchActivity.class);
            startActivityForResult(launchIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //load all extras in their own arraylists
                ArrayList<Integer> ids  = data.getIntegerArrayListExtra("id");
                ArrayList<String> coverURL = data.getStringArrayListExtra("pictures");
                ArrayList<String> title = data.getStringArrayListExtra("title");
                ArrayList<String> author = data.getStringArrayListExtra("author");

                //clear the list for rebinding upon resume
                bookList.clear();

                //loop through list and create new books while adding them to list
                for (int i = 0; i < ids.size(); i++ ){
                    Book temp = new Book(title.get(i), author.get(i));
                    temp.setId(ids.get(i));
                    temp.setCoverURL(coverURL.get(i));
                    bookList.add(temp);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookListFragment.newInstance(bookList)).addToBackStack(null).commit();
            }
            if(resultCode==RESULT_FIRST_USER){
                //if the search is not in the JSON file, return blank
                bookList.clear();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookListFragment.newInstance(bookList)).addToBackStack(null).commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(audiobookService != null)
            audiobookService.setProgressHandler(audioHandler);
    }

    @Override
    public void itemClicked(int position) {

        //position is given to global variable
        placeholder = position;
        controlFragment.storeId(position);
        controlFragment.storeBookTitle(bookList.get(position).getTitle());

        //screen will transfer if in portrait mode, otherwise the detail view will be changed
        if(!container2present) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookDetailsFragment.newInstance(bookList.get(position))).addToBackStack(null).commit();
        }else{
            bookDetailsFragment.displayBook(bookList.get(position));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("place", placeholder);
        outState.putParcelable("parcel", bookList);
        outState.putBoolean("contain3", contain3);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //save place for current search item
        int placing = savedInstanceState.getInt("place");
        placeholder = placing;
        bookList = savedInstanceState.getParcelable("parcel");
        contain3 = savedInstanceState.getBoolean("contain3");

        if(isConnected){
            audiobookService.play(placeholder);
        }
        //check if booklist has been loaded yey
        if(bookList.size() != 0) {

            controlFragment.storeId(placeholder);
            controlFragment.storeBookTitle(bookList.get(placeholder).getTitle());
            if (!container2present) {
                //operation for a single portrait view
                getSupportFragmentManager().beginTransaction().replace(R.id.container_1, BookDetailsFragment.newInstance(bookList.get(placing))).addToBackStack(null).commit();

            } else {
                //operation for a landscape view
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
                bookDetailsFragment.displayBook(bookList.get(placing));

            }
        }
    }

    public int getBookItem(){
        return placeholder;
    }

    public String getBookName(){
        if(bookList.isEmpty()){
            return "blank";
        }else {
            return bookList.get(placeholder).getTitle();
        }
    }

    public boolean playing(){
        return audiobookService.isPlaying();
    }

    @Override
    public void playAudio(int id) {
        //check if previously downloaded
        if(file.exists() && currentBookSave == id){
            //play from stored file or download current file
            audiobookService.play(file,0 );
            audiobookService.setProgressHandler(audioHandler);

        }else{
            audiobookService.play(id);
            audiobookService.setProgressHandler(audioHandler);
            storeFile(id);
        }
    }

    @Override
    public void pauseAudio() {
        controlFragment.switchText();
        audiobookService.pause();
    }

    @Override
    public void stopAudio() {
        audiobookService.stop();
    }

    public void seekAudio(int position) {
        if(audiobookService != null && isConnected) {
            audiobookService.seekTo(position);

        }
    }
}