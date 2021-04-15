package supercoder79.darkforests.world;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import supercoder79.darkforests.Darkforests;

import java.util.Random;

public class DarkOakSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    public static final DarkOakSurfaceBuilder INSTANCE = new DarkOakSurfaceBuilder(TernarySurfaceConfig.CODEC);

    public static void init() {
        Registry.register(Registry.SURFACE_BUILDER, Darkforests.id("dark_oak"), INSTANCE);
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, Darkforests.id("dark_oak"), INSTANCE.withConfig(SurfaceBuilder.GRASS_CONFIG));
    }

    public DarkOakSurfaceBuilder(Codec<TernarySurfaceConfig> codec) {
        super(codec);
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int height, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int i, long l, TernarySurfaceConfig surfaceConfig) {
        double scaledNoise = noise + random.nextDouble() / 1.6;

        if (scaledNoise > 1.6 || scaledNoise < -1.9 && random.nextDouble() > 0.025) {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, i, l, SurfaceBuilder.PODZOL_CONFIG);
        } else {
            SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, height, noise, defaultBlock, defaultFluid, seaLevel, i, l, SurfaceBuilder.GRASS_CONFIG);
        }
    }
}
