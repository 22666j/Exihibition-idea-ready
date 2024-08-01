package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkProviderSettings;
import org.lwjgl.input.Keyboard;

public class GuiScreenCustomizePresets extends GuiScreen {
   private static final List presetList = Lists.newArrayList();
   private GuiScreenCustomizePresets.ListPreset listPresets;
   private GuiButton btnSelect;
   private GuiTextField txtField;
   private GuiCustomizeWorldScreen field_175314_r;
   protected String title = "Customize World Presets";
   private String strShare;
   private String strList;

   public GuiScreenCustomizePresets(GuiCustomizeWorldScreen p_i45524_1_) {
      this.field_175314_r = p_i45524_1_;
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.title = I18n.format("createWorld.customize.custom.presets.title");
      this.strShare = I18n.format("createWorld.customize.presets.share");
      this.strList = I18n.format("createWorld.customize.presets.list");
      this.txtField = new GuiTextField(2, this.fontRendererObj, 50, 40, this.width - 100, 20);
      this.listPresets = new GuiScreenCustomizePresets.ListPreset();
      this.txtField.setMaxStringLength(2000);
      this.txtField.setText(this.field_175314_r.func_175323_a());
      this.buttonList.add(this.btnSelect = new GuiButton(0, this.width / 2 - 102, this.height - 27, 100, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 3, this.height - 27, 100, 20, I18n.format("gui.cancel")));
      this.func_175304_a();
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.listPresets.handleMouseScrolling();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.txtField.mouseClicked(mouseX, mouseY, mouseButton);
      super.mouseClicked(mouseX, mouseY, mouseButton);
   }

   protected void keyTyped(char typedChar, int keyCode) throws IOException {
      if (!this.txtField.textboxKeyTyped(typedChar, keyCode)) {
         super.keyTyped(typedChar, keyCode);
      }

   }

