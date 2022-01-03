package com.jonathan.bankapp.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jonathan.bankapp.R;
import com.jonathan.bankapp.data.repository.CustomRequest;
import com.jonathan.bankapp.databinding.FragmentLoginBinding;
import com.jonathan.bankapp.view.Extensions;
import com.jonathan.bankapp.view.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private NavController navController;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final Extensions extensions = new Extensions();
    //private final CustomRequest customRequest = new CustomRequest();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        launchRegisterFragment(view);
        launchHomeActivity();
        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void launchRegisterFragment(View view) {
        navController = Navigation.findNavController(view);
        binding.materialButtonRegister.setOnClickListener(view2 ->
                navController.navigate(R.id.registerFragment));
    }

    private void setViewHome() {
        Intent intent = new Intent(getContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void launchHomeActivity() {
        binding.materialButtonLogin.setOnClickListener(view -> validateFields());
    }

    private void validateFields() {
        String email = binding.textInputEditTextEmail.getText().toString();
        String password = binding.textInputEditTextPassword.getText().toString();
        String state = "ACTIVO";

        if (!email.isEmpty() || !password.isEmpty()) {
            if (extensions.isValidEmail(email)) {
                petitionPostLogin(email, password, state);
            } else {
                Toast.makeText(getContext(), "Correo Electronico invalido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
        }
    }

    private void petitionPostLogin(String email, String password, String state) {

        try {
            String URL = getResources().getString(R.string.url_login_home); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES DE LA PC DE WPOSS
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);

            CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, jsonBody, headers, response -> {
                try {
                    extensions.setUpProgress(getContext()).show();
                    if (response.getBoolean("status")) {
                        JSONObject jsonData = response.getJSONObject("data");
                        String names = jsonData.getString("user_name");
                        String identification = jsonData.getString("user_identification");
                        JSONObject jsonBill = jsonData.getJSONObject("bill");
                        long bill = jsonBill.getLong("bill_number");
                        long amount = jsonBill.getLong("bill_amount");
                        if (jsonData.getString("user_estate").equals(state)) {
                            Toast.makeText(getContext(), "Bienvenido al sistema", Toast.LENGTH_SHORT).show();
                            editor.putString("token", (String) response.get("token"));
                            editor.putString("numberBill", String.valueOf(bill));
                            editor.putString("amount", String.valueOf(amount));
                            editor.putString("names", names);
                            editor.putString("identification", identification);
                            editor.apply();
                            setViewHome();
                        } else {
                            Toast.makeText(getContext(), "Usuario Inhabilitado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Email y/o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
                Log.e("ErrorRequest", "El error fue: " + error);
            });

            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            requestQueue.add(customRequest);
            extensions.setUpProgress(getContext()).dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}