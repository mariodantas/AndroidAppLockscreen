package p32929.passcodelock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import java.util.Objects;

public class LockApplicationActivity extends LockscreenHandler implements ActivityChanger {

    @SuppressWarnings("rawtypes")
    private static Class classToGoAfter;
    private final int[] passButtonIds = {
            R.id.lbtn1,
            R.id.lbtn2,
            R.id.lbtn3,
            R.id.lbtn4,
            R.id.lbtn5,
            R.id.lbtn6,
            R.id.lbtn7,
            R.id.lbtn8,
            R.id.lbtn9,
            R.id.lbtn0
    };
    //
    private final String CheckForPassword = "check";
    private final String SetPassword = "set";
    private final String SetPasswordConfirm = "set1";
    private final String ChangePassword_EnterOldPassword = "change";
    private final String ChangePassword_EnterNewPassword = "change1";
    private final String ChangePassword_EnterNewPasswordConfirm = "change2";
    String TransitoryPassword = "";
    private TextView PasswordAsDots;
    private TextView ActionMessage;
    private String EnteredPassword = "", StoredPassword = "";
    private String ReceivedAction = "";
    private boolean ShouldGoToActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_application);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            if (getString(R.string.app_name) != null && !Objects.equals(getString(R.string.app_name), "")) {
                getSupportActionBar().setTitle(getString(R.string.app_name));
            } else {
                getSupportActionBar().setTitle("Locked !");
            }
        }

        EasylockSP.init(this);
        StoredPassword = GetStoredPassword();
        initViews();

        ReceivedAction = getIntent().getExtras().getString("action", "check");
        ShouldGoToActivity = getIntent().getExtras().getBoolean("gotoactivity", true);
        if (ReceivedAction.equals(SetPassword))
            ActionMessage.setText(R.string.enter_a_new_password_txt);
        String disableStatus = "disable";
        if (ReceivedAction.equals(disableStatus)) {
            EasylockSP.put(EasyLock.PASSWORD_TO_UNLOCK_APP, null);
            ShowToast(this, getString(R.string.password_disabled_txt));
            gotoActivity();
        }
    }


    private void initViews() {
        ActionMessage = findViewById(R.id.span_text);
        ActionMessage.setTextColor(EasyLock.foregroundColor);

        PasswordAsDots = findViewById(R.id.dotText);
        PasswordAsDots.setTextColor(EasyLock.foregroundColor);

        ImageView imgLogo = findViewById(R.id.logo);
        imgLogo.setColorFilter(EasyLock.foregroundColor);

        View passwordUnderline = findViewById(R.id.password_underline);
        passwordUnderline.setBackgroundColor(EasyLock.foregroundColor);

        TextView textViewForgotPassword = findViewById(R.id.forgot_pass_textview);
        textViewForgotPassword.setTextColor(EasyLock.foregroundColor);

        Button buttonEnter = findViewById(R.id.lbtnEnter);
        buttonEnter.setTextColor(EasyLock.foregroundColor);

        ImageButton imageButtonDelete = findViewById(R.id.lbtnDelete);
        imageButtonDelete.setBackgroundColor(EasyLock.backgroundColor);
        imageButtonDelete.setColorFilter(EasyLock.foregroundColor);


        RelativeLayout relativeLayoutBackground = findViewById(R.id.background_layout);
        relativeLayoutBackground.setBackgroundColor(EasyLock.backgroundColor);


        textViewForgotPassword.setOnClickListener(EasyLock.onClickListener);

        imageButtonDelete.setOnClickListener(view -> {
            if (EnteredPassword.length() > 0) {
                EnteredPassword = EnteredPassword.substring(0, EnteredPassword.length() - 1);
            }
            PasswordAsDots.setText(EnteredPassword);
        });

        buttonEnter.setOnClickListener(view -> {

            //
            switch (ReceivedAction) {
                // Check
                case CheckForPassword:
                    if (EnteredPassword != null && EnteredPassword.equals(LockAES.Decrypt(StoredPassword))) {
                        finish();
                    } else {
                        EnteredPassword = "";
                        PasswordAsDots.setText(EnteredPassword);
                        ShowToast(this, getString(R.string.incorrect_password_txt));
                    }
                    break;

                // Set
                case SetPassword:
                    TransitoryPassword = EnteredPassword;
                    EnteredPassword = "";
                    ReceivedAction = SetPasswordConfirm;

                    ActionMessage.setText(R.string.confirm_password_txt);
                    PasswordAsDots.setText(EnteredPassword);
                    break;

                // Confirm Set
                case SetPasswordConfirm:
                    if (EnteredPassword.equals(TransitoryPassword)) {
                        EasylockSP.put(EasyLock.PASSWORD_TO_UNLOCK_APP, LockAES.Encrypt(EnteredPassword));
                        ShowToast(LockApplicationActivity.this, getString(R.string.password_is_set_txt));
                        gotoActivity();
                    } else {
                        EnteredPassword = "";
                        TransitoryPassword = "";
                        ReceivedAction = SetPassword;
                        PasswordAsDots.setText(EnteredPassword);
                        ActionMessage.setText(R.string.enter_a_new_password_txt);
                        ShowToast(LockApplicationActivity.this, getString(R.string.please_enter_a_new_password_again_txt));
                    }
                    break;

                // Change
                case ChangePassword_EnterOldPassword:
                    if (EnteredPassword.equals(StoredPassword)) {
                        EnteredPassword = "";
                        TransitoryPassword = "";
                        ReceivedAction = ChangePassword_EnterNewPassword;
                        ActionMessage.setText(R.string.enter_a_new_password_txt);
                        PasswordAsDots.setText(EnteredPassword);
                    } else {
                        EnteredPassword = "";
                        PasswordAsDots.setText(EnteredPassword);
                        ShowToast(LockApplicationActivity.this, getString(R.string.please_enter_current_password_txt));
                    }
                    break;

                // New change
                case ChangePassword_EnterNewPassword:
                    TransitoryPassword = EnteredPassword;
                    EnteredPassword = "";
                    ReceivedAction = ChangePassword_EnterNewPasswordConfirm;
                    ActionMessage.setText(R.string.confirm_password_txt);
                    PasswordAsDots.setText(EnteredPassword);
                    break;

                // Confirm change
                case ChangePassword_EnterNewPasswordConfirm:
                    if (EnteredPassword.equals(TransitoryPassword)) {
                        EasylockSP.put(EasyLock.PASSWORD_TO_UNLOCK_APP, LockAES.Encrypt(EnteredPassword));
                        ShowToast(LockApplicationActivity.this, getString(R.string.password_changed_txt));
                        gotoActivity();
                    } else {
                        EnteredPassword = "";
                        TransitoryPassword = "";
                        ReceivedAction = ChangePassword_EnterNewPassword;

                        PasswordAsDots.setText(EnteredPassword);
                        ActionMessage.setText(R.string.enter_a_new_password_txt);
                        ShowToast(LockApplicationActivity.this, getString(R.string.please_enter_a_new_password_again_txt));
                    }
                    break;
            }

        });

        for (int passButtonId : passButtonIds) {
            final Button NumberButton = findViewById(passButtonId);
            NumberButton.setTextColor(EasyLock.foregroundColor);
            NumberButton.setOnClickListener(view -> {
                if (EnteredPassword.length() >= 8) {
                    ShowToast(LockApplicationActivity.this, getString(R.string.max_8_characters_txt));
                } else {
                    EnteredPassword += NumberButton.getText().toString();
                }
                PasswordAsDots.setText(EnteredPassword);
                PasswordAsDots.refreshDrawableState();
            });
        }
    }

    private String GetStoredPassword() {
        String p = EasylockSP.getString(EasyLock.PASSWORD_TO_UNLOCK_APP, null);
        if (p != null) {
            return LockAES.Decrypt(p);
        }
        return null;
    }

    private void gotoActivity() {
        if (ShouldGoToActivity) {
            Intent intent = new Intent(LockApplicationActivity.this, classToGoAfter);
            startActivity(intent);
        }
        finish();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void activityClass(Class activityClassToGo) {
        classToGoAfter = activityClassToGo;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ReceivedAction.equals("check")) {
            finishAffinity();
        }
    }

    private static void ShowToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        //View view = toast.getView();
        toast.show();

    }

}
