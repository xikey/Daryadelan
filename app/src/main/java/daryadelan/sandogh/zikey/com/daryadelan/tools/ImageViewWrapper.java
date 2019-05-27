package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;

import daryadelan.sandogh.zikey.com.daryadelan.R;


/**
 * Created by Zikey on 11/07/2017.
 */

public class ImageViewWrapper {

    private ImageView imgView;
    private String url;
    private File file;
    private Context context;
    private int placeHolder;

    public ImageViewWrapper defaultImage(int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public ImageViewWrapper(Context context) {
        this.context = context;
    }

    public ImageViewWrapper into(ImageView view) {
        this.imgView = view;
        return this;
    }


    public ImageViewWrapper FromUrl(String url) {
        this.url = url;
        return this;
    }

    public ImageViewWrapper FromFile(File file) {
        this.file = file;
        return this;
    }

       public void load() {

        Picasso.with(context).load(url).fit().centerInside()
                .placeholder(R.drawable.bg_product_avatar)
                .error(R.drawable.bg_product_avatar)
                .into(imgView);

    }

    public void loadCenterCrop() {

        Picasso.with(context).load(url).fit().centerCrop()
                .placeholder(R.drawable.bg_product_avatar)
                .error(R.drawable.bg_product_avatar)
                .into(imgView);


    }


    public void loadCenterCropWithoutAvatar() {

        Picasso.with(context).load(url).fit().centerCrop()
                .into(imgView);


    }

    public void loadCenterFit() {

        Picasso.with(context).load(url).fit()
                .placeholder(R.drawable.bg_product_avatar)
                .error(R.drawable.bg_product_avatar)
                .into(imgView);

    }

    public void loadFromSd() {

        Glide.with(context)
                .load(file) // path of the picture
                .into(imgView);

    }

}
