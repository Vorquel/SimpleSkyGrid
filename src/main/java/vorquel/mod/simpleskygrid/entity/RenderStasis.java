package vorquel.mod.simpleskygrid.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderStasis extends Render {
    
    private ResourceLocation texture = new ResourceLocation("textures/blocks/glass.png");
    private double max = .625;
    private double min = -max;
    private IIcon icon = Blocks.glass.getIcon(0, 0);
    private double minU = icon.getMinU();
    private double minV = icon.getMinV();
    private double maxU = icon.getMaxU();
    private double maxV = icon.getMaxV();
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float rot, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        bindEntityTexture(entity);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV(min, min, max, minU, minV);
        tessellator.addVertexWithUV(min, max, max, maxU, minV);
        tessellator.addVertexWithUV(min, max, min, maxU, maxV);
        tessellator.addVertexWithUV(min, min, min, minU, maxV);
        
        tessellator.addVertexWithUV(max, min, min, minU, minV);
        tessellator.addVertexWithUV(max, max, min, maxU, minV);
        tessellator.addVertexWithUV(max, max, max, maxU, maxV);
        tessellator.addVertexWithUV(max, min, max, minU, maxV);
        
        tessellator.addVertexWithUV(max, min, min, minU, minV);
        tessellator.addVertexWithUV(max, min, max, maxU, minV);
        tessellator.addVertexWithUV(min, min, max, maxU, maxV);
        tessellator.addVertexWithUV(min, min, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, max, min, minU, minV);
        tessellator.addVertexWithUV(min, max, max, maxU, minV);
        tessellator.addVertexWithUV(max, max, max, maxU, maxV);
        tessellator.addVertexWithUV(max, max, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, max, min, minU, minV);
        tessellator.addVertexWithUV(max, max, min, maxU, minV);
        tessellator.addVertexWithUV(max, min, min, maxU, maxV);
        tessellator.addVertexWithUV(min, min, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, min, max, minU, minV);
        tessellator.addVertexWithUV(max, min, max, maxU, minV);
        tessellator.addVertexWithUV(max, max, max, maxU, maxV);
        tessellator.addVertexWithUV(min, max, max, minU, maxV);
        
        tessellator.draw();
        
        GL11.glPopMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return texture;
    }
}
