package daryadelan.sandogh.zikey.com.daryadelan;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.customview.DownloaderFragment;
import daryadelan.sandogh.zikey.com.daryadelan.customview.Indicator;
import daryadelan.sandogh.zikey.com.daryadelan.customview.PageFragment;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.News;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.UrlBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AppInfoServerWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.NewsWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAdvertise;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAppInfo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.INews;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.AdvertiseServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.AppInfoServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.NewsServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.sqlite.UserSqliteRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CalendarWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.ImageViewWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String KEY_DOWNLOAD_URL = "http://";
    private final String KEY_APP_NAME = "Daryadelan";

    private final String KEY_NEWS_NAME = "news";


    public final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 11;

    private IAdvertise advertiseRepo;

    int advertisePageCount = 3;
    ViewPager pager;
    Boolean pageChanged = true;

    Handler slideHandler = null;

    private CardView crdPayrolls;
    private CardView lyAhkam;
    boolean doubleBackToExitPressedOnce = false;

    private TextView txtUserType;
    private TextView txtUserName;
    private TextView txtMoreNews;

    private IUser userRepo;
    private IUser userSqliteRepo;
    private IAppInfo appInfoRepo;
    private User loadedUser;
    private LinearLayout lyProgress;
    private LinearLayout lyNews;

    private INews newsRepo;

    private RecyclerView rvItem;
    private NewsAdapter adapter;

    private CardView crdNews;
    private CardView crdGallery;
    private CardView crdCamps;
    private CardView crdRequestsList;

    private LinearLayout lyRowOne;

    //for getting app from server if permission is first time
    private String appUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView =  getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);

        }
        setContentView(R.layout.activity_main);


        initToolbar();
        initRepo();
        initViews();
        initNewsRecycleView();
        getUserSavedData();
        initSlideAutoChanger();
        initListeners();
        initNavHeader();



    }

    private void checkForUpdates() {
        if (appInfoRepo == null)
            return;

        if (loadedUser == null)
            return;

        appInfoRepo.appVersionChecker(getApplicationContext(), BuildConfig.VERSION_NAME, loadedUser.getTokenType(), loadedUser.getToken(), new IRepoCallBack<AppInfoServerWrapper>() {
            @Override
            public void onAnswer(AppInfoServerWrapper answer) {
                if (answer != null && answer.getAppInfo() != null) {

                    String url = BuildConfig.IPAddress + answer.getAppInfo().getAppPath();


                    updateApp(url, answer.getAppInfo().getAppVersinDescription());

                } else {

                }
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else  {
            View decorView =  getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);

        }

    }

    private void getUserSavedData() {


        if (userSqliteRepo == null)
            return;
        userSqliteRepo.getUserDatas(getApplicationContext(), new IRepoCallBack<User>() {
            @Override
            public void onAnswer(User answer) {

                if (answer == null || answer.getResultId() < 0) {
                    new CustomDialogBuilder().showAlert(MainActivity.this, "خطا در دریافت اطلاعات کاربر. لطفا عملیات ورود را مجددا انجام نمایید");
                    return;
                }

                if (!answer.getLoginDate().equals(CalendarWrapper.getCurrentPersianDate())) {
//                    new CustomDialogBuilder().showAlert(MainActivity.this, "تاریخ انقضا استفاده از برنامه تمام شده است. لطفا عملیات ورود را مجددا انجام نمایید.", new CustomAlertDialog.OnCancelClickListener() {
//                        @Override
//                        public void onClickCancel(DialogFragment fragment) {
//                            fragment.dismiss();
//                            LoginActivity.start(MainActivity.this, answer.getMobile());
//                            finish();
//                            return;
//                        }
//
//                        @Override
//                        public void onClickOutside(DialogFragment fragment) {
//                            fragment.dismiss();
//                            LoginActivity.start(MainActivity.this,answer.getMobile());
//                            finish();
//                            return;
//                        }
//                    });

                    LoginActivity.start(MainActivity.this, answer.getMobile());
                    finish();
                    return;

                }
                loadedUser = answer;
                initContent();
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }

    private void initNavHeader() {
        try {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View hView = navigationView.getHeaderView(0);

            final TextView txtUserFullName = (TextView) hView.findViewById(R.id.txtUserFullName);

            if (loadedUser == null)
                return;

            if (!TextUtils.isEmpty(loadedUser.getFirstName())) {
                txtUserFullName.setText(loadedUser.getFirstName() + " " + loadedUser.getLastName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUserExtraInfos() {
        if (userRepo == null)
            return;

        if (loadedUser != null && loadedUser.isUserDataComplete()) {
            initUserDatas(loadedUser);
        } else
            userRepo.userInfo(getApplicationContext(), loadedUser.getTokenType(), loadedUser.getToken(), new IRepoCallBack<User>() {
                @Override
                public void onAnswer(User answer) {
                    if (answer == null) {
                        new CustomDialogBuilder().showYesNOCustomAlert(MainActivity.this, "خطا در دریافت اطلاعات", "متاسفانه خطایی هنگام دریافت اطلاعات کاربری شما رخ داده است، لطفا پس از اطمینان از فعال بودن اینترنت دستگاه مجددا تلاش نمایید!", "تلاش مجدد", "خروج", new CustomAlertDialog.OnActionClickListener() {
                            @Override
                            public void onClick(DialogFragment fragment) {
                                fragment.dismiss();
                                getUserExtraInfos();
                            }
                        }, new CustomAlertDialog.OnCancelClickListener() {
                            @Override
                            public void onClickCancel(DialogFragment fragment) {
                                fragment.dismiss();
                                finish();

                            }

                            @Override
                            public void onClickOutside(DialogFragment fragment) {
                                fragment.dismiss();
                                finish();

                            }
                        });
                        return;
                    }
                    initUserDatas(answer);

                }

                @Override
                public void onError(Throwable error) {
                    new CustomDialogBuilder().showYesNOCustomAlert(MainActivity.this, "خطا در دریافت اطلاعات", "متاسفانه خطایی هنگام دریافت اطلاعات کاربری شما رخ داده است، لطفا پس از اطمینان از فعال بودن اینترنت دستگاه مجددا تلاش نمایید!", "تلاش مجدد", "خروج", new CustomAlertDialog.OnActionClickListener() {
                        @Override
                        public void onClick(DialogFragment fragment) {
                            fragment.dismiss();
                            getUserExtraInfos();
                        }
                    }, new CustomAlertDialog.OnCancelClickListener() {
                        @Override
                        public void onClickCancel(DialogFragment fragment) {
                            fragment.dismiss();
                            finish();

                        }

                        @Override
                        public void onClickOutside(DialogFragment fragment) {
                            fragment.dismiss();
                            finish();

                        }
                    });

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onProgress(int p) {

                }
            });
    }

    private void getNews() {
        if (newsRepo == null)
            return;

        newsRepo.news(getApplicationContext(), KEY_NEWS_NAME, 0, loadedUser.getTokenType(), loadedUser.getToken(), new IRepoCallBack<NewsWrapper>() {
            @Override
            public void onAnswer(NewsWrapper answer) {
                if (answer == null)
                    return;
                if (answer.getNews() == null || answer.getNews().size() == 0)
                    return;

                lyNews.setVisibility(View.VISIBLE);
                if (adapter != null)
                    adapter.setItems(answer.getNews());
            }

            @Override
            public void onError(Throwable error) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }

    private void initContent() {


        if (loadedUser == null)
            return;


        saveAdvertiseUrl();
        getUserExtraInfos();
        checkForUpdates();

        try {
            if (!TextUtils.isEmpty(loadedUser.getPersonTypeName()))
                txtUserType.setText(loadedUser.getPersonTypeName());
//            if (loadedUser.getPersonalCode() != 0)
//                txtPersonelCode.setText("" + loadedUser.getPersonalCode());
            if (!TextUtils.isEmpty(loadedUser.getFirstName()))
                txtUserName.setText(loadedUser.getFirstName() + " " + loadedUser.getLastName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRepo() {

        if (advertiseRepo == null)
            advertiseRepo = new AdvertiseServerRepo();

        if (userRepo == null)
            userRepo = new UserServerRepo();


        if (userSqliteRepo == null)
            userSqliteRepo = new UserSqliteRepo();

        if (newsRepo == null)
            newsRepo = new NewsServerRepo();

        if (appInfoRepo == null)
            appInfoRepo = new AppInfoServerRepo();
    }


    private void initListeners() {

        crdPayrolls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserPersmission())
                    PayrollHeaderActivity.start(MainActivity.this);
            }
        });
        lyAhkam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserPersmission())
                    AhkamHeaderActivity.start(MainActivity.this);
            }
        });

        txtMoreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.start(MainActivity.this);
            }
        });

        crdNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsActivity.start(MainActivity.this);
            }
        });
        crdGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleriesActivity.start(MainActivity.this);
            }
        });

        crdCamps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserPersmission())
                    CampsActivity.start(MainActivity.this);
            }
        });

        crdRequestsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserPersmission())
                    CampsRequestsHistoryActivity.start(MainActivity.this);

            }
        });


    }

    private void initViews() {
        crdPayrolls = (CardView) findViewById(R.id.crdPayrolls);
        lyAhkam = (CardView) findViewById(R.id.lyAhkam);
        crdNews = (CardView) findViewById(R.id.crdNews);
        crdGallery = (CardView) findViewById(R.id.crdGallery);

        txtUserType = (TextView) findViewById(R.id.txtUserType);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtMoreNews = (TextView) findViewById(R.id.txtMoreNews);
        lyNews = (LinearLayout) findViewById(R.id.lyNews);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyProgress.setVisibility(View.VISIBLE);
        rvItem = (RecyclerView) findViewById(R.id.rtItem);
        crdCamps = (CardView) findViewById(R.id.crdCamps);
        crdRequestsList = (CardView) findViewById(R.id.crdRequestsList);
        lyRowOne = (LinearLayout) findViewById(R.id.lyRowOne);
        lyNews.setVisibility(View.GONE);


        try {
            FontChanger.applyTitleFont(findViewById(R.id.lyMainHeader));
            FontChanger.applyYekanFont(findViewById(R.id.lblQuickShortcut));
            FontChanger.applyYekanFont(findViewById(R.id.lblUserDatas));
            FontChanger.applyYekanFont(findViewById(R.id.lblQuickNews));
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ViewGroup.LayoutParams layoutParams = lyRowOne.getLayoutParams();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (width * (0.39));
            layoutParams.height = height;
            lyRowOne.setLayoutParams(layoutParams);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initNewsRecycleView() {
        if (adapter == null)
            adapter = new NewsAdapter();

        if (rvItem == null)
            initViews();

        rvItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvItem.setAdapter(adapter);
    }

    private void initToolbar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toasty.info(MainActivity.this, getString(R.string.doble_tap_to_exit)).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exit) {
            String question = "مایل به خروج از حساب کاربری خود میباشید؟";
            question += "\n\n" + "*توجه*" + "\n" + "در صورت خروج از حساب کاربری، عملیات ورود به حساب کاربری باید مجددا انجام پذیرد.";
            new CustomDialogBuilder().showYesNOCustomAlert(MainActivity.this, "خروج از حساب کاربری", question, "خروج", "انصراف", new CustomAlertDialog.OnActionClickListener() {
                @Override
                public void onClick(DialogFragment fragment) {
                    exitApp();
                    fragment.dismiss();
                }
            }, null);
        } else if (id == R.id.nav_hokm) {
            if (checkUserPersmission())
                AhkamHeaderActivity.start(MainActivity.this);
        } else if (id == R.id.nav_payroll) {
            if (checkUserPersmission())
                PayrollHeaderActivity.start(MainActivity.this);
        } else if (id == R.id.nav_news) {

            NewsActivity.start(MainActivity.this);
        } else if (id == R.id.nav_gallery) {

            GalleriesActivity.start(MainActivity.this);
        }
// else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Runnable slideChanger = new Runnable() {
        @Override
        public void run() {
            if (!pageChanged) {
                if (pager != null) {
                    int i = pager.getCurrentItem();
                    i = i + 1;
                    if (i >= advertisePageCount)
                        i = 0;
                    pager.setCurrentItem(i);
                }
            }
            pageChanged = false;
            slideHandler.postDelayed(slideChanger, 5000);
        }
    };


    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    private void initSlideAutoChanger() {
        slideHandler = new Handler(Looper.getMainLooper());
        slideChanger.run();
    }

    private void saveAdvertiseUrl() {
        initAdvertiseBox();

        if (advertiseRepo == null)
            return;
        advertiseRepo.getAdvertise(getApplicationContext(), "MainSlider", loadedUser.getTokenType(), loadedUser.getToken(), new IRepoCallBack<AdvertiseWrapper>() {
            @Override
            public void onAnswer(AdvertiseWrapper answer) {
                if (answer != null && answer.getData() != null && answer.getData().size() != 0) {
                    try {
                        advertisePageCount = answer.getData().size();
                        if (answer.getData().get(0) != null)
                            SessionManagement.getInstance(getApplicationContext()).setAdvertise_1Url(new UrlBuilder(getApplicationContext()).getAdvertiseImageUrl(answer.getData().get(0)));
                        if (answer.getData().get(1) != null)
                            SessionManagement.getInstance(getApplicationContext()).setAdvertise_2Url(new UrlBuilder(getApplicationContext()).getAdvertiseImageUrl(answer.getData().get(1)));
                        if (answer.getData().get(2) != null)
                            SessionManagement.getInstance(getApplicationContext()).setAdvertise_3Url(new UrlBuilder(getApplicationContext()).getAdvertiseImageUrl(answer.getData().get(2)));
                        if (answer.getData().get(3) != null)
                            SessionManagement.getInstance(getApplicationContext()).setAdvertise_4Url(new UrlBuilder(getApplicationContext()).getAdvertiseImageUrl(answer.getData().get(3)));
                        if (answer.getData().get(4) != null)
                            SessionManagement.getInstance(getApplicationContext()).setAdvertise_5Url(new UrlBuilder(getApplicationContext()).getAdvertiseImageUrl(answer.getData().get(4)));


                    } catch (Exception ex) {
                        LogWrapper.loge("MainActivity_saveAdvertiseUrl :", ex);
                    }
                }
                initAdvertiseBox();
            }

            @Override
            public void onError(Throwable error) {
                initAdvertiseBox();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }

    private void initAdvertiseBox() {
        try {
            View advertiseHeaderBox = findViewById(R.id.advertiseHeaderBox);

            int width = getResources().getDisplayMetrics().widthPixels;

            int height = width / 2;
            ViewGroup.LayoutParams mainParams = advertiseHeaderBox.getLayoutParams();
            mainParams.height = height;
            advertiseHeaderBox.setLayoutParams(mainParams);

            final TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), height);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);
            final Indicator indexBox = (Indicator) findViewById(R.id.indexBox);
            indexBox.setViewPager(pager);


            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pageChanged = true;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {
        int height = 300;

        public TabsPagerAdapter(FragmentManager fm, int height) {
            super(fm);
            this.height = height;
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position + 1, height);
        }

        @Override
        public int getCount() {
            return advertisePageCount;
        }

    }

    private void exitApp() {

        LoginActivity.start(MainActivity.this, loadedUser.getMobile());
        finish();
    }

    private boolean checkUserPersmission() {
        if (loadedUser == null) {
            new CustomDialogBuilder().showAlert(MainActivity.this, "خطا در دریافت اطلاعات کاربر. لطفا عملیات ورود را مجددا انجام نمایید");
            return false;
        }

        if (loadedUser.isPersonGuest()) {
            new CustomDialogBuilder().showAlert(MainActivity.this, "دسترسی به این بخش برای کاربر مهمان وجود ندارد");
            return false;
        }

        return true;
    }


    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

        ArrayList<News> items;

        public void setItems(ArrayList<News> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_main_item, parent, false);
            if (view == null)
                return null;
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            if (holder == null)
                return;


            holder.position = position;
            News news = items.get(position);
            if (news == null)
                return;

            holder.news = news;
            String url = BuildConfig.IPAddress + "/" + news.getPostThumbImage();
            holder.txtNew.setText(news.getPostTitle());

            new ImageViewWrapper(getApplicationContext()).FromUrl(url).defaultImage(R.drawable.bg_product_avatar).into(holder.imgProduct).load();

        }

        @Override
        public int getItemCount() {
            return (items == null) ? 0 : items.size();
        }

        public class NewsHolder extends RecyclerView.ViewHolder {

            LinearLayout lyRoot;
            TextView txtNew;

            ImageView imgProduct;
            News news;

            public int position = -1;


            public NewsHolder(View v) {
                super(v);

                lyRoot = (LinearLayout) v.findViewById(R.id.lyRoot);
                txtNew = (TextView) v.findViewById(R.id.txtNew);
                imgProduct = v.findViewById(R.id.imgProduct);
                FontChanger.applyTitleFont(txtNew);
                lyRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenedNewsActivity.start(MainActivity.this, news);
                    }
                });

            }
        }


    }

    private void initUserDatas(User answer) {
        try {
            lyProgress.setVisibility(View.GONE);
            txtUserName.setText(answer.getFirstName() + " " + answer.getLastName());
//            txtPersonelCode.setText("" + answer.getPersonalCode());
            txtUserType.setText("" + answer.getPersonTypeName());

            loadedUser.setFirstName(answer.getFirstName());
            loadedUser.setPersonalCode(answer.getPersonalCode());
            loadedUser.setLastName(answer.getLastName());
            loadedUser.setPersonType(answer.getPersonType());

            UserInstance.getInstance().setUser(loadedUser);
            getNews();

            if (userSqliteRepo != null)
                userSqliteRepo.updateUserDatas(getApplicationContext(), answer, new IRepoCallBack<User>() {
                    @Override
                    public void onAnswer(User answer) {

                    }

                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onProgress(int p) {

                    }
                });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initStgoragePermission(String url) {

        int writeExternalStorage = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        appUrl = url;

        if (writeExternalStorage != 0) {
            requestWriteExternalStoragePermission();
        } else {
            if (!TextUtils.isEmpty(url))
                startDownloadingApp(url);
        }
    }

    private void startDownloadingApp(String url) {

        new CustomDialogBuilder().showProgressDialog(MainActivity.this, "دریافت فایل", url, BuildConfig.FILES_DIRECTORI, KEY_APP_NAME, "apk", true, new DownloaderFragment.OnCancelClickListener() {
            @Override
            public void onClickCancel(DialogFragment fragment) {
                fragment.dismiss();
            }

            @Override
            public void onClickOutside(DialogFragment fragment) {

            }
        });

    }

    private void updateApp(String appUrl, String title) {

        String qst1 = "نسخه جدید نرم افزار در دسترس میباشد.\n\nپس از دریافت فایل، نرم افزار به صورت خودکار اقدام به بروز رسانی نرم افزار مینماید.\n\n";
        String qst = "\n\n" + title;


        new CustomDialogBuilder().showYesNOCustomAlert_HTML(this, "دریافت نسخه نهایی", qst1 + qst, "دریافت فایل", null, new CustomAlertDialog.OnActionClickListener() {
            @Override
            public void onClick(DialogFragment fragment) {
                fragment.dismiss();

                initStgoragePermission(appUrl);


            }
        }, new CustomAlertDialog.OnCancelClickListener() {
            @Override
            public void onClickCancel(DialogFragment fragment) {
                fragment.dismiss();
            }

            @Override
            public void onClickOutside(DialogFragment fragment) {
                fragment.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloadingApp(appUrl);
                }

            }
        }
    }


    private int requestWriteExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        return PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;
    }

}
