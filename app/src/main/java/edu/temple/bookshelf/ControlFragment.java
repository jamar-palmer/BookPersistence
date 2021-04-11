package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ResourceBundle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    private TextView textView;
    private Button play;
    private Button pause;
    private Button stop;
    private SeekBar seekBar;

    private int currentId = -1;
    private int currentProgress;
    private String bookTitle;
    private boolean paused = false;

    public ControlFragment() {

    }

    public static ControlFragment newInstance(String param1) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", currentId);
        outState.putString("bookT", bookTitle);
        outState.putInt("prog", currentProgress);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt("id");
            bookTitle = savedInstanceState.getString("bookT");
            currentProgress = savedInstanceState.getInt("prog");
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(textView != null && currentId >= 0){
            nowPlaying();
            setProgress(currentProgress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View l = inflater.inflate(R.layout.fragment_control, container, false);

        textView = l.findViewById(R.id.txtPlaying);
        play = l.findViewById(R.id.btnPlay);
        pause = l.findViewById(R.id.btnPause);
        stop = l.findViewById(R.id.btnStop);
        seekBar = l.findViewById(R.id.seekBar);



        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = ((MainActivity) getActivity()).getBookItem();

                if(temp < 0){
                    //do nothing
                }else{
                    ((ControlFragmentInterface) getActivity()).playAudio(temp+1);
                    bookTitle = ((MainActivity) getActivity()).getBookName();
                    nowPlaying();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ControlFragmentInterface) getActivity()).pauseAudio();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ControlFragmentInterface) getActivity()).stopAudio();
                clearSelection();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               if(fromUser) {
                   ((ControlFragmentInterface) getActivity()).seekAudio(progress);
                   currentProgress = progress;
               }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        return l;
    }

    public void storeId(int id){
        this.currentId = id;
    }

    public void storeBookTitle(String naming){
        this.bookTitle = naming;
    }

    public void clearSelection(){
        textView.setText(" ");
    }

    public void nowPlaying(){
        textView.setText("Now Playing: " + bookTitle);
    }

    public void setProgress(int progress){
        seekBar.setProgress(progress);
    }

    public void switchText(){

            if (paused) {
                pause.setText("PAUSE");
            } else {
                pause.setText("UNPAUSE");
            }
            paused = !paused;

    }


    interface ControlFragmentInterface{
        public void playAudio(int id);
        public void pauseAudio();
        public void stopAudio();
        public void seekAudio(int position);
    }

}