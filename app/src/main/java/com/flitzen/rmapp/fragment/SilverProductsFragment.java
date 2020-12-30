package com.flitzen.rmapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.flitzen.rmapp.activities.HomeActivity;
import com.flitzen.rmapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SilverProductsFragment extends Fragment {

    @BindView(R.id.btn_silver_items_1)
    View btnClick1;
    @BindView(R.id.btn_silver_items_2)
    View btnClick2;
    @BindView(R.id.btn_silver_items_3)
    View btnClick3;
    @BindView(R.id.btn_silver_items_4)
    View btnClick4;
    @BindView(R.id.btn_silver_items_5)
    View btnClick5;
    @BindView(R.id.txt_silver_products_title)
    TextView txtTitle;

    @BindView(R.id.img_silver_products_back)
    View btnBack;

    public SilverProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_silver_products, container, false);
        ButterKnife.bind(this, view);

        Bundle b = getArguments();
        txtTitle.setText(b.getString("title"));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).onBackPress();
            }
        });

        return view;
    }
}