package com.example.automobilestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.UpdateAd;
import com.example.automobilestore.R;
import com.example.automobilestore.model.PostListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostListViewHolder> {
    Context context;
    List<PostListModel> postlist;
    FirebaseFirestore db;
    FirebaseUser curUser;
    FirebaseAuth auth;
    String userid = null;


    public PostListAdapter(Context context, List<PostListModel> postlist) {
        this.context = context;
        this.postlist = postlist;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            userid = curUser.getUid();
        }

    }

    @NonNull
    @Override
    public PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postlist_item, parent, false);

        return new PostListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostListViewHolder holder, final int position) {
        Picasso.get().load(postlist.get(position).getImage()).fit().into(holder.image);
        holder.Price.setText("Price:- " + postlist.get(position).getAmount() + "$");
        holder.Year.setText("Year:- " + postlist.get(position).getYear());
        holder.Engine_type.setText("Engine Type:- " + postlist.get(position).getEngine_type());
        holder.Model.setText(postlist.get(position).getModel());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), UpdateAd.class);
                i.putExtra("id", postlist.get(position).getCarId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }


    public static final class PostListViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView Price, Engine_type, Year, Model;
        View item;

        public PostListViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.postlist_image);
            Price = itemView.findViewById(R.id.postlist_price);
            Engine_type = itemView.findViewById(R.id.postlist_Type);
            Year = itemView.findViewById(R.id.postlist_Year);
            Model = itemView.findViewById(R.id.postlist_Model);
            item = itemView;


        }
    }
}
