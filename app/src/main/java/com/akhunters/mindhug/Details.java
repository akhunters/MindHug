package com.akhunters.mindhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.akhunters.mindhug.databinding.ActivityDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class Details extends AppCompatActivity {

    ActivityDetailsBinding binding;

    ImageView back;
    MaterialCardView goingTick;
    MaterialCardView box;
    MaterialCardView cancelTick;
    MaterialTextView textGoing;


    //setContent
    MaterialTextView title;
    MaterialTextView peopleCount;
    MaterialTextView spots;
    MaterialTextView name;
    MaterialTextView email;
    MaterialTextView day;
    MaterialTextView timeSpan;
    MaterialTextView address1;
    MaterialTextView address2;
    MaterialTextView hostName;
    MaterialTextView description;
    MaterialTextView spotBox;

    CircleImageView hostProfile;

    String goingPeople;
    String spotsLeft;

    DatabaseReference ref;

    String position = null;
    int i = 0;
    String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        back = binding.back;
        goingTick = binding.goingTick;
        box = binding.goingBox;
        cancelTick = binding.cancelTick;
        textGoing = binding.textGoing;
        title = binding.title;
        peopleCount = binding.peopleCount;
        spots = binding.spots;
        name = binding.name;
        email = binding.email;
        day = binding.day;
        timeSpan = binding.timeSpan;
        address1 = binding.address1;
        address2 = binding.address2;
        hostName = binding.hostName;
        spotBox = binding.spotsBox;
        description = binding.description;
        hostProfile = binding.hostProfile;

        Intent intent = getIntent();
        position = intent.getStringExtra("position");

        ref = FirebaseDatabase.getInstance().getReference().child("Posts");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goingTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFullScreenDialog();
            }
        });

        setContent();

    }

    public void openFullScreenDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.full_screen_dialog);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = dialog.findViewById(R.id.close);
        MaterialCardView plus = dialog.findViewById(R.id.increase);
        MaterialCardView minus = dialog.findViewById(R.id.decrease);
        MaterialButton next = dialog.findViewById(R.id.next);
        final MaterialTextView counter = dialog.findViewById(R.id.counter);


        count = Integer.toString(i);
        counter.setText(count);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                cancelTick.setVisibility(View.GONE);
                goingTick.setCardBackgroundColor(Color.parseColor("#58b2be"));
                box.setStrokeWidth(1);
                textGoing.setTextColor(Color.parseColor("#000000"));

                String add = (String) counter.getText();
                int n = Integer.parseInt(add);
                int a = Integer.parseInt(goingPeople);
                int b = Integer.parseInt(spotsLeft);

                int temp = b;
                if ((n + a) <= b) {
                    a = a + n;
                    peopleCount.setText(Integer.toString(a));

                    b = b - a;
                    spots.setText(Integer.toString(b));
                    spotBox.setText((b) + " spots left");
                    Intent i = getIntent();
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(i.getStringExtra("position")).child("coming").setValue(Integer.toString(a));
                    dialog.dismiss();
                } else if (b != 0) {
                    Toast.makeText(Details.this, "Please select less spots than " + (temp), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Details.this, "Seats are full", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                count = Integer.toString(i);
                counter.setText(count);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i != 0)
                    i--;
                count = Integer.toString(i);
                counter.setText(count);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void setContent() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Intent i = getIntent();
        String id = i.getStringExtra("position");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                goingPeople = dataSnapshot.child("coming").getValue().toString();
                spotsLeft = dataSnapshot.child("spots").getValue().toString();
                int i = Integer.parseInt(spotsLeft) - Integer.parseInt(goingPeople);
                spotsLeft = Integer.toString(i);
                spotBox.setText(spotsLeft + " spots left");
                title.setText(dataSnapshot.child("title").getValue().toString());
                peopleCount.setText(dataSnapshot.child("coming").getValue().toString());
                spots.setText(spotsLeft);
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                Date dt1 = null;
                try {
                    dt1 = format1.parse(dataSnapshot.child("date").getValue().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat format2 = new SimpleDateFormat("EEEE");
                String finalDay = format2.format(dt1);
                day.setText(finalDay);
                String dateOfEvent = dataSnapshot.child("date").getValue().toString();
                String time = dataSnapshot.child("startTime").getValue().toString();
                time = dateOfEvent+"\n"+time + " - " + dataSnapshot.child("endTime").getValue().toString();
                timeSpan.setText(time);
                address1.setText(dataSnapshot.child("address1").getValue().toString());
                address2.setText(dataSnapshot.child("address2").getValue().toString());
                description.setText(dataSnapshot.child("description").getValue().toString());


                FirebaseDatabase.getInstance().getReference().child("Users").child(dataSnapshot.child("uid").getValue().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        name.setText(dataSnapshot1.child("userName").getValue().toString());
                        email.setText(dataSnapshot1.child("userEmail").getValue().toString());
                        String hostedBy = "Hosted by " + dataSnapshot1.child("userName").getValue().toString();
                        Glide.with(Details.this)
                                .load(dataSnapshot1.child("userProfileUrl").getValue().toString())
                                .error(R.drawable.welcome_back)
                                .into(hostProfile);

                        hostName.setText(hostedBy);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
dialog.show();
    }

}
