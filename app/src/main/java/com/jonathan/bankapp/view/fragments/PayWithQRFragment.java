package com.jonathan.bankapp.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jonathan.bankapp.R;
import com.jonathan.bankapp.data.repository.CustomRequest;
import com.jonathan.bankapp.databinding.FragmentPayWithQRBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayWithQRFragment extends Fragment {

    private FragmentPayWithQRBinding binding;

    SharedPreferences sharedPreferences;
    private String token;
    private String numberBill;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPayWithQRBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        numberBill = sharedPreferences.getString("numberBill", "");

        try {
            generateQRCode();
        } catch (JSONException | AuthFailureError e) {
            e.printStackTrace();
        }
    }

    private void generateQRCode() throws JSONException, AuthFailureError {
        String URL = getResources().getString(R.string.url_qr_code_home); //TODO CAMBIAR IP
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

       // sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("numberBill", numberBill);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, jsonBody, headers, response -> {
            try {
                if((Boolean) response.get("status")) {
                    String imageQR = response.getString("img");
                    byte[] decodedImageQR = Base64.decode(imageQR, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImageQR, 0, decodedImageQR.length);
                    binding.imageViewQR.setImageBitmap(decodedByte);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ErrorRequestGenerateQR", "El error fue: " + error);
            error.printStackTrace();
        });
        customRequest.getHeaders().put("Authorization", token);

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(customRequest);
    }
}