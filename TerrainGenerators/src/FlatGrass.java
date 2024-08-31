
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.generator.LevelGenerator;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.server.MinecraftServer;

public class FlatGrass extends LevelGenerator {

    public FlatGrass(MinecraftServer server) {
        super(server);
        changeSize();
    }

    public FlatGrass(MinecraftServer server, long seed) {
        super(server, seed);
        changeSize();
    }

    private void changeSize()
    {
        setWidth(1024);
        setHeight(64);
        setDepth(1024);
    }

    @Override
    public Level generate(String creator) {

        this.blocks = new byte[width * depth * height];

        // Fill the world with air
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < depth; z++) {
                for (int x = 0; x < width; x++) {
                    setBlock(x, y, z, (byte) 0);
                }
            }
        }

        // Generate bedrock layer at y = 0
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 0, z, (byte) Block.BEDROCK.id);
            }
        }

        // Generate grass block layer at y = 1
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 1, z, (byte) Block.GRASS.id);
            }
        }

        Level level = new Level();
        level.waterLevel = 0; // Set water level to 0 since we don't want any water
        level.setData(width, height, depth, this.blocks);
        level.createTime = System.currentTimeMillis();
        level.creator = creator;
        level.name = "Flat Bedrock and Grass World";

        return level;
    }

    private void setBlock(int x, int y, int z, byte blockId) {
        blocks[(y * depth + z) * width + x] = blockId;
    }
}