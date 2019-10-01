package com.sidoarjolaptopservice.dashboard_donatur.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sidoarjolaptopservice.dashboard_donatur.MasjidTerdekatActivity;
import com.sidoarjolaptopservice.dashboard_donatur.TransferActivity;
import com.sidoarjolaptopservice.dashboard_donatur.model.Jarak;
import com.sidoarjolaptopservice.masjid.R;
import com.sidoarjolaptopservice.masjid.util.Server;

import java.util.ArrayList;
import java.util.List;


import androidx.recyclerview.widget.RecyclerView;

import static com.sidoarjolaptopservice.masjid.LoginActivity.TAG_USERNAME;

public class JarakAdapter extends RecyclerView.Adapter<JarakAdapter.ProductViewHolder>implements Filterable{

    private LayoutInflater inflater;
    public List<Jarak> productList;
    private List<Jarak> productListFiltered;
    private JarakAdapterListener listener;
    private Context mCtx;
    private OnItemClickListener mListener;
    Bitmap thumbnail;
    Bitmap decoded;
    int bitmap_size = 60;
    String username;
    Intent i;
    Intent userIntent;
    LinearLayout layoutBottomSheet;
    String nama;
    String donasi,id;
    String jenis,jeniss;
    int success;
    private static final String TAG = MasjidTerdekatActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String ADD_URL = Server.URL+"transaksi_pembayaran/add";

    String tag_json_obj = "json_obj_req";
    private String[] jenis_shadaqah = {
            "Shadaqah",
            "Infaq"
    };
    private String[] jenis_zakat = {
            "Zakat Maal",
            "Zakat Penghasilan",
            "Zakat Fidyah"
    };
    private String[] jenis_wakaf = {
            "Wakaf Masjid",
            "Wakaf Pendidikan"
    };
    BottomSheetBehavior sheetBehavior;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public JarakAdapter(Context mCtx, List<Jarak> productList, MasjidTerdekatActivity tampilJarakActivity) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.productListFiltered = productList;
        i = new Intent(mCtx, MasjidTerdekatActivity.class);
        userIntent= ((Activity)mCtx).getIntent();
        username= userIntent.getStringExtra(TAG_USERNAME);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.rv_masjid, null);
        return new ProductViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        final Jarak jarak = productListFiltered.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jenis=((MasjidTerdekatActivity)mCtx).jenis_donasi;
                if(jenis.equals("shadaqah")){
                    shadaqah(jarak.getId());
                }
                else if(jenis.equals("zakat")){
                    zakat(jarak.getId());
                }
                else if(jenis.equals("wakaf")){
                    wakaf(jarak.getId());
                }

            }
        });

        String nama_jarak = jarak.getNama();
        //loading the image
        Glide
                .with(mCtx)
                .load("http://sidoarjolaptopservice.com/lomba/foto/"+jarak.getGambar())
                .override(512, 512) // resizes the image to these dimensions (in pixel). resize does not respect aspect ratio
                .into(holder.imageView);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mCtx, EditJarakActivity.class);
