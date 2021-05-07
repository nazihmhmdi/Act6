package com.example.act6.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.act6.MainActivity;
import com.example.act6.R;
import com.example.act6.UpdateData;
import com.example.act6.database.DBController;
import com.example.act6.database.Teman;

import java.util.ArrayList;
import java.util.HashMap;

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

                    PopupMenu pm = new PopupMenu(cardku.getContext(), itemView);
                    pm.setOnMenuItemClickListener(TemanAdapter.this);
                    pm.inflate(R.menu.popupmenu);
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
}
