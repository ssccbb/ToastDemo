/**
 * Copyright 2014 John Persano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sung.toastdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class HkRotationToastManager extends Handler {

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "HkToastManager";

    private static final class Messages {

        private static final int DISPLAY_CloudToast = 0x445354;
        private static final int ADD_CloudToast = 0x415354;
        private static final int REMOVE_CloudToast = 0x525354;

    }

    private static HkRotationToastManager mCloudToastManager;

    private final Queue<HkRotationToast> mQueue;

    private HkRotationToastManager() {

        mQueue = new LinkedBlockingQueue<HkRotationToast>();

    }

    public static synchronized HkRotationToastManager getInstance() {

        if (mCloudToastManager != null) {

            return mCloudToastManager;

        } else {

            mCloudToastManager = new HkRotationToastManager();

            return mCloudToastManager;

        }

    }

    public void add(HkRotationToast CloudToast) {

        if (mQueue.isEmpty()) {
            mQueue.add(CloudToast);
        }
        this.showNextCloudToast();

    }

    private void showNextCloudToast() {

        if (mQueue.isEmpty()) {

            return;

        }

        final HkRotationToast cloudToast = mQueue.peek();

        if (!cloudToast.isShowing()) {

            final Message message = obtainMessage(Messages.ADD_CloudToast);
            message.obj = cloudToast;
            sendMessage(message);

        } else {

            sendMessageDelayed(cloudToast, Messages.DISPLAY_CloudToast,
                    getDuration(cloudToast));

        }

    }

    private void sendMessageDelayed(HkRotationToast CloudToast, final int messageId, final long delay) {

        Message message = obtainMessage(messageId);
        message.obj = CloudToast;
        sendMessageDelayed(message, delay);

    }

    private long getDuration(HkRotationToast CloudToast) {

        long duration = CloudToast.getDuration();
        duration += 1000;

        return duration;

    }

    @Override
    public void handleMessage(Message message) {

        final HkRotationToast CloudToast = (HkRotationToast)
                message.obj;

        switch (message.what) {

            case Messages.DISPLAY_CloudToast:

                showNextCloudToast();

                break;

            case Messages.ADD_CloudToast:

                displayCloudToast(CloudToast);

                break;

            case Messages.REMOVE_CloudToast:

                removeCloudToast(CloudToast);

                break;

            default: {

                super.handleMessage(message);

                break;

            }

        }

    }

    private void displayCloudToast(HkRotationToast CloudToast) {

        if (CloudToast.isShowing()) {

            return;

        }

        final WindowManager windowManager = CloudToast
                .getWindowManager();

        final View toastView = CloudToast.getView();

        final WindowManager.LayoutParams params = CloudToast
                .getWindowManagerParams();

        if (windowManager != null) {
            try{
            windowManager.addView(toastView, params);
            }catch (Exception e){
                Log.e(TAG,e.getMessage()+e.getCause());
            }
        }

        sendMessageDelayed(CloudToast, Messages.REMOVE_CloudToast,
                CloudToast.getDuration() + 500);

    }

    protected void removeCloudToast(HkRotationToast CloudToast) {

        final WindowManager windowManager = CloudToast
                .getWindowManager();

        final View toastView = CloudToast.getView();

        if (windowManager != null) {

            mQueue.poll();

            windowManager.removeView(toastView);

            sendMessageDelayed(CloudToast,
                    Messages.DISPLAY_CloudToast, 500);

            /*if(HkRotationToast.getOnDismissListener() != null) {

                HkRotationToast.getOnDismissListener().onDismiss(HkRotationToast.getView());

            }*/

        }

    }

    public void cancelAllCloudToasts() {
        removeMessages(Messages.ADD_CloudToast);
        removeMessages(Messages.DISPLAY_CloudToast);
        removeMessages(Messages.REMOVE_CloudToast);
        for (HkRotationToast CloudToast : mQueue) {
            if (CloudToast.isShowing()) {
                WindowManager wm = CloudToast.getWindowManager();
                if (wm != null) {
                    wm.removeView(CloudToast.getView());
                }
            }
        }

        mQueue.clear();
    }

}
