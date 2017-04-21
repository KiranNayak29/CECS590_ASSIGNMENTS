package android.csulb.edu.letsconnect.Wifi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.csulb.edu.letsconnect.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.body.AsyncHttpRequestBody;
import com.koushikdutta.async.http.body.JSONObjectBody;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.support.design.widget.FloatingActionButton;

public class LandingActivity extends AppCompatActivity {
    private static String myIP;
    MediaPlayer mediaPlayer;


    //REC
    Button play,stop,record,sendthis;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private ListView listView;
    private ClientAdapter clientadapter;
    private static final String TAG="LandingActivity";
    private ProgressDialog pDialog;
    private String otherUsernameWhenWeCall;
    private int callState;
    private static final int CALL_STATE_IN_CALL=1;
    private static final int CALL_STATE_AVAILABLE=2;
    String ImagePath="";
    //drawer
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    WifiApManager wifiApManager;
    wifiMainActivity mains;
    static String RW;
    private Button btnSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        play=(Button)findViewById(R.id.button3);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button);
        sendthis=(Button)findViewById(R.id.button4);
        Button sendtext,sendImg,sendrec;
        sendtext=(Button)findViewById(R.id.btnSend);
        sendImg=(Button)findViewById(R.id.btnSend1);
        sendrec=(Button)findViewById(R.id.btnSend2);
        play.setEnabled(false);
        stop.setEnabled(false);
        record.setEnabled(false);
        sendthis.setEnabled(false);
        sendtext.setEnabled(false);
        sendImg.setEnabled(false);
        sendrec.setEnabled(false);
        callState=CALL_STATE_AVAILABLE;
        ListView listView= (ListView) findViewById(R.id.landingListView);

        wifiApManager = new WifiApManager(this);
         ClientAdapter clientadapter = new ClientAdapter(LandingActivity.this,wifiApManager.results);
        listView.setAdapter(clientadapter);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String myUsername=prefs.getString(Util.KEY_PREFS_EMAIL,"user@gmail.com");
        ((TextView)findViewById(R.id.drawerEmail)).setText(myUsername);
        //adapter=new HomeAdapter(this,R.layout.main_listview_row,DatabaseHelper.getInstance(this).getAllContacts());
        //listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //show alertdialog with either history or call
                ClientScanResult a = wifiApManager.results.get(position);
                //otherUsernameWhenWeCall=((Contact) adapter.getItem(position)).getUsername();
                otherUsernameWhenWeCall = a.getHWAddr();
                //final String rawIP = ((Contact) adapter.getItem(position)).getIp();
                final String rawIP = a.getIpAddr();
                RW = rawIP;
                new AlertDialog.Builder(LandingActivity.this)
                        .setTitle(otherUsernameWhenWeCall)
