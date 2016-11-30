package org.grupovialibre.dev.reto_10f;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.event.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Evento> eventos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getStringArrayList("locations") != null) {
                locations = extras.getStringArrayList("locations");

                if(locations!=null){
                    //Build locations
                    Log.e("LOCATIONS", "--->"+locations.size());
                }

            }

            if (extras.getParcelableArrayList("eventos") != null) {
                eventos = extras.getParcelableArrayList("eventos");

                if(eventos!=null){

                    //Build locations
                    Log.e("EVENTOS", "--->"+eventos.size());
                    Toast.makeText(getApplicationContext(),
                            "Se encontraron "+eventos.size()+" incidentes",
                            Toast.LENGTH_LONG)
                            .show();


                }

            }
        }


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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng colombia = new LatLng(5.088637,-74.196760);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombia, 5));


        for (Evento evento : eventos){

            String[] loc = evento.getLocation().split(",");
            String lat = loc[0];
            String lng = loc[1];
            LatLng neoLatLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

            mMap.addMarker(new MarkerOptions()
                            .position(neoLatLng)
                            .snippet(evento.getEvento()+"-"+evento.getTipoEvento())
                            .title(evento.getMunicipio()+"-"+evento.getSitio())
            );

        }

        /*
        for (String reporte : locations){

            String[] loc = reporte.split(",");
            String lat = loc[0];
            String lng = loc[1];
            LatLng neoLatLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

            mMap.addMarker(new MarkerOptions()
                            .position(neoLatLng)
                    //.snippet(reporte.getUserName()+"-"+reporte.getDate())
                    //.title(reporte.getDescription())
            );

        }*/
    }
}
