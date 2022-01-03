package com.jonathan.bankapp.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;
import com.jonathan.bankapp.R;
import com.jonathan.bankapp.data.repository.CustomRequest;
import com.jonathan.bankapp.databinding.FragmentWithdrawalsBinding;
import com.jonathan.bankapp.view.Extensions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WithdrawalsFragment extends Fragment {

    private FragmentWithdrawalsBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String code = "";
    private String token;
    private String numberBill;
    private String amount;
    private long billAmount;
    private CountDownTimer countDownTimer;

    private final Extensions extensions = new Extensions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWithdrawalsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        numberBill = sharedPreferences.getString("numberBill", "");
        billAmount = Long.parseLong(sharedPreferences.getString("amount", ""));

        generateCode();
        withdrawals();
    }

    private void generateCode() {
        binding.materialButtonGenerateCode.setOnClickListener(view -> {
            try {
                codeAuthWithdrawals();
            } catch (JSONException | AuthFailureError e) {
                e.printStackTrace();
            }
        });
    }

    private void withdrawals() {
        binding.materialButtonMoney.setOnClickListener(view -> {
            try {
                requestWithdrawals();
            } catch (JSONException | AuthFailureError e) {
                e.printStackTrace();
            }
        });
    }

    private String time(long timeSeconds) {
        long horas = (timeSeconds / 3600);
        long min = ((timeSeconds - horas * 3600) / 60);
        long seconds = timeSeconds - (horas * 3600 + min * 60);

        return (seconds < 10) ? min + ":0" + seconds : min + ":" + seconds;
    }

    private void codeAuthWithdrawals() throws JSONException, AuthFailureError {
        String URL = getResources().getString(R.string.url_code_withdrawals_home); //TODO CAMBIAR IP
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JSONObject jsonBody = new JSONObject();
        sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        jsonBody.put("numberBill", numberBill);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URL, jsonBody, headers, response -> {
            try {
                JSONObject jsonData = response.getJSONObject("data");
                code = jsonData.getString("code");
                binding.materialButtonGenerateCode.setEnabled(false);
                binding.materialTextViewCode.setText(jsonData.getString("code"));
                countDownTimer = new CountDownTimer(1800000, 1000) {
                    @Override
                    public void onTick(long l) {
                        binding.materialTextViewTitleTime.setVisibility(MaterialTextView.VISIBLE);
                        binding.materialTextViewTitleTime.setText("Le queda este tiempo para hacer el retiro: ");
                        binding.materialTextViewTime.setText(time(l / 1000));
                    }

                    @Override
                    public void onFinish() {
                        code = "";
                        binding.materialTextViewTime.setText("");
                        binding.materialTextViewCode.setText("");
                        binding.materialButtonGenerateCode.setEnabled(true);
                    }
                }.start();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("ErrorRequestAuthWithdrawals", "El error fue: " + error);
            error.printStackTrace();
        });
        customRequest.getHeaders().put("Authorization", token);

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(customRequest);
    }

    private void requestWithdrawals() throws JSONException, AuthFailureError {
        String codeWithdrawals = binding.materialTextViewCode.getText().toString();
        String confirmCode = binding.textInputEditTextConfirmCode.getText().toString();
        String amountWithdrawals = binding.textInputEditTextAmount.getText().toString();

        if (!confirmCode.isEmpty() && !amountWithdrawals.isEmpty()) {
            if (confirmCode.equals(codeWithdrawals)) {
                if (Long.parseLong(amountWithdrawals) <= billAmount) {
                    Log.i("billAmount", String.valueOf(billAmount));
                    String amounts = String.valueOf(binding.textInputEditTextAmount.getText());
                    sharedPreferences = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    amount = sharedPreferences.getString("amount", "");

                    String URL = getResources().getString(R.string.url_withdrawals_home);
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");

                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("numberBill", numberBill);
                    jsonBody.put("typeTransation", "RETIRO");
                    jsonBody.put("amount", amounts);
                    jsonBody.put("codeAut", code);

                    CustomRequest customRequest = new CustomRequest(Request.Method.PUT, URL, jsonBody, headers, response -> {
                        try {
                            if ((Boolean) response.get("status")) {
                                Toast.makeText(getActivity(), response.getString("msg"), Toast.LENGTH_SHORT).show();
                                if (amount.isEmpty()) {
                                    editor.putString("amount", amounts);
                                } else {
                                    editor.remove("amount");
                                    long newBalance = Integer.parseInt(amount) - Integer.parseInt(amounts);
                                    editor.putString("amount", String.valueOf(newBalance));
                                    editor.apply();
                                }
                                countDownTimer.cancel();
                                code = "";
                                binding.materialTextViewTime.setText("");
                                binding.materialTextViewCode.setText("");
                                binding.textInputEditTextConfirmCode.setText("");
                                binding.textInputEditTextAmount.setText("");
                                binding.materialButtonGenerateCode.setEnabled(true);
                                binding.materialTextViewTitleTime.setVisibility(MaterialTextView.GONE);
                            } else {
                                Toast.makeText(getActivity(), response.getString("Retiro Fallido"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Log.e("ErrorRequestWithdrawals", "El error fue: " + error);
                        error.printStackTrace();
                    });
                    customRequest.getHeaders().put("Authorization", token);

                    RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                    requestQueue.add(customRequest);

                } else {
                    Toast.makeText(getContext(), "No tiene dinero suficiente", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Verifique que el codigo este escrito correctamente", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
        }
    }
}