package android.heller.photo.photomap.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.heller.photo.photomap.R;
import android.heller.photo.photomap.database.AppDatabase;
import android.heller.photo.photomap.database.PhotoLocation;
import android.heller.photo.photomap.models.LocationModel;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PhotoMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMapClickListener {
    private final String TAG = PhotoMap.class.getSimpleName();
    private GoogleMap mMap;
    boolean mPermissionsGranted = false;
    LocationManager lm;
    final int ZOOM_LEVEL = 15;
    private LocationModel mLocationModel;
    List<Marker> mMarkers;
    public static final String MARKER_EXTRA = "android.heller.photo.MARKER_EXTRA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            mPermissionsGranted = true;
        } else {
            mPermissionsGranted = false;
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFrag.getMapAsync(this);
        LocationModel.setContext(getApplicationContext());
        mLocationModel = LocationModel.getInstance();
        mMarkers = new LinkedList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: AEH ON RESUME");
        if (mMap != null) {
            mMap.clear();
            loadPoints();
        }
    }

    // call onResume and onMapReady
    // to load things from database -> map
    void loadPoints(){
        if (mLocationModel == null) {
            mLocationModel = LocationModel.getInstance();
        }
        List<PhotoLocation> locationList = mLocationModel.loadSavedLocations(mMap);

        mLocationModel = LocationModel.getInstance();

        if (locationList != null && locationList.size() > 0) {
            for(PhotoLocation data : locationList) {
                MarkerOptions opt = new MarkerOptions();
                opt.position(new LatLng(data.lat, data.lon));
                opt.title(data.name);
                Marker m = mMap.addMarker(opt);
                Log.d(TAG, "onMapReady: adding id to map " + data.id);
                m.setTag(data.id);
                mMarkers.add(m);
            }
        } else {
            Log.d(TAG, "onMapReady: aeh locationData == null | 0");
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if(grantResults[i] == PERMISSION_GRANTED) {
                    postPermissions(true);
                    return;
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void postPermissions(boolean xPermissions) {
        mMap.setMyLocationEnabled(xPermissions);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), ZOOM_LEVEL));
    }

    @Override
    public void onMapReady(GoogleMap xMap) {
        mMap = xMap;
        loadPoints();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);
        } else {
            postPermissions(mPermissionsGranted);
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location xLocation) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(xLocation.getLatitude(), xLocation.getLongitude()), ZOOM_LEVEL));
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent i = new Intent(PhotoMap.this, MarkerClickActivity.class);
        Log.d(TAG, "onMarkerClick: marker.getTag() == " + marker.getTag());
        i.putExtra(MARKER_EXTRA, (String) marker.getTag());
        PhotoMap.this.startActivity(i);
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
        marker.setTag(UUID.randomUUID().toString());
        mLocationModel.addMarker(marker);
        mMarkers.add(marker);
    }
}
