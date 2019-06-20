package daryadelan.sandogh.zikey.com.daryadelan.model;

import android.content.Context;
import android.text.TextUtils;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;

/**
 * Created by Zikey on 06/03/2018.
 */

public class UrlBuilder {

    Context context;

    public UrlBuilder(Context context) {
        this.context = context;
    }


    public String getAdvertiseImageUrl(Advertise ad) {

        if (ad == null)
            return null;

        if (TextUtils.isEmpty(ad.getImagepath()))
            return null;

//        Calendar c = Calendar.getInstance();
//        c.setTime(ad.getCreateDate());
//        int month = c.get(Calendar.MONTH) + 1;
//        return BuildConfig.URL + "images/advertise/" + c.get(Calendar.YEAR) +
//                "/" + month + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + ad.getId() + "/" + ad.getId() + "-android.jpg";

        String url=BuildConfig.IPAddress;
        return url+ad.getImagepath();

    }


}
