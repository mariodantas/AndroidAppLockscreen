package p32929.easypasscodelock.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import p32929.easypasscodelock.Interfaces.ActivityChanger;
import p32929.easypasscodelock.activities.LockApplicationActivity;

/**
 * Created by p32929 on 7/17/2018.
 */

@SuppressWarnings("rawtypes")
public class EasyLock {
    public static int backgroundColor = Color.parseColor("#000000");
    public static View.OnClickListener onClickListener;
    private static ActivityChanger activityChanger;

    private static void init(Context context) {
        EasylockSP.init(context);
        if (activityChanger == null) {
            activityChanger = new LockApplicationActivity();
        }
    }

    public static void setPassword(Context context, Class activityClassToGo, boolean ShouldGoToActivity) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockApplicationActivity.class);
        intent.putExtra("action", "set");
        intent.putExtra("gotoactivity", ShouldGoToActivity);
        context.startActivity(intent);
    }

    public static void changePassword(Context context, Class activityClassToGo, boolean ShouldGoToActivity) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockApplicationActivity.class);
        intent.putExtra("action", "change");
        intent.putExtra("gotoactivity", ShouldGoToActivity);
        context.startActivity(intent);
    }

    public static void disablePassword(Context context, Class activityClassToGo, boolean ShouldGoToActivity) {
        init(context);
        activityChanger.activityClass(activityClassToGo);
        Intent intent = new Intent(context, LockApplicationActivity.class);
        intent.putExtra("action", "disable");
        intent.putExtra("gotoactivity", ShouldGoToActivity);
        context.startActivity(intent);
    }

    public static void checkPassword(Context context) {
        init(context);
        if (EasylockSP.getString("password_to_unlock_app", null) != null) {
            Intent intent = new Intent(context, LockApplicationActivity.class);
            intent.putExtra("action", "check");
            context.startActivity(intent);
        }
    }

    public static void setBackgroundColor(int backgroundColor) {
        EasyLock.backgroundColor = backgroundColor;
    }

    public static void forgotPassword(View.OnClickListener onClickListener) {
        EasyLock.onClickListener = onClickListener;
    }

}
