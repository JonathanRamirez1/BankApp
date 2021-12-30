package com.jonathan.banco.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jonathan.banco.R;
import com.jonathan.banco.databinding.FragmentLoginBinding;
import com.jonathan.banco.view.Extensions;
import com.jonathan.banco.view.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private NavController navController;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private final Extensions extensions = new Extensions();

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
        sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
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
            if(extensions.isValidEmail(email)) {
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
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String URL = getResources().getString(R.string.url_login); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES DE LA PC DE WPOSS
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);

            JsonObjectRequest petition = new JsonObjectRequest(Request.Method.POST, URL ,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if(jsonObject.getBoolean("status")) {
                                    JSONObject jsonData = jsonObject.getJSONObject("data");
                                    if(jsonData.getString("user_estate").equals(state)) {
                                        Toast.makeText(getContext(), "Bienvenido al sistema", Toast.LENGTH_SHORT).show();
                                        String token = jsonObject.getString("token");
                                        editor.putString("token", token);
                                        editor.apply();
                                        setViewHome();
                                    } else  {
                                        Toast.makeText(getContext(), "Usuario Inhabilitado", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getContext(), "No se pudo ingresar al sistema", Toast.LENGTH_SHORT).show();
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