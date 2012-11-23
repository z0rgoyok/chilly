package com.zabozhanov.chilly.chilly_player;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 23.11.12
 * Time: 3:35
 * To change this template use File | Settings | File Templates.
 */
public interface ChillyPlayerDelegate {
    void preparingStart();
    void preparingFinish();
    void paused();
    void error(String msg);
}
