package com.example.ronifitgo.ronifitgo.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ronifitgo.R;
import com.example.ronifitgo.ronifitgo.Object.User;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_trainer extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public interface TrainerListener {
            void click(User trainer, int position);
        }
    private Activity activity;
    private ArrayList<User> users;
    private TrainerListener trainerListener;


    public Adapter_trainer(Activity activity, ArrayList<User> users) {
        this.activity = activity;
        this.users = users;
    }

    public void setTrainerListener(TrainerListener trainerListener) {
        this.trainerListener = trainerListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        UserHolder holder = new UserHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final UserHolder holder = (UserHolder) viewHolder;
        User user= getUser(position);

        holder.listUser_TXT_name.setText(user.getName().toString());
        Glide
                .with(activity)
                .load(user.getProfileImgUrl())
                .into(holder.listUser_IMG_pic);


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public User getUser(int position){
        return users.get(position);
    }

    class UserHolder extends RecyclerView.ViewHolder {

        private MaterialTextView listUser_TXT_name;
        private CircleImageView listUser_IMG_pic;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            listUser_TXT_name =itemView.findViewById(R.id.listUser_TXT_name);
            listUser_IMG_pic = itemView.findViewById(R.id.listUser_IMG_pic);

            listUser_TXT_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (trainerListener != null) {
                        trainerListener.click(getUser(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });

        }
    }
}