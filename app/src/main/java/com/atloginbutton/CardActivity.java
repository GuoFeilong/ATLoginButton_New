package com.atloginbutton;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import customview.ATCardItemViewGroup;

/**
 * Created by jsion on 16/7/21.
 */

public class CardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_item);
        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        ATCardItemViewGroup cardItemViewGroup = (ATCardItemViewGroup) findViewById(R.id.atg);
        ATCardItemViewGroup cardItemViewGroup1 = (ATCardItemViewGroup) findViewById(R.id.at_card_item);
        cardItemViewGroup.setCardSelectState(true);
        cardItemViewGroup1.setCardSelectState(true);

    }
}
