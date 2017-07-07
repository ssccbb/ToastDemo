package com.sung.toastdemo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
* 竖向的自定义toast
* */
public class HkRotationToast {

    public static final String TAG = "HkToast";

    private Context mContext;
    private int mGravity = Gravity.CENTER;
    private int mDuration = Duration.SHORT;
    private int mXOffset = 0;
    private int mYOffset = 0;
    private RelativeLayout mRootLayout;
    private TextView mMessageTextView;
    private View mToastView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    public HkRotationToast(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        mContext = context;

        final LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mToastView = layoutInflater.inflate(R.layout.haokan_rotation_toast, null);
        mWindowManager = (WindowManager) mToastView.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        mRootLayout = (RelativeLayout) mToastView.findViewById(R.id.root_layout);
        mMessageTextView = (TextView) mToastView.findViewById(R.id.message_textview);
    }

    public void show() {
        mWindowManagerParams = new WindowManager.LayoutParams();

        mWindowManagerParams.height = 400;
        mWindowManagerParams.width = 400;
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
//        mWindowManagerParams.windowAnimations = R.style.JingYu_Widget_Toast;
        mWindowManagerParams.gravity = mGravity;
        mWindowManagerParams.x = mXOffset;
        mWindowManagerParams.y = mYOffset;
        HkRotationToastManager.getInstance().add(this);

    }

    public void setDuration(int duration) {

        if (duration > Duration.EXTRA_LONG) {
            this.mDuration = Duration.EXTRA_LONG;
        } else {
            this.mDuration = duration;

        }
    }

    public int getDuration() {

        return this.mDuration;

    }

    /**
     * Sets the message text of the {@value #TAG}.
     *
     * @param text {@link CharSequence}
     */
    public void setText(CharSequence text) {

        mMessageTextView.setText(text);

    }

    /**
     * Returns the {@value #TAG} view.
     *
     * @return {@link View}
     */
    public View getView() {

        return mToastView;

    }

    /**
     * Returns true if the {@value #TAG} is showing.
     *
     * @return boolean
     */
    public boolean isShowing() {

        return mToastView != null && mToastView.isShown();

    }

    /**
     * Returns the window manager that the {@value #TAG} is attached to.
     *
     * @return {@link WindowManager}
     */
    public WindowManager getWindowManager() {

        return mWindowManager;

    }

    /**
     * Returns the window manager layout params of the {@value #TAG}.
     *
     * @return {@link WindowManager.LayoutParams}
     */
    public WindowManager.LayoutParams getWindowManagerParams() {

        return mWindowManagerParams;

    }

    public static HkRotationToast create(Context context, CharSequence textCharSequence,
                                         int durationInteger) {

        HkRotationToast superToast = new HkRotationToast(context);
        superToast.setText(textCharSequence);
        superToast.setDuration(durationInteger);

        return superToast;

    }

    /**
     * Dismisses and removes all showing/pending {@value #TAG}.
     */
    public static void cancelAllCloudToasts() {
        HkRotationToastManager.getInstance().cancelAllCloudToasts();
    }

    /**
     * Durations for all types of SuperToasts.
     */
    public static class Duration {

        public static final int VERY_SHORT = (1500);
        public static final int SHORT = (2000);
        public static final int MEDIUM = (2750);
        public static final int LONG = (3500);
        public static final int EXTRA_LONG = (4500);

    }
}
