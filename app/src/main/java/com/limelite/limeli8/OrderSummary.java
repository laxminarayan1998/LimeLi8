package com.limelite.limeli8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderSummary extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    TextView productOption, productName, productPrice, deliveryPrice, offerText, onOrderAbovePrice, totalPrice, grandTotalPrice, price, quantityMultiplyPrice, userName, userDeatils;
    HashMap<String, String> hashMap;
    Spinner spin;
    String product_name, product_price, product_url, delivery_price;
    ArrayList<String> list;
    ImageView imageView;
    ArrayAdapter<String> aa;
    LinearLayout offerLayout, orderNow;
    String offerOnOrderAbove = "0";
    String offerPrice = "0", pcs = "0";
    RelativeLayout onOrderAboveLayout;
    Double productPriceDouble;
    Double deliveryPriceDouble = 0.0;
    CustomProgressBar customProgressBar;

    Double changePriceWithQuantity;

    String fromOfferIntentProduct, fromOfferIntentSubProduct, product, subProduct;

    DecimalFormat decimalFormatPaytm = new DecimalFormat("#####.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        customProgressBar = new CustomProgressBar(OrderSummary.this);

        userName = findViewById(R.id.name);
        userDeatils = findViewById(R.id.details);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/" + FirebaseAuth.getInstance().getUid() + "/userDetails");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String name = dataSnapshot1.child("name").getValue(String.class);
                    String details = dataSnapshot1.child("houseNo").getValue(String.class) + "," + dataSnapshot1.child("roadNo").getValue(String.class) + "\n" + dataSnapshot1.child("city").getValue(String.class) + "," + dataSnapshot1.child("state").getValue(String.class) + "\nPin Code- " + dataSnapshot1.child("pinCode").getValue(String.class) + "\n" + dataSnapshot1.child("landmark").getValue(String.class)
                            + "\n" + dataSnapshot1.child("number").getValue(String.class) + "\n" + dataSnapshot1.child("alterNumber").getValue(String.class);

                    userName.setText(name);
                    userDeatils.setText(details);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = findViewById(R.id.homePageToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Summary");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back_button);
        toolbar.setTitleTextAppearance(this, R.style.fontFamily);

        productName = findViewById(R.id.product_name);
        productOption = findViewById(R.id.product_option);
        spin = findViewById(R.id.spinner);
        imageView = findViewById(R.id.image);
        productPrice = findViewById(R.id.product_price);
        deliveryPrice = findViewById(R.id.deliveryPrice);
        offerLayout = findViewById(R.id.offer_layout);
        offerText = findViewById(R.id.offer_text);
        onOrderAboveLayout = findViewById(R.id.on_order_above_layout);
        onOrderAbovePrice = findViewById(R.id.on_order_above_price);
        totalPrice = findViewById(R.id.total_price);
        grandTotalPrice = findViewById(R.id.grand_total_price);
        price = findViewById(R.id.product_price);
        quantityMultiplyPrice = findViewById(R.id.quantity_multiply_price);
        orderNow = findViewById(R.id.order_now);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        list = new ArrayList<>();

        Intent intent = getIntent();
        product_name = intent.getStringExtra("product_name");
        product_price = intent.getStringExtra("product_price");
        product_url = intent.getStringExtra("product_url");
        delivery_price = intent.getStringExtra("delivery_price");
        hashMap = (HashMap<String, String>) intent.getSerializableExtra("hashMap");

        if (intent.getStringExtra("product") == null || intent.getStringExtra("subProduct") == null) {

        } else {
            fromOfferIntentProduct = intent.getStringExtra("product");
            fromOfferIntentSubProduct = intent.getStringExtra("subProduct");
        }


        final DecimalFormat decimalFormat = new DecimalFormat("₹###,###.##");

        productPriceDouble = Double.valueOf(product_price);

        productName.setText(product_name);
        Picasso.get()
                .load(product_url)
                .into(imageView);
        productPrice.setText(decimalFormat.format(productPriceDouble));

        if (fromOfferIntentProduct == null || fromOfferIntentSubProduct == null) {
            product = HorizontalScrollRecyclerAdapter.PRODUCT_NAME;
            subProduct = subProductsRecyclerAdapter.SUB_PRODUCT_NAME;
        } else {
            product = fromOfferIntentProduct;
            subProduct = fromOfferIntentSubProduct;
        }

        deliveryPriceDouble = Double.valueOf(delivery_price);
        deliveryPrice.setText(decimalFormat.format(deliveryPriceDouble));


        DatabaseReference quantityRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/quantity");
        quantityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    list.add(dataSnapshot1.child("number").getValue(String.class));
                }
                spin.setAdapter(aa);


//                DatabaseReference offerPriceRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerPrice");
//                offerPriceRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            offerPrice = dataSnapshot.getValue(String.class);
//                        } else {
//                            offerPrice = String.valueOf(0);
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//                DatabaseReference pcsRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/pieces");
//                pcsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        pcs = dataSnapshot.getValue(String.class);
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//                DatabaseReference offerOnOrderRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerOnAbove");
//                offerOnOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        if (dataSnapshot.exists()) {
//                            offerLayout.setVisibility(View.VISIBLE);
//                            offerOnOrderAbove = dataSnapshot.getValue(String.class);
//
//                            DatabaseReference additionalOfferText = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerText");
//                            additionalOfferText.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    if (dataSnapshot.exists()) {
//                                        offerText.setText(Html.fromHtml(dataSnapshot.getValue(String.class)));
//                                    } else {
//                                        offerText.setText("Get ₹" + offerPrice + " off on order above " + offerOnOrderAbove + "pc(s)");
//                                    }
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//
//                        } else {
//                            offerLayout.setVisibility(View.GONE);
//                            offerOnOrderAbove = String.valueOf(0);
//                        }
//
//                        //customProgressBar.dismissProgressBar();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

            String next = entry.getKey() + ": ";
            SpannableString ss = new SpannableString(next);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#777777")), 0, next.length(), 0);
            builder.append(ss);

            String white = entry.getValue();
            SpannableString whiteSpannable = new SpannableString(white);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, white.length(), 0);
            builder.append(whiteSpannable + "\n");

        }

        productOption.setText(builder);

        aa = new ArrayAdapter<>(this, R.layout.custom_textview_spinner, list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setOnItemSelectedListener(this);


        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("tx_amount", decimalFormat.format(changePriceWithQuantity));
                if (ContextCompat.checkSelfPermission(OrderSummary.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderSummary.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                final String M_id = "cfMlsN67852114360621";
                final String customer_id = FirebaseAuth.getInstance().getUid();
                final String order_id = "LIMELI8" + UUID.randomUUID().toString().substring(0, 5);
                String url = "https://limel8.000webhostapp.com/Paytm/generateChecksum.php";
                final String callBack = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

                RequestQueue requestQueue = Volley.newRequestQueue(OrderSummary.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("CHECKSUMHASH")) {

                                String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                                PaytmPGService paytmPGService = PaytmPGService.getStagingService("");

                                HashMap<String, String> paramMap = new HashMap<String, String>();
                                paramMap.put("MID", M_id);
                                paramMap.put("ORDER_ID", order_id);
                                paramMap.put("CUST_ID", customer_id);
                                paramMap.put("CHANNEL_ID", "WAP");
                                paramMap.put("TXN_AMOUNT", decimalFormatPaytm.format(changePriceWithQuantity));
                                paramMap.put("WEBSITE", "WEBSTAGING");
                                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                                paramMap.put("CALLBACK_URL", callBack);
                                paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                                PaytmOrder order = new PaytmOrder(paramMap);

                                paytmPGService.initialize(order, null);
                                paytmPGService.startPaymentTransaction(OrderSummary.this, true, true, new PaytmPaymentTransactionCallback() {
                                    @Override
                                    public void onTransactionResponse(Bundle inResponse) {

                                        if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {
                                            successAlertDialog("ORDER ID : " + order_id);
                                        }

                                        Log.i("Bundle", inResponse.toString());

                                    }

                                    @Override
                                    public void networkNotAvailable() {
                                        Toast.makeText(getApplicationContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void clientAuthenticationFailed(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void someUIErrorOccurred(String inErrorMessage) {
                                        Toast.makeText(getApplicationContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                        Toast.makeText(getApplicationContext(), "Unable to load webpage " + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onBackPressedCancelTransaction() {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                        Toast.makeText(getApplicationContext(), "Transaction cancelled", Toast.LENGTH_LONG).show();
                                        Log.i("inResponse", inResponse.toString());
                                        Log.i("inErrorMessage", inErrorMessage.toString());
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            customProgressBar.dismissProgressBar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> paramMap = new HashMap<String, String>();
                        paramMap.put("MID", M_id);
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", decimalFormatPaytm.format(changePriceWithQuantity));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CALLBACK_URL", callBack);

                        return paramMap;
                    }
                };

                requestQueue.add(stringRequest);

            }
        });


    }

    public void successAlertDialog(String order_id) {

        Button dismissBotton;
        TextView orderId;

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog.setCanceledOnTouchOutside(false);

        dismissBotton = dialogView.findViewById(R.id.dismiss_button);
        orderId = dialogView.findViewById(R.id.order_id);

        orderId.setText(order_id);

        dismissBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        hashMap.clear();
        finish();
        super.onBackPressed();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

        customProgressBar.startProgressBar();


        DatabaseReference offerPriceRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerPrice");
        offerPriceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    offerPrice = dataSnapshot.getValue(String.class);
                } else {
                    offerPrice = String.valueOf(0);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference pcsRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/pieces");
        pcsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pcs = dataSnapshot.getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference offerOnOrderRef = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerOnAbove");
        offerOnOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    offerLayout.setVisibility(View.VISIBLE);
                    offerOnOrderAbove = dataSnapshot.getValue(String.class);

                    DatabaseReference additionalOfferText = FirebaseDatabase.getInstance().getReference("Products/" + product + "/subProducts/" + subProduct + "/offerText");
                    additionalOfferText.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                offerText.setText(Html.fromHtml(dataSnapshot.getValue(String.class)));
                            } else {
                                offerText.setText("Get ₹" + offerPrice + " off on order above " + offerOnOrderAbove + "pc(s)");
                            }

                            DecimalFormat decimalFormat = new DecimalFormat("₹###,###.##");

                            Double selectedQuantity = Double.valueOf(list.get(position));
                            Double orderOnAboveOffer = Double.valueOf(offerOnOrderAbove);
                            Double offerPriceDouble = Double.valueOf(offerPrice);
                            Double pcsDouble = Double.valueOf(pcs);

                            Double showPrice = (productPriceDouble / pcsDouble) * selectedQuantity;

                            if (selectedQuantity >= orderOnAboveOffer) {
                                changePriceWithQuantity = showPrice + deliveryPriceDouble - offerPriceDouble;
                                onOrderAbovePrice.setText("-" + decimalFormat.format(offerPriceDouble));
                                onOrderAboveLayout.setVisibility(View.VISIBLE);
                            } else {
                                changePriceWithQuantity = showPrice + deliveryPriceDouble;
                                onOrderAboveLayout.setVisibility(View.GONE);
                            }

                            Log.i("total", changePriceWithQuantity.toString());
                            totalPrice.setText(decimalFormat.format(changePriceWithQuantity));
                            price.setText(decimalFormat.format(showPrice));
                            grandTotalPrice.setText(decimalFormat.format(changePriceWithQuantity));
                            quantityMultiplyPrice.setText("Price " + "(₹" + product_price + " for " + pcs + " pcs * " + list.get(position) + " quantity)");

                            customProgressBar.dismissProgressBar();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    offerLayout.setVisibility(View.GONE);
                    offerOnOrderAbove = String.valueOf(0);
                }

                //customProgressBar.dismissProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
