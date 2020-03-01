package cn.trunch.weidong.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class UniteApi extends Api {

    private String jsonCode;
    private JSONArray jsonData;
    private JSONObject jsonPage;

    private String url;

    public UniteApi(String url, HashMap<String, String> paramsMap) {
        this.url = url;
        setParamsMap(paramsMap);
    }

    public String getJsonCode() {
        return jsonCode;
    }

    public JSONArray getJsonData() {
        return jsonData;
    }


    public JSONObject getJsonPage() {
        return jsonPage;
    }

    @Override
    protected void parseCode(JSONObject jsonObject) {
        this.jsonCode = jsonObject.optString("code");
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        this.jsonData = jsonObject.getJSONArray("data");
    }

    @Override
    protected void parsePage(JSONObject jsonObject) throws Exception {
        this.jsonPage = jsonObject.getJSONObject("pageInfo");
    }


    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected boolean isBackToUiThread() {
        return true;
    }
}
