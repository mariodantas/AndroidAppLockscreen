package p32929.passcodelock;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import p32929.easypasscodelock.utils.EasyLock;
import p32929.easypasscodelock.utils.LockscreenHandler;

public class MainActivity extends LockscreenHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EasyLock.setBackgroundColor(Color.BLUE);
        EasyLock.checkPassword(this);

        EasyLock.forgotPassword(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked on forgot password", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setPass(View view) {
        EasyLock.setPassword(this, TestActivity.class, true);
    }

    public void changePass(View view) {
        EasyLock.changePassword(this, TestActivity.class, true);
    }

    public void disable(View view) {
        EasyLock.disablePassword(this, TestActivity.class, true);
    }

    public void checkPass(View view) {
        EasyLock.checkPassword(this);
    }


}
