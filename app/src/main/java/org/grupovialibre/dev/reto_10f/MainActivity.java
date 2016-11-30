package org.grupovialibre.dev.reto_10f;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private TextView denSpinnerDialogText;
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<Evento> eventos = new ArrayList<>();
    private String yearSelected = "";
    private ProgressDialog pDialog;
    private String url = "https://www.datos.gov.co/resource/644k-i2xw.json";
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Spinner mDenSpinner;
        ArrayAdapter denAdapter;


        denAdapter = ArrayAdapter.createFromResource(this, R.array.departamentos_colombia, android.R.layout.simple_spinner_item);

        mDenSpinner = (Spinner) findViewById(R.id.departamentosSpinner);
        mDenSpinner.setAdapter(denAdapter);
        mDenSpinner.setOnItemSelectedListener(this);
        mDenSpinner.setSelection(0);

        Button generateButton = (Button) findViewById(R.id.generateMapButton);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String departamento = denSpinnerDialogText.getText().toString();

                //yearSelected

                if((yearSelected!=null && !yearSelected.isEmpty())
                        && (departamento!=null && !departamento.isEmpty())){
                    url = "https://www.datos.gov.co/resource/644k-i2xw.json?&departamento="+departamento+"&ano="+yearSelected;
                }else{
                    if(yearSelected!=null && !yearSelected.isEmpty()){
                        url = "https://www.datos.gov.co/resource/644k-i2xw.json?&ano="+yearSelected;
                    }

                    if(departamento!=null && !departamento.isEmpty()){
                        url = "https://www.datos.gov.co/resource/644k-i2xw.json?&departamento="+departamento.toUpperCase();
                    }
                }




                if(url!=null && !url.isEmpty()){
                    new GetLocations().execute();
                }



                /*finish();
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
                Intent mIntent = new Intent(MainActivity.this, MapsActivity.class);
                mIntent.putStringArrayListExtra("locations",locations);
                startActivity(mIntent);*/
            }
        });


    }




    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLocations extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray registers = new JSONArray(jsonStr);

                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    //JSONArray registers = jsonObj.getJSONArray("");

                    // looping through All Contacts
                    for (int i = 0; i < registers.length(); i++) {
                        JSONObject c = registers.getJSONObject(i);

                        Log.e(TAG, "JSON Object: " + c.toString());

                        String latitud = c.getString("latitudcabecera");
                        String longitud = c.getString("longitudcabecera");

                        String location = latitud+","+longitud;
                        String evento = c.getString("evento");
                        String tipoEvento = c.getString("tipoevento");
                        String municipio = c.getString("municipio");

                        String sitio = "";
                        /*if(c.getString("sitio")!=null){
                            sitio = c.getString("sitio");
                        }*/


                        Evento nuevoEvento = new Evento(location,evento,tipoEvento,municipio,sitio);


                        eventos.add(nuevoEvento);
























                        locations.add(location);























                        /*
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");
                        */

                        /*
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);*/
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            /*ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{"name", "email",
                    "mobile"}, new int[]{R.id.name,
                    R.id.email, R.id.mobile});

            lv.setAdapter(adapter);*/

            finish();
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
            Intent mIntent = new Intent(MainActivity.this, MapsActivity.class);
            mIntent.putStringArrayListExtra("locations",locations);
            mIntent.putParcelableArrayListExtra("eventos",eventos);
            startActivity(mIntent);
        }

    }











    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonYear1998:
                if (checked)
                    yearSelected="1998";
                    break;
            case R.id.radioButtonYear2006:
                if (checked)
                    yearSelected="2006";
                    break;

            case R.id.radioButtonYear2016:
                if (checked)
                    yearSelected="2016";
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if (spinner.getId() == R.id.departamentosSpinner) {
            denSpinnerDialogText = (TextView) view;
            denSpinnerDialogText.setTextSize(10);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
