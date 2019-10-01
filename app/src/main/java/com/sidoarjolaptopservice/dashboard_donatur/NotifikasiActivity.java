package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

import com.sidoarjolaptopservice.masjid.R;

public class NotifikasiActivity extends AppCompatActivity {
    TextView notif;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        data=getIntent().getStringExtra("status");
        notif =findViewById(R.id.notif);
        notif.setText(data);
    }
}
