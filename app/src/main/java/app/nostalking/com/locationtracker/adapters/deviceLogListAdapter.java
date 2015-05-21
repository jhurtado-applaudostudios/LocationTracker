package app.nostalking.com.locationtracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.PhoneLog;

public class deviceLogListAdapter extends RecyclerView.Adapter<deviceLogListAdapter.ViewHolder> {
    private final PhoneLog mPhoneLog;
    private final Context mContext;
    private final onItemClickListener mCallback;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mDate;
        private final TextView mType;
        private final TextView mName;
        private final TextView mDuration;
        private final TextView mPhoneNumber;
        private final LinearLayout mItem;

        public ViewHolder(View convertView) {
            super(convertView);
            mItem = (LinearLayout) convertView.findViewById(R.id.log_relative_layout);
            mDate = (TextView) convertView.findViewById(R.id.txt_date);
            mType = (TextView) convertView.findViewById(R.id.txt_type);
            mName = (TextView) convertView.findViewById(R.id.txt_caller);
            mDuration = (TextView) convertView.findViewById(R.id.txt_duration);
            mPhoneNumber = (TextView) convertView.findViewById(R.id.txt_phone_number);
        }

        public TextView getmDate(){
            return mDate;
        }

        public TextView getmType(){
            return mType;
        }

        public TextView getmName(){
            return mName;
        }

        public TextView getmDuration(){
            return mDuration;
        }

        public TextView getmPhoneNumber(){
            return mPhoneNumber;
        }

        public LinearLayout getItem(){
            return mItem;
        }
    }

    public deviceLogListAdapter(PhoneLog dataSet, Context context, onItemClickListener listener) {
        mCallback = listener;
        mContext = context;
        mPhoneLog = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.full_log_row, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getmDate().setText("Date         : " + mPhoneLog.getmLogList().get(position).getmDate());
        viewHolder.getmDuration().setText("Duration     : " + mPhoneLog.getmLogList().get(position).getmDuration());
        viewHolder.getmName().setText("Name         : " + mPhoneLog.getmLogList().get(position).getmName());
        viewHolder.getmPhoneNumber().setText("Phone Number : " + mPhoneLog.getmLogList().get(position).getmPhoneNumber());
        viewHolder.getmType().setText("Call State   : " + mPhoneLog.getmLogList().get(position).getmType());
        viewHolder.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhoneLog.getmLogList().size();
    }

    public interface onItemClickListener {
        public void onItemClick(View view, int position);
    }

}