package com.zabozhanov.chilly.chilly_player;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 23.11.12
 * Time: 2:17
 * To change this template use File | Settings | File Templates.
 */
public class ChillyPlayer implements MediaPlayer.OnPreparedListener{

    protected String _url;
    protected MediaPlayer _player;
    protected Context _context;

    protected ChillyPlayerDelegate _delegate;

    protected Boolean _isPaused = false;

    public ChillyPlayer(String url, Context context, ChillyPlayerDelegate delegate)
    {
        this._url = url;
        _context = context;
        _delegate = delegate;
        _player = new MediaPlayer();
        try {
            _player.setDataSource(_url);
        } catch (IOException e) {
            _delegate.error(e.getMessage());
        }
        _player.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (!_player.isPlaying()) {
            _delegate.preparingFinish();
            _player.start();
            _isPaused = false;
        }
    }

    public void play() {
        if (!_player.isPlaying()) {

            if (!_isPaused) {
                _player.prepareAsync();
                _delegate.preparingStart();
            }
            else
            {
                _player.start();
                _delegate.preparingFinish();
                _isPaused = false;
            }
        }
    }

    public void pause() {
        _player.pause();
        _delegate.paused();
        _isPaused = true;
    }
}
