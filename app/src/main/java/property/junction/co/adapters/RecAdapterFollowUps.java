package property.junction.co.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import property.junction.co.R;
import property.junction.co.methods.UniMethods;

public class RecAdapterFollowUps extends RecyclerView.Adapter<RecAdapterFollowUps.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    UniMethods uniMethods = new UniMethods();
    TaskClickListener taskClickListener;
    LayoutInflater inflater;

    public interface TaskClickListener {
        void onTaskClicked(String task, int pos, String client_id, HashMap<String, Object> hm_data);
    }

    public RecAdapterFollowUps(Context context, ArrayList<HashMap<String, Object>> list, TaskClickListener taskClickListener) {
        this.context = context;
        this.list = list;
        this.taskClickListener = taskClickListener;
        inflater = (LayoutInflater) Objects.requireNonNull(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        switch (Objects.requireNonNull(String.valueOf(list.get(position).get(context.getString(R.string.view))))) {
            case "category":
                return 1;
            case "date":
                return 2;
            case "event":
                return 3;
            case "left_clients":
                return 4;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_ll_follow_up_category, parent, false));
            case 2:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_date, parent, false));
            case 3:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.row_client_to_follow_up, parent, false));
            case 4:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_client_left, parent, false));
            default:
                return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_ll_follow_up_category, parent, false));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HashMap<String, Object> hashMap = list.get(position);
        switch (Objects.requireNonNull(String.valueOf(list.get(position).get(context.getString(R.string.view))))) {
            case "category":
                int tint = (int) hashMap.get("tint");
                int category_icon = (int) hashMap.get("category_icon");

                holder.iv_category_icon.setImageDrawable(AppCompatResources.getDrawable(context, category_icon));
                holder.iv_category_icon.setImageTintList(AppCompatResources.getColorStateList(context, tint));

                uniMethods.changeTextColor(context, holder.tv_category, tint);
                holder.tv_category.setText(String.valueOf(hashMap.get("category_text")));

                uniMethods.changeTextColor(context, holder.tv_count, tint);
                holder.tv_count.setCompoundDrawableTintList(AppCompatResources.getColorStateList(context, tint));
                holder.tv_count.setText(String.valueOf(hashMap.get("category_count")));
                break;
            case "left_clients":
                break;
            case "date":
                String date = String.valueOf(hashMap.get("category_text"));
                String count = String.valueOf(hashMap.get("category_count"));
                /*String date = uniMethods.getTimeChat(String.valueOf(hashMap.get("event_date")) + "000000")
                        .replace(">", "This ")
                        .replace("<", "Last ");
                if (date.contains("am") || date.contains("pm")) {
                    date = date.substring(0, date.indexOf(","));
                } else if (date.contains("This") && date.matches("^.*\\d$")) {
                    date = date.replace("This ", "");
                }*/
                holder.tv_date.setText(date + " (" + count + ")");
                break;
            case "event":
                String name = String.valueOf(hashMap.get("name"));
                String number = String.valueOf(hashMap.get("number"));
                String client_id = String.valueOf(hashMap.get("client_id"));
                String event_id = String.valueOf(hashMap.get("event_id"));
                String whatsapp_number = String.valueOf(hashMap.get("whatsapp_number"));
                String other_number = String.valueOf(hashMap.get("other_number"));
                String time = String.valueOf(hashMap.get("time"));
                String address = String.valueOf(hashMap.get("address"));
                String requirement = String.valueOf(hashMap.get("requirement"));
                String note = String.valueOf(hashMap.get("note"));
                String source = String.valueOf(hashMap.get("source"));
                String priority = String.valueOf(hashMap.get("priority"));
                String status = String.valueOf(hashMap.get("status"));
                String total_pins = String.valueOf(hashMap.get("total_pins")).replace("null", "0");
                String last_pin = String.valueOf(hashMap.get("last_pin"));
                String last_pin_note = String.valueOf(hashMap.get("last_pin_note"));
                String contact_saved = String.valueOf(hashMap.get("contact_saved"));
                String future_event = String.valueOf(hashMap.get("future_event"));

                String main_time = uniMethods.getAmPmTime(future_event);
                String numerical_time = main_time.substring(0, main_time.length() - 3).replace(" ", "");
                String am_pm_time = main_time.substring(main_time.length() - 3).replace(" ", "");
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_category, tv_count, tv_date, tv_total_pins,
                tv_last_pin, tv_last_pin_note, tv_client_count, tv_time_main, tv_time_sub;
        ImageView iv_category_icon, iv_call, iv_ready_for_visit, iv_visit_done, iv_lower, iv_upper, iv_checkbox;
        LottieAnimationView lott_clock;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_count = itemView.findViewById(R.id.tv_count);
            iv_category_icon = itemView.findViewById(R.id.iv_category_icon);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}