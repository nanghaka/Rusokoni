package com.ilicit.rusokoni.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ilicit.rusokoni.R;
import com.ilicit.rusokoni.model.PriceModel;

import java.util.ArrayList;

/**
 * Created by Shaffic on 4/17/15.
 */
public  class PricesAdapter  extends RecyclerView.Adapter<PricesAdapter.ViewHolder> {

    private int rowLayout;

    Context context;
    ArrayList<PriceModel> marketsList;


    public PricesAdapter(ArrayList<PriceModel> marketsList, int rowLayout, Context act) {
        this.marketsList = marketsList;
        this.rowLayout = rowLayout;
        this.context = act;
    }








    public void clearApplications() {
        int size = this.marketsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                marketsList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addApplications(ArrayList<PriceModel> applications) {
        this.marketsList.addAll(applications);
        this.notifyItemRangeInserted(0, applications.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final PriceModel market = marketsList.get(i);
        viewHolder.name.setText(market.getProd_name());
        viewHolder.price.setText(market.getMl_price());
        viewHolder.wholesale.setText(market.getMl_wholesaleprice());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAct.animateActivity(appInfo, viewHolder.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return marketsList == null ? 0 : marketsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public TextView wholesale;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            wholesale = (TextView) itemView.findViewById(R.id.wholesale);
//
        }

    }









}
