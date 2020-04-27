package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity {

    private List<SlideModel> slideItems;
    private ViewPager slidePager;
    private TabLayout indicator;
    private List<ProductModel> printSectionItems = new ArrayList<>();
    private List<ProductModel> merchandisePrintSectionItems = new ArrayList<>();
    private List<ProductModel> tvcSectionItems = new ArrayList<>();
    private List<ProductModel> eventSectionItems = new ArrayList<>();
    private List<ProductModel> digitalMarketingItems = new ArrayList<>();
    private List<ProductModel> websiteApplicationItems = new ArrayList<>();
    private List<ProductModel> otherItems = new ArrayList<>();
    RecyclerView printRecyclerView, merchandisePrintRecyclerView, tvcRecyclerView, eventRecyclerView, digitalMarketingRecyclerView, websiteApplicationRecyclerView, otherRecyclerView;
    ImageView noticeImage;
    DatabaseReference databaseReference;
    CustomProgressBar customProgressBar;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        customProgressBar = new CustomProgressBar(this);
        customProgressBar.startProgressBar();
        customProgressBar.dialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_account_circle_black_24dp);
        getSupportActionBar().setTitle("LimeLite");

        slidePager = findViewById(R.id.view_pager);
        indicator = findViewById(R.id.indicator);
        printRecyclerView = findViewById(R.id.print_recycler_view);
        merchandisePrintRecyclerView = findViewById(R.id.merchandise_recycler_view);
        tvcRecyclerView = findViewById(R.id.tvc_recycler_view);
        eventRecyclerView = findViewById(R.id.event_recycler_view);
        digitalMarketingRecyclerView = findViewById(R.id.digital_recycler_view);
        websiteApplicationRecyclerView = findViewById(R.id.website_recycler_view);
        otherRecyclerView = findViewById(R.id.other_recycler_view);
        noticeImage = findViewById(R.id.notice_image);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i("getInstanceId failed", String.valueOf(task.getException()));
                            return;
                        }
                        String token = task.getResult().getToken();

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/");
                        userRef.child(user.getUid() + "/deviceId").setValue(token);
                    }
                });


        DatabaseReference noticeRef = FirebaseDatabase.getInstance().getReference("data/notice");
        noticeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noticeImage.setVisibility(View.VISIBLE);
                    Picasso
                            .get()
                            .load(dataSnapshot.getValue(String.class))
                            .into(noticeImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    customProgressBar.dismissProgressBar();
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                } else {
                    noticeImage.setVisibility(View.GONE);
                    customProgressBar.dismissProgressBar();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        slideItems = new ArrayList<>();


        DatabaseReference adsRef = FirebaseDatabase.getInstance().getReference("data/ads/");
        adsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                slideItems.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    SlideModel slideModel = dataSnapshot1.getValue(SlideModel.class);

                    slideItems.add(slideModel);
                }
                SlidePageAdapter slidePageAdapter = new SlidePageAdapter(HomePage.this, slideItems);
                slidePager.setAdapter(slidePageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        slidePager.setPadding(80, 0, 80, 0);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new HomePage.SliderTimer(), 5000, 5000);
        indicator.setupWithViewPager(slidePager, true);


        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        Query visitingCardQuery = databaseReference.orderByChild("category").equalTo("Print");
        visitingCardQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                printSectionItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    printSectionItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, printSectionItems);
                printRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                printRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query merchandisePrintQuery = databaseReference.orderByChild("category").equalTo("Merchandise Print");
        merchandisePrintQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                merchandisePrintSectionItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    merchandisePrintSectionItems.add(productModel);
                }


                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, merchandisePrintSectionItems);
                merchandisePrintRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                merchandisePrintRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query tvcSectionQuery = databaseReference.orderByChild("category").equalTo("TVC");
        tvcSectionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                tvcSectionItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    tvcSectionItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, tvcSectionItems);
                tvcRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                tvcRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query eventQuery = databaseReference.orderByChild("category").equalTo("Events and Wedding");
        eventQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                eventSectionItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    eventSectionItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, eventSectionItems);
                eventRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                eventRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query digitalMarketingQuery = databaseReference.orderByChild("category").equalTo("Digital Marketing");
        digitalMarketingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                digitalMarketingItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    digitalMarketingItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, digitalMarketingItems);
                digitalMarketingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                digitalMarketingRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query websiteApplicationQuery = databaseReference.orderByChild("category").equalTo("Website and Application");
        websiteApplicationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                websiteApplicationItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    websiteApplicationItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, websiteApplicationItems);
                websiteApplicationRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                websiteApplicationRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query otherQuery = databaseReference.orderByChild("category").equalTo("Others");
        otherQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                otherItems.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ProductModel productModel = postSnapshot.getValue(ProductModel.class);
                    otherItems.add(productModel);
                }

                HorizontalScrollRecyclerAdapter horizontalScrollRecyclerAdapter = new HorizontalScrollRecyclerAdapter(HomePage.this, otherItems);
                otherRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

                otherRecyclerView.setAdapter(horizontalScrollRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    class SliderTimer extends TimerTask {

        @Override
        public void run() {

            HomePage.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (slidePager.getCurrentItem() < slideItems.size() - 1) {
                        slidePager.setCurrentItem(slidePager.getCurrentItem() + 1);
                    } else
                        slidePager.setCurrentItem(0);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_booking:
                //startActivity(new Intent(getApplicationContext(), MyBooking.class));
                return true;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivity(new Intent(getApplicationContext(), LogIn.class));
                    finish();
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
