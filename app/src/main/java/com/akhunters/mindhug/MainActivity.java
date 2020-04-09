package com.akhunters.mindhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.akhunters.mindhug.databinding.ActivityMainBinding;
import com.akhunters.mindhug.databinding.HeaderBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView close;
    ImageView open;
    LinearLayout menuClickBottom;
    FloatingActionButton floatingActionButton;
    BottomAppBar bottomAppBar;
    FrameLayout frameLayout;

    DatabaseReference mRef;

    MaterialTextView bottomTitle;
    ImageView bottomAppBarLogo;

    CircleImageView headerProfile;
    MaterialTextView name;
    long maxId = 0;
    String strDate;
    MaterialTextView email;
    String category;
    String UID;

    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager manager;
    List<Post> items = new ArrayList<>();
    DatabaseReference reference;

    ImageView topIcon;
    MaterialTextView topText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        menuClickBottom = binding.bottomAppBarContentContainer;
        floatingActionButton = binding.fab;
        bottomAppBar = binding.bottomAppBar;
        //frameLayout = binding.frame;
        bottomTitle = binding.bottomAppBarTitle;
        bottomAppBarLogo = binding.bottomAppBarLogo;

        recyclerView = binding.recycler;
        //topIcon = binding.topIcon;
      //  topText = binding.topText;

        manager = new LinearLayoutManager(this);
        adapter = new Adapter(this, items);
        recyclerView.addItemDecoration(new itemDecoration());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        items = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Posts");

        mRef = FirebaseDatabase.getInstance().getReference().child("Events");

        navigationView = binding.navigationMenu;
        drawerLayout = binding.drawerLayout;
        setTrendingContent();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_trending:
                        drawerLayout.closeDrawers();
                        bottomTitle.setText("Trending");
                        Glide.with(MainActivity.this)
                                .load(R.drawable.trend_logo)
                                .into(bottomAppBarLogo);
                     /*   Glide.with(MainActivity.this)
                                .load(R.drawable.trend_logo)
                                .into(topIcon);

                        topText.setText("Trending");
*/
                        //setTrendingContent();
                       /* LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.activity_trending, null);
                        frameLayout.addView(linearLayout);*/
                        break;
                    case R.id.nav_therapies:
                        bottomTitle.setText("Therapies");
                        Glide.with(MainActivity.this)
                                .load(R.drawable.ic_supervisor_account_black_24dp)
                                .into(bottomAppBarLogo);
                       /* topText.setText("Therapies");
                        Glide.with(MainActivity.this)
                                .load(R.drawable.ic_supervisor_account_black_24dp)
                                .into(topIcon);*/
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_events:
                        bottomTitle.setText("Events");
                       // topText.setText("Events");
                        Glide.with(MainActivity.this)
                                .load(R.drawable.ic_date_range_black_24dp)
                                .into(bottomAppBarLogo);
                       /* Glide.with(MainActivity.this)
                                .load(R.drawable.ic_date_range_black_24dp)
                                .into(topIcon);*/
                        drawerLayout.closeDrawers();

                        break;
                    case R.id.nav_contact:

                        contactUs();
                      /*  bottomTitle.setText("Contact");
                        Glide.with(MainActivity.this)
                                .load(R.drawable.ic_person_black_24dp)
                                .into(bottomAppBarLogo);*/
                        drawerLayout.closeDrawers();

                        break;
                    case R.id.nav_about_us:
                        aboutUs();
                       /* Glide.with(MainActivity.this)
                                .load(R.drawable.ic_info_outline_black_24dp)
                                .into(bottomAppBarLogo);*/


                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        break;
                }

                return false;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        close = headerView.findViewById(R.id.closeDrawer);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });
        open = binding.bottomAppBarChevron;
        menuClickBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }


    public void openDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.post_add_dialog, null);

        final TextInputLayout title = dialogView.findViewById(R.id.titleLayout);
        final TextInputLayout location = dialogView.findViewById(R.id.locationLayout);
        final TextInputLayout date = dialogView.findViewById(R.id.dateLayout);
        final TextInputLayout time = dialogView.findViewById(R.id.timeLayout);
        final TextInputLayout dropDownLayout = dialogView.findViewById(R.id.dropdownLayout);
        final CircularProgressButton add = dialogView.findViewById(R.id.submit);
        MaterialButton cancel = dialogView.findViewById(R.id.cancel);

        TextInputEditText dateEdit = dialogView.findViewById(R.id.dateEditText);
        final TextInputEditText timeEdit = dialogView.findViewById(R.id.timeEditText);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final AutoCompleteTextView chooser = dialogView.findViewById(R.id.choose);

        String[] list = new String[]{"Music", "Art", "Yoga", "Devotional"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        chooser.setAdapter(adapter);

        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String time;
                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            selectedHour -= 12;
                        }
                        time = selectedHour + ":" + selectedMinute + " " + AM_PM;
                        timeEdit.setText(time);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        strDate = day + "/" + month + "/" + year;
                        date.getEditText().setText(strDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        dialogBuilder.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add.startAnimation();

                final String strTitle = title.getEditText().getText().toString().trim();
                final String strLocation = location.getEditText().getText().toString().trim();
                category = dropDownLayout.getEditText().getText().toString().trim();

                if (strTitle.isEmpty()) {
                    title.setError("Field cannot be empty");
                    title.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                } else {
                    title.setError(null);
                }
                if (strLocation.isEmpty()) {
                    location.setError("Field cannot be empty");
                    location.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                } else {
                    location.setError(null);
                }

                if (date.getEditText().getText().toString().isEmpty()) {
                    date.setError("Field cannot be empty");
                    date.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                } else {
                    date.setError(null);
                }

                if (time.getEditText().getText().toString().isEmpty()) {
                    time.setError("Field cannot be empty");
                    time.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                } else {
                    time.setError(null);
                }

                if (dropDownLayout.getEditText().getText().toString().isEmpty()) {
                    dropDownLayout.setError("Field cannot be empty");
                    dropDownLayout.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                    return;
                } else {
                    dropDownLayout.setError(null);
                }

                final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                UID = user.getUid();


                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            maxId = dataSnapshot.getChildrenCount();
                        }

                        final String[] url = new String[1];
                        final String[] newStr = new String[1];
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                url[0] = dataSnapshot.child("userProfileUrl").getValue().toString();
                                newStr[0] = url[0];

                                final Post item = new Post();
                                item.category = category;
                                item.profileUrl = url[0];
                                String time = timeEdit.getText().toString().trim();
                                item.date = strDate + " " + time;
                                item.location = strLocation;
                                item.title = strTitle;


                                String max = Long.toString(maxId + 1);

                                postRef.child(max).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            /*Snackbar.make(findViewById(R.id.coordinatorLayout), "Post Added", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();*/
                                            Toast.makeText(MainActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
                                            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp);
                                            add.doneLoadingAnimation(ContextCompat.getColor(MainActivity.this, android.R.color.black), icon);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    add.revertAnimation();
                                                    add.setBackgroundResource(R.drawable.welcome_signup_background);
                                                    dialogBuilder.dismiss();
                                                }
                                            }, 1000);

                                            setTrendingContent();
                                        } else {
                                            Snackbar.make(findViewById(R.id.coordinatorLayout), task.getException().getMessage(), Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            add.revertAnimation();
                                            add.setBackgroundResource(R.drawable.welcome_signup_background);
                                            return;
                                        }

                                    }
                                });
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


            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }


    public void setTrendingContent() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


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
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public  void contactUs(){


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.contact_us);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView close = dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void aboutUs(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_us);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView close = dialog.findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
