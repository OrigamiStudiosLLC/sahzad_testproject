package com.nytimes.utalities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.nytimes.R;

import java.util.Locale;

public class Util {
    private static final String TAG = Util.class.getName();

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null)
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
        }
        return false;
    }

    public static void showToastMsg(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {

        }
    }

    public static Boolean isValidURL(String URL) {
        boolean isValid = false;
        isValid = Patterns.WEB_URL.matcher(URL).matches();
        if (isValid) {
            if (URL.startsWith("https://") || URL.startsWith("http://")) {
                isValid = true;
            } else {
                isValid = false;
            }
        }
        return isValid;

    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void hideSoftKeyboard(Context context, EditText input) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Context context, EditText input) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(input, 0);
        } catch (Exception ignored) {
        }
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Dialog loadingDialog(Context mContext) {
        Dialog pd = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.progress_dialog, null);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd.setContentView(view);
        return pd;
    }

    public static boolean isValidString(String value) {
        return !(value == null || value.isEmpty());
    }

    public static boolean containsEqualIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;
        if (str.equalsIgnoreCase(searchStr)) {
            return true;
        }
        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    public static void launchURL(Context context, String url) {
        if (!url.equals("") && URLUtil.isValidUrl(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        }
    }

    public static void launchShareIntent(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    private static String capitalizeAllWords(String str) {
        StringBuilder phrase = new StringBuilder();
        boolean capitalize = true;
        for (char c : str.toLowerCase().toCharArray()) {
            if (Character.isLetter(c) && capitalize) {
                phrase.append(Character.toUpperCase(c));
                capitalize = false;
                continue;
            } else if (c == ' ') {
                capitalize = true;
            }
            phrase.append(c);
        }
        return phrase.toString();
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        if (str.contains(searchStr))
            return true;

       /* for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }*/
        return false;
    }

    public static void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }


}
