package edu.temple.bookshelf;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

                if(currentId < 0){
                    //do nothing
                }else{
                    ((ControlFragmentInterface) getActivity()).playAudio(currentId+1);
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
                if(progress <1){
                    progress = 0;
                }
                    ((ControlFragmentInterface) getActivity()).seekAudio(progress);

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