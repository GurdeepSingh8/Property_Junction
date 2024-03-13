package property.junction.co;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import property.junction.co.methods.InFirebase;
import property.junction.co.methods.MyMethods;
import property.junction.co.methods.UniMethods;

public class Login extends AppCompatActivity {

    Context context = Login.this;
    Activity activity = Login.this;

    InFirebase inFirebase;
    UniMethods uniMethods = new UniMethods();
    MyMethods myMethods = new MyMethods();

    ConstraintLayout cons_login, cons_otp;
    TextInputLayout til_name, til_number, til_country_code, til_otp;
    TextView tv_exception, tv_forget_pass, tv_asso_login_subtext, tv_helper_otp, tv_change_number, tv_resend_otp;
    Button btn_login, btn_verify_otp;
    ImageView iv_open_country_codes;
    ProgressBar prog_login, prog_otp, prog_resend_otp, prog_forgetPass;
    String country_code = "+91";
    String number = "0";

    CountDownTimer countDownTimer = null;
    AlertDialog alert_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inFirebase = new InFirebase(activity);
        cons_login = findViewById(R.id.cons_login);
        cons_otp = findViewById(R.id.cons_otp);
        btn_login = findViewById(R.id.btn_login);
        btn_verify_otp = findViewById(R.id.btn_verify_otp);
        til_otp = findViewById(R.id.til_otp);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);
        iv_open_country_codes = findViewById(R.id.iv_open_country_codes);
        til_name = findViewById(R.id.til_name);
        til_country_code = findViewById(R.id.til_country_code);
        til_number = findViewById(R.id.til_number);
        prog_login = findViewById(R.id.prog_login);
        prog_otp = findViewById(R.id.prog_otp);
        tv_resend_otp = findViewById(R.id.tv_resend_otp);
        prog_resend_otp = findViewById(R.id.prog_resend_otp);
        prog_forgetPass = findViewById(R.id.prog_forgetPass);
        tv_asso_login_subtext = findViewById(R.id.tv_asso_login_subtext);
        tv_helper_otp = findViewById(R.id.tv_helper_otp);
        tv_change_number = findViewById(R.id.tv_change_number);
        tv_exception = findViewById(R.id.tv_exception);

        alert_dialog = myMethods.displayProgress(context);
        if (activity != null && !activity.isFinishing()) {
            alert_dialog.show(); // if fragment use getActivity().isFinishing() or isAdded() method
        }

        inFirebase.isDatabaseCreated(new InFirebase(activity, new InFirebase.ResultFromFirebase() {
            @Override
            public void onResultFromFirebase(String callback, boolean condition, Object value) {
                if (condition) {
                    inFirebase.isUpdateAvailable(new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                        @Override
                        public void onResultFromFirebase(String callback, boolean condition, Object value) {
                            if (condition) {
                                alert_dialog.dismiss();
                                myMethods.showUpdatePopup(Login.this, getSupportFragmentManager(), value);
                            } else {
                                if (uniMethods.getSp(Login.this, activity.getString(R.string.user_profile_in_sp), activity.getString(R.string.sp_login_status)).equals(activity.getString(R.string.logged_in))) {
                                    inFirebase.getSecurityLevel(new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                                        @Override
                                        public void onResultFromFirebase(String callback, boolean condition, Object value) {
                                            if (callback.equals("0")) {
                                                Intent intent = new Intent(Login.this, Dashboard.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (callback.equals("1")) {
                                                alert_dialog.dismiss();
                                            } else {
                                                alert_dialog.dismiss();
                                            }
                                        }
                                    }));
                                } else {
                                    login_procedure();
                                    alert_dialog.dismiss();
                                }
                            }
                        }
                    }));
                } else {
                    alert_dialog.dismiss();
                    myMethods.showImageTextBottomSheet(activity,
                            R.drawable.img_cloud_problem,
                            0,
                            0,
                            0,
                            0,
                            uniMethods.getPxValueOfDp(activity, 120),
                            activity.getString(R.string.database_not_found_header),
                            activity.getString(R.string.database_not_found_sub_header),
                            activity.getString(R.string.database_not_found_button_text),
                            getSupportFragmentManager(),
                            false,
                            new UniMethods(new UniMethods.Notifier() {
                                @Override
                                public void onTaskCompleted(String s, boolean b, Object o) {
                                    finishAffinity();
                                }
                            }));
                }
            }
        }));

    }

    private void login_procedure() {
        til_name.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        til_country_code.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                country_code = charSequence.toString().substring(charSequence.toString().indexOf("+"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        til_number.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                number = charSequence.toString();
                if (charSequence.toString().contains("+91")) {
                    TextWatcher tw = this;
                    til_number.getEditText().removeTextChangedListener(tw);
                    til_number.getEditText().setText(charSequence.toString().replace("+91", ""));
                    til_number.getEditText().addTextChangedListener(tw);
                }
                til_number.setError(null);
                til_number.setHelperText(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        til_country_code.getEditText().setText(context.getString(R.string.default_country_code));

        til_otp.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_otp.setBoxStrokeColor(getResources().getColor(R.color.app_theme_color));
                til_otp.setHintTextColor(AppCompatResources.getColorStateList(activity, R.color.app_theme_color));
                uniMethods.changeTextColor(context, tv_helper_otp, R.color.app_theme_color);
                tv_helper_otp.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        iv_open_country_codes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, CountryCodes.class);
                startActivityForResult(intent, 3);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_exception.setVisibility(View.INVISIBLE);
                til_name.setError(null);
                til_number.setError(null);
                uniMethods.onPress(btn_login, prog_login);
                uniMethods.onPress(tv_resend_otp, prog_resend_otp);
                String name = til_name.getEditText().getText().toString();
                number = til_number.getEditText().getText().toString();
                if (til_name.getEditText().getText() == null || name.equals("")) {
                    til_name.setError("Please enter your name");
                    uniMethods.onRelease(btn_login, prog_login);
                    uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                    return;
                }
                if (til_number.getEditText().getText() == null || number.equals("")) {
                    til_number.setError("Please enter your Mobile Number");
                    uniMethods.onRelease(btn_login, prog_login);
                    uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                    return;
                }
                if (number.length() < 10) {
                    til_number.setError("Please enter a valid Mobile Number");
                    uniMethods.onRelease(btn_login, prog_login);
                    uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                    return;
                }
                sendCode(country_code, number);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (cons_otp.getVisibility() == View.VISIBLE) {
            tv_change_number.performClick();
        }
    }

    private void sendCode(String country_code, String number) {
        uniMethods.onPress(btn_login, prog_login);
        uniMethods.onPress(tv_resend_otp, prog_resend_otp);
        inFirebase.sendOtp(Login.this, country_code, number,
                new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                    @Override
                    public void onResultFromFirebase(String verificationId, boolean condition, Object value) {
                        if (condition) {
                            btn_verify_otp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    uniMethods.onPress(btn_verify_otp, prog_otp);
                                    String code = Objects.requireNonNull(til_otp.getEditText()).getText().toString();
                                    if (code.length() < 1) {
                                        tv_helper_otp.setText(context.getString(R.string.enter_otp_helper_text_1));
                                        uniMethods.onRelease(btn_verify_otp, prog_otp);
                                        return;
                                    }
                                    if (code.length() < 6) {
                                        tv_helper_otp.setText(context.getString(R.string.enter_otp_helper_text_2));
                                        uniMethods.onRelease(btn_verify_otp, prog_otp);
                                        return;
                                    }
                                    inFirebase.otpVerification(verificationId, code,
                                            new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                                                @Override
                                                public void onResultFromFirebase(String callback, boolean condition, Object value) {
                                                    if (condition) {
                                                        if (cons_otp.getVisibility() == View.VISIBLE) {
                                                            inFirebase.registerAssociate(til_name.getEditText().getText().toString(),
                                                                    til_country_code.getEditText().getText().toString(),
                                                                    til_number.getEditText().getText().toString(), callback,
                                                                    new InFirebase(activity, new InFirebase.ResultFromFirebase() {
                                                                        @Override
                                                                        public void onResultFromFirebase(String callback, boolean condition, Object value) {
                                                                            if (condition) {
                                                                                if (callback.equals(activity.getString(R.string.cb_exists))) {
                                                                                    if (cons_otp.getVisibility() == View.VISIBLE) {
                                                                                        Intent intent = new Intent(activity, Dashboard.class);
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                } else if (callback.equals(activity.getString(R.string.cb_not_exists))) {
                                                                                    if (cons_otp.getVisibility() == View.VISIBLE) {
                                                                                        myMethods.showLottieTextBottomSheet(activity,
                                                                                                R.raw.lottie_success_plain,
                                                                                                0f, 1f,
                                                                                                uniMethods.getPxValueOfDp(context, 120),
                                                                                                1.4f,
                                                                                                activity.getString(R.string.login_complete_header),
                                                                                                activity.getString(R.string.login_complete_sub_header),
                                                                                                activity.getString(R.string.login_complete_button_text),
                                                                                                getSupportFragmentManager(),
                                                                                                true,
                                                                                                new UniMethods(new UniMethods.Notifier() {
                                                                                                    @Override
                                                                                                    public void onTaskCompleted(String s, boolean b, Object o) {
                                                                                                        Intent intent = new Intent(activity, Dashboard.class);
                                                                                                        startActivity(intent);
                                                                                                        uniMethods.onRelease(btn_verify_otp, prog_otp);
                                                                                                        finish();
                                                                                                    }
                                                                                                }));
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                uniMethods.onRelease(btn_verify_otp, prog_otp);
                                                                            }
                                                                        }
                                                                    }));
                                                        } else {
                                                            uniMethods.onRelease(btn_verify_otp, prog_otp);
                                                        }
                                                    } else {
                                                        til_otp.setBoxStrokeColor(getResources().getColor(R.color.red_g));
                                                        til_otp.setHintTextColor(AppCompatResources.getColorStateList(activity, R.color.red_g));
                                                        uniMethods.changeTextColor(context, tv_helper_otp, R.color.red_g);
                                                        tv_helper_otp.setText(activity.getString(R.string.enter_otp_helper_text_3));
                                                        uniMethods.shakeView(tv_helper_otp);
                                                        uniMethods.onRelease(btn_verify_otp, prog_otp);
                                                    }
                                                }
                                            }));
                                }
                            });
                            String otp_sub_text =
                                    "which was sent to your mobile number " + "<b>" + country_code + " " + number + "</b> ";
                            tv_asso_login_subtext.setText(Html.fromHtml(otp_sub_text));
                            createTimer();
                            if (cons_login.getVisibility() == View.VISIBLE) {
                                cons_login.setVisibility(View.GONE);
                                cons_otp.setVisibility(View.VISIBLE);
                                uniMethods.callKeyboard(Login.this, til_otp, true);
                                uniMethods.onRelease(btn_login, prog_login);
                                uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                            } else {
                                Toast.makeText(Login.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                uniMethods.onRelease(btn_login, prog_login);
                                uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                            }
                            tv_change_number.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (cons_otp.getVisibility() == View.VISIBLE) {
                                        cons_otp.setVisibility(View.GONE);
                                        cons_login.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else {
                            tv_exception.setText(verificationId);
                            uniMethods.onRelease(btn_login, prog_login);
                            uniMethods.onRelease(tv_resend_otp, prog_resend_otp);
                        }
                    }
                }));
    }

    private void createTimer() {
        tv_resend_otp.setEnabled(false);
        tv_resend_otp.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.background_0));
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

                String str = "Resend OTP in 0:" + String.valueOf(millisUntilFinished / 1000) + " sec";
                tv_resend_otp.setText(str);

            }

            @Override
            public void onFinish() {
                tv_resend_otp.setEnabled(true);
                tv_resend_otp.setBackground(AppCompatResources.getDrawable(context, R.drawable.round_4));
                tv_resend_otp.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.background_1));
                uniMethods.changeTextColor(context, tv_resend_otp, R.color.app_theme_color);
                tv_resend_otp.setText("Resend OTP");
                tv_resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uniMethods.onPress(tv_resend_otp, prog_resend_otp);
                        uniMethods.onPress(btn_login, prog_login);
                        sendCode(country_code, number);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            try {
                HashMap<String, Object> hm_country = (HashMap<String, Object>) data.getSerializableExtra("data");
                til_country_code.getEditText().setText(String.valueOf(hm_country.get("flag")) + "  " + String.valueOf(hm_country.get("country_code")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alert_dialog != null && alert_dialog.isShowing()) {
            alert_dialog.dismiss();
        }
    }

}