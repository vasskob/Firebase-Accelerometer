package com.task.vasskob.firebase.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.service.MyUploadService;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoadFileFromStorage extends BaseActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = LoadFileFromStorage.class.getSimpleName();

    @Bind(R.id.upload_file_btn)
    Button uploadFileBtn;
    @Bind(R.id.accelerometer_btn)
    Button accelerometerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_file);
        ButterKnife.bind(this);

        uploadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        accelerometerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoadFileFromStorage.this, MainActivity.class));
            }
        });
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Log.d(TAG, "Catch there is no path ");
                    }
                    Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload

                    uploadFromUri(uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFromUri(Uri uri) {
        startService(new Intent(this, MyUploadService.class)
                .putExtra(MyUploadService.EXTRA_FILE_URI, uri)
                .setAction(MyUploadService.ACTION_UPLOAD));
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseOperations.logout();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
