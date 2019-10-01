package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.sidoarjolaptopservice.masjid.LoginActivity;
import com.sidoarjolaptopservice.masjid.R;
import com.sidoarjolaptopservice.masjid.app.AppController;
import com.sidoarjolaptopservice.masjid.util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_ID;
import static com.sidoarjolaptopservice.masjid.LoginActivity.session_status;

public class PaymentActivity extends AppCompatActivity {
    TextView nama_donatur,nama_bank,nominal;
    Button btn_konfirmasi,btn_kembali;int success;
    private static final String TAG = TransferActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String ADD_URL = Server.URL+"transaksi_pembayaran/addkonfirmasi";
    String tag_json_obj = "json_obj_req";
    String jenis;
    SharedPreferences sharedpreferences;
    Boolean session = false;
    public String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id_user = sharedpreferences.getString(TAG_ID, null);
        String nama=getIntent().getStringExtra("nama");
        String donasi=getIntent().getStringExtra("donasi");
        jenis=getIntent().getStringExtra("jenis");
        String id_masjid=getIntent().getStringExtra("id_masjid");
        String bank=getIntent().getStringExtra("bank");
        String rekening=getIntent().getStringExtra("rekening");
        nama_donatur=findViewById(R.id.txt_nama);
        nama_bank=findViewById(R.id.txt_bank);
        nominal=findViewById(R.id.nominal);
        nominal.setText(donasi);
        nama_bank.setText(bank+" "+rekening);
        nama_donatur.setText("Assalamualaikum, "+nama);
        getNotif("Status Pembayaran :","Belum Bayar");
        btn_konfirmasi=findViewById(R.id.btn_konfirmasi);
        btn_kembali=findViewById(R.id.btn_kembali);
        btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasi();
            }
        });
        btn_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DonaturActivity.class);
                startActivity(intent);
            }
        });

    }

    private void konfirmasi() {
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
                                Intent intent = new Intent(getApplicationContext(),DonaturActivity.class);
                                intent.putExtra("jenis_donasi",jenis);
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
                params.put("jenis_donasi", jenis);
                params.put("id_user",id_user);
                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

    }

    private void getNotif(String title, String messages){
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.logomas)
                .setContentTitle(title)
                .setContentText(messages);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
