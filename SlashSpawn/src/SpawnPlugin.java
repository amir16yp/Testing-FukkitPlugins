import com.fukkit.*;
import com.fukkit.event.EventSystem;

public class SpawnPlugin extends JavaPlugin {

    private static final String CONFIG_X = "x";
    private static final String CONFIG_Y = "y";
    private static final String CONFIG_Z = "z";
    private static final int DEFAULT_COORD = 64;

    private PluginConfig config;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        config = this.getConfig();
        loadSpawnLocation();
        saveSpawnLocation();

        API api = API.getInstance();
        api.registerCommand("setspawn", this::handleSetSpawn, "spawn.set");
        api.registerCommand("spawn", this::handleSpawnCommand, "*");
    }

    private void loadSpawnLocation() {
        int x = config.getInt(CONFIG_X, DEFAULT_COORD);
        int y = config.getInt(CONFIG_Y, DEFAULT_COORD);
        int z = config.getInt(CONFIG_Z, DEFAULT_COORD);
        spawnLocation = Location.fromTileCoordinates(x, y, z);
    }

    private void saveSpawnLocation() {
        config.setInt(CONFIG_X, spawnLocation.getX());
        config.setInt(CONFIG_Y, spawnLocation.getY());
        config.setInt(CONFIG_Z, spawnLocation.getZ());
        config.save();
    }

    private void handleSpawnCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).teleport(spawnLocation);
        } else {
            sender.sendMessage("Only players can use this command.");
        }
    }

    private void handleSetSpawn( CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            spawnLocation = player.getLocation();
            saveSpawnLocation();
            player.sendMessage("Spawn location has been set to your current position.");
        } else {
            sender.sendMessage("Only players can use this command.");
        }
    }

    @EventSystem.EventHandler
    public void onPlayerJoin(EventSystem.PlayerJoinEvent event) {
        event.getPlayer().teleport(spawnLocation);
    }

    @Override
    public void onDisable() {
        // No specific actions needed on disable
    }

    @Override
    public String getName() {
        return "SlashSpawn";
    }
}