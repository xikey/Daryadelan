package daryadelan.sandogh.zikey.com.daryadelan.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.razanpardazesh.razanlibs.Tools.Convertor;

import daryadelan.sandogh.zikey.com.daryadelan.R;


/**
 * Created by Torabi on 9/8/2016.
 */

public class Indicator extends LinearLayout {

    private ViewPager pager;
    private int pageNumber = 0;
    private int selectedPage = 0;


    public Indicator(Context context) {
        super(context);
        init();
    }

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Indicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        if (isInEditMode())
            return;

        setOrientation(LinearLayout.HORIZONTAL);

        if (Build.VERSION.SDK_INT > 17)
            setLayoutDirection(LAYOUT_DIRECTION_LTR);
    }

    public void setViewPager(ViewPager pager) {
        if (pager == null)
            return;

        this.pager = pager;
        this.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setAlpha(0.5f);
                }

                getChildAt(position).setAlpha(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        notifyDatabasChange();
    }

    public void notifyDatabasChange() {
        if (pager == null || pager.getAdapter() == null) {
            selectedPage = 0;
            pageNumber = 0;
            return;
        }

        selectedPage = pager.getCurrentItem();
        pageNumber = pager.getAdapter().getCount();
        initView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        removeAllViews();

        for (int i = 0; i < pageNumber; i++) {
            View child = new View(getContext());
            LayoutParams params = new LayoutParams(Convertor.toPixcel(10, getContext()), Convertor.toPixcel(10, getContext()));

            if (i != 0) {
                int left = Convertor.toPixcel(5, getContext());
                if (Build.VERSION.SDK_INT < 17)
                    params.setMargins(left, 0, 0, 0);
                else
                    params.setMarginStart(left);
            }

            child.setLayoutParams(params);
            child.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_indicator_cycle, null));

            if (selectedPage == i)
                child.setAlpha(1);
            else
                child.setAlpha(0.5f);
            addView(child);
        }
    }

}
