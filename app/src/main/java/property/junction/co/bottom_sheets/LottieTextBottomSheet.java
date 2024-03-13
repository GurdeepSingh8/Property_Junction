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
import androidx.appcompat.widget.LinearLayoutCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import property.junction.co.R;

public class LottieTextBottomSheet extends BottomSheetDialogFragment {

    TextView tv_head, tv_sub_head;
    Button btn_positive;
    String value_back = "";
    String header, sub_header, btn_text;
    LottieAnimationView lottie;
    int lottie_raw = 0;
    float scale = 0;
    float animation_start = 0f;
    float animation_end = 1f;
    int size = 0;
    FirebaseFirestore db;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public LottieTextBottomSheet(Activity activity, int lottie_raw, float animation_start, float animation_end, int size, float scale, String header, String sub_header, String text_on_button, Notify inNotify) {
        this.activity = activity;
        this.lottie_raw = lottie_raw;
        this.animation_start = animation_start;
        this.animation_end = animation_end;
        this.size = size;
        this.scale = scale;
        this.header = header;
        this.sub_header = sub_header;
        this.btn_text = text_on_button;
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
        View v = inflater.inflate(R.layout.bottom_sheet_lottie_text, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        tv_head = v.findViewById(R.id.tv_head);
        tv_sub_head = v.findViewById(R.id.tv_sub_head);
        lottie = v.findViewById(R.id.lottie);
        btn_positive = v.findViewById(R.id.btn_positive);

        lottie.setAnimation(lottie_raw);

        if (size != 0) {
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(size, size);
            lottie.setLayoutParams(params);
        }
        if (scale != 1) {
            lottie.setScaleX(scale);
            lottie.setScaleY(scale);
        }

        tv_head.setText(header);
        tv_sub_head.setText(sub_header);
        if (btn_text.length() > 0) {
            btn_positive.setText(btn_text);
        }
        value_back = "finish";
        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value_back = "finish";
                dismiss();
            }
        });

        lottie.setMinAndMaxProgress(animation_start, animation_end);
        lottie.playAnimation();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inNotify.on_notify(true, value_back);
    }
}
