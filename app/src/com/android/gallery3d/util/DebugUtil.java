package com.android.gallery3d.util;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.android.gallery3d.BuildConfig;
import com.squareup.leakcanary.LeakCanary;


/* Need LeakCanary please add below in build.gradle,
 * the latest version please visit https://github.com/square/leakcanary

debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.6.3'
releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.6.3'
// Optional, if you use support library fragments:
// debugImplementation 'com.squareup.leakcanary:leakcanary-support-fragment:1.6.3'

*/
public class DebugUtil {

    public static void startLeakDetection(Context context) {
        if (context instanceof Application) {
            if (LeakCanary.isInAnalyzerProcess(context.getApplicationContext())) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install((Application) context);
        }

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }
}
