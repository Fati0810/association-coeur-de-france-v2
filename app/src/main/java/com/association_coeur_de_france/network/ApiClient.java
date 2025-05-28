package com.association_coeur_de_france.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.association_coeur_de_france.model.UserModel;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private static Context ctx;

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

    public void registerUser(UserModel user, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = "http://192.168.1.69:8888/mon_api/insert_user.php";

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

        // Timeout et retry policy
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        addToRequestQueue(postRequest);
    }

}
