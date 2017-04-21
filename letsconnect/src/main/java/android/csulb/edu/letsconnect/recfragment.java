package android.csulb.edu.letsconnect;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kiran on 4/16/2017.
 */

public class recfragment extends Fragment {
    Button play,stop,record,sendthis;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    ChatService chatService;
    MainActivity mainActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recfrag, container, false);


    //

        play=(Button) getView().findViewById(R.id.button3);
        stop=(Button) getView().findViewById(R.id.button2);
        record=(Button) getView().findViewById(R.id.button);
        sendthis=(Button) getView().findViewById(R.id.button4);

        stop.setEnabled(false); // to disable button , it will enable after record button pressed
        play.setEnabled(false);
        sendthis.setEnabled(false);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/nlabz-recording"+ts+".3gp";

        myAudioRecorder=new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getActivity().getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;

                stop.setEnabled(false);
                play.setEnabled(true);
                sendthis.setEnabled(true);
                Toast.makeText(getActivity().getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getActivity().getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });

        return view;

    //
    }

    public String getStringFile() {
        InputStream inputStream = null;
        mainActivity = new MainActivity();
        chatService = new ChatService(getActivity(), mainActivity.handler);
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(outputFile);

            byte[] buffer = new byte[100240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
            encodedFile=encodedFile+"RECZZ";
            mainActivity.sendMessage(encodedFile);
           // chatService.write(encodedFile.getBytes());
            Intent back = new Intent(getActivity(),MainActivity.class);
            startActivity(back);
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }


    public void sendfile(View view)
    {
        getStringFile();
    }



}
