package com.example.act6.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.act6.EditTeman;
import com.example.act6.MainActivity;
import com.example.act6.R;
import com.example.act6.UpdateData;
import com.example.act6.app.AppController;
import com.example.act6.database.DBController;
import com.example.act6.database.Teman;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TemanAdapter extends RecyclerView.Adapter<TemanAdapter.TemanViewHolder> implements PopupMenu.OnMenuItemClickListener{
    private ArrayList<Teman> listData;
    String id,nm,tlp;
    View view;


    public TemanAdapter(ArrayList<Teman> listData) {
        this.listData = listData;
    }

    @Override
    public TemanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInf = LayoutInflater.from(parent.getContext());
        view = layoutInf.inflate(R.layout.row_data_teman, parent, false);

        return new TemanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemanViewHolder holder, int position) {
        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tlp = listData.get(position).getTelpon();

        holder.namaTxt.setTextColor(Color.BLACK);
        holder.namaTxt.setTextSize(20);
        holder.namaTxt.setText(nm);
        holder.telponTxt.setText(tlp);
    }

    public class TemanViewHolder extends RecyclerView.ViewHolder {

        private CardView cardku;
        private TextView namaTxt,telponTxt;

        public TemanViewHolder(View view) {

            super(view);
            cardku = (CardView) view.findViewById(R.id.kartuku);
            namaTxt = (TextView) view.findViewById(R.id.textNama);
            telponTxt = (TextView) view.findViewById(R.id.textTelpon);

            cardku.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu pm = new PopupMenu(v.getContext(), v);

                    pm.inflate(R.menu.popupmenu);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    Bundle bendel = new Bundle();
                                    bendel.putString("kunci1", id);
                                    bendel.putString("kunci2", nm);
                                    bendel.putString("kunci3", tlp);
                                    Intent inten = new Intent(v.getContext(), EditTeman.class);
                                    inten.putExtras(bendel);
                                    v.getContext().startActivity(inten);
                                    break;

                                case R.id.hapus:
                                    Context context;
                                    AlertDialog.Builder alertdb = new AlertDialog.Builder(v.getContext());
                                    alertdb.setTitle("Yakin " + nm + " akan dihapus?");
                                    alertdb.setMessage("Tekan Ya untuk menghapus");
                                    alertdb.setCancelable(false);
                                    alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HapusData(id);
                                            Toast.makeText(v.getContext(), "Data " + id + "telah dihapus", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(v.getContext(), MainActivity.class);
                                            v.getContext().startActivity(intent);
                                        }
                                    });
                                    alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog adlg = alertdb.create();
                                    adlg.show();
                                    break;
                                }
                                return true;
                        }
                    });
                    pm.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (listData != null)?listData.size() : 0;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        DBController controller = new DBController(view.getContext());
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(view.getContext(), UpdateData.class);
                intent.putExtra("id", id);
                intent.putExtra("nama", nm);
                intent.putExtra("telpon", tlp);
                view.getContext().startActivity(intent);
                break;
            case R.id.hapus:
                HashMap<String,String> qvalues = new HashMap<>();
                qvalues.put("id", id);
                controller.deleteData(qvalues);
                Intent i = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(i);
                Toast.makeText(view.getContext(),"Data sudah dihapus !", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
    }
    
    private void HapusData(final String idx) {
        String url_update = "http://10.0.2.2/umyTI/deteletm.php";
        final String TAG = MainActivity.class.getSimpleName();
        final String TAG_SUCCES = "Success";
        final int[] sukses = new int[1];
        
        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon : " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error : "+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();
                params.put("id", idx);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringReq);
    }     
}
