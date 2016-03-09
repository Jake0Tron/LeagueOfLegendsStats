package com.example.jake.LeagueStats;

import org.json.JSONObject;

/**
 * Created by Jake on 1/3/2016.
 */
public interface AsyncVersionResponse {
    void processVersionFinish(JSONObject output);
}
