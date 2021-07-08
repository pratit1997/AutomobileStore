package com.example.automobilestore.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.R;
import com.example.automobilestore.adapter.Horizontal_Car_Adapter;
import com.example.automobilestore.adapter.Vertical_Car_Adapter;
import com.example.automobilestore.databinding.FragmentHomeBinding;
import com.example.automobilestore.model.HorizontalCarData;
import com.example.automobilestore.model.VerticalCarData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    FloatingActionButton add_btn;
    RecyclerView HorizontalRecycler, VerticalRecycler;
    Vertical_Car_Adapter VerticalAdapter;
    StorageReference storageReference;
    private FirebaseAuth auth;
    FirebaseFirestore db;
    private FirebaseUser curUser;
    List<HorizontalCarData> HorizontalList = new ArrayList<>();
    List<VerticalCarData> VerticalList = new ArrayList<>();
    Horizontal_Car_Adapter HorizontalAdapter;
    boolean priceChanged = false;
    boolean passengerChanged = false;
    ImageView filter;
    float min = 0, max = 100000;
    float minp = 1, maxp = 8;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
        HorizontalRecycler = v.findViewById(R.id.rv_hcar);
        VerticalRecycler = v.findViewById(R.id.rv_vcar);

        RefreshData();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopUpClass popUpClass = new PopUpClass();
