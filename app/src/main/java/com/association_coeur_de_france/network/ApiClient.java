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

    private static final String BASE_URL = "http://192.168.1.70:8888/mon_api/";


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
                       String city, String country, String createdAt);
        void onError(String message);
    }


    // Méthode loginUser avec Volley et callback personnalisé
    public void loginUser(String email, String password, LoginCallback callback) {
        String url = BASE_URL + "login_user.php";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");
                        if (status.equals("success")) {
                            String token = json.getString("token");

                            JSONObject userJson = json.getJSONObject("user");
                            int id = userJson.getInt("id");
                            String firstName = userJson.getString("first_name");
                            String lastName = userJson.getString("last_name");
                            String userEmail = userJson.getString("email");
                            String birthdate = userJson.optString("birthdate", "");
                            String address = userJson.optString("address", "");
                            String postalCode = userJson.optString("postal_code", "");
                            String city = userJson.optString("city", "");
                            String country = userJson.optString("country", "");
                            String createdAt = userJson.optString("created_at", "");

                            // Passe toutes les infos dans le callback
                            callback.onSuccess(
                                    id, firstName, lastName, userEmail, token,
                                    birthdate, address, postalCode, city, country, createdAt
                            );

                        } else {
                            String message = json.optString("message", "Erreur inconnue");
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
                params.put("password", password);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(postRequest);
    }


    // Garde les autres méthodes telles quelles

    public void registerUser(UserModel user, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "insert_user.php";

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
        String url = BASE_URL + "get_user.php?id=" + userId;

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
        String url = BASE_URL + "forgot_password.php";

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
        String url = BASE_URL + "/don.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user_id", donModel.getUserId());
            jsonBody.put("montant", donModel.getMontant());
            jsonBody.put("contribution", donModel.getContribution());
            jsonBody.put("total", donModel.getTotal());
            jsonBody.put("date", donModel.getDate()); // timestamp en ms

            Log.d("API_REQUEST", "Requête JSON envoyée : " + jsonBody.toString());
        } catch (JSONException e) {
            Log.e("API_ERROR", "Erreur JSON lors de la création du corps de la requête", e);
            callback.onError(e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Log.d("API_RESPONSE", "Réponse reçue : " + response.toString());
                    callback.onSuccess(response.toString());  // Passe la réponse brute pour analyse
                },
                error -> {
                    Log.e("API_ERROR", "Erreur lors de la requête API", error);
                    callback.onError(error);
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        // Optionnel : augmenter timeout pour éviter les timeout trop courts
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000, // 10 secondes timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);
    }



}
