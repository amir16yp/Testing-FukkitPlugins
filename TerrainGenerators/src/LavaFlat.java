import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.generator.LevelGenerator;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.server.MinecraftServer;
import java.util.Random;

public class LavaFlat extends LevelGenerator {
    private Random random;

    public LavaFlat(MinecraftServer server) {
        super(server);
        changeSize();
        this.random = new Random();
    }

    public LavaFlat(MinecraftServer server, long seed) {
        super(server, seed);
        changeSize();
        this.random = new Random(seed);
    }

    private void changeSize() {
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
                    setBlock(x, y, z, 0);
                }
            }
        }

        // Generate bedrock layer at y = 0
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 0, z, Block.BEDROCK.id);
            }
        }

        // Generate lava layer at y = 1
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 1, z, Block.STATIONARY_LAVA.id);
            }
        }

        // Generate grass layer at y = 2
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 2, z, Block.GRASS.id);
            }
        }

        Level level = new Level();
        level.waterLevel = 0;
        level.setData(width, height, depth, this.blocks);
        level.createTime = System.currentTimeMillis();
        level.creator = creator;
        level.name = "Flat Terrain";

        return level;
    }

    private void setBlock(int x, int y, int z, int blockId) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            blocks[(y * depth + z) * width + x] = (byte) blockId;
        }
    }
}