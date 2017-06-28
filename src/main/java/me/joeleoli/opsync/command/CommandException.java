package me.joeleoli.opsync.command;

import java.util.List;

public class CommandException extends Exception {

	private List<String> messages;
    
    public CommandException(List<String> messages) {
        this.messages = messages;
    }
    
    public List<String> getMessages() {
        return this.messages;
    }

}