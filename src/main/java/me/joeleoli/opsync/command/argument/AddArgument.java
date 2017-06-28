package me.joeleoli.opsync.command.argument;

import me.joeleoli.opsync.OPSync;
import me.joeleoli.opsync.command.CommandException;
import me.joeleoli.opsync.command.PluginCommandArgument;
import me.joeleoli.opsync.data.GenericCallback;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AddArgument extends PluginCommandArgument {

    @Override
    public void onCommand(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException(Collections.singletonList("Invalid Usage: /opsync add <player>"));
        }

        OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[1]);

        if (target == null) {
            throw new CommandException(Collections.singletonList("That player has never joined before."));
        }

        sender.sendMessage(ChatColor.GRAY + "(Resolving Player...)");

        OPSync.getInstance().getDatabaseManager().isAuthorized(target.getUniqueId(), new GenericCallback() {
            @Override
            public void call(boolean result, boolean error) {
                if (error) {
                    sender.sendMessage(ChatColor.RED + "The server failed to establish a connection to the database.");
                    return;
                }

                if (!result) {
                    sender.sendMessage(ChatColor.GREEN + "You have authorized the player.");
                    OPSync.getInstance().getDatabaseManager().addAuthorized(target.getUniqueId());
                }
                else {
                    sender.sendMessage(ChatColor.RED + "That player is already authorized.");
                }
            }
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public String getPermission() {
        return "opsync.admin";
    }

}