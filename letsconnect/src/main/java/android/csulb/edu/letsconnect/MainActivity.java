/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.csulb.edu.letsconnect;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	private ListView lvMainChat;
	private EditText etMain;
	private Button btnSend;

	private String connectedDeviceName = null;
	private ArrayAdapter<String> chatArrayAdapter;

	private StringBuffer outStringBuffer;
	private BluetoothAdapter bluetoothAdapter = null;
	private ChatService chatService = null;
	static String a="";
	MediaPlayer mediaPlayer;


	//REC
	Button play,stop,record,sendthis;
	private MediaRecorder myAudioRecorder;
	private String outputFile = null;
	//Recend

	public Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				switch (msg.arg1) {
				case ChatService.STATE_CONNECTED:
					setStatus(getString(R.string.title_connected_to,
							connectedDeviceName));
					chatArrayAdapter.clear();
					break;
				case ChatService.STATE_CONNECTING:
					setStatus(R.string.title_connecting);
					break;
				case ChatService.STATE_LISTEN:
				case ChatService.STATE_NONE:
					setStatus(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;

				String writeMessage = new String(writeBuf);
				if(writeMessage.substring(writeMessage.length() - 4).equals("TXTZ"))
				{
					String finalstring = writeMessage.substring(0, writeMessage.length() - 4);
					chatArrayAdapter.add("Me" + ":  " + finalstring);
				}

				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;

				String readMessage = new String(readBuf, 0, msg.arg1);

				if(readMessage.substring(readMessage.length() - 5).equals("RECZZ"))
				{
					a+=readMessage;

					String finalstring = a.substring(0, a.length() - 5);
					a="";
                    Log.v("Audiotest",finalstring);
					byte[] sound = Base64.decode(finalstring, Base64.DEFAULT);






					//
					try {
						// create temp file that will hold byte array
						File tempMp3 = File.createTempFile("kurchina", "3gp", getCacheDir());
						tempMp3.deleteOnExit();
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
				   //
				}

				if(readMessage.substring(readMessage.length() - 4).equals("TXTZ"))
				{
					String finalstring = readMessage.substring(0, readMessage.length() - 4);
					chatArrayAdapter.add(connectedDeviceName + ":  " + finalstring);
					break;
				}


				try {
					readMessage = readMessage.substring(readMessage.indexOf("TXTZ"), readMessage.length());

 				if(readMessage.substring(0, Math.min(readMessage.length(), 4)).equals("TXTZ"))
				{
					readMessage=readMessage.substring(4);
				}
				}
				catch(Exception e)
				{}
				if(readMessage.substring(readMessage.length() - 4).equals("DONZ"))
				{
					a+=readMessage;

					String finalstring = a.substring(0, a.length() - 4);
					a="";
					finalstring.replaceAll("\\s+","");
					finalstring.replaceAll("\t", "");
					finalstring.replaceAll("\n", "");

					Log.v("READ",finalstring);

					byte[] decodedString = Base64.decode(finalstring, Base64.DEFAULT);


					Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
					ImageView imagess = (ImageView) findViewById(R.id.Image);
					imagess.setImageBitmap(decodedByte);

					readMessage="";

				}
				else
				{
                  a+=readMessage;
				}
				break;
			case MESSAGE_DEVICE_NAME:

				connectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + connectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			return false;
		}
	});

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		getWidgetReferences();
		bindEventHandler();

		if (bluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		//Rec






		//Rec end





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
	Long tsLong = System.currentTimeMillis()/1000;
	String ts = tsLong.toString();
	outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/nlabz-recording"+ts+".3gp";

	myAudioRecorder=new MediaRecorder();
	myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
	myAudioRecorder.setOutputFile(outputFile);


	record.setOnClickListener(new OnClickListener() {
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

	stop.setOnClickListener(new OnClickListener() {
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

	play.setOnClickListener(new OnClickListener() {
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

	private void getWidgetReferences() {
		lvMainChat = (ListView) findViewById(R.id.lvMainChat);
		etMain = (EditText) findViewById(R.id.etMain);
		btnSend = (Button) findViewById(R.id.btnSend);
	}

	private void bindEventHandler() {
		etMain.setOnEditorActionListener(mWriteListener);

		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String message = etMain.getText().toString();
				sendMessage(message+"TXTZ");
			}
		});
	}
	public void Img(View view)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 20);

		Toast.makeText(MainActivity.this, "Hello Image", Toast.LENGTH_SHORT).show();	
		
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		
			case 20:
				Uri uri = data.getData();
				String ImagePath = uri.toString();
				performimageshare(uri);
				break;
		case REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				setupChat();
			} else {
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void performimageshare(Uri uri) {
		Bitmap bm = null;
		try {
			bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
		byte[] b = baos.toByteArray();
		String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		Log.v("PERF",encodedImage);
		encodedImage=encodedImage+"DONZ";
		sendMessage(encodedImage);
	}

	private void connectDevice(Intent data, boolean secure) {
		String address = data.getExtras().getString(
				DeviceListActivity.DEVICE_ADDRESS);
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
		chatService.connect(device, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent,
					REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		case R.id.discoverable:
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	private void ensureDiscoverable() {
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	public void sendMessage(String message) {
		if (chatService.getState() != ChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (message.length() > 0) {
			byte[] send = message.getBytes();
			chatService.write(send);

		outStringBuffer.setLength(0);
			etMain.setText(outStringBuffer);
		}
	}

	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			return true;
		}
	};

	private final void setStatus(int resId) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setSubtitle(subTitle);
	}

	private void setupChat() {
		chatArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
		lvMainChat.setAdapter(chatArrayAdapter);

		chatService = new ChatService(this, handler);

		outStringBuffer = new StringBuffer("");
	}

	@Override
	public void onStart() {
		super.onStart();

		if (!bluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (chatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();

		if (chatService != null) {
			if (chatService.getState() == ChatService.STATE_NONE) {
				chatService.start();
			}
		}
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatService != null)
			chatService.stop();
	}

//REC


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
				rawstring = rawstring + "RECZZ";
                 Log.v("SENDING",rawstring);
				sendMessage(rawstring);
				stop.setEnabled(false); // to disable button , it will enable after record button pressed
				play.setEnabled(false);
				sendthis.setEnabled(false);
				record.setEnabled(true);
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





	//Recends



}
