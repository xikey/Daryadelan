package daryadelan.sandogh.zikey.com.daryadelan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import daryadelan.sandogh.zikey.com.daryadelan.customview.CustomAlertDialog;
import daryadelan.sandogh.zikey.com.daryadelan.customview.Indicator;
import daryadelan.sandogh.zikey.com.daryadelan.customview.PageFragment;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserInstance;
import daryadelan.sandogh.zikey.com.daryadelan.model.SessionManagement;
import daryadelan.sandogh.zikey.com.daryadelan.model.UrlBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.model.User;
import daryadelan.sandogh.zikey.com.daryadelan.model.serverWrapper.AdvertiseWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IAdvertise;
import daryadelan.sandogh.zikey.com.daryadelan.repo.instanseRepo.IUser;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.AdvertiseServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.serverRepo.UserServerRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.sqlite.UserSqliteRepo;
import daryadelan.sandogh.zikey.com.daryadelan.repo.tools.IRepoCallBack;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private IAdvertise advertiseRepo;

    int advertisePageCount = 3;
    ViewPager pager;
    Boolean pageChanged = true;

    Handler slideHandler = null;

    private CardView crdPayrolls;
    private CardView lyAhkam;
    boolean doubleBackToExitPressedOnce = false;

    private TextView txtUserType;
    private TextView txtPersonelCode;
    private TextView txtUserName;

    private IUser userRepo;
    private IUser userSqliteRepo;

    private User loadedUser;
    private LinearLayout lyProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();
        initRepo();
        initViews();
        getUserSavedData();
        initSlideAutoChanger();
        initListeners();
        initNavHeader();

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


                try {
                    lyProgress.setVisibility(View.GONE);
                    txtUserName.setText(answer.getFirstName() + " " + answer.getLastName());
                    txtPersonelCode.setText("" + answer.getPersonalCode());
                    txtUserType.setText("" + answer.getPersonTypeName());

                    loadedUser.setFirstName(answer.getFirstName());
                    loadedUser.setPersonalCode(answer.getPersonalCode());
                    loadedUser.setLastName(answer.getLastName());
                    loadedUser.setPersonType(answer.getPersonType());

                    UserInstance.getInstance().setUser(loadedUser);

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

        try {
            if (!TextUtils.isEmpty(loadedUser.getPersonTypeName()))
                txtUserType.setText(loadedUser.getPersonTypeName());
            if (loadedUser.getPersonalCode() != 0)
                txtPersonelCode.setText("" + loadedUser.getPersonalCode());
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
    }

    private void initViews() {
        crdPayrolls = (CardView) findViewById(R.id.crdPayrolls);
        lyAhkam = (CardView) findViewById(R.id.lyAhkam);

        txtUserType = (TextView) findViewById(R.id.txtUserType);
        txtPersonelCode = (TextView) findViewById(R.id.txtPersonelCode);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        lyProgress.setVisibility(View.VISIBLE);

        try {
            FontChanger.applyTitleFont(findViewById(R.id.lyMainHeader));
            FontChanger.applyYekanFont(findViewById(R.id.lblQuickShortcut));
            FontChanger.applyYekanFont(findViewById(R.id.lblUserDatas));
            FontChanger.applyYekanFont(findViewById(R.id.lblQuickNews));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

     LoginActivity.start(MainActivity.this);
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
}
