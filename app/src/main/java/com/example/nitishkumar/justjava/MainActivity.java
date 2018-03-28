package com.example.nitishkumar.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    int quantity = 1, basePrice = 5;
    CheckBox whippedCreamCheckBox;
    CheckBox chocolateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream stream = null;
        try {
            stream = getAssets().open("@drawable/coffee4.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        GifWebView view = new GifWebView(this, "@drawable/coffee3");
//        setContentView(view);

        whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_Cream_Check_Box);
        whippedCreamCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                displayPrice(quantity);
            }
        });

        chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_Check_Box);
        chocolateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                displayPrice(quantity);
            }
        });
    }

    private String createOrderSummary(String uName, int price, boolean addWhipedCream, boolean addChocolate) {

        String priceMessage = getString(R.string.order_summary_name, uName);
        priceMessage += "\n" + getString(R.string.whippedCreamadd, addWhipedCream);
        priceMessage += "\n" + getString(R.string.chocolateadd, addChocolate);
        priceMessage += "\n" + getString(R.string.quantity, quantity);
        priceMessage += "\n" + getString(R.string.total, price);
        priceMessage += "\n" + getString(R.string.thankyoumsg);
        return priceMessage;
    }


    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public boolean whippedCreamCheckBoxStatus() {
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        return hasWhippedCream;
    }

    public boolean chocolateCheckBoxStatus() {
        boolean hasChocolate = chocolateCheckBox.isChecked();
        return hasChocolate;
    }


    public int calculateTotalPrice(int numberOfCups) {
        int totalPrice = basePrice;
        if (whippedCreamCheckBoxStatus()) {
            totalPrice += 1;
        }
        if (chocolateCheckBoxStatus()) {
            totalPrice += 2;
        }

        return totalPrice * numberOfCups;

    }

    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(calculateTotalPrice(number)));
    }

    public void increment(View view) {
        if (quantity == 100) {
            return;
        }
        quantity = quantity + 1;
        display(quantity);
        displayPrice(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            return;
        }
        quantity = quantity - 1;
        display(quantity);
        displayPrice(quantity);
    }

    public void submitOrder(View view) {
        EditText user_name = (EditText) findViewById(R.id.nameOfUser);
        String name = user_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            user_name.setError("It can't be Empty, Please fill it!!!");
            return;
        } else {

            String priceMessage = createOrderSummary(name, calculateTotalPrice(quantity), whippedCreamCheckBoxStatus(), chocolateCheckBoxStatus());

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + "njha6184@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee Order For : " + name);
            intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
        //displayOrderSummary(priceMessage);
    }
}