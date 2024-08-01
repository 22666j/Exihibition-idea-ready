package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry {
   private final GuiMultiplayer field_148292_c;
   protected final Minecraft field_148293_a;
   protected final LanServerDetector.LanServer field_148291_b;
   private long field_148290_d = 0L;

   protected ServerListEntryLanDetected(GuiMultiplayer p_i45046_1_, LanServerDetector.LanServer p_i45046_2_) {
      this.field_148292_c = p_i45046_1_;
      this.field_148291_b = p_i45046_2_;
      this.field_148293_a = Minecraft.getMinecraft();
   }

   public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected) {
      this.field_148293_a.fontRendererObj.drawString(I18n.format("lanServer.title"), (float)(x + 32 + 3), (float)(y + 1), 16777215);
      this.field_148293_a.fontRendererObj.drawString(this.field_148291_b.getServerMotd(), (float)(x + 32 + 3), (float)(y + 12), 8421504);
      if (this.field_148293_a.gameSettings.hideServerAddress) {
         this.field_148293_a.fontRendererObj.drawString(I18n.format("selectServer.hiddenAddress"), (float)(x + 32 + 3), (float)(y + 12 + 11), 3158064);
      } else {
         this.field_148293_a.fontRendererObj.drawString(this.field_148291_b.getServerIpPort(), (float)(x + 32 + 3), (float)(y + 12 + 11), 3158064);
      }

   }

   public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
      this.field_148292_c.selectServer(p_148278_1_);
      if (Minecraft.getSystemTime() - this.field_148290_d < 250L) {
         this.field_148292_c.connectToSelected();
      }

      this.field_148290_d = Minecraft.getSystemTime();
      return false;
   }

   public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
   }

   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
   }

   public LanServerDetector.LanServer getLanServer() {
      return this.field_148291_b;
   }
}
