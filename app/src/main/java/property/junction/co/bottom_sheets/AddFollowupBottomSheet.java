package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import property.junction.co.R;
import property.junction.co.methods.UniMethods;

public class AddFollowupBottomSheet extends BottomSheetDialogFragment {

    ConstraintLayout cons_add_note, cons_add_whatsapp, cons_add_call, cons_add_visit_ready, cons_add_visit_done;
    FirebaseFirestore db;
    Activity activity;
    Notify inNotify;

    public interface Notify {
        void on_notify(boolean condition, String value);
    }

    public AddFollowupBottomSheet(Activity activity, Notify inNotify) {
        this.activity = activity;
        this.inNotify = inNotify;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.bottom_sheet_add_followup, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        db = FirebaseFirestore.getInstance();

        cons_add_note = v.findViewById(R.id.cons_add_note);
        cons_add_whatsapp = v.findViewById(R.id.cons_add_whatsapp);
        cons_add_call = v.findViewById(R.id.cons_add_call);
        cons_add_visit_ready = v.findViewById(R.id.cons_add_visit_ready);
        cons_add_visit_done = v.findViewById(R.id.cons_add_visit_done);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inNotify.on_notify(true, view.getTag().toString());
                dismiss();
            }
        };

        cons_add_note.setOnClickListener(onClickListener);
        cons_add_whatsapp.setOnClickListener(onClickListener);
        cons_add_call.setOnClickListener(onClickListener);
        cons_add_visit_ready.setOnClickListener(onClickListener);
        cons_add_visit_done.setOnClickListener(onClickListener);

        UniMethods uniMethods = new UniMethods((UniMethods.Notifier) requireContext());

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inNotify.on_notify(false, "view_destroyed");
    }
}