package android.heller.photo.photomap.activities;

import android.graphics.Bitmap;
import android.heller.photo.photomap.R;
import android.heller.photo.photomap.models.LocationModel;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class MarkerClickActivity extends AppCompatActivity implements CameraKitEventListener {

    private final String TAG = MarkerClickActivity.class.getSimpleName();
    LocationModel mModel;
    Marker mMarker;
    TextView mLat;
    TextView mLong;
    TextView mName;
    Button mDeleteButton;
    CameraView mCamera;
    ImageButton mTakePhotoButton;
    String mUuid;

    @Override
    protected void onResume() {
        super.onResume();
        mCamera.start();
    }

    @Override
    protected void onPause() {
        mCamera.stop();
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        mModel = LocationModel.getInstance();
        String uuid = getIntent().getStringExtra(PhotoMap.MARKER_EXTRA);
        mMarker = mModel.getMarker(uuid);
        mLat = findViewById(R.id.lat_text);
        mUuid = (String) mMarker.getTag();
        mCamera = findViewById(R.id.camera_surface);
        mCamera.addCameraKitListener(this);
        mTakePhotoButton = findViewById(R.id.takePhotoButton);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {
                    Log.d(TAG, "onClick: AEH capturing image");
                    mCamera.captureImage();
                }
            }
        });
        mLong = findViewById(R.id.long_text);
        mLat.setText(String.format(Locale.US, "%f", mMarker.getPosition().latitude));
        mLong.setText(String.format(Locale.US, "%f", mMarker.getPosition().longitude));
        mName = findViewById(R.id.marker_name);
        mName.setText((mMarker.getTitle() == null ? "" : mMarker.getTitle()));
        mDeleteButton = findViewById(R.id.marker_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarker != null) {
                    LocationModel model = LocationModel.getInstance();
                    model.removeMarkerFromMap(mMarker);
                    finish();
                } else {
                    Log.d(TAG, "onClick: ClickActivity mMarker == NULL");
                }
            }
        });
    }

    @Override
    public void onEvent(CameraKitEvent cameraKitEvent) {

    }

    @Override
    public void onError(CameraKitError cameraKitError) {

    }

    @Override
    public void onImage(CameraKitImage cameraKitImage) {
        storeImage(cameraKitImage.getBitmap());
    }

    @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {

    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = mUuid + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}
