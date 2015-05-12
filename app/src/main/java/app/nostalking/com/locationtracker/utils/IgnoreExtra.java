package app.nostalking.com.locationtracker.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by Applaudo Dev on 5/12/2015.
 */
public class IgnoreExtra {

    public <T> T ignoreExtraCode(InputStream is,Class<T> classOfT){
        String modifyText;
        String body = convertStreamToString(is);

        modifyText = body.substring(0, body.lastIndexOf("}") +  1);
        Gson gson = new Gson();
        T object;
        object = gson.fromJson(modifyText,(Type) classOfT);

        return object;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
