package com.association_coeur_de_france.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.association_coeur_de_france.model.DonModel;
import com.association_coeur_de_france.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    // private static final String BASE_URL = "http://192.168.1.100:8888/mon_api/";

    private static final String BASE_URL = "http://192.168.1.70:8888/";


    private ApiClient(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context.getApplicationContext());
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    // Interface callback spécifique pour login
    public interface LoginCallback {
        void onSuccess(int id, String firstName, String lastName, String email, String token,
                       String birthdate, String address, String postalCode,
                       String city, String country);
        void onError(String message);
    }


    // Méthode loginUser avec Volley et callback personnalisé
    public void loginUser(String email, String password, LoginCallback callback) {
        String url = BASE_URL + "api/login";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError("Erreur création JSON");
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Log.d("LOGIN_RESPONSE", response.toString());
                    try {
                        String status = response.getString("status");
                        if (status.equals("success")) {
                            String token = response.getString("token");
                            JSONObject userJson = response.getJSONObject("user");
                            int id = userJson.getInt("id");
                            String firstName = userJson.getString("first_name");
                            String lastName = userJson.getString("last_name");
                            String userEmail = userJson.getString("email");
                            String birthdate = userJson.optString("birthdate", "");
                            String address = userJson.optString("address", "");
                            String postalCode = userJson.optString("postal_code", "");
                            String city = userJson.optString("city", "");
                            String country = userJson.optString("country", "");

                            callback.onSuccess(id, firstName, lastName, userEmail, token,
                                    birthdate, address, postalCode, city, country);

                        } else {
                            String message = response.optString("message", "Erreur inconnue");
                            callback.onError(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Erreur de parsing JSON");
                    }
                },
                error -> {
                    callback.onError("Erreur réseau : " + (error.getMessage() != null ? error.getMessage() : "Erreur inconnue"));
                });

        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(jsonRequest);
    }



    // Garde les autres méthodes telles quelles

    public void registerUser(UserModel user, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "api/register";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", user.getFirstName());
                params.put("last_name", user.getLastName());
                params.put("email", user.getEmail());
                params.put("password", user.getPassword());
                params.put("password_confirm", user.getPasswordConfirmation());
                params.put("birthdate", user.getBirthdate());
                params.put("address", user.getAddress());
                params.put("postal_code", user.getPostalCode());
                params.put("city", user.getCity());
                params.put("country", user.getCountry());
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(postRequest);
    }

    public void getUserById(int userId, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "api/user?id=" + userId;

        StringRequest getRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);

        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(getRequest);
    }

    public interface ResetPasswordCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public void resetPassword(String email, ResetPasswordCallback callback) {
        String url = BASE_URL + "api/reset-password";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");
                        String message = json.optString("message", "Pas de message");

                        if (status.equals("success")) {
                            callback.onSuccess(message);
                        } else {
                            callback.onError(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("Erreur de parsing JSON");
                    }
                },
                error -> {
                    callback.onError("Erreur réseau : " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(postRequest);
    }

    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onError(Throwable error);
    }

    public void enregistrerDon(DonModel donModel, final ApiCallback<String> callback) {
        String url = BASE_URL + "api/don";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", donModel.getUserId());
            jsonBody.put("montant", donModel.getMontant());
            jsonBody.put("contribution", donModel.getContribution());
            jsonBody.put("total", donModel.getTotal());

            // Convertir le long en String pour la date
            long dateLong = donModel.getDate();
            String dateString = String.valueOf(dateLong);
            jsonBody.put("date", dateString);

            Log.d("API_REQUEST", "Requête JSON envoyée : " + jsonBody.toString());
        } catch (JSONException e) {
            Log.e("API_ERROR", "Erreur JSON lors de la création du corps de la requête", e);
            callback.onError(e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());
                    callback.onSuccess(response.toString());
                },
                error -> {
                    String errorMessage = "Erreur inconnue";
                    if (error != null) {
                        if (error.getMessage() != null) {
                            errorMessage = error.getMessage();
                        }

                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String body = "";
                            if (error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                } catch (Exception e) {
                                    Log.e("API_ERROR", "Erreur décodage corps de réponse", e);
                                    body = new String(error.networkResponse.data);
                                }
                            }
                            Log.e("API_ERROR", "Status code: " + statusCode + ", body: " + body);
                            errorMessage += "\nStatus code: " + statusCode + "\nBody: " + body;
                        }
                    }
                    Log.e("API_ERROR", "Erreur lors de la requête API: " + errorMessage);
                    callback.onError(new Throwable(errorMessage));
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Timeout
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }

}
