package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.razanpardazesh.razanlibs.CustomView.DialogBuilder;

import daryadelan.sandogh.zikey.com.daryadelan.R;

/**
 * Created by Zikey on 01/06/2017.
 */

public class LogWrapper {

    private static String DEBUG_TAG = "com.razanpardazesh";
    private static boolean isDebugable = true;

    public static void loge(String call, Throwable e) {
        if (!isDebugable)
            return;
        Log.e(DEBUG_TAG, call, e);
    }

    public static void logd(String call, String msg) {
        if (!isDebugable)
            return;
        Log.e(DEBUG_TAG, call + ":_________________:" + msg);
    }

    public static void showValidateError(View view, String fieldName, Boolean isEmpty) {
        if (view == null) {
            return;
        }

        String msgFormat = isEmpty ? view.getResources().getString(R.string.please_enter_variable) : view.getResources().getString(R.string.is_not_valid_value);
        showError(view, String.format(msgFormat, fieldName));
    }

    public static void showError(View view, String Message) {

        if (view == null) {
            return;
        }

        if (view instanceof TextInputLayout) {
            TextInputLayout textInputLayout = (TextInputLayout) view;
            textInputLayout.setEnabled(true);
            textInputLayout.setError(Message);
            return;
        }

        if (view instanceof TextView) {
            TextView textInputLayout = (TextView) view;
            textInputLayout.setError(Message);
            return;
        }

        if (view.getContext() instanceof AppCompatActivity) {
         new DialogBuilder().showAlert((AppCompatActivity) view.getContext(), Message);
            return;
        }

        new AlertDialog.Builder(view.getContext()).setMessage(Message).show();

        return;
    }

}
