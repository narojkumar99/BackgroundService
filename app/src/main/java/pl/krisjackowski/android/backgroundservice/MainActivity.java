package pl.krisjackowski.android.backgroundservice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    Unbinder unbinder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.button)
    public void onButtonClicked() {
        BackgroundService.start(this);
        showProgressDialog();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMigrationFinished(BackgroundService.MigrationFinishedEvent migrationFinishedEvent) {
        hideProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMigrationUpdated(BackgroundService.MigrationUpdateEvent migrationUpdateEvent) {
        progressDialog.setMessage("Background process ...: "+migrationUpdateEvent.migrationIterationStatus);
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(
                MainActivity.this, "Please wait ...",	"Background process ...", true);
    }
}
