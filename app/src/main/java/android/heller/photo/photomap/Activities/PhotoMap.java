package android.heller.photo.photomap.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.heller.photo.photomap.R;
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

public class PhotoMap extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private final String TAG = PhotoMap.class.getSimpleName();
    private GoogleMap mMap;
    boolean mPermissionsGranted = false;
    LocationManager lm;
    final int ZOOM_LEVEL = 15;

    public static final String LAT_EXTRA = "android.heller.photo.LAT_EXTRA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mPermissionsGranted = true;
        } else {
            mPermissionsGranted = false;
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap xMap) {
        mMap = xMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "onMarkerClick: marker.name: " + marker.getId());
                Intent i = new Intent(PhotoMap.this, MarkerClickActivity.class);
                i.putExtra(LAT_EXTRA, marker.getPosition());
                PhotoMap.this.startActivity(i);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                LocationModel.getInstance().addLocation(marker.getPosition(), marker);
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: override onPermissionsGranted thing
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 255);
            return;
        }
        mMap.setMyLocationEnabled(mPermissionsGranted);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), ZOOM_LEVEL));
    }

    @Override
    public void onMyLocationClick(@NonNull Location xLocation) {
        Log.d(TAG, "onMyLocationClick: ");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(xLocation.getLatitude(), xLocation.getLongitude()), ZOOM_LEVEL));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.d(TAG, "onMyLocationButtonClick: ");
        return false;
    }
}