package com.chandigarhadmin.receviers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.chandigarhadmin.interfaces.OTPListener;

/**
 * Created by harendrasinghbisht on 23/09/17.
 */

public class OtpReader extends BroadcastReceiver {

    /**
     * Constant TAG for logging key.
     */
    private static final String TAG = "OtpReader";

    /**
     * The bound OTP Listener that will be trigerred on receiving message.
     */
    private static OTPListener otpListener;

    /**
     * The Sender number string.
     */
    private static String receiverString;

    /**
     * Binds the sender string and listener for callback.
     *
     * @param listener
     * @param sender
     */
    public static void bind(OTPListener listener, String sender) {
        otpListener = listener;
        receiverString = sender;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final Object[] pdusArr = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusArr.length; i++) {

                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i]);
                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Log.i(TAG, "senderNum: " + senderNum + " message: " + message);

                if (!TextUtils.isEmpty(receiverString) && senderNum.contains(receiverString)) { //If message received is from required number.
                    //If bound a listener interface, callback the overriden method.
                    if (otpListener != null) {
                        otpListener.otpReceived(message);
                    }
                }
            }
        }
    }

    /**
     * Unbinds the sender string and listener for callback.
     */
    public static void unbind() {
        otpListener = null;
        receiverString = null;
    }
}