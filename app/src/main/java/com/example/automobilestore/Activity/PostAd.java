package com.example.automobilestore.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Horizontal_Car_Adapter;
import com.example.automobilestore.adapter.Vertical_Car_Adapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostAd extends AppCompatActivity {

    final int GALLERY_REQUEST_CODE = 105;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload;
    ImageView[] image;
    FirebaseStorage storage;
    Vertical_Car_Adapter VerticalAdapter;
    Horizontal_Car_Adapter HorizontalAdapter;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_model,et_address, et_description, et_amount, et_phone_number, et_seaters, et_Car_Classification, et_color, et_power, et_year;
    RadioGroup rbGps, rbAirbags, rbParking, rbFuelType, rbBluetooth, rbair_conditioning, rbTransmission, rbpowerwindow, rb_bssenser, rbsroof, rbaids, rbcondition;
    RadioButton btn_Gps,btn_Airbags, btn_Parking, btn_FuelType, btn_Bluetooth, btn_air_conditioning, btn_Transmission, btn_powerwindow, btn__bssenser, btn_sroof, btn_aids, btn_condition;
    int photos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ad);


        et_model = findViewById(R.id.Model);
        et_description=findViewById(R.id.des);
        et_amount=findViewById(R.id.amount);
        et_phone_number=findViewById(R.id.pnum);
        et_address=findViewById(R.id.address);
        et_seaters=findViewById(R.id.seaters);
        et_Car_Classification=findViewById(R.id.car_class);
        et_color=findViewById(R.id.car_color);
        et_power=findViewById(R.id.Power);
        et_year=findViewById(R.id.year);

        upload = findViewById(R.id.uploadImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};

        Button btn_postad = findViewById(R.id.post_ad);
        rbGps=findViewById(R.id.rbGps);
        rbAirbags =findViewById(R.id.rbAirbags);
        rbParking=findViewById(R.id.rbParking);
        rbFuelType=findViewById(R.id.rbFuelType);
        rbBluetooth=findViewById(R.id.rbBluetooth);
        rbair_conditioning=findViewById(R.id.rbair_conditioning);
        rbTransmission=findViewById(R.id.rbTransmission);
        rbpowerwindow=findViewById(R.id.rbpowerwindow);
        rb_bssenser=findViewById(R.id.rb_bssenser);
        rbsroof=findViewById(R.id.rbsroof);
        rbaids=findViewById(R.id.rbaids);
        rbcondition=findViewById(R.id.rbcondition);
        AutoCompleteTextView seaters=findViewById(R.id.et_seaters);
        AutoCompleteTextView Car_Classification=findViewById(R.id.et_Car_Classification);
        AutoCompleteTextView car_color=findViewById(R.id.et_color);
        AutoCompleteTextView car_year=findViewById(R.id.et_year);
        String[] seater = new String[]{"2", "4", "6","8","10"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                PostAd.this,
                R.layout.dropdown_item,
                seater
        );
        seaters.setAdapter(adapter1);
        String[] Classification = new String[]{"SEDAN", "COUPE", "SPORTS CAR","STATION WAGON","HATCHBACK","CONVERTIBLE","SPORT-UTILITY VEHICLE (SUV)","MINIVAN","PICKUP TRUCK","Others"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                PostAd.this,
                R.layout.dropdown_item,
                Classification
        );
        Car_Classification.setAdapter(adapter2);

        String[] colors = new String[]{"White", "Black", "Gray","Silver","Red","Blue","Brown","Green","Beige","Orange","Gold","Yellow","Purple","Others"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                PostAd.this,
                R.layout.dropdown_item,
                colors
        );
        car_color.setAdapter(adapter3);
        String[] year = new String[]{"2000", "2001", "2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021"};

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(
                PostAd.this,
                R.layout.dropdown_item,
                year
        );
        car_year.setAdapter(adapter4);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btn_postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fstore = FirebaseFirestore.getInstance();
                final String Model = et_model.getEditText().getText().toString().trim();
                final String Description = et_description.getEditText().getText().toString().trim();
                final String Address = et_address.getEditText().getText().toString().trim();
                final String Amount = et_amount.getEditText().getText().toString().trim();
                String PhoneNumber = et_phone_number.getEditText().getText().toString().trim();
                String Seaters = et_seaters.getEditText().getText().toString().trim();
                String Classification = et_Car_Classification.getEditText().getText().toString().trim();
                String Color = et_color.getEditText().getText().toString().trim();
                String Power = et_power.getEditText().getText().toString().trim();
                String Year = et_year.getEditText().getText().toString().trim();
                if (Model.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Enter Model", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (Description.length() > 10000) {
                    Toast.makeText(PostAd.this, "Title should be 10000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please enter Amount ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(PostAd.this, "Please Enter Amount in Digit", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (Address == null) {
                    Toast.makeText(PostAd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please enter PhoneNumber ", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(PostAd.this, "Please Enter PhoneNumber in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.length() < 10 || PhoneNumber.length() > 10) {
                    Toast.makeText(PostAd.this, "Please enter 10 to 12 digit PhoneNumber", Toast.LENGTH_LONG).show();
                    return;
                }else if (Seaters.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Select Seaters from DropDown", Toast.LENGTH_LONG).show();
                    return;
                }else if (Classification.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Select Car Type from DropDown", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (Color.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Select Car Color from DropDown", Toast.LENGTH_LONG).show();
                    return;
                }else if (Power.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please enter the Engine Power ", Toast.LENGTH_LONG).show();
                    return;
                }else if (Power.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(PostAd.this, "Please Enter Power in Digit", Toast.LENGTH_LONG).show();
                    return;
                }
                else if (Year.isEmpty()) {
                    Toast.makeText(PostAd.this, "Please Select Car year from DropDown ", Toast.LENGTH_LONG).show();
                    return;
                }else if (photos < 1) {
                    Toast.makeText(PostAd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                }else{
                    final ProgressDialog pd;
                    pd = new ProgressDialog(PostAd.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    int selectedId1 = rbGps.getCheckedRadioButtonId();
                    btn_Gps = findViewById(selectedId1);
                    String Gps = btn_Gps.getText().toString().trim();
                    Log.v("tagvv", " " +Gps);

                    int selectedId2 = rbAirbags.getCheckedRadioButtonId();
                    btn_Airbags = findViewById(selectedId2);
                    String AirBags = btn_Airbags.getText().toString().trim();
                    Log.v("tagvv", " " +AirBags);

                    int selectedId3 = rbParking.getCheckedRadioButtonId();
                    btn_Parking = findViewById(selectedId3);
                    String Parking_Sensor = btn_Parking.getText().toString().trim();
                    Log.v("tagvv", " " +Parking_Sensor);

                    int selectedId4 = rbFuelType.getCheckedRadioButtonId();
                    btn_FuelType = findViewById(selectedId4);
                    String Fuel_type = btn_FuelType.getText().toString().trim();
                    Log.v("tagvv", " " +Fuel_type);

                    int selectedId5 = rbBluetooth.getCheckedRadioButtonId();
                    btn_Bluetooth = findViewById(selectedId5);
                    String Bluetooth = btn_Bluetooth.getText().toString().trim();
                    Log.v("tagvv", " " +Bluetooth);

                    int selectedId6 = rbair_conditioning.getCheckedRadioButtonId();
                    btn_air_conditioning = findViewById(selectedId6);
                    String AC = btn_air_conditioning.getText().toString().trim();
                    Log.v("tagvv", " " +AC);

                    int selectedId7 = rbTransmission.getCheckedRadioButtonId();
                    btn_Transmission = findViewById(selectedId7);
                    String Transmission= btn_Transmission.getText().toString().trim();
                    Log.v("tagvv", " " +Transmission);

                    int selectedId8 = rbpowerwindow.getCheckedRadioButtonId();
                    btn_powerwindow = findViewById(selectedId8);
                    String Power_Window= btn_powerwindow.getText().toString().trim();
                    Log.v("tagvv", " " +Power_Window);

                    int selectedId9 = rb_bssenser.getCheckedRadioButtonId();
                    btn__bssenser = findViewById(selectedId9);
                    String Blind_Spot_Senser = btn__bssenser.getText().toString().trim();
                    Log.v("tagvv", " " +Blind_Spot_Senser);

                    int selectedId10 = rbsroof.getCheckedRadioButtonId();
                    btn_sroof = findViewById(selectedId10);
                    String Sun_Roof = btn_sroof.getText().toString().trim();
                    Log.v("tagvv", " " +Sun_Roof);

                    int selectedId11 = rbaids.getCheckedRadioButtonId();
                    btn_aids = findViewById(selectedId11);
                    String Visual_Aids = btn_aids.getText().toString().trim();
                    Log.v("tagvv", " " +Visual_Aids);

                    int selectedId12 = rbcondition.getCheckedRadioButtonId();
                    btn_condition = findViewById(selectedId12);
                    String Conditon = btn_condition.getText().toString().trim();
                    Log.v("tagvv", " " +Conditon);


                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.v("tagvv", " " + uid);


                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("UserID", uid);
                    userMap.put("Model", Model);
                    userMap.put("Description", Description);
                    userMap.put("Amount", Amount);
                    userMap.put("Address", Address);
                    userMap.put("PhoneNumber", PhoneNumber);
                    userMap.put("Seaters", Seaters);
                    userMap.put("Classification", Classification);
                    userMap.put("Color", Color);
                    userMap.put("Power", Power);
                    userMap.put("Year", Year);
                    userMap.put("Gps", Gps);
                    userMap.put("AirBags", AirBags);
                    userMap.put("Parking_Sensor", Parking_Sensor);
                    userMap.put("Fuel_type", Fuel_type);
                    userMap.put("Bluetooth", Bluetooth);
                    userMap.put("AC", AC);
                    userMap.put("Transmission", Transmission);
                    userMap.put("Power_Window", Power_Window);
                    userMap.put("Blind_Spot_Senser", Blind_Spot_Senser);
                    userMap.put("Sun_Roof", Sun_Roof);
                    userMap.put("Visual_Aids", Visual_Aids);
                    userMap.put("Conditon", Conditon);

                    fstore.collection("Car").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Demoooooooo1o1", "onSuccess: "+documentReference.getId());
                            uploadImage((String) documentReference.getId());
                            Toast.makeText(PostAd.this, " Post added Successfully ", Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                            finish();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String Error = e.getMessage();
                            Toast.makeText(PostAd.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });

//        Toast.makeText(this,"text: ",Toast.LENGTH_LONG).show();
    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostAd.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    contenturi.clear();
                    upload.setImageResource(R.drawable.uploadnew);
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, ""), GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ClipData clipdata = data.getClipData();

                if (clipdata != null) {
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 4) {
                        Toast.makeText(this, "Please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }
                } else {
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }
    }

    private void uploadImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int j = 0; j < contenturi.size(); j++) {

            StorageReference ref = storageReference.child("images").child(id + "/" + j);
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(PostAd.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }
}