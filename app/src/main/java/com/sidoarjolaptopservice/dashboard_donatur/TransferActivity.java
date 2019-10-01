package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
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
import com.sidoarjolaptopservice.masjid.LoginActivity;
import com.sidoarjolaptopservice.masjid.R;
import com.sidoarjolaptopservice.masjid.app.AppController;
import com.sidoarjolaptopservice.masjid.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_ID;
import static com.sidoarjolaptopservice.masjid.LoginActivity.session_status;

public class TransferActivity extends AppCompatActivity {
    private RecyclerView sectionHeader;
    private SectionedRecyclerViewAdapter sectionAdapter;
    int success;
    private static final String TAG = TransferActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String ADD_URL = Server.URL+"transaksi_pembayaran/add";
    String tag_json_obj = "json_obj_req";
    String nama,donasi,jenis,id;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        nama=getIntent().getStringExtra("nama");
        donasi=getIntent().getStringExtra("donasi");
        jenis=getIntent().getStringExtra("jenis");
        id=getIntent().getStringExtra("id");
        CardView c1=findViewById(R.id.c1);
        CardView c2=findViewById(R.id.c2);
        CardView c3=findViewById(R.id.c3);
        CardView c4=findViewById(R.id.c4);
        CardView c5=findViewById(R.id.c5);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_user = sharedpreferences.getString(TAG_ID, null);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donasi_masjid(nama,donasi,jenis, Integer.parseInt(id),"BNI Syariah","a/n Moh Dahlan");
            }
        });

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),nama+donasi+jenis+ id,Toast.LENGTH_SHORT).show();
                donasi_masjid(nama,donasi,jenis, Integer.parseInt(id),"BCA","a/n Nabilah Imro'atul Fauziah");
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),nama+donasi+jenis+ id,Toast.LENGTH_SHORT).show();
                donasi_masjid(nama,donasi,jenis, Integer.parseInt(id),"MANDIRI","a/n Achmad Aqil Atamimi");
            }
        });

        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),nama+donasi+jenis+ id,Toast.LENGTH_SHORT).show();
                donasi_masjid2(nama,donasi,jenis, Integer.parseInt(id));
            }
        });

        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),nama+donasi+jenis+ id,Toast.LENGTH_SHORT).show();
                donasi_masjid2(nama,donasi,jenis, Integer.parseInt(id));
            }
        });
    }

    private void donasi_masjid(final String nama, final String donasi, final String jenis, final int id,final String bank,final String no_rekening) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),PaymentActivity.class);
                                intent.putExtra("nama",nama);
                                intent.putExtra("donasi",donasi);
                                intent.putExtra("jenis",jenis);
                                intent.putExtra("bank",bank);
                                intent.putExtra("rekening",no_rekening);
                                intent.putExtra("id_masjid",String.valueOf(id));
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog

                        //menampilkan toast
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
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic cHRwaTpwdHBpMTIzLg==");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                Time now = new Time();
                now.setToNow();
                String tanggal = now.format("%d-%m-%Y %T");
                //menambah parameter yang di kirim ke web servis
                params.put("id_masjid", String.valueOf(id));
                params.put("nama_donatur", nama);
                params.put("jenis_donasi", jenis);
                params.put("nominal", donasi);
                params.put("tanggal_donasi",tanggal);
                params.put("id_user",id_user);
                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void donasi_masjid2(final String nama, final String donasi, final String jenis, final int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),PaymentOnlineActivity.class);
                                intent.putExtra("nama",nama);
                                intent.putExtra("donasi",donasi);
                                intent.putExtra("jenis",jenis);
                                intent.putExtra("id_masjid",String.valueOf(id));
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog

                        //menampilkan toast
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
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Basic cHRwaTpwdHBpMTIzLg==");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                Time now = new Time();
                now.setToNow();
                String tanggal = now.format("%d-%m-%Y %T");
                //menambah parameter yang di kirim ke web servis
                params.put("id_masjid", String.valueOf(id));
                params.put("nama_donatur", nama);
                params.put("jenis_donasi", jenis);
                params.put("nominal", donasi);
                params.put("tanggal_donasi",tanggal);
                params.put("id_user",id_user);
                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);



    }
}