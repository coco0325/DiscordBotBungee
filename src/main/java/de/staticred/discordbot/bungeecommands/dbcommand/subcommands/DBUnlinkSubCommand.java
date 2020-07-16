package de.staticred.discordbot.bungeecommands.dbcommand.subcommands;

import de.staticred.discordbot.Main;
import de.staticred.discordbot.db.VerifyDAO;
import de.staticred.discordbot.util.SubCommand;
import de.staticred.discordbot.util.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.sql.SQLException;
import java.util.UUID;

public class DBUnlinkSubCommand extends SubCommand {

    public DBUnlinkSubCommand(String name, CommandSender sender, String[] args) {
        super(name, sender, args);
    }

    @Override
    public void execute(String name, CommandSender sender, String[] args) {
        if(!sender.hasPermission("db.cmd.unlink")) {
            sender.sendMessage(new TextComponent(Main.getInstance().getStringFromConfig("NoPermissions",true)));
            return;
        }

        if(args.length != 2) {
            sender.sendMessage(new TextComponent("§cUse: /db unlink <player>"));
            return;
        }

        String inputName = args[1];

        UUID uuid = UUIDFetcher.getUUID(inputName);

        if(uuid == null) {
            sender.sendMessage(new TextComponent(Main.getInstance().getStringFromConfig("UserNotFound",true)));
            return;
        }

        try {
            if(!VerifyDAO.INSTANCE.isPlayerVerified(uuid)) {
                sender.sendMessage(new TextComponent(Main.getInstance().getStringFromConfig("UserNotVerifiedYet",true)));
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            VerifyDAO.INSTANCE.setPlayerAsUnVerified(uuid);
            sender.sendMessage(new TextComponent(Main.getInstance().getStringFromConfig("UnlinkedPlayer",true)));
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }
}