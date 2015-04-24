package com.ilicit.rusokoni.adapter;



    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.app.FragmentManager;
    import android.app.FragmentTransaction;
    import android.os.Bundle;
    import android.support.v7.widget.RecyclerView;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import com.ilicit.rusokoni.MainActivity;
    import com.ilicit.rusokoni.MarketListActivity;
    import com.ilicit.rusokoni.R;
    import com.ilicit.rusokoni.entity.AppInfo;
    import com.ilicit.rusokoni.model.MarketModel;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;

    public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

        private int rowLayout;

        Context context;
        List<MarketModel> marketsList;


        public MarketAdapter(List<MarketModel> marketsList, int rowLayout, Context act) {
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

        public void addApplications(List<MarketModel> applications) {
            this.marketsList.addAll(applications);
            this.notifyItemRangeInserted(0, applications.size() - 1);
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i)  {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            viewHolder.name.setOnClickListener(this);
            viewHolder.name.setOnLongClickListener(this);

            viewHolder.name.setTag(viewHolder);
            v.setTag(viewHolder);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final MarketModel market = marketsList.get(i);
            viewHolder.name.setText(market.getMkt_name());


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

        @Override
        public void onClick(View v) {

            ViewHolder holder = (ViewHolder) v.getTag();
            int position = holder.getPosition();

            if (v.getId() == holder.name.getId()) {
                MarketModel item = marketsList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + item.getMkt_id());

                MarketListActivity fragment2 = new MarketListActivity();
                fragment2.setArguments(bundle);
                Activity ac = (Activity) context;
                FragmentManager fragmentManager = ac.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, fragment2).addToBackStack(null);
                fragmentTransaction.commit();


            }

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;


            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.mkt_name);

            }

        }





    }