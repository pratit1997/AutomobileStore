package com.example.automobilestore.othersClass;
import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.automobilestore.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PopUpClass extends AppCompatActivity{

    //PopupWindow display method

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.addpost, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 900, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler

//        TextView test2 = popupView.findViewById(R.id.titleText);
//        test2.setText(R.string.textTitle);

        Button buttonEdit = popupView.findViewById(R.id.post_ad);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //As an example, display the message
                Toast.makeText(view.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();
               // sendAdData(v);

            }
        });



        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void sendAdData(View v) {
         final int GALLERY_REQUEST_CODE = 105;
        FirebaseFirestore fstore;
        FirebaseAuth auth;
        Context context = null;
        ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload;
        ImageView[] image;
        FirebaseStorage storage;
        StorageReference storageReference;
        ArrayList<Uri> contenturi = new ArrayList<Uri>();
        TextInputLayout edt_model, et_description, et_amount, et_phone_number, et_seaters, et_Car_Classification, et_color, et_power, et_year;
      RadioGroup rbGps, rbAirbags, rbParking, rbFuelType, rbBluetooth, rbair_conditioning, rbTransmission, rbpowerwindow, rb_bssenser, rbsroof, rbaids, rbcondition;
        RadioButton btn_Airbags, btn_Parking, btn_FuelType, btn_Bluetooth, btn_air_conditioning, btn_Transmission, btn_powerwindow, btn__bssenser, btn_sroof, btn_aids, btn_condition;
        int photos = 0;

//        edt_model=v.findViewById(R.id.et_model1);
//        et_description=v.findViewById(R.id.et_description);
//       String s=edt_model.getEditText().getText().toString().trim();
//        Toast.makeText(v.getContext(),"text: "+s,Toast.LENGTH_LONG).show();
    }

}
