package app.nostalking.com.locationtracker.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.AccountExistence;
import app.nostalking.com.locationtracker.model.ReportID;
import app.nostalking.com.locationtracker.service.LocationService;
import app.nostalking.com.locationtracker.utils.ApiStates;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 5/5/2015.
 */
public class UpdateActivity extends Activity {
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_NORMAL = 3;
    public static final int PRIORITY_HIGH = 4;
    public static final int FREQUENCY_15MIN = 900000;
    public static final int FREQUENCY_30MIN = 1800000;
    public static final int FREQUENCY_45MIN = 2700000;
    public static final int FREQUENCY_1HOUR = 3600000;
    private PackageManager mPackageManager;
    private ComponentName componentName;
    private final ScheduledExecutorService worker =
            Executors.newSingleThreadScheduledExecutor();
    private ProgressBar mDialogProgressBar;
    private TextView mDialogText;
    private Dialog mDialog;
    private Context mContext;
    private RadioGroup mRadioGroup;
    private RadioButton m15Min;
    private SeekBar mAccuracyAdjustment;
    private TextView mAccuracyText;
    private EditText mNicknameSource;
    private EditText mUsernameSource;
    private EditText mPasswordSource;
    private Button mConfirmationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmitter);
        initViews();
        setListeners();
        mContext = getApplicationContext();
        mPackageManager = mContext.getPackageManager();
        componentName = new ComponentName(mContext,
                FirstActivity.class);
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_row);
        mDialogProgressBar = (ProgressBar) mDialog.findViewById(R.id.pg_loadign_sign);
        mDialogText = (TextView) mDialog.findViewById(R.id.txt_retriving);

    }

    private void setListeners(){
        m15Min.setChecked(true);
        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_15MIN);
        mAccuracyAdjustment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > 0 && progress <= 25){
                    mAccuracyText.setText(ApiStates.LOW);
                    TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(PRIORITY_LOW);
                }

                if(progress > 25 && progress <= 50){
                    mAccuracyText.setText(ApiStates.MEDIUM);
                    TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(PRIORITY_MEDIUM);
                }

                if(progress > 50 && progress <= 75){
                    mAccuracyText.setText(ApiStates.NORMAL);
                    TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(PRIORITY_NORMAL);
                }

                if(progress > 75 && progress <= 100){
                    mAccuracyText.setText(ApiStates.HIGH);
                    TrackerApplication.getInstance().getDataSharedPreferences().saveUsagePreference(PRIORITY_HIGH);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mConfirmationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialogText.setText(R.string.checking_user_info);
                retrofitCallback();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.option_one:
                        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_15MIN);
                        break;
                    case R.id.option_two:
                        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_30MIN);
                        break;
                    case R.id.option_three:
                        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_45MIN);
                        break;
                    case R.id.option_four:
                        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_1HOUR);
                        break;
                    default:
                        TrackerApplication.getInstance().getDataSharedPreferences().saveUpdateFrequency(FREQUENCY_15MIN);
                        break;
                }
            }
        });
    }

    private void initViews(){
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        m15Min = (RadioButton) findViewById(R.id.option_one);
        mAccuracyAdjustment = (SeekBar) findViewById(R.id.pg_gps_accuracy);
        mAccuracyText = (TextView) findViewById(R.id.txt_accuracy_level);
        mNicknameSource = (EditText) findViewById(R.id.ed_nickname);
        mUsernameSource = (EditText) findViewById(R.id.ed_transmitter_usernme);
        mPasswordSource = (EditText) findViewById(R.id.ed_transmitter_password);
        mConfirmationButton = (Button) findViewById(R.id.btn_save_hide);
    }

    private void retrofitCallback(){
        String username =  mUsernameSource.getText().toString();
        String password = mPasswordSource.getText().toString();
        final String nickname = mNicknameSource.getText().toString();

        if(!username.equals(ApiStates.EMPTY) && !password.equals(ApiStates.EMPTY) && !nickname.equals(ApiStates.EMPTY)){
            TrackerApplication.getInstance().getmApi().ceckUserExistence(username, password, new Callback<Response>() {
                @Override
                public void success(Response accountExistence, Response response) {

                    try {
                        AccountExistence parsedObject = TrackerApplication.getInstance()
                                .getTrashCodeIgnorer()
                                .fromJsonIgnoreExtra(accountExistence.getBody().in(), AccountExistence.class);

                        if (parsedObject.getStatus().equals(ApiStates.STATUS_OK)) {
                            String id = parsedObject.getStalkerId();
                            mDialogText.setText(R.string.checking_user_info);
                            registerDevice(nickname, id);
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(mContext, R.string.wrong_user_pass, Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    mDialog.dismiss();
                }
            });
        } else {
            mDialog.dismiss();
            Toast.makeText(mContext, R.string.incomplete_fields, Toast.LENGTH_LONG).show();
        }

    }

    private void registerDevice(String nickname, String id){

        TrackerApplication.getInstance().getmApi().registerMyDevice(id, nickname, new Callback<Response>() {
            @Override
            public void success(Response reportID, Response response) {

                try {
                    ReportID parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(reportID.getBody().in(), ReportID.class);

                    if(!parsedObject.getmPostingId().equals(ApiStates.IDLE)){
                        mDialogText.setGravity(Gravity.CENTER);
                        TrackerApplication.getInstance().getDataSharedPreferences().saveReportingId(parsedObject.getmPostingId());
                        mDialogText.setText(R.string.transmitter_text);
                        mDialogProgressBar.setVisibility(View.GONE);
                        Runnable task = new Runnable() {
                            public void run() {
                                mDialog.dismiss();
                                hideApp();
                                startService(new Intent(UpdateActivity.this, LocationService.class));
                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                            }
                        };
                        worker.schedule(task, 5, TimeUnit.SECONDS);
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(mContext, R.string.nickname_exist, Toast.LENGTH_LONG).show();
                    }

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            private void hideApp(){
                mPackageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
            }

            @Override
            public void failure(RetrofitError error) {
                mDialog.dismiss();
                Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
            }
        });
    }
}
