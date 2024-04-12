package com.app.fivepl.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.app.fivepl.R;

// Assuming you have a custom dialog box layout file named custom_dialog_box.xml
public class ProgressDisplay {

    public static View mCustomDialogBox; // Change from ProgressBar to View for custom dialog box

    @SuppressLint("UseCompatLoadingForDrawables")
    public ProgressDisplay(Activity activity) {
        try {
            ViewGroup layout = (ViewGroup) (activity).findViewById(android.R.id.content).getRootView();

            // Replace ProgressBar with a custom dialog box
            mCustomDialogBox = activity.getLayoutInflater().inflate(R.layout.custom_dialog_box, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

            RelativeLayout rl = new RelativeLayout(activity);
            rl.setGravity(Gravity.CENTER);
            rl.addView(mCustomDialogBox);
            layout.addView(rl, params);
            hideProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress() {
        if (mCustomDialogBox.getVisibility() == View.GONE)
            mCustomDialogBox.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        new Handler().postDelayed(() -> mCustomDialogBox.setVisibility(View.GONE), 500); // 2000 milliseconds = 2 seconds
    }

}
