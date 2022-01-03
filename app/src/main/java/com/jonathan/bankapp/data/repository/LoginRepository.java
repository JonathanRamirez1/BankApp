package com.jonathan.bankapp.data.repository;

public class LoginRepository {

    /*SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private void petitionPostLogin(String email, String password, String state) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            String URL = getResources().getString(R.string.url_login_home); //TODO CAMBIAR IP EN CASA, LA ACTUAL ES DE LA PC DE WPOSS
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("user_email", email);
            jsonBody.put("user_password", password);

            JsonObjectRequest petition = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (jsonObject.getBoolean("status")) {
                                    JSONObject jsonData = jsonObject.getJSONObject("data");
                                    String names = jsonData.getString("user_name");
                                    String identification = jsonData.getString("user_identification");
                                    JSONObject jsonBill = jsonData.getJSONObject("bill");
                                    long bill = jsonBill.getLong("bill_number");
                                    long amount = jsonBill.getLong("bill_amount");
                                    if (jsonData.getString("user_estate").equals(state)) {
                                        Toast.makeText(getContext(), "Bienvenido al sistema", Toast.LENGTH_SHORT).show();
                                        editor.putString("token", (String) jsonObject.get("token"));
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
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    });

            requestQueue.add(petition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/
}
