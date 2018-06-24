package android.heller.photo.photomap.Activities;

import android.heller.photo.photomap.R;
import android.heller.photo.photomap.models.LocationModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Locale;

public class MarkerClickActivity extends AppCompatActivity {

    private final String TAG = MarkerClickActivity.class.getSimpleName();

    Button mDeleteButton;
    TextView mName;
    TextView mLat;
    TextView mLong;
    LatLng mPosition;
    Marker mMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        mPosition = getIntent().getParcelableExtra(PhotoMap.LAT_EXTRA);
        mMarker = LocationModel.getInstance().getMarker(mPosition);
        mLat = findViewById(R.id.lat_text);
        mLong = findViewById(R.id.long_text);
        mLat.setText(String.format(Locale.US, "%f", mPosition.latitude));
        mLong.setText(String.format(Locale.US, "%f", mPosition.longitude));
        mName = findViewById(R.id.marker_name);
        mName.setText("test");
        mDeleteButton = findViewById(R.id.marker_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarker != null) mMarker.remove();
                MarkerClickActivity.this.finish();
            }
        });
    }
}
