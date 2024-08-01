package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockDeadBush extends BlockBush {
   protected BlockDeadBush() {
      super(Material.vine);
      float var1 = 0.4F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.8F, 0.5F + var1);
   }

   protected boolean canPlaceBlockOn(Block ground) {
      return ground == Blocks.sand || ground == Blocks.hardened_clay || ground == Blocks.stained_hardened_clay || ground == Blocks.dirt;
   }

   public boolean isReplaceable(World worldIn, BlockPos pos) {
      return true;
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return null;
   }

   public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
      if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
         playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
         Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.deadbush, 1, 0));
      } else {
         super.harvestBlock(worldIn, playerIn, pos, state, te);
      }

   }
}
