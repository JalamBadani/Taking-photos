package com.badani.takephoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;


import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.btn_camera)).setOnClickListener(this);
        ((Button) findViewById(R.id.btn_gallery)).setOnClickListener(this);
        ivImage = findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                openCamera();
                break;
            case R.id.btn_gallery:
                openGallery();
                break;
        }
    }

    private void openCamera()
    {
        EasyImage.openCamera(MainActivity.this, REQUEST_CODE_CAMERA);
    }

    private void openGallery() {
        EasyImage.openGallery(MainActivity.this, REQUEST_CODE_GALLERY);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }
            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                onPhotosReturned(imageFiles);
            }


        });
    }



    private void onPhotosReturned(List<File> returnedPhotos) {

        Glide.with(MainActivity.this)
                .load(returnedPhotos.get(0))
                .into(ivImage);
    }

}
