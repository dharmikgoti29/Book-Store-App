package com.example.admin_book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class modeladapter extends RecyclerView.Adapter<modeladapter.myview> {

    Context context;
    ArrayList<model> list;
    modeladapter(Context context, ArrayList<model> list)
    {
        this.context=context;
        this.list=list;
    }
    {

    }

    @NonNull
    @Override
    public myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.demo_view_book,parent,false);
        myview myview= new myview(v);
        return myview;

    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull myview holder, int position) {

        holder.name.setText(list.get(position).bname);
        holder.author.setText(list.get(position).aname);
        String imageUrl = list.get(position).pic;
        Glide.with(context)
                .load(imageUrl)
                .into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = list.get(position).did;
                Intent intent = new Intent(context,Book.class);
                intent.putExtra("Documentid",id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class myview extends RecyclerView.ViewHolder {
        TextView name,author;
        ImageView img;
        public myview(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.demo_bookname);
            author=itemView.findViewById(R.id.demo_authorname);
            img=itemView.findViewById(R.id.demo_image);

        }
    }
}
