package org.spacebison.miscellanea;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewGroup mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = (ViewGroup) findViewById(R.id.root);

        addActivityButton(StatefulMediaPlayerTestActivity.class);
        addActivityButton(TaskProgressBarTestActivity.class);
        addActivityButton(ProgressBarActivity.class);
    }

    private void addActivityButton(final Class<?> activityClass) {
        final String name = activityClass.getSimpleName();
        Log.d(TAG, "Adding activity button: " + name);
        Button button = new Button(this);
        button.setText(name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, activityClass));
            }
        });
        mRoot.addView(button);
    }

}
