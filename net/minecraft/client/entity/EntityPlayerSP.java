package net.minecraft.client.entity;

import exhibition.Client;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventChat;
import exhibition.event.impl.EventMotionUpdate;
import exhibition.event.impl.EventMove;
import exhibition.event.impl.EventPushBlock;
import exhibition.module.impl.movement.NoSlowdown;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class EntityPlayerSP extends AbstractClientPlayer {
   public final NetHandlerPlayClient sendQueue;
   private final StatFileWriter field_146108_bO;
   private double field_175172_bI;
   private double field_175166_bJ;
   private double field_175167_bK;
   private float field_175164_bL;
   private float field_175165_bM;
   private boolean field_175170_bN;
   private boolean field_175171_bO;
   private int field_175168_bP;
   private boolean field_175169_bQ;
   private String clientBrand;
   public MovementInput movementInput;
   protected Minecraft mc;
   protected int sprintToggleTimer;
   public int sprintingTicksLeft;
   public float renderArmYaw;
   public float renderArmPitch;
   public float prevRenderArmYaw;
   public float prevRenderArmPitch;
   private int horseJumpPowerCounter;
   private float horseJumpPower;
   public float timeInPortal;
   public float prevTimeInPortal;

   public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_) {
      super(worldIn, p_i46278_3_.func_175105_e());
      this.sendQueue = p_i46278_3_;
      this.field_146108_bO = p_i46278_4_;
      this.mc = mcIn;
      this.dimension = 0;
   }

   public boolean isEntityInsideOpaqueBlock() {
      return super.isEntityInsideOpaqueBlock();
   }

   public boolean attackEntityFrom(DamageSource source, float amount) {
      return false;
   }

   public void heal(float p_70691_1_) {
   }

   public void moveEntity(double x, double y, double z) {
      EventMove em = (EventMove)EventSystem.getInstance(EventMove.class);
      em.fire(x, y, z);
      super.moveEntity(em.getX(), em.getY(), em.getZ());
   }

   public void mountEntity(Entity entityIn) {
      super.mountEntity(entityIn);
      if (entityIn instanceof EntityMinecart) {
         this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart)entityIn));
      }

   }

   public void onUpdate() {
      if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
         super.onUpdate();
         if (this.isRiding()) {
            this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
            this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
         } else {
            this.motionUpdates();
         }
      }

   }

   public void motionUpdates() {
      EventMotionUpdate em = (EventMotionUpdate)EventSystem.getInstance(EventMotionUpdate.class);
      em.fire(this.boundingBox.minY, this.rotationYaw, this.rotationPitch, this.onGround);
      if (em.isCancelled()) {
         em.fire();
      } else {
         boolean var1 = this.isSprinting();
         if (var1 != this.field_175171_bO) {
            if (var1) {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
            } else {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.field_175171_bO = var1;
         }

         boolean var2 = this.isSneaking();
         if (var2 != this.field_175170_bN) {
            if (var2) {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
            } else {
               this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.field_175170_bN = var2;
         }

         if (this.func_175160_A()) {
            double var3 = this.posX - this.field_175172_bI;
            double var5 = em.getY() - this.field_175166_bJ;
            double var7 = this.posZ - this.field_175167_bK;
            double var9 = (double)(em.getYaw() - this.field_175164_bL);
            double var11 = (double)(em.getPitch() - this.field_175165_bM);
            boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4D || this.field_175168_bP >= 20;
            boolean var14 = var9 != 0.0D || var11 != 0.0D;
            if (this.ridingEntity != null) {
               this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
               var13 = false;
            } else if ((!var13 || !var14) && !em.shouldAlwaysSend()) {
               if (var13) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.posX, em.getY(), this.posZ, em.isOnground()));
               } else if (var14) {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(em.getYaw(), em.getPitch(), em.isOnground()));
               } else {
                  this.sendQueue.addToSendQueue(new C03PacketPlayer(em.isOnground()));
               }
            } else {
               this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, em.getY(), this.posZ, em.getYaw(), em.getPitch(), em.isOnground()));
            }

            ++this.field_175168_bP;
            if (var13) {
               this.field_175172_bI = this.posX;
               this.field_175166_bJ = this.getEntityBoundingBox().minY;
               this.field_175167_bK = this.posZ;
               this.field_175168_bP = 0;
            }

            if (var14) {
               this.field_175164_bL = em.getYaw();
               this.field_175165_bM = em.getPitch();
            }
         }

         em.fire();
      }
   }

   public EntityItem dropOneItem(boolean p_71040_1_) {
      C07PacketPlayerDigging.Action var2 = p_71040_1_ ? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
      this.sendQueue.addToSendQueue(new C07PacketPlayerDigging(var2, BlockPos.ORIGIN, EnumFacing.DOWN));
      return null;
   }

   protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
   }

   public void sendChatMessage(String text) {
      EventChat ec = (EventChat)EventSystem.getInstance(EventChat.class);
      ec.fire(text);
      if (!ec.isCancelled()) {
         this.sendQueue.addToSendQueue(new C01PacketChatMessage(ec.getText()));
      }
   }

   public void swingItem() {
      super.swingItem();
      this.sendQueue.addToSendQueue(new C0APacketAnimation());
   }

   public void swingItemFake() {
      super.swingItem();
   }

   public void respawnPlayer() {
      this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
   }

   protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
      if (!this.func_180431_b(p_70665_1_)) {
         this.setHealth(this.getHealth() - p_70665_2_);
      }

   }

   public void closeScreen() {
      this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
      this.func_175159_q();
   }

   public void func_175159_q() {
      this.inventory.setItemStack((ItemStack)null);
      super.closeScreen();
      this.mc.displayGuiScreen((GuiScreen)null);
   }

   public void setPlayerSPHealth(float p_71150_1_) {
      if (this.field_175169_bQ) {
         float var2 = this.getHealth() - p_71150_1_;
         if (var2 <= 0.0F) {
            this.setHealth(p_71150_1_);
            if (var2 < 0.0F) {
               this.hurtResistantTime = this.maxHurtResistantTime / 2;
            }
         } else {
            this.lastDamage = var2;
            this.setHealth(this.getHealth());
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.damageEntity(DamageSource.generic, var2);
            this.hurtTime = this.maxHurtTime = 10;
         }
      } else {
         this.setHealth(p_71150_1_);
         this.field_175169_bQ = true;
      }

   }

   public void addStat(StatBase p_71064_1_, int p_71064_2_) {
      if (p_71064_1_ != null && p_71064_1_.isIndependent) {
         super.addStat(p_71064_1_, p_71064_2_);
      }

   }

   public void sendPlayerAbilities() {
      this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
   }

   public boolean func_175144_cb() {
      return true;
   }

   protected void sendHorseJump() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP, (int)(this.getHorseJumpPower() * 100.0F)));
   }

   public void func_175163_u() {
      this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
   }

   public void func_175158_f(String p_175158_1_) {
      this.clientBrand = p_175158_1_;
   }

   public String getClientBrand() {
      return this.clientBrand;
   }

   public StatFileWriter getStatFileWriter() {
      return this.field_146108_bO;
   }

   public void addChatComponentMessage(IChatComponent p_146105_1_) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(p_146105_1_);
   }

   protected boolean pushOutOfBlocks(double x, double y, double z) {
      if (this.noClip) {
         return false;
      } else {
         BlockPos var7 = new BlockPos(x, y, z);
         double var8 = x - (double)var7.getX();
         double var10 = z - (double)var7.getZ();
         if (!this.func_175162_d(var7)) {
            byte var12 = -1;
            double var13 = 9999.0D;
            if (this.func_175162_d(var7.offsetWest()) && var8 < var13) {
               var13 = var8;
               var12 = 0;
            }

            if (this.func_175162_d(var7.offsetEast()) && 1.0D - var8 < var13) {
               var13 = 1.0D - var8;
               var12 = 1;
            }

            if (this.func_175162_d(var7.offsetNorth()) && var10 < var13) {
               var13 = var10;
               var12 = 4;
            }

            if (this.func_175162_d(var7.offsetSouth()) && 1.0D - var10 < var13) {
               var13 = 1.0D - var10;
               var12 = 5;
            }

            float var15 = 0.1F;
            if (var12 == 0) {
               this.motionX = (double)(-var15);
            }

            if (var12 == 1) {
               this.motionX = (double)var15;
            }

            if (var12 == 4) {
               this.motionZ = (double)(-var15);
            }

            if (var12 == 5) {
               this.motionZ = (double)var15;
            }
         }

         return false;
      }
   }

   private boolean func_175162_d(BlockPos p_175162_1_) {
      return !this.worldObj.getBlockState(p_175162_1_).getBlock().isNormalCube() && !this.worldObj.getBlockState(p_175162_1_.offsetUp()).getBlock().isNormalCube();
   }

   public void setSprinting(boolean sprinting) {
      super.setSprinting(sprinting);
      this.sprintingTicksLeft = sprinting ? 600 : 0;
   }

   public void setXPStats(float p_71152_1_, int p_71152_2_, int p_71152_3_) {
      this.experience = p_71152_1_;
      this.experienceTotal = p_71152_2_;
      this.experienceLevel = p_71152_3_;
   }

   public void addChatMessage(IChatComponent message) {
      this.mc.ingameGUI.getChatGUI().printChatMessage(message);
   }

   public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
      return permissionLevel <= 0;
   }

   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
   }

   public void playSound(String name, float volume, float pitch) {
      this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
   }

   public boolean isServerWorld() {
      return true;
   }

   public boolean isRidingHorse() {
      return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse && ((EntityHorse)this.ridingEntity).isHorseSaddled();
   }

   public float getHorseJumpPower() {
      return this.horseJumpPower;
   }

   public void func_175141_a(TileEntitySign p_175141_1_) {
      this.mc.displayGuiScreen(new GuiEditSign(p_175141_1_));
   }

   public void func_146095_a(CommandBlockLogic p_146095_1_) {
      this.mc.displayGuiScreen(new GuiCommandBlock(p_146095_1_));
   }

   public void displayGUIBook(ItemStack bookStack) {
      Item var2 = bookStack.getItem();
      if (var2 == Items.writable_book) {
         this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
      }

   }

   public void displayGUIChest(IInventory chestInventory) {
      String var2 = chestInventory instanceof IInteractionObject ? ((IInteractionObject)chestInventory).getGuiID() : "minecraft:container";
      if ("minecraft:chest".equals(var2)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
      } else if ("minecraft:hopper".equals(var2)) {
         this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
      } else if ("minecraft:furnace".equals(var2)) {
         this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
      } else if ("minecraft:brewing_stand".equals(var2)) {
         this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
      } else if ("minecraft:beacon".equals(var2)) {
         this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
      } else if (!"minecraft:dispenser".equals(var2) && !"minecraft:dropper".equals(var2)) {
         this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
      } else {
         this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
      }

   }

   public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
      this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, p_110298_2_, p_110298_1_));
   }

   public void displayGui(IInteractionObject guiOwner) {
      String var2 = guiOwner.getGuiID();
      if ("minecraft:crafting_table".equals(var2)) {
         this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
      } else if ("minecraft:enchanting_table".equals(var2)) {
         this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
      } else if ("minecraft:anvil".equals(var2)) {
         this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
      }

   }

   public void displayVillagerTradeGui(IMerchant villager) {
      this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
   }

   public void onCriticalHit(Entity p_71009_1_) {
      this.mc.effectRenderer.func_178926_a(p_71009_1_, EnumParticleTypes.CRIT);
   }

   public void onEnchantmentCritical(Entity p_71047_1_) {
      this.mc.effectRenderer.func_178926_a(p_71047_1_, EnumParticleTypes.CRIT_MAGIC);
   }

   public boolean isSneaking() {
      boolean var1 = this.movementInput != null ? this.movementInput.sneak : false;
      return var1 && !this.sleeping;
   }

   public void updateEntityActionState() {
      super.updateEntityActionState();
      if (this.func_175160_A()) {
         this.moveStrafing = this.movementInput.moveStrafe;
         this.moveForward = this.movementInput.moveForward;
         this.isJumping = this.movementInput.jump;
         this.prevRenderArmYaw = this.renderArmYaw;
         this.prevRenderArmPitch = this.renderArmPitch;
         this.renderArmPitch = (float)((double)this.renderArmPitch + (double)(this.rotationPitch - this.renderArmPitch) * 0.5D);
         this.renderArmYaw = (float)((double)this.renderArmYaw + (double)(this.rotationYaw - this.renderArmYaw) * 0.5D);
      }

   }

   protected boolean func_175160_A() {
      return this.mc.func_175606_aa() == this;
   }

   public void onLivingUpdate() {
      if (this.sprintingTicksLeft > 0) {
         --this.sprintingTicksLeft;
         if (this.sprintingTicksLeft == 0) {
            this.setSprinting(false);
         }
      }

      if (this.sprintToggleTimer > 0) {
         --this.sprintToggleTimer;
      }

      this.prevTimeInPortal = this.timeInPortal;
      if (this.inPortal) {
         if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
            this.mc.displayGuiScreen((GuiScreen)null);
         }

         if (this.timeInPortal == 0.0F) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
         }

         this.timeInPortal += 0.0125F;
         if (this.timeInPortal >= 1.0F) {
            this.timeInPortal = 1.0F;
         }

         this.inPortal = false;
      } else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
         this.timeInPortal += 0.006666667F;
         if (this.timeInPortal > 1.0F) {
            this.timeInPortal = 1.0F;
         }
      } else {
         if (this.timeInPortal > 0.0F) {
            this.timeInPortal -= 0.05F;
         }

         if (this.timeInPortal < 0.0F) {
            this.timeInPortal = 0.0F;
         }
      }

      if (this.timeUntilPortal > 0) {
         --this.timeUntilPortal;
      }

      boolean var1 = this.movementInput.jump;
      boolean var2 = this.movementInput.sneak;
      float var3 = 0.8F;
      boolean var4 = this.movementInput.moveForward >= var3;
      this.movementInput.updatePlayerMoveState();
      if (this.isUsingItem() && !this.isRiding() && !Client.getModuleManager().isEnabled(NoSlowdown.class)) {
         this.movementInput.moveStrafe *= 0.2F;
         this.movementInput.moveForward *= 0.2F;
         this.sprintToggleTimer = 0;
      }

      EventPushBlock epb = (EventPushBlock)EventSystem.getInstance(EventPushBlock.class);
      if (!epb.isCancelled()) {
         this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
         this.pushOutOfBlocks(this.posX - (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
         this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double)this.width * 0.35D);
         this.pushOutOfBlocks(this.posX + (double)this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double)this.width * 0.35D);
      }

      boolean var5 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;
      if (this.onGround && !var2 && !var4 && this.movementInput.moveForward >= var3 && !this.isSprinting() && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
         if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
            this.sprintToggleTimer = 7;
         } else {
            this.setSprinting(true);
         }
      }

      if (!this.isSprinting() && this.movementInput.moveForward >= var3 && var5 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.getIsKeyPressed()) {
         this.setSprinting(true);
      }

      if (this.isSprinting() && (this.movementInput.moveForward < var3 && this.movementInput.moveStrafe < var3 || this.isCollidedHorizontally || !var5)) {
         this.setSprinting(false);
      }

      if (this.capabilities.allowFlying) {
         if (this.mc.playerController.isSpectatorMode()) {
            if (!this.capabilities.isFlying) {
               this.capabilities.isFlying = true;
               this.sendPlayerAbilities();
            }
         } else if (!var1 && this.movementInput.jump) {
            if (this.flyToggleTimer == 0) {
               this.flyToggleTimer = 7;
            } else {
               this.capabilities.isFlying = !this.capabilities.isFlying;
               this.sendPlayerAbilities();
               this.flyToggleTimer = 0;
            }
         }
      }

      if (this.capabilities.isFlying && this.func_175160_A()) {
         if (this.movementInput.sneak) {
            this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
         }

         if (this.movementInput.jump) {
            this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
         }
      }

      if (this.isRidingHorse()) {
         if (this.horseJumpPowerCounter < 0) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter == 0) {
               this.horseJumpPower = 0.0F;
            }
         }

         if (var1 && !this.movementInput.jump) {
            this.horseJumpPowerCounter = -10;
            this.sendHorseJump();
         } else if (!var1 && this.movementInput.jump) {
            this.horseJumpPowerCounter = 0;
            this.horseJumpPower = 0.0F;
         } else if (var1) {
            ++this.horseJumpPowerCounter;
            if (this.horseJumpPowerCounter < 10) {
               this.horseJumpPower = (float)this.horseJumpPowerCounter * 0.1F;
            } else {
               this.horseJumpPower = 0.8F + 2.0F / (float)(this.horseJumpPowerCounter - 9) * 0.1F;
            }
         }
      } else {
         this.horseJumpPower = 0.0F;
      }

      super.onLivingUpdate();
      if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
         this.capabilities.isFlying = false;
         this.sendPlayerAbilities();
      }

   }

   public float getDirection() {
      float var1 = this.rotationYaw;
      if (this.moveForward < 0.0F) {
         var1 += 180.0F;
      }

      float forward = 1.0F;
      if (this.moveForward < 0.0F) {
         forward = -0.5F;
      } else if (this.moveForward > 0.0F) {
         forward = 0.5F;
      } else {
         forward = 1.0F;
      }

      if (this.moveStrafing > 0.0F) {
         var1 -= 90.0F * forward;
      }

      if (this.moveStrafing < 0.0F) {
         var1 += 90.0F * forward;
      }

      var1 *= 0.017453292F;
      return var1;
   }
}
