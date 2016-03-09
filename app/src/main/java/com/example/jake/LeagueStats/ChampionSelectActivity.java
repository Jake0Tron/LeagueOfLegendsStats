package com.example.jake.LeagueStats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChampionSelectActivity extends AppCompatActivity implements AsyncResponse, AsyncImageResponse {

    private ArrayList<String> champNameList;
    private ArrayList<String> champNameDisplayList;
    private ArrayList<String> champDescrips;
    private ArrayList<Bitmap> championPictures;
    private boolean itemBuilder;
    private Context context;    // for anonymous inner calls

    private PictureTextSubTextAdapter picTextSubText;
    private ListView champListView;

    private String versionString;
    private ArrayList<Integer> champIdsToShow;
    private String incomingChampionsJsonString, incomingSummonerJSONString;
    private JSONObject incomingSummonerJSON, incomingChampionJSON;
    private SummonerData summoner;
    private URLBuilder urlBuilder;
    private HttpAsyncTask http;
    private HttpAsyncImageTask imageTask;
    private ArrayList<JSONObject> champJsons;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);

        // Helper that grabs all data sent from previous activity and sets up current activity
        build();

        // helper function that loads ArrayList<int> with Id's of champs to show
        getChampsToShow();
        // champ names for images
        this.champNameList = new ArrayList<>();
        // names to displays
        this.champNameDisplayList = new ArrayList<>();
        // champ Info for subText
        this.champDescrips = new ArrayList<>();
        // set up arrayList for  pictures
        this.championPictures = new ArrayList<>();

        // set up ListView
        this.champListView = (ListView)findViewById(R.id.champ_select_champ_list);


        // get all static data
        String url = this.urlBuilder.getAllChampionStaticDataURL(this.versionString);
        this.http.execute(url);

        // after executing, Fire AlertDialogue with message saying that data is being grabbed

        this.alertDialogBuilder = new AlertDialog.Builder(context);
        // add scrollable view to dialogue
        //http://developer.android.com/guide/topics/ui/dialogs.html
        alertDialogBuilder
                .setView(View.inflate(getApplicationContext(), R.layout.alert_dialogue_scroll_view, null))
                .setTitle("Please Wait...")
                .setMessage("Getting data from Riot's servers...");
        // create the dialog
        this.alertDialog = alertDialogBuilder.create();
        // Show the AlertDialog.
        alertDialog.show();

    }

    // ensures data is available for views etc
    @Override
    public void onResume(){
        super.onResume();
        build();
    }

    //Homemade builder method for setup onCreate/onResume
    public void build(){
        // Set up views
        this.context = this;
        Intent i = getIntent();
        this.versionString = i.getStringExtra("iconVer");
        this.itemBuilder = i.getBooleanExtra("ItemBuilder", false);         // if true, start next ItemBuilder Activity
        this.incomingChampionsJsonString = i.getStringExtra("champJSON");          // List of champions to display
        this.incomingSummonerJSONString = i.getStringExtra("sumJSON");              // Summoner Info
        this.urlBuilder = new URLBuilder();
        this.http = new HttpAsyncTask();
        this.imageTask = new HttpAsyncImageTask();
        this.http.delegate = this;
        this.imageTask.delegate = this;

        try {
            this.incomingSummonerJSON = new JSONObject(this.incomingSummonerJSONString);
            this.incomingChampionJSON = new JSONObject(this.incomingChampionsJsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.summoner = new SummonerData(incomingSummonerJSON, i.getStringExtra("Region"));
    }

    // helper function that loads ArrayList<int> with Id's of champs to show
    public void getChampsToShow(){


        // FORMAT:
        //  {"champions":
        //      [
        //          {"id":51,"active":true,"botEnabled":true,"freeToPlay":true,"botMmEnabled":true,"rankedPlayEnabled":true},
        //          ...
        //          {"id":134,"active":true,"botEnabled":false,"freeToPlay":true,"botMmEnabled":false,"rankedPlayEnabled":true}
        //  ]
        // }
        try {
            // json Object
            JSONObject champObj = new JSONObject(incomingChampionJSON.toString());
           // Log.d("champArray", champObj.toString());

            //list of champNameList
            JSONArray champList = new JSONArray(champObj.getJSONArray("champions").toString());
            //Log.d("champions", champList.toString());

            this.champIdsToShow = new ArrayList<>();

            // Make a list of champ static data to show
            for (int i=0; i < champList.length(); i++){
                JSONObject champion = new JSONObject(champList.getJSONObject(i).toString());
                // add champion id's to show to list
                champIdsToShow.add(champion.getInt("id"));
            }

            // have all ID's to show on ListView
            // Log.d("champ", champion.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //TODO: Handle Item Builder Sequence (uhghhhh thats gonna hurt)
    // handles Item Builder query
    public void startNewItemBuilderActivity(int position){
        Intent i = new Intent(context, ItemBuilderRuneSelectionActivity.class);
        // TODO: Item Builder add data to be sent over

        // start next activity
        startActivity(i);
        // don't restart this one
        finish();
    }

    // handles individual champ query
    public void startNewIndividualChampionActivity(int position){
        Intent i = new Intent(context, IndividualChampionActivity.class);
        //add data to be sent over from champ selected
        JSONObject selectedChampion = champJsons.get(position);

        Log.d("SelectedChampion", selectedChampion.toString());

        // put version info
        i.putExtra("iconVer", this.versionString);
        // put region info
        i.putExtra("Region", this.summoner.getRegion());
        // put summoner info
        i.putExtra("sumJSON", incomingSummonerJSON.toString());
        // put individual champion info (query by ID)
        i.putExtra("champJSON", selectedChampion.toString());
        // display name for champs that don't parse nicely
        i.putExtra("selectedChampDisplay", champNameDisplayList.get(position));
        // selected Champ Description
        i.putExtra("selectedChampDescrip", champDescrips.get(position));

        startActivity(i);
    }

    // JSON Returns here
    @Override
    public void processFinish(JSONObject output) {
        // get list of champions to display
        this.incomingChampionJSON = output;
        this.incomingChampionsJsonString = incomingChampionJSON.toString();
        //Log.d("champSelRecJSON" , incomingChampionsJsonString);

        if (itemBuilder){
            // THIS WAS NOT IMPLEMENTED FOR THE ASSIGNMENT!
        }else{
            try {
                // get "data"
                JSONObject data = new JSONObject(this.incomingChampionJSON.getJSONObject("data").toString());

                // get all champ names
                JSONArray names = data.names();

                // parse all champs as array

                //set up champ list for selection
                this.champJsons = new ArrayList<>();
                // cut out champs not to be displayed
                for (int i=0; i < names.length(); i++){
                    JSONObject currentIndivChampStats = new JSONObject (data.getJSONObject(names.getString(i)).toString());
                    // get currentChampion ID
                    int currentChampId = currentIndivChampStats.getInt("key");
                    // only show champions requested (compare to currentChampId)
                    if (champIdsToShow.contains(currentChampId)){
                        champJsons.add(currentIndivChampStats);
                    }
                }

                //Log.d("AsyncPics",champJsons.size() + " champs" );
                // get title strings out of JSON that are needed for display
                for (int j=0; j < champJsons.size(); j++){
                    String titleString = "", subTitleString = "";
                    // get name for Pic URL and make URL
                    this.champNameList.add(
                            this.urlBuilder.getIndividualChampImageURL(
                                    champJsons.get(j).getString("id"),
                                    this.versionString
                            )
                    );
                    // populate arrays for listadapter
                    // name for display
                    titleString += champJsons.get(j).getString("name");
                    this.champNameDisplayList.add(titleString);
                    subTitleString += champJsons.get(j).getString("title");
                    this.champDescrips.add(subTitleString);
                }
                //Log.d("AsyncPics", champNameList.size() + " champs");
                // notifyChanged listadapter
                //this.picTextSubText.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // set up query for champion Images
        this.imageTask.execute(champNameList);

    }

    // Image returns here
    @Override
    public void imageProcessFinish(ArrayList<Bitmap> images) {
        //Log.d("FINISHEDIMAGES", images.size() + " IMAGES LOADED" );
        // set adapted list
        this.championPictures = images;
        // set up adapter
        picTextSubText = new PictureTextSubTextAdapter(this,
                champNameDisplayList,
                champDescrips,
                championPictures);

        // Adapt names/pictures/descriptions to listview
        this.champListView.setAdapter(picTextSubText);

        champListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO: Handle champion selection
                if (itemBuilder)
                    startNewItemBuilderActivity(position);
                else
                    startNewIndividualChampionActivity(position);
            }
        });
        // update adapter
        this.picTextSubText.notifyDataSetChanged();

        // close the dialog if it's still showing
        if (this.alertDialog.isShowing())
            this.alertDialog.cancel();

    }
}
