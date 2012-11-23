package com.zabozhanov.chilly.chilly_player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.zabozhanov.chilly.MyActivity;
import com.zabozhanov.chilly.R;

import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 23.11.12                               ц
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */

public class ChillyService extends Service implements MediaPlayer.OnPreparedListener {

    private String _stream_url = "http://www.chilloungestation.com:8000/chilloungestation-playlist";
    private MediaPlayer _player = null;
    private int NOTIFICATION = 1234;
    private NotificationManager _nm;

    private Boolean _paused = false;

    private Boolean _isPreparing = true;

    private ChillyDelegate _delegate = null;
    public ChillyDelegate get_delegate() {
        return _delegate;
    }

    public void playPause() {
        if (_paused) {
            play();
        } else {
            pause();
        }
    }

    public void setDelegate(ChillyDelegate delegate) {
        _delegate = delegate;

        if (_isPreparing) {
            _delegate.preparing();
            return;
        }

        if (_player.isPlaying()) {
            _delegate.playing();
        } else {
            _delegate.paused();
        }
    }

    private void play() {
        _player.start();
        _delegate.playing();
        _paused = false;
    }
    protected void pause() {
        _player.pause();
        _delegate.paused();
        _paused = true;
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        _isPreparing = true;
        if (!_player.isPlaying()) {
            play();
        }
    }



    public void initPlayback(ChillyDelegate delegate) {

        this._delegate = delegate;

        if (_player == null) {

            _player = new MediaPlayer();
            try {
                _player.setDataSource(_stream_url);
            } catch (IOException e) {
            }

            _delegate.preparing();
            _player.setOnPreparedListener(this);
            _player.prepareAsync();
        } else {

            _delegate.preparing();
        }

    }




    public class ChillyBinder extends Binder {
        public ChillyService getService() {
            return ChillyService.this;
        }
    }

    @Override
    public void onCreate() {
        _nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        Toast.makeText(this, String.valueOf(new Random().nextInt()), Toast.LENGTH_SHORT).show();
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "ChillyService started";

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_menu_search, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MyActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "ChillyService label",
                text, contentIntent);

        // Send the notification.
        _nm.notify(NOTIFICATION, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        _nm.cancel(NOTIFICATION);

        Toast.makeText(this, "ChillyService stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _Binder;
    }

    private final IBinder _Binder = new ChillyBinder();
}
