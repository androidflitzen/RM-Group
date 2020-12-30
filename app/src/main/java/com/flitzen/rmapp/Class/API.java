package com.flitzen.rmapp.Class;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class API {
    public static int TIMEOUT_LIMIT = 120000;
    public static int SUCCESS = 1;
    public static int FAIL = 0;

    private static String baseUrl = "https://rmsilver.com/apis/";
    public static String Login = baseUrl + "users/login_user?email=%s&password=%s";
    public static String SignUp = baseUrl + "users/signup_user?name=%s&email=%s&phone_no=%s&password=%s";

    public static String CategoryData = baseUrl + "products/all_category?user_id=%s";
    public static String ProductsData = baseUrl + "products/category_product?user_id=%s&c_id=%s";

    public static String AddToCart = baseUrl + "invoice/add_to_cart?";
    public static String UpdateToCart = baseUrl + "invoice/update_cart?";
    public static String ProfileInfo = baseUrl + "customer/my_profile?user_id=%s";
    public static String CartInfo = baseUrl + "invoice/my_cart?user_id=%s";
    public static String RemoveItemFromCart = baseUrl + "invoice/remove_from_cart?cv_id=%s";
    public static String ClearCart = baseUrl + "invoice/remove_all?user_id=%s";
    public static String CheckOut = baseUrl + "invoice/checkout?user_id=%s";
    public static String FavoriteList = baseUrl + "products/all_favourite?user_id=%s";
    public static String FavoriteItem = baseUrl + "products/fav_unfav_category?user_id=%s&cat_id=%s";
    public static String OrderList = baseUrl + "invoice/my_receipt?user_id=%s";
    public static String UpdateProfile = baseUrl + "customer/update_profile?user_id=%s&customer_name=%s&phone_number=%s";

    public static class Helper {
        public static String STATUS = "status";
        public static String MESSAGE = "message";
    }

    public static String utf8Value(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
