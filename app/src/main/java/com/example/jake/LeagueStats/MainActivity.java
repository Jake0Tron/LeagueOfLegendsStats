package com.example.jake.LeagueStats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncResponse, AsyncVersionResponse {
    //  SUMMONER NAME
    private EditText eText;
    private Button submitButton;
    private Spinner regionSpinner;
    private ArrayList<String> regions;

    private Intent i;
    private String jsonString;
    private boolean verFin, httpFin;
    private SummonerData summoner;
    private URLBuilder urlBuilder;
    private HttpAsyncTask http;
    private HttpAsyncVersionTask versionTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Homemade helper function for setup at resume/create
        build();

        // enterButton
        this.submitButton = (Button) findViewById(R.id.enterSumName);
        //editText
        this.eText = (EditText) findViewById(R.id.sumName);
        // spinner
        this.regionSpinner = (Spinner) findViewById(R.id.login_region);

        // Dropdown
        ArrayAdapter<CharSequence> aa = ArrayAdapter.createFromResource(this, R.array.regions, R.layout.support_simple_spinner_dropdown_item);
        this.regionSpinner.setAdapter(aa);

        // TODO: debug values
        this.eText.setText("jake0tron");
        this.regionSpinner.setSelection(7, false);   // default to NA for debug
    }

    public void onResume(){
        super.onResume();
        // TODO: debug values
        build();
        this.eText.setText("jake0tron");
        this.regionSpinner.setSelection(7, false);   // default to NA for debug
    }

    public void build(){

        this.urlBuilder = new URLBuilder();
        this.http = new HttpAsyncTask();
        this.http.delegate = this;
        this.versionTask = new HttpAsyncVersionTask();
        this.versionTask.delegate = this;
        this.summoner = new SummonerData();
        this.i = new Intent(this, MainMenuActivity.class);
    }

    public void onSummonerNameSubmit(View view){
        this.summoner.setSummonerName(eText.getText().toString().replace(" ", ""));
        this.summoner.setRegion(this.regionSpinner.getSelectedItem().toString().toLowerCase());

        //TODO: Save information if selected...

        // Build URL for first query
        String url = urlBuilder.singleSummonerURL(
                this.summoner.
                        getSummonerName().
                        toLowerCase(),
                this.summoner.getRegion()
        );

        String verURL = urlBuilder.getVersionDataURL();


        // deactivate button so only 1 click
        this.submitButton.setActivated(false);
        //execute queries

        this.http = new HttpAsyncTask();
        this.http.setDelegate(this);
        this.http.execute(url);

        this.versionTask = new HttpAsyncVersionTask();
        this.versionTask.setDelegate(this);
        this.versionTask.execute(verURL);
    }

    public void showDisclaimer(View v){
        Toast.makeText(this, R.string.disclaimer_text, Toast.LENGTH_LONG).show();
    }

    public void saveDefaultSummonerName(String name){

    }

    public void checkStartNextActivity(){
        if (verFin && httpFin){
            startActivity(i);
        }
    }

    public void processVersionFinish(JSONObject verJson){
        //Log.d("VERSIONjson", verJson.toString());
    //{
        // "n":
        // {
        // "item":"5.24.2",
        // "rune":"5.24.2",
        // "mastery":"5.24.2",
        // "summoner":"5.24.2",
        // "champion":"5.24.2",
        // "profileicon":"5.24.2",
        // "map":"5.24.2",
        // "language":"5.24.2"
        // },
        // "v":"5.24.2",
        // "l":"en_US",
        // "cdn":"http:\/\/ddragon.leagueoflegends.com\/cdn",
        // "dd":"5.24.2",
        // "lg":"5.24.2",
        // "css":"5.24.2",
        // "profileiconmax":28,
        // "store":null
        // }

        if (verJson != null){
            String versionJSONstr = verJson.toString();

            try {
                String allVersions = verJson.getString("n");
                JSONObject verO = new JSONObject(allVersions);

                String iconVer = verO.getString("profileicon");
                Log.d("VERSIONjson", iconVer );
                i.putExtra("iconVer", iconVer);
                verFin = true;

                checkStartNextActivity();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void processFinish(JSONObject output) {
        if (output != null){
            this.jsonString = output.toString();
            this.i.putExtra("sumJSON", jsonString);
            this.i.putExtra("region", this.summoner.getRegion());
            httpFin = true;

            checkStartNextActivity();

        }
        else{
            Toast.makeText(this,
                    "Sorry, no summoner with that name found in " + this.summoner
                            .getRegion()
                            .toUpperCase(),
                    Toast.LENGTH_LONG)
                    .show();
            this.eText.setText("");
        }

    }
}
