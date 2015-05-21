package app.nostalking.com.locationtracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.activities.TrackerApplication;
import app.nostalking.com.locationtracker.intefaces.AnimationListener;
import app.nostalking.com.locationtracker.utils.Animation;
import app.nostalking.com.locationtracker.utils.ExplodeAnimation;

/**
 * Created by Juan Hurtado on 5/4/2015.
 */
public class FragmentDecision extends Fragment implements View.OnClickListener {
    public static final int CASE_DEFAULT = 3;
    public static final int CASE_RECEPTOR = 0;
    public static final int CASE_TRANSMITTER = 1;

    private TextView mContent;
    private TextView mReceptor;
    private TextView mTransmitter;
    private onModeSelectedListener mCallback;
    private boolean isConfirmation = false;
    private boolean isTransmitter = false;

    public static FragmentDecision getInstance(){
        return new FragmentDecision();
    }

    @Override
    public void onClick(View v) {

        TrackerApplication.getInstance().getDataSharedPreferences().saveFirstTime(true);
        if(!isConfirmation){
            switch (v.getId()){
                case R.id.receptor:
                    isTransmitter = false;
                    confirmation();
                    break;
                case R.id.transmitter:
                    isTransmitter = true;
                    confirmation();
                    break;
            }
        } else {
            switch (v.getId()){
                case R.id.receptor:
                    if(isTransmitter){
                        mCallback.onSelection(CASE_TRANSMITTER);
                    } else {
                        mCallback.onSelection(CASE_RECEPTOR);
                    }
                    break;
                case R.id.transmitter:
                    isTransmitter = true;
                    isConfirmation = false;
                    originalText();
                    break;
            }
        }

    }


    public interface onModeSelectedListener {
        public void onSelection(int selection);
    }

    private void originalText(){
        mContent.setText(R.string.decision);
        mTransmitter.setText(R.string.transmitter);
        mReceptor.setText(R.string.receptor);
    }

    private void confirmation(){
        isConfirmation = true;
        mContent.setText(R.string.warning_text);
        mTransmitter.setText(R.string.no);
        mReceptor.setText(R.string.yes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_decision, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContent = (TextView) view.findViewById(R.id.txt_decision);
        mReceptor = (TextView) view.findViewById(R.id.receptor);
        mTransmitter = (TextView) view.findViewById(R.id.transmitter);
        mTransmitter.setOnClickListener(this);
        mReceptor.setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        mCallback = (onModeSelectedListener) activity;
        super.onAttach(activity);
    }
}
