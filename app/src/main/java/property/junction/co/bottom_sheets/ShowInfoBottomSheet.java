package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.TextViewCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import property.junction.co.R;

public class ShowInfoBottomSheet extends BottomSheetDialogFragment {

    TextView tv_head, tv_sub_head;
    Button btn_positive;
    String header, sub_header, btn_text;
    ImageView iv_image;
    int drawable_left = 0;
    int drawable_right = 0;
    int drawable_top = 0;
    int drawable_bottom = 0;
    int img_drawable = 0;
    int size = 0;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public ShowInfoBottomSheet(Activity activity,
                               int img_drawable,
                               int size,
                               int text_drawable,
                               String drawable_location,
                               String header,
                               String sub_header,
                               String text_on_button,
                               Notify inNotify) {
        this.activity = activity;
        this.img_drawable = img_drawable;
        this.size = size;
        this.header = header;
        this.sub_header = sub_header;
        this.btn_text = text_on_button;
        this.inNotify = inNotify;

        switch (drawable_location) {
            case "left":
            case "start":
                this.drawable_left = text_drawable;
                break;
            case "right":
            case "end":
                this.drawable_right = text_drawable;
                break;
            case "up":
            case "top":
                this.drawable_top = text_drawable;
                break;
            case "down":
            case "bottom":
                this.drawable_bottom = text_drawable;
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        /*int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.bottom_sheet_show_info, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        tv_head = v.findViewById(R.id.tv_head);
        tv_sub_head = v.findViewById(R.id.tv_sub_head);
        iv_image = v.findViewById(R.id.iv_image);
        btn_positive = v.findViewById(R.id.btn_positive);

        if (img_drawable != 0) {
            iv_image.setImageDrawable(AppCompatResources.getDrawable(requireContext(), img_drawable));
            if (size != 0) {
                LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(size, size);
                iv_image.setLayoutParams(params);
            }
        } else {
            iv_image.setVisibility(View.GONE);
        }

        if (header == null || header.length() <= 0) {
            tv_head.setVisibility(View.GONE);
        } else {
            tv_head.setText(Html.fromHtml(header));
        }

        if (sub_header == null || sub_header.length() <= 0) {
            tv_sub_head.setVisibility(View.GONE);
        } else {
            tv_sub_head.setText(Html.fromHtml(sub_header));
            if (drawable_left != 0 || drawable_right != 0 || drawable_top != 0 || drawable_bottom != 0) {
                tv_sub_head.setCompoundDrawablesWithIntrinsicBounds(drawable_left, drawable_top, drawable_right, drawable_bottom);
            }
        }

        if (btn_text.length() > 0) {
            btn_positive.setText(btn_text);
        }

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                inNotify.on_notify(true, btn_text);
            }
        });
        return v;
    }
}



