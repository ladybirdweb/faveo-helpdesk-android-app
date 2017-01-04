package co.helpdesk.faveo;

/**
 * Created by sumit on 3/13/2016.
 */


import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import java.io.File;

import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import io.fabric.sdk.android.Fabric;

public class FaveoApplication extends MultiDexApplication {
    private static FaveoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //AndroidNetworking.initialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        instance = this;
    }

    public static synchronized FaveoApplication getInstance() {
        return instance;
    }

    public void setInternetListener(InternetReceiver.InternetReceiverListener listener) {
        InternetReceiver.internetReceiverListener = listener;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib"))
                    deleteDir(new File(appDir, s));
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children)
                return deleteDir(new File(dir, aChildren));
        }
        return dir != null && dir.delete();
    }
}