package daryadelan.sandogh.zikey.com.daryadelan.tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import daryadelan.sandogh.zikey.com.daryadelan.R;


/**
 * Created by Zikey on 01/06/2017.
 */

public class ToolbarWrapper {



    Toolbar toolbar;

    private AppCompatActivity act;

    public ToolbarWrapper(AppCompatActivity act) {
        this.act = act;
    }

    public void initToolbarWithBackArrow(int toolbarID, String title, final View.OnClickListener onClickListener) {

        Toolbar toolbar = (Toolbar) act.findViewById(toolbarID);
        act.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (onClickListener != null)
                                                         onClickListener.onClick(v);
                                                     else act.finish();
                                                 }

                                             }

        );
        act.setTitle(title);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        act.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);
    }

    public void initToolbarWithBackArrow(int toolbarID, String title,String subTitle, final View.OnClickListener onClickListener) {

        Toolbar toolbar = (Toolbar) act.findViewById(toolbarID);

        toolbar.setSubtitle(subTitle);
        act.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (onClickListener != null)
                                                         onClickListener.onClick(v);
                                                     else act.finish();
                                                 }

                                             }

        );
        act.setTitle(title);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        act.getSupportActionBar().setSubtitle(subTitle);
        act.getSupportActionBar().setTitle(title);
        act.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_white_24dp);

    }

    public void initToolbarWithBackBlackArrow(int toolbarID, String title,String subTitle, final View.OnClickListener onClickListener) {

        Toolbar toolbar = (Toolbar) act.findViewById(toolbarID);

        toolbar.setSubtitle(subTitle);
        act.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (onClickListener != null)
                                                         onClickListener.onClick(v);
                                                     else act.finish();
                                                 }

                                             }

        );
        act.setTitle(title);
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        act.getSupportActionBar().setSubtitle(subTitle);
        act.getSupportActionBar().setTitle(title);
        act.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward_black_24dp);

    }



}
