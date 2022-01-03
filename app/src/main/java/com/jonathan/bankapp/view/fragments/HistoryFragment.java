package com.jonathan.bankapp.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jonathan.bankapp.databinding.FragmentHistoryBinding;
import com.jonathan.bankapp.utils.Transfer;
import com.jonathan.bankapp.view.adapters.TransferAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private List<Transfer> transferList;
    SharedPreferences sharedPreferences;
    private RecyclerView.Adapter adapter;

    private String token;
    private String numberBill;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        numberBill = sharedPreferences.getString("numberBill", "");

        setAdapter();
        try {
            generateCode();
        } catch (JSONException | AuthFailureError e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        binding.recyclerViewHistory.setLayoutManager(layoutManager);
        transferList = new ArrayList<>();
        adapter = new TransferAdapter(transferList);
        binding.recyclerViewHistory.setHasFixedSize(true);
        binding.recyclerViewHistory.setAdapter(adapter);
    }

    private void generateCode() throws JSONException, AuthFailureError {
        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        String URL = getResources().getString(R.string.url_get_transfer_home); //TODO CAMBIAR IP
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("numberBill", numberBill);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, jsonBody, headers, response -> {
            try {
                JSONArray jsonDataArray = response.getJSONArray("data");
                for (int i = 0; i < jsonDataArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonDataArray.getJSONObject(i);

                        Transfer transaction = new Transfer();
                        transaction.setType(jsonObject.getString("transaction_type"));
                        transaction.setDescription(jsonObject.getString("transaction_description"));
                        transaction.setAmount(jsonObject.getString("transaction_amount"));
                        transaction.setDate(jsonObject.getString("transaction_date"));
                        transaction.setEstate((Boolean) jsonObject.get("transaction_estate"));
                        transferList.add(transaction);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ErrorRequestGetTransfers", "El error fue: " + error);
            error.printStackTrace();
        });
        customRequest.getHeaders().put("Authorization", token);

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(customRequest);
    }
}