package app.nostalking.com.locationtracker.utils;

import com.google.gson.Gson;
import java.io.InputStream;
import java.lang.reflect.Type;

public class IgnoreExtra {

    public <T> T fromJsonIgnoreExtra(InputStream is, Class<T> classOfT){
        String modifyText;
        String body = convertStreamToString(is);

        modifyText = body.substring(0, body.lastIndexOf("}") +  1);
        Gson gson = new Gson();
        T object;
        object = gson.fromJson(modifyText,(Type) classOfT);

        return object;
    }

    public String ignoreExtraText(InputStream is){
        String modifyText;
        String body = convertStreamToString(is);

        modifyText = body.substring(0, body.lastIndexOf("}") +  1);
        return  modifyText;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : ApiStates.BLANK;
    }
}