//                String idd= String.valueOf(productList.get(position).getId());
//                intent.putExtra(TAG_USERNAME, username);
//                intent.putExtra("id",idd);
//                intent.putExtra("gambar_jarak", productList.get(position).getGambar_jarak());
//                intent.putExtra("nama_jarak", productList.get(position).getNama_jarak());
////                Toast.makeText(mCtx,productList.get(position).getNama_jarak(),Toast.LENGTH_SHORT).show();
//                mCtx.startActivity(intent);
//            }
//        });

        holder.nama.setText(jarak.getNama());
        Double jarakk=Double.parseDouble(jarak.getJarak());
        holder.jarak.setText("Jarak : "+String.format("%.2f", jarakk)+" Km");
        holder.nama_takmir.setText(jarak.getNama_takmir());
        holder.alamat.setText(jarak.getAlamat());
    }




    private void shadaqah(int id) {
        Toast.makeText(mCtx,jenis,Toast.LENGTH_SHORT).show();
        View modelBottomSheet = LayoutInflater.from(mCtx).inflate(R.layout.bottom_sheet_shadaqah, null);
        Spinner spNamen2= modelBottomSheet.findViewById(R.id.sp_jenis);
        final EditText edt_nama=modelBottomSheet.findViewById(R.id.nama);
        final EditText edt_donasi=modelBottomSheet.findViewById(R.id.donasi);
        final ArrayAdapter<String> adapterr = new ArrayAdapter<>(mCtx,
                R.layout.spinner_item, jenis_shadaqah);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamen2.setAdapter(adapterr);
        spNamen2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                jeniss=adapterr.getItem(i);
//                        Toast.makeText(mCtx, "Selected "+ jeniss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nama=edt_nama.getText().toString();
        donasi=edt_donasi.getText().toString();
        this.id = String.valueOf(id);
        Button btn=modelBottomSheet.findViewById(R.id.bayar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx,TransferActivity.class);
                intent.putExtra("nama",edt_nama.getText().toString());
                intent.putExtra("donasi",edt_donasi.getText().toString());
                intent.putExtra("jenis",jeniss);
                intent.putExtra("id", JarakAdapter.this.id);

                mCtx.startActivity(intent);
//                        donasi_masjid(edt_nama.getText().toString(),edt_donasi.getText().toString(),jeniss,jarak.getId());
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(mCtx,R.style.BottomSheetDialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setContentView(modelBottomSheet);
        dialog.show();
    }

    private void zakat(int id) {
        Toast.makeText(mCtx,jenis,Toast.LENGTH_SHORT).show();
        View modelBottomSheet = LayoutInflater.from(mCtx).inflate(R.layout.bottom_sheet_zakat, null);
        Spinner spNamen2= modelBottomSheet.findViewById(R.id.sp_jenis);
        final EditText edt_nama=modelBottomSheet.findViewById(R.id.nama);
        final EditText edt_donasi=modelBottomSheet.findViewById(R.id.donasi);
        final ArrayAdapter<String> adapterr = new ArrayAdapter<>(mCtx,
                R.layout.spinner_item, jenis_zakat);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamen2.setAdapter(adapterr);
        spNamen2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                jeniss=adapterr.getItem(i);
//                        Toast.makeText(mCtx, "Selected "+ jeniss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nama=edt_nama.getText().toString();
        donasi=edt_donasi.getText().toString();
        this.id = String.valueOf(id);
        Button btn=modelBottomSheet.findViewById(R.id.bayar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx,TransferActivity.class);
                intent.putExtra("nama",edt_nama.getText().toString());
                intent.putExtra("donasi",edt_donasi.getText().toString());
                intent.putExtra("jenis",jeniss);
                intent.putExtra("id", JarakAdapter.this.id);

                mCtx.startActivity(intent);
//                        donasi_masjid(edt_nama.getText().toString(),edt_donasi.getText().toString(),jeniss,jarak.getId());
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(mCtx,R.style.BottomSheetDialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setContentView(modelBottomSheet);
        dialog.show();
    }

    private void wakaf(int id) {
        Toast.makeText(mCtx,jenis,Toast.LENGTH_SHORT).show();
        View modelBottomSheet = LayoutInflater.from(mCtx).inflate(R.layout.bottom_sheet_zakat, null);
        Spinner spNamen2= modelBottomSheet.findViewById(R.id.sp_jenis);
        final EditText edt_nama=modelBottomSheet.findViewById(R.id.nama);
        final EditText edt_donasi=modelBottomSheet.findViewById(R.id.donasi);
        final ArrayAdapter<String> adapterr = new ArrayAdapter<>(mCtx,
                R.layout.spinner_item, jenis_wakaf);
        adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamen2.setAdapter(adapterr);
        spNamen2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                jeniss=adapterr.getItem(i);
//                        Toast.makeText(mCtx, "Selected "+ jeniss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nama=edt_nama.getText().toString();
        donasi=edt_donasi.getText().toString();
        this.id = String.valueOf(id);
        Button btn=modelBottomSheet.findViewById(R.id.bayar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mCtx,TransferActivity.class);
                intent.putExtra("nama",edt_nama.getText().toString());
                intent.putExtra("donasi",edt_donasi.getText().toString());
                intent.putExtra("jenis",jeniss);
                intent.putExtra("id", JarakAdapter.this.id);

                mCtx.startActivity(intent);
//                        donasi_masjid(edt_nama.getText().toString(),edt_donasi.getText().toString(),jeniss,jarak.getId());
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(mCtx,R.style.BottomSheetDialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setContentView(modelBottomSheet);
        dialog.show();

    }


    @Override
    public int getItemCount() {

        return productListFiltered.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Jarak> filteredList = new ArrayList<>();
                    for (Jarak row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase()) ||row.getNama().toUpperCase().contains(charString.toUpperCase()) || row.getNama().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<Jarak>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public TextView nama,jarak,alamat,nama_takmir;
        public ImageView imageView;
        private RelativeLayout itemList;
        private Context context;


        public ProductViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            jarak=(TextView) itemView.findViewById(R.id.jarak);
            nama_takmir=(TextView)itemView.findViewById(R.id.nama_takmir);
            alamat = (TextView)itemView.findViewById(R.id.alamat);

        }
    }

    public interface JarakAdapterListener {
        void onJarakSelected(Jarak jarak);
    }

}