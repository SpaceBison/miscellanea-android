package org.spacebison.permissionrequestactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.spacebison.R;

public class PermissionRequestActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS = PermissionRequestActivity.class.getPackage().getName() + "EXTRA_PERMISSIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            finish();
            return;
        }


    }
}
