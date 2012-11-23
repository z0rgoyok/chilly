package com.zabozhanov.chilly.chilly_player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 23.11.12                               ц
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class ChillyService extends Service {

    final String LOG_TAG = "ChillyService";

    private String stream_url = "http://www.chilloungestation.com:8000/chilloungestation-playlist";
    private MediaPlayer _player;


    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "onCreate");
    }
}
