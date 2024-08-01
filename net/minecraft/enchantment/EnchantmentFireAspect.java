package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFireAspect extends Enchantment {
   protected EnchantmentFireAspect(int p_i45770_1_, ResourceLocation p_i45770_2_, int p_i45770_3_) {
      super(p_i45770_1_, p_i45770_2_, p_i45770_3_, EnumEnchantmentType.WEAPON);
      this.setName("fire");
   }

   public int getMinEnchantability(int p_77321_1_) {
      return 10 + 20 * (p_77321_1_ - 1);
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return super.getMinEnchantability(p_77317_1_) + 50;
   }

   public int getMaxLevel() {
      return 2;
   }
}
