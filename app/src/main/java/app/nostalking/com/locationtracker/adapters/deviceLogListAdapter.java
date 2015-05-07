package app.nostalking.com.locationtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.Locations;

/**
 * Created by Applaudo Dev on 4/30/2015.
 */
public class deviceLogListAdapter extends RecyclerView.Adapter<deviceLogListAdapter.ViewHolder> {
    private Locations mDevices;
    private static final String TAG = "CustomAdapter";
    private LayoutInflater mInflater;
    private Context mContext;
    private onItemClickListenr mCallback;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTime;
        private TextView mLog;
        private TextView mLocation;
        private RelativeLayout mItem;

        public ViewHolder(View convertView) {
            super(convertView);
            mItem = (RelativeLayout) convertView.findViewById(R.id.log_relative_layout);
            mTime = (TextView) convertView.findViewById(R.id.txt_time);
            mLog = (TextView) convertView.findViewById(R.id.txt_phone_log);
            mLocation = (TextView) convertView.findViewById(R.id.txt_location);
        }

        public TextView getTime() {
            return mTime;
        }

        public TextView getmLog() {
            return mLog;
        }

        public TextView getmLocation() {
            return mLocation;
        }

        public RelativeLayout getItem(){
            return mItem;
        }
    }

    public deviceLogListAdapter(Locations dataSet, Context context, onItemClickListenr listener) {
        mCallback = listener;
        mContext = context;
        mDevices = dataSet;

        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.full_log_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getmLog().setText(mDevices.getmLocations().get(position).getModifyPhoneLog());
        viewHolder.getmLocation().setText(mDevices.getmLocations().get(position).getmStreetAddress());
        viewHolder.getTime().setText(mDevices.getmLocations().get(position).getParsedDate());
        viewHolder.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDevices.getmLocations().size();
    }

    public interface onItemClickListenr{
        public void onItemClick(View view, int position);
    }

}
