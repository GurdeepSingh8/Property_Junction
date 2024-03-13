package property.junction.co.bottom_sheets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.R;
import property.junction.co.methods.InFirebase;
import property.junction.co.methods.MyMethods;
import property.junction.co.methods.UniMethods;

public class AddFollowupEventBottomSheet extends BottomSheetDialogFragment {

    FirebaseFirestore db;
    Activity activity;
    String value;
    String key = "note";
    String action_type, followup_id;
    Notify inNotify;

    String reminder_date = "000000000000";
    String reminder_date_action_from_previous;
    String note = "";

    ImageView iv_icon, iv_cancel_reminder;
    TextView tv_head, tv_header, tv_add_reminder;
    Button btn_add_followup;
    TextInputLayout til_note;
    ProgressBar prog_add_followup;
    UniMethods uniMethods;
    MyMethods myMethods;
    InFirebase inFirebase;
    HashMap<String, Object> hashMap = new HashMap<>();

    public interface Notify {
        void on_notify(boolean condition, String value, Object o);
    }

    public AddFollowupEventBottomSheet(Activity activity, String value, HashMap<String, Object> hashMap, Notify inNotify) {
        this.activity = activity;
        this.value = value;
        this.inNotify = inNotify;
        this.hashMap = hashMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        /*int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;*/
        View v = inflater.inflate(R.layout.bottom_sheet_add_followup_event, container, false);
        //v.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        uniMethods = new UniMethods((UniMethods.Notifier) requireContext());
        myMethods = new MyMethods();
        inFirebase = new InFirebase(activity);
        reminder_date = "000000000000";
        iv_icon = v.findViewById(R.id.iv_icon);
        iv_cancel_reminder = v.findViewById(R.id.iv_cancel_reminder);
        tv_head = v.findViewById(R.id.tv_head);
        tv_header = v.findViewById(R.id.tv_header);
        tv_add_reminder = v.findViewById(R.id.tv_add_reminder);
        btn_add_followup = v.findViewById(R.id.btn_add_followup);
        til_note = v.findViewById(R.id.til_note);
        prog_add_followup = v.findViewById(R.id.prog_add_followup);

        iv_cancel_reminder.setVisibility(View.GONE);

        Objects.requireNonNull(til_note.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                til_note.setError(null);
                note = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        int labelColor11 = getResources().getColor(R.color.app_theme_color);
        String сolorString11 = String.format("%X", labelColor11).substring(2);
        String line11 = String.format("<font color=\"#%s\"><b>" + "Set Reminder" + "</b></font>", сolorString11);
        int labelColor22 = getResources().getColor(R.color.background_1);
        String сolorString22 = String.format("%X", labelColor22).substring(2);
        String line22 = String.format("<font color=\"#%s\"><small><small>" + "For Next Followup" + "</small></small></font>", сolorString22);
        tv_add_reminder.setText(Html.fromHtml(line11 + "<br>" + line22));

        if (hashMap != null) {
            action_type = String.valueOf(hashMap.get("action_type"));
        } else {
            action_type = "custom_task";
        }

        if (action_type.equals("edit")) {
            String note = String.valueOf(hashMap.get("note"));
            followup_id = String.valueOf(hashMap.get("followup_id"));
            til_note.getEditText().setText(note);
            btn_add_followup.setText("Update Followup");
            reminder_date = String.valueOf(hashMap.get("future_event"));
            if (!reminder_date.equals("000000000000") && !reminder_date.equals("empty") && !reminder_date.equals("0")) {
                tv_add_reminder.setText("Reset Reminder");
                reminder_date_action_from_previous = "available";
                iv_cancel_reminder.setVisibility(View.VISIBLE);
                int labelColor = getResources().getColor(R.color.background_1);
                String сolorString = String.format("%X", labelColor).substring(2);
                String line1 = String.format("<font color=\"#%s\"><small><small>" + "Reminder set for" + "</small></small></font>", сolorString);
                int labelColor1 = getResources().getColor(R.color.app_theme_color);
                String сolorString1 = String.format("%X", labelColor1).substring(2);
                String line2 = String.format("<font color=\"#%s\"><b>" + uniMethods.getDayName(reminder_date) + ", " +
                        uniMethods.getMonthName(reminder_date) + " " + reminder_date.substring(4, 6) +
                        ", at " + uniMethods.getAmPmTime(reminder_date) + "</b></font>", сolorString1);
                tv_add_reminder.setText(Html.fromHtml(line1 + "<br>" + line2));
                iv_cancel_reminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iv_cancel_reminder.setVisibility(View.GONE);
                        reminder_date = "000000000000";
                        int labelColor = getResources().getColor(R.color.app_theme_color);
                        String сolorString = String.format("%X", labelColor).substring(2);
                        String line1 = String.format("<font color=\"#%s\"><b>" + "Set Reminder" + "</b></font>", сolorString);
                        int labelColor1 = getResources().getColor(R.color.background_1);
                        String сolorString1 = String.format("%X", labelColor1).substring(2);
                        String line2 = String.format("<font color=\"#%s\"><small><small>" + "For Next Followup" + "</small></small></font>", сolorString1);
                        tv_add_reminder.setText(Html.fromHtml(line1 + "<br>" + line2));
                    }
                });
            } else {
                reminder_date_action_from_previous = "none";
            }
        }

        iv_icon.setImageTintList(ColorStateList.valueOf(requireContext().getResources().getColor(R.color.app_theme_color)));
        db = FirebaseFirestore.getInstance();
        switch (value) {
            case "note":
                key = "note";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Add Followup Note");
                tv_header.setText("Add an updated followup of this client");
                break;
            case "whatsapp":
                key = "details_sent_on_whatsapp";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Whatsapp Update");
                tv_header.setText("Update whenever you send something on whatsapp");
                break;
            case "call":
                key = "talked_on_call";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Call Update");
                tv_header.setText("Update whenever you call the client");
                break;
            case "ready":
                key = "ready_for_visit";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Ready For Visit");
                tv_header.setText("Update if client is ready for visit");
                break;
            case "done":
                key = "visit_done";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Visit Done");
                tv_header.setText("Update if client has visited your property");
                break;
            case "custom_task":
                key = "custom_task";
                iv_icon.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.img_padlock));
                tv_head.setText("Add Task");
                tv_header.setText("add a task with reminder date to get notification");
                break;
            default:
                break;
        }

        tv_add_reminder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // todo tv_add_reminder.setEnabled(false);
                myMethods.openDatePicker(requireContext(), new MyMethods(new MyMethods.CallReturn() {
                    @Override
                    public void onCallReturn(String s, boolean b, Object o) {
                        if (b) {
                            reminder_date = s;
                            if (!reminder_date.equals("000000000000")) {
                                tv_add_reminder.setText("Reset Reminder");
                                iv_cancel_reminder.setVisibility(View.VISIBLE);
                                int labelColor = getResources().getColor(R.color.background_1);
                                String сolorString = String.format("%X", labelColor).substring(2);
                                String line1 = String.format("<font color=\"#%s\"><small><small>" + "Reminder set for" + "</small></small></font>", сolorString);
                                int labelColor1 = getResources().getColor(R.color.app_theme_color);
                                String сolorString1 = String.format("%X", labelColor1).substring(2);
                                String line2 = String.format("<font color=\"#%s\"><b>" + uniMethods.getDayName(reminder_date) + ", " +
                                        uniMethods.getMonthName(reminder_date) + " " + reminder_date.substring(4, 6) +
                                        ", at " + uniMethods.getAmPmTime(reminder_date) + "</b></font>", сolorString1);
                                tv_add_reminder.setText(Html.fromHtml(line1 + "<br>" + line2));
                                iv_cancel_reminder.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        iv_cancel_reminder.setVisibility(View.GONE);
                                        reminder_date = "000000000000";
                                        int labelColor = getResources().getColor(R.color.app_theme_color);
                                        String сolorString = String.format("%X", labelColor).substring(2);
                                        String line1 = String.format("<font color=\"#%s\"><b>" + "Set Reminder" + "</b></font>", сolorString);
                                        int labelColor1 = getResources().getColor(R.color.background_1);
                                        String сolorString1 = String.format("%X", labelColor1).substring(2);
                                        String line2 = String.format("<font color=\"#%s\"><small><small>" + "For Next Followup" + "</small></small></font>", сolorString1);
                                        tv_add_reminder.setText(Html.fromHtml(line1 + "<br>" + line2));
                                    }
                                });
                            }
                        }
                    }
                }));
            }
        });

        btn_add_followup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                uniMethods.onPressNew(btn_add_followup, prog_add_followup);
                if (Objects.requireNonNull(til_note.getEditText()).getText().toString().length() <= 0) {
                    til_note.setError("Please Add a Note");
                    uniMethods.onReleaseNew(btn_add_followup, prog_add_followup);
                    return;
                }
                if (hashMap != null) {
                    String name = String.valueOf(hashMap.get("name"));
                    ArrayList<HashMap<String, Object>> phone_numbers = (ArrayList<HashMap<String, Object>>) hashMap.get("phone_numbers");
                    String client_id = String.valueOf(hashMap.get("client_id"));
                    String date_added = String.valueOf(hashMap.get("date_added"));
                    String address = String.valueOf(hashMap.get("address"));
                    String requirement = String.valueOf(hashMap.get("requirement"));
                    String note = String.valueOf(hashMap.get("note"));
                    String time = String.valueOf(hashMap.get("time"));
                    String followup_type = String.valueOf(hashMap.get("followup_type"));
                    int pins_count = Integer.parseInt(String.valueOf(hashMap.get("pins_count")));
                    String source = String.valueOf(hashMap.get("source"));
                    String priority = String.valueOf(hashMap.get("priority"));
                    String status = String.valueOf(hashMap.get("status"));
                    String contact_saved = String.valueOf(hashMap.get("contact_saved"));
                    boolean details_sent_on_whatsapp = Boolean.TRUE.equals(hashMap.get("details_sent_on_whatsapp"));
                    boolean talked_on_call = Boolean.TRUE.equals(hashMap.get("talked_on_call"));
                    boolean ready_for_visit = Boolean.TRUE.equals(hashMap.get("ready_for_visit"));
                    boolean visit_done = Boolean.TRUE.equals(hashMap.get("visit_done"));

                    if (action_type.equals("edit")) {
                        /*inFirebase.updateFollowUp(client_id, followup_id, Objects.requireNonNull(til_note.getEditText()).getText().toString()
                                , reminder_date, followup_type, time, new InFirebase(new InFirebase.OnMethodCompleteListener() {
                                    @Override
                                    public void onComplete(int code, String s1, String s2, boolean condition, Object v) {
                                        if (condition) {
                                            if (s1.equals("updateFollowUpWithEvent")) {
                                                inNotify.on_notify(true, "updateFollowUpWithEvent", v);
                                                dismiss();
                                            } else if (s1.equals("updateFollowUp")) {
                                                inNotify.on_notify(true, "updateFollowUp", v);
                                                dismiss();
                                            } else {
                                                Toast.makeText(activity, "Unmatched Task", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                        }
                                    }
                                }));*/
                    } else {
                        /*inFirebase.addFollowUp(name, phone_numbers, client_id, value,
                                Objects.requireNonNull(til_note.getEditText()).getText().toString()
                                , reminder_date, new InFirebase(new InFirebase.OnMethodCompleteListener() {
                                    @Override
                                    public void onComplete(int code, String s1, String s2, boolean condition, Object v) {
                                        if (condition) {
                                            if (s1.equals("addFollowUpWithEvent")) {
                                                inNotify.on_notify(true, "addFollowUpWithEvent", v);
                                                dismiss();
                                            } else if (s1.equals("addFollowUp")) {
                                                inNotify.on_notify(true, "addFollowUp", v);
                                                dismiss();
                                            } else {
                                                Toast.makeText(activity, "Unmatched Task", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                        }
                                    }
                                }));*/
                    }
                } else {
                    /*inFirebase.addCustomTask(value, Objects.requireNonNull(til_note.getEditText()).getText().toString()
                            , reminder_date, new InFirebase(new InFirebase.OnMethodCompleteListener() {
                                @Override
                                public void onComplete(int code, String s1, String s2, boolean condition, Object v) {
                                    if (condition) {
                                        if (s1.equals("addFollowUpWithEvent")) {
                                            inNotify.on_notify(true, "addFollowUpWithEvent", v);
                                            dismiss();
                                        } else if (s1.equals("addFollowUp")) {
                                            inNotify.on_notify(true, "addFollowUp", v);
                                            dismiss();
                                        } else {
                                            Toast.makeText(activity, "Unmatched Task", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                    }
                                }
                            }));*/
                }
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        inNotify.on_notify(false, "view_destroyed", "none");
    }

}
