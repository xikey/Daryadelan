package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import java.util.concurrent.TimeUnit;


import daryadelan.sandogh.zikey.com.daryadelan.R;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by MTorabi on 24/09/2017.
 */

public class SearchViewWrapper {

    private final int KEY_SEARCH_DELAY = 500; // unit is MILLISECONDS

    private AppCompatActivity act;

    public SearchViewWrapper(AppCompatActivity act) {
        this.act = act;
    }

    public SearchView initSearchView(int searchId, int menuResId, Menu menu, final SearchClickListener listener) {



        MenuInflater inflater = act.getMenuInflater();
        inflater.inflate(menuResId, menu);

        SearchManager searchManager =
                (SearchManager) act.getSystemService(Context.SEARCH_SERVICE);

        if (searchId == 0) {
            return null;
        }
        try {

        } catch (Exception ex) {
            LogWrapper.loge("SearchViewWrapper_initSearchView_Exception: ", ex);
        }

        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(searchId));

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(act.getComponentName()));

        searchView.setQueryHint(
                act.getString(R.string.search));


        if (searchView == null)
            return null;


        final BehaviorSubject<String> subject = BehaviorSubject.create("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });

        subject.debounce(KEY_SEARCH_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (listener != null)
                            listener.onSearch(s);

                    }
                });

        return searchView;
    }


    public SearchView initSearchView(int searchId, int menuResId, Menu menu,boolean collapse, final SearchClickListener listener) {

        MenuInflater inflater = act.getMenuInflater();
        inflater.inflate(menuResId, menu);

        SearchManager searchManager =
                (SearchManager) act.getSystemService(Context.SEARCH_SERVICE);

        if (searchId == 0) {
            return null;
        }
        try {

        } catch (Exception ex) {
            LogWrapper.loge("SearchViewWrapper_initSearchView_Exception: ", ex);
        }

        final SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(menu.findItem(searchId));

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(act.getComponentName()));

        searchView.setQueryHint(
                act.getString(R.string.search));

        searchView.setIconified(collapse);


        if (searchView == null)
            return null;


        final BehaviorSubject<String> subject = BehaviorSubject.create("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subject.onNext(newText);
                return true;
            }
        });

        subject.debounce(KEY_SEARCH_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                    }

                    @Override
                    public void onNext(String s) {
                        if (listener != null)
                            listener.onSearch(s);

                    }
                });
        return searchView;
    }


    public interface SearchClickListener {

        public void onSearchBoxOpened();

        public void onSearchBoxClosed();

        public void onSearch(String searchKey);
    }
}
