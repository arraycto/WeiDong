package cn.trunch.weidong.http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UploadApi extends Api {

    private String jsonCode;
    private JSONArray jsonData;
    private JSONObject jsonPage;

    private String url;

    public UploadApi(String url, String filePath) {
        this.url = url;
        setFilePath(filePath);
    }

    public UploadApi(String url, List<String> filePaths) {
        this.url = url;
        setFilePaths(filePaths);
    }

    public UploadApi(String url, List<String> filePaths,int type) {
        this.url = url;
        setFilePaths(filePaths,type);
    }
    public String getJsonCode() {
        return jsonCode;
    }

    public JSONArray getJsonData() {
        return jsonData;
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
        this.jsonPage= jsonObject.getJSONObject("pageInfo");
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
