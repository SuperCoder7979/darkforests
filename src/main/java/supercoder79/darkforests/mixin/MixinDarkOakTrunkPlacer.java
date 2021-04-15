package supercoder79.darkforests.mixin;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.DarkOakTrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

@Mixin(DarkOakTrunkPlacer.class)
public abstract class MixinDarkOakTrunkPlacer extends TrunkPlacer {
    public MixinDarkOakTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    /**
     * @author
     */
    @Overwrite
    public List<FoliagePlacer.TreeNode> generate(TestableWorld testableWorld, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int height, BlockPos blockPos, TreeFeatureConfig treeFeatureConfig) {
        // Add our height
        height += random.nextInt(6) + 2;
        boolean spooky = false;

        if (random.nextDouble() < 0.02) {
            spooky = true;
        }

        List<FoliagePlacer.TreeNode> nodes = Lists.newArrayList();
        BlockPos blockPos2 = blockPos.down();

        // Set blocks to dirt
        setToDirt(testableWorld, biConsumer, random, blockPos2, treeFeatureConfig);
        generateRoot(biConsumer,  random.nextBoolean() ? blockPos.west() : blockPos.north(), random, treeFeatureConfig);

        setToDirt(testableWorld, biConsumer, random, blockPos2.east(), treeFeatureConfig);
        generateRoot(biConsumer,  random.nextBoolean() ? blockPos.east().north() : blockPos.east(2), random, treeFeatureConfig);

        setToDirt(testableWorld, biConsumer, random, blockPos2.south(), treeFeatureConfig);
        generateRoot(biConsumer,  random.nextBoolean() ? blockPos.south().west() : blockPos.south(2), random, treeFeatureConfig);

        setToDirt(testableWorld, biConsumer, random, blockPos2.south().east(), treeFeatureConfig);
        generateRoot(biConsumer,  random.nextBoolean() ? blockPos.south(2).east() : blockPos.south().east(2), random, treeFeatureConfig);

        Direction direction = Direction.Type.HORIZONTAL.random(random);
        int j = height - random.nextInt(4);
        int k = 2 - random.nextInt(3);
        int l = blockPos.getX();
        int m = blockPos.getY();
        int n = blockPos.getZ();
        int o = l;
        int p = n;
        int q = m + height - 1;

        int t;
        int u;
        for(t = 0; t < height; ++t) {
            if (t >= j && k > 0) {
                o += direction.getOffsetX();
                p += direction.getOffsetZ();
                --k;
            }

            u = m + t;
            BlockPos blockPos3 = new BlockPos(o, u, p);
            if (TreeFeature.isAirOrLeaves(testableWorld, blockPos3)) {
                method_35375(testableWorld, biConsumer, random, blockPos3, treeFeatureConfig);
                method_35375(testableWorld, biConsumer, random, blockPos3.east(), treeFeatureConfig);
                method_35375(testableWorld, biConsumer, random, blockPos3.south(), treeFeatureConfig);
                method_35375(testableWorld, biConsumer, random, blockPos3.east().south(), treeFeatureConfig);
            }
        }

        nodes.add(new FoliagePlacer.TreeNode(new BlockPos(o, q, p), random.nextInt(2), true));

        for(t = -1; t <= 2; ++t) {
            for(u = -1; u <= 2; ++u) {
                if ((t < 0 || t > 1 || u < 0 || u > 1) && random.nextInt(3) <= 0) {
                    int v = random.nextInt(3) + 2;

                    // Set branch
                    for(int w = 0; w < v; ++w) {
                        method_35375(testableWorld, biConsumer, random, new BlockPos(l + t, q - w - 1, n + u), treeFeatureConfig);
                    }

                    if (spooky) {
                        biConsumer.accept(new BlockPos(l + t, q - v - 1, n + u), Blocks.COBWEB.getDefaultState());
                    }

                    nodes.add(new FoliagePlacer.TreeNode(new BlockPos(o + t, q, p + u), random.nextInt(2), false));
                }
            }
        }

        return nodes;
    }

    private static void generateRoot(BiConsumer<BlockPos, BlockState> world, BlockPos pos, Random random, TreeFeatureConfig config) {
        //generate and check height
        int height = random.nextInt(4);
        if (height == 0) return;

        //set ground to dirt
        world.accept(pos.down(), Blocks.DIRT.getDefaultState());
        for (int i = 0; i < height; i++) {
            world.accept(pos.up(i), config.trunkProvider.getBlockState(random, pos));
        }
    }
}
