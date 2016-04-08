package org.spacebison.miscellanea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewGroup root = (ViewGroup) findViewById(R.id.root);

        Button mpButton = new Button(this);
        mpButton.setText("Media Player");
        mpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatefulMediaPlayerTestActivity.class));
            }
        });
        root.addView(mpButton);

        Button tpButton = new Button(this);
        tpButton.setText("Media Player");
        tpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TaskProgressBarTestActivity.class));
            }
        });
        root.addView(tpButton);
    }
}
