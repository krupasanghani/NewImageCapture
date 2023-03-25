package com.example.multiplecamera;

import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiplecamera.utils.CameraPreview;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaActionSound sound = new MediaActionSound();
    private FrameLayout frameLayout;
    private Camera mCamera;
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();
            try {
                FileOutputStream fos = new FileOutputStream(picture_file);
                sound.play(MediaActionSound.SHUTTER_CLICK);
                fos.write(data);
                fos.close();
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private CameraPreview cameraPreview;
    private int currentCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame_layout);

        XXPermissions.with(this)
                .permission(Permission.CAMERA)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            toast("获取部分权限成功，但部分权限未正常授予");
                            return;
                        }
                        toast("获取录音和日历权限成功");

                        File folder_ui = new File(Environment.getExternalStorageDirectory() + File.separator + "Camera App");

                        if (folder_ui.exists() && folder_ui.isDirectory()) {
                            String[] children = folder_ui.list();

                            System.out.println("folder_ui.list() : " + folder_ui.list());
                            for (int i = 0; i < children.length; i++) {
                                new File(folder_ui, children[i]).delete();
                            }
                        }
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            toast("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            toast("获取录音和日历权限失败");
                        }
                    }
                });

//        Permission.checkAndRequestPermissions(this);
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCamera = getCameraInstance(currentCameraId);
        cameraPreview = new CameraPreview(this, mCamera);
        frameLayout.addView(cameraPreview);
    }

    private Camera getCameraInstance(int currentCameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(currentCameraId);
        } catch (Exception e) {
        }
        return camera;
    }

    public void rotateCamera(View view) {
        mCamera.stopPreview();
        cameraPreview.getHolder().removeCallback(cameraPreview);
        mCamera.release();

        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        else
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

        mCamera = getCameraInstance(currentCameraId);
        cameraPreview = new CameraPreview(MainActivity.this, mCamera);
        frameLayout.removeAllViews();
        frameLayout.addView(cameraPreview);
    }

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED))
            return null;
        else {
            File folder_ui = new File(Environment.getExternalStorageDirectory() + File.separator + "Camera App");

            if (!folder_ui.exists()) {
                folder_ui.mkdirs();
            }

            File outputFile = new File(folder_ui, System.currentTimeMillis() + "_image.jpg");
            return outputFile;
        }
    }

    public void captureImage(View view) {
        if (mCamera != null) {
            mCamera.startPreview();
            mCamera.takePicture(null, null, mPictureCallback);
        }
    }

    public void openGallery(View view) {
        startActivity(new Intent(this, GalleryActivity.class));
    }
}