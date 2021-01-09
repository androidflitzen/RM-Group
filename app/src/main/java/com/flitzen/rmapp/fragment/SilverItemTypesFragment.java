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

import com.android.volley.VolleyError;
import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryAdapter;
import com.flitzen.rmapp.AdaptersAndItems.SilverSubCategoryItems;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.BaseActivity;
import com.flitzen.rmapp.activities.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SilverItemTypesFragment extends Fragment {

    public static String PRAM_TITLE = "title";
    public static String PRAM_DATA = "data";
    public static String PRAM_MAIN_CAT_ID = "mainCategoryId";

    @BindView(R.id.txt_silver_items_types_title)
    TextView txtTitle;
    @BindView(R.id.recyclerview_silver_items_types_list)
    RecyclerView recyclerView;

    @BindView(R.id.img_silver_item_types_back)
    View btnBack;

    String jsonData = "";
    int mainCatId = 0;

    ArrayList<SilverSubCategoryItems> arrayList = new ArrayList<SilverSubCategoryItems>();
    private SilverSubCategoryAdapter mAdaper;

    BaseActivity baseActivity;

    public SilverItemTypesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_silver_item_types, container, false);
        ButterKnife.bind(this, view);

        baseActivity = ((BaseActivity) getActivity());

        Bundle b = getArguments();
        txtTitle.setText(b.getString(PRAM_TITLE));
        jsonData = b.getString(PRAM_DATA);
        mainCatId = b.getInt(PRAM_MAIN_CAT_ID);

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
                bundle.putString(ProductDetailsFragment.PRAM_TITLE, arrayList.get(position).name);
                bundle.putInt(ProductDetailsFragment.PRAM_CAT_ID, arrayList.get(position).id);
                Fragment fragment = new ProductDetailsFragment();
                fragment.setArguments(bundle);
                ((HomeActivity) getActivity()).replaceFragment(fragment);
            }

            @Override
            public void onItemFavoriteClick(int position) {
                favoriteClick(position);
            }
        });

        return view;
    }

    public void setData() {
        try {
            System.out.println("========jsonData   "+jsonData.toString());
            JSONArray jsonArray = new JSONArray(jsonData);
            arrayList.clear();
            if (jsonArray.length() == 0) {
                Toast.makeText(getActivity(), "No data found!", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    SilverSubCategoryItems item = new SilverSubCategoryItems();
                    item.setId(jObj.getInt("c_id"));
                    item.setMainCategoryId(mainCatId);
                    item.setName(jObj.getString("c_name"));
                    item.setImage(jObj.getString("c_image"));
                    item.setData(jObj.getString("children"));
                    item.setFavorite(jObj.getInt("is_favourite") != 0);
                    item.setSetFavorite(true);
                    item.setParentCategoryId(jObj.getInt("c_parent_id"));
                    arrayList.add(item);
                }
            }
            mAdaper.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void favoriteClick(int position) {
        String url = String.format(API.FavoriteItem,
                baseActivity.sharedPreferences.getString(SharePref.UID, ""),
                arrayList.get(position).id);

        baseActivity.requestAPI(url, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getActivity(), jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        arrayList.get(position).setFavorite(true);
                    } else {
                        arrayList.get(position).setFavorite(false);
                    }
                    mAdaper.notifyItemChanged(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}