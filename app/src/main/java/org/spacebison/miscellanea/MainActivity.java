package org.spacebison.miscellanea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = (ViewGroup) findViewById(R.id.root);

        addActivityButton(StatefulMediaPlayerTestActivity.class);
        addActivityButton(TaskProgressBarTestActivity.class);
    }

    private void addActivityButton(final Class<?> activityClass) {
        Button mpButton = new Button(this);
        mpButton.setText(activityClass.getSimpleName());
        mpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, activityClass));
            }
        });
        mRoot.addView(mpButton);
    }

}
