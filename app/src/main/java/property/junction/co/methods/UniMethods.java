package property.junction.co.methods;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.Inflater;

import property.junction.co.R;
import property.junction.co.notification.FcmNotificationsSender;

public class UniMethods {

    Notifier notifier;
    String value = "0";
    String final_value = "";
    LayoutInflater inflater;

    public UniMethods() {
    }

    public interface Notifier {
        void onTaskCompleted(String s, boolean b, Object o);
    }

    public UniMethods(Notifier notifier) {
        this.notifier = notifier;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void addListenerToCollapseAppBarLayoutWhenFocused(AppBarLayout app_bar_layout, EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View til_view, boolean b) {
                if (b) {
                    app_bar_layout.setExpanded(false, true);
                }
            }
        });
    }

    public void addListenerToCollapseAppBarLayoutWhenFocused(AppBarLayout app_bar_layout, NestedScrollView nsv, EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View til_view, boolean b) {
                if (b) {
                    app_bar_layout.setExpanded(false, true);
                    scrollToView(nsv, editText);
                }
            }
        });
    }

    public void scrollToView(NestedScrollView scrollView, View view) {
        //rootView.clearFocus();
        int[] locationOfView = new int[2];
        view.getLocationOnScreen(locationOfView);
        int location_of_view = locationOfView[1];

        int[] locationOfScrollView = new int[2];
        scrollView.getLocationOnScreen(locationOfScrollView);
        int location_of_scroll_view = locationOfScrollView[1];

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, location_of_view + scrollView.getScrollY() - location_of_scroll_view);
            }
        });
    }

    public boolean checkIfEmpty(TextInputLayout editText, String error) {
        boolean b;
        if (Objects.requireNonNull(editText.getEditText()).getText().toString().trim().equals("")) {
            editText.setError(error);
            editText.requestFocus();
            b = true;
        } else {
            editText.setError(null);
            b = false;
        }
        return b;
    }

    public void sendNotification(String token_name, String title, String body
            , Context context) {
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                "/topics/" + token_name
                , title
                , body
                , context);
        notificationsSender.SendNotifications();
    }

    public void fadingView(View view, float from, float to, int duration, UniMethods uniMethods) {
        if (from > to) {
            if (view.getVisibility() == View.VISIBLE) {
                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", from, to);
                fadeOut.setDuration(duration);
                fadeOut.start();
                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (uniMethods != null) {
                            uniMethods.notifier.onTaskCompleted("none", true, "none");
                        }
                    }

                    @Override
                    public void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
                        super.onAnimationStart(animation, isReverse);
                    }
                });
            }
        } else {
            view.setAlpha(0);
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", from, to);
            fadeOut.setDuration(duration);
            fadeOut.start();
            fadeOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (uniMethods != null) {
                        uniMethods.notifier.onTaskCompleted("1", true, "none");
                    }
                }

                @Override
                public void onAnimationStart(@NonNull Animator animation, boolean isReverse) {
                    super.onAnimationStart(animation, isReverse);
                }
            });
        }
    }

    public void searchArrayList(ArrayList<String> list, String search, UniMethods uniMethods) {
        String line = "";
        String word = "";
        int i = 0;
        boolean result = false;
        for (String str : list) {
            for (String wrd : str.split(" ")) {
                if (wrd.toLowerCase().startsWith(search.toLowerCase())) {
                    line = str;
                    word = wrd;
                    i++;
                    result = true;
                } else if (str.toLowerCase().startsWith(search.toLowerCase())) {
                    line = str;
                    word = wrd;
                    i++;
                    result = true;
                }
            }
        }
        uniMethods.notifier.onTaskCompleted(word, result, i);
    }

    public void makeRecViewV(RecyclerView rc, Context context) {
        rc.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);
    }

    public void makeRecViewScrollTop(RecyclerView rc, Context context) {
        rc.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.scrollToPositionWithOffset(2, 20);
        lm.setOrientation(RecyclerView.VERTICAL);
        rc.setLayoutManager(lm);
    }

    public void makeRecViewVStack(RecyclerView rc, Context context) {
        rc.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.VERTICAL);
        lm.setStackFromEnd(true);
        rc.setLayoutManager(lm);
    }

    public void makeRecViewH(RecyclerView rc, Context context) {
        rc.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rc.setLayoutManager(lm);
    }

    public String generateOTP(int len) {
        String numbers = "0123456789";
        Random rndm_method = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    public void changeTextColor(Context context, TextView t, int color) {
        t.setTextColor(context.getResources().getColorStateList(color));
    }

    public String[] arrayListToStringArray(ArrayList<String> list) {
        return list.toArray(new String[0]);
    }

    public void selectButton(Context context, View selected_view, View de_selected_view) {
        ImageView iv = selected_view.findViewWithTag("iv" + selected_view.getTag().toString());
        TextView tv = selected_view.findViewWithTag("tv" + selected_view.getTag().toString());
        iv.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_theme_color)));
        changeTextColor(context, tv, R.color.app_theme_color);
        tv.setTypeface(null, Typeface.BOLD);

        if (de_selected_view != null) {
            ImageView de_iv = de_selected_view.findViewWithTag("iv" + de_selected_view.getTag().toString());
            TextView de_tv = de_selected_view.findViewWithTag("tv" + de_selected_view.getTag().toString());
            de_iv.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_hint)));
            changeTextColor(context, de_tv, R.color.text_hint);
            de_tv.setTypeface(null, Typeface.NORMAL);
        }
    }

    public void sendOnWhatsappMultipleImages(Context context, String number, ArrayList<Uri> images) {
        Intent IntentW = new Intent(Intent.ACTION_SEND_MULTIPLE);
        IntentW.putExtra("jid", number.replace("+", "") + "@s.whatsapp.net");
        IntentW.setType("image/jpg");
        IntentW.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
        IntentW.setPackage(getSp(context, "SETTINGS", "default_whatsapp"));
        IntentW.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(IntentW);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    public void sendOnWhatsappSingleImageWithText(Context context, String number, Uri image, String text) {
        Intent IntentW = new Intent(Intent.ACTION_SEND);
        IntentW.putExtra("jid", number + "@s.whatsapp.net");
        IntentW.setType("image/jpg");
        IntentW.putExtra(Intent.EXTRA_STREAM, image);
        IntentW.setType("text/plain");
        IntentW.putExtra(Intent.EXTRA_TEXT, text);
        IntentW.setPackage(getSp(context, "SETTINGS", "default_whatsapp"));
        IntentW.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(IntentW);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    public void sendOnWhatsappOnlyText(Context context, String number, String text) {
        String msgurl = "https://api.whatsapp.com/send?phone=+" + number + "&text=" + text;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(msgurl));
        i.setPackage(getSp(context, "SETTINGS", "default_whatsapp"));
        context.startActivity(i);
    }

    public void openInstantWhatsapp(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra("jid", number + "@s.whatsapp.net");
        intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
        intent.setPackage(getSp(context, "SETTINGS", "default_whatsapp"));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }

    public void getUriFromUrl(Context context, ArrayList<String> url_list, ArrayList<Uri> uri_list, UniMethods uniMethods) {
        for (String url_as_string : url_list) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    try {
                        url = new URL(url_as_string);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    HttpURLConnection connection = null;
                    try {
                        assert url != null;
                        connection = (HttpURLConnection)
                                url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert connection != null;
                    connection.setDoInput(true);
                    try {
                        connection.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    InputStream input = null;
                    try {
                        input = connection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap imgBitmap = BitmapFactory.decodeStream(input);
                    Random rand = new Random();
                    int randNo = rand.nextInt(100000);
                    String imgBitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), imgBitmap, "IMG:" + randNo, null);
                    Uri imgBitmapUri = Uri.parse(imgBitmapPath);
                    uri_list.add(imgBitmapUri);
                    if (url_list.size() == uri_list.size()) {
                        uniMethods.notifier.onTaskCompleted(String.valueOf(uri_list.get(0)), true, uri_list);
                    }
                }
            }).start();
        }
    }

    public void copyTextToClipBoard(Context context, String string) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text", string);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void getFilteredList(ArrayList<HashMap<String, Object>> list
            , String field, String filter) {
        if (filter.equals("All")) {
            notifier.onTaskCompleted(filter, true, sortList(list, "date_added", "ascending"));
        } else if (field.equals("budget")) {
            if (!filter.equals("")) {
                for (HashMap<String, Object> hashMap : list) {
                    if (Double.parseDouble(String.valueOf(hashMap.get("budget_min"))) <= Double.parseDouble(filter)
                            && Double.parseDouble(String.valueOf(hashMap.get("budget_max"))) >= Double.parseDouble(filter)) {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
            }
        } else {
            for (HashMap<String, Object> hashMap : list) {
                if (String.valueOf(hashMap.get(field)).toLowerCase().contains(filter.toLowerCase())) {
                    notifier.onTaskCompleted(filter, true, hashMap);
                }
            }
        }
    }

    public ArrayList<String> getFilteredList(ArrayList<String> list
            , String keyword, int limit_result) {
        ArrayList<String> to_add = new ArrayList<>();
        if (!keyword.equals("All")) {
            for (String word : list) {
                if (word.toLowerCase().contains(keyword.toLowerCase())) {
                    int index_of_adding = word.toLowerCase().indexOf(keyword.toLowerCase());
                    to_add.add(String.valueOf(addLeadingCharacters("0", 5, index_of_adding)) + "-" + word);
                }
            }
            list.clear();
            Collections.sort(to_add);
            if (limit_result == 0) {
                for (String s : to_add) {
                    list.add(s.substring(s.indexOf("-") + 1));
                }
            } else {
                if (limit_result > to_add.size()) {
                    limit_result = to_add.size();
                }
                for (int i = 0; limit_result > i; i++) {
                    String s = to_add.get(i);
                    list.add(s.substring(s.indexOf("-") + 1));
                }
            }
        }
        return list;
    }

    public void simpleDocListSearch(ArrayList<DocumentSnapshot> list
            , String field, String search, UniMethods uniMethods) {
        ArrayList<DocumentSnapshot> filtered_list = new ArrayList<>();
        for (DocumentSnapshot hashMap : list) {
            if (String.valueOf(hashMap.get(field)).toLowerCase().contains(search.toLowerCase())) {
                filtered_list.add(hashMap);
            }
        }
        uniMethods.notifier.onTaskCompleted(search, true, filtered_list);
    }

    public void simpleListSearch(ArrayList<HashMap<String, Object>> list
            , String field, String search, UniMethods uniMethods) {
        ArrayList<HashMap<String, Object>> filtered_list = new ArrayList<>();
        for (HashMap<String, Object> hashMap : list) {
            if (String.valueOf(hashMap.get(field)).toLowerCase().contains(search.toLowerCase())) {
                if (String.valueOf(hashMap.get(field)).toLowerCase().startsWith(search.toLowerCase())) {
                    filtered_list.add(0, hashMap);
                } else {
                    filtered_list.add(hashMap);
                }
            }
        }
        uniMethods.notifier.onTaskCompleted("getFilteredList", true, filtered_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCatFilteredList(ArrayList<HashMap<String, Object>> list, String filter) {
        switch (filter) {
            case "new_added_last":
                notifier.onTaskCompleted(filter, true, sortList(list, "date_added", "descending"));
                break;
            case "budget_high_to_low":
                notifier.onTaskCompleted(filter, true, sortList(list, "budget_min", "ascending"));
                break;
            case "budget_low_to_high":
                notifier.onTaskCompleted(filter, true, sortList(list, "budget_min", "descending"));
                break;
            case "all_clients":
            case "new_added_first":
                notifier.onTaskCompleted(filter, true, sortList(list, "date_added", "ascending"));
                break;
            case "not_connected_on_whatsapp":
                for (HashMap<String, Object> hashMap : list) {
                    if (Boolean.TRUE.equals(hashMap.get("details_sent_on_whatsapp"))) {
                    } else {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
                break;
            case "not_talked_on_call":
                for (HashMap<String, Object> hashMap : list) {
                    if (Boolean.TRUE.equals(hashMap.get("talked_on_call"))) {
                    } else {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
                break;
            case "ready_for_visit":
                for (HashMap<String, Object> hashMap : list) {
                    if (Boolean.TRUE.equals(hashMap.get("ready_for_visit"))) {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
                break;
            case "added_today":
                String date = getCurrentTime().substring(0, 6) + "000000";
                for (HashMap<String, Object> hashMap : list) {
                    if (Double.parseDouble(date) <= Double.parseDouble(String.valueOf(hashMap.get("date_added")))) {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
                break;
            case "visit_done":
                for (HashMap<String, Object> hashMap : list) {
                    if (Boolean.TRUE.equals(hashMap.get("visit_done"))) {
                        notifier.onTaskCompleted(filter, true, hashMap);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onPressNew(View btn, View prog) {
        btn.setVisibility(View.INVISIBLE);
        prog.setVisibility(View.VISIBLE);
    }

    public void onReleaseNew(View btn, View prog) {
        prog.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    public void onPress(View btn, View prog) {
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        prog.setVisibility(View.VISIBLE);

    }

    public void onRelease(View btn, View prog) {
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        prog.setVisibility(View.INVISIBLE);
    }

    public void shakeView(View view) {
        YoYo.with(Techniques.Shake)
                .duration(500)
                .repeat(1)
                .playOn(view);
    }

    public void zoomInOutView(View view) {
        YoYo.with(Techniques.Tada)
                .playOn(view);
    }

    public void applyProgress(Context context, ConstraintLayout constraintLayout, View parent_view, View progress_bar, int size_in_dp, UniMethods uniMethods) {
        progress_bar.setLayoutParams(new ConstraintLayout.LayoutParams(getPxValueOfDp(context, size_in_dp), getPxValueOfDp(context, size_in_dp)));
        parent_view.setVisibility(View.VISIBLE);
        fadingView(parent_view, 1, 0, 300, new UniMethods(new Notifier() {
            @Override
            public void onTaskCompleted(String s, boolean b, Object o) {
                parent_view.setVisibility(View.INVISIBLE);
                parent_view.setAlpha(1);
                progress_bar.setVisibility(View.INVISIBLE);
                fadingView(progress_bar, 0, 1, 300, new UniMethods(new Notifier() {
                    @Override
                    public void onTaskCompleted(String s, boolean b, Object o) {
                        progress_bar.setVisibility(View.VISIBLE);
                        uniMethods.notifier.onTaskCompleted("", true, "");
                    }
                }));
            }
        }));

        ConstraintSet set = new ConstraintSet();

        constraintLayout.addView(progress_bar);
        // cannot set id after add
        set.clone(constraintLayout);

        set.connect(progress_bar.getId(), ConstraintSet.TOP, parent_view.getId(), ConstraintSet.TOP, 0);
        set.connect(progress_bar.getId(), ConstraintSet.BOTTOM, parent_view.getId(), ConstraintSet.BOTTOM, 0);
        set.connect(progress_bar.getId(), ConstraintSet.START, parent_view.getId(), ConstraintSet.START, 0);
        set.connect(progress_bar.getId(), ConstraintSet.END, parent_view.getId(), ConstraintSet.END, 0);
        set.applyTo(constraintLayout);
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(250);
        v.startAnimation(anim);
    }

    public String convertInTime(String digits) {
        if (digits == null || digits.equals("") || digits.length() < 6) {
            return "Not available";
        } else if (digits.length() == 6) {
            String year = digits.substring(0, 2);
            String month = digits.substring(2, 4);
            String day = digits.substring(4, 6);
            /*String hour = "";
            String minute = digits.substring(8, 10);
            String second = digits.substring(10, 12);*/
            /*String format = "";
            int i = Integer.parseInt(digits.substring(6, 8));
            if (i > 12) {
                hour = String.valueOf(i - 12);
                format = " PM";
            } else {
                hour = String.valueOf(i);
                format = " AM";
            }
            if (hour.length() < 2) {
                hour = "0" + hour;
            }*/
            return day + "/" + month + "/20" + year;
        } else {
            String year = digits.substring(0, 2);
            String month = digits.substring(2, 4);
            String day = digits.substring(4, 6);
            String hour = "";
            String minute = digits.substring(8, 10);
            String second = digits.substring(10, 12);
            String format = "";
            int i = Integer.parseInt(digits.substring(6, 8));
            if (i > 12) {
                hour = String.valueOf(i - 12);
                format = " PM";
            } else {
                hour = String.valueOf(i);
                format = " AM";
            }
            if (hour.length() < 2) {
                hour = "0" + hour;
            }
            return day + "/" + month + "/20" + year + ", " + hour + ":" + minute + format;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getModernTime(String digits) {

        if (digits == null || digits.equals("")) {
            return "Not available";
        } else {
            if (digits.length() == 6) {
                String year = digits.substring(0, 2);
                String month = digits.substring(2, 4);
                String day = digits.substring(4, 6);

            } else if (digits.length() == 12) {
                String year = digits.substring(0, 2);
                String month = digits.substring(2, 4);
                String day = digits.substring(4, 6);
                String hour = "";
                String minute = digits.substring(8, 10);
                String second = digits.substring(10, 12);
                String format = "";
                int i = Integer.parseInt(digits.substring(6, 8));
                if (i > 12) {
                    hour = String.valueOf(i - 12);
                    format = " PM";
                } else {
                    hour = String.valueOf(i);
                    format = " AM";
                }
                if (hour.length() < 2) {
                    hour = "0" + hour;
                }

                long current_time = Long.parseLong(getCurrentTime());
                long given_time = Long.parseLong(digits);
                long difference = current_time - given_time;

                return String.valueOf(difference);

            } else {

            }

        }
        return "0";
    }

    public HashMap<String, Object> contactExists(Context context, String number) {
        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        //String name = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
        try {
            if (cur.moveToFirst()) {
                String name = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("boolean", true);
                hashMap.put("name", name);
                return hashMap;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("boolean", false);
        hashMap.put("name", "none");
        return hashMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getAgoTime(String time) {
        if (time == null || time.length() < 12) {
            return "Not Available";
        }
        String current_time = String.valueOf(Long.parseLong(getCurrentTime()));
        String year_current_time = current_time.substring(0, 2);
        String month_current_time = current_time.substring(2, 4);
        String day_current_time = current_time.substring(4, 6);

        String hour_current_time = current_time.substring(6, 8);
        String minute_current_time = current_time.substring(8, 10);
        String second_current_time = current_time.substring(10, 12);

        String given_time = String.valueOf(Long.parseLong(time));
        String year_given_time = given_time.substring(0, 2);
        String month_given_time = given_time.substring(2, 4);
        String day_given_time = given_time.substring(4, 6);

        String hour_given_time = given_time.substring(6, 8);
        String minute_given_time = given_time.substring(8, 10);
        String second_given_time = given_time.substring(10, 12);

        String diff_year = String.valueOf(Long.parseLong(year_current_time) - Long.parseLong(year_given_time));
        String diff_month = String.valueOf(Long.parseLong(month_current_time) - Long.parseLong(month_given_time));
        String diff_day = String.valueOf(Long.parseLong(day_current_time) - Long.parseLong(day_given_time));
        String diff_hour = String.valueOf(Long.parseLong(hour_current_time) - Long.parseLong(hour_given_time));
        String diff_minute = String.valueOf(Long.parseLong(minute_current_time) - Long.parseLong(minute_given_time));
        String diff_sec = String.valueOf(Long.parseLong(second_current_time) - Long.parseLong(second_given_time));

        String year_left = "0";
        String month_left = "0";
        String day_left = "0";
        String hour_left = "0";
        String minute_left = "0";
        String sec_left = "0";

        String diff = "0";

        //String formatted = String.format("%12d", String.valueOf(difference));
        if (Long.parseLong(diff_year) == 0) {
            if (Long.parseLong(diff_month) == 0) {
                if (Long.parseLong(diff_day) == 0) {
                    if (Long.parseLong(diff_hour) == 0) {
                        if (Long.parseLong(diff_minute) == 0) {
                            diff = "now";
                        } else {
                            diff = diff_minute + " minute";
                        }
                    } else {
                        diff = diff_hour + " hour";
                    }
                } else {
                    diff = diff_day + " day";
                }
            } else {
                diff = diff_month + " month";
            }
        } else {
            diff = diff_year + " year";
        }
        return diff;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeChat(String time) {

        if (time == null || time.length() < 12) {
            return "Not Available";
        }

        String current_time = String.valueOf(Long.parseLong(getCurrentTime()));
        String year_current_time = current_time.substring(0, 2);
        String month_current_time = current_time.substring(2, 4);
        String day_current_time = current_time.substring(4, 6);

        String hour_current_time = current_time.substring(6, 8);
        String minute_current_time = current_time.substring(8, 10);
        String second_current_time = current_time.substring(10, 12);

        String given_time = String.valueOf(Long.parseLong(time));
        String year_given_time = given_time.substring(0, 2);
        String month_given_time = given_time.substring(2, 4);
        String day_given_time = given_time.substring(4, 6);

        String hour_given_time = given_time.substring(6, 8);
        String minute_given_time = given_time.substring(8, 10);
        String second_given_time = given_time.substring(10, 12);

        String diff_year = String.valueOf(Long.parseLong(year_current_time) - Long.parseLong(year_given_time)).replace("-", "");
        String diff_month = String.valueOf(Long.parseLong(month_current_time) - Long.parseLong(month_given_time)).replace("-", "");
        String diff_day = String.valueOf(Long.parseLong(day_current_time) - Long.parseLong(day_given_time)).replace("-", "");
        String diff_hour = String.valueOf(Long.parseLong(hour_current_time) - Long.parseLong(hour_given_time)).replace("-", "");
        String diff_minute = String.valueOf(Long.parseLong(minute_current_time) - Long.parseLong(minute_given_time)).replace("-", "");
        String diff_sec = String.valueOf(Long.parseLong(second_current_time) - Long.parseLong(second_given_time)).replace("-", "");

        String year_left = "0";
        String month_left = "0";
        String day_left = "0";
        String hour_left = "0";
        String minute_left = "0";
        String sec_left = "0";

        String diff = "0";

        if (Long.parseLong(given_time.substring(0, 10)) > Long.parseLong(getCurrentTime().substring(0, 10))) {
            if (Long.parseLong(diff_year) + Long.parseLong(diff_month) + Long.parseLong(diff_day) == 0) {
                // Same day
                return "Today, " + getAmPmTime(given_time.substring(6, 12));
            } else if (Long.parseLong(diff_year) + Long.parseLong(diff_month) == 0) {
                if (Long.parseLong(diff_day) < 7) {
                    if (Long.parseLong(diff_day) == 1) {

                        return "Tomorrow, " + getAmPmTime(given_time.substring(6, 12));
                    } else {
                        return ">" + getDayName(given_time) + ", " + getAmPmTime(given_time.substring(6, 12));
                    }

                } else {
                    // show year date
                    return ">" + getMonthName(given_time) + " " + day_given_time + ", " + year_given_time;
                }
            } else {
                return ">" + getMonthName(given_time) + " " + day_given_time + ", " + year_given_time;
            }
        } else if (Long.parseLong(given_time.substring(0, 10)) < Long.parseLong(getCurrentTime().substring(0, 10))) {
            if (Long.parseLong(diff_year) + Long.parseLong(diff_month) + Long.parseLong(diff_day) == 0) {
                // Same day
                return "Today" + ", " + getAmPmTime(given_time.substring(6, 12));
            } else if (Long.parseLong(diff_year) + Long.parseLong(diff_month) == 0) {
                if (Long.parseLong(diff_day) < 7) {
                    if (Long.parseLong(diff_day) == 1) {
                        return "Yesterday" + ", " + getAmPmTime(given_time.substring(6, 12));
                    } else {
                        return "<" + getDayName(given_time) + ", " + getAmPmTime(given_time.substring(6, 12));
                    }
                } else {
                    // show year date
                    return "<" + getMonthName(given_time) + " " + day_given_time + ", " + year_given_time;
                }
            } else {
                return "<" + getMonthName(given_time) + " " + day_given_time + ", " + year_given_time;
            }
        } else {
            return "Now";
        }
    }

    public String addLeadingCharacters(String character, int how_many, long number) {
        return String.format((Locale) null, "%" + character + String.valueOf(how_many) + "d", number);
    }

    public String addLeadingCharacters(String character, int how_many, String number) {
        return String.format((Locale) null, "%" + character + String.valueOf(how_many) + "d", number);
    }

    public String getAgoTimeAbondened(long difference) {
        String diff = addLeadingCharacters("0", 12, difference);
        if (Long.parseLong(diff) < 100) {
            return "now";
        } else {
            String year = diff.substring(0, 2);
            String month = diff.substring(2, 4);
            String day = diff.substring(4, 6);
            String hour = diff.substring(6, 8);
            String minute = diff.substring(8, 10);
            String second = diff.substring(10, 12);
        }
        return "0";
    }

    public double getDouble(String s) {
        Scanner st = new Scanner(s);
        while (!st.hasNextDouble()) {
            st.next();
        }
        return st.nextDouble();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void disableButton(Context context, View view, int color) {
        view.setEnabled(false);
        view.setBackgroundTintList(context.getColorStateList(color));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void enableButton(Context context, View view, int color) {
        view.setEnabled(true);
        view.setBackgroundTintList(context.getColorStateList(color));
    }

    public void flipNext(ViewFlipper vf, Context context) {
        Animation inLeft = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_left_fast_in);
        Animation outLeft = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_left_fast_out);
        vf.setInAnimation(inLeft);
        vf.setOutAnimation(outLeft);
        vf.showNext();
    }

    public void flipPrevious(ViewFlipper vf, Context context) {
        Animation inRight = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_right_fast_in);
        Animation outRight = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_right_fast_out);
        vf.setInAnimation(inRight);
        vf.setOutAnimation(outRight);
        vf.showPrevious();
    }

    public void flipNextTo(Context context, ViewFlipper vf, int child) {
        Animation inLeft = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_left_fast_in);
        Animation outLeft = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_left_fast_out);
        vf.setInAnimation(inLeft);
        vf.setOutAnimation(outLeft);
        vf.setDisplayedChild(child - 1);
    }

    public void flipPreviousTo(Context context, ViewFlipper vf, int child) {
        Animation inRight = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_right_fast_in);
        Animation outRight = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.slide_right_fast_out);
        vf.setInAnimation(inRight);
        vf.setOutAnimation(outRight);
        vf.setDisplayedChild(child - 1);
    }

    public void callKeyboard(Activity a, View v, boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) a.getSystemService(INPUT_METHOD_SERVICE);
        if (show) {
            inputMethodManager.showSoftInput(v, 0);
        } else {
            inputMethodManager.hideSoftInputFromWindow(a.getCurrentFocus().getApplicationWindowToken(), 0);
        }
    }

    public void backButtonPanel(Activity activity, String header) {
        ImageView iv_back = activity.findViewById(R.id.iv_back);
        TextView tv_back = activity.findViewById(R.id.tv_back);
        tv_back.setText(header);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public void setStatusBarColor(Activity activity, int color, String text_color) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (text_color.equals("light")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.getInsetsController().setSystemBarsAppearance(0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.getInsetsController().setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));

    }

    public void getContactListFromPhonebook(Activity activity, UniMethods uniMethods) {
        //todo: save this in your activity
        /*@Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        ArrayList<HashMap<String, Object>> contact_list = new ArrayList<>();
        Cursor cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
            HashMap<String, Object> contact_map = new HashMap<>();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact_map.put("name", name);
            contact_map.put("number", number);
            contact_list.add(contact_map);
        }
        uniMethods.notifier.onTaskCompleted(String.valueOf(contact_list.size()), true, contact_list);
    }

    public String checkReadContactsPermission(Activity activity) {
        //todo: save this in your activity
        /*@Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                return "declined";
            } else {
                return "declined_forever";
            }
        } else {
            return "granted";
        }
    }

    public String checkCallLogPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CALL_LOG)) {
                return "declined";
            } else {
                return "declined_forever";
            }
        } else {
            return "granted";
        }
    }

    public String checkWriteExternalStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return "declined";
            } else {
                return "declined_forever";
            }
        } else {
            return "granted";
        }
    }

    public String checkNotificationPermission(Activity activity) {
        //todo: save this in your activity
        /*@Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                return "declined";
            } else {
                return "declined_forever";
            }
        } else {
            return "granted";
        }
    }

    public void requestPermission(int request_code, Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(new String[]{permission}, request_code);
        }
    }

    public void toggle(int view_id, ViewGroup parent) {
        View v = parent.findViewById(view_id);
        Transition transition = new Fade();
        transition.setDuration(600);
        transition.addTarget(view_id);

        TransitionManager.beginDelayedTransition(parent, transition);
        //v.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public String convertDateToDigit(String date) {
        String day = date.substring(0, date.indexOf("/"));
        String month = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
        String year = date.substring(date.lastIndexOf("/") + 3);
        if (month.length() == 1) {
            month = "0" + month;
        }
        return year + month + day;
    }

    public void circularReveal(View myView, boolean reveal, UniMethods uniMethods) {
        if (reveal) {
            // Check if the runtime version is at least Lollipop
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    uniMethods.notifier.onTaskCompleted("complete", true, "none");
                }
            });
        } else {
            // Check if the runtime version is at least Lollipop
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            // get the initial radius for the clipping circle
            float initialRadius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0f);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.GONE);
                }
            });

            // start the animation
            anim.start();
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    uniMethods.notifier.onTaskCompleted("complete", true, "none");
                }
            });
        }
    }

    public int getPxValueOfDp(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public void hideKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboardFromFragment(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
    }

    public void hideKeyboardFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void startTimer(CountDownTimer countDownTimer, int duration, TextView timer, View attach_button, String text_before_timer
            , String format, UniMethods uniMethods) {
        if (attach_button != null) {
            attach_button.setEnabled(false);
        }
        if (timer.getAlpha() < 1) {
            fadingView(timer, 0, 1, 300, null);
        }
        if (timer.getVisibility() != View.VISIBLE) {
            fadingView(timer, 0, 1, 300, null);
            timer.setVisibility(View.VISIBLE);
        }
        int convertedTime;
        switch (format) {
            case "sec":
                convertedTime = duration * 1000;
                break;
            default:
                convertedTime = duration * 60000;
                break;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(convertedTime, 1000) {

            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                switch (format) {
                    case "hour":
                        timer.setText(text_before_timer + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                        break;
                    case "min":
                        timer.setText(text_before_timer + f.format(min) + ":" + f.format(sec));
                        break;
                    case "sec":
                        timer.setText(text_before_timer + "0" + ":" + f.format(sec));
                        break;
                    default:
                        break;
                }
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                uniMethods.notifier.onTaskCompleted("none", true, "none");
                if (attach_button != null) {
                    attach_button.setEnabled(true);
                }
            }
        };
        countDownTimer.start();
        uniMethods.notifier.onTaskCompleted("none", false, countDownTimer);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getMonthName(String date) {
        String actual_date = date.substring(0, 6);
        String y = "20" + actual_date.substring(0, 2);
        String m = actual_date.substring(2, 4);
        String d = actual_date.substring(4, 6);

        String input = d + "-" + m + "-" + y;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-M-u", Locale.ENGLISH);
        LocalDate det = LocalDate.parse(input, dtf);
        Month mon = det.getMonth();
        return mon.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDayName(String date) {
        String actual_date = date.substring(0, 6);
        String y = "20" + actual_date.substring(0, 2);
        String m = actual_date.substring(2, 4);
        String d = actual_date.substring(4, 6);

        String input = d + "-" + m + "-" + y;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-M-u", Locale.ENGLISH);
        LocalDate det = LocalDate.parse(input, dtf);
        DayOfWeek dow = det.getDayOfWeek();
        return dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    public String getAmPmTime(String time) {
        if (time.length() == 6) {
            if (Double.parseDouble(time.substring(0, 2)) > 24) {
                return "Not Available";
            } else {
                String h = time.substring(0, 2);
                String am_pm = "am";
                if (Integer.parseInt(h) > 12) {
                    h = String.valueOf(Integer.parseInt(h) - 12);
                    am_pm = "pm";
                } else if (Integer.parseInt(h) == 12) {
                    am_pm = "pm";
                    h = "12";
                } else if (Integer.parseInt(h) == 00) {
                    am_pm = "am";
                    h = "12";
                }
                String m = time.substring(2, 4);
                return h + ":" + m + " " + am_pm;
            }
        } else if (time.length() == 12) {
            String t = time.substring(6);
            String h = t.substring(0, 2);
            String am_pm = "am";
            if (Integer.parseInt(h) > 12) {
                h = String.valueOf(Integer.parseInt(h) - 12);
                am_pm = "pm";
            } else if (Integer.parseInt(h) == 12) {
                am_pm = "pm";
            }
            String m = t.substring(2, 4);
            return h + ":" + m + " " + am_pm;
        } else {
            return "Not Available";
        }
    }

    public void createSpinner(Spinner spinner, String[] list, Context context) {
        ArrayAdapter ad
                = new ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                list);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spinner.setAdapter(ad);
    }

    public void createSpinnerCustom(Spinner spinner, String[] list, Context context) {
        ArrayAdapter ad
                = new ArrayAdapter(
                context,
                R.layout.spinner_item,
                list);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                R.layout
                        .spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner.setAdapter(ad);
    }

    public void setSp(Context context, String collection, String key, String value) {
        SharedPreferences Sp = context.getSharedPreferences(collection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getSp(Context context, String sp, String key) {
        SharedPreferences Sp = context.getSharedPreferences(sp, Context.MODE_PRIVATE);
        return Sp.getString(key, "0");
    }

    public String getLastClickedButton(Context context) {
        SharedPreferences Sp = context.getSharedPreferences("ACTIONS", Context.MODE_PRIVATE);
        return Sp.getString("last_clicked", "0");
    }

    public void setLastClickedButton(Context context, String value) {
        SharedPreferences Sp = context.getSharedPreferences("ACTIONS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Sp.edit();
        editor.putString("last_clicked", value);
        editor.apply();
    }

    public void logout(Activity activity) {
        deleteSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_name));
        deleteSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_number));
        deleteSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_country_code));
        deleteSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_user_id));
        setSp(activity, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_login_status), activity.getString(R.string.logged_out));
    }

    public String checkPermissionPopup(Context context, String key) {
        SharedPreferences Sp = context.getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
        return Sp.getString(key, "0");
    }

    public void increasePermissionPopup(Context context, String key) {
        SharedPreferences Sp = context.getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
        int i = Integer.parseInt(checkPermissionPopup(context, key));
        i++;
        SharedPreferences.Editor editor = Sp.edit();
        editor.putString(key, String.valueOf(i));
        editor.apply();
    }

    public void resetPermissionPopup(Context context, String key) {
        SharedPreferences Sp = context.getSharedPreferences("PERMISSIONS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Sp.edit();
        editor.putString(key, "0");
        editor.apply();
    }

    public String getMyId(Context context) {
        SharedPreferences Sp = context.getSharedPreferences("USER_PROFILE", Context.MODE_PRIVATE);
        return Sp.getString("user_id", "0");
    }

    public String getMySavedSuffix(Context context) {
        SharedPreferences Sp = context.getSharedPreferences("USER_PROFILE", Context.MODE_PRIVATE);
        return Sp.getString("my_saved_suffix", "none");
    }

    public String getAssociateName(Context context) {
        SharedPreferences Sp = context.getSharedPreferences("USER_PROFILE", Context.MODE_PRIVATE);
        return Sp.getString("name", "0");
    }

    public String getAssociateNumber(Context context) {
        SharedPreferences Sp = context.getSharedPreferences("USER_PROFILE", Context.MODE_PRIVATE);
        return Sp.getString("number", "0");
    }

    public void deleteSp(Context context, String collection, String key) {
        SharedPreferences Sp = context.getSharedPreferences(collection, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public String getRowForUpdate(Context context, String key) {
        SharedPreferences Sp = context.getSharedPreferences("row_update", Context.MODE_PRIVATE);
        return Sp.getString(key, "none");
    }

    public void setRowForUpdate(Context context, String key, int i, boolean b) {
        if (b) {
            setSp(context, "row_update", key, String.valueOf(i));
        } else {
            deleteSp(context, "row_update", key);
        }
    }

    public ArrayList<HashMap<String, Object>> sortList(ArrayList<HashMap<String, Object>> list,
                                                       String field, String flow) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(new Comparator<HashMap<String, Object>>() {
                @Override
                public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                    return String.valueOf(o1.get(field)).compareTo(
                            String.valueOf(o2.get(field)));
                }
            });
            if (flow.equals("ascending")) {
                return reverseListHashmap(list);
            } else if (flow.equals("descending")) {
                return list;
            } else {
                return list;
            }
        }
        return list;
    }

    public List<String> reverseListString(List<String> a) {
        Collections.reverse(a);
        return a;
    }

    public ArrayList<HashMap<String, Object>> reverseListHashmap(List<HashMap<String, Object>> a) {
        Collections.reverse(a);
        return (ArrayList<HashMap<String, Object>>) a;
    }

    public void getNumber(ContentResolver cr) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        // use the cursor to access the contacts
        while (phones.moveToNext()) {
            // String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            // get display name
            //  String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // get phone number
        }

    }

    public void textWithFadeIn(String text, TextView tv) {
        fadingView(tv, 0, 1, 500, new UniMethods(new Notifier() {
            @Override
            public void onTaskCompleted(String s, boolean b, Object o) {

            }
        }));
        tv.setText(text);
    }

    public void getNumber2(ContentResolver cr) {
        Cursor cursor = null;
        try {
            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            int contactIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneNumberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int photoIdIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
            cursor.moveToFirst();
            do {
                String idContact = cursor.getString(contactIdIdx);
                String name = cursor.getString(nameIdx);
                String phoneNumber = cursor.getString(phoneNumberIdx);
                //...
            } while (cursor.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<HashMap<String, Object>> getCallDetails(Context context) {
        ArrayList<HashMap<String, Object>> call_logs_list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phName = cursor.getString(name);
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            /*phName = "HellO worLd";
            StringBuilder name_with_space = new StringBuilder();
            int sL = phName.length();
            int i = 0;
            for (; i < sL; i++) {
                if (isUpperCase(phName.charAt(i))) {
                    //name_with_space.append(phName.replaceFirst(String.valueOf(phName.charAt(i)), " " + String.valueOf(phName.charAt(i))));
                } else {
                    name_with_space.append(String.valueOf(phName.charAt(i)));
                }
            }*/
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", phName);
            hashMap.put("number", phNumber);
            hashMap.put("call_type", dir);
            hashMap.put("call_date", callDayTime);
            hashMap.put("time", callDayTime);
            hashMap.put("duration", callDuration);
            hashMap.put("view_type", "call_log");
            call_logs_list.add(hashMap);
        }
        cursor.close();
        return call_logs_list;
    }

    public ArrayList<HashMap<String, Object>> getFilteredCallDetails(Context context, ArrayList<String> filter_list) {
        ArrayList<HashMap<String, Object>> call_logs_list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phName = cursor.getString(name);
            String phNumber = cursor.getString(number);
            if (filter_list.contains(phNumber.replaceAll(" ", ""))) {
                String callType = cursor.getString(type);
                String callDate = cursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = cursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }

            /*phName = "HellO worLd";
            StringBuilder name_with_space = new StringBuilder();
            int sL = phName.length();
            int i = 0;
            for (; i < sL; i++) {
                if (isUpperCase(phName.charAt(i))) {
                    //name_with_space.append(phName.replaceFirst(String.valueOf(phName.charAt(i)), " " + String.valueOf(phName.charAt(i))));
                } else {
                    name_with_space.append(String.valueOf(phName.charAt(i)));
                }
            }*/
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", phName);
                hashMap.put("number", phNumber);
                hashMap.put("call_type", dir);
                hashMap.put("call_date", callDayTime);
                hashMap.put("time", callDayTime);
                hashMap.put("duration", callDuration);
                hashMap.put("view_type", "call_log");
                call_logs_list.add(hashMap);
            }
        }
        cursor.close();
        return call_logs_list;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void setForOtp(Context context, View v) {
        /*EditText e1 = v.findViewById(R.id.et_otp_1);
        EditText e2 = v.findViewById(R.id.et_otp_2);
        EditText e3 = v.findViewById(R.id.et_otp_3);
        EditText e4 = v.findViewById(R.id.et_otp_4);
        EditText e5 = v.findViewById(R.id.et_otp_5);
        EditText e6 = v.findViewById(R.id.et_otp_6);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e1.getText().length() > 0) {
                    e2.requestFocus();
                }
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e2.getText().length() > 0) {
                    e3.requestFocus();
                } else {
                    e1.requestFocus();
                }
            }
        });

        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e3.getText().length() > 0) {
                    e4.requestFocus();
                } else {
                    e2.requestFocus();
                }
            }
        });

        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e4.getText().length() > 0) {
                    e5.requestFocus();
                } else {
                    e3.requestFocus();
                }
            }
        });

        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e5.getText().length() > 0) {
                    e6.requestFocus();
                } else {
                    e4.requestFocus();
                }
            }
        });

        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (e6.getText().length() > 0) {

                } else {
                    e5.requestFocus();
                }
            }
        });*/

    }

    public void moveView(View view, String translation, float f, int duration) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, translation, f);
        animation.setDuration(duration);
        animation.start();
    }

    public void moveView(View view, String translation, float f, int duration, UniMethods uniMethods) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, translation, f);
        animation.setDuration(duration);
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                uniMethods.notifier.onTaskCompleted("end", true, animation);
            }
        });
    }

    public HashMap<String, Object> getAssociateProfileMap(Activity activity,
                                                          String name,
                                                          String country_code,
                                                          String number,
                                                          String recovery_email,
                                                          String pin,
                                                          String status,
                                                          String address,
                                                          String company,
                                                          String designation,
                                                          String security_level) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (name != null) {
            hashMap.put(activity.getString(R.string.name), name);
        }
        if (number != null) {
            hashMap.put(activity.getString(R.string.number), number);
        }
        if (number != null) {
            hashMap.put(activity.getString(R.string.country_code), country_code);
        }
        if (recovery_email != null) {
            hashMap.put(activity.getString(R.string.recovery_email), recovery_email);
        }
        if (pin != null) {
            hashMap.put(activity.getString(R.string.pin), activity.getString(R.string.default_pin));
        }
        if (status != null) {
            hashMap.put(activity.getString(R.string.status), status);
        }
        if (address != null) {
            hashMap.put(activity.getString(R.string.address), address);
        }
        if (company != null) {
            hashMap.put(activity.getString(R.string.company), company);
        }
        if (designation != null) {
            hashMap.put(activity.getString(R.string.designation), designation);
        }
        if (security_level != null) {
            hashMap.put(activity.getString(R.string.security_level), security_level);
        }
        return hashMap;
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

    public void animationForDelay(View view, int delay, UniMethods uniMethods) {
        //Animation object = AnimationUtils.loadAnimation(context, R.anim.fade_in_long);
        ObjectAnimator object = ObjectAnimator.ofFloat(view, "translationY", 0f);
        object.setDuration(delay);
        object.start();
        object.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                uniMethods.notifier.onTaskCompleted("end", true, "");
            }
        });
    }

    public String[] getArrayUnionFromList(ArrayList<String> list) {
        String[] new_array = new String[list.size()];
        if (list.size() > 0) {
            int i = 0;
            for (String ss : list) {
                new_array[i] = ss;
                i++;
            }
        }
        return new_array;
    }

    public String getIndianFormattedCurrencyAmount(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,##,###");
        return formatter.format(amount);
    }

    public CountDownTimer addDelay(long millis, boolean loop, UniMethods uniMethods) {
        return new CountDownTimer(millis, millis) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (loop) {
                    addDelay(millis, true, uniMethods);
                }
                uniMethods.notifier.onTaskCompleted("", true, this);
            }
        }.start();
    }

    public String[] getPriceUnits(Context context, String listing_purpose, String property_type) {
        if (listing_purpose.equals(context.getString(R.string.for_sale))) {
            switch (property_type) {
                case "Plot":
                case "Commercial Plot":
                    return context.getString(R.string.price_unit_array_for_plot).split("#");
                case "Flat":
                case "Farm House":
                case "Shop":
                case "Office":
                    return context.getString(R.string.price_unit_array_for_flat_sale).split("#");
                case "House":
                case "Villa":
                case "Row House":
                    return context.getString(R.string.price_unit_array_for_house_sale).split("#");
                case "Land":
                    return context.getString(R.string.price_unit_array_for_Land).split("#");
                default:
                    return context.getString(R.string.price_unit_array_for_house_sale).split("#");
            }
        } else if (listing_purpose.equals(context.getString(R.string.for_rent))) {
            switch (property_type) {
                case "Plot":
                    break;
                case "Flat":
                    break;
                case "Commercial Plot":
                    break;
                case "House":
                    break;
                case "Villa":
                    break;
                case "Row House":
                    break;
                case "Farm House":
                    break;
                case "Shop":
                    break;
                case "Office":
                    break;
                case "Land":
                    break;
                default:
                    break;
            }
            return null;
        } else {
            return null;
        }
    }

    public Object addViewIntoLinearLayoutCompat(Activity activity, int parent_ll_compat_id, int child_layout, int width, int height, int margin_left, int margin_top, int margin_right, int margin_bottom) {
        inflater = (LayoutInflater) Objects.requireNonNull(activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(width, height);
        ViewGroup view = (LinearLayoutCompat) inflater.inflate(child_layout, null);
        params.setMargins(margin_left, margin_top, margin_right, margin_bottom);
        view.setLayoutParams(params);
        LinearLayoutCompat ll_compat = activity.findViewById(parent_ll_compat_id);
        ll_compat.addView(view);
        return view;
    }

}
