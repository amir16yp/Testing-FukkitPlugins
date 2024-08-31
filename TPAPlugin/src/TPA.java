import com.fukkit.API;
import com.fukkit.CommandSender;
import com.fukkit.JavaPlugin;
import com.fukkit.Player;;
import com.fukkit.Scheduler;

import java.util.HashMap;
import java.util.Map;

public class TPA extends JavaPlugin {

    private API api;
    private Map<String, String> tpaRequests = new HashMap<>(); // Maps sender to receiver

    @Override
    public void onEnable() {
        api = API.getInstance();
        api.registerCommand("tpa", this::tpaHandler, "*");
        api.registerCommand("tpaccept", this::tpAcceptHandler, "*");
        api.registerCommand("tpdeny", this::tpDenyHandler, "*");
    }

    private void tpaHandler(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return;
        }

        Player sender = (Player) commandSender;
        if (args.length != 1) {
            sender.sendMessage("Usage: /tpa <player>");
            return;
        }

        String targetName = args[0];
        Player target = api.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage("Player not found.");
            return;
        }

        if (target.equals(sender)) {
            sender.sendMessage("You cannot teleport to yourself.");
            return;
        }

        if (tpaRequests.containsKey(sender.getName())) {
            sender.sendMessage("You have already sent a teleport request.");
            return;
        }

        // Send teleport request to the target player
        tpaRequests.put(sender.getName(), target.getName());
        target.sendMessage(sender.getName() + " wants to teleport to you. Type /tpaccept or /tpdeny.");
        sender.sendMessage("Teleport request sent to " + target.getName() + ".");

        // Remove the request after 60 seconds
        this.getScheduler().scheduleSyncDelayedTask(this, () -> {
            if (tpaRequests.containsKey(sender.getName())) {
                tpaRequests.remove(sender.getName());
                target.sendMessage("Teleport request from " + sender.getName() + " has expired.");
            }
        }, 60 * 20L);
    }

    private void tpAcceptHandler(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return;
        }

        Player target = (Player) commandSender;
        String senderName = tpaRequests.entrySet().stream()
                .filter(entry -> entry.getValue().equals(target.getName()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (senderName == null) {
            target.sendMessage( "You have no teleport requests.");
            return;
        }

        Player sender = api.getPlayer(senderName);
        if (sender == null) {
            target.sendMessage("The player who sent the request is no longer online.");
            return;
        }

        // Teleport sender to the target player
        sender.teleport(target.getLocation());
        target.sendMessage("You have accepted the teleport request from " + senderName + ".");
        sender.sendMessage("You have been teleported to " + target.getName() + ".");

        // Remove the request
        tpaRequests.remove(senderName);
    }

    private void tpDenyHandler(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return;
        }

        Player target = (Player) commandSender;
        String senderName = tpaRequests.entrySet().stream()
                .filter(entry -> entry.getValue().equals(target.getName()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (senderName == null) {
            target.sendMessage("You have no teleport requests.");
            return;
        }

        Player sender = api.getPlayer(senderName);
        if (sender == null) {
            target.sendMessage("The player who sent the request is no longer online.");
            return;
        } else {
            if (!sender.isOnline())
            {
                target.sendMessage("The player who sent the request is no longer online.");
                return;
            }
        }

        // Notify sender that the request was denied
        sender.sendMessage(target.getName() + " has denied your teleport request.");
        target.sendMessage("You have denied the teleport request from " + senderName + ".");

        // Remove the request
        tpaRequests.remove(senderName);
    }

    @Override
    public void onDisable() {
        tpaRequests.clear();
    }

    @Override
    public String getName() {
        return "TPA";
    }
}
