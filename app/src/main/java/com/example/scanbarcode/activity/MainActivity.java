package com.example.scanbarcode.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import com.example.scanbarcode.R;
import com.example.scanbarcode.data.Data;
import com.example.scanbarcode.model.Product;
import com.example.scanbarcode.model.ProductAdapter;
import com.example.scanbarcode.model.ProductModel;
import com.example.scanbarcode.fragment.ScanFragment;
import com.example.scanbarcode.fragment.WareHouseFragment;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import static com.example.scanbarcode.data.Data.products;
import static com.example.scanbarcode.fragment.WareHouseFragment.adapter;
import static com.example.scanbarcode.fragment.WareHouseFragment.rvProduct;

public class MainActivity extends AppCompatActivity{
    private View mainContainer;

    private ProductModel productModel;

    private BottomAppBar bottomAppBar;

    private NavigationView navigationView;

    private CoordinatorLayout coordinatorLayoutMain;

    private BottomSheetBehavior bottomSheetBehavior;

    private SearchView searchView;

    private Data data;

    private FloatingActionButton btnScan;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();

        setSupportActionBar(bottomAppBar);

        handleIntent(getIntent());

        MappingEvent();


        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            System.out.println(query);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Đã được cấp quyền Camera",
                    Toast.LENGTH_SHORT)
                    .show();
        }else{
            Toast.makeText(this, "Từ chối cấp quyền sử dụng Camera !",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void Mapping() {
        mainContainer = findViewById(R.id.main_container);

        navigationView = findViewById(R.id.navigationView_activity_main);

        data = new Data();



        bottomSheetBehavior = BottomSheetBehavior.from(navigationView);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        productModel = new ProductModel();

        bottomAppBar = findViewById(R.id.bottomAppBar);

        btnScan = findViewById(R.id.btn_activity_main_scan);

        coordinatorLayoutMain = findViewById(R.id.coordinatorLayout_main);


//        An Layout sub

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new WareHouseFragment()).commit();

    }

    private void MappingEvent(){
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanFragment scanFragment = new ScanFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,scanFragment).commit();
            }
        });

//       Event cho menu drawer
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bottomAppBar.getFabAlignmentMode() != BottomAppBar.FAB_ALIGNMENT_MODE_END)
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                else
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    navigationView.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }
                else{

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    navigationView.setVisibility(View.GONE);

                }
            }
        });

//        Event item menu
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


//                Thay doi vi tri btn Scan ve End khi nhan btn Search
                if (item.getItemId() == R.id.mnu_item_search){
                    Log.i("itemClick", String.valueOf(item.getItemId()));
                    Log.i("itemClick", String.valueOf(R.id.mnu_item_search));

                    if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.mnu_item_warehouse:
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        fragment = new WareHouseFragment();
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();

                return false;
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.mnu_item_search);
        Log.i("item", "True");
        final SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                ArrayList<Product> searchProducts = searchIdAndUrl(queryText);
                ProductAdapter productAdapter = new ProductAdapter(searchProducts, MainActivity.this);
                rvProduct.setAdapter(productAdapter);
                rvProduct.scrollToPosition(products.size() - 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String newFilter = !TextUtils.isEmpty(newText) ? newText : null;

                return true;
            }
        });
        
        searchView.setSubmitButtonEnabled(true);


        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                Thay doi vi tri btn Scan ve Center
                if (bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER)
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
//              Gan lai data goc cho recycleview
                rvProduct.setAdapter(adapter);
                rvProduct.scrollToPosition(products.size() - 1);
                return true;
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                rvProduct.setAdapter(adapter);
                rvProduct.scrollToPosition(products.size() - 1);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println("Search Clicked");
        switch (item.getItemId()){
            case R.id.mnu_item_search:

        }
        return super.onOptionsItemSelected(item);
    }


    private ArrayList<Product> searchIdAndUrl(String keySearch){

        ArrayList<Product> resultSearch = new ArrayList<>();
        for (Product product : products){
            if (product.getName().trim().contains(keySearch) || product.getId().trim().contains(keySearch)){
                resultSearch.add(product);
            }
        }

        return resultSearch;

    }
}

