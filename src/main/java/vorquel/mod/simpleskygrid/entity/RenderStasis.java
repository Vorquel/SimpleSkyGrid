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
    private double maxY = max + .625;
    private double minY = min + .625;
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
        
        tessellator.addVertexWithUV(min, minY, max, minU, minV);
        tessellator.addVertexWithUV(min, maxY, max, maxU, minV);
        tessellator.addVertexWithUV(min, maxY, min, maxU, maxV);
        tessellator.addVertexWithUV(min, minY, min, minU, maxV);
        
        tessellator.addVertexWithUV(max, minY, min, minU, minV);
        tessellator.addVertexWithUV(max, maxY, min, maxU, minV);
        tessellator.addVertexWithUV(max, maxY, max, maxU, maxV);
        tessellator.addVertexWithUV(max, minY, max, minU, maxV);
        
        tessellator.addVertexWithUV(max, minY, min, minU, minV);
        tessellator.addVertexWithUV(max, minY, max, maxU, minV);
        tessellator.addVertexWithUV(min, minY, max, maxU, maxV);
        tessellator.addVertexWithUV(min, minY, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, maxY, min, minU, minV);
        tessellator.addVertexWithUV(min, maxY, max, maxU, minV);
        tessellator.addVertexWithUV(max, maxY, max, maxU, maxV);
        tessellator.addVertexWithUV(max, maxY, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, maxY, min, minU, minV);
        tessellator.addVertexWithUV(max, maxY, min, maxU, minV);
        tessellator.addVertexWithUV(max, minY, min, maxU, maxV);
        tessellator.addVertexWithUV(min, minY, min, minU, maxV);
        
        tessellator.addVertexWithUV(min, minY, max, minU, minV);
        tessellator.addVertexWithUV(max, minY, max, maxU, minV);
        tessellator.addVertexWithUV(max, maxY, max, maxU, maxV);
        tessellator.addVertexWithUV(min, maxY, max, minU, maxV);
        
        tessellator.draw();
        
        GL11.glPopMatrix();
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return texture;
    }
}
