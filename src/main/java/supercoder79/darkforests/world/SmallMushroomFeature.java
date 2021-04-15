package supercoder79.darkforests.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.HeightmapDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import supercoder79.darkforests.Darkforests;

import java.util.Random;

public class SmallMushroomFeature extends Feature<DefaultFeatureConfig> {
    public static final SmallMushroomFeature INSTANCE = new SmallMushroomFeature(DefaultFeatureConfig.CODEC);

    public static void init() {
        Registry.register(Registry.FEATURE, Darkforests.id("small_mushroom"), INSTANCE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Darkforests.id("small_mushroom"),
                INSTANCE.configure(FeatureConfig.DEFAULT)
                        .decorate(Decorator.HEIGHTMAP.configure(new HeightmapDecoratorConfig(Heightmap.Type.OCEAN_FLOOR_WG)))
                        .spreadHorizontally());
    }

    public SmallMushroomFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        BlockState downState = world.getBlockState(pos.down());
        if (!downState.isOf(Blocks.GRASS_BLOCK) && !downState.isOf(Blocks.PODZOL)) {
            return false;
        }

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = 0; y < 3; y++) {

                    if (!world.getBlockState(pos.add(x, y, z)).isAir()) {
                        return false;
                    }
                }
            }
        }

        if (random.nextBoolean()) {
            // Brown mushroom
            world.setBlockState(pos, Blocks.MUSHROOM_STEM.getDefaultState(), 3);
            world.setBlockState(pos.up(), Blocks.MUSHROOM_STEM.getDefaultState(), 3);

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    world.setBlockState(pos.add(x, 2, z), Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), 3);
                }
            }
        } else {
            // Red mushroom
            world.setBlockState(pos, Blocks.MUSHROOM_STEM.getDefaultState(), 3);
            world.setBlockState(pos.up(), Blocks.MUSHROOM_STEM.getDefaultState(), 3);

            for (Direction direction : Direction.values()) {
                if (direction != Direction.DOWN) {
                    world.setBlockState(pos.up(2).offset(direction), Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), 3);
                }
            }
        }

        return true;
    }
}
