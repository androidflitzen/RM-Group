package com.flitzen.rmapp.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.flitzen.rmapp.AdaptersAndItems.ProductListAdapter;
import com.flitzen.rmapp.AdaptersAndItems.ProductListItems;
import com.flitzen.rmapp.Class.API;
import com.flitzen.rmapp.Class.Helper;
import com.flitzen.rmapp.Class.ProgressDialog;
import com.flitzen.rmapp.Class.SharePref;
import com.flitzen.rmapp.R;
import com.flitzen.rmapp.activities.BaseActivity;
import com.flitzen.rmapp.activities.HomeActivity;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsFragment extends Fragment {

    public static String PRAM_TITLE = "title";
    public static String PRAM_CAT_ID = "catId";

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recyclerview_product_details_list)
    RecyclerView recyclerView;
    @BindView(R.id.txt_product_list_unit)
    TextView txtUnit;

    @BindView(R.id.layout_1)
    View layoutOfVariation1;
    @BindView(R.id.layout_2)
    View layoutOfVariation2;
    @BindView(R.id.layout_3)
    View layoutOfVariation3;
    @BindView(R.id.layout_4)
    View layoutOfVariation4;
    @BindView(R.id.layout_5)
    View layoutOfVariation5;

    @BindView(R.id.view_product_details_v2_vity)
    View viewV2Vity;
    @BindView(R.id.view_product_details_v3_vity)
    View viewV3Vity;
    @BindView(R.id.view_product_details_v3_type)
    View viewV3Type;
    @BindView(R.id.view_product_details_v4_gop)
    LinearLayout viewV4Type;
    @BindView(R.id.view_product_details_v5_size)
    LinearLayout viewV5Size;

    //Variation 1
    @BindView(R.id.txt_product_list_v1_soda)
    TextView txtSoda;
    @BindView(R.id.txt_product_list_v1_mina)
    TextView txtMina;

    @BindView(R.id.txt_product_list_v1_dal)
    TextView txtDal;
    @BindView(R.id.txt_product_list_v1_keri)
    TextView txtKeri;
    @BindView(R.id.txt_product_list_v1_pankho)
    TextView txtPankho;
    @BindView(R.id.txt_product_list_v1_fancy)
    TextView txtFancy;

    @BindView(R.id.txt_product_list_v1_rava)
    TextView txtRava;
    @BindView(R.id.txt_product_list_v1_resa)
    TextView txtResa;

    //Variation 2
    @BindView(R.id.rdb_product_details_v2_pm60)
    RadioButton rdbPm60;
    @BindView(R.id.rdb_product_details_v2_pm70)
    RadioButton rdbPm70;
    @BindView(R.id.rdb_product_details_v2_pmsil)
    RadioButton rdbPmSil;
    @BindView(R.id.tab_product_details_v2_vity)
    TabLayout tabVity;
    @BindView(R.id.tab_product_details_v3_vity)
    TabLayout tabVityV3;

    //Variation 3
    @BindView(R.id.edt_product_details_v3_stamp)
    EditText edtStamp;
    @BindView(R.id.edt_product_details_v3_touch)
    EditText edtTouch;
    @BindView(R.id.tab_product_details_v3_type)
    TabLayout tabTypes;

    //Variation 4
    @BindView(R.id.tab_GOP_MILAN)
    TabLayout tabGOPV4;

    //Variation 5
    @BindView(R.id.tab_size_selection)
    TabLayout tabSizeSelection;


    @BindView(R.id.img_back)
    View btnBack;
    @BindView(R.id.card_product_details_add_to_cart)
    CardView btnAddToCart;
    @BindView(R.id.card_product_details_already_in_cart)
    CardView btnAlreadyInCart;

    @BindView(R.id.edt_product_details_notes)
    EditText edtNote;

    int categoryId = 0;
    BaseActivity baseActivity;

    ArrayList<ProductListItems> arrayList = new ArrayList<>();
    private ProductListAdapter mAdapter;

    int cartId = 0;
    String variation1 = "";
    String variation2 = "";
    String variation3 = "";
    String variation4 = "";
    String variation5 = "";
    String variation6 = "";
    String variation7 = "";
    String variation8 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, view);

        baseActivity = ((BaseActivity) getActivity());
        categoryId = getArguments().getInt(PRAM_CAT_ID);

        txtTitle.setText(getArguments().getString(PRAM_TITLE));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProductListAdapter(getActivity(), arrayList);
        mAdapter.setCategoryId(categoryId);
        recyclerView.setAdapter(mAdapter);

        getProducts();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).onBackPress();
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCartClick(true);
            }
        });

        btnAlreadyInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToCartClick(false);
            }
        });
        return view;
    }

    public void getProducts() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String url = String.format(API.ProductsData,
                baseActivity.sharedPreferences.getString(SharePref.UID, ""),
                categoryId);

        baseActivity.requestAPI(url, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        txtUnit.setText("Quantity in " + jsonObject.getString("product_unit"));
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        if (jsonArray.length() > 0) {
                            arrayList.clear();
                            boolean isInCart = false;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                ProductListItems item = new ProductListItems();
                                item.setId(jObj.getInt("product_id"));
                                item.setName(jObj.getString("product_name"));
                                item.setDetails(jObj.getString("product_details"));
                                item.setQty(jObj.getInt("unit_in_cart"));
                                item.setThubnailImage(jObj.getString("thumbnail_image"));
                                item.setImage(jObj.getString("original_image"));
                                if (jObj.getBoolean("is_cart")) {
                                    isInCart = true;
                                }

                                edtNote.setText(jObj.getString("notes"));

                                if (cartId == 0 && isInCart) {
                                    cartId = jObj.getInt("cv_id");
                                    variation1 = jObj.getString("variation_1");
                                    variation2 = jObj.getString("variation_2");
                                    variation3 = jObj.getString("variation_3");
                                    variation4 = jObj.getString("variation_4");
                                    variation5 = jObj.getString("variation_5");
                                    variation6 = jObj.getString("variation_6");
                                    variation7 = jObj.getString("variation_7");
                                    variation8 = jObj.getString("variation_8");
                                }
                                arrayList.add(item);
                            }
                            initCategoryVariationView();

                            if (isInCart) {
                                btnAddToCart.setVisibility(View.GONE);
                                btnAlreadyInCart.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Product not found!", Toast.LENGTH_SHORT).show();
                            ((HomeActivity) getActivity()).onBackPress();
                        }
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getActivity(), "Failed to get products, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectPercentage(TextView view) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_set_percentage_picker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        TextView txtTitle = dialogView.findViewById(R.id.txt_dialog_title);
        TextView txtValue = dialogView.findViewById(R.id.txt_percentage_value);
        SeekBar seekBar = dialogView.findViewById(R.id.seekbar_percentage);
        Button btnDSave = dialogView.findViewById(R.id.btn_dialog_save);
        Button btnDCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

        txtTitle.setText(view.getTag().toString());
        //seekBar.setMin(1);
        seekBar.setMax(100);

        if (!view.getText().toString().isEmpty()) {
            int value = Integer.parseInt(view.getText().toString().replace("%", "").trim());
            seekBar.setProgress(value);
            txtValue.setText(value + " %");
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtValue.setText(progress + " %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnDSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = seekBar.getProgress();
                view.setText(seekBar.getProgress() + "%");

                if (view.getId() == R.id.txt_product_list_v1_soda) {
                    int remainProgress = 100 - progress;
                    txtMina.setText(remainProgress + "%");
                }
                if (view.getId() == R.id.txt_product_list_v1_mina) {
                    int remainProgress = 100 - progress;
                    txtSoda.setText(remainProgress + "%");
                }

                if (view.getId() == R.id.txt_product_list_v1_rava) {
                    int remainProgress = 100 - progress;
                    txtResa.setText(remainProgress + "%");
                }
                if (view.getId() == R.id.txt_product_list_v1_resa) {
                    int remainProgress = 100 - progress;
                    txtRava.setText(remainProgress + "%");
                }

                if (view.getId() == R.id.txt_product_list_v1_dal) {
                    int remainProgress = (100 - progress) / 3;
                    txtKeri.setText(remainProgress + "%");
                    txtPankho.setText(remainProgress + "%");
                    txtFancy.setText(remainProgress + "%");
                }

                if (view.getId() == R.id.txt_product_list_v1_keri) {
                    int remainProgress = (100 - (progress + Integer.parseInt(txtDal.getText().toString().trim().replace("%", "")))) / 2;
                    txtPankho.setText(remainProgress + "%");
                    txtFancy.setText(remainProgress + "%");
                }

                if (view.getId() == R.id.txt_product_list_v1_pankho) {
                    int remainProgress = (100 - (progress + Integer.parseInt(txtDal.getText().toString().trim().replace("%", "")) + Integer.parseInt(txtKeri.getText().toString().trim().replace("%", ""))));
                    txtFancy.setText(remainProgress + "%");
                }

                alertDialog.dismiss();
            }
        });

        btnDCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    TextView.OnClickListener variationOnClick = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectPercentage(((TextView) v));
        }
    };

    public void initCategoryVariationView() {
        if (categoryId == 9 || categoryId == 12 || categoryId == 13) {
            layoutOfVariation1.setVisibility(View.VISIBLE);
            initLayout1Click();
            txtSoda.setText((Helper.isNullOrEmpty(variation1) ? "0" : variation1) + "%");
            txtMina.setText((Helper.isNullOrEmpty(variation2) ? "0" : variation2) + "%");
            txtDal.setText((Helper.isNullOrEmpty(variation3) ? "0" : variation3) + "%");
            txtKeri.setText((Helper.isNullOrEmpty(variation4) ? "0" : variation4) + "%");
            txtPankho.setText((Helper.isNullOrEmpty(variation5) ? "0" : variation5) + "%");
            txtFancy.setText((Helper.isNullOrEmpty(variation6) ? "0" : variation6) + "%");
            txtRava.setText((Helper.isNullOrEmpty(variation7) ? "0" : variation7) + "%");
            txtResa.setText((Helper.isNullOrEmpty(variation8) ? "0" : variation8) + "%");
        } else if (categoryId == 10 || categoryId == 11 || categoryId == 14 || categoryId == 15 || categoryId == 16 || categoryId == 17 || categoryId == 18 || categoryId == 19 || categoryId == 20
                || categoryId == 21 || categoryId == 22 || categoryId == 23 || categoryId == 24 || categoryId == 25 || categoryId == 26 || categoryId == 27 || categoryId == 28 || categoryId == 29
                || categoryId == 30 || categoryId==39 || categoryId == 42) {
            layoutOfVariation2.setVisibility(View.VISIBLE);
            if (categoryId == 14 || categoryId == 15 || categoryId == 16 || categoryId == 17) {
                viewV2Vity.setVisibility(View.VISIBLE);
                if (variation2.equals("1"))
                    tabVity.selectTab(tabVity.getTabAt(0));
                if (variation2.equals("3"))
                    tabVity.selectTab(tabVity.getTabAt(1));
                if (variation2.equals(5))
                    tabVity.selectTab(tabVity.getTabAt(2));
            } else {
                viewV2Vity.setVisibility(View.GONE);
            }
            initLayout2Click();
            if (rdbPm70.getText().toString().equals(variation1)) {
                rdbPm70.setVisibility(View.VISIBLE);
                rdbPmSil.setVisibility(View.VISIBLE);
                rdbPm60.setVisibility(View.GONE);
                rdbPm70.setChecked(true);
            } else if (categoryId == 22 || categoryId == 24 || categoryId==23 || categoryId==39) {
                rdbPm70.setVisibility(View.GONE);
                rdbPmSil.setVisibility(View.GONE);
                rdbPm60.setVisibility(View.VISIBLE);
                rdbPm60.setChecked(true);
            }else if(categoryId == 42){
                rdbPm70.setVisibility(View.GONE);
                rdbPmSil.setVisibility(View.VISIBLE);
                rdbPm60.setVisibility(View.GONE);
                rdbPmSil.setChecked(true);
            } else {
                rdbPm70.setVisibility(View.VISIBLE);
                rdbPmSil.setVisibility(View.VISIBLE);
                rdbPm60.setVisibility(View.GONE);
                rdbPmSil.setChecked(true);
            }

            if(categoryId == 18){
                layoutOfVariation3.setVisibility(View.VISIBLE);
                initLayout3Click1();
            }

            if(categoryId == 21){
                layoutOfVariation4.setVisibility(View.VISIBLE);
                viewV4Type.setVisibility(View.VISIBLE);

                for (int i = 0; i < tabGOPV4.getTabCount(); i++) {
                    if (variation3.equals(tabGOPV4.getTabAt(i).getText().toString()))
                        tabGOPV4.selectTab(tabGOPV4.getTabAt(i));
                }
            }
        }
        else if(categoryId == 43){
            layoutOfVariation5.setVisibility(View.VISIBLE);
            initLayout5Click();
        }

        /*else if (categoryId == 14) {
            layoutOfVariation3.setVisibility(View.VISIBLE);
            initLayout3Click();
            edtStamp.setText(variation1);
            edtTouch.setText(variation2);
        }*/
    }

    public void initLayout1Click() {
        txtSoda.setOnClickListener(variationOnClick);
        txtMina.setOnClickListener(variationOnClick);
        txtDal.setOnClickListener(variationOnClick);
        txtKeri.setOnClickListener(variationOnClick);
        txtPankho.setOnClickListener(variationOnClick);
        txtFancy.setOnClickListener(variationOnClick);
        txtRava.setOnClickListener(variationOnClick);
        txtResa.setOnClickListener(variationOnClick);
    }

    public void initLayout2Click() {
        if (categoryId == 14 || categoryId == 17 || categoryId == 15) {
            tabVity.removeTabAt(0);
            tabVity.removeTabAt(0);
        }
    }

    public void initLayout3Click() {
        if (categoryId == 14 || categoryId == 17) {
            viewV3Vity.setVisibility(View.VISIBLE);
            tabVityV3.removeTabAt(0);
            tabVityV3.removeTabAt(0);
            if (variation3.equals("1"))
                tabVityV3.selectTab(tabVity.getTabAt(0));
            if (variation3.equals("3"))
                tabVityV3.selectTab(tabVity.getTabAt(1));
            if (variation3.equals(5))
                tabVityV3.selectTab(tabVity.getTabAt(2));
        } else {
            viewV3Vity.setVisibility(View.GONE);
        }

        if (categoryId == 18) {
            viewV3Type.setVisibility(View.VISIBLE);
            for (int i = 0; i < tabTypes.getTabCount(); i++) {
                if (variation3.equals(tabTypes.getTabAt(i).getText().toString()))
                    tabTypes.selectTab(tabTypes.getTabAt(i));
            }
        } else {
            viewV3Type.setVisibility(View.GONE);
        }
    }

    public void initLayout3Click1() {
        if (categoryId == 18) {
            viewV3Type.setVisibility(View.VISIBLE);
            tabTypes.removeTabAt(2);
            tabTypes.removeTabAt(2);
            tabTypes.removeTabAt(2);
            for (int i = 0; i < tabTypes.getTabCount(); i++) {
                if (variation3.equals(tabTypes.getTabAt(i).getText().toString()))
                    tabTypes.selectTab(tabTypes.getTabAt(i));
            }
        }
    }

    public void initLayout4Click() {
        if (categoryId == 21) {

        }
    }

    public void initLayout5Click() {
        if (categoryId == 43) {
            viewV5Size.setVisibility(View.VISIBLE);
            for (int i = 0; i < tabSizeSelection.getTabCount(); i++) {
                if (variation1.equals(tabSizeSelection.getTabAt(i).getText().toString()))
                    tabSizeSelection.selectTab(tabSizeSelection.getTabAt(i));
            }
        }
    }

    public void onAddToCartClick(boolean isFromAddToCart) {
        int qty = 0;
        int qtyProductCount = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            qty += arrayList.get(i).qty;
            if (arrayList.get(i).qty > 0)
                qtyProductCount++;
        }

        if (qty == 0) {
            Toast.makeText(getActivity(), "Select product qty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (layoutOfVariation1.isShown()) {
            if (Helper.isNullOrEmpty(txtSoda.getText().toString())) {
                Toast.makeText(getActivity(), "Select Soda value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtMina.getText().toString())) {
                Toast.makeText(getActivity(), "Select Mina value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtDal.getText().toString())) {
                Toast.makeText(getActivity(), "Select Dal value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtKeri.getText().toString())) {
                Toast.makeText(getActivity(), "Select Keri value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtPankho.getText().toString())) {
                Toast.makeText(getActivity(), "Select Pankho value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtFancy.getText().toString())) {
                Toast.makeText(getActivity(), "Select Fancy value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtRava.getText().toString())) {
                Toast.makeText(getActivity(), "Select Rava value", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Helper.isNullOrEmpty(txtResa.getText().toString())) {
                Toast.makeText(getActivity(), "Select Resa value", Toast.LENGTH_SHORT).show();
                return;
            }
            int DalValue = Integer.parseInt(txtDal.getText().toString().trim().replace("%", ""));
            int KeriValue = Integer.parseInt(txtKeri.getText().toString().trim().replace("%", ""));
            int PankhoValue = Integer.parseInt(txtPankho.getText().toString().trim().replace("%", ""));
            int FancyValue = Integer.parseInt(txtFancy.getText().toString().trim().replace("%", "").replace("-", ""));

            if ((DalValue + KeriValue + PankhoValue + FancyValue) < 100 || (DalValue + KeriValue + PankhoValue + FancyValue) > 100) {
                Toast.makeText(getActivity(), "Dal, Keri, Pankho, Fancy value is not matched", Toast.LENGTH_LONG).show();
                return;
            }
        }

        if (layoutOfVariation2.isShown()) {
            if (!rdbPm70.isChecked() && !rdbPmSil.isChecked() && !rdbPm60.isChecked()) {
                Toast.makeText(getActivity(), "Select any one value of Stamp/Touch", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        /*if (layoutOfVariation3.isShown()) {
            if (edtStamp.getText().toString().trim().isEmpty()) {
                edtStamp.setError("Enter value");
                return;
            }
            if (edtTouch.getText().toString().trim().isEmpty()) {
                edtTouch.setError("Enter value");
                return;
            }
        }*/

        String[] productId = new String[qtyProductCount];
        String[] productQty = new String[qtyProductCount];
        int tempPosition = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).qty > 0) {
                productId[tempPosition] = String.valueOf(arrayList.get(i).id);
                productQty[tempPosition] = String.valueOf(arrayList.get(i).qty);
                tempPosition++;
            }
        }

        String url = "";
        if (cartId == 0)
            url = API.AddToCart;
        else url = API.UpdateToCart;

        String endFix = "user_id=" + baseActivity.sharedPreferences.getString(SharePref.UID, "");
        for (String value :
                productId) {
            if (endFix.isEmpty())
                endFix += "product_id[]=" + API.utf8Value(value);
            else endFix += "&product_id[]=" + API.utf8Value(value);
        }

        for (String value :
                productQty) {
            endFix += "&product_unit[]=" + API.utf8Value(value);
        }

        if (layoutOfVariation1.isShown()) {
            endFix += "&variation_1=" + API.utf8Value(txtSoda.getText().toString().replace("%", ""));
            endFix += "&variation_2=" + API.utf8Value(txtMina.getText().toString().replace("%", ""));
            endFix += "&variation_3=" + API.utf8Value(txtDal.getText().toString().replace("%", ""));
            endFix += "&variation_4=" + API.utf8Value(txtKeri.getText().toString().replace("%", ""));
            endFix += "&variation_5=" + API.utf8Value(txtPankho.getText().toString().replace("%", ""));
            endFix += "&variation_6=" + API.utf8Value(txtFancy.getText().toString().replace("%", ""));
            endFix += "&variation_7=" + API.utf8Value(txtRava.getText().toString().replace("%", ""));
            endFix += "&variation_8=" + API.utf8Value(txtResa.getText().toString().replace("%", ""));
        }

        if (layoutOfVariation2.isShown()) {
            if (rdbPm70.isChecked()) {
                endFix += "&variation_1=" + API.utf8Value(rdbPm70.getText().toString());
            } else if (rdbPmSil.isChecked()) {
                endFix += "&variation_1=" + API.utf8Value(rdbPmSil.getText().toString());
            }else if (rdbPm60.isChecked()) {
                endFix += "&variation_1=" + API.utf8Value(rdbPm60.getText().toString());
            }
            if (viewV2Vity.isShown())
                endFix += "&variation_2=" + API.utf8Value(tabVity.getTabAt(tabVity.getSelectedTabPosition()).getText().toString());
        }

        if (layoutOfVariation3.isShown()) {
            //endFix += "&variation_1=" + API.utf8Value(edtStamp.getText().toString());
            //endFix += "&variation_2=" + API.utf8Value(edtTouch.getText().toString());
            if (viewV3Type.isShown())
                endFix += "&variation_3=" + API.utf8Value(tabTypes.getTabAt(tabTypes.getSelectedTabPosition()).getText().toString());
            if (viewV3Vity.isShown())
                endFix += "&variation_4=" + API.utf8Value(tabVityV3.getTabAt(tabVityV3.getSelectedTabPosition()).getText().toString());
        }

        if(layoutOfVariation4.isShown()){
            endFix += "&variation_3=" + API.utf8Value(tabGOPV4.getTabAt(tabGOPV4.getSelectedTabPosition()).getText().toString());
        }

        if(layoutOfVariation5.isShown()){
            endFix += "&variation_1=" + API.utf8Value(tabSizeSelection.getTabAt(tabSizeSelection.getSelectedTabPosition()).getText().toString());
        }


        endFix += "&notes=" + API.utf8Value(edtNote.getText().toString());
        if (cartId != 0)
            endFix += "&cv_id=" + cartId;

        url += endFix;

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        if (cartId == 0)
            progressDialog.setMessage("Adding to cart...");
        else progressDialog.setMessage("Updating cart...");
        progressDialog.show();
        baseActivity.requestAPI(url, new BaseActivity.RequestApiInterface() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt(API.Helper.STATUS) == API.SUCCESS) {
                        btnAddToCart.setVisibility(View.GONE);
                        btnAlreadyInCart.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString(API.Helper.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getActivity(), "Failed to add product to cart, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}