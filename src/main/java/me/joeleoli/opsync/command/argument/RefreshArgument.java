package me.joeleoli.opsync.command.argument;

import me.joeleoli.opsync.OPSync;
import me.joeleoli.opsync.command.CommandException;
import me.joeleoli.opsync.command.PluginCommandArgument;
import me.joeleoli.opsync.data.GenericCallback;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class RefreshArgument extends PluginCommandArgument {

    @Override
    public void onCommand(CommandSender sender, String[] args) throws CommandException {
        sender.sendMessage(ChatColor.GRAY + "(Resolving Authorizations...)");

        OPSync.getInstance().getDatabaseManager().refreshAuthorizedCache(new GenericCallback() {
            @Override
            public void call(boolean result, boolean error) {
                if (error) {
                    sender.sendMessage(ChatColor.RED + "Failed to resolve authorizations.");
                    return;
                }

                if (result) {
                    sender.sendMessage(ChatColor.GREEN + "Finished refreshing authorized users!");
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