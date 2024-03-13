package property.junction.co.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import property.junction.co.R;
import property.junction.co.methods.UniMethods;

public class RecAdapterAddPropertyImages extends RecyclerView.Adapter<RecAdapterAddPropertyImages.MyViewHolder> {

    Context context;
    ArrayList<String> list = new ArrayList<>();
    OnClick onClick;

    public RecAdapterAddPropertyImages(Context context, ArrayList<String> list, OnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick = onClick;
    }

    public interface OnClick {
        void imageClicked(int i, String s, Object o);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image;
        if (position == 0) {
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.iv_img.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.img_add_image));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.imageClicked(holder.getAdapterPosition(), "add_image", holder);
                }
            });
        } else {
            image = list.get(position);
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.imageClicked(holder.getAdapterPosition(), "delete", image);
                }
            });
            Glide.with(holder.iv_img).load(image).into(holder.iv_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_img, iv_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}



