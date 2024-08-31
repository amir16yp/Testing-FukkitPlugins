import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.generator.LevelGenerator;
import com.mojang.minecraft.level.tile.Block;
import com.mojang.minecraft.server.MinecraftServer;
import java.util.Random;

public class Backrooms extends LevelGenerator {
    private Random random;

    public Backrooms(MinecraftServer server) {
        super(server);
        changeSize();
        this.random = new Random();
    }

    public Backrooms(MinecraftServer server, long seed) {
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

        // Generate floor (yellow wool to represent carpet)
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 0, z, Block.YELLOW_WOOL.id);
            }
        }

        // Generate ceiling (white wool to represent panels)
        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                setBlock(x, 4, z, Block.WHITE_WOOL.id);
            }
        }

        // Generate maze-like corridors
        for (int z = 0; z < depth; z += 4) {
            for (int x = 0; x < width; x += 4) {
                generateRoom(x, z);
            }
        }

        // Add random unsettling elements
        addUnsettlingElements();

        Level level = new Level();
        level.waterLevel = 0;
        level.setData(width, height, depth, this.blocks);
        level.createTime = System.currentTimeMillis();
        level.creator = creator;
        level.name = "Backrooms";

        return level;
    }

    private void generateRoom(int x, int z) {
        // Generate walls (stone to represent wallpaper)
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx < 4; dx++) {
                for (int dz = 0; dz < 4; dz++) {
                    if (dx == 0 || dx == 3 || dz == 0 || dz == 3) {
                        setBlock(x + dx, dy, z + dz, Block.STONE.id);
                    }
                }
            }
        }

        // Create doorways
        int doorX = random.nextBoolean() ? 0 : 3;
        int doorZ = random.nextBoolean() ? 0 : 3;
        for (int dy = 1; dy <= 2; dy++) {
            setBlock(x + doorX, dy, z + 1, 0);
            setBlock(x + doorX, dy, z + 2, 0);
            setBlock(x + 1, dy, z + doorZ, 0);
            setBlock(x + 2, dy, z + doorZ, 0);
        }
    }

    private void addUnsettlingElements() {
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int z = random.nextInt(depth);
            int y = random.nextInt(3) + 1;

            switch (random.nextInt(5)) {
                case 0: // Cobweb-like effect using gray wool
                    setBlock(x, y, z, Block.GRAY_WOOL.id);
                    break;
                case 1: // Water puddles
                    setBlock(x, 1, z, Block.STATIONARY_WATER.id);
                    break;
                case 2: // Mysterious glowing blocks (gold blocks)
                    setBlock(x, y, z, Block.GOLD_BLOCK.id);
                    break;
                case 3: // Unexpected holes in the floor
                    setBlock(x, 0, z, 0);
                    break;
                case 4: // Strange growths (brown mushrooms)
                    setBlock(x, 1, z, Block.BROWN_MUSHROOM.id);
                    break;
            }
        }
    }

    private void setBlock(int x, int y, int z, int blockId) {
        if (x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth) {
            blocks[(y * depth + z) * width + x] = (byte) blockId;
        }
    }
}