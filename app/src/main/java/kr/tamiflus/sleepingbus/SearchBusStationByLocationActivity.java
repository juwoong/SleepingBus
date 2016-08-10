package kr.tamiflus.sleepingbus;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.utils.BusStationByLocationParser;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.LocManager;

public class SearchBusStationByLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LinearLayout infoView;
    TextView name, id, region;
    Animation up, down;
    boolean show = false;
    ArrayList<BusStation> list = new ArrayList<BusStation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus_station_by_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LocManager manager = new LocManager(getApplicationContext(), SearchBusStationByLocationActivity.this);

        manager.checkIsGPSOn();
        manager.loadLocation();

        // Add a marker in Sydney and move the camera
        LatLng current = new LatLng(manager.getLatitude(), manager.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("현재 위치"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 17f));

        infoView = (LinearLayout) findViewById(R.id.busStationInfoLayout);
        name = (TextView) findViewById(R.id.busStationInfoName);
        id = (TextView) findViewById(R.id.busStationInfoID);
        region = (TextView) findViewById(R.id.busStationInfoRegion);

        up = AnimationUtils.loadAnimation(this, R.anim.up);
        down = AnimationUtils.loadAnimation(this, R.anim.down);

        infoView.setVisibility(View.INVISIBLE);

        OnCurrentLocationParseConnector parser = new OnCurrentLocationParseConnector();
        parser.execute(Double.toString(manager.getLongitude()), Double.toString(manager.getLatitude()));
        manager.stop();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet() == null) return false;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                int val = Integer.parseInt(marker.getSnippet());
                name.setText(list.get(val).getName());
                id.setText(list.get(val).getId());
                region.setText(list.get(val).getRegion());

                if(show == false ) {
                    infoView.setAnimation(up);
                    infoView.setVisibility(View.VISIBLE);
                    show = true;
                }
                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(show == true) {
                    infoView.setAnimation(down);
                    infoView.setVisibility(View.INVISIBLE);
                    show = false;
                }
            }
        });

    }

    private class OnCurrentLocationParseConnector extends AsyncTask<String, Void, ArrayList<BusStation>> {
        @Override
        protected ArrayList<BusStation> doInBackground(String... strings) {
            ArrayList<BusStation> list = null;
            BusStationByLocationParser parser = new BusStationByLocationParser();

            try {
                list = parser.parse(strings[0], strings[1]);
            }catch(IOException e) {
                e.printStackTrace();
            }catch(XmlPullParserException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<BusStation> busStations) {
            super.onPostExecute(busStations);

            Log.d("onPostExecute", Integer.toString(busStations.size()));
            for(int i=0; i<busStations.size(); i++) {
                double lat = Double.parseDouble(busStations.get(i).getX());
                double lng = Double.parseDouble(busStations.get(i).getY());
                mMap.addMarker(new MarkerOptions().position(new LatLng(lng, lat)).title(busStations.get(i).getName()).snippet(Integer.toString(i)));
            }

            BusStationDBHelper helper = new BusStationDBHelper(getApplicationContext());
            busStations =  helper.fillStation(busStations);

            list = busStations;
        }
    }
}
