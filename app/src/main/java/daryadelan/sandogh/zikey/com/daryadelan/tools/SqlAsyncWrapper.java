package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.content.Context;

import com.razanpardazesh.razanlibs.Tools.AsyncWrapper;

/**
 * Created by Zikey on 04/09/2017.
 */

public class SqlAsyncWrapper extends AsyncWrapper {

    private Context context;

    public Context getContext() {
        return context;
    }

    @Override
    public void run(Context context, Object... params) {
        super.run(context, params);
        this.context = context;
    }
}
