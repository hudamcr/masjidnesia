package com.sidoarjolaptopservice.masjid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.sidoarjolaptopservice.masjid.app.AppController;
import com.sidoarjolaptopservice.masjid.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {
    TextView error;
    RadioGroup radioGroupNb;
    private RadioButton radioButtonNb;
    Button btn_regist;
    String level;
    EditText edit_username,edit_password,edit_confpassword;
    int success;
    ConnectivityManager conMgr;
    ProgressDialog pDialog;
    private static final String Register_URL = Server.URL+"register/add";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_USERNAME = "username";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
//        radioGroupNb = (RadioGroup) findViewById(R.id.radioGroupNb);
        error=findViewById(R.id.error);
        edit_username=findViewById(R.id.username);
        edit_password=findViewById(R.id.password);
        edit_confpassword =findViewById(R.id.password2);
        btn_regist=findViewById(R.id.bregist);

        TextView txt_login=findViewById(R.id.txt_login);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        edit_confpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strPass1 = edit_password.getText().toString();
                String strPass2 = edit_confpassword.getText().toString();
                if (strPass1.equals(strPass2)) {
                    error.setVisibility(View.GONE);
                    btn_regist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            int selectedId = radioGroupNb.getCheckedRadioButtonId();

//                            // mencari radio button
//                            radioButtonNb = (RadioButton) findViewById(selectedId);
//                            if(radioButtonNb.getText().equals("Donatur")){
//                                level="donatur";
//                            }
//                            else{
//                                level="takmir";
//                            }
//                            regist(level);
                            regist();
                        }
                    });
                } else {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Password Tidak sama");
                    btn_regist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"Tolong masukkan password yang sama",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

    }

    private void regist() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Register_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(),"Registrasi Berhasil",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        finish();
                        startActivity(intent);


                    } else {
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edit_username.getText().toString());
                params.put("password", edit_password.getText().toString());
                params.put("level","donatur");

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
