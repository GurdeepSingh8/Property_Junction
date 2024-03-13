package property.junction.co.rec_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import property.junction.co.R;

public class RecAdapterCountryList extends RecyclerView.Adapter<RecAdapterCountryList.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    Notify notify;

    public interface Notify {
        void onNotify(int i, HashMap<String, Object> contact);
    }

    public RecAdapterCountryList(Context context, ArrayList<HashMap<String, Object>> list, Notify notify) {
        this.context = context;
        this.list = list;
        this.notify = notify;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_country_names, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HashMap<String, Object> hashMap = list.get(position);
        String flag = String.valueOf(hashMap.get("flag"));
        String country = String.valueOf(hashMap.get("country"));
        String country_code = String.valueOf(hashMap.get("country_code"));

        holder.tv_flag.setText(flag);
        holder.tv_country.setText(country);
        holder.tv_country_code.setText(country_code);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.setEnabled(false);
                notify.onNotify(holder.getAdapterPosition(), hashMap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_flag, tv_country, tv_country_code;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_flag = itemView.findViewById(R.id.tv_flag);
            tv_country = itemView.findViewById(R.id.tv_country);
            tv_country_code = itemView.findViewById(R.id.tv_country_code);
        }
    }
}





