package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block {
   public BlockOre() {
      super(Material.rock);
      this.setCreativeTab(CreativeTabs.tabBlock);
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return this == Blocks.coal_ore ? Items.coal : (this == Blocks.diamond_ore ? Items.diamond : (this == Blocks.lapis_ore ? Items.dye : (this == Blocks.emerald_ore ? Items.emerald : (this == Blocks.quartz_ore ? Items.quartz : Item.getItemFromBlock(this)))));
   }

   public int quantityDropped(Random random) {
      return this == Blocks.lapis_ore ? 4 + random.nextInt(5) : 1;
   }

   public int quantityDroppedWithBonus(int fortune, Random random) {
      if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), random, fortune)) {
         int var3 = random.nextInt(fortune + 2) - 1;
         if (var3 < 0) {
            var3 = 0;
         }

         return this.quantityDropped(random) * (var3 + 1);
      } else {
         return this.quantityDropped(random);
      }
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
      if (this.getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
         int var6 = 0;
         if (this == Blocks.coal_ore) {
            var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
         } else if (this == Blocks.diamond_ore) {
            var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
         } else if (this == Blocks.emerald_ore) {
            var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
         } else if (this == Blocks.lapis_ore) {
            var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
         } else if (this == Blocks.quartz_ore) {
            var6 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
         }

         this.dropXpOnBlockBreak(worldIn, pos, var6);
      }

   }

   public int getDamageValue(World worldIn, BlockPos pos) {
      return 0;
   }

   public int damageDropped(IBlockState state) {
      return this == Blocks.lapis_ore ? EnumDyeColor.BLUE.getDyeColorDamage() : 0;
   }
}
