package tc.oc.iplimiter;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class IPLimiter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void login(final PlayerLoginEvent event) {
        List<String> ips = this.getConfig().getStringList(event.getPlayer().getName());
        if(ips.size() > 0) {
            for(String ip : ips) {
                System.out.println(ip);
                if(event.getAddress().getHostAddress().equals(ip)) {
                    this.getLogger().info("Allowing " + event.getPlayer().getName() + " to login from " + event.getAddress().getHostAddress());
                    return;
                }
            }
            event.disallow(Result.KICK_WHITELIST, ChatColor.RED + " Your IP has not been approved");
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(sender.hasPermission("iplimiter.reload")) {
            this.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Configuration reloaded");
        } else {
            sender.sendMessage(ChatColor.RED + "No permission");
        }
        return true;
    }
}