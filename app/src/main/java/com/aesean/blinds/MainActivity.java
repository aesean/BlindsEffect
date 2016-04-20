package com.aesean.blinds;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.aesean.blinds.lib.BlindsEffect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                BlindsEffect blindsEffect = new BlindsEffect((LinearLayout) findViewById(R.id.ll_blind), R.drawable.lry, R.drawable.lry2);
                blindsEffect.start();
//                BlindsEffect blindsEffect = new BlindsEffect((LinearLayout) findViewById(R.id.ll_blind), findViewById(R.id.iv_0), findViewById(R.id.iv_1));
//                blindsEffect.start();
            }
        });
    }

}
