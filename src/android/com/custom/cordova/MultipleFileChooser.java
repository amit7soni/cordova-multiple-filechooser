/**
 */
package com.custom.cordova;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MultipleFileChooser extends CordovaPlugin {

  private static final String TAG = "FileChooser";
  private static final String ACTION_OPEN = "open";
  private static final int PICK_FILE_REQUEST = 1;

  public static final String MIME = "mime";

  CallbackContext callback;

  @Override
  public boolean execute(String action, JSONArray inputs, CallbackContext callbackContext) throws JSONException {

    if (action.equals(ACTION_OPEN)) {
      JSONObject filters = inputs.optJSONObject(0);
      Log.w("Filters : ", filters.toString());
      chooseFile(filters, callbackContext);
      return true;
    }

    return false;
  }

  public void chooseFile(JSONObject filter, CallbackContext callbackContext) {
    String uri_filter = filter.has(MIME) ? filter.optString(MIME) : "*/*";
    //Boolean _has = filter.has("multiple");
    //Log.w("Filter CF : ", filter.toString());
    //Log.w("VAR : ", _has.toString());
    Boolean is_multiple = filter.has("multiple") ? filter.optBoolean("multiple") : false;
    //Log.w("Filter VAR : ", is_multiple.toString());
    // type and title should be configurable

    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType(uri_filter);
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, is_multiple);
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

    Intent chooser = Intent.createChooser(intent, "Select File");
    cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);

    PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
    pluginResult.setKeepCallback(true);
    callback = callbackContext;
    callbackContext.sendPluginResult(pluginResult);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    JSONArray files = new JSONArray();
    if (requestCode == PICK_FILE_REQUEST && callback != null) {

      if (resultCode == Activity.RESULT_OK) {

        //Uri uri = data.getData();
        ClipData clipData = data.getClipData();
        //Integer uri = clipData.getItemCount();

        if (clipData != null) {
          for (int i = 0; i < clipData.getItemCount(); i++) {
            String _uri = Uri.parse(UriUtils.getPathFromUri(this.cordova.getContext(), clipData.getItemAt(i).getUri())).toString();
            //files.put(clipData.getItemAt(i).getUri());
            files.put(_uri);
          }
          //Log.w(TAG, uri.toString());
          //callback.success(uri.toString());

        } else if (data.getData() != null) {
          String _uri = Uri.parse(UriUtils.getPathFromUri(this.cordova.getContext(), data.getData())).toString();
          //files.put(data.getData());
          files.put(_uri);
        }
        else {

          callback.error("File uri was null");

        }

        callback.success(files);

      } else if (resultCode == Activity.RESULT_CANCELED) {
        // keep this string the same as in iOS document picker plugin
        // https://github.com/iampossible/Cordova-DocPicker
        callback.error("User canceled.");
      } else {

        callback.error(resultCode);
      }
    }
  }

}