//                popUpClass.showPopupWindow(v);
                Intent i = new Intent(getActivity().getApplicationContext(), PostAd.class);
                startActivity(i);
                RefreshData();

            }
        });
        filter = v.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter(getActivity());
            }
        });


        return v;
    }

    public void RefreshData() {

//
//        HorizontalList.add(new HorizontalCarData("Honda", "$19000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Hyundai", "$17000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Toyota", "$25000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Ford", "$7000", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Mazda", "$17899", R.drawable.logo));
//        HorizontalList.add(new HorizontalCarData("Suzuki", "$5670", R.drawable.logo));
//        setHorizontal(HorizontalList);
//
//        List<VerticalCarData> VerticalList = new ArrayList<>();
//        VerticalList.add(new VerticalCarData("Honda", "$19000", R.drawable.logo,  "OLD"));
//        VerticalList.add(new VerticalCarData("Hyundai", "$17000", R.drawable.logo,  "OLD"));
//        VerticalList.add(new VerticalCarData("Toyota", "$25000", R.drawable.logo,  "NEW"));
//        VerticalList.add(new VerticalCarData("Ford", "$7000", R.drawable.logo, "NEW"));
//        VerticalList.add(new VerticalCarData("Mazda", "$17899", R.drawable.logo, "OLD"));
//        VerticalList.add(new VerticalCarData("Suzuki", "$5670", R.drawable.logo, "NEW"));
//
//        setVertical(VerticalList);

        HorizontalList.clear();
        VerticalList.clear();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        db.collection("Car")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                String Model = (String) document.getData().get("Model");
                                String Amount = (String) document.getData().get("Amount");
                                String Conditon=(String) document.getData().get("Conditon");
                                String UserID=document.getId();
                                getImage(UserID,Model,Amount,Conditon);
                                setHorizontal();
                                setVertical();


                                Log.d("homeeeeeee", "modellll" + Model);
                                Log.d("homeeeee", "amounttttt" +   Amount);


                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

//
//
    }
    private void setHorizontal() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        HorizontalRecycler.setLayoutManager(layoutManager);
        HorizontalAdapter = new Horizontal_Car_Adapter(getActivity(), HorizontalList);
        HorizontalRecycler.setAdapter(HorizontalAdapter);
    }
    private void setVertical() {


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        VerticalRecycler.setLayoutManager(layoutManager);
        VerticalAdapter = new Vertical_Car_Adapter(getActivity(), VerticalList);
        VerticalRecycler.setAdapter(VerticalAdapter);

    }
    private void getImage(final String UserID,final String Model,final String Amount,final String Conditon) {
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + UserID + "/0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("TAG", "image got"+Conditon);
                if(Conditon.equals("yes")||Conditon.equals("Yes")){
                    HorizontalList.add(new HorizontalCarData(UserID,Model, Amount, uri));
                    HorizontalAdapter.notifyDataSetChanged();
                }
                else{
                    if (Conditon=="yes"||Conditon=="Yes"){
                        VerticalList.add(new VerticalCarData(UserID,Model, Amount,uri,"NEW"));
                    }else{
                        VerticalList.add(new VerticalCarData(UserID,Model, Amount,uri,"OlD"));
                    }

                    setVertical();
                    VerticalAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.d("TAG", "image not got");
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    //    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }
    public void filter(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_filter);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final RangeSlider price_slider = dialog.findViewById(R.id.price_slider);
        final RangeSlider Seaters_slider = dialog.findViewById(R.id.Seaters_slider);
        final TextView to = dialog.findViewById(R.id.to);
        final TextView from = dialog.findViewById(R.id.from);
        final TextView toSeaters = dialog.findViewById(R.id.toSeaters);
        final TextView fromSeaters = dialog.findViewById(R.id.fromSeaters);
//   dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//       @Override
//       public void onDismiss(DialogInterface dialog) {
//           RefreshData();
//       }
//   });

        price_slider.setValues(min, max);
        to.setText("Min " + min);
        from.setText("Max " + max);
        dialog.show();


        Seaters_slider.setValues(minp, maxp);
        toSeaters.setText("Min " + minp);
        fromSeaters.setText("Max " + maxp);
        dialog.show();

        Button apply = dialog.findViewById(R.id.apply);
        Button reset = dialog.findViewById(R.id.reset);

        price_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        price_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        Seaters_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);

            }
        });

        Seaters_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List Passenger = slider.getValues();
                minp = (float) Passenger.get(0);
                maxp = (float) Passenger.get(1);
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List price = price_slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
                Log.d("", min + "");
                priceChanged = true;

                List passenger = Seaters_slider.getValues();
                Log.d("", "VALUE SET " + passenger);
                minp = (float) passenger.get(0);
                Log.d("", "VALUE SET " + minp);
                maxp = (float) passenger.get(1);
                Log.d("", "VALUE SET " + maxp);
                toSeaters.setText("Min " + minp);
                Log.d("", "VALUE SET " + toSeaters);
                fromSeaters.setText("Max " + maxp);
                Log.d("", "VALUE SET " + fromSeaters);
                passengerChanged = true;
                dialog.dismiss();
                FilterData();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = 0;
                max = 100000;
                to.setText("Min " + min);
                from.setText("Max " + max);
                priceChanged = false;

                minp = 1;
                maxp = 8;
                toSeaters.setText("Min " + minp);
                fromSeaters.setText("Max " + maxp);
                passengerChanged = false;
                dialog.dismiss();
                FilterData();
            }
        });

    }
    public void FilterData() {

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        db.collection("Car")
//                .whereGreaterThan("Amount",min).whereLessThan("Amount",max)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Filterrrrrr Data", "filter calll") ;
                                Log.d("", document.getId() + " => " + document.getData());
                                System.out.println(document.getId() + " => " + document.getData());
                                Log.d("Filterrrrrr Data", "filter calll2") ;
                                String Model = (String) document.getData().get("Model");
                                Float Amount = Float.parseFloat(String.valueOf(document.getData().get("Amount")));
                                String Conditon=(String) document.getData().get("Conditon");
                                Log.d("Filterrrrrr Data", "filter calll3") ;
                                int Seaters=Integer.parseInt(String.valueOf(document.getData().get("Seaters")));
                                Log.d("Filterrrrrr Data", "filter calll4") ;
                                String UserID=document.getId();


                                Log.d("Filterrrrrr Data", "filter modellll" + Model);
                                Log.d("Filterrrrrr Data", "filter amounttttt" +   Amount);
                                if (priceChanged && passengerChanged) {
                                    if (Amount >= min && Amount <= max && Seaters >= minp && Seaters <= maxp) {
                                        HorizontalList.clear();
                                        VerticalList.clear();
                                        getImage(UserID, Model, Amount.toString(), Conditon);
                                        setHorizontal();
                                        setVertical();
                                    }
                                    else {
                                        HorizontalList.clear();
                                        VerticalList.clear();
                                        setHorizontal();
                                        setVertical();
//                                        Toast.makeText(getActivity().getApplicationContext(),"Oops No Cars Found",Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity().getApplicationContext(), "else call", Toast.LENGTH_SHORT).show();
                                }


                            }

                        } else {
                            Log.d("", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


}