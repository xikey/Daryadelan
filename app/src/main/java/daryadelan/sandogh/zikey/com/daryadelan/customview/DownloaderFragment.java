package daryadelan.sandogh.zikey.com.daryadelan.customview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.razanpardazesh.razanlibs.Tools.Convertor;

import java.io.File;

import daryadelan.sandogh.zikey.com.daryadelan.R;
import daryadelan.sandogh.zikey.com.daryadelan.tools.CustomDialogBuilder;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FileAddressGenerator;
import daryadelan.sandogh.zikey.com.daryadelan.tools.FontChanger;
import daryadelan.sandogh.zikey.com.daryadelan.tools.LogWrapper;
import daryadelan.sandogh.zikey.com.daryadelan.tools.NumberSeperator;


/**
 * Created by Zikey on 23/10/2017.
 */

public class DownloaderFragment extends DialogFragment {

    private static final String KEY_ALERT_DIALOG = "ALERT_DIALOG";

    private DownloaderFragment dialog;

    int downloadID;
    public boolean isCanceleed = true;

    //Views
    private ViewGroup root;
    private RelativeLayout lyContent;
    private RelativeLayout lyBackground;
    private TextView txtTitle;
    private TextView txtComment;

    private TextView txtDownloadedSize;
    private TextView txtTotalSize;

    private ProgressBar determinateBar;

    private Button btnClose;


    private OnCancelClickListener onCancleClickListener;

    private String title;
    private String question;
    private String submitText;
    private String cancelText;

    private String url;
    private String appName;
    private String filePath;
    private String fileExtension;
    boolean installAPK;


    public DownloaderFragment setUrl(String url) {
        this.url = url;
        return this;
    }

    public DownloaderFragment setInstallAPK(boolean installAPK) {
        this.installAPK = installAPK;
        return this;
    }

