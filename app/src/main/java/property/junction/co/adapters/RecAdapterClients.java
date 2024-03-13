package property.junction.co.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.R;

public class RecAdapterClients extends RecyclerView.Adapter<RecAdapterClients.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    public RecAdapterClients(Context context, ArrayList<HashMap<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_client, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*DocumentSnapshot hashMap = list.get(position);
        setAnimation(holder.itemView, position);
        String client_id = String.valueOf(hashMap.getId());
        String name = String.valueOf(hashMap.getString("name"));
        String number = String.valueOf(hashMap.getString("number"));
        String cc_number = String.valueOf(hashMap.getString("cc_number"));
        String whatsapp_number = String.valueOf(hashMap.getString("whatsapp_number"));
        String cc_whatsapp_number = String.valueOf(hashMap.getString("cc_whatsapp_number"));
        String client_dp = String.valueOf(hashMap.getString("client_dp"));
        String other_number = String.valueOf(hashMap.getString("other_number"));
        String cc_other_number = String.valueOf(hashMap.getString("cc_other_number"));
        String date_added = String.valueOf(hashMap.getString("date_added"));
        String address = String.valueOf(hashMap.getString("address"));
        ArrayList<String> requirement = (ArrayList<String>) hashMap.get("requirement");
        String budget_min = String.valueOf(hashMap.getString("budget_min"));
        String budget_max = String.valueOf(hashMap.getString("budget_max"));
        String note = String.valueOf(hashMap.getString("note"));
        String occupation = String.valueOf(hashMap.getString("occupation"));
        String note_when_added = String.valueOf(hashMap.get("note_when_added"));
        String source = String.valueOf(hashMap.get("source"));
        String priority = String.valueOf(hashMap.get("priority"));
        String status = String.valueOf(hashMap.get("status"));
        String last_pin = String.valueOf(hashMap.get("last_pin"));
        String last_pin_note = String.valueOf(hashMap.get("last_pin_note"));
        String total_pins = String.valueOf(hashMap.get("total_pins"));
        String contact_saved = String.valueOf(hashMap.get("contact_saved"));
        boolean details_sent_on_whatsapp = Boolean.TRUE.equals(hashMap.get("details_sent_on_whatsapp"));
        boolean talked_on_call = Boolean.TRUE.equals(hashMap.get("talked_on_call"));
        boolean ready_for_visit = Boolean.TRUE.equals(hashMap.get("ready_for_visit"));
        boolean visit_done = Boolean.TRUE.equals(hashMap.get("visit_done"));

        Glide.with(holder.iv_client_dp).load(client_dp).into(holder.iv_client_dp);

        if (details_sent_on_whatsapp) {
            holder.iv_whatsapp.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_theme_color)));
            holder.iv_whatsapp.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
        } else {
            holder.iv_whatsapp.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
            holder.iv_whatsapp.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_secondary)));
        }
        if (talked_on_call) {
            holder.iv_call.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_theme_color)));
            holder.iv_call.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
        } else {
            holder.iv_call.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
            holder.iv_call.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_secondary)));
        }
        if (ready_for_visit) {
            holder.iv_ready_for_visit.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_theme_color)));
            holder.iv_ready_for_visit.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
        } else {
            holder.iv_ready_for_visit.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
            holder.iv_ready_for_visit.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_secondary)));
        }
        if (visit_done) {
            holder.iv_visit_done.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_theme_color)));
            holder.iv_visit_done.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
        } else {
            holder.iv_visit_done.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.new_grey_1)));
            holder.iv_visit_done.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.text_secondary)));
        }

        holder.tv_client_name.setText(name);
        if (occupation.length() <= 0 || occupation.equals("null")) {
            myMethods.changeTextColor(context, holder.tv_client_number, R.color.red_g);
            holder.tv_client_number.setText("⚠️ Occupation Not Added");
        } else {
            myMethods.changeTextColor(context, holder.tv_client_number, R.color.dark_grey_1);
            holder.tv_client_number.setText("\uD83D\uDCBC : " + occupation);
        }
        if (note.equals("") || note.equals("null")) {
            holder.tv_note.setVisibility(View.GONE);
        } else {
            holder.tv_note.setVisibility(View.VISIBLE);
        }
        holder.tv_note.setText(note.replace(" , ", ".\n")
                .replace(", ", ".\n")
                .replace(",", ".\n"));
        holder.tv_date_added.setText(String.format("Client added on : %s", myMethods.getTimeChat(date_added)
                .replace(">", "This ")
                .replace("<", "Last ")));
        holder.tv_client_count.setText(String.valueOf(holder.getAdapterPosition() + 1));

        holder.tv_total_pins.setText(total_pins);
        if (last_pin_note.equals("null") || last_pin_note.equals("")) {
            holder.tv_last_pin_note.setVisibility(View.GONE);
            holder.tv_last_pin_text.setVisibility(View.GONE);
            holder.tv_divider.setVisibility(View.GONE);
            holder.tv_last_pin.setVisibility(View.GONE);
        } else {
            holder.tv_last_pin.setText("Last Followup: " + myMethods.getTimeChat(last_pin)
                    .replace(">", "This ")
                    .replace("<", "Last "));
            holder.tv_last_pin_note.setText(last_pin_note.replace(" , ", ".\n")
                    .replace(", ", ".\n")
                    .replace(",", ".\n"));
            holder.tv_last_pin_note.setVisibility(View.VISIBLE);
            holder.tv_last_pin_text.setVisibility(View.VISIBLE);
            holder.tv_divider.setVisibility(View.VISIBLE);
            holder.tv_last_pin.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMethods.setSp(context, "ACTIONS", "update_client_id", client_id);
                Intent intent = new Intent(context, ClientDetails.class);
                intent.putExtra("client_id", client_id);
                *//*HashMap<String, DocumentSnapshot> h_data = new HashMap<>();
                h_data.put("document_snapshot", hashMap);
                intent.putExtra("client_data", h_data);*//*
                if (holder.itemView.getTag() != null) {
                    intent.putExtra("perform", holder.itemView.getTag().toString());
                }
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_client_name, tv_client_number, tv_note, tv_date_added, tv_total_pins,
                tv_last_pin, tv_last_pin_note, tv_client_count, tv_last_pin_text, tv_divider;
        ImageView iv_whatsapp, iv_client_dp, iv_call, iv_ready_for_visit, iv_visit_done;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           /* tv_client_name = itemView.findViewById(R.id.tv_client_name);
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
