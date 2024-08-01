package net.minecraft.util;

import java.util.Random;
import java.util.UUID;

public class MathHelper {
   public static final float field_180189_a = sqrt_float(2.0F);
   private static final int SIN_BITS = 12;
   private static final int SIN_MASK = 4095;
   private static final int SIN_COUNT = 4096;
   public static final float PI = 3.1415927F;
   public static final float PI2 = 6.2831855F;
   public static final float PId2 = 1.5707964F;
   private static final float radFull = 6.2831855F;
   private static final float degFull = 360.0F;
   private static final float radToIndex = 651.8986F;
   private static final float degToIndex = 11.377778F;
   public static final float deg2Rad = 0.017453292F;
   private static final float[] SIN_TABLE_FAST = new float[4096];
   public static boolean fastMath = false;
   private static final float[] SIN_TABLE = new float[65536];
   private static final int[] multiplyDeBruijnBitPosition;

   public static float sin(float p_76126_0_) {
      return fastMath ? SIN_TABLE_FAST[(int)(p_76126_0_ * 651.8986F) & 4095] : SIN_TABLE[(int)(p_76126_0_ * 10430.378F) & '\uffff'];
   }

   public static float cos(float p_76134_0_) {
      return fastMath ? SIN_TABLE_FAST[(int)((p_76134_0_ + 1.5707964F) * 651.8986F) & 4095] : SIN_TABLE[(int)(p_76134_0_ * 10430.378F + 16384.0F) & '\uffff'];
   }

   public static float sqrt_float(float p_76129_0_) {
      return (float)Math.sqrt((double)p_76129_0_);
   }

   public static float sqrt_double(double p_76133_0_) {
      return (float)Math.sqrt(p_76133_0_);
   }

   public static int floor_float(float p_76141_0_) {
      int var1 = (int)p_76141_0_;
      return p_76141_0_ < (float)var1 ? var1 - 1 : var1;
   }

   public static int truncateDoubleToInt(double p_76140_0_) {
      return (int)(p_76140_0_ + 1024.0D) - 1024;
   }

   public static int floor_double(double p_76128_0_) {
      int var2 = (int)p_76128_0_;
      return p_76128_0_ < (double)var2 ? var2 - 1 : var2;
   }

   public static long floor_double_long(double p_76124_0_) {
      long var2 = (long)p_76124_0_;
      return p_76124_0_ < (double)var2 ? var2 - 1L : var2;
   }

   public static int func_154353_e(double p_154353_0_) {
      return (int)(p_154353_0_ >= 0.0D ? p_154353_0_ : -p_154353_0_ + 1.0D);
   }

   public static float abs(float p_76135_0_) {
      return p_76135_0_ >= 0.0F ? p_76135_0_ : -p_76135_0_;
   }

   public static int abs_int(int p_76130_0_) {
      return p_76130_0_ >= 0 ? p_76130_0_ : -p_76130_0_;
   }

   public static int ceiling_float_int(float p_76123_0_) {
      int var1 = (int)p_76123_0_;
      return p_76123_0_ > (float)var1 ? var1 + 1 : var1;
   }

   public static int ceiling_double_int(double p_76143_0_) {
      int var2 = (int)p_76143_0_;
      return p_76143_0_ > (double)var2 ? var2 + 1 : var2;
   }

   public static int clamp_int(int value, int min, int max) {
      return value < min ? min : (value > max ? max : value);
   }

   public static float clamp_float(float value, float min, float max) {
      return value < min ? min : (value > max ? max : value);
   }

   public static double clamp_double(double value, double min, double max) {
      return value < min ? min : (value > max ? max : value);
   }

