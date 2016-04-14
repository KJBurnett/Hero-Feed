package com.burntech.kyler.comicrss;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Kyler James Burnett on 3/25/2015.
 */
public class FirstFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page, String title) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.thetextview);
        if (page != 4)
        tvLabel.setText(page + " -- " + title);
        ImageView iv = (ImageView) view.findViewById(R.id.image_view);
        setImage(iv, page);
        return view;
    }

    private void setImage(ImageView iv, int page) {
        switch(page) {
            case 0:
                Picasso
                        .with(getActivity().getApplicationContext())
                        .load("http://static.comicvine.com/uploads/scale_large/6/67663/4426746-40.jpg")
                        .fit()
                        .into(iv);
                break;
            case 1:
                Picasso
                        .with(getActivity().getApplicationContext())
                        .load("http://static.comicvine.com/uploads/scale_large/6/67663/4412530-01.jpg")
                        .fit()
                        .into(iv);
                break;
            case 2:
                Picasso
                        .with(getActivity().getApplicationContext())
                        .load("http://img1.wikia.nocookie.net/__cb20150129235847/marveldatabase/images/3/30/Spider-Gwen_Vol_1_1_Latour_Variant.jpg")
                        .fit()
                        .into(iv);
                break;
            case 3:
                Picasso
                        .with(getActivity().getApplicationContext())
                        .load("http://comix4free.comix4free.netdna-cdn.com/wp-content/uploads/bfi_thumb/4465492-futures-end-2yrov046kogx7ccwnqkf7k.jpg")
                        .fit()
                        .into(iv);
                break;
            case 4:
                break;
        }
    }
}