//                        .setMessage("Do you want to accept a call from " + otherUsername)
                        .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Call selected
                                Toast.makeText(LandingActivity.this, "Connected to "+RW, Toast.LENGTH_SHORT).show();


                                Button sendtext1=(Button)findViewById(R.id.btnSend);
                                Button sendImg1=(Button)findViewById(R.id.btnSend1);
                                Button sendrec1=(Button)findViewById(R.id.btnSend2);

                                sendtext1.setEnabled(true);
                                sendImg1.setEnabled(true);
                                sendrec1.setEnabled(true);



                                                           }
                        })
                        .setNegativeButton("Send Image", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*Intent i=new Intent(LandingActivity.this,HistoryActivity.class);
                                i.putExtra(Util.KEY_OTHER_USERNAME,otherUsernameWhenWeCall);
                                startActivity(i);*/
                                Toast.makeText(LandingActivity.this, "Sending Image", Toast.LENGTH_SHORT).show();
                                sendImage();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();


            }
        });
        //add new contact:


        //save my ip
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        myIP= Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        if(myIP!=null)
            ((TextView)findViewById(R.id.landingTextView)).setText("Your IP address: " + myIP);

        //button listener
        ((Button)findViewById(R.id.callButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request the other partner for a call
                //create json request body
                Toast.makeText(LandingActivity.this, "Disabled", Toast.LENGTH_SHORT).show();

            }
        });

        //start HTTP server
        startService(new Intent(LandingActivity.this, HTTPServerService.class));



    }

   public void senddata(View view)
   {
       EditText wherewewrite = (EditText) findViewById(R.id.etMain);
       String whatwewrite = wherewewrite.getText().toString();
       TextView history = (TextView) findViewById(R.id.chathistory);
       wherewewrite.setText("");
       String historyhistory = history.getText().toString();
       history.setText(historyhistory +"\n" + "Me : "+whatwewrite);

       callState=CALL_STATE_IN_CALL;
       //build a proper ip address
       String calleeIPFullHTTP = Util.PROTOCOL_HTTP + RW + ":" + Util.HTTP_PORT;
       SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

       WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
       WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
       int ip = wifiInfo.getIpAddress();
       String localIP = Formatter.formatIpAddress(ip);

       String myUsername=prefs.getString(Util.KEY_PREFS_USERNAME,RW);
       JSONObject requestJSON = new JSONObject();
       try {
           requestJSON.put(Util.KEY_OPERATION, Util.OPERATION_TYPE_REQUEST_TEXT);
           requestJSON.put(Util.KEY_OTHER_USERNAME,localIP);
           requestJSON.put(Util.KEY_TEXT,whatwewrite);
           // Not really required: in the service, we can get this ip anyway: requestJSON.put(Util.KEY_CALLER_IP,myIP);   //while sending caller's ip,send it raw(without http and port no) - its the receivers responsibility to handle a raw ip

       } catch (JSONException e) {
           e.printStackTrace();
       }


       AsyncHttpRequest req = new AsyncHttpPost(calleeIPFullHTTP);

       AsyncHttpRequestBody body = new JSONObjectBody(requestJSON);
       req.setBody(body);
       AsyncHttpClient.getDefaultInstance().executeJSONObject(req, null);
       //show ui showing that a call is being attempted
       Toast.makeText(LandingActivity.this, "Sending Text", Toast.LENGTH_SHORT).show();



   }




    //TODO: use SetContacts on the adapter in either onresume or in onstart..
    private void performCall(String rawIP) {
        callState=CALL_STATE_IN_CALL;
        //build a proper ip address
        String calleeIPFullHTTP = Util.PROTOCOL_HTTP + rawIP + ":" + Util.HTTP_PORT;
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

        String myUsername=prefs.getString(Util.KEY_PREFS_USERNAME,rawIP);
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put(Util.KEY_OPERATION, Util.OPERATION_TYPE_REQUEST_CALL);
            requestJSON.put(Util.KEY_OTHER_USERNAME,myUsername);
            // Not really required: in the service, we can get this ip anyway: requestJSON.put(Util.KEY_CALLER_IP,myIP);   //while sending caller's ip,send it raw(without http and port no) - its the receivers responsibility to handle a raw ip

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AsyncHttpRequest req = new AsyncHttpPost(calleeIPFullHTTP);

        AsyncHttpRequestBody body = new JSONObjectBody(requestJSON);
        req.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeJSONObject(req, null);
        //show ui showing that a call is being attempted
        Toast.makeText(LandingActivity.this, "Calling the other party", Toast.LENGTH_SHORT).show();
    }

    // image

    private void sendImage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri uri = data.getData();
        String ImagePath = uri.toString();
        performimageshare(RW,uri);
    }
    private void performimageshare(String rawIP,Uri ImagePath) {

       // String path = ImagePath;
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImagePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        callState=CALL_STATE_IN_CALL;
        //build a proper ip address
        String calleeIPFullHTTP = Util.PROTOCOL_HTTP + rawIP + ":" + Util.HTTP_PORT;
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String localIP = Formatter.formatIpAddress(ip);

        String myUsername=prefs.getString(Util.KEY_PREFS_USERNAME,rawIP);
        JSONObject requestJSON = new JSONObject();
        try {
            requestJSON.put(Util.KEY_OPERATION, Util.OPERATION_TYPE_REQUEST_IMAGE);
            requestJSON.put(Util.KEY_OTHER_USERNAME,localIP);
            requestJSON.put(Util.KEY_Image,encodedImage);
            // Not really required: in the service, we can get this ip anyway: requestJSON.put(Util.KEY_CALLER_IP,myIP);   //while sending caller's ip,send it raw(without http and port no) - its the receivers responsibility to handle a raw ip

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AsyncHttpRequest req = new AsyncHttpPost(calleeIPFullHTTP);

        AsyncHttpRequestBody body = new JSONObjectBody(requestJSON);
        req.setBody(body);
        AsyncHttpClient.getDefaultInstance().executeJSONObject(req, null);
        //show ui showing that a call is being attempted
        Toast.makeText(LandingActivity.this, "Sending Image", Toast.LENGTH_SHORT).show();
    }


    // image
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter=new IntentFilter(Util.INTENT_FILTER_SERVICE_ACTIVITY);
        LocalBroadcastManager.getInstance(this).registerReceiver(incomingCallBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(incomingCallBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, "Destroying session and stopping RTSP server");
//        if(mSession!=null)
//            mSession.stop();

        super.onDestroy();
    }

    private BroadcastReceiver incomingCallBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            final String otherIP=intent.getStringExtra(Util.KEY_OTHER_IP);


            switch(intent.getIntExtra(Util.KEY_INTENT_FILTER_REASON, Util.INTENT_FILTER_REASON_NO_REASON)) {


                case Util.INTENT_FILTER_REASON_NEW_INCOMING_VOICE:

                    final String Voicedata = intent.getStringExtra(Util.KEY_INTENT_FILTER_VOICE);
                    final String ipforvoice = intent.getStringExtra(Util.KEY_INTENT_FILTER_OTHER_USERNAME);


                    byte[] sound = Base64.decode(Voicedata, Base64.DEFAULT);






                    //
                    try {
                        // create temp file that will hold byte array
                        File tempMp3 = File.createTempFile("kurchina", "3gp", getCacheDir());
                        tempMp3.deleteOnExit();
                        Toast.makeText(getApplicationContext(), "Received Voice Message from "+RW, Toast.LENGTH_SHORT).show();
                        FileOutputStream fos = new FileOutputStream(tempMp3);
                        fos.write(sound);
                        fos.close();
                        mediaPlayer = new MediaPlayer();
                        // resetting mediaplayer instance to evade problems
                        mediaPlayer.reset();

                        // In case you run into issues with threading consider new instance like:
                        // MediaPlayer mediaPlayer = new MediaPlayer();

                        // Tried passing path directly, but kept getting
                        // "Prepare failed.: status=0x1"
                        // so using file descriptor instead
                        FileInputStream fis = new FileInputStream(tempMp3);
                        mediaPlayer.setDataSource(fis.getFD());

                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException ex) {
                        String s = ex.toString();
                        ex.printStackTrace();
                    }



                    break;
                case Util.INTENT_FILTER_REASON_NEW_INCOMING_TEXT:

                    final String textdata = intent.getStringExtra(Util.KEY_INTENT_FILTER_TEXT);
                    final String ipfortext = intent.getStringExtra(Util.KEY_INTENT_FILTER_OTHER_USERNAME);

                    final TextView history = (TextView) findViewById(R.id.chathistory);
                    final String  historyhistory = history.getText().toString();
                    history.setText(historyhistory +"\n" + RW+" : "+textdata);


                    break;

                case Util.INTENT_FILTER_REASON_NEW_INCOMING_IMAGE:
                    final String image = intent.getStringExtra(Util.KEY_INTENT_FILTER_IMAGE);
                    final String ip = intent.getStringExtra(Util.KEY_INTENT_FILTER_OTHER_USERNAME);
                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ImageView imagess = (ImageView) findViewById(R.id.Image);
                  //  imagess.setImageBitmap(decodedByte);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File path = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS);
                    File currentimage=null;
                    try {
                       currentimage  = File.createTempFile(imageFileName,".jpg",path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(currentimage);
                        decodedByte.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intentshowimage = new Intent();
                    intentshowimage.setAction(Intent.ACTION_VIEW);
                    intentshowimage.setDataAndType(Uri.parse("file://" + currentimage), "image/*");
                    startActivity(intentshowimage);
                    Toast.makeText(context, "Received Image : " + currentimage + "from : " + ip, Toast.LENGTH_SHORT).show();

                    // /storeImage(decodedByte);



                    break;


                //Util.OPERATION_TYPE_END_CALL: This case will never be handled here (since we ll always be in the IN Call UI when we receive this
                default:
                    Log.d(TAG,"Invalid int extra received via broadcast!");
                    break;
            }
        }
    };
        @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                else
                    mDrawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER

                return true;



        }
        return super.onOptionsItemSelected(item);
    }

    public void rec (View view)

    {

        play=(Button)findViewById(R.id.button3);
        stop=(Button)findViewById(R.id.button2);
        record=(Button)findViewById(R.id.button);
        sendthis=(Button)findViewById(R.id.button4);

        stop.setEnabled(false); // to disable button , it will enable after record button pressed
        play.setEnabled(false);
        sendthis.setEnabled(false);
        record.setEnabled(true);
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

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getStringFile() {
        InputStream inputStream = null;

        String encodedFile= "", lastVal;
        byte[] b;





			/*inputStream = new FileInputStream(outputFile);

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
			Log.v("SendAudio",encodedFile);
			sendMessage(encodedFile);*/

        File file = new File(outputFile);

        b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int i = 0; i < b.length; i++) {
                System.out.print((char)b[i]);
            }

            String rawstring = Base64.encodeToString(b, Base64.DEFAULT);

            Log.v("SENDING",rawstring);
            //


            callState=CALL_STATE_IN_CALL;
            //build a proper ip address
            String calleeIPFullHTTP = Util.PROTOCOL_HTTP + RW + ":" + Util.HTTP_PORT;
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);

            WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String localIP = Formatter.formatIpAddress(ip);

            String myUsername=prefs.getString(Util.KEY_PREFS_USERNAME,RW);
            JSONObject requestJSON = new JSONObject();
            try {
                requestJSON.put(Util.KEY_OPERATION, Util.OPERATION_TYPE_REQUEST_VOICE);
                requestJSON.put(Util.KEY_OTHER_USERNAME,localIP);
                requestJSON.put(Util.KEY_INTENT_FILTER_VOICE,rawstring);
                // Not really required: in the service, we can get this ip anyway: requestJSON.put(Util.KEY_CALLER_IP,myIP);   //while sending caller's ip,send it raw(without http and port no) - its the receivers responsibility to handle a raw ip

            } catch (JSONException e) {
                e.printStackTrace();
            }


            AsyncHttpRequest req = new AsyncHttpPost(calleeIPFullHTTP);

            AsyncHttpRequestBody body = new JSONObjectBody(requestJSON);
            req.setBody(body);
            AsyncHttpClient.getDefaultInstance().executeJSONObject(req, null);
            //show ui showing that a call is being attempted
            Toast.makeText(LandingActivity.this, "Sending Voice", Toast.LENGTH_SHORT).show();





            ///
            stop.setEnabled(false); // to disable button , it will enable after record button pressed
            play.setEnabled(false);
            sendthis.setEnabled(false);
            record.setEnabled(false);
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1) {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }







    }


    public void sendfile(View view)
    {
        getStringFile();
    }
    public void Img(View view)
    {
        sendImage();
    }


}
