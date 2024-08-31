import com.fukkit.API;
import com.fukkit.JavaPlugin;

public class TerrainGenerators extends JavaPlugin {
    @Override
    public void onEnable() {
        API.getInstance().getWorldManager().registerLevelGenerator("flatgrass", FlatGrass.class);
        API.getInstance().getWorldManager().registerLevelGenerator("backrooms", Backrooms.class);
        
    }

    @Override
    public void onDisable() {

    }

    @Override
    public String getName() {
        return "TerrainGenerators";
    }
}