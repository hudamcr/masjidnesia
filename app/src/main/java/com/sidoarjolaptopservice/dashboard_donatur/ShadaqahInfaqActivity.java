package com.sidoarjolaptopservice.dashboard_donatur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sidoarjolaptopservice.masjid.R;

public class ShadaqahInfaqActivity extends AppCompatActivity {
    LinearLayout l1,l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadaqah_infaq);
        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        ImageView imageBack =findViewById(R.id.imageClose2);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserActivity.class);
                startActivity(intent);
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MasjidTerdekatActivity.class);
                intent.putExtra("jenis_donasi","shadaqah");
                startActivity(intent);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MasjidTerdekatActivity.class);
                intent.putExtra("jenis_donasi","infaq");
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(i);
        finish();
    }

}
