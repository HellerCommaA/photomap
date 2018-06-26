package android.heller.photo.photomap.activities;

import android.heller.photo.photomap.R;
import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.database.PhotoLocation;
import android.heller.photo.photomap.models.LocationModel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;
import java.util.UUID;

public class MarkerClickActivity extends AppCompatActivity {

    private final String TAG = MarkerClickActivity.class.getSimpleName();
    LocationModel mModel;
    Marker mMarker;
    TextView mLat;
    TextView mLong;
    TextView mName;
    Button mDeleteButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        mModel = LocationModel.getInstance();
        UUID uuid = UUID.fromString(getIntent().getStringExtra(PhotoMap.MARKER_EXTRA));
        mMarker = mModel.getMarker(uuid);
        mLat = findViewById(R.id.lat_text);
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
                    mMarker.remove();
                    MarkerClickActivity.this.finish();
                } else {
                    Log.d(TAG, "MarkerClickActivity.onCreate.onClick$run: attempt to remove marker in background thread failed. marker == null.");
                }
            }
        });
    }
}
