package pl.krisjackowski.android.backgroundservice;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

public class BackgroundService extends Service {

    public static void start(Context context) {
        Intent starter = new Intent(context, BackgroundService.class);
        context.startService(starter);
    }

    public BackgroundService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Thread backgroundThread = new Thread(BackgroundService.class.getSimpleName()) {
            @Override
            public void run() {
                runMigration();
            }
        };
        backgroundThread.start();
    }

    private void runMigration() {
        for (int i = 0; i < 20000; i++) {
            EventBus.getDefault().post(new MigrationUpdateEvent(i));
        }
        EventBus.getDefault().post(new MigrationFinishedEvent(true));
        stopSelf();
    }

    static class MigrationFinishedEvent {
        boolean migrationStatus;

        MigrationFinishedEvent(boolean migrationStatus) {
            this.migrationStatus = migrationStatus;
        }
    }

    static class MigrationUpdateEvent {
        int migrationIterationStatus;

        MigrationUpdateEvent(int migrationIterationStatus) {
            this.migrationIterationStatus = migrationIterationStatus;
        }
    }
}
