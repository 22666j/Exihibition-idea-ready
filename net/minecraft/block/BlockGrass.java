package net.minecraft.block;

import exhibition.Client;
import exhibition.module.impl.render.Xray;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockGrass extends Block implements IGrowable {
   public static final PropertyBool SNOWY = PropertyBool.create("snowy");

   protected BlockGrass() {
      super(Material.grass);
      this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, false));
      this.setTickRandomly(true);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      Block var4 = worldIn.getBlockState(pos.offsetUp()).getBlock();
      return state.withProperty(SNOWY, var4 == Blocks.snow || var4 == Blocks.snow_layer);
   }

   public int getBlockColor() {
      return ColorizerGrass.getGrassColor(0.5D, 1.0D);
   }

   public int getRenderColor(IBlockState state) {
      return this.getBlockColor();
   }

   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
      return BiomeColorHelper.func_180286_a(worldIn, pos);
   }

   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
      if (!worldIn.isRemote) {
         if (worldIn.getLightFromNeighbors(pos.offsetUp()) < 4 && worldIn.getBlockState(pos.offsetUp()).getBlock().getLightOpacity() > 2) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
         } else if (worldIn.getLightFromNeighbors(pos.offsetUp()) >= 9) {
            for(int var5 = 0; var5 < 4; ++var5) {
               BlockPos var6 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
               Block var7 = worldIn.getBlockState(var6.offsetUp()).getBlock();
               IBlockState var8 = worldIn.getBlockState(var6);
               if (var8.getBlock() == Blocks.dirt && var8.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(var6.offsetUp()) >= 4 && var7.getLightOpacity() <= 2) {
                  worldIn.setBlockState(var6, Blocks.grass.getDefaultState());
               }
            }
         }
      }

   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
   }

   public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
      return true;
   }

   public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
      return true;
   }

   public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
      BlockPos var5 = p_176474_3_.offsetUp();

      label37:
      for(int var6 = 0; var6 < 128; ++var6) {
         BlockPos var7 = var5;

         for(int var8 = 0; var8 < var6 / 16; ++var8) {
            var7 = var7.add(p_176474_2_.nextInt(3) - 1, (p_176474_2_.nextInt(3) - 1) * p_176474_2_.nextInt(3) / 2, p_176474_2_.nextInt(3) - 1);
            if (worldIn.getBlockState(var7.offsetDown()).getBlock() != Blocks.grass || worldIn.getBlockState(var7).getBlock().isNormalCube()) {
               continue label37;
            }
         }

         if (worldIn.getBlockState(var7).getBlock().blockMaterial == Material.air) {
            if (p_176474_2_.nextInt(8) == 0) {
               BlockFlower.EnumFlowerType var11 = worldIn.getBiomeGenForCoords(var7).pickRandomFlower(p_176474_2_, var7);
               BlockFlower var9 = var11.func_176964_a().func_180346_a();
               IBlockState var10 = var9.getDefaultState().withProperty(var9.func_176494_l(), var11);
               if (var9.canBlockStay(worldIn, var7, var10)) {
                  worldIn.setBlockState(var7, var10, 3);
               }
            } else {
               IBlockState var12 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.GRASS);
               if (Blocks.tallgrass.canBlockStay(worldIn, var7, var12)) {
                  worldIn.setBlockState(var7, var12, 3);
               }
            }
         }
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return Client.getModuleManager().isEnabled(Xray.class) ? super.getBlockLayer() : EnumWorldBlockLayer.CUTOUT_MIPPED;
   }

   public int getMetaFromState(IBlockState state) {
      return 0;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{SNOWY});
   }
}
