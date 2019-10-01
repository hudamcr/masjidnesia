package com.sidoarjolaptopservice.dashboard_donatur.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.sidoarjolaptopservice.dashboard_donatur.MasjidTerdekatActivity;
import com.sidoarjolaptopservice.dashboard_donatur.NotifikasiActivity;
import com.sidoarjolaptopservice.masjid.LoginActivity;
import com.sidoarjolaptopservice.masjid.R;
import com.sidoarjolaptopservice.masjid.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.PopupMenu;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.viewpager.widget.PagerAdapter;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_ID;
import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_USERNAME;
import static com.sidoarjolaptopservice.masjid.LoginActivity.session_status;

public class SlidingImage_Adapter extends PagerAdapter {

    SharedPreferences sharedpreferences;
    Boolean session = false;
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    String u;
    String id;
    ImageView image_notif;
    ImageView image_notif2;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String tag_json_obj = "json_obj_req";
    String jenis;
    private static final String TAG = DonaturActivity.class.getSimpleName();


    public SlidingImage_Adapter(Context context,ArrayList<Integer> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        sharedpreferences = context.getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        u = sharedpreferences.getString(TAG_USERNAME, null);
        id =sharedpreferences.getString(TAG_ID,null);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        TextView textView=imageLayout.findViewById(R.id.username);
        image_notif=imageLayout.findViewById(R.id.img_notif);
        image_notif2=imageLayout.findViewById(R.id.img_notif2);

        final ImageView image_user=imageLayout.findViewById(R.id.img_user);
        textView.setText("Assalamualaikum,"+u);
        imageView.setImageResource(IMAGES.get(position));
        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, image_user);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.logout, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(LoginActivity.session_status, false);
                        editor.putString(TAG_ID, null);
                        editor.putString(TAG_USERNAME, null);
                        editor.commit();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
//        image_notif.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, NotifikasiActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });

        if(position==0){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MasjidTerdekatActivity.class);
                    intent.putExtra("jenis_donasi", "shadaqah");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    jenis="shadaqah";
                    // finish();
                }
            });
        }
        if(position==1){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MasjidTerdekatActivity.class);
                    intent.putExtra("jenis_donasi", "zakat");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    jenis="zakat";
                    // finish();
                }
            });
        }

        if(position==2){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MasjidTerdekatActivity.class);
                    intent.putExtra("jenis_donasi", "wakaf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    jenis="wakaf";
                    // finish();
                }
            });
        }
        image_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tampil_notif(id);
            }
        });


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    private void tampil_notif(final String idx) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sidoarjolaptopservice.com/lomba/ucc/index.php/notifikasi/addnotif",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("[]")){
                            Toast.makeText(context, "Tidak ada notifikasi", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Donasi anda telah diterima oleh takmir masjid yang bersangkutan", Toast.LENGTH_LONG).show();

                        }
                        Log.e(TAG, "Response: " + response.toString());
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Log.e("v Delete", jObj.toString());


//
//                                Toast.makeText(context, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
//                                Intent i =new Intent(context,NotifikasiActivity.class);
//                                context.startActivity(i);

                            } else {
                                Toast.makeText(context, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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

                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user",idx);
                Log.e(TAG, " " + params);
                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
