package com.jonathan.bankapp.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jonathan.bankapp.R;
import com.jonathan.bankapp.data.repository.CustomRequest;
import com.jonathan.bankapp.databinding.FragmentTransferBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TransferFragment extends Fragment {

    private FragmentTransferBinding binding;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String token;
    private String numberBill;
    private String amount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransferBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        numberBill = sharedPreferences.getString("numberBill", "");
        openScanQRCode();
        transfer();
    }

    private void transfer() {
        binding.materialButtonTransfer.setOnClickListener(view -> {
            try {
                transferWithQRCode();
            } catch (JSONException | AuthFailureError e) {
                e.printStackTrace();
            }
        });
    }

    private void openScanQRCode() {
        binding.materialButtonScanQR.setOnClickListener(view -> scanQRCode());
    }

    private void scanQRCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Lector QR");
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.forSupportFragment(TransferFragment.this).initiateScan();
    }

    private void  transferWithQRCode() throws JSONException, AuthFailureError {
        String URL = getResources().getString(R.string.url_transfer_home); //TODO CAMBIAR IP
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        String accountDestiny = String.valueOf(binding.textViewAccount.getText());
        String amountDestiny = String.valueOf(binding.EditTextAmount.getText());

      //  sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        amount = sharedPreferences.getString("amount", "");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("numberBill", numberBill);
        jsonBody.put("typeTransation", "TRANSFERENCIA");
        jsonBody.put("amount", amountDestiny);
        jsonBody.put("numberBillDestiny", accountDestiny);

        CustomRequest customRequest = new CustomRequest(Request.Method.PUT, URL, jsonBody, headers, response -> {
            try {
                if((Boolean) response.get("status")) {
                    Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                    if(amount.isEmpty()) {
                        editor.putString("amount", amountDestiny);
                    } else {
                        editor.remove("amount");
                        long newBalance = Integer.parseInt(amount) - Integer.parseInt(amountDestiny);
                        editor.putString("amount", String.valueOf(newBalance));
                        editor.apply();
                    }
                } else {
                    Toast.makeText(getActivity(), "La transferencia a fallado", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ErrorRequestTransfer", "El error fue: " + error);
            error.printStackTrace();
        });
        customRequest.getHeaders().put("Authorization", token);

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(customRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getActivity(), "Se cancelo el scan", Toast.LENGTH_SHORT).show();
            } else {
                    binding.textViewAccount.setText(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}