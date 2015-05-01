package app.nostalking.com.locationtracker.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.Locations;
import app.nostalking.com.locationtracker.model.TrackingDevices;

/**
 * Created by Applaudo Dev on 4/22/2015.
 */
public class DeviceLogAdapter extends BaseAdapter {
    private ArrayList<Locations.LocationObject> mDevices = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public DeviceLogAdapter(Context context,Locations objects) {
        super();

        int lenght = objects.getmLocations().size();

        for (int i = 0; i < lenght; i ++){
            if(i < 10){
                mDevices.add(objects.getmLocations().get(i));
            }
        }
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        if(convertView == null){

            convertView = mInflater.inflate(R.layout.full_log_row, null);
            holder = new ViewHolder();
            holder.log = (TextView) convertView.findViewById(R.id.txt_phone_log);
            holder.location = (TextView) convertView.findViewById(R.id.txt_location);
            holder.time = (TextView) convertView.findViewById(R.id.txt_time);

            holder.log.setText(mDevices.get(position).getModifyPhoneLog());
            holder.location.setText(mDevices.get(position).getmStreetAddress());
            holder.time.setText("updated at : " + "[ " + mDevices.get(position).getmTime() + " ]");

            convertView.setTag(holder);


        }else{

            holder = (ViewHolder) convertView.getTag();
            holder.log = (TextView) convertView.findViewById(R.id.txt_phone_log);
            holder.location = (TextView) convertView.findViewById(R.id.txt_location);
            holder.time = (TextView) convertView.findViewById(R.id.txt_time);

            holder.log.setText(mDevices.get(position).getModifyPhoneLog());
            holder.location.setText(mDevices.get(position).getmStreetAddress());
            holder.time.setText(mDevices.get(position).getParsedDate());

        }

        return convertView;
    }

    class ViewHolder{
        TextView time;
        TextView log;
        TextView location;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDevices.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public interface onItemClickListener{
        public void onItemClick(View view);
    }

}
