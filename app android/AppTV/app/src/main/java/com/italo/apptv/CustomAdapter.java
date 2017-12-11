package com.italo.apptv;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by italo on 10/12/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private Context context;
    private List<Thumbnail> myData;

    public CustomAdapter(Context context, List<Thumbnail> myData) {
        this.context = context;
        this.myData = myData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent,
                false);

        return new ViewHolder(itemView, context, myData);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(myData.get(position).getName());
        holder.id = position + 1;
        Glide.with(context).load(myData.get(position).getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView imageView;
        public List<Thumbnail> myData = new ArrayList<Thumbnail>();
        public Context context;
        public int id;

        public ViewHolder(View itemView, final Context context, final List<Thumbnail> myData) {
            super(itemView);

            this.myData = myData;
            this.context = context;
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShowDetailsActivity.class);
                    intent.putExtra("id", myData.get(id).getId() - 1);
                    context.startActivity(intent);
                }
            });
        }
    }
}
