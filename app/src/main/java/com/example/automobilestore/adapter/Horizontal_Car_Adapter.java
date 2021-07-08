package com.example.automobilestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.CarDetails;
import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.R;
import com.example.automobilestore.databinding.ActivityCarDetailsBinding;
import com.example.automobilestore.model.HorizontalCarData;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Horizontal_Car_Adapter extends RecyclerView.Adapter<Horizontal_Car_Adapter.HorizontalViewHolder> {

    Context context;
    List<HorizontalCarData> HorizontalList;



    public Horizontal_Car_Adapter(Context context, List<HorizontalCarData> popularFoodList) {
        this.context = context;
        this.HorizontalList = popularFoodList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {

        Picasso.get().load(HorizontalList.get(position).getImageUrl()).fit().into(holder.carh_image);
        holder.h_name.setText(HorizontalList.get(position).getName());
        holder.h_amount.setText("$"+HorizontalList.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Toast.makeText(context, "position"+position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(view.getContext(), CarDetails.class);
                i.putExtra("id", HorizontalList.get(position).getId());
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return HorizontalList.size();
    }


    public static final class HorizontalViewHolder extends RecyclerView.ViewHolder{


        ImageView carh_image;
        TextView h_amount, h_name;

        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);

            carh_image = itemView.findViewById(R.id.carh_image);
            h_amount = itemView.findViewById(R.id.h_amount);
            h_name = itemView.findViewById(R.id.h_name);



        }
    }

}
