package app.nostalking.com.locationtracker.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import app.nostalking.com.locationtracker.R;
import app.nostalking.com.locationtracker.model.AccountExistence;
import app.nostalking.com.locationtracker.model.SimpleConfirmation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Applaudo Dev on 4/10/2015.
 */
public class LogInActivity extends Activity {
    private boolean mSwitchState;
    private Context mContext;
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

        boolean credentials = TrackerApplication.getInstance().getDataSharedPreferences().isLoged();

        if(credentials){
            startActivity(new Intent(LogInActivity.this, ReceptorActivity.class));
        } else {
            initViews();
            setListeners();
            mLogginIn.setVisibility(View.GONE);
        }

    }

    public void initViews() {
        mContext = getApplicationContext();
        mKeepMeLogIn = (Switch) findViewById(R.id.switch_keep_logged);
        mActionButtons = (LinearLayout) findViewById(R.id.ll_action_buttons);
        mLogginIn = (ProgressBar) findViewById(R.id.pb_loggin_in);
        mUsernameSource = (EditText) findViewById(R.id.ed_username);
        mPasswordSource = (EditText) findViewById(R.id.ed_password);
        mLogginButton = (Button) findViewById(R.id.btn_log_in);
        mSignUpButton = (Button) findViewById(R.id.btn_sign_up);
    }

    public void setListeners() {
        mKeepMeLogIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mSwitchState = true;
                }else{
                    mSwitchState = false;
                }
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

                if (!username.equals("") && !password.equals("")) {
                    mActionButtons.setVisibility(View.GONE);
                    mLogginIn.setVisibility(View.VISIBLE);
                    actionLogIn(username, password);
                } else {
                    Toast.makeText(mContext, "all the fields are require to log in", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public void signUpDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sign_up_form);
        dialog.setTitle("Sign up");

        final EditText DialogUsernameSurce = (EditText) dialog.findViewById(R.id.ed_dialog_username);
        final EditText DialogEnailSource = (EditText) dialog.findViewById(R.id.ed_dialig_email);
        final EditText DialogPasswordSource = (EditText) dialog.findViewById(R.id.ed_dialog_password);
        final ProgressBar DialogCreatingAccount = (ProgressBar) dialog.findViewById(R.id.pg_creating_account);

        final Button DialogLogginButton = (Button) dialog.findViewById(R.id.btn_dialog_sign_up);
        DialogCreatingAccount.setVisibility(View.GONE);

        DialogLogginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = DialogUsernameSurce.getText().toString();
                String password = DialogPasswordSource.getText().toString();
                String email = DialogEnailSource.getText().toString();

                if (!username.equals("") && !password.equals("") && !email.equals("")) {
                    DialogCreatingAccount.setVisibility(View.VISIBLE);
                    DialogLogginButton.setVisibility(View.GONE);
                    signUp(username, password, email, dialog,DialogLogginButton, DialogCreatingAccount );
                } else {
                    Toast.makeText(getApplicationContext(), "All field are required to create an account", Toast.LENGTH_LONG).show();
                    DialogCreatingAccount.setVisibility(View.GONE);
                    DialogLogginButton.setVisibility(View.VISIBLE);
                }
            }
        });

        dialog.show();
    }

    private void actionLogIn(final String username, final String password){

        TrackerApplication.getInstance().getmApi().ceckUserExistence(username, password, new Callback<AccountExistence>() {
            @Override
            public void success(AccountExistence accountExistence, Response response) {
                if (accountExistence.getStatus().equals("200")) {
                    String id = accountExistence.getStalkerId();

                    if(mSwitchState){
                        TrackerApplication.getInstance().getDataSharedPreferences().stayLogged(true);
                    }else{
                        TrackerApplication.getInstance().getDataSharedPreferences().stayLogged(false);
                    }

                    TrackerApplication.getInstance().getDataSharedPreferences().storeMyId(id);
                    startActivity(new Intent(LogInActivity.this, ReceptorActivity.class));
                } else {
                    mActionButtons.setVisibility(View.VISIBLE);
                    mLogginIn.setVisibility(View.GONE);
                    Toast.makeText(mContext, "account does not exist, please try again or create an account", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "ups, something went wrong", Toast.LENGTH_LONG).show();
                mActionButtons.setVisibility(View.VISIBLE);
                mLogginIn.setVisibility(View.GONE);
            }
        });
    }

    private void signUp(String mUsername, String mPassword, String mEmail, final Dialog mDialog, final Button mDialogLogginButton,
    final ProgressBar mDialogCreatingAccount){
        TrackerApplication.getInstance().getmApi().registerUser(mUsername, mPassword, mEmail, new Callback<SimpleConfirmation>() {
            @Override
            public void success(SimpleConfirmation simpleConfirmation, Response response) {
                if (simpleConfirmation.getmStatus().equals("200")) {
                    Toast.makeText(mContext, "account successfully created", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                } else {
                    Toast.makeText(mContext, "this username is alredy in use", Toast.LENGTH_LONG).show();
                    mDialogLogginButton.setVisibility(View.VISIBLE);
                    mDialogCreatingAccount.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(mContext, "ups something went wrong", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });
    }

}


