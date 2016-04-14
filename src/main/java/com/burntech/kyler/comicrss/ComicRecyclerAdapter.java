package com.burntech.kyler.comicrss;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kyler J. Burnett on 4/10/2015.
 */
public class ComicRecyclerAdapter extends RecyclerView.Adapter<ComicRecyclerAdapter.ViewHolder> {
    private ArrayList<Comic> _comics;
    private OnItemClickListener _onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View _view;
        TextView _txtTitle;
        TextView _txtSubtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            _view = itemView;
            _txtTitle = (TextView) itemView.findViewById(R.id.item_title);
            _txtSubtitle = (TextView) itemView.findViewById(R.id.item_subtitle);
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
    public ComicRecyclerAdapter(ArrayList<Comic> comics) {
        _comics = comics;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ComicRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_list_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder._txtTitle.setText(_comics.get(position).getTitle());
        holder._txtSubtitle.setText(_comics.get(position).getDate());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        _onItemClickListener = onItemClickListener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return _comics.size();
    }
}
