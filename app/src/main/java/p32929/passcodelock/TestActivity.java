package p32929.passcodelock;

import android.os.Bundle;

public class TestActivity extends LockscreenHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
