package me.joeleoli.opsync.data;

public interface Callback {

    void call(boolean result, boolean error);

}