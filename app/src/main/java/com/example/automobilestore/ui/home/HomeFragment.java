package com.example.automobilestore.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.automobilestore.Activity.CreateAccount;
import com.example.automobilestore.Activity.PostAd;
import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;
import com.example.automobilestore.databinding.FragmentHomeBinding;
import com.example.automobilestore.othersClass.PopUpClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    FloatingActionButton add_btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        add_btn=v.findViewById(R.id.add_post_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopUpClass popUpClass = new PopUpClass();
//                popUpClass.showPopupWindow(v);
                Intent i = new Intent(getActivity().getApplicationContext(), PostAd.class);
                startActivity(i);

            }
        });

        return v;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


//    private void AddPost() {
//        Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
//    }



}