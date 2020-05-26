package daryadelan.sandogh.zikey.com.daryadelan.tools;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import rx.subjects.BehaviorSubject;


/**
 * Created by Zikey on 01/06/2017.
 */

public class RxSearch {

    public static BehaviorSubject<String> fromSearchView(@NonNull final SearchView searchView) {
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

        return subject;
    }
}