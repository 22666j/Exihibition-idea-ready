package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSign extends BlockContainer {
   protected BlockSign() {
      super(Material.wood);
      float var1 = 0.25F;
      float var2 = 1.0F;
      this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      return super.getSelectedBoundingBox(worldIn, pos);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
      return true;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public TileEntity createNewTileEntity(World worldIn, int meta) {
      return new TileEntitySign();
   }

   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
      return Items.sign;
   }

   public Item getItem(World worldIn, BlockPos pos) {
      return Items.sign;
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote) {
         return true;
      } else {
         TileEntity var9 = worldIn.getTileEntity(pos);
         return var9 instanceof TileEntitySign ? ((TileEntitySign)var9).func_174882_b(playerIn) : false;
      }
   }
}
