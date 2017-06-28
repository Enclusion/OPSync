package me.joeleoli.opsync.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class PluginCommandArgument {

    public abstract void onCommand(CommandSender sender, String[] args) throws CommandException;

    public abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public abstract List<String> getAliases();

    public abstract boolean requiresPlayer();

    public abstract boolean requiresPermission();

    public abstract String getPermission();

}