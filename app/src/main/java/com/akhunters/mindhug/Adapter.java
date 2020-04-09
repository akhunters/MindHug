package com.akhunters.mindhug;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    Context context;
    List<Post> items;

    public Adapter(Context context, List<Post> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trending_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        final Post post = items.get(position);
        holder.time.setText(post.date);
        holder.title.setText(post.title);
        holder.location.setText(post.location);
        holder.category.setText(post.category);

        if(position == items.size()-1){

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0,120);
            holder.back.setLayoutParams(params);
        }

        /*if (position == 0){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 120, 0,0);
            holder.margin.setLayoutParams(params);
        }
*/
        if(position%2 == 0)
            holder.back.setBackgroundResource(R.drawable.trend_color);

        holder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Details.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

        Glide.with(context)

                .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                .load(post.profileUrl)
                .error(R.drawable.profile_avatar)
                .into(holder.profile);

        /*holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifyStudent.class);
                intent.putExtra("phoneNumber", student.getPhoneNumber());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView time;
        MaterialTextView title;
        MaterialTextView location;
        MaterialTextView category;
        CircleImageView profile;
        LinearLayout back;
        RelativeLayout margin;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeItem);
            title = itemView.findViewById(R.id.titleItem);
            location = itemView.findViewById(R.id.addressItem);
            profile = itemView.findViewById(R.id.profileItem);
            back = itemView.findViewById(R.id.back);
            category = itemView.findViewById(R.id.category);
            margin = itemView.findViewById(R.id.margin);
        }
    }
}
