package com.zabozhanov.chilly;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.zabozhanov.chilly.chilly_player.ChillyDelegate;
import com.zabozhanov.chilly.chilly_player.ChillyService;

public class MyActivity extends Activity implements View.OnClickListener, ChillyDelegate {

    private Button _playButton;
    private TextView _statusView;
    private Button _btnStopService;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _playButton = (Button) findViewById(R.id.btnPlay);
        _playButton.setOnClickListener(this);
        _btnStopService = (Button) findViewById(R.id.stop_service);
        _statusView = (TextView) findViewById(R.id.lblStatus);

        if (_BoundService == null) {
            doBindService();
        } else {
            _BoundService.setDelegate(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        if (null != _BoundService) {
            _BoundService.playPause();
        }
    }

    private void setStatus(String status) {
        _statusView.setText("Status: "+ status);
    }

    @Override
    public void playing() {
        setStatus("Playing");
        _playButton.setText("Pause");
        _playButton.setEnabled(true);
    }

    @Override
    public void preparing() {
        setStatus("Preparing");
        _playButton.setEnabled(false);
    }

    @Override
    public void paused() {
        setStatus("Paused");
        _playButton.setText("Play");
        _playButton.setEnabled(true);
    }

    private static ChillyService _BoundService = null;
    private Boolean _IsBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            _BoundService = ((ChillyService.ChillyBinder)service).getService();
            Toast.makeText(MyActivity.this, "Chilly connected", Toast.LENGTH_SHORT).show();
            _BoundService.initPlayback(MyActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
            _BoundService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(MyActivity.this,
                ChillyService.class), mConnection, Context.BIND_AUTO_CREATE);
        _IsBound = true;
    }

    void doUnbindService() {
        if (_IsBound) {
            unbindService(mConnection);
            _IsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

}
