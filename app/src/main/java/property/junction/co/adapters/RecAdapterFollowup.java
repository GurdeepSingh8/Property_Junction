package property.junction.co.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.R;

public class RecAdapterFollowup extends RecyclerView.Adapter<RecAdapterFollowup.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    public RecAdapterFollowup(Context context, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        switch (Objects.requireNonNull(String.valueOf(list.get(position).get(context.getString(R.string.view))))) {
            case "client_added":
                return 1;
            case "activity":
                return 2;
            case "add_activity":
                return 3;
            case "call_log":
                return 4;
            case "client_details":
                return 5;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_default_follow_up, parent, false));
            case 2:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_follow_up, parent, false));
            case 3:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_add_follow_up, parent, false));
            case 5:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_client_details, parent, false));
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*int labelColor = getResources().getColor(R.color.text_hint);
        String сolorString = String.format("%X", labelColor).substring(2);
        String line1 = String.format("<font color=\"#%s\">" + "No follow up scheduled." + "</font>", сolorString);
        int labelColor2 = getResources().getColor(R.color.app_theme_color);
        String сolorString2 = String.format("%X", labelColor2).substring(2);
        String line2 = String.format("<font color=\"#%s\">" + "<b>" + " Schedule Now" + "</b></font>", сolorString2);
        String head = line1 + line2;

        tv_follow_up.setText(Html.fromHtml(head));*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_follow_up, tv_client_name, tv_client_number, tv_note, tv_date_added, tv_total_pins,
                tv_last_pin, tv_last_pin_note, tv_client_count, tv_last_pin_text, tv_divider;
        ImageView iv_whatsapp, iv_client_dp, iv_call, iv_ready_for_visit, iv_visit_done;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           /* tv_client_name = itemView.findViewById(R.id.tv_client_name);
            tv_follow_up = itemView.findViewById(R.id.tv_follow_up);
            iv_client_dp = itemView.findViewById(R.id.iv_client_dp);
            tv_client_number = itemView.findViewById(R.id.tv_client_number);
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_date_added = itemView.findViewById(R.id.tv_date_added);
            tv_total_pins = itemView.findViewById(R.id.tv_total_pins);
            tv_last_pin = itemView.findViewById(R.id.tv_last_pin);
            tv_last_pin_note = itemView.findViewById(R.id.tv_last_pin_note);
            iv_whatsapp = itemView.findViewById(R.id.iv_whatsapp);
            iv_call = itemView.findViewById(R.id.iv_call);
            iv_ready_for_visit = itemView.findViewById(R.id.iv_ready_for_visit);
            iv_visit_done = itemView.findViewById(R.id.iv_visit_done);
            tv_client_count = itemView.findViewById(R.id.tv_client_count);
            tv_last_pin_text = itemView.findViewById(R.id.tv_last_pin_text);
            tv_divider = itemView.findViewById(R.id.tv_divider);*/
        }
    }
}
