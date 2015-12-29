package net.leejjon.blufpoker.interfaces;

/**
 * Created by Leejjon on 29-12-2015.
 */
public interface Lockable {
    void lock();
    void unlock();
    boolean isLocked();
}
