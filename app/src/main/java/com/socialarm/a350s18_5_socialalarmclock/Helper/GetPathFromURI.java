package com.socialarm.a350s18_5_socialalarmclock.Helper;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

/**
 * Converts URI to actual path
 * References: https://stackoverflow.com/questions/26195731/android-get-real-path-from-uri?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */

public class GetPathFromURI {

    /**
     * Converts URI to string of absolute path of URI
     * @param context Context of the current activity
     * @param uri URI to be converted
     * @return String of actual path
     */
    public static String getPathFromURI(final Context context, final Uri uri) {
        // MediaStore (and general)
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            //cursor to query files
            Cursor cursor = null;
            final String column = "_data";

            String path = null;

            try {
                cursor = context.getContentResolver().query(uri, new String[] { column }, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    //get correct column
                    path = cursor.getString(cursor.getColumnIndexOrThrow(column));
                    cursor.close();
                    return path;
                }
            } catch (Exception e) {
                Log.e("Cursor cannot find path from uri", e.toString());
            }
            return path;
        }

        return null;
    }
}
