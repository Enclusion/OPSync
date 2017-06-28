package me.joeleoli.opsync.data;

public abstract class GenericCallback implements Callback {

    private boolean called = false;

    public boolean isCalled() {
        return called;
    }

    public void setCalled(boolean called) {
        this.called = called;
    }

    public abstract void call(boolean result, boolean error);

}