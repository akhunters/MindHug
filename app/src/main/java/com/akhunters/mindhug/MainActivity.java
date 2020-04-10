package com.akhunters.mindhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.annotation.GlideModule;
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
import android.text.Layout;
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
CircleImageView bottomProfile;
    DatabaseReference mRef;

    MaterialTextView bottomTitle;
    ImageView bottomAppBarLogo;

    CircleImageView headerProfile;
    MaterialTextView name;
    long maxId = 0;
    String strDate;
    MaterialTextView email;
    String profileUrl = null;
    String category;
    String UID;

    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager manager;
    List<PostRecyclerView> items = new ArrayList<>();
    DatabaseReference reference;

    ImageView topIcon;
    MaterialTextView topText;

    FirebaseAuth mAuth;


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
        bottomProfile = binding.profile;




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
                        startActivity(new Intent(getApplicationContext(),Welcome.class));
                        finish();
                        break;
                }

                return false;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.nameHeader);
        email = headerView.findViewById(R.id.emailHeader);
        close = headerView.findViewById(R.id.closeDrawer);
        headerProfile = headerView.findViewById(R.id.profileImageView);
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


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_add_dialog);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextInputLayout title = dialog.findViewById(R.id.titleLayout);
        final TextInputLayout address1 = dialog.findViewById(R.id.streetLayout);
        final TextInputLayout address2 = dialog.findViewById(R.id.locationLayout);
        final TextInputLayout date = dialog.findViewById(R.id.dateLayout);
        final TextInputLayout spots = dialog.findViewById(R.id.spotsLayout);
        final TextInputLayout startTime = dialog.findViewById(R.id.startTimeLayout);
        final TextInputLayout endTime = dialog.findViewById(R.id.endTimeLayout);
        final TextInputLayout dropDownLayout = dialog.findViewById(R.id.dropdownLayout);
        final TextInputLayout description = dialog.findViewById(R.id.contentLayout);

        final CircularProgressButton add = dialog.findViewById(R.id.submit);
        MaterialButton cancel = dialog.findViewById(R.id.cancel);

        final TextInputEditText dateEdit = dialog.findViewById(R.id.dateEditText);
        final TextInputEditText startTimeEdit = dialog.findViewById(R.id.startTimeEditText);
        final TextInputEditText endTimeEdit = dialog.findViewById(R.id.endTimeEditText);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final AutoCompleteTextView chooser = dialog.findViewById(R.id.choose);

        String[] list = new String[]{"Music", "Art", "Yoga", "Devotional"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
        chooser.setAdapter(adapter);

        startTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String hour = null;
                        String minute = null;
                        String time = null;
                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            selectedHour = selectedHour - 12;
                        }
                        if(selectedHour < 10){
                            hour = "0"+selectedHour;

                        }
                        else {
                            hour = Integer.toString(selectedHour);

                        }
                        if(selectedMinute < 10){
                            minute = "0"+selectedMinute;

                        }
                        else {
                            minute = Integer.toString(selectedMinute);

                        }

                        time = hour + ":" + minute + " " + AM_PM;
                        startTimeEdit.setText(time);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = null;
                        String minute = null;
                        String time = null;
                        String AM_PM;
                        if (selectedHour < 12) {
                            AM_PM = "AM";

                        } else {
                            AM_PM = "PM";
                            selectedHour = selectedHour - 12;
                        }
                        if(selectedHour < 10){
                            hour = "0"+selectedHour;

                        }
                        else {
                            hour = Integer.toString(selectedHour);

                        }
                        if(selectedMinute < 10){
                            minute = "0"+selectedMinute;

                        }
                        else {
                            minute = Integer.toString(selectedMinute);

                        }


                        time = hour + ":" + minute + " " + AM_PM;
                        endTimeEdit.setText(time);
                    }
                }, hour, minute, true);
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
                        String dd = "";
                        String mm = "";
                        if (day < 10)
                            dd = "0" + day;
                        else
                            dd = Integer.toString(day);
                        if (month < 10)
                            mm = "0" + month;
                        else
                            mm = Integer.toString(month);
                        strDate = dd + "/" + mm + "/" + year;
                        date.getEditText().setText(strDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add.startAnimation();

                final String strTitle = title.getEditText().getText().toString().trim();
                final String strAddress1 = address1.getEditText().getText().toString().trim();
                final String strAddress2 = address2.getEditText().getText().toString().trim();
                final String strDate = date.getEditText().getText().toString().trim();
                final String strSpots = spots.getEditText().getText().toString().trim();
                final String strStartTime = startTime.getEditText().getText().toString().trim();
                final String strEndTime = endTimeEdit.getText().toString().trim();
                final String strCategory = dropDownLayout.getEditText().getText().toString().trim();
                final String strDescription = description.getEditText().getText().toString().trim();

                if (strTitle.isEmpty()) {
                    title.setError("Field cannot be Empty");
                    title.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    title.setError(null);
                }


                if (strAddress1.isEmpty()) {
                    address1.setError("Field cannot be Empty");
                    address1.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    address1.setError(null);
                }


                if (strAddress2.isEmpty()) {
                    address2.setError("Field cannot be Empty");
                    address2.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    address2.setError(null);
                }


                if (strDate.isEmpty()) {
                    date.setError("Field cannot be Empty");
                    date.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    date.setError(null);
                }


                if (strSpots.isEmpty()) {
                    spots.setError("Field cannot be Empty");
                    spots.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    spots.setError(null);
                }


                if (strStartTime.isEmpty()) {
                    startTime.setError("Field cannot be Empty");
                    startTime.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    startTime.setError(null);
                }


                if (strEndTime.isEmpty()) {
                    endTime.setError("Field cannot be Empty");
                    endTime.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    endTime.setError(null);
                }


                if (strCategory.isEmpty()) {
                    dropDownLayout.setError("Field cannot be Empty");
                    dropDownLayout.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    dropDownLayout.setError(null);
                }


                if (strDescription.isEmpty()) {
                    description.setError("Field cannot be Empty");
                    description.requestFocus();
                    add.revertAnimation();
                    add.setBackgroundResource(R.drawable.welcome_login_background);

                    return;
                } else {
                    description.setError(null);
                }

                final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                UID = user.getUid();

                final Post item = new Post();

                item.title = strTitle;
                item.address1 = strAddress1;
                item.address2 = strAddress2;
                item.date = strDate;
                item.spots = strSpots;
                item.startTime = strStartTime;
                item.endTime = strEndTime;
                item.category = strCategory;
                item.description = strDescription;
                item.UID = UID;
                item.coming = "0";

                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            maxId = dataSnapshot.getChildrenCount();
                        }

                        String Id = Long.toString(maxId + 1);
                        postRef.child(Id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Post Added", Toast.LENGTH_SHORT).show();

                                    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.black_tick_png);
                                    add.doneLoadingAnimation(ContextCompat.getColor(MainActivity.this, R.color.colorAccent), icon);
                                    setTrendingContent();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                        }
                                    }, 1500);
                                } else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    add.revertAnimation();
                                    add.setBackgroundResource(R.drawable.welcome_login_background);
                                }


                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        dialog.show();
    }

    public void setTrendingContent() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String currentUID = user.getUid();


        FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("userName").getValue().toString());
                email.setText(dataSnapshot.child("userEmail").getValue().toString());
                Glide.with(MainActivity.this)
                        .load(dataSnapshot.child("userProfileUrl").getValue().toString())
                        .error(R.drawable.welcome_back)
                        .into(bottomProfile);

                Glide.with(MainActivity.this)
                        .load(dataSnapshot.child("userProfileUrl").getValue().toString())
                        .error(R.drawable.welcome_back).
                        into(headerProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                if (dataSnapshot.exists())
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final PostRecyclerView postRecyclerView = new PostRecyclerView();

                        postRecyclerView.title = dataSnapshot1.child("title").getValue().toString();
                        postRecyclerView.location = dataSnapshot1.child("address1").getValue().toString();
                        postRecyclerView.date = dataSnapshot1.child("date").getValue().toString();
                        String UID = dataSnapshot1.child("uid").getValue().toString();
                        postRecyclerView.category = dataSnapshot1.child("category").getValue().toString();
                        FirebaseDatabase.getInstance().getReference().child("Users").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                              postRecyclerView.profileUrl = dataSnapshot.child("userProfileUrl").getValue().toString();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        items.add(postRecyclerView);

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

    public void contactUs() {


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

    public void aboutUs() {
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
