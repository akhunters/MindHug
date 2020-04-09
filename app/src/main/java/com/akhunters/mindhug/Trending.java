package com.akhunters.mindhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.akhunters.mindhug.databinding.ActivityTrendingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Trending extends AppCompatActivity {

    ActivityTrendingBinding binding;
    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager manager;
    List<Post> items = new ArrayList<>();
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrendingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.recyclerViewTrending;
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Trending.this, "opened", Toast.LENGTH_SHORT).show();
            }
        });
        manager = new LinearLayoutManager(this);
        adapter = new Adapter(this, items);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        items = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        setContentPosts();
    }

    public void setContentPosts() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post post = new Post();
                    post.title = dataSnapshot1.child("title").getValue().toString();
                    post.location = dataSnapshot1.child("location").getValue().toString();
                    post.date = dataSnapshot1.child("date").getValue().toString();
                    post.profileUrl = dataSnapshot1.child("profileUrl").getValue().toString();
                    post.category = dataSnapshot1.child("category").getValue().toString();
                    items.add(post);

                }
                adapter = new Adapter(getApplicationContext(), items);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Trending.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }
}