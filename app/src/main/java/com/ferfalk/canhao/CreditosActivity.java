package com.ferfalk.canhao;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CreditosActivity extends AppCompatActivity {

    TextView tvCreditos2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        tvCreditos2 = (TextView) findViewById(R.id.tvCreditos2);
        Resources res = getResources();
        String s1 = res.getString(R.string.app_name);
        String creditos = res.getString(R.string.creditos2, s1);
        tvCreditos2.setText(creditos);
    }

    public void btVoltarOnClick(View v) {
        finish();
    }
}
