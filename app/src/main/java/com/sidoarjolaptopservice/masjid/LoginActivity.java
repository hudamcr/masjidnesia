package com.sidoarjolaptopservice.masjid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sidoarjolaptopservice.dashboard_donatur.DonaturActivity;
import com.sidoarjolaptopservice.dashboard_donatur.UserActivity;
import com.sidoarjolaptopservice.dashboard_takmir.TakmirActivity;
import com.sidoarjolaptopservice.masjid.app.AppController;
import com.sidoarjolaptopservice.masjid.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class LoginActivity extends AppCompatActivity {
    int success;
    ConnectivityManager conMgr;
    ProgressDialog pDialog;
    private static final String Login_URL= Server.URL+"login/add";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_LEVEL = "level";
    String tag_json_obj = "json_obj_req";
    SharedPreferences sharedpreferences,sharedpreferences2;
    Boolean session = false;
    EditText edt_username,edt_password;
    Button btn_login;
    Boolean session2 = false;
    String id, username,level,id2, username2,level2;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String my_shared_preferences2 = "my_shared_preferences2";
    public static final String session_status = "session_status";
    public static final String session_status2 = "session_status2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet",
                        Toast.LENGTH_LONG).show();
            }
        }

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        username = sharedpreferences.getString(TAG_USERNAME, null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, DonaturActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_USERNAME, username);
            finish();
            startActivity(intent);
        }


        edt_username=findViewById(R.id.username);
        edt_password=findViewById(R.id.password);
        btn_login=findViewById(R.id.blogin);
        TextView txt_registrasi=findViewById(R.id.txt_resgistrasi);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });


        txt_registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RegistrasiActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkLogin(final String username, final String password) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Tunggu Sebentar ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, Login_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    String level = jObj.getString(TAG_LEVEL);
                    String id = jObj.getString(TAG_ID);
                    String username=jObj.getString(TAG_USERNAME);

                    Log.e("Successfully Login!", jObj.toString());

                    if(level.equals("donatur")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_USERNAME, username);
                        editor.putString(TAG_LEVEL,level);
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, DonaturActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_USERNAME, username);
                        intent.putExtra(TAG_LEVEL,level);
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof ServerError) {
                    message = "Server tidak di temukan. Tolong coba lagi nanti!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof ParseError) {
                    message = "Parsing data gagal! Tolong coba lagi nanti!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                } else if (error instanceof TimeoutError) {
                    message = "Tidak ada koneksi Internet...Tolong cek koneksi anda!";
                }
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

                Log.e(TAG, "Input gagal: " + error.getMessage());
                hideDialog();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic cGVyZGFtYWlhbjpwdHBpMTIzLjs=");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }
}
