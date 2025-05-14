package com.example.mobilecomputingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        // navigation bar
        ImageButton profile = findViewById(R.id.profileBtn);
        ImageButton settings = findViewById(R.id.settingsBtn);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        // end of navigation bar


        // add dummy data for gridView (fake products)
        List<Product> productList = new ArrayList<>();

        productList.add(new Product(R.mipmap.apple_product, "Apple Phone", "Rating: 5"));
        productList.add(new Product(R.mipmap.apple_product, "Apple", "Rating: 3"));
        productList.add(new Product(R.mipmap.apple_product, "Fresh Apple", "Rating: 4"));
        productList.add(new Product(R.mipmap.apple_product, "Wormy Apple", "Rating: 1"));
        productList.add(new Product(R.mipmap.apple_product, "Stolen Apple Phone", "Rating: 2"));

        //add to the gridview
        ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_item, productList);
        GridView gridView = findViewById(R.id.productList);
        gridView.setAdapter(productAdapter);
    }

}