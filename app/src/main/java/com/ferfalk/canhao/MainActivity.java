package com.ferfalk.canhao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btJogarOnClick(View v) {
        Intent it = new Intent(this, GameActivity.class);
        startActivity(it);
    }

    public void btCreditosOnClick(View v) {
        Intent it = new Intent(this, CreditosActivity.class);
        startActivity(it);
    }

    public void btComoJogarOnClick(View v) {
        Intent it = new Intent(this, ComoJogarActivity.class);
        startActivity(it);
    }

    public void btSairOnClick(View v) {
        finish();
    }
}
