package com.sample.app12306;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OrdersFragment extends Fragment {

    RecyclerView mRecyclerViewNotTravelledOrders;
    RecyclerView mRecyclerViewAlreadyTravelledOrders;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        mRecyclerViewNotTravelledOrders = v.findViewById(R.id.recycler_view_not_travelled_orders);
        mRecyclerViewNotTravelledOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewNotTravelledOrders.setAdapter(new NotTravelledTicketOrderAdapter());

        mRecyclerViewAlreadyTravelledOrders = v.findViewById(R.id.recycler_view_already_travelled_orders);
        mRecyclerViewAlreadyTravelledOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewAlreadyTravelledOrders.setAdapter(new AlreadyTravelledTicketOrderAdapter());
        return v;
    }

    private class NotTravelledTicketOrderAdapter extends RecyclerView.Adapter<TicketOrderHolder> {

        @Override
        public TicketOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TicketOrderHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TicketOrderHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class AlreadyTravelledTicketOrderAdapter extends RecyclerView.Adapter<TicketOrderHolder> {

        @Override
        public TicketOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TicketOrderHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TicketOrderHolder holder, int position) {
            holder.mImageViewLogo.setImageResource(R.drawable.yellow_ticket_icon);
        }

        @Override
        public int getItemCount() {
            return 60;
        }
    }


    private class TicketOrderHolder extends RecyclerView.ViewHolder {
        ImageView mImageViewLogo;
        TextView mTextViewTrainNum;
        TextView mTextViewDepartureTime;
        TextView mTextViewSeatClass;
        TextView mTextViewPrice;
        TextView mTextViewPassenger;

        public TicketOrderHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_ticket_order_info, parent, false));
            mImageViewLogo = itemView.findViewById(R.id.image_view_logo);
            mTextViewTrainNum = itemView.findViewById(R.id.text_view_train_num);
            mTextViewDepartureTime = itemView.findViewById(R.id.text_view_departure_time);
            mTextViewSeatClass = itemView.findViewById(R.id.text_view_seat_class);
            mTextViewPrice = itemView.findViewById(R.id.text_view_price);
            mTextViewPassenger = itemView.findViewById(R.id.text_view_passenger);
        }
    }

}
