package com.example.assignmet_ph31536.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignmet_ph31536.Home;
import com.example.assignmet_ph31536.Model.FruitModel;
import com.example.assignmet_ph31536.R;

import java.text.NumberFormat;
import java.util.List;

public class AdapterFruit extends RecyclerView.Adapter<AdapterFruit.ViewHolder>{
    private List<FruitModel> list;
    private Context context;
    private Home home;

    public AdapterFruit(List<FruitModel> list, Context context, Home home) {
        this.list = list;
        this.context = context;
        this.home = home;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FruitModel fruitModel = list.get(position);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();

        holder.tvTen.setText("Tên: "+fruitModel.getTen());
        holder.tvGia.setText("Giá: " +numberFormat.format(fruitModel.getGia())+" đ");
        holder.tvSoLuong.setText("Số lượng: "+numberFormat.format(fruitModel.getSoLuong()));

        Glide.with(context)
                .load(fruitModel.getAvatar())
                .thumbnail(Glide.with(context).load(R.drawable.loading))
                .into(holder.image);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.xoa(fruitModel.get_id());
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home.them(context, 1, fruitModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvSoLuong, tvGia;
        Button btnDelete, btnUpdate;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTen = itemView.findViewById(R.id.tvTen);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvGia = itemView.findViewById(R.id.tvGia);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }

}
