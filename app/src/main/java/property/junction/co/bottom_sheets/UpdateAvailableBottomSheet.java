package property.junction.co.bottom_sheets;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;

import property.junction.co.R;

public class UpdateAvailableBottomSheet extends BottomSheetDialogFragment {

    TextView tv_header, tv_version, tv_update_features;
    Button btn_update_later, btn_update_now;
    ImageView iv_main;
    Activity activity;
    Object value;

    public UpdateAvailableBottomSheet(Activity activity, Object value) {
        this.activity = activity;
        this.value = value;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        /*int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.bottom_sheet_update_available, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));

        tv_header = v.findViewById(R.id.tv_header);
        tv_version = v.findViewById(R.id.tv_version);
        tv_update_features = v.findViewById(R.id.tv_update_features);
        btn_update_later = v.findViewById(R.id.btn_update_later);
        btn_update_now = v.findViewById(R.id.btn_update_now);
        iv_main = v.findViewById(R.id.iv_main);

        HashMap<String, Object> hashMap = (HashMap<String, Object>) value;
        String update_link = String.valueOf(hashMap.get(activity.getString(R.string.update_link)));


        btn_update_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                requireActivity().finishAffinity();
            }
        });

        btn_update_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = requireActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(update_link)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}