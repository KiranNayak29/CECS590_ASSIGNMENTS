package android.csulb.edu.cecs590_assignments;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Order extends AppCompatActivity {
    public static String myorder;
    public static ArrayList<String> Tacosize = new ArrayList<String>();
    public static ArrayList<String> Tortilla = new ArrayList<String>();
    public static ArrayList<String> Fillings = new ArrayList<String>();
    public static ArrayList<String> Beverages = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
    }
    public void onsizeRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.medium:
                if (checked)
                    // Pirates are the best
                    if(Tacosize.contains("Large"))
                    {
                        Tacosize.remove("Large");
                    }
                     Tacosize.add("Medium");

                    break;
            case R.id.large:
                if (checked)
                    // Ninjas rule
                    if(Tacosize.contains("Medium"))
                    {
                        Tacosize.remove("Medium");
                    }
                Tacosize.add("Large");
                    break;
        }
    }


    public void ontortillaRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.Corn:
                if (checked)
                    // Pirates are the best
                    if(Tortilla.contains("Flour"))
                    {
                        Tortilla.remove("Flour");
                    }
                Tortilla.add("Corn");
                    break;
            case R.id.Flour:
                if (checked)
                    // Ninjas rule
                    if(Tortilla.contains("Corn"))
                    {
                        Tortilla.remove("Corn");
                    }
                Tortilla.add("Flour");
                    break;
        }
    }


    public void onfillingsCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_beef:
                if (checked)
                {
                 Fillings.add("Beef");
                }

                else
                    Fillings.remove("Beef");
                break;
            case R.id.checkbox_chicken:
                if (checked)
                {
                    Fillings.add("Chicken");
                }
                // Cheese me
                else
                // I'm lactose intolerant
                    Fillings.remove("Chicken");
                break;
            case R.id.checkbox_WhiteFish:
                if (checked)
                {
                    Fillings.add("WhiteFish");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("WhiteFish");
                break;
            case R.id.checkbox_cheese:
                if (checked)
                {
                    Fillings.add("Cheese");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("Cheese");
                break;
            case R.id.checkbox_seafood:
                if (checked)
                {
                    Fillings.add("SeaFood");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("SeaFood");
                break;
            case R.id.checkbox_rice:
                if (checked)
                {
                    Fillings.add("Rice");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("Rice");
                break;
            case R.id.checkbox_beans:
                if (checked)
                {
                    Fillings.add("Beans");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("Beans");
                break;
            case R.id.checkbox_pico:
                if (checked)
                {
                    Fillings.add("PicoDeGallo");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("PicoDeGallo");
                break;
            case R.id.checkbox_guacamole:
                if (checked)
                {
                    Fillings.add("Gaucamole");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("Gaucamole");
                break;
            case R.id.LBT:
                if (checked)
                {
                    Fillings.add("LBT");
                }
                // Cheese me
                else
                    // I'm lactose intolerant
                    Fillings.remove("LBT");
                break;

        }
    }




    public void onbeveragesCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Soda:
                if (checked)
                Beverages.add("Soda");
                else
                Beverages.remove("Soda");
                break;
            case R.id.Cerveza:
                if (checked)
                Beverages.add("Cerveza");
                else
                Beverages.remove("Cerveza");
                break;
            case R.id.margarita:
                if (checked)
                    Beverages.add("Margarita");
                else
                    Beverages.remove("Margarita");
                break;
            case R.id.tequila:
                if (checked)
                    Beverages.add("Taquila");
                else
                    Beverages.remove("Taquila");
                break;
            // TODO: Veggie sandwich
        }
    }

   public void PlaceOrder(View view)
   {
       int size=0,tortilla=0,fillings=0,beverages=0,total=0;

       myorder = "Size : ";
       for (String temp : Tacosize)
       {
            myorder += temp + ", ";
           if(temp == "Medium")
           {
               size = 4;
           }
           else
           {
               size = 7;
           }
       }
       myorder += "  Tortilla : ";

       for (String temp : Tortilla)
       {
           myorder += temp + ", ";
           if(temp == "Corn")
           {
               tortilla = 6;
           }
           else
           {
               tortilla = 5;
           }

       }
       myorder += "  Fillings : ";
        fillings = Fillings.size();
       for (String temp : Fillings)
       {
           myorder += temp + ", ";
       }
       myorder += "  Beverages : ";
       beverages = Beverages.size();
       for (String temp : Beverages)
       {
           myorder += temp + ", ";
       }
       total = size+tortilla+(fillings*2)+(beverages*4);
       myorder += "  Priced at " + "$"+ total;
       Log.d("Hello", myorder);
       Toast toast = Toast.makeText(this, "Your Order Has Been Successfully Placed", Toast.LENGTH_LONG);
       toast.show();

       SmsManager smsManager = SmsManager.getDefault();
       smsManager.sendTextMessage("5628507043", null, "I WANT A BIG TACO - " + myorder, null, null);
       Reset();

   }

    public void Reset()
    {
       CheckBox reset = (CheckBox) findViewById(R.id.checkbox_beans);
       reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.checkbox_cheese);
        reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.checkbox_beef);
        reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.checkbox_chicken);
        reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.checkbox_guacamole);
        reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.checkbox_pico);
        reset.setChecked(false);
         reset = (CheckBox) findViewById(R.id.LBT);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.checkbox_seafood);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.checkbox_WhiteFish);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.checkbox_rice);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.Cerveza);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.margarita);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.Soda);
        reset.setChecked(false);
        reset = (CheckBox) findViewById(R.id.tequila);
        reset.setChecked(false);
    }


}
