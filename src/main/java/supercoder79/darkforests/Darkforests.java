package supercoder79.darkforests;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import supercoder79.darkforests.world.DarkOakSurfaceBuilder;
import supercoder79.darkforests.world.SmallMushroomFeature;

public class Darkforests implements ModInitializer {
    @Override
    public void onInitialize() {
        DarkOakSurfaceBuilder.init();
        SmallMushroomFeature.init();

        BiomeModifications.create(id("darkforests"))
                .add(ModificationPhase.ADDITIONS, (ctx) -> ctx.getBiomeKey() == BiomeKeys.DARK_FOREST || ctx.getBiomeKey() == BiomeKeys.DARK_FOREST_HILLS, ctx -> {
                    ctx.getGenerationSettings().addFeature(GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, id("small_mushroom")));
                    ctx.getGenerationSettings().setSurfaceBuilder(RegistryKey.of(Registry.CONFIGURED_SURFACE_BUILDER_KEY, id("dark_oak")));
                });
    }

    public static Identifier id(String name) {
        return new Identifier("darkforests", name);
    }
}
