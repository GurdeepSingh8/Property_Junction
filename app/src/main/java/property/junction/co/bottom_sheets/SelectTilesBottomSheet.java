package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.R;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.MyMethods;

public class SelectTilesBottomSheet extends BottomSheetDialogFragment {

    TextView tv_head;
    ImageView iv_cross;
    LinearLayoutCompat ll_tiles;
    ArrayList<HashMap<String, Object>> views_tags = new ArrayList<>();
    FirebaseFirestore db;
    String head;
    Activity activity;
    Notify inNotify;
    InFirebase inFirebase;
    MyMethods myMethods;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public SelectTilesBottomSheet(Activity activity, ArrayList<HashMap<String, Object>> views_tags, String head, Notify inNotify) {
        this.activity = activity;
        this.views_tags = views_tags;
        this.head = head;
        this.inNotify = inNotify;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        myMethods = new MyMethods();
        inFirebase = new InFirebase(activity);
        /*int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.select_tiles_bottom_sheet, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        tv_head = v.findViewById(R.id.tv_head);
        iv_cross = v.findViewById(R.id.iv_cross);
        ll_tiles = v.findViewById(R.id.ll_tiles);

        tv_head.setText(Html.fromHtml(head));

        ll_tiles.removeAllViews();

        for (HashMap<String, Object> image_hm : views_tags) {
            String tag = String.valueOf(image_hm.get("tag"));
            int image = Integer.parseInt(String.valueOf(image_hm.get("image")));
            View view = (View) inflater.inflate(R.layout.image_in_cv_layout, null);
            ImageView iv_img = view.findViewById(R.id.iv_img);
            iv_img.setImageDrawable(AppCompatResources.getDrawable(requireContext(), image));
            view.setTag(tag);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inNotify.on_notify(true, String.valueOf(view.getTag()));
                    dismiss();
                }
            });
            ll_tiles.addView(view);
        }

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                inNotify.on_notify(false, "iv_cross");
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //inNotify.on_notify(false, "view_destroyed");
    }
}