   protected void actionPerformed(GuiButton button) throws IOException {
      switch(button.id) {
      case 0:
         this.field_175314_r.func_175324_a(this.txtField.getText());
         this.mc.displayGuiScreen(this.field_175314_r);
         break;
      case 1:
         this.mc.displayGuiScreen(this.field_175314_r);
      }

   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      this.drawDefaultBackground();
      this.listPresets.drawScreen(mouseX, mouseY, partialTicks);
      this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
      this.drawString(this.fontRendererObj, this.strShare, 50, 30, 10526880);
      this.drawString(this.fontRendererObj, this.strList, 50, 70, 10526880);
      this.txtField.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTicks);
   }

   public void updateScreen() {
      this.txtField.updateCursorCounter();
      super.updateScreen();
   }

   public void func_175304_a() {
      this.btnSelect.enabled = this.func_175305_g();
   }

   private boolean func_175305_g() {
      return this.listPresets.field_178053_u > -1 && this.listPresets.field_178053_u < presetList.size() || this.txtField.getText().length() > 1;
   }

   static {
      ChunkProviderSettings.Factory tempFactory = ChunkProviderSettings.Factory.createFactory("{ \"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":8.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":0.5, \"biomeScaleWeight\":2.0, \"biomeScaleOffset\":0.375, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":255 }");
      ResourceLocation tempResource = new ResourceLocation("textures/gui/presets/water.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.waterWorld"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":3000.0, \"heightScale\":6000.0, \"upperLimitScale\":250.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      tempResource = new ResourceLocation("textures/gui/presets/isles.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.isleLand"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":5.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":1.0, \"biomeScaleWeight\":4.0, \"biomeScaleOffset\":1.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      tempResource = new ResourceLocation("textures/gui/presets/delight.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveDelight"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":738.41864, \"heightScale\":157.69133, \"upperLimitScale\":801.4267, \"lowerLimitScale\":1254.1643, \"depthNoiseScaleX\":374.93652, \"depthNoiseScaleZ\":288.65228, \"depthNoiseScaleExponent\":1.2092624, \"mainNoiseScaleX\":1355.9908, \"mainNoiseScaleY\":745.5343, \"mainNoiseScaleZ\":1183.464, \"baseSize\":1.8758626, \"stretchY\":1.7137525, \"biomeDepthWeight\":1.7553768, \"biomeDepthOffset\":3.4701107, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":2.535211, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }");
      tempResource = new ResourceLocation("textures/gui/presets/madness.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.mountains"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":1000.0, \"mainNoiseScaleY\":3000.0, \"mainNoiseScaleZ\":1000.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":20 }");
      tempResource = new ResourceLocation("textures/gui/presets/drought.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.drought"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":2.0, \"lowerLimitScale\":64.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":6 }");
      tempResource = new ResourceLocation("textures/gui/presets/chaos.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveChaos"), tempResource, tempFactory));
      tempFactory = ChunkProviderSettings.Factory.createFactory("{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":40 }");
      tempResource = new ResourceLocation("textures/gui/presets/luck.png");
      presetList.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.goodLuck"), tempResource, tempFactory));
   }

   class ListPreset extends GuiSlot {
      public int field_178053_u = -1;

      public ListPreset() {
         super(GuiScreenCustomizePresets.this.mc, GuiScreenCustomizePresets.this.width, GuiScreenCustomizePresets.this.height, 80, GuiScreenCustomizePresets.this.height - 32, 38);
      }

      protected int getSize() {
         return GuiScreenCustomizePresets.presetList.size();
      }

      protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
         this.field_178053_u = slotIndex;
         GuiScreenCustomizePresets.this.func_175304_a();
         GuiScreenCustomizePresets.this.txtField.setText(((GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.presetList.get(GuiScreenCustomizePresets.this.listPresets.field_178053_u)).field_178954_c.toString());
      }

      protected boolean isSelected(int slotIndex) {
         return slotIndex == this.field_178053_u;
      }

      protected void drawBackground() {
      }

      private void func_178051_a(int p_178051_1_, int p_178051_2_, ResourceLocation p_178051_3_) {
         int var4 = p_178051_1_ + 5;
         GuiScreenCustomizePresets.this.drawHorizontalLine(var4 - 1, var4 + 32, p_178051_2_ - 1, -2039584);
         GuiScreenCustomizePresets.this.drawHorizontalLine(var4 - 1, var4 + 32, p_178051_2_ + 32, -6250336);
         GuiScreenCustomizePresets var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawVerticalLine(var4 - 1, p_178051_2_ - 1, p_178051_2_ + 32, -2039584);
         var10000 = GuiScreenCustomizePresets.this;
         GuiScreenCustomizePresets.drawVerticalLine(var4 + 32, p_178051_2_ - 1, p_178051_2_ + 32, -6250336);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(p_178051_3_);
         boolean var6 = true;
         boolean var7 = true;
         Tessellator var8 = Tessellator.getInstance();
         WorldRenderer var9 = var8.getWorldRenderer();
         var9.startDrawingQuads();
         var9.addVertexWithUV((double)(var4 + 0), (double)(p_178051_2_ + 32), 0.0D, 0.0D, 1.0D);
         var9.addVertexWithUV((double)(var4 + 32), (double)(p_178051_2_ + 32), 0.0D, 1.0D, 1.0D);
         var9.addVertexWithUV((double)(var4 + 32), (double)(p_178051_2_ + 0), 0.0D, 1.0D, 0.0D);
         var9.addVertexWithUV((double)(var4 + 0), (double)(p_178051_2_ + 0), 0.0D, 0.0D, 0.0D);
         var8.draw();
      }

      protected void drawSlot(int p_180791_1_, int p_180791_2_, int p_180791_3_, int p_180791_4_, int p_180791_5_, int p_180791_6_) {
         GuiScreenCustomizePresets.Info var7 = (GuiScreenCustomizePresets.Info)GuiScreenCustomizePresets.presetList.get(p_180791_1_);
         this.func_178051_a(p_180791_2_, p_180791_3_, var7.field_178953_b);
         GuiScreenCustomizePresets.this.fontRendererObj.drawString(var7.field_178955_a, (float)(p_180791_2_ + 32 + 10), (float)(p_180791_3_ + 14), 16777215);
      }
   }

   static class Info {
      public String field_178955_a;
      public ResourceLocation field_178953_b;
      public ChunkProviderSettings.Factory field_178954_c;

      public Info(String p_i45523_1_, ResourceLocation p_i45523_2_, ChunkProviderSettings.Factory p_i45523_3_) {
         this.field_178955_a = p_i45523_1_;
         this.field_178953_b = p_i45523_2_;
         this.field_178954_c = p_i45523_3_;
      }
   }
}
