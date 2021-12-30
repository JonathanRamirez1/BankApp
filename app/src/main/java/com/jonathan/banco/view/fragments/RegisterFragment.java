package com.jonathan.banco.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.jonathan.banco.R;
import com.jonathan.banco.databinding.FragmentRegisterBinding;
import com.jonathan.banco.view.Extensions;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private NavController navController;

    private final Extensions extensions = new Extensions();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        launchFragmentLogin(view);
    }

    private void launchFragmentLogin(View view) {
        MaterialButton buttonRegister = binding.materialButton;
        navController = Navigation.findNavController(view);
        buttonRegister.setOnClickListener(view2 -> validateFields());
    }

    private void validateFields() {
        String names = binding.textInputEditTextEmail.getText().toString();
        String identification = binding.textInputEditTextIdentification.getText().toString();
        String email = binding.textInputEditTextEmail.getText().toString();
        String password = binding.textInputEditTextPassword.getText().toString();
        String state = "ACTIVO";

        if (!names.isEmpty() || !identification.isEmpty() || !email.isEmpty() || !password.isEmpty()) {
            if(extensions.isValidEmail(email)) {
                petitionPostRegister(names, identification, email, password, state);

            } else {
                Toast.makeText(getContext(), "Correo Electronico invalido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    private void petitionPostRegister(String names, String identification, String email, String password, String state) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String URL = getResources().getString(R.string.url_register); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES LA DE LA PC DE WPOSS
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_name", names);
            jsonBody.put("user_identification", identification);
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);
            jsonBody.put("user_estate", state);

            JsonObjectRequest petition = new JsonObjectRequest(Request.Method.POST, URL ,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if(jsonObject.getBoolean("status")) {
                                    Toast.makeText(getContext(), "Se registro en el sistema", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.loginFragment);
                                }else{
                                    Toast.makeText(getContext(), "No se pudo registrar en el sistema", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    } );

            requestQueue.add(petition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}