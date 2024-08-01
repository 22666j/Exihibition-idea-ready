package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCompressed extends Block {
   private final MapColor mapColor;

   public BlockCompressed(MapColor p_i45414_1_) {
      super(Material.iron);
      this.mapColor = p_i45414_1_;
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public MapColor getMapColor(IBlockState state) {
      return this.mapColor;
   }
}
