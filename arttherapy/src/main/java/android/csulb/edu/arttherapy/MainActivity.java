package android.csulb.edu.arttherapy;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private CanvasView customCanvas;
    private SensorManager sensorManager;
    private long lastUpdate;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},1);
        Intent myintent = new Intent(this,BroadcastManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,myintent,0);
        customCanvas = (CanvasView) findViewById(R.id.signature_canvas);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate= System.currentTimeMillis();

    }

    public void clearCanvas(View v) {
        customCanvas.clearCanvas();
    }




    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {

            checkShake(event);
        }
    }

    private void checkShake(SensorEvent event) {


        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float accelarationsqroot = (x*x+y*y+z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);
        long time = System.currentTimeMillis();
        if(accelarationsqroot >= 2)
        {
            if(time-lastUpdate < 200)
            {
                return;
            }
        lastUpdate=time;
        Toast.makeText(this,"Shake Detected",Toast.LENGTH_LONG).show();
            Intent sound = new Intent(this,SoundService.class);
            startService(sound);
            customCanvas.clearCanvas();
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

public void Gclick(View view)
{
customCanvas.mPaint.setColor(Color.parseColor("#8BC34A"));
}
    public void Rclick(View view)
    {
        customCanvas.mPaint.setColor(Color.parseColor("#F44336"));
    }
    public void Bclick(View view)
    {
        customCanvas.mPaint.setColor(Color.parseColor("#2196F3"));
    }
    public void Yclick(View view)
    {
        customCanvas.mPaint.setColor(Color.parseColor("#FFEB3B"));
    }
    public void Blclick(View view)
    {
        customCanvas.mPaint.setColor(Color.parseColor("#000000"));
    }

}
