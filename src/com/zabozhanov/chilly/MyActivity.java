package com.zabozhanov.chilly;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.zabozhanov.chilly.chilly_player.ChillyPlayer;
import com.zabozhanov.chilly.chilly_player.ChillyPlayerDelegate;

public class MyActivity extends Activity implements View.OnClickListener, ChillyPlayerDelegate {

    protected Button _playButton;
    protected TextView _statusView;
    protected boolean  _playing = true;
    protected ChillyPlayer _player = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        _playButton = (Button) findViewById(R.id.btnPlay);
        _playButton.setOnClickListener(this);

        _statusView = (TextView) findViewById(R.id.lblStatus);
        try {
            if (_player == null) {
                _player = new ChillyPlayer("http://www.chilloungestation.com:8000/chilloungestation-playlist", this, this);
                _player.play();
            }
        } catch (Exception e) {
            _statusView.setText("creating err: " + e.getMessage() + e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        _playing = !_playing;
        if (_playing) {
            _playButton.setText("Pause");
            _player.play();
        } else {
            _playButton.setText("Play");
            _player.pause();
        }
    }

    @Override
    public void preparingStart() {
        _statusView.setText("Status: подготовка");
        _playButton.setEnabled(false);
    }

    @Override
    public void preparingFinish() {
        _statusView.setText("Status: проигрывание");
        _playButton.setEnabled(true);
    }

    @Override
    public void paused() {
        _statusView.setText("Status: пауза");
    }

    @Override
    public void error(String msg) {
        _statusView.setText("error from player:" + msg);
    }
}
