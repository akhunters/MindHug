package com.akhunters.mindhug;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.akhunters.mindhug.databinding.ActivityDetailsBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Details extends AppCompatActivity {

    ActivityDetailsBinding binding;

    ImageView back;
    MaterialCardView goingTick;
    MaterialCardView box;
    MaterialCardView cancelTick;
    MaterialTextView textGoing;

    DatabaseReference ref ;

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

        ref = FirebaseDatabase.getInstance().getReference().child("")

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
                dialog.dismiss();
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

                if(i!=0)
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

}
