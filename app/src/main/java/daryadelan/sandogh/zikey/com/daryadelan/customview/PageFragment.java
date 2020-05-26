package daryadelan.sandogh.zikey.com.daryadelan.customview;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import daryadelan.sandogh.zikey.com.daryadelan.R;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;


/**
 * Created by Torabi on 9/6/2016.
 */

public class PageFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";
    private static final String ARG_PAGE_HEIGHT = "ARG_PAGE_HEIGHT";

    public PageFragment() {
    }

    public static PageFragment newInstance(int page, int height) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        args.putInt(ARG_PAGE_HEIGHT, height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_layout, container, false);

        final ImageView imgAdv = (ImageView) rootView.findViewById(R.id.imgAdv);

        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
        int height = getArguments().getInt(ARG_PAGE_HEIGHT, 300);

        String temp = "";

        final int placeHolder = 0;

        switch (page) {
            case 1:
                try {
                    String url = SessionManagement.getInstance(getActivity()).getAdvertise_1Url();
                   new ImageViewWrapper(getActivity()).FromUrl(url).into(imgAdv).defaultImage(placeHolder).load();
                } catch (Exception ex) {
                    LogWrapper.loge("PageFragment_onCreateView_Exception: ", ex);
                }

                imgAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

//                           UriParser.run(getActivity(), SessionManagement.getInstance(getActivity()).getAdvertise_1Uri());
                        } catch (Exception ex) {
//                            LogWrapper.loge("PageFragment_onClick_Exception: ", ex);
                        }
                    }
                });
                break;
            case 2:
                try {
                     String url = SessionManagement.getInstance(getActivity()).getAdvertise_2Url();
                   new ImageViewWrapper(getActivity()).FromUrl(url).into(imgAdv).defaultImage(placeHolder).load();
                } catch (Exception ex) {
                    LogWrapper.loge("PageFragment_onCreateView_Exception: ", ex);
                }

                imgAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
//                            UriParser.run(getActivity(), SessionManagement.getInstance(getActivity()).getAdvertise_2Uri());

                        } catch (Exception ex) {
//                            LogWrapper.loge("PageFragment_onClick_Exception: ", ex);
                        }
                    }
                });
                break;
            case 3:
                try {
                     String url = SessionManagement.getInstance(getActivity()).getAdvertise_3Url();
                    new ImageViewWrapper(getActivity()).FromUrl(url).into(imgAdv).defaultImage(placeHolder).load();
                } catch (Exception ex) {
                    LogWrapper.loge("PageFragment_onCreateView_Exception: ", ex);
                }


                imgAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
//                           UriParser.run(getActivity(), SessionManagement.getInstance(getActivity()).getAdvertise_3Uri());
                        } catch (Exception ex) {
//                            LogWrapper.loge("PageFragment_onClick_Exception: ", ex);
                        }

                    }
                });
                break;

            case 4:
                try {
                    String url = SessionManagement.getInstance(getActivity()).getAdvertise_4Url();
                    new ImageViewWrapper(getActivity()).FromUrl(url).into(imgAdv).defaultImage(placeHolder).load();
                } catch (Exception ex) {
                    LogWrapper.loge("PageFragment_onCreateView_Exception: ", ex);
                }


                imgAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
//                           UriParser.run(getActivity(), SessionManagement.getInstance(getActivity()).getAdvertise_3Uri());
                        } catch (Exception ex) {
//                            LogWrapper.loge("PageFragment_onClick_Exception: ", ex);
                        }

                    }
                });
                break;


            case 5:
                try {
                    String url = SessionManagement.getInstance(getActivity()).getAdvertise_5Url();
                    new ImageViewWrapper(getActivity()).FromUrl(url).into(imgAdv).defaultImage(placeHolder).load();
                } catch (Exception ex) {
                    LogWrapper.loge("PageFragment_onCreateView_Exception: ", ex);
                }


                imgAdv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
//                           UriParser.run(getActivity(), SessionManagement.getInstance(getActivity()).getAdvertise_3Uri());
                        } catch (Exception ex) {
//                            LogWrapper.loge("PageFragment_onClick_Exception: ", ex);
                        }

                    }
                });
                break;

        }

        final ViewGroup.LayoutParams params = imgAdv.getLayoutParams();
        params.height = height;
        imgAdv.setLayoutParams(params);

        return rootView;
    }



    public void onClick(View view) {
//        ProductActivity.openActivity(getActivity(), new ProductSummary());
    }


}
