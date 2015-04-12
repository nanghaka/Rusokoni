package com.ilicit.rusokoni;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.ui.adapter.LibsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev on 4/9/2015.
 */
public class MarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context parent;
    private String URL = "http://rusokoni.org/index.php/api/rest/markets/format/json";
    private String[] markets;

    public MarketAdapter(Context parent, String URL)
    {
        this.parent = parent;
        this.URL = URL;
        this.markets = new String[]{"boy", "gal", "manm"};
    }

    public MarketAdapter(ArrayList<HashMap> hashMaps, int row_application, MainActivity mainActivity) {
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_application, parent, false);
        TextView tv  = (TextView)v.findViewById(R.id.mkt_name);
        tv.setText("isiisisi");
        return new LibsRecyclerViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(232323); //setText(markets[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return markets.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mkt_name);

        }

    }


}
