package gregtech.common.potion;

import gregtech.api.GTValues;

import gregtech.api.util.GTLog;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*

GTFOPotion is a class made by Vazkii (with a few alterations here and there)

 */

public abstract class GTPotion extends Potion {
    private static final ResourceLocation resource = new ResourceLocation(GTValues.MODID, "textures/gui/potions.png");
    private final int iconIndex;

    public GTPotion(String name, boolean badEffect, int color, int iconIndex) {
        super(badEffect, color);
        setRegistryName(new ResourceLocation(GTValues.MODID, name));
        try {
            setPotionName(GTValues.MODID + ".potion." + name);
        } catch (Exception e) {
            GTLog.logger.error("You are currently using the wrong type of jar of GTFO. This usually means that I, bruberu, accidentally released the wrong version. Report this immediately!");
            throw e;
        }
        this.iconIndex = iconIndex;
        GTPotions.POTIONS.add(this);
    }

    public boolean hasEffect(EntityLivingBase entity) {
        return hasEffect(entity, this);
    }

    public boolean hasEffect(EntityLivingBase entity, Potion potion) {
        return entity.getActivePotionEffect(potion) != null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        render(x + 6, y + 7, 1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
        render(x + 3, y + 3, alpha);
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return this.canRender();
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return this.canRender();
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect) {
        return this.canRender();
    }

    @SideOnly(Side.CLIENT)
    private void render(int x, int y, float alpha) {
        if (this.canRender()) {
            Minecraft.getMinecraft().renderEngine.bindTexture(resource);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buf = tessellator.getBuffer();
            buf.begin(7, DefaultVertexFormats.POSITION_TEX);
            GlStateManager.color(1, 1, 1, alpha);

            int textureX = iconIndex % 8 * 18;
            int textureY = 198 + iconIndex / 8 * 18;

            buf.pos(x, y + 18, 0).tex(textureX * 0.00390625, (textureY + 18) * 0.00390625).endVertex();
            buf.pos(x + 18, y + 18, 0).tex((textureX + 18) * 0.00390625, (textureY + 18) * 0.00390625).endVertex();
            buf.pos(x + 18, y, 0).tex((textureX + 18) * 0.00390625, textureY * 0.00390625).endVertex();
            buf.pos(x, y, 0).tex(textureX * 0.00390625, textureY * 0.00390625).endVertex();

            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    protected abstract boolean canRender();


}
