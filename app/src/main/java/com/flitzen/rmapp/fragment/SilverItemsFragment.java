package com.flitzen.rmapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryAdapter;
import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryItems;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SilverItemsFragment extends Fragment {

    public static String PRAM_TITLE = "title";
    public static String PRAM_DATA = "data";

    @BindView(R.id.txt_silver_items_title)
    TextView txtTitle;
    @BindView(R.id.recyclerview_silver_items_list)
    RecyclerView recyclerView;

    @BindView(R.id.img_silver_items_back)
    View btnBack;

    String jsonData = "";
    ArrayList<SilverSubCategoryItems> arrayList = new ArrayList<SilverSubCategoryItems>();
    private SilverSubCategoryAdapter mAdaper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_silver_items_category, container, false);
        ButterKnife.bind(this, view);

        jsonData = getArguments().getString(PRAM_DATA);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdaper = new SilverSubCategoryAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(mAdaper);

        setData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).onBackPress();
            }
        });

        mAdaper.SetOnItemClickListner(new SilverSubCategoryAdapter.SetOnItemClick() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(SilverItemTypesFragment.PRAM_TITLE, arrayList.get(position).name);
                bundle.putInt(SilverItemTypesFragment.PRAM_MAIN_CAT_ID, arrayList.get(position).mainCategoryId);
                bundle.putString(SilverItemTypesFragment.PRAM_DATA, arrayList.get(position).data);
                Fragment fragment = new SilverItemTypesFragment();
                fragment.setArguments(bundle);
                ((HomeActivity) getActivity()).replaceFragment(fragment);
            }

            @Override
            public void onItemFavoriteClick(int position) {

            }

        });
        return view;
    }

    public void setData() {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            arrayList.clear();
            if (jsonArray.length() == 0) {
                Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    SilverSubCategoryItems item = new SilverSubCategoryItems();
                    item.setId(jObj.getInt("c_id"));
                    item.setMainCategoryId(jObj.getInt("c_parent_id"));
                    item.setName(jObj.getString("c_name"));
                    item.setImage(jObj.getString("c_image"));
                    item.setData(jObj.getString("children"));
                    item.setParentCategoryId(jObj.getInt("c_parent_id"));
                    arrayList.add(item);
                }
            }
            mAdaper.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}