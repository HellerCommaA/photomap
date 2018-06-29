package android.heller.photo.photomap.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.heller.photo.photomap.R;
import android.heller.photo.photomap.models.LocationModel;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.OutputStream;
import java.util.Locale;

public class MarkerClickActivity extends AppCompatActivity implements View.OnClickListener, CameraKitEventListener {

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
        mTakePhotoButton = findViewById(R.id.takePhotoButton);
        mTakePhotoButton.setOnClickListener(this);
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
    public void onClick(View view) {
        if (mCamera != null) {
            Log.d(TAG, "onClick: AEH capturing image");
            mCamera.captureImage();
        }
    }

    @Override
    public void onEvent(CameraKitEvent cameraKitEvent) {

    }

    @Override
    public void onError(CameraKitError cameraKitError) {

    }

    @Override
    public void onImage(CameraKitImage cameraKitImage) {
        try {
            Bitmap bitmap = cameraKitImage.getBitmap();
            String path = Environment.getExternalStorageDirectory().toString();
            Log.d(TAG, "onImage: AEH PATH: " + path);
            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, mUuid + ".jpg");
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVideo(CameraKitVideo cameraKitVideo) {

    }
}
