package com.ferfalk.canhao;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ComoJogarActivity extends AppCompatActivity {

    TextView tvComoJogar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_como_jogar);

        tvComoJogar1 = (TextView) findViewById(R.id.tvComoJogar1);
        Resources res = getResources();
        String s1 = res.getString(R.string.app_name);
        String texto = res.getString(R.string.como_jogar_intro, s1);
        tvComoJogar1.setText(texto);
    }

    public void btVoltarOnClick(View v) {
        finish();
    }

}
