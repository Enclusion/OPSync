package me.joeleoli.opsync.command;

import me.joeleoli.opsync.command.argument.AddArgument;
import me.joeleoli.opsync.command.argument.RefreshArgument;
import me.joeleoli.opsync.command.argument.RemoveArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class OPSyncCommand extends PluginCommand {

    private Map<String, PluginCommandArgument> commandArguments;

    public OPSyncCommand(Plugin plugin) {
        super(plugin);

        this.commandArguments = new HashMap<>();
        this.commandArguments.put("refresh", new RefreshArgument());
        this.commandArguments.put("add", new AddArgument());
        this.commandArguments.put("remove", new RemoveArgument());
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public String getPermission() {
        return "opsync.admin";
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) throws CommandException {
        String solve = args.length == 0 ? "help" : args[0].toLowerCase();
        PluginCommandArgument argument = null;

        if (this.commandArguments.containsKey(solve)) {
            argument = this.commandArguments.get(solve);
        }
        else {
            for (Map.Entry<String, PluginCommandArgument> commandArgument : this.commandArguments.entrySet()) {
                if (commandArgument.getValue().getAliases().contains(args[0].toLowerCase())) {
                    argument = commandArgument.getValue();
                }
            }
        }

        if (argument == null) {
            argument = this.commandArguments.get("help");
        }
        else {
            if (argument.requiresPlayer() && !(sender instanceof Player)) {
                throw new CommandException(Collections.singletonList("You must be a player to execute that command."));
            }

            if (argument.requiresPermission() && !sender.hasPermission(argument.getPermission())) {
                throw new CommandException(Collections.singletonList("You don't have permission to execute that command."));
            }
        }

        try {
            argument.onCommand(sender, args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

}