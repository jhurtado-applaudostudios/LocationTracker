package app.nostalking.com.locationtracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.TrackingDevices;

/**
 * Created by Applaudo Dev on 4/30/2015.
 */
public class deviceListAdapter extends RecyclerView.Adapter<deviceListAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private TrackingDevices mDataSet;
    private onItemClickListenr mCallback;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView deviceName;
        private TextView locationId;
        private RelativeLayout mItem;

        public ViewHolder(View convertView) {
            super(convertView);
            mItem = (RelativeLayout) convertView.findViewById(R.id.relative_Layout);
            dateTime = (TextView) convertView.findViewById(R.id.txt_date_time);
            deviceName = (TextView) convertView.findViewById(R.id.txt_device_name);
            locationId = (TextView) convertView.findViewById(R.id.txt_device_id);
        }

        public TextView getDateTime() {
            return dateTime;
        }

        public TextView getDeviceName() {
            return deviceName;
        }

        public TextView getLocationId() {
            return locationId;
        }

        public RelativeLayout getItem(){
            return mItem;
        }
    }

    public deviceListAdapter(TrackingDevices dataSet, onItemClickListenr listener) {
        mCallback = listener;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trackign_device_list_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        viewHolder.getDateTime().setText("Registered: " + mDataSet.getmDevices().get(position).getParsedDate());
        viewHolder.getLocationId().setText("id: " + mDataSet.getmDevices().get(position).getSmSearchId());
        viewHolder.getDeviceName().setText(mDataSet.getmDevices().get(position).getmDevice());
        viewHolder.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.getmDevices().size();
    }

    public interface onItemClickListenr{
        public void onItemClick(View view, int position);
    }

}

