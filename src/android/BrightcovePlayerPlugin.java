package net.nopattern.cordova.brightcoveplayer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;

public class BrightcovePlayerPlugin extends CordovaPlugin {
  protected static final String LOG_TAG = "[BrightcoveCordovaPlugin]";

  private String token = null;
  private String imaLang = null;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("playByUrl")) {
      String url = args.getString(0);
      String vast = args.getString(1);
      this.playByUrl(url, vast, callbackContext);
      return true;
    } else if (action.equals("playById")) {
      String id = args.getString(0);
      String vast = args.getString(1);
      this.playById(id, vast, callbackContext);
      return true;
    } else if(action.equals("init")) {
      String token = args.getString(0);
      this.initBrightcove(token, callbackContext);
      return true;
    } else if(action.equals("setLanguage")) {
      String imaLang = args.getString(0);
      this.setImaLang(imaLang, callbackContext);
      return true;
    }
    return false;
  }

  private void playByUrl(String url, String vast, CallbackContext callbackContext) {
    if (this.urlIsValid(url)) {

      Context context = this.cordova.getActivity().getApplicationContext();
      Intent intent = new Intent(context, BrightcoveActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
      intent.putExtra("video-url", url);
      intent.putExtra("brightcove-token", this.token);
      intent.putExtra("vast-link", vast);
      intent.putExtra("ima-language", this.imaLang);
      context.startActivity(intent);

      callbackContext.success(LOG_TAG + " Playing now with URL: " + url);
    } else {
      callbackContext.error(LOG_TAG + " URL is not valid or empty!");
    }
  }

  private void playById(String id, String vast, CallbackContext callbackContext) {
    if (this.token == null){
      callbackContext.error(LOG_TAG + " Please init the brightcove with token!");
      return;
    }
    if (id != null && id.length() > 0){

      Context context = this.cordova.getActivity().getApplicationContext();
      Intent intent = new Intent(context, BrightcoveActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
      intent.putExtra("video-id", id);
      intent.putExtra("brightcove-token", this.token);
      intent.putExtra("vast-link", vast);
      intent.putExtra("ima-language", this.imaLang);
      context.startActivity(intent);

      callbackContext.success(LOG_TAG + " Playing now with Brightcove ID: " + id);
    } else{
      callbackContext.error(LOG_TAG + " Empty video ID!");
    }
  }

  private void initBrightcove(String token, CallbackContext callbackContext) {
    if (token != null && token.length() > 0){
      this.token = token;
      callbackContext.success(LOG_TAG + " Inited");
    } else{
      callbackContext.error(LOG_TAG + " Empty Brightcove token!");
    }
  }

  private void setImaLang(String imaLang, CallbackContext callbackContext) {
    if (imaLang != null && imaLang.length() > 0){
      this.imaLang = imaLang;
      callbackContext.success(LOG_TAG + " Language inited");
    } else{
      callbackContext.error(LOG_TAG + " Please set language!");
    }
  }

  private boolean urlIsValid(String url) {
    return Patterns.WEB_URL.matcher(url).matches();
  }
}