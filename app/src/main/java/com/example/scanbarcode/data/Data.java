package com.example.scanbarcode.data;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.scanbarcode.model.Product;
import com.example.scanbarcode.model.ProductAdapter;
import com.example.scanbarcode.model.ProductModel;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.example.scanbarcode.fragment.WareHouseFragment.adapter;
import static com.example.scanbarcode.fragment.WareHouseFragment.rvProduct;

public class Data {
    public static ArrayList<Product> products = new ArrayList<>();
    public ProductModel productModel = new ProductModel();



    public void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public ArrayList<Product> loadProductFromDB() {
        try {
            return new ProductModel().getuserlist();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void reloadData(Context context){
        products = loadProductFromDB();
        adapter = new ProductAdapter(products, context);
        rvProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        rvProduct.scrollToPosition(products.size() - 1);
    }
}
