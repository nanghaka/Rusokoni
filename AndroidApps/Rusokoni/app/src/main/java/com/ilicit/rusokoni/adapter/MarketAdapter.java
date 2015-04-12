package com.ilicit.rusokoni.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Dev on 4/6/2015.
 */
public class MarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
//    // Connection detector
//    ConnectionDetector cd;
//
//    // Alert dialog manager
//    AlertDialogManager alert = new AlertDialogManager();
//
//    // Progress Dialog
//    private ProgressDialog pDialog;
//
//    private int rowLayout;
//
//    // albums JSON url
//
//
//    // ALL JSON node names
//    private static final String TAG_ID = "mkt_id";
//    private static final String TAG_NAME = "mkt_name";
//
//    public MarketAdapter(List<HashMap> markets, int rowLayout, MainActivity act) {
//        this.markets = markets;
//        this.rowLayout = rowLayout;
//
//    }
//
//
//    public void clearMarkets() {
//        int size = this.markets.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                markets.remove(0);
//            }
//
//            this.notifyItemRangeRemoved(0, size);
//        }
//    }
//
//    public void addMarkets(List<HashMap> markets) {
//        this.markets.addAll(markets);
//        this.notifyItemRangeInserted(0, markets.size() - 1);
//    }
//
//
//    new     MarketAdapter().execute();
//
//
//
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
//            return new RecyclerView.ViewHolder(v);
//
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }
}
