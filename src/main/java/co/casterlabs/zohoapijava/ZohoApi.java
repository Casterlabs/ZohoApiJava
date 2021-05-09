package co.casterlabs.zohoapijava;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ZohoApi {

    public static final Gson GSON = new GsonBuilder()
        .disableHtmlEscaping()
        .create();

}
