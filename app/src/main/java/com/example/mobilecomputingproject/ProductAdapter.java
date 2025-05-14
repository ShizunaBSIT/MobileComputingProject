package com.example.mobilecomputingproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.mobilecomputingproject.R;

import java.util.List;
public class ProductAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private int mResource;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        // views
        ImageView imageView = convertView.findViewById(R.id.productImage);
        TextView nameTextView = convertView.findViewById(R.id.productNameTxt);
        TextView ratingTextView = convertView.findViewById(R.id.productRatingTxt);
        Button orderBtn = convertView.findViewById(R.id.orderBtn);

        if (product != null) {
            imageView.setImageResource(product.getImageResource());
            nameTextView.setText(product.getName());
            ratingTextView.setText(product.getRating());


            // listener
            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, OrderActivity.class);

                    intent.putExtra("productName", product.getName());

                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

}
