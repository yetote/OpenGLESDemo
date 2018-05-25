package com.example.ether.openglesdemo.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextRecourseReader {
    private static final String TAG = "TextRecourseReader";

    public static String readTextFileFromRecourse(Context context, int recourseId) throws IOException {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = context.getResources().openRawResource(recourseId);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String nextLine ;
            while ((nextLine = bufferedReader.readLine()) != null) {
                sb.append(nextLine);
                sb.append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "readTextFileFromRecourse: " + e);
        }
        return sb.toString();
    }
}
