package com.example.scanbarcode.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.scanbarcode.R;
import com.example.scanbarcode.activity.UpdateBarcodeActivity;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemViewHolder> implements Filterable {
    private ArrayList<Product> products;
    private ArrayList<Product> oldproducts;
    private Context context;
//    private static ItemClickListener mClickListener;

    public ProductAdapter(ArrayList<Product> products, Context c) {
        this.products = products;
        oldproducts = products;
        this.context = c;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public ProductItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        parent.scrollTo(0, 0);
        return new ProductItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductItemViewHolder holder, int position) {
        Product product = products.get(position);
        String urlImg = "https://dongylamdep.com/img/Product/Small/" + product.urlImg;
//        Gan data
        Picasso.with(context)
                .load(urlImg)
                .into(holder.ivImg);

        if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE){
            holder.flipView.flipTheView();
        }
        holder.tvName.setText(product.name);
        holder.tvAmount.setText(String.valueOf(product.amount));
        holder.tvBarcode.setText(product.getBarCode());

        holder.flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.flipView.flipTheView();
            }
        });

        holder.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateBarcodeActivity.class);
                intent.putExtra("id", product.getId());
                context.startActivity(intent);
            }
        });

        holder.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.flipView.flipTheView();
            }
        });

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    products = oldproducts;
                }
                else {
                    ArrayList<Product> list = new ArrayList<>();
                    for (Product item :
                            oldproducts) {
                        if (item.getName().trim().contains(charSequence.toString())){
                            list.add(item);
                        }
                    }
                    products = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = products;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                products = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ProductItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        Front item
        private ImageView ivImg;
        private ImageView ivBarcode;

        private TextView tvName;
        private TextView tvAmount;
        private TextView tvBarcode;
        private EasyFlipView flipView;
//        Back item
        private MaterialButton btnScan;
        private MaterialButton btnExit;



        //         Luu mau Items
        @SuppressLint("WrongViewCast")
        public ProductItemViewHolder(View itemView) {
            super(itemView);
//            Front item
            ivImg = itemView.findViewById(R.id.iv_front_product_item_img);

            tvName = itemView.findViewById(R.id.tv_front_product_item_name);
            tvAmount = itemView.findViewById(R.id.tv_front_product_item_amount);
            tvBarcode = itemView.findViewById(R.id.tv_front_product_item_barcode);
            flipView = itemView.findViewById(R.id.flip_item_product);
//            xxx = items.
//            Back item
            btnScan = itemView.findViewById(R.id.btn_back_item_scan);
            btnExit = itemView.findViewById(R.id.btn_back_item_exit);



//            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            flipView.flipTheView();
        }
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }


}
