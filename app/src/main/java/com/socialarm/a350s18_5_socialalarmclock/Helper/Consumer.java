package com.socialarm.a350s18_5_socialalarmclock.Helper;

/**
 * Created by Alex on 4/17/2018.
 * Based on the Consumer<T> interface in Java8.
 * Java8 is only available in api level 24 and we would like to use an older version
 */

public interface Consumer<T> {
    // If we were following Java8, this would be called accept.
    // To keep compatibility, leaving as callback.
    void callback(T t);
}
