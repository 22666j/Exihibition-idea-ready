package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentLootBonus extends Enchantment {
   protected EnchantmentLootBonus(int p_i45767_1_, ResourceLocation p_i45767_2_, int p_i45767_3_, EnumEnchantmentType p_i45767_4_) {
      super(p_i45767_1_, p_i45767_2_, p_i45767_3_, p_i45767_4_);
      if (p_i45767_4_ == EnumEnchantmentType.DIGGER) {
         this.setName("lootBonusDigger");
      } else if (p_i45767_4_ == EnumEnchantmentType.FISHING_ROD) {
         this.setName("lootBonusFishing");
      } else {
         this.setName("lootBonus");
      }

   }

   public int getMinEnchantability(int p_77321_1_) {
      return 15 + (p_77321_1_ - 1) * 9;
   }

   public int getMaxEnchantability(int p_77317_1_) {
      return super.getMinEnchantability(p_77317_1_) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }

   public boolean canApplyTogether(Enchantment p_77326_1_) {
      return super.canApplyTogether(p_77326_1_) && p_77326_1_.effectId != Enchantment.silkTouch.effectId;
   }
}
