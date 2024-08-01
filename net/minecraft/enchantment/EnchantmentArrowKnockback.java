package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowKnockback extends Enchantment {
   public EnchantmentArrowKnockback(int p_i45775_1_, ResourceLocation p_i45775_2_, int p_i45775_3_) {
      super(p_i45775_1_, p_i45775_2_, p_i45775_3_, EnumEnchantmentType.BOW);
      this.setName("arrowKnockback");
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 12 + (p_77321_1_ - 1) * 20;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return this.getMinEnchantability(p_77317_1_) + 25;
   }

   public int getMaxLevel() {
      return 2;
   }
}
