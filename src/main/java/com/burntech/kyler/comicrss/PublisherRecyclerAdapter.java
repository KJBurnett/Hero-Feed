package com.burntech.kyler.comicrss;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyler J. Burnett on 4/8/2015.
 */
public class PublisherRecyclerAdapter extends RecyclerView.Adapter<PublisherRecyclerAdapter.ViewHolder> {
    private ArrayList<String> _publishers;
    private OnItemClickListener _onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View _view;
        TextView _txtTitle;
        //TextView _txtSubtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            _view = itemView;
            _txtTitle = (TextView) itemView.findViewById(R.id.item_title);
            //_txtSubtitle = (TextView) itemView.findViewById(R.id.item_subtitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (_onItemClickListener != null) {
                _onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PublisherRecyclerAdapter(ArrayList<String> publishers) {
        _publishers = publishers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PublisherRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publisher_list_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder._txtTitle.setText(_publishers.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        _onItemClickListener = onItemClickListener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _publishers.size();
    }
}