package android.csulb.edu.letsconnect;

import android.content.Intent;
import android.csulb.edu.letsconnect.Wifi.wifiMainActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

public void Bluetooth(View view)
{
    Intent blue = new Intent(this, MainActivity.class);
    startActivity(blue);
}

    public void Wifi(View view)
    {
        Intent blue = new Intent(this, wifiMainActivity.class);
        startActivity(blue);
    }


}


