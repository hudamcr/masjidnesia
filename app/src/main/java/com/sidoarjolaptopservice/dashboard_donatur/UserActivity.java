package com.sidoarjolaptopservice.dashboard_donatur;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sidoarjolaptopservice.dashboard_donatur.adapter.Adapter;
import com.sidoarjolaptopservice.dashboard_donatur.model.Model;
import com.sidoarjolaptopservice.masjid.LoginActivity;
import com.sidoarjolaptopservice.masjid.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_USERNAME;
import static com.sidoarjolaptopservice.masjid.LoginActivity.session_status;

public class UserActivity extends AppCompatActivity {

    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    SharedPreferences sharedpreferences;
    String username,level,id;
    Boolean session = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        username = sharedpreferences.getString(TAG_USERNAME, null);
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Assalamualaikum, "+username, "logout"};
        ArrayAdapter<String> dropadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        dropadapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dropadapter);
        models = new ArrayList<>();
        models.add(new Model(R.drawable.sedekah, "Shadaqah & Infaq", "Shadaqah dan Infaq pada Aplikasi ini membuat para donatur "));
        models.add(new Model(R.drawable.zakat, "Zakat", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
        models.add(new Model(R.drawable.wakaf, "Wakaf", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
        models.add(new Model(R.drawable.qurban, "Qurban", "Business cards are cards bearing business information about a company or individual."));

        adapter = new Adapter(models, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
//        viewPager.setPadding(130, 0, 130, 0);

//        Integer[] colors_temp = {
//                getResources().getColor(R.color.color1),
//                getResources().getColor(R.color.color2),
//                getResources().getColor(R.color.color3),
//                getResources().getColor(R.color.color4)
//        };
//
//        colors = colors_temp;

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
//                    viewPager.setBackgroundColor(
//
//                            (Integer) argbEvaluator.evaluate(
//                                    positionOffset,
//                                    colors[position],
//                                    colors[position + 1]
//                            )
//                    );
//                }
//
//                else {
//                    viewPager.setBackgroundColor(colors[colors.length - 1]);
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}