   public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_) {
      return p_151238_4_ < 0.0D ? p_151238_0_ : (p_151238_4_ > 1.0D ? p_151238_2_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
   }

   public static double abs_max(double p_76132_0_, double p_76132_2_) {
      if (p_76132_0_ < 0.0D) {
         p_76132_0_ = -p_76132_0_;
      }

      if (p_76132_2_ < 0.0D) {
         p_76132_2_ = -p_76132_2_;
      }

      return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
   }

   public static int bucketInt(int p_76137_0_, int p_76137_1_) {
      return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
   }

   public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_) {
      return p_76136_1_ >= p_76136_2_ ? p_76136_1_ : p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
   }

   public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_) {
      return p_151240_1_ >= p_151240_2_ ? p_151240_1_ : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
   }

   public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_) {
      return p_82716_1_ >= p_82716_3_ ? p_82716_1_ : p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
   }

   public static double average(long[] p_76127_0_) {
      long var1 = 0L;
      long[] var3 = p_76127_0_;
      int var4 = p_76127_0_.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long var6 = var3[var5];
         var1 += var6;
      }

      return (double)var1 / (double)p_76127_0_.length;
   }

   public static boolean func_180185_a(float p_180185_0_, float p_180185_1_) {
      return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
   }

   public static int func_180184_b(int p_180184_0_, int p_180184_1_) {
      return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
   }

   public static float wrapAngleTo180_float(float angle) {
      angle %= 360.0F;
      if (angle >= 180.0F) {
         angle -= 360.0F;
      }

      if (angle < -180.0F) {
         angle += 360.0F;
      }

      return angle;
   }

   public static float wrapAngleTo90_float(float angle) {
      angle %= 180.0F;
      if (angle >= 90.0F) {
         angle -= 180.0F;
      }

      if (angle < -90.0F) {
         angle += 180.0F;
      }

      return angle;
   }

   public static double wrapAngleTo180_double(double p_76138_0_) {
      p_76138_0_ %= 360.0D;
      if (p_76138_0_ >= 180.0D) {
         p_76138_0_ -= 360.0D;
      }

      if (p_76138_0_ < -180.0D) {
         p_76138_0_ += 360.0D;
      }

      return p_76138_0_;
   }

   public static int parseIntWithDefault(String p_82715_0_, int p_82715_1_) {
      try {
         return Integer.parseInt(p_82715_0_);
      } catch (Throwable var3) {
         return p_82715_1_;
      }
   }

   public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_) {
      return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
   }

   public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_) {
      try {
         return Double.parseDouble(p_82712_0_);
      } catch (Throwable var4) {
         return p_82712_1_;
      }
   }

   public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_) {
      return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
   }

   public static int roundUpToPowerOfTwo(int p_151236_0_) {
      int var1 = p_151236_0_ - 1;
      var1 |= var1 >> 1;
      var1 |= var1 >> 2;
      var1 |= var1 >> 4;
      var1 |= var1 >> 8;
      var1 |= var1 >> 16;
      return var1 + 1;
   }

   private static boolean isPowerOfTwo(int p_151235_0_) {
      return p_151235_0_ != 0 && (p_151235_0_ & p_151235_0_ - 1) == 0;
   }

   private static int calculateLogBaseTwoDeBruijn(int p_151241_0_) {
      p_151241_0_ = isPowerOfTwo(p_151241_0_) ? p_151241_0_ : roundUpToPowerOfTwo(p_151241_0_);
      return multiplyDeBruijnBitPosition[(int)((long)p_151241_0_ * 125613361L >> 27) & 31];
   }

   public static int calculateLogBaseTwo(int p_151239_0_) {
      return calculateLogBaseTwoDeBruijn(p_151239_0_) - (isPowerOfTwo(p_151239_0_) ? 0 : 1);
   }

   public static int func_154354_b(int p_154354_0_, int p_154354_1_) {
      if (p_154354_1_ == 0) {
         return 0;
      } else if (p_154354_0_ == 0) {
         return p_154354_1_;
      } else {
         if (p_154354_0_ < 0) {
            p_154354_1_ *= -1;
         }

         int var2 = p_154354_0_ % p_154354_1_;
         return var2 == 0 ? p_154354_0_ : p_154354_0_ + p_154354_1_ - var2;
      }
   }

   public static int func_180183_b(float p_180183_0_, float p_180183_1_, float p_180183_2_) {
      return func_180181_b(floor_float(p_180183_0_ * 255.0F), floor_float(p_180183_1_ * 255.0F), floor_float(p_180183_2_ * 255.0F));
   }

   public static int func_180181_b(int p_180181_0_, int p_180181_1_, int p_180181_2_) {
      int var3 = (p_180181_0_ << 8) + p_180181_1_;
      var3 = (var3 << 8) + p_180181_2_;
      return var3;
   }

   public static int func_180188_d(int p_180188_0_, int p_180188_1_) {
      int var2 = (p_180188_0_ & 16711680) >> 16;
      int var3 = (p_180188_1_ & 16711680) >> 16;
      int var4 = (p_180188_0_ & '\uff00') >> 8;
      int var5 = (p_180188_1_ & '\uff00') >> 8;
      int var6 = p_180188_0_ & 255;
      int var7 = p_180188_1_ & 255;
      int var8 = (int)((float)var2 * (float)var3 / 255.0F);
      int var9 = (int)((float)var4 * (float)var5 / 255.0F);
      int var10 = (int)((float)var6 * (float)var7 / 255.0F);
      return p_180188_0_ & -16777216 | var8 << 16 | var9 << 8 | var10;
   }

   public static long func_180186_a(Vec3i pos) {
      return func_180187_c(pos.getX(), pos.getY(), pos.getZ());
   }

   public static long func_180187_c(int x, int y, int z) {
      long var3 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
      var3 = var3 * var3 * 42317861L + var3 * 11L;
      return var3;
   }

   public static UUID func_180182_a(Random p_180182_0_) {
      long var1 = p_180182_0_.nextLong() & -61441L | 16384L;
      long var3 = p_180182_0_.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
      return new UUID(var1, var3);
   }

   static {
      int i;
      for(i = 0; i < 65536; ++i) {
         SIN_TABLE[i] = (float)Math.sin((double)i * 3.141592653589793D * 2.0D / 65536.0D);
      }

      multiplyDeBruijnBitPosition = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};

      for(i = 0; i < 4096; ++i) {
         SIN_TABLE_FAST[i] = (float)Math.sin((double)(((float)i + 0.5F) / 4096.0F * 6.2831855F));
      }

      for(i = 0; i < 360; i += 90) {
         SIN_TABLE_FAST[(int)((float)i * 11.377778F) & 4095] = (float)Math.sin((double)((float)i * 0.017453292F));
      }

   }
}
