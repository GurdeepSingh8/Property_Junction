package property.junction.co;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import property.junction.co.adapters.PlacesAutoCompleteAdapter;
import property.junction.co.methods.AsyncGeocoder;
import property.junction.co.methods.AsyncGeocoderObject;
import property.junction.co.methods.UniMethods;

public class GoogleMapAddress extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener {

    Activity activity = GoogleMapAddress.this;
    UniMethods uniMethods = new UniMethods();

    TextView tv_getLocation, tv_current_address_main, tv_edit_address, tv_address_title;
    TextInputLayout til_address_line_1, til_address_line_2;
    LottieAnimationView lottie_map;
    ConstraintLayout cons_current_location;
    LinearLayoutCompat ll_location, ll_bottom, ll_first_address;
    ImageView iv_crosshair, iv_location_mark, iv_icon_search, iv_cross;

    Context context = GoogleMapAddress.this;

    SupportMapFragment sf_google_map;
    GoogleMap google_map;
    FusedLocationProviderClient client;

    String combined_lat_long = "";
    String main_add = "";
    String sub_add = "";
    boolean is_manual_address_open = false;

    String address_line = "";
    int zoom = 17;

    Button btn_set_address;

    ProgressBar progress_bar;

    //AutocompleteSupportFragment autocompleteFragment;
    private com.google.android.gms.location.LocationRequest locationRequest;

