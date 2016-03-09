package com.example.jake.LeagueStats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements AsyncResponse, AsyncImageResponse {

    private Button singleSumStats,
            singleSumGameStats,
            singleSumChampStats,
            champsOnRotation,
            championBuilder;
    private TextView versionText;
    private SummonerData summoner;

    private String sumURL;

    private ImageView summonerProfileIcon;
    private JSONObject json;
    private String sumJsonString;
    private String returnedJSONString;
    private String versionString;
    private URLBuilder urlBuilder;
    private HttpAsyncTask http;
    private HttpAsyncImageTask imageTask;

    private int menuChoice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // homemade helper function to be called on resume/create
        build();

        // populate values
        TextView v = (TextView) findViewById(R.id.main_menu_summoner_name);
        v.setText(this.summoner.getSummonerName());
        v = (TextView) findViewById(R.id.main_menu_selected_region);
        v.setText(this.summoner.getRegion().toUpperCase());

        this.singleSumStats = (Button) findViewById(R.id.main_menu_single_summoner);
        this.singleSumGameStats = (Button) findViewById(R.id.main_menu_single_summoner_game);
        this.singleSumChampStats = (Button) findViewById(R.id.main_menu_single_summoner_champion);
        this.champsOnRotation = (Button) findViewById(R.id.main_menu_all_rotation_champions);
        this.championBuilder = (Button) findViewById(R.id.main_menu_build_a_champ);
        this.summonerProfileIcon = (ImageView) findViewById(R.id.main_menu_summoner_profile_icon);
        this.versionText = (TextView) findViewById(R.id.versionText);

        String imgUrl = this.urlBuilder.getSummonerProfileIconURL(this.summoner.getProfileIconId(), versionString);
        this.versionText.setText(versionString);


        //Log.d("ProfileIcon", imgUrl);

        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add(imgUrl);
        // get summoner profile Icon
        this.imageTask.execute(imgUrls);
    }

    // CUSTOM SET UP FUNCTION
    // ADD TO THIS IF NEEDED ON CREATE OR RESUME!
    public void build(){
        // get JSON string from mainActivity call
        // FORMAT: {"tehbean":{"id":49449174,"name":"tehbean","profileIconId":785,"summonerLevel":30,"revisionDate":1447465652000}}
        Intent i = getIntent();
        this.sumJsonString = i.getStringExtra("sumJSON");
        this.returnedJSONString = i.getStringExtra("statJSON");
        try {
            this.json = new JSONObject(sumJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // create summonerdata class from JSON
        this.summoner = new SummonerData(json, i.getStringExtra("region"));
        this.versionString = "Version" + i.getStringExtra("iconVer");
        this.urlBuilder = new URLBuilder();
        this.http = new HttpAsyncTask();
        this.http.delegate = this;
        this.imageTask = new HttpAsyncImageTask();
        this.imageTask.delegate = this;
    }

    // called on Single Summoner Button Click
    // starts Http query to API for JSON on single summoner
    public void getSingleSumStats(View v){
        this.menuChoice = 1;
        // Build stat summary URL
        String url = urlBuilder.singleSummonerStatSummaryURL(
                        this.summoner.getRegion().
                                toLowerCase(),
                        this.summoner.getSummonerID()
                        );
        Log.d("MainMenu URL", url);
        // execute API call
        http.execute(url);
    }

    // called on Summoner Stats per Champ Button Click
    // starts Http query to API for JSON on single summoner Champ stats
    public void getBaseChampStats(View v){
        this.menuChoice = 3;
        String url = this.urlBuilder.getAllChampionIdsURL(
                false,                          // rotation
                this.summoner.getRegion()
                        .toLowerCase());
        this.http.execute(url);
    }

    // called on Champ Rotation Button Click
    // starts Http query to API for JSON on single summoner
    public void getAllChampsOnRotation(View v){
        this.menuChoice = 4;
        String url = this.urlBuilder.getAllChampionIdsURL(true, this.summoner.getRegion());
        this.http.execute(url);
    }

    // called on Single Summoner In Game Button Click
    // starts Http query to API for JSON on single summoner in game
    public void getSingleSumStatsInGame(View v){
        //this.menuChoice = 2;
        //Intent i = new Intent(this, InGameSummonersActivity.class);
        //i.putExtra("SummonerName", this.summoner.getSummonerName());
        //i.putExtra("Region", this.summoner.getRegion());
        //startActivity(i);

        // See Documentation for more information, This was out of scope for this assignment!
        Toast.makeText(this, "Not Implemented for this Assignment!", Toast.LENGTH_LONG).show();
    }

    // called on Champion Item Builder Button Click
    // starts Http query to API for JSON on single summoner
    public void getChampionItemBuilder(View v){
//        this.menuChoice = 5;
//        Intent i = new Intent(this, ItemBuilderMenuActivity.class);
//        i.putExtra("SummonerName", this.summoner.getSummonerName());
//        i.putExtra("Region", this.summoner.getRegion());
//        i.putExtra("Rotation", false);
//        startActivity(i);

        // See Documentation for more information, This was out of scope for this assignment!
        Toast.makeText(this, "Not Implemented for this Assignment!", Toast.LENGTH_LONG).show();
    }

    // Called through Interface to get API data from query.
    // Handles multiple cases by switching on menuChoice (set on button click)
    @Override
    public void processFinish(JSONObject output) {
        // Get incoming JSON Object
        this.json = output;
        this.returnedJSONString = this.json.toString();

       // Log.d("MainMenuChoice", String.valueOf(menuChoice));
        //Log.d("MenuReturnedJSONString", returnedJSONString);

        // Handle Menu Case
        switch(menuChoice){
            case 1:{
                // Single Summoner
                // FORMAT: {"tehbean":{"id":49449174,"name":"tehbean","profileIconId":785,"summonerLevel":30,"revisionDate":1447465652000}}
                Intent i = new Intent(this, SingleSummonerActivity.class);
                // send newly acquired Stat JSON
                i.putExtra("statJSON", this.returnedJSONString);
                // send single summoner data over as string
                i.putExtra("sumJSON", this.sumJsonString);
                // send region
                i.putExtra("region", this.summoner.getRegion());
                startActivity(i);
            }break;
            case 2:{
                // single summoner stats in game
                Intent i = new Intent(this, SingleSummonerActivity.class);
                // send newly acquired Stat JSON
                i.putExtra("statJSON", this.returnedJSONString);
                // send single summoner data over as string
                i.putExtra("sumJSON", this.sumJsonString);
                // send region
                i.putExtra("region", this.summoner.getRegion());
                startActivity(i);
            }break;
            case 3: {
                // ALL CHAMP STATS

                // FORMAT:
                //  {"champions":
                //      [
                //          {"id":51,"active":true,"botEnabled":true,"freeToPlay":true,"botMmEnabled":true,"rankedPlayEnabled":true},
                //          ...
                //          {"id":134,"active":true,"botEnabled":false,"freeToPlay":true,"botMmEnabled":false,"rankedPlayEnabled":true}
                //  ]
                // }
                Intent i = new Intent(this, ChampionSelectActivity.class);
                i.putExtra("iconVer",this.versionString);
                i.putExtra("sumJSON", this.sumJsonString);
                i.putExtra("Region", this.summoner.getRegion());
                i.putExtra("Rotation", false);
                i.putExtra("champJSON", returnedJSONString);
                startActivity(i);
            }break;
            case 4: {
                // champs on ROTATION (same as above)
                //Champion Select [?freeToPlay=true]
                Intent i = new Intent(this, ChampionSelectActivity.class);
                i.putExtra("iconVer",this.versionString);
                i.putExtra("sumJSON", this.sumJsonString);
                i.putExtra("Region", this.summoner.getRegion());
                i.putExtra("Rotation", true);
                i.putExtra("champJSON", returnedJSONString);
                startActivity(i);
            }break;
            case 5:{

            }break;

        }


    }

    // ensures data is set up for previous activity
    @Override
    public void onStop(){
        super.onStop();

        // FORMAT: {"tehbean":{"id":49449174,"name":"tehbean","profileIconId":785,"summonerLevel":30,"revisionDate":1447465652000}}
        Intent i = new Intent(this, SingleSummonerActivity.class);
        // send newly acquired Stat JSON
        i.putExtra("statJSON", this.returnedJSONString);
        // send single summoner data over as string
        i.putExtra("sumJSON", this.sumJsonString);
        // send region
        i.putExtra("region", this.summoner.getRegion());
    }

    // ensures data is available for views etc
    @Override
    public void onResume(){
        super.onResume();
        build();
    }

    @Override
    public void imageProcessFinish(ArrayList<Bitmap> images) {
        Bitmap img = images.get(0);

        this.summonerProfileIcon.setImageBitmap(img);
    }
}
