package com.jonathan.bankapp.view.fragments;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.jonathan.bankapp.R;
import com.jonathan.bankapp.data.repository.CustomRequest;
import com.jonathan.bankapp.databinding.FragmentRegisterBinding;
import com.jonathan.bankapp.view.Extensions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            if (extensions.isValidEmail(email)) {
                extensions.setUpProgress(getContext()).show();
                petitionPostRegister(names, identification, email, password, state);
            } else {
                Toast.makeText(getContext(), "Correo Electronico invalido", Toast.LENGTH_SHORT).show();
            }
            extensions.setUpProgress(getContext()).dismiss();
        } else {
            extensions.setUpProgress(getContext()).dismiss();
            Toast.makeText(getContext(), "Ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
        }
        extensions.setUpProgress(getContext()).dismiss();
    }

    private void petitionPostRegister(String names, String identification, String email, String password, String state) {

        try {
            String URL = getResources().getString(R.string.url_register_home); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES LA DE LA PC DE WPOSS
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_name", names);
            jsonBody.put("user_identification", identification);
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);
            jsonBody.put("user_estate", state);

            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, jsonBody, headers, response -> {
                try {
                    //extensions.setUpProgress(getContext()).show();
                    if (response.getBoolean("status")) {
                        Toast.makeText(getContext(), "Se registro en el sistema", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.loginFragment);
                       // extensions.setUpProgress(getContext()).dismiss();
                    } else {
                        Toast.makeText(getContext(), "No se pudo registrar en el sistema", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.e("ErrorRequest", "El error fue: "+error);
            });

            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            requestQueue.add(customRequest);
          //  extensions.setUpProgress(getContext()).dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}