    public DownloaderFragment setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public DownloaderFragment setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public DownloaderFragment setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public DownloaderFragment setOnCancleClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancleClickListener = onCancelClickListener;
        return this;
    }

    public DownloaderFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public DownloaderFragment setQuestion(String question) {
        this.question = question;
        return this;
    }

    public DownloaderFragment setSubmitText(String submitText) {
        this.submitText = submitText;
        return this;
    }

    public DownloaderFragment setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public DownloaderFragment setDialog(DownloaderFragment dialog) {
        this.dialog = dialog;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setStatusBarColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

        }

        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (onCancleClickListener != null)
            onCancleClickListener.onClickOutside(DownloaderFragment.this);

        try {
            PRDownloader.cancelAll();
        } catch (Exception ex) {
            LogWrapper.loge("DownloaderFragment_onDismiss_Exception: ", ex);
        }

        super.onDismiss(dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.progress_alert_dialog, null);
        if (root == null)
            return null;

        initDialog();
        initViews();
        initContent();
        initClickListeners();
        downloadFile();

        return root;

    }

    private void downloadFile() {
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(false)
                .build();
        PRDownloader.initialize(getActivity(),config);

        final FileAddressGenerator addressGenerator = new FileAddressGenerator();
        addressGenerator.setFileName_Extension(appName);
        addressGenerator.setFolderName(filePath);
        if (TextUtils.isEmpty(addressGenerator.getAddress(filePath, appName)))
            addressGenerator.generateDirectory();

        downloadID = PRDownloader.download(url, addressGenerator.getFile().getPath(), appName + "." + fileExtension)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                        dismiss();
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        try {
                            long total = progress.totalBytes / 1000;
                            long current = progress.currentBytes / 1000;
                            txtTotalSize.setText(NumberSeperator.separate(total) + " kb");
                            txtDownloadedSize.setText(NumberSeperator.separate(current) + " kb ");

                            int per = (int) (progress.currentBytes * 100 / progress.totalBytes);
                            determinateBar.setProgress(per);
                        } catch (Exception ex) {
                            LogWrapper.loge("AboutUsActivity_onProgress_Exception: ", ex);
                        }
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        try {


                            Intent intent_install = new Intent(Intent.ACTION_VIEW);
                            intent_install.setDataAndType(Uri.fromFile(new File(addressGenerator.getFile().getAbsoluteFile() + "/" + filePath + "." + fileExtension)), "application/vnd.android.package-archive");
                            startActivity(intent_install);
                            dismiss();
                            return;

                        } catch (Exception ex) {

                            try {
                                Intent install = new Intent(Intent.ACTION_VIEW);
                                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                File file=new File(addressGenerator.getFile().getAbsoluteFile() + "/" + filePath + "." + fileExtension);
                                Uri apkURI = FileProvider.getUriForFile(
                                        getActivity(),
                                        getActivity()
                                                .getPackageName() + ".provider", file);
                                install.setDataAndType(apkURI,  "application/vnd.android.package-archive");
                                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                getActivity().startActivity(install);
                                dismiss();
                                return;


                            } catch (Exception e) {
e.printStackTrace();
                            }


                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                            dismiss();
                        }

                        dismiss();

                    }

                    @Override
                    public void onError(Error error) {
                        try {
                            new CustomDialogBuilder().showAlert((AppCompatActivity) getActivity(), "خطا در دریافت فایل");
                            dismiss();
                        } catch (Exception ex) {
                            LogWrapper.loge("DownloaderFragment_onError_Exception: ", ex);
                        }


                    }
                });


    }


    private void initClickListeners() {

        if (onCancleClickListener == null)
            onCancleClickListener = new OnCancelClickListener() {
                @Override
                public void onClickCancel(DialogFragment fragment) {
                    if (fragment != null)
                        fragment.dismiss();
                }

                @Override
                public void onClickOutside(DialogFragment fragment) {
                    if (fragment != null)
                        fragment.dismiss();
                }
            };

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickCancel(DownloaderFragment.this);
            }
        });


        lyBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickOutside(DownloaderFragment.this);

            }
        });

        lyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void initContent() {
        btnClose.setText(getString(R.string.cancel));


        if (!TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }

        if (!TextUtils.isEmpty(question)) {
            txtComment.setText(question);
        }

        if (!TextUtils.isEmpty(cancelText)) {
            btnClose.setText(cancelText);
        }

    }

    private void initDialog() {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

    }

    void initViews() {

        lyContent = (RelativeLayout) root.findViewById(R.id.lyContent);
        txtTitle = (TextView) root.findViewById(R.id.txtTitle);
        txtComment = (TextView) root.findViewById(R.id.txtComment);
        determinateBar = root.findViewById(R.id.determinateBar);

        txtDownloadedSize = root.findViewById(R.id.txtDownloadedSize);
        txtTotalSize = root.findViewById(R.id.txtTotalSize);

        lyBackground = (RelativeLayout) root.findViewById(R.id.lyBackground);

        btnClose = (Button) root.findViewById(R.id.btnClose);

        fitLayoutSize(lyContent);
        FontChanger.applyMainFont(lyContent);

//        determinateBar.getProgressDrawable().setColorFilter(Color.parseColor("#ffb4ce"), PorterDuff.Mode.SRC_IN);

    }

    private void fitLayoutSize(ViewGroup v) {
        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = v.getLayoutParams();
        float d = Convertor.toDp(width, getActivity());
        if (d > 350) {
            int p = (int) Convertor.toDp(110, getActivity());
            if (params instanceof RelativeLayout.LayoutParams)
                ((RelativeLayout.LayoutParams) params).setMargins(p, p, p, p);

        }
    }

    public static DownloaderFragment Show(FragmentActivity act, String title, String question, String cancelText, String url, String filePath, String fileName, String fileExtension, boolean installAPK, OnCancelClickListener onCancelClickListener) {
        try {
            DownloaderFragment fragment = new DownloaderFragment();

            fragment.setTitle(title)
                    .setQuestion(question)
                    .setCancelText(cancelText)
                    .setDialog(fragment)
                    .setAppName(fileName)
                    .setUrl(url)
                    .setInstallAPK(installAPK)
                    .setFileExtension(fileExtension)
                    .setFilePath(filePath)
                    .setOnCancleClickListener(onCancelClickListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!act.isDestroyed())
                    fragment.show(act.getFragmentManager(), KEY_ALERT_DIALOG);
            }

            return fragment;
        } catch (Exception ex) {

            LogWrapper.loge("VisitorsAlertDialog_Show_Exception: ", ex);
        }
        return null;

    }

    @Override
    public void onDestroy() {

        try {
            if (isCanceleed) {
                if (onCancleClickListener != null)
                    onCancleClickListener.onClickCancel(DownloaderFragment.this);
            }
        } catch (Exception ex) {
            LogWrapper.loge("VisitorsAlertDialog_onDestroy_Exception: ", ex);
        }

        super.onDestroy();
    }


    public interface OnCancelClickListener {

        void onClickCancel(DialogFragment fragment);

        void onClickOutside(DialogFragment fragment);

    }



}
