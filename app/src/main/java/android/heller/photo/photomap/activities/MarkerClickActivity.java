package android.heller.photo.photomap.activities;

import android.app.Activity;
import android.content.Intent;
import android.heller.photo.photomap.R;
import android.heller.photo.photomap.models.LocationModel;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.Locale;

public class MarkerClickActivity extends AppCompatActivity {

    private final String TAG = MarkerClickActivity.class.getSimpleName();
    LocationModel mModel;
    Marker mMarker;
    TextView mLat;
    TextView mLong;
    TextView mName;
    Button mDeleteButton;
    public static final String DELETE_RESULT = "android.heller.photo.DELETE_RESULT";
    public static final String UUID_RESULT = "android.heller.photo.UUID_RESULT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click);
        mModel = LocationModel.getInstance();
        String uuid = getIntent().getStringExtra(PhotoMap.MARKER_EXTRA);
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
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(DELETE_RESULT, true);
                    returnIntent.putExtra(UUID_RESULT, (String) mMarker.getTag());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } else {
                    Log.d(TAG, "onClick: ClickActivity mMarker == NULL");
                }
            }
        });
    }
}
