package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache {
   private static final Map regionsByFilename = Maps.newHashMap();

   public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ) {
      File var3 = new File(worldDir, "region");
      File var4 = new File(var3, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
      RegionFile var5 = (RegionFile)regionsByFilename.get(var4);
      if (var5 != null) {
         return var5;
      } else {
         if (!var3.exists()) {
            var3.mkdirs();
         }

         if (regionsByFilename.size() >= 256) {
            clearRegionFileReferences();
         }

         RegionFile var6 = new RegionFile(var4);
         regionsByFilename.put(var4, var6);
         return var6;
      }
   }

   public static synchronized void clearRegionFileReferences() {
      Iterator var0 = regionsByFilename.values().iterator();

      while(var0.hasNext()) {
         RegionFile var1 = (RegionFile)var0.next();

         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

      regionsByFilename.clear();
   }

   public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ) {
      RegionFile var3 = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
      return var3.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
   }

   public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ) {
      RegionFile var3 = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
      return var3.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
   }
}
