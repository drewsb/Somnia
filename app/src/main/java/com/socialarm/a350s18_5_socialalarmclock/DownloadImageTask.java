package com.socialarm.a350s18_5_socialalarmclock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Used to download images online and convert into a Bitmap object, which is then
 * used to set the image of an image view.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /**
     * Returns a Bitmap object representing an image at the given url
     * @param urls
     * @return Bitmap object
     */
    protected Bitmap doInBackground(String... urls) {
        String url= urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return icon;
    }

    /**
     * Set image of the image view field
     * @param result
     */
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
