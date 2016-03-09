package com.example.jake.LeagueStats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndividualChampionActivity extends AppCompatActivity implements AsyncResponse, AsyncImageResponse {

    final String TAG = "INDIVCHAMP";

    private int champLevel;
    private Context context;
    private NumberPicker levelSelector;
    private NumberPicker.OnValueChangeListener pickerListener;
    private ListView statList;
    private ArrayList<String> mathList;
    private ArrayList<String> statsTitle;
    private ArrayList<String> statsValue;
    private ArrayList<String> perLevelTitle;
    private ArrayList<Double> perLevelValue;

    private ArrayList<Bitmap> picList;
    private TextView champName,
                    champDescrip,
                    champTags;
    private SeekBar
            attackValueView,
            defenseValueView,
            magicValueView,
            difficultyValueView;
    private ImageView champImage;

    private String versionString;
    private PictureTextSubTextAdapter statAdapter;
    private String incomingSummonerJSONString,
                    selectedChampJSONstring,
                    incomingChampionDisplay,
                    champDescripString,
                    champTagString;
    private JSONObject summonerJSON, champJSON;
    private SummonerData summoner;
    private URLBuilder urlBuilder;
    private HttpAsyncTask http;
    private HttpAsyncImageTask imageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_champion);
        // TODO: Populate Stat Arrays
        this.statsTitle = new ArrayList<>();
        this.statsValue = new ArrayList<>();
        this.picList = new ArrayList<>();
        this.statAdapter = new PictureTextSubTextAdapter(this, statsTitle, statsValue, picList);
        this.statList = (ListView) findViewById(R.id.indiv_champ_skins_list);
        this.statList.setAdapter(statAdapter);

        // handle other text views to be populated in build();
        this.champName = (TextView) findViewById(R.id.indiv_champ_title);
        this.champDescrip = (TextView) findViewById(R.id.indiv_champ_descrip);
        this.champImage = (ImageView) findViewById(R.id.indiv_champ_champion_picture);
        this.champTags = (TextView) findViewById(R.id.indiv_champ_tags);

        // helper called on resume and create
        build();

        // Set up Level Spinner
        this.levelSelector = (NumberPicker)findViewById(R.id.indiv_champ_level_select);
        this.levelSelector.setMinValue(1);
        this.levelSelector.setMaxValue(18);
        this.pickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int level) {



                for (int i=0; i < perLevelTitle.size(); i++){
                    String statToGet = perLevelTitle.get(i).replace("perlevel","");
                    // if the stat increases per level
                    if (statsTitle.contains(statToGet)){
                        int index = statsTitle.indexOf(statToGet);

                        double newStat = ((Double)(level * perLevelValue.get(i)))
                                + Double.valueOf(mathList.get(index));

                        newStat = Math.round(newStat);

                        statsValue.set(index,
                                String.format("%.0f", newStat)
                        );
                    }
                }
                statAdapter.notifyDataSetChanged();

            }
        };
        this.levelSelector.setOnValueChangedListener(pickerListener);
    }

    public void build(){
        this.context = this;
        // incoming data from prev. intent
        Intent i = getIntent();
        this.versionString = i.getStringExtra("iconVer");
        this.incomingSummonerJSONString = i.getStringExtra("sumJSON");              // Summoner Info
        this.selectedChampJSONstring = i.getStringExtra("champJSON");       // selected champion
        this.incomingChampionDisplay = i.getStringExtra("selectedChampDisplay");    // display name
        this.champDescripString = i.getStringExtra("selectedChampDescrip");         // description

        // Async Tasks
        this.urlBuilder = new URLBuilder();
        this.http = new HttpAsyncTask();
        this.http.delegate = this;
        this.imageTask = new HttpAsyncImageTask();
        this.imageTask.delegate = this;

        String imageURL = "";

        // helper to reduce clutter in build
        // sets up seek bar parameters
        buildSeekBars();

        try {
            this.summonerJSON = new JSONObject(incomingSummonerJSONString);
            this.champJSON = new JSONObject(selectedChampJSONstring);
            //Log.d(TAG, champJSON.toString());
            String champName =
                    this.urlBuilder.getIndividualChampImageURL(this.champJSON.getString("id"), this.versionString);
            //Log.d(TAG, champName.toString());
            JSONObject stats = this.champJSON.getJSONObject("stats");
            //Log.d(TAG + "stats", stats.toString());

            // Stat increase arrays
            this.perLevelTitle = new ArrayList<>();
            this.perLevelValue = new ArrayList<>();
            // base stat Array

            // populate Listview with base stats
            for (int j=0; j < stats.length(); j++ ){
                String title, value;
                title = (String)stats.names().get(j);
                value = String.valueOf(stats.getDouble(title));

                // add to per level array
                if (title.contains("perlevel")){
                    this.perLevelTitle.add(title);
                    this.perLevelValue.add(Double.valueOf(value));
                }else{
                    // add to base stat array
                    //Log.d(TAG, title + " " + value);
                    this.statsTitle.add(title);
                    this.statsValue.add(value);
                }
            }
            // use this array for any necessary math
            this.mathList = new ArrayList<>(statsValue);
            // update view
            this.statAdapter.notifyDataSetChanged();
            // handle champ stat ratings:

            JSONObject infoArray = this.champJSON.getJSONObject("info");
            //Log.d(TAG+"info", infoArray.toString());

            this.magicValueView.setProgress(infoArray.getInt("magic"));
            this.attackValueView.setProgress(infoArray.getInt("attack"));
            this.defenseValueView.setProgress(infoArray.getInt("defense"));
            this.difficultyValueView.setProgress(infoArray.getInt("difficulty"));

            //Handle Champ tags
            JSONArray tagArray = this.champJSON.getJSONArray("tags");
            this.champTagString = "Tags: ";
            for (int k=0; k < tagArray.length(); k++){
                this.champTagString += tagArray.get(k);
                if (k != tagArray.length())
                    this.champTagString += ", ";
            }

            //Log.d(TAG, champTagString);

            this.champTags.setText(champTagString);

            // handle champ image
            ArrayList<String> imageNames = new ArrayList<>();
            imageNames.add(champName);
            this.imageTask.execute(imageNames);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.summoner = new SummonerData(this.summonerJSON, i.getStringExtra("Region"));
        this.champDescrip.setText(champDescripString);
        this.champName.setText(incomingChampionDisplay);
    }

    public void buildSeekBars(){
        // set up ratings and disable scrolling (no response on touch) and thumb
        this.magicValueView = (SeekBar)findViewById(R.id.indiv_champ_rating_magic);
        this.magicValueView.setMax(10);
        this.magicValueView.setClickable(false);
        this.magicValueView.setThumb(null);
        this.magicValueView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        this.attackValueView = (SeekBar)findViewById(R.id.indiv_champ_rating_attack);
        this.attackValueView.setMax(10);
        this.attackValueView.setClickable(false);
        this.attackValueView.setThumb(null);
        this.attackValueView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        this.defenseValueView = (SeekBar)findViewById(R.id.indiv_champ_rating_defense);
        this.defenseValueView.setMax(10);
        this.defenseValueView.setClickable(false);
        this.defenseValueView.setThumb(null);
        this.defenseValueView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        this.difficultyValueView = (SeekBar)findViewById(R.id.indiv_champ_rating_difficulty);
        this.difficultyValueView.setMax(10);
        this.difficultyValueView.setClickable(false);
        this.difficultyValueView.setThumb(null);
        this.difficultyValueView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }


    // get JSON info here
    @Override
    public void processFinish(JSONObject output) {

    }

    // get Image info here
    @Override
    public void imageProcessFinish(ArrayList<Bitmap> images) {

        Bitmap img = images.get(0);
        this.champImage.setImageBitmap(img);
    }
}
