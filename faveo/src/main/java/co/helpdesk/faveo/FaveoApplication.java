package co.helpdesk.faveo;

/**
 * Created by sumit on 3/13/2016.
 */
import java.io.File;

import android.app.Application;

public class FaveoApplication extends Application {
    private static FaveoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static FaveoApplication getInstance() {
        return instance;
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
        return dir.delete();
    }
}