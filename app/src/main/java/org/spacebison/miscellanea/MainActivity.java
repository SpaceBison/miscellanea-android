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

        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);

        Button mpButton = new Button(this);
        mpButton.setText("Media Player");
        mpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StatefulMediaPlayerTestActivity.class));
            }
        });
        content.addView(mpButton);
    }
}
