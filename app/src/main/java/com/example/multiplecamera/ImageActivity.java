package com.example.multiplecamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Collections;

public class ImageActivity extends AppCompatActivity {
    private ImageView imageView;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        imgFile = (File) getIntent().getExtras().get("img");
        imageView = findViewById(R.id.imageView);
        Glide.with(this).load(imgFile).into(imageView);
    }
}
