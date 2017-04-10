package com.task.vasskob.firebase.ui;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.service.MyUploadService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadFileFromStorage extends BaseActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = LoadFileFromStorage.class.getSimpleName();

    @OnClick(R.id.upload_file_btn)
    public void onUploadBtnClick() {
        showFileChooser();
    }

    @OnClick(R.id.accelerometer_btn)
    public void onAccelerometerBtnClick() {
        startActivity(new Intent(LoadFileFromStorage.this, MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_file);
        ButterKnife.bind(this);

    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

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
        Uri uri;
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {

                    if (data != null) {
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                uri = item.getUri();
                                uploadFromUri(uri);
                            }
                        } else {
                            uri = data.getData();
                            uploadFromUri(uri);
                        }
                    }
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
