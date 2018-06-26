package android.heller.photo.photomap.activities;

import android.heller.photo.photomap.R;
import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.models.LocationModel;
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

    Button mDeleteButton;
    TextView mName;
    TextView mLat;
    TextView mLong;
    Marker mMarker;
    UUID mUuid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        Log.d(TAG, "onCreate: AEH STRING EXTRA: " + getIntent().getStringExtra(PhotoMap.MARKER_EXTRA));
        mUuid = UUID.fromString(getIntent().getStringExtra(PhotoMap.MARKER_EXTRA));
        Log.d(TAG, "onCreate: AEH mUuid: " + mUuid.toString());
        LocationModel model = LocationModel.getInstance();
        mMarker = model.getMarker(mUuid);
        mLat = findViewById(R.id.lat_text);
        mLong = findViewById(R.id.long_text);
        mLat.setText(String.format(Locale.US, "%f", mMarker.getPosition().latitude));
        mLong.setText(String.format(Locale.US, "%f", mMarker.getPosition().longitude));
        mName = findViewById(R.id.marker_name);
        mName.setText("test");
        mDeleteButton = findViewById(R.id.marker_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarker != null) {
                    AppDatabase md = AppDatabase.getDatabase(MarkerClickActivity.this);
                    md.locationModel().delete(mMarker.getId());
                    mMarker.remove();
                    MarkerClickActivity.this.finish();
                }
            }
        });
    }
}
