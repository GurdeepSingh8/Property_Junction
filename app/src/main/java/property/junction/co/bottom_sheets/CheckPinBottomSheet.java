package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import property.junction.co.R;
import property.junction.co.methods.MyMethods;
import property.junction.co.methods.UniMethods;

public class CheckPinBottomSheet extends BottomSheetDialogFragment {

    TextInputLayout et_otp;
    TextView tv_header;
    String PIN = "";
    ImageView iv_lock;
    String checkFor = "";
    LottieAnimationView lott_lock;
    FirebaseFirestore db;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public CheckPinBottomSheet(Activity activity, String pin, String checkFor, Notify inNotify) {
        this.activity = activity;
        this.PIN = pin;
        this.checkFor = checkFor;
        this.inNotify = inNotify;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        View v = inflater.inflate(R.layout.bottom_sheet_check_pin, container, false);
        v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        et_otp = v.findViewById(R.id.et_otp);
        iv_lock = v.findViewById(R.id.iv_lock);
        lott_lock = v.findViewById(R.id.lott_lock);
        tv_header = v.findViewById(R.id.tv_header);

        UniMethods uniMethods = new UniMethods((UniMethods.Notifier) requireContext());
        uniMethods.setEditTextMaxLength(Objects.requireNonNull(et_otp.getEditText()), PIN.length());

        if (checkFor.equals("pin")) {
            iv_lock.setVisibility(View.INVISIBLE);
            lott_lock.setVisibility(View.VISIBLE);
            lott_lock.setMinAndMaxProgress(0.3f, 1f);
            lott_lock.playAnimation();
            tv_header.setText("Confirm your PIN to unlock your account");
            Objects.requireNonNull(et_otp.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() >= PIN.length()) {
                        if (PIN.equals(et_otp.getEditText().getText().toString())) {
                            inNotify.on_notify(true, PIN);
                        } else {
                            inNotify.on_notify(false, "Wrong PIN");
                            et_otp.setError("Wrong PIN");
                        }
                    } else {
                        et_otp.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (checkFor.equals("otp")) {
            iv_lock.setVisibility(View.VISIBLE);
            lott_lock.setVisibility(View.INVISIBLE);
            tv_header.setText("Please enter " + PIN.length() + " Digit OTP sent to your registered number");
            Objects.requireNonNull(et_otp.getEditText()).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() >= PIN.length()) {
                        if (PIN.equals(et_otp.getEditText().getText().toString())) {
                            inNotify.on_notify(true, PIN);
                        } else {
                            inNotify.on_notify(false, "Wrong OTP");
                            et_otp.setError("Wrong OTP");
                        }
                    } else {
                        et_otp.setError(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //inNotify.on_notify(false, "view_destroyed");
    }
}


