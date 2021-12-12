package App.mydonate.API;

import android.renderscript.Type;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import App.mydonate.Model.Donation;

public class DonationApi {
    //////////////////////////////////////////////////////////////////////////////////
    public static List<Donation> getAll(String call) {
        String json = Rest.get(call);
        //Log.v("donate", "JSON RESULT : " + json);
        Type collectionType = (Type) new TypeToken<List<Donation>>(){}.getType();

        return new Gson().fromJson(json, (java.lang.reflect.Type) collectionType);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static Donation get(String call,String id) {
        String json = Rest.get(call + "/" + id);
        Log.v("donate", "JSON RESULT : " + json);
        Type objType = (Type) new TypeToken<Donation>(){}.getType();

        return new Gson().fromJson(json, (java.lang.reflect.Type) objType);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String deleteAll(String call) {
        return Rest.delete(call);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String delete(String call, String id) {
        return Rest.delete(call + "/" + id);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String insert(String call,Donation donation) {
        Type objType = (Type) new TypeToken<Donation>(){}.getType();
        String json = new Gson().toJson(donation, (java.lang.reflect.Type) objType);

        return Rest.post(call,json);
    }
}