    EditText et_search_address;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map_address);

        uniMethods.backButtonPanel(activity, activity.getString(R.string.choose_property_location));
        ll_location = findViewById(R.id.ll_location);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_first_address = findViewById(R.id.ll_first_address);
        progress_bar = findViewById(R.id.progress_bar);
        iv_crosshair = findViewById(R.id.iv_crosshair);
        iv_location_mark = findViewById(R.id.iv_location_mark);
        iv_icon_search = findViewById(R.id.iv_icon_search);
        iv_cross = findViewById(R.id.iv_cross);
        cons_current_location = findViewById(R.id.cons_current_location);
        recyclerView = findViewById(R.id.places_recycler_view);
        et_search_address = findViewById(R.id.et_search_address);
        tv_getLocation = findViewById(R.id.tv_getLocation);
        tv_current_address_main = findViewById(R.id.tv_current_address_main);
        tv_edit_address = findViewById(R.id.tv_edit_address);
        btn_set_address = findViewById(R.id.btn_set_address);
        lottie_map = findViewById(R.id.lottie_map);

        toggleCurrentLocationButton(false);

        client = LocationServices.getFusedLocationProviderClient(GoogleMapAddress.this);
        Places.initialize(this, "AIzaSyCyjKFgQyxCxQ4mzA608CJwQ-oQ7Kz3klY");

        et_search_address.addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();

        LayoutInflater inflater = (LayoutInflater) Objects.requireNonNull(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout cons = (ConstraintLayout) inflater.inflate(R.layout.support_fragment_google_map, null);
        tv_address_title = cons.findViewById(R.id.tv_address_title);
        ll_location.addView(cons);

        createGoogleMap();

        btn_set_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_set_address.setEnabled(false);
                Intent intent = new Intent();
                HashMap<String, Object> location_hm = new HashMap<>();
                location_hm.put("display_address", main_add + ", " + sub_add);
                location_hm.put("full_address", address_line);
                location_hm.put("lat_long", combined_lat_long);
                intent.putExtra("location", location_hm);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(LocationRequest.QUALITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setSmallestDisplacement(300f);
        locationRequest.setFastestInterval(1000);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_search_address.setText("");
                uniMethods.hideKeyboardFromActivity(GoogleMapAddress.this);
                uniMethods.hideKeyboardFromFragment(context, cons);
            }
        });

        cons_current_location.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                toggleCurrentLocationButton(true);
                uniMethods.hideKeyboardFromActivity(GoogleMapAddress.this);
                uniMethods.hideKeyboardFromFragment(context, cons);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getCurrentLocation();
                } else {
                    permissionRequest();
                }
            }
        });

        getLastLocation();

        tv_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_manual_address_open = true;
                uniMethods.hideKeyboardFromActivity(activity);
                iv_icon_search.setVisibility(View.GONE);
                iv_cross.setVisibility(View.GONE);
                et_search_address.setVisibility(View.GONE);
                cons_current_location.setVisibility(View.GONE);

                LinearLayoutCompat.LayoutParams params_manual_address = new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                ConstraintLayout view_manual_address = (ConstraintLayout) inflater.inflate(R.layout.view_manual_address, null);
                TextView tv_fill_manually_heading = view_manual_address.findViewById(R.id.tv_fill_manually_heading);
                til_address_line_1 = view_manual_address.findViewById(R.id.til_address_line_1);
                til_address_line_2 = view_manual_address.findViewById(R.id.til_address_line_2);
                Button btn_cancel_manual_address = view_manual_address.findViewById(R.id.btn_cancel_manual_address);
                Button btn_save_manual_address = view_manual_address.findViewById(R.id.btn_save_manual_address);
                view_manual_address.setLayoutParams(params_manual_address);
                tv_fill_manually_heading.setText(Html.fromHtml("<b><big>" + context.getString(R.string.fill_manually_heading) + "</big></b><br>" + context.getString(R.string.fill_manually_sub)));

                til_address_line_1.getEditText().setText(main_add);
                til_address_line_2.getEditText().setText(sub_add);

                til_address_line_1.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        tv_address_title.setText(charSequence.toString() + "\n" + til_address_line_2.getEditText().getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                til_address_line_2.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        tv_address_title.setText(til_address_line_1.getEditText().getText().toString() + "\n" + charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                ll_bottom.removeAllViews();
                ll_bottom.addView(view_manual_address);
                btn_cancel_manual_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_address_title.setText(main_add + "\n" + sub_add);
                        uniMethods.hideKeyboardFromActivity(activity);
                        ll_bottom.removeAllViews();
                        ll_bottom.addView(ll_first_address);
                        ll_bottom.addView(tv_edit_address);
                        ll_bottom.addView(btn_set_address);
                        is_manual_address_open = false;

                        cons_current_location.setVisibility(View.VISIBLE);
                        et_search_address.setVisibility(View.VISIBLE);
                        iv_cross.setVisibility(View.VISIBLE);
                        iv_icon_search.setVisibility(View.VISIBLE);
                    }
                });

                btn_save_manual_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        uniMethods.hideKeyboardFromActivity(activity);
                        is_manual_address_open = false;
                        main_add = til_address_line_1.getEditText().getText().toString();
                        sub_add = til_address_line_2.getEditText().getText().toString();
                        ll_bottom.removeAllViews();
                        ll_bottom.addView(ll_first_address);
                        ll_bottom.addView(tv_edit_address);
                        ll_bottom.addView(btn_set_address);
                        tv_current_address_main.setText(Html.fromHtml("<b>" + main_add + "</b><br><small>" + sub_add + "</small>"));
                        tv_address_title.setText(main_add + "\n" + sub_add);

                        cons_current_location.setVisibility(View.VISIBLE);
                        et_search_address.setVisibility(View.VISIBLE);
                        iv_cross.setVisibility(View.VISIBLE);
                        iv_icon_search.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }

    private void createGoogleMap() {
        sf_google_map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.sf_google_map);
        sf_google_map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                google_map = googleMap;
                /*LatLng latLng = new LatLng(google_map.getCameraPosition().target.latitude, google_map.getCameraPosition().target.longitude);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.dot);
                MarkerOptions markerOptions = new MarkerOptions()
                        .icon(icon).anchor(0.5f, 0.5f)
                        .position(latLng).title("You are here");
                CircleOptions circleOptions = new CircleOptions().center(latLng)
                        .radius(7)
                        .fillColor(Color.WHITE)
                        .strokeWidth(5);
                markerOptions.draggable(false);
                googleMap.addMarker(markerOptions);
                googleMap.addCircle(circleOptions);*/
                google_map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                google_map.setTrafficEnabled(false);
                google_map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        showAddressProgress();
                    }
                });
                google_map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        updateAllFields(google_map.getCameraPosition().target.latitude,
                                google_map.getCameraPosition().target.longitude);
                    }
                });
            }
        });
    }

    private void toggleCurrentLocationButton(boolean press) {
        if (press) {
            tv_getLocation.setVisibility(View.GONE);
            lottie_map.setVisibility(View.VISIBLE);
            cons_current_location.setElevation(0f);
            cons_current_location.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.transparent));
        } else {
            tv_getLocation.setVisibility(View.VISIBLE);
            iv_crosshair.setVisibility(View.VISIBLE);
            lottie_map.setVisibility(View.GONE);
            cons_current_location.setElevation(4f);
            cons_current_location.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.background_0));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(GoogleMapAddress.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (isGpsEnabled()) {
                client.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        //client.removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            moveCameraOnPoint(locationResult.getLocations().get(index).getLatitude()
                                    , locationResult.getLocations().get(index).getLongitude());
                        }
                    }
                }, Looper.getMainLooper());
            } else {
                turnOnGps();
            }
        } else {
            permissionRequest();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLastLocation() {
        zoom = 17;
        if (ActivityCompat.checkSelfPermission(GoogleMapAddress.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult() == null) {
                            cons_current_location.performClick();
                        } else {
                            moveCameraOnPoint(task.getResult().getLatitude()
                                    , task.getResult().getLongitude());
                        }
                    }
                }
            });
        } else {
            permissionRequest();
        }
    }

    private void moveCameraOnPoint(double latitude, double longitude) {
        google_map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
        toggleCurrentLocationButton(false);
        btn_set_address.setEnabled(true);
    }

    private void turnOnGps() {
        lottie_map.setVisibility(View.INVISIBLE);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(GoogleMapAddress.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(GoogleMapAddress.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissionRequest() {
        lottie_map.setVisibility(View.INVISIBLE);
        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        lottie_map.setVisibility(View.INVISIBLE);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                cons_current_location.performClick();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                    ll_location.setVisibility(View.GONE);
                    cons_current_location.setVisibility(View.GONE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    ll_location.setVisibility(View.VISIBLE);
                    cons_current_location.setVisibility(View.VISIBLE);

                }
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    @Override
    public void click(Place place, String action) {
        if (place == null) {
            if (action.equals("remove_views")) {
                iv_cross.performClick();
            } else {

            }
        } else {
            if (place.getLatLng() != null) {
                zoom = 16;
                moveCameraOnPoint(place.getLatLng().latitude, place.getLatLng().longitude);
            }
        }

    }

    private void hideAddressProgress() {
        if (progress_bar.getVisibility() == View.VISIBLE) {
            tv_address_title.setVisibility(View.VISIBLE);
            if (is_manual_address_open) {
                til_address_line_1.setVisibility(View.VISIBLE);
                til_address_line_2.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            } else {
                ll_first_address.setVisibility(View.VISIBLE);
                tv_edit_address.setVisibility(View.VISIBLE);
                btn_set_address.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
            }
        }
    }

    private void showAddressProgress() {
        if (progress_bar.getVisibility() != View.VISIBLE) {
            tv_address_title.setVisibility(View.INVISIBLE);
            if (is_manual_address_open) {
                til_address_line_1.setVisibility(View.INVISIBLE);
                til_address_line_2.setVisibility(View.INVISIBLE);
                progress_bar.setVisibility(View.VISIBLE);
            } else {
                btn_set_address.setVisibility(View.INVISIBLE);
                tv_edit_address.setVisibility(View.INVISIBLE);
                ll_first_address.setVisibility(View.INVISIBLE);
                progress_bar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateAllFields(double latitude, double longitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        new AsyncGeocoder().execute(new AsyncGeocoderObject(
                new Geocoder(context),
                location,
                new AsyncGeocoderObject.ResultGeocoder() {
                    @Override
                    public void onResult(List<Address> adds) {
                        if (adds != null && adds.size() > 0) {
                            Address add = adds.get(adds.size() - 1);
                            int max = add.getMaxAddressLineIndex();
                            if (max != -1) {
                                    /*for (int i = 0; i <= max; i++) {
                                        //   currentAddress += add.getAddressLine(i) + " ";
                                    }*/
                                address_line = String.valueOf(add.getAddressLine(0));

                                String locality = String.valueOf(add.getLocality());
                                String postal_code = String.valueOf(add.getPostalCode());
                                String admin_area = String.valueOf(add.getAdminArea());
                                String feature_name = String.valueOf(add.getFeatureName());
                                String sub_locality = String.valueOf(add.getSubLocality());
                                String sub_admin_area = String.valueOf(add.getSubAdminArea());
                                String thorough_fare = String.valueOf(add);

                                if (feature_name.contains("+")) {
                                    if (address_line.contains(feature_name)) {
                                        address_line = address_line.replace(feature_name + ", ", "").replace(feature_name + ",", "").replace(feature_name, "");
                                    }
                                }

                                if (sub_locality.equals("") || sub_locality.equals("null")) {
                                    if (address_line.contains(sub_admin_area)) {
                                        main_add = address_line.substring(0, address_line.indexOf(sub_admin_area));
                                        sub_add = address_line.substring(address_line.indexOf(sub_admin_area));
                                    } else if (address_line.contains(admin_area)) {
                                        main_add = address_line.substring(0, address_line.indexOf(admin_area));
                                        sub_add = address_line.substring(address_line.indexOf(admin_area));
                                    }
                                } else {
                                    if (address_line.contains(locality)) {
                                        main_add = address_line.substring(0, address_line.indexOf(locality));
                                        sub_add = address_line.substring(address_line.indexOf(locality));
                                    } else if (address_line.contains(sub_admin_area)) {
                                        main_add = address_line.substring(0, address_line.indexOf(sub_admin_area));
                                        sub_add = address_line.substring(address_line.indexOf(sub_admin_area));
                                    } else if (address_line.contains(admin_area)) {
                                        main_add = address_line.substring(0, address_line.indexOf(admin_area));
                                        sub_add = address_line.substring(address_line.indexOf(admin_area));
                                    }
                                }

                                sub_add = sub_add.replace(" " + postal_code, "").replace(postal_code, "");

                                if (main_add.endsWith(",")) {
                                    main_add = main_add.substring(0, main_add.lastIndexOf(","));
                                } else if (main_add.endsWith(", ")) {
                                    main_add = main_add.substring(0, main_add.lastIndexOf(", "));
                                } else if (main_add.endsWith(",  ")) {
                                    main_add = main_add.substring(0, main_add.lastIndexOf(",  "));
                                }

                                if (main_add.equals("") || main_add.equals(" ")) {
                                    main_add = sub_add;
                                    sub_add = "";
                                }
                                main_add = main_add.replaceAll(",,", ",").replaceAll(" {2}", " ");
                                sub_add = sub_add.replaceAll(",,", ",").replaceAll(" {2}", " ");

                                hideAddressProgress();
                                tv_current_address_main.setText(Html.fromHtml("<b>" + main_add + "</b><br><small>" + sub_add + "</small>"));
                                tv_address_title.setText(main_add + "\n" + sub_add);
                                if (til_address_line_1 != null && til_address_line_2 != null) {
                                    til_address_line_1.getEditText().setText(main_add);
                                    til_address_line_2.getEditText().setText(sub_add);
                                    /*til_address_line_2.getEditText().setText("full  - " + address_line + "\n\n" +
                                            "feature_name - " + feature_name + "\n\n" +
                                            "sub_locality - " + sub_locality + "\n\n" +
                                            "locality - " + locality + "\n\n" +

                                            "sub_admin_area - " + sub_admin_area + "\n\n" +
                                            "admin_area - " + admin_area + "\n\n" +

                                            "postal_code - " + postal_code + "\n\n" +
                                            "thorough_fare - " + thorough_fare + "\n\n");*/
                                }
                            }
                        }
                    }
                }
        ));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void onBottomClick(View view) {
    }

}