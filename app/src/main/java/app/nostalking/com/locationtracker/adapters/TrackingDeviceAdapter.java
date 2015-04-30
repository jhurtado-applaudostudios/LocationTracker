package app.nostalking.com.locationtracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.TrackingDevices;

/**
 * Created by Applaudo Dev on 4/13/2015.
 */
public class TrackingDeviceAdapter extends BaseAdapter {
    private TrackingDevices mDevices;
    private Context mContext;
    private LayoutInflater mInflater;

    public TrackingDeviceAdapter(Context context,TrackingDevices objects) {
        super();
        mDevices = objects;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        if(convertView == null){

            convertView = mInflater.inflate(R.layout.trackign_device_list_row, null);
            holder = new ViewHolder();
            holder.dateTime = (TextView) convertView.findViewById(R.id.txt_date_time);
            holder.deviceName = (TextView) convertView.findViewById(R.id.txt_device_name);
            holder.locationId = (TextView) convertView.findViewById(R.id.txt_device_id);
            holder.dateTime.setText("Registered: " + mDevices.getmDevices().get(position).getParsedDate());
            holder.locationId.setText("ID: " + mDevices.getmDevices().get(position).getSmSearchId());
            holder.deviceName.setText(mDevices.getmDevices().get(position).getmDevice());

            convertView.setTag(holder);

        }else{

            holder = (ViewHolder) convertView.getTag();
            holder.dateTime.setText("Registered: " + mDevices.getmDevices().get(position).getParsedDate());
            holder.locationId.setText("ID: " + mDevices.getmDevices().get(position).getSmSearchId());
            holder.deviceName.setText(mDevices.getmDevices().get(position).getmDevice());
        }

        return convertView;
    }

    class ViewHolder{
        TextView dateTime;
        TextView deviceName;
        TextView locationId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDevices.getmDevices().size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDevices.getmDevices().get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}