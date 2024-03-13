package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import property.junction.co.R;
import property.junction.co.methods.MyMethods;

public class ConfirmationBottomSheet extends BottomSheetDialogFragment {

    TextView tv_head, tv_sub_head;
    LottieAnimationView lott_success;
    Button btn_positive, btn_negative;
    String heading, sub_text, negative_btn_text, positive_btn_text;
    String value_back = "";
    ImageView iv_main;
    int img_main;
    FirebaseFirestore db;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public ConfirmationBottomSheet(Activity activity, int img_main, String heading, String sub_text, String negative_btn_text, String positive_btn_text, Notify inNotify) {
        this.activity = activity;
        this.img_main = img_main;
        this.heading = heading;
        this.sub_text = sub_text;
        this.negative_btn_text = negative_btn_text;
        this.positive_btn_text = positive_btn_text;
        this.inNotify = inNotify;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        /*int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.bottom_sheet_confirmation, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        iv_main = v.findViewById(R.id.iv_main);
        tv_head = v.findViewById(R.id.tv_head);
        tv_sub_head = v.findViewById(R.id.tv_sub_head);
        btn_positive = v.findViewById(R.id.btn_positive);
        btn_negative = v.findViewById(R.id.btn_negative);

        MyMethods myMethods = new MyMethods();

        iv_main.setImageDrawable(AppCompatResources.getDrawable(requireContext(), img_main));

        tv_head.setText(heading);
        tv_sub_head.setText(sub_text);


        btn_negative.setText(negative_btn_text);
        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value_back = "negative";
                dismiss();
            }
        });

        btn_positive.setText(positive_btn_text);
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value_back = "positive";
                dismiss();
            }
        });


        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (value_back.equals("positive")) {
            inNotify.on_notify(true, value_back);
        } else if (value_back.equals("negative")) {
            inNotify.on_notify(false, value_back);
        } else {
            inNotify.on_notify(false, value_back);
        }
    }
}





