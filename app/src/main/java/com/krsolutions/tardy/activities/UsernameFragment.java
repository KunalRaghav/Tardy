package com.krsolutions.tardy.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.krsolutions.tardy.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class UsernameFragment extends Fragment {
    FragmentTransaction ft;


    View view = null;
    EditText etUsername;
    MaterialButton saveButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_name,container,false);
        return view;
    }

    public void passFt(FragmentTransaction fragmentTransaction){
        ft=fragmentTransaction;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        etUsername = view.findViewById(R.id.etUsername);
        saveButton = view.findViewById(R.id.saveButton);

        final SharedPreferences prefs = getActivity().getSharedPreferences("com.krsolutions.tardy",MODE_PRIVATE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                if(username.isEmpty()){
                    Snackbar snack = Snackbar.make(v,"Username can't be empty",Snackbar.LENGTH_LONG);
                    snack.show();
                }else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putString("pref_user",username).commit();

                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    ActivityOptions options =
                            ActivityOptions.makeClipRevealAnimation(view,(int)view.getX(),(int)view.getY(),view.getWidth(),100);
                    getActivity().startActivity(intent, options.toBundle());
                    getActivity().finish();
                }
            }
        });

    }
}
