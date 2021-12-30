package com.jonathan.banco.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jonathan.banco.R;
import com.jonathan.banco.databinding.FragmentWithdrawalsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WithdrawalsFragment extends Fragment {

    private FragmentWithdrawalsBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWithdrawalsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
      //  codeAuthWithdrawals("")
    }

    private void codeAuthWithdrawals(String numberBill) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String URL = getResources().getString(R.string.url_withdrawals); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES DE LA PC DE WPOSS
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("numberBill", numberBill);

            JsonObjectRequest petition = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getBoolean("status")) {
                                    JSONObject jsonData = jsonObject.getJSONObject("data");
                                    String code = jsonData.getString("code");
                                    System.out.println("cvxdvuyhxdfhdfyufggbyhhnfgunfg"+code);
                                    Toast.makeText(getContext(), "Bienvenido al sistema", Toast.LENGTH_SHORT).show();
                                    //setViewHome();
                                } else {
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
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> mapHeader = new HashMap<String, String>();
                    String token = sharedPreferences.getString("token" ,null);
                    mapHeader.put("Authorization", token);
                    System.out.println(token);
                    return mapHeader;
                }
            };

            requestQueue.add(petition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}