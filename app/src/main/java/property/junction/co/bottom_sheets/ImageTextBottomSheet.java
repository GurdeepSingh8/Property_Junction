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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import property.junction.co.R;
import property.junction.co.methods.UniMethods;

public class ImageTextBottomSheet extends BottomSheetDialogFragment {

    TextView tv_head, tv_sub_head;
    Button btn_positive;
    String value_back = "";
    String header, sub_header, btn_text;
    ImageView iv_image;
    int drawable = 0;
    int drawable_color = 0;
    int background = 0;
    int bg_color = 0;
    int padding = 0;
    int size = 0;
    FirebaseFirestore db;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public ImageTextBottomSheet(Activity activity, int drawable, int drawable_color, int background, int bg_color, int padding, int size, String header, String sub_header, String text_on_button, Notify inNotify) {
        this.activity = activity;
        this.drawable = drawable;
        this.drawable_color = drawable_color;
        this.background = background;
        this.bg_color = bg_color;
        this.padding = padding;
        this.size = size;
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
        View v = inflater.inflate(R.layout.bottom_sheet_image_text, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        tv_head = v.findViewById(R.id.tv_head);
        tv_sub_head = v.findViewById(R.id.tv_sub_head);
        iv_image = v.findViewById(R.id.iv_image);
        btn_positive = v.findViewById(R.id.btn_positive);

        if (drawable != 0) {
            iv_image.setImageDrawable(AppCompatResources.getDrawable(requireContext(), drawable));
        }
        if (drawable_color != 0) {
            iv_image.setImageTintList(AppCompatResources.getColorStateList(requireContext(), drawable_color));
        }
        if (background != 0) {
            iv_image.setBackground(AppCompatResources.getDrawable(requireContext(), background));
        }
        if (bg_color != 0) {
            iv_image.setBackgroundTintList(AppCompatResources.getColorStateList(requireContext(), bg_color));
        }
        if (padding != 0) {
            iv_image.setPadding(padding, padding, padding, padding);
        }
        if (size != 0) {
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(size, size);
            iv_image.setLayoutParams(params);
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
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inNotify.on_notify(true, value_back);
    }
}