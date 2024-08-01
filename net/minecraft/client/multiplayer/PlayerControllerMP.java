package net.minecraft.client.multiplayer;

import exhibition.Client;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventAttack;
import exhibition.event.impl.EventDamageBlock;
import exhibition.module.Module;
import exhibition.module.impl.combat.Killaura;
import exhibition.module.impl.other.AntiDesync;
import exhibition.module.impl.render.Freecam;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerControllerMP {
   private final Minecraft mc;
   private final NetHandlerPlayClient netClientHandler;
   private BlockPos field_178895_c = new BlockPos(-1, -1, -1);
   private ItemStack currentItemHittingBlock;
   public float curBlockDamageMP;
   private float stepSoundTickCounter;
   public int blockHitDelay;
   private boolean isHittingBlock;
   private WorldSettings.GameType currentGameType;
   private int currentPlayerItem;

   public PlayerControllerMP(Minecraft mcIn, NetHandlerPlayClient p_i45062_2_) {
      this.currentGameType = WorldSettings.GameType.SURVIVAL;
      this.mc = mcIn;
      this.netClientHandler = p_i45062_2_;
   }

   public WorldSettings.GameType getCurrentGameType() {
      return this.currentGameType;
   }

   public static void func_178891_a(Minecraft mcIn, PlayerControllerMP p_178891_1_, BlockPos p_178891_2_, EnumFacing p_178891_3_) {
      if (!mcIn.theWorld.func_175719_a(mcIn.thePlayer, p_178891_2_, p_178891_3_)) {
         p_178891_1_.func_178888_a(p_178891_2_, p_178891_3_);
      }

   }

   public void setPlayerCapabilities(EntityPlayer p_78748_1_) {
      this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
   }

   public boolean enableEverythingIsScrewedUpMode() {
      return this.currentGameType == WorldSettings.GameType.SPECTATOR;
   }

   public void setGameType(WorldSettings.GameType p_78746_1_) {
      this.currentGameType = p_78746_1_;
      this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
   }

   public void flipPlayer(EntityPlayer playerIn) {
      playerIn.rotationYaw = -180.0F;
   }

   public boolean shouldDrawHUD() {
      return this.currentGameType.isSurvivalOrAdventure();
   }

   public boolean func_178888_a(BlockPos p_178888_1_, EnumFacing p_178888_2_) {
      if (this.currentGameType.isAdventure()) {
         if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
         }

         if (!this.mc.thePlayer.func_175142_cm()) {
            Block var3 = this.mc.theWorld.getBlockState(p_178888_1_).getBlock();
            ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();
            if (var4 == null) {
               return false;
            }

            if (!var4.canDestroy(var3)) {
               return false;
            }
         }
      }

      if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
         return false;
      } else {
         WorldClient var8 = this.mc.theWorld;
         IBlockState var9 = var8.getBlockState(p_178888_1_);
         Block var5 = var9.getBlock();
         if (var5.getMaterial() == Material.air) {
            return false;
         } else {
            var8.playAuxSFX(2001, p_178888_1_, Block.getStateId(var9));
            boolean var6 = var8.setBlockToAir(p_178888_1_);
            if (var6) {
               var5.onBlockDestroyedByPlayer(var8, p_178888_1_, var9);
            }

            this.field_178895_c = new BlockPos(this.field_178895_c.getX(), -1, this.field_178895_c.getZ());
            if (!this.currentGameType.isCreative()) {
               ItemStack var7 = this.mc.thePlayer.getCurrentEquippedItem();
               if (var7 != null) {
                  var7.onBlockDestroyed(var8, var5, p_178888_1_, this.mc.thePlayer);
                  if (var7.stackSize == 0) {
                     this.mc.thePlayer.destroyCurrentEquippedItem();
                  }
               }
            }

            return var6;
         }
      }
   }

   public boolean func_180511_b(BlockPos p_180511_1_, EnumFacing p_180511_2_) {
      Block var3;
      if (this.currentGameType.isAdventure()) {
         if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
            return false;
         }

         if (!this.mc.thePlayer.func_175142_cm()) {
            var3 = this.mc.theWorld.getBlockState(p_180511_1_).getBlock();
            ItemStack var4 = this.mc.thePlayer.getCurrentEquippedItem();
            if (var4 == null) {
               return false;
            }

            if (!var4.canDestroy(var3)) {
               return false;
            }
         }
      }

      if (!this.mc.theWorld.getWorldBorder().contains(p_180511_1_)) {
         return false;
      } else {
         if (this.currentGameType.isCreative()) {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
            func_178891_a(this.mc, this, p_180511_1_, p_180511_2_);
            this.blockHitDelay = 5;
         } else if (!this.isHittingBlock || !this.func_178893_a(p_180511_1_)) {
            if (this.isHittingBlock) {
               this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, p_180511_2_));
            }

            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180511_1_, p_180511_2_));
            var3 = this.mc.theWorld.getBlockState(p_180511_1_).getBlock();
            boolean var5 = var3.getMaterial() != Material.air;
            if (var5 && this.curBlockDamageMP == 0.0F) {
               var3.onBlockClicked(this.mc.theWorld, p_180511_1_, this.mc.thePlayer);
            }

            if (var5 && var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180511_1_) >= 1.0F) {
               if (!Client.getModuleManager().isEnabled(AntiDesync.class)) {
                  this.func_178888_a(p_180511_1_, p_180511_2_);
               }
            } else {
               this.isHittingBlock = true;
               this.field_178895_c = p_180511_1_;
               this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
               this.curBlockDamageMP = 0.0F;
               this.stepSoundTickCounter = 0.0F;
               this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int)(this.curBlockDamageMP * 10.0F) - 1);
            }
         }

         return true;
      }
   }

   public void resetBlockRemoving() {
      if (this.isHittingBlock) {
         this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, this.field_178895_c, EnumFacing.DOWN));
         this.isHittingBlock = false;
         this.curBlockDamageMP = 0.0F;
         this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, -1);
      }

   }

   public boolean breakBlock(BlockPos p_180512_1_, EnumFacing p_180512_2_) {
      EventDamageBlock event = (EventDamageBlock)EventSystem.getInstance(EventDamageBlock.class);
      event.fire(p_180512_1_);
      if (event.isCancelled()) {
         return false;
      } else {
         this.syncCurrentPlayItem();
         if (this.blockHitDelay > 0) {
            --this.blockHitDelay;
            return true;
         } else if ((this.currentGameType.isCreative() || ((Module)Client.getModuleManager().get(Freecam.class)).isEnabled()) && this.mc.theWorld.getWorldBorder().contains(p_180512_1_)) {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
            func_178891_a(this.mc, this, p_180512_1_, p_180512_2_);
            return true;
         } else if (this.func_178893_a(p_180512_1_)) {
            Block var3 = this.mc.theWorld.getBlockState(p_180512_1_).getBlock();
            if (var3.getMaterial() == Material.air) {
               this.isHittingBlock = false;
               return false;
            } else {
               this.curBlockDamageMP += var3.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, p_180512_1_);
               if (this.stepSoundTickCounter % 4.0F == 0.0F) {
                  this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var3.stepSound.getStepSound()), (var3.stepSound.getVolume() + 1.0F) / 8.0F, var3.stepSound.getFrequency() * 0.5F, (float)p_180512_1_.getX() + 0.5F, (float)p_180512_1_.getY() + 0.5F, (float)p_180512_1_.getZ() + 0.5F));
               }

               ++this.stepSoundTickCounter;
               if (this.curBlockDamageMP >= 1.0F) {
                  this.isHittingBlock = false;
                  this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p_180512_1_, p_180512_2_));
                  if (!Client.getModuleManager().isEnabled(AntiDesync.class)) {
                     this.func_178888_a(p_180512_1_, p_180512_2_);
                  }

                  this.curBlockDamageMP = 0.0F;
                  this.stepSoundTickCounter = 0.0F;
                  this.blockHitDelay = 5;
               }

               this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), this.field_178895_c, (int)(this.curBlockDamageMP * 10.0F) - 1);
               return true;
            }
         } else {
            return this.func_180511_b(p_180512_1_, p_180512_2_);
         }
      }
   }

   public float getBlockReachDistance() {
      return this.currentGameType.isCreative() ? 5.0F : 4.5F;
   }

   public void updateController() {
      this.syncCurrentPlayItem();
      if (this.netClientHandler.getNetworkManager().isChannelOpen()) {
         this.netClientHandler.getNetworkManager().processReceivedPackets();
      } else {
         this.netClientHandler.getNetworkManager().checkDisconnected();
      }

   }

   private boolean func_178893_a(BlockPos p_178893_1_) {
      ItemStack var2 = this.mc.thePlayer.getHeldItem();
      boolean var3 = this.currentItemHittingBlock == null && var2 == null;
      if (this.currentItemHittingBlock != null && var2 != null) {
         var3 = var2.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(var2, this.currentItemHittingBlock) && (var2.isItemStackDamageable() || var2.getMetadata() == this.currentItemHittingBlock.getMetadata());
      }

      return p_178893_1_.equals(this.field_178895_c) && var3;
   }

   private void syncCurrentPlayItem() {
      int var1 = this.mc.thePlayer.inventory.currentItem;
      if (var1 != this.currentPlayerItem) {
         this.currentPlayerItem = var1;
         this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
      }

   }

   public boolean onPlayerRightClick(EntityPlayerSP player, WorldClient world, ItemStack stack, BlockPos pos, EnumFacing facing, Vec3 vec) {
      this.syncCurrentPlayItem();
      float var7 = (float)(vec.xCoord - (double)pos.getX());
      float var8 = (float)(vec.yCoord - (double)pos.getY());
      float var9 = (float)(vec.zCoord - (double)pos.getZ());
      boolean var10 = false;
      if (!this.mc.theWorld.getWorldBorder().contains(pos)) {
         return false;
      } else {
         if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            IBlockState var11 = world.getBlockState(pos);
            if ((!player.isSneaking() || player.getHeldItem() == null) && var11.getBlock().onBlockActivated(world, pos, var11, player, facing, var7, var8, var9)) {
               var10 = true;
            }

            if (!var10 && stack != null && stack.getItem() instanceof ItemBlock) {
               ItemBlock var12 = (ItemBlock)stack.getItem();
               if (!var12.canPlaceBlockOnSide(world, pos, facing, player, stack)) {
                  return false;
               }
            }
         }

         this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(pos, facing.getIndex(), player.inventory.getCurrentItem(), var7, var8, var9));
         if (!var10 && this.currentGameType != WorldSettings.GameType.SPECTATOR) {
            if (stack == null) {
               return false;
            } else if (this.currentGameType.isCreative()) {
               int meta = stack.getMetadata();
               int stackSize = stack.stackSize;
               boolean itemUsed = stack.onItemUse(player, world, pos, facing, var7, var8, var9);
               stack.setItemDamage(meta);
               stack.stackSize = stackSize;
               return itemUsed;
            } else {
               return stack.onItemUse(player, world, pos, facing, var7, var8, var9);
            }
         } else {
            return true;
         }
      }
   }

   public boolean sendUseItem(EntityPlayer playerIn, World worldIn, ItemStack itemStackIn) {
      if (this.currentGameType == WorldSettings.GameType.SPECTATOR) {
         return false;
      } else {
         this.syncCurrentPlayItem();
         this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(playerIn.inventory.getCurrentItem()));
         int var4 = itemStackIn.stackSize;
         ItemStack var5 = itemStackIn.useItemRightClick(worldIn, playerIn);
         if (var5 != itemStackIn || var5 != null && var5.stackSize != var4) {
            playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = var5;
            if (var5.stackSize == 0) {
               playerIn.inventory.mainInventory[playerIn.inventory.currentItem] = null;
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public EntityPlayerSP func_178892_a(World worldIn, StatFileWriter p_178892_2_) {
      return new EntityPlayerSP(this.mc, worldIn, this.netClientHandler, p_178892_2_);
   }

   public void attackEntity(EntityPlayer playerIn, Entity targetEntity) {
      this.syncCurrentPlayItem();
      ((EventAttack)EventSystem.fire(EventSystem.getInstance(EventAttack.class))).fire(targetEntity, true);
      this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
      if (this.currentGameType != WorldSettings.GameType.SPECTATOR) {
         playerIn.attackTargetEntityWithCurrentItem(targetEntity);
      }

      ((EventAttack)EventSystem.fire(EventSystem.getInstance(EventAttack.class))).fire(targetEntity, false);
   }

   public boolean interactWithEntitySendPacket(EntityPlayer playerIn, Entity targetEntity) {
      this.syncCurrentPlayItem();
      this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
      return this.currentGameType != WorldSettings.GameType.SPECTATOR && playerIn.interactWith(targetEntity);
   }

   public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_) {
      this.syncCurrentPlayItem();
      Vec3 var4 = new Vec3(p_178894_3_.hitVec.xCoord - p_178894_2_.posX, p_178894_3_.hitVec.yCoord - p_178894_2_.posY, p_178894_3_.hitVec.zCoord - p_178894_2_.posZ);
      this.netClientHandler.addToSendQueue(new C02PacketUseEntity(p_178894_2_, var4));
      return this.currentGameType != WorldSettings.GameType.SPECTATOR && p_178894_2_.func_174825_a(p_178894_1_, var4);
   }

   public ItemStack windowClick(int windowId, int slotId, int p_78753_3_, int p_78753_4_, EntityPlayer playerIn) {
      short var6 = playerIn.openContainer.getNextTransactionID(playerIn.inventory);
      ItemStack var7 = playerIn.openContainer.slotClick(slotId, p_78753_3_, p_78753_4_, playerIn);
      this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, p_78753_3_, p_78753_4_, var7, var6));
      return var7;
   }

   public void sendEnchantPacket(int p_78756_1_, int p_78756_2_) {
      this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
   }

   public void sendSlotPacket(ItemStack itemStackIn, int slotId) {
      if (this.currentGameType.isCreative()) {
         this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
      }

   }

   public void sendPacketDropItem(ItemStack itemStackIn) {
      if (this.currentGameType.isCreative() && itemStackIn != null) {
         this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
      }

   }

   public void onStoppedUsingItem(EntityPlayer playerIn) {
      this.syncCurrentPlayItem();
      Module module = (Module)Client.getModuleManager().get(Killaura.class);
      if (!module.isEnabled() || ((Boolean)module.getSetting("AUTOBLOCK").getValue()).booleanValue() && Killaura.loaded.isEmpty() && Killaura.target == null) {
         this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      }

      playerIn.stopUsingItem();
   }

   public boolean gameIsSurvivalOrAdventure() {
      return this.currentGameType.isSurvivalOrAdventure();
   }

   public boolean isNotCreative() {
      return !this.currentGameType.isCreative();
   }

   public boolean isInCreativeMode() {
      return this.currentGameType.isCreative();
   }

   public boolean extendedReach() {
      return this.currentGameType.isCreative();
   }

   public boolean isRidingHorse() {
      return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
   }

   public float getCurBlockDamageMP() {
      return this.curBlockDamageMP;
   }

   public boolean isSpectatorMode() {
      return this.currentGameType == WorldSettings.GameType.SPECTATOR;
   }

   public WorldSettings.GameType func_178889_l() {
      return this.currentGameType;
   }
}
