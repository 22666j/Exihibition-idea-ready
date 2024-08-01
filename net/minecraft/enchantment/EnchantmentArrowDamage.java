package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentArrowDamage extends Enchantment {
   public EnchantmentArrowDamage(int p_i45778_1_, ResourceLocation p_i45778_2_, int p_i45778_3_) {
      super(p_i45778_1_, p_i45778_2_, p_i45778_3_, EnumEnchantmentType.BOW);
      this.setName("arrowDamage");
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 1 + (p_77321_1_ - 1) * 10;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return this.getMinEnchantability(p_77317_1_) + 15;
   }

   public int getMaxLevel() {
      return 5;
   }
}
