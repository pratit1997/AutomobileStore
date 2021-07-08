package com.example.automobilestore.Activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.example.automobilestore.ui.home.HomeFragment;
import com.example.automobilestore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

public class UpdateAd extends AppCompatActivity {
    final int GALLERY_REQUEST_CODE = 105;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload,del;
    ImageView[] image;
    String doc_id;
    Boolean changed = false;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_model,et_address, et_description, et_amount, et_phone_number, et_seaters, et_Car_Classification, et_color, et_power, et_year;
    RadioGroup rbGps, rbAirbags, rbParking, rbFuelType, rbBluetooth, rbair_conditioning, rbTransmission, rbpowerwindow, rb_bssenser, rbsroof, rbaids, rbcondition;
    RadioButton btn_Gps,btn_Airbags, btn_Parking, btn_FuelType, btn_Bluetooth, btn_air_conditioning, btn_Transmission, btn_powerwindow, btn__bssenser, btn_sroof, btn_aids, btn_condition;
    int photos = 0 ,internetPhotos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ad);
        Intent intent = getIntent();
        doc_id = intent.getStringExtra("id");
        et_model = findViewById(R.id.Model_Update);
        et_description=findViewById(R.id.des__Update);
        et_amount=findViewById(R.id.amount_Update);
        et_phone_number=findViewById(R.id.pnum_Update);
        et_address=findViewById(R.id.address);
        et_seaters=findViewById(R.id.seaters);
        et_Car_Classification=findViewById(R.id.car_class);
        et_color=findViewById(R.id.car_color);
        et_power=findViewById(R.id.Power);
        et_year=findViewById(R.id.year);


        upload = findViewById(R.id.uploadImage_Update);
        del=findViewById(R.id.delete);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};

        Button btn_update = findViewById(R.id.Update_ad);
        rbGps=findViewById(R.id.rbGps__Update);
        rbAirbags =findViewById(R.id.rbAirbags_Update);
        rbParking=findViewById(R.id.rbParking_Update);
        rbFuelType=findViewById(R.id.rbFuelType_Update);
        rbBluetooth=findViewById(R.id.rbBluetooth_Update);
        rbair_conditioning=findViewById(R.id.rbair_conditioning_Update);
        rbTransmission=findViewById(R.id.rbTransmission_Update);
        rbpowerwindow=findViewById(R.id.rbpowerwindow_Update);
        rb_bssenser=findViewById(R.id.rb_bssenser_Update);
        rbsroof=findViewById(R.id.rbsroof_Update);
        rbaids=findViewById(R.id.rbaids_Update);
        rbcondition=findViewById(R.id.rbcondition_Update);
        AutoCompleteTextView seaters=findViewById(R.id.et_seaters_Update);
        AutoCompleteTextView Car_Classification=findViewById(R.id.et_Car_Classification_Update);
        AutoCompleteTextView car_color=findViewById(R.id.et_color_Update);
        AutoCompleteTextView car_year=findViewById(R.id.et_year_Update);
        String[] seater = new String[]{"2", "4", "6","8","10"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                seater
        );
        seaters.setAdapter(adapter1);
        String[] Classification = new String[]{"SEDAN", "COUPE", "SPORTS CAR","STATION WAGON","HATCHBACK","CONVERTIBLE","SPORT-UTILITY VEHICLE (SUV)","MINIVAN","PICKUP TRUCK","Others"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                Classification
        );
        Car_Classification.setAdapter(adapter2);

        String[] colors = new String[]{"White", "Black", "Gray","Silver","Red","Blue","Brown","Green","Beige","Orange","Gold","Yellow","Purple","Others"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                colors
        );
        car_color.setAdapter(adapter3);
        String[] year = new String[]{"2000", "2001", "2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016","2017","2018","2019","2020","2021"};

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                year
        );
        car_year.setAdapter(adapter4);

        getdata();
        getImages();
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Delete", "Cancel"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateAd.this);
                builder1.setTitle("Delete Post");
                builder1.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Delete")) {
                            delete();
                            HomeFragment hm=new HomeFragment();
                            hm.RefreshData();
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder1.show();

            }
        });
    }

    private void getdata() {

        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Car").document(doc_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();
                        String Model1=data1.get("Model").toString();
                        String Description1=data1.get("Description").toString();
                        String Amount1=data1.get("Amount").toString();
                        String Address1=data1.get("Address").toString();
                        String PhoneNumber1=data1.get("PhoneNumber").toString();
                        String Seaters1=data1.get("Seaters").toString();
                        String Classification1=data1.get("Classification").toString();
                        String Color1=data1.get("Color").toString();
                        String Power1=data1.get("Power").toString();
                        String Year1=data1.get("Year").toString();
                        String Gps1=data1.get("Gps").toString();
                        String AirBags1=data1.get("AirBags").toString();
                        String Parking_Sensor1=data1.get("Parking_Sensor").toString();
                        String Fuel_type1=data1.get("Fuel_type").toString();
                        String Bluetooth1=data1.get("Bluetooth").toString();
                        String AC1=data1.get("AC").toString();
                        String Transmission1=data1.get("Transmission").toString();
                        String Power_Window1=data1.get("Power_Window").toString();
                        String Blind_Spot_Sensor1 =data1.get("Blind_Spot_Senser").toString();
                        String Sun_Roof1=data1.get("Sun_Roof").toString();
                        String Visual_Aids1=data1.get("Visual_Aids").toString();
                        String Condition1 =data1.get("Conditon").toString();


                        et_model.getEditText().setText(Model1);
                        et_address.getEditText().setText(Address1);
                        et_description.getEditText().setText(Description1);
                        et_amount.getEditText().setText(Amount1);
                        et_phone_number.getEditText().setText(PhoneNumber1);
                        et_seaters.getEditText().setText(Seaters1);
                        et_Car_Classification.getEditText().setText(Classification1);
                        et_color.getEditText().setText(Color1);
                        et_power.getEditText().setText(Power1);
                        et_year.getEditText().setText(Year1);


                        if (Gps1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.Gpsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Gpsno);
                            rb2.setChecked(true);
                        }if (AirBags1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.Airbagsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Airbagsno);
                            rb2.setChecked(true);
                        }if (Parking_Sensor1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.Pyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Pno);
                            rb2.setChecked(true);
                        }
                        if (Fuel_type1.equals("Diesel")) {
                            RadioButton rb1 = findViewById(R.id.diesel);
                            rb1.setChecked(true);
                        }else if(Fuel_type1.equals("Petrol")){
                            RadioButton rb2 = findViewById(R.id.petrol);
                            rb2.setChecked(true);
                        }
                        else {
                            RadioButton rb3 = findViewById(R.id.electric);
                            rb3.setChecked(true);
                        }if (Bluetooth1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.bluetoothyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.bluetoothno);
                            rb2.setChecked(true);
                        }
                        if (AC1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.ayes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.ano);
                            rb2.setChecked(true);
                        }if (Transmission1.equals("Automatic")) {
                            RadioButton rb1 = findViewById(R.id.Tyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Tno);
                            rb2.setChecked(true);
                        }
                        if (Power_Window1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.pwyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.pwno);
                            rb2.setChecked(true);
                        }
                        if (Blind_Spot_Sensor1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.bsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.bsno);
                            rb2.setChecked(true);
                        }
                        if (Sun_Roof1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.sryes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.srno);
                            rb2.setChecked(true);
                        }
                        if (Visual_Aids1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.aidsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.aidsno);
                            rb2.setChecked(true);
                        }
                        if (Condition1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.new_car);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.used_car);
                            rb2.setChecked(true);
                        }




                    }

                }

            }
        });

    }
    private void getImages() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("images/" + doc_id);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            storageReference = FirebaseStorage.getInstance().getReference();
                            String location = item.toString();
                            String image = location.substring(location.length() - 1);
                            System.out.println(image);
                            storageReference = FirebaseStorage.getInstance().getReference();
                            storageReference.child("images/" + doc_id + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    contenturi.add(uri);
                                    internetPhotos = internetPhotos + 1;
                                    photos = 1;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    private void delete() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Apartment").document(doc_id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteImages();
                Log.d("tagvv", "DocumentSnapshot successfully deleted!");
                Toast.makeText(UpdateAd.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tagvv", "Error deleting document", e);
                    }
                });

    }
    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateAd.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {

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
    private void deleteImages() {

        storageReference = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
        for (int i = 0; i < internetPhotos; i++) {
            StorageReference desertRef = storageReference.child("images/" + doc_id + "/" + i);
// Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
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

                            Toast.makeText(UpdateAd.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ClipData clipdata = data.getClipData();
                contenturi.clear();
                deleteImages();
                //  Toast.makeText(this, "" + internetPhotos, Toast.LENGTH_SHORT).show();

                if (clipdata != null) {
                    changed = true;
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 4) {
                        Toast.makeText(this, "please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }

                } else {
                    changed = true;
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }
    }
}