package com.zabozhanov.chilly.chilly_player;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 23.11.12
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public interface ChillyDelegate {
    void playing();
    void preparing();
    void paused();
}
