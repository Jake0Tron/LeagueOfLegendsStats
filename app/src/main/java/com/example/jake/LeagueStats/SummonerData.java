package com.example.jake.LeagueStats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jake on 11/13/2015.
 */
public class SummonerData {

    // Summoner ID
    public long summonerID;

    // Summoner Region
    public String region;

    // Summoner Name
    public String summonerName;

    // ID of the summoner icon associated with the summoner.
    public long profileIconId;

    //Date summoner was last modified specified as epoch milliseconds.
    public long revisionDate;

    //Summoner level associated with the summoner.
    public int summonerLevel;

    //#######################################################
    // Build Summoner class off of JSONObject
    public SummonerData (JSONObject summonerJSON, String region){
        try {
            this.region = region;
            // get object name (returned as JSON object name)
            JSONArray names = summonerJSON.names();

            // Use  SummonerName to get rest of data
            JSONObject stats = (JSONObject) summonerJSON.get(names.getString(0));
            //JSONObject stats = (JSONObject) summonerJSON.get(this.summonerName);

            this.summonerID = stats.getLong("id");
            this.profileIconId = stats.getLong("profileIconId");
            this.revisionDate = stats.getLong("revisionDate");
            this.summonerLevel = stats.getInt("summonerLevel");
            this.summonerName = stats.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SummonerData(int summonerID, String summonerName, String region) {
        this.summonerID = summonerID;
        this.summonerName = summonerName;
        this.region = region;
    }

    public SummonerData() {
        this.summonerID = -1;
        this.summonerName = "";
        this.region = "";
        this.summonerLevel = 0;
        this.revisionDate = 0;
    }

    public String toString(){
        return "Summoner name: " + this.summonerName
                + "\n Level:" + this.summonerLevel
                +"\n ID: "+ this.summonerID;
    }


// SETTERS/GETTERS

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public long getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(long profileIconId) {
        this.profileIconId = profileIconId;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public long getSummonerLevel() {
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel) {
        this.summonerLevel = summonerLevel;
    }

    public long getSummonerID() {
        return summonerID;
    }

    public void setSummonerID(long summonerID) {
        this.summonerID = summonerID;
    }
}
