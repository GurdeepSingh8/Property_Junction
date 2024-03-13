package property.junction.co;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.adapters.RecAdapterFollowUps;
import property.junction.co.methods.UniMethods;

public class FollowUps extends AppCompatActivity {

    Context context = FollowUps.this;
    Activity activity = FollowUps.this;
    ArrayList<HashMap<String, Object>> list_follow_up_category = new ArrayList<>();

    UniMethods uniMethods = new UniMethods();

    RecyclerView rec_followups;
    RecAdapterFollowUps recAdapterFollowUps;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_ups);

        uniMethods.backButtonPanel(activity, "Follow ups");

        rec_followups = findViewById(R.id.rec_followups);

        uniMethods.makeRecViewV(rec_followups, context);
        RecAdapterFollowUps.TaskClickListener taskClickListener = new RecAdapterFollowUps.TaskClickListener() {
            @Override
            public void onTaskClicked(String task, int pos, String client_id, HashMap<String, Object> hm_data) {

            }
        };
        recAdapterFollowUps = new RecAdapterFollowUps(context, list_follow_up_category, taskClickListener);
        rec_followups.setAdapter(recAdapterFollowUps);

        HashMap<String, Object> hm_category_1 = new HashMap<>();
        hm_category_1.put("category_icon", R.drawable.img_calender_overdue);
        hm_category_1.put("category_text", "Overdue");
        hm_category_1.put("category_count", "3");
        hm_category_1.put(context.getString(R.string.view), "category");
        hm_category_1.put("tint", R.color.red_g);
        hm_category_1.put("time", "");

        HashMap<String, Object> hm_category_2 = new HashMap<>();
        hm_category_2.put("category_icon", R.drawable.img_calender_upcoming);
        hm_category_2.put("category_text", "Upcoming");
        hm_category_2.put("category_count", "32");
        hm_category_2.put(context.getString(R.string.view), "category");
        hm_category_2.put("tint", R.color.text_body);
        hm_category_2.put("time", "");

        HashMap<String, Object> hm_category_3 = new HashMap<>();
        hm_category_3.put("category_icon", R.drawable.img_calender_anyday);
        hm_category_3.put("category_text", "Someday");
        hm_category_3.put("category_count", "12");
        hm_category_3.put(context.getString(R.string.view), "category");
        hm_category_3.put("tint", R.color.text_hint);
        hm_category_3.put("time", "");

        HashMap<String, Object> hm_category_4 = new HashMap<>();
        hm_category_4.put("category_text", "Today");
        hm_category_4.put("category_count", "0");
        hm_category_4.put(context.getString(R.string.view), "date");

        list_follow_up_category.add(hm_category_1);
        list_follow_up_category.add(hm_category_2);
        list_follow_up_category.add(hm_category_3);
        list_follow_up_category.add(hm_category_4);

        HashMap<String, Object> hm_category_5 = new HashMap<>();
        hm_category_5.put("category_text", "Today");
        hm_category_5.put("category_count", "0");
        hm_category_5.put(context.getString(R.string.view), "event");
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);
        list_follow_up_category.add(hm_category_5);


        HashMap<String, Object> hm_category_6 = new HashMap<>();
        hm_category_6.put("category_text", "Today");
        hm_category_6.put("category_count", "0");
        hm_category_6.put(context.getString(R.string.view), "left_clients");
        list_follow_up_category.add(hm_category_6);
        recAdapterFollowUps.notifyDataSetChanged();

    }
}