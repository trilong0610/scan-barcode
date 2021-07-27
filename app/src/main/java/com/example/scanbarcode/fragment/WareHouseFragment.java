package com.example.scanbarcode.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scanbarcode.R;
import com.example.scanbarcode.data.Data;
import com.example.scanbarcode.model.ProductAdapter;

import static com.example.scanbarcode.data.Data.products;



public class WareHouseFragment extends Fragment {

    public static   RecyclerView rvProduct;
    public static   ProductAdapter adapter;
    public  GridLayoutManager gridLayoutManager;

    Data data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_ware_house,container, false);
        setHasOptionsMenu(true);
        Mapping(view);

        return view;

    }

    private void Mapping(View view) {
        rvProduct = view.findViewById(R.id.rv_warehouse_product);
//        searchWarehouse = view.findViewById(R.id.search_warehouse);
        data = new Data();
        gridLayoutManager = new GridLayoutManager(getContext(),1);


        //        Đảo ngược chiều thêm item từ dưới lên
        gridLayoutManager.setReverseLayout(true);

//        gridLayoutManager.setStackFromEnd(true);
//        rvUsers.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvProduct.setLayoutManager(gridLayoutManager);

        products = data.loadProductFromDB();

        adapter = new ProductAdapter(products, getContext());

        rvProduct.setAdapter(adapter);

        rvProduct.scrollToPosition(products.size() - 1);




    }


}