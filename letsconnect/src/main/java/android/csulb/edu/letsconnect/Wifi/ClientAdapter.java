package android.csulb.edu.letsconnect.Wifi;

import android.app.Activity;
import android.csulb.edu.letsconnect.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

/**
 * Created by Kiran on 4/2/2017.
 */

public class ClientAdapter extends ArrayAdapter<ClientScanResult> {
    public ClientAdapter(Activity context, ArrayList<ClientScanResult> clients) {
        super(context, 0,clients);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.main_listview_row, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        ClientScanResult currentclient = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.listName);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentclient.getIpAddr());

        // Find the TextView in the list_item.xml layout with the ID version_number

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.listPhoto);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView

        iconView.setImageResource(R.drawable.ic_launcher);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }




}
