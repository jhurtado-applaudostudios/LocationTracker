package app.nostalking.com.locationtracker.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.intefaces.AnimationListener;
import app.nostalking.com.locationtracker.model.AccountExistence;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import app.nostalking.com.locationtracker.utils.Animation;
import app.nostalking.com.locationtracker.utils.ApiStates;
import app.nostalking.com.locationtracker.utils.ExplodeAnimation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public class LogInActivity extends Activity {
    private boolean mSwitchState;
    private Context mContext;
    private ImageView mLogo;
    private LinearLayout mActionButtons;
    private ProgressBar mLogginIn;
    private EditText mUsernameSource;
    private EditText mPasswordSource;
    private Button mLogginButton;
    private Button mSignUpButton;
    private Switch mKeepMeLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        boolean credentials = TrackerApplication.getInstance().getDataSharedPreferences().isLogged();

        if(credentials){
            startActivity(new Intent(LogInActivity.this, ReceptorActivity.class));
        } else {
            initViews();
            setListeners();
            mLogginIn.setVisibility(View.GONE);
        }

    }

    void initViews() {
        mContext = getApplicationContext();
        mLogo = (ImageView) findViewById(R.id.img_lloggin_logo);
        mKeepMeLogIn = (Switch) findViewById(R.id.switch_keep_logged);
        mActionButtons = (LinearLayout) findViewById(R.id.ll_action_buttons);
        mLogginIn = (ProgressBar) findViewById(R.id.pb_loggin_in);
        mUsernameSource = (EditText) findViewById(R.id.ed_username);
        mPasswordSource = (EditText) findViewById(R.id.ed_password);
        mLogginButton = (Button) findViewById(R.id.btn_log_in);
        mSignUpButton = (Button) findViewById(R.id.btn_sign_up);
    }

    void setListeners() {
        mKeepMeLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSwitchState = isChecked;
            }
        });

        mLogginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpDialog();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsernameSource.getText().toString();
                String password = mPasswordSource.getText().toString();

                if (!username.equals(ApiStates.EMPTY) && !password.equals(ApiStates.EMPTY)) {
                    mActionButtons.setVisibility(View.GONE);
                    mLogginIn.setVisibility(View.VISIBLE);
                    actionLogIn(username, password);
                } else {
                    Toast.makeText(mContext, R.string.incomplete_fields, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    void signUpDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sign_up_form);
        dialog.setTitle(R.string.sign_up);

        final EditText DialogUsernameSource = (EditText) dialog.findViewById(R.id.ed_dialog_username);
        final EditText DialogEmailSource = (EditText) dialog.findViewById(R.id.ed_dialig_email);
        final EditText DialogPasswordSource = (EditText) dialog.findViewById(R.id.ed_dialog_password);
        final ProgressBar DialogCreatingAccount = (ProgressBar) dialog.findViewById(R.id.pg_creating_account);

        final Button DialogLoggingButton = (Button) dialog.findViewById(R.id.btn_dialog_sign_up);
        DialogCreatingAccount.setVisibility(View.GONE);

        DialogLoggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = DialogUsernameSource.getText().toString();
                String password = DialogPasswordSource.getText().toString();
                String email = DialogEmailSource.getText().toString();

                if (!username.equals(ApiStates.EMPTY) && !password.equals(ApiStates.EMPTY) && !email.equals(ApiStates.EMPTY)) {
                    DialogCreatingAccount.setVisibility(View.VISIBLE);
                    DialogLoggingButton.setVisibility(View.GONE);
                    signUp(username, password, email, dialog, DialogLoggingButton, DialogCreatingAccount);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.incomplete_fields, Toast.LENGTH_LONG).show();
                    DialogCreatingAccount.setVisibility(View.GONE);
                    DialogLoggingButton.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    private void actionLogIn(final String username, final String password){

        TrackerApplication.getInstance().getmApi().ceckUserExistence(username, password, new Callback<Response>() {
            @Override
            public void success(Response accountExistence, Response response) {
                try {
                    AccountExistence parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(accountExistence.getBody().in(), AccountExistence.class);

                    if (parsedObject.getStatus().equals(ApiStates.STATUS_OK)) {
                        String id = parsedObject.getStalkerId();

                        if(mSwitchState){
                            TrackerApplication.getInstance().getDataSharedPreferences().stayLogged(true);
                        }else{
                            TrackerApplication.getInstance().getDataSharedPreferences().stayLogged(false);
                        }

                        TrackerApplication.getInstance().getDataSharedPreferences().storeMyId(id);

                        mLogginIn.setVisibility(View.INVISIBLE);
                        new ExplodeAnimation(mLogo)
                                .setExplodeMatrix(ExplodeAnimation.MATRIX_3X3)
                                .setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .setListener(new AnimationListener() {
                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        startActivity(new Intent(LogInActivity.this, ReceptorActivity.class));
                                    }
                                })
                                .animate();

                    } else {
                        mActionButtons.setVisibility(View.VISIBLE);
                        mLogginIn.setVisibility(View.GONE);
                        Toast.makeText(mContext, R.string.account_inexistence, Toast.LENGTH_LONG).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                mActionButtons.setVisibility(View.VISIBLE);
                mLogginIn.setVisibility(View.GONE);
            }
        });
    }

    private void signUp(String mUsername, String mPassword, String mEmail, final Dialog mDialog, final Button mDialogLogginButton,
    final ProgressBar mDialogCreatingAccount){
        TrackerApplication.getInstance().getmApi().registerUser(mUsername, mPassword, mEmail, new Callback<Response>() {
            @Override
            public void success(Response simpleConfirmation, Response response) {
                try {
                    SimpleConfirmation parsedObject = TrackerApplication.getInstance()
                            .getTrashCodeIgnorer()
                            .fromJsonIgnoreExtra(simpleConfirmation.getBody().in(), SimpleConfirmation.class);

                    if (parsedObject.getmStatus().equals(ApiStates.STATUS_OK)) {
                        Toast.makeText(mContext, R.string.account_created, Toast.LENGTH_LONG).show();
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(mContext, R.string.user_in_user, Toast.LENGTH_LONG).show();
                        mDialogLogginButton.setVisibility(View.VISIBLE);
                        mDialogCreatingAccount.setVisibility(View.GONE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext,R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

}


