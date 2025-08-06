//package de.dlyt.yanndroid.sudoku;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.style.ForegroundColorSpan;
//import android.view.animation.Animation;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.preference.PreferenceManager;
//
//import dev.oneuiproject.oneui.layout.SplashLayout;
//
//public class SplashActivity extends AppCompatActivity {
//
//    private boolean launchCanceled = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//
//        SplashLayout splashView = findViewById(R.id.splash);
//
//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dev_enabled", false)) {
//            Spannable dev_text = new SpannableString(getString(R.string._dev));
//            dev_text.setSpan(new ForegroundColorSpan(getColor(R.color.orange)), 0, dev_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((TextView) splashView.findViewById(R.id.oui_splash_text)).append(dev_text);
//        }
//
//        Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(splashView::startSplashAnimation, 500);
//
//        splashView.setSplashAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (!launchCanceled) launchApp();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//    }
//
//    private void launchApp() {
//        Intent intent = new Intent().setClass(getApplicationContext(), MainActivity.class);
//        intent.setData(getIntent().getData()); //transfer intent data -> game import
//        intent.setAction(getIntent().getAction()); //transfer intent action -> shortcuts
//        startActivity(intent);
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        launchCanceled = true;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (launchCanceled) launchApp();
//    }
//}


package de.dlyt.yanndroid.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import dev.oneuiproject.oneui.layout.SplashLayout;

/**
 * An initial splash screen that appears when the app is launched.
 * It handles the entry animation and safely transitions to the MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    // A constant for the splash animation delay, making it easier to change.
    private static final long SPLASH_START_DELAY_MS = 500;

    /**
     * Flag to prevent launching MainActivity if the user navigates away
     * from the splash screen before the animation completes.
     */
    private boolean isLaunchCancelled = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SplashLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout = findViewById(R.id.splash);

        setupDevModeIndicator();
        startDelayedSplashAnimation();
    }

    /**
     * If the user pauses the app (e.g., presses home), we cancel the upcoming launch.
     */
    @Override
    protected void onPause() {
        super.onPause();
        isLaunchCancelled = true;
    }

    /**
     * If the user returns to the app and the launch was previously cancelled,
     * launch the main activity immediately.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isLaunchCancelled) {
            launchApp();
        }
    }

    /**
     * Checks for the developer mode flag in SharedPreferences and, if enabled,
     * appends a colored "DEV" tag to the splash screen text.
     */
    private void setupDevModeIndicator() {
        boolean isDevModeEnabled = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("dev_enabled", false);

        if (isDevModeEnabled) {
            TextView splashTextView = splashLayout.findViewById(R.id.oui_splash_text);
            if (splashTextView != null) {
                Spannable devText = new SpannableString(getString(R.string._dev));
                int orangeColor = ContextCompat.getColor(this, R.color.orange);
                devText.setSpan(new ForegroundColorSpan(orangeColor), 0, devText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                splashTextView.append(" "); // Add a space for separation
                splashTextView.append(devText);
            }
        }
    }

    /**
     * Starts the splash screen animation after a defined delay.
     */
    private void startDelayedSplashAnimation() {
        handler.postDelayed(this::beginAnimation, SPLASH_START_DELAY_MS);
    }

    private void beginAnimation() {
        // Ensure the activity is still running before starting the animation
        if (isFinishing()) {
            return;
        }
        splashLayout.startSplashAnimation();
        splashLayout.setSplashAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No action needed
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Launch the app only if the user hasn't navigated away
                if (!isLaunchCancelled) {
                    launchApp();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No action needed
            }
        });
    }

    /**
     * Launches the MainActivity, passing along any intent data from the original launch intent.
     * This is crucial for handling things like game imports or app shortcuts.
     */
    private void launchApp() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        // Transfer data and action from the original intent (e.g., for game import)
        mainIntent.setData(getIntent().getData());
        mainIntent.setAction(getIntent().getAction());

        startActivity(mainIntent);
        // Apply a standard fade transition for a smooth entry.
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        // Finish this activity so it's removed from the back stack.
        finish();
    }
}