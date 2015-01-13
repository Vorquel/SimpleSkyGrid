package vorquel.mod.simpleskygrid.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.helper.Ref;

//Because over engineering is always the right solution
public abstract class WorldProviderSkyGrid extends WorldProvider {

    private static Class<? extends WorldProvider> otherType;
    private WorldProvider other;
    private String name;

    public static void setOtherType(Class<? extends WorldProvider> other) {
        WorldProviderSkyGrid.otherType = other;
    }

    protected WorldProviderSkyGrid(String name) {
        this.name = name;
        try {
            other = otherType.newInstance();
            updatePublicFields();
        } catch(Exception e) {
            SimpleSkyGrid.logger.fatal(String.format("Unable to create WorldProvider%s proxy", name));
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        IChunkProvider temp = other.createChunkGenerator();
        updatePublicFields();
        if(worldObj.getWorldInfo().getTerrainType() == Ref.worldType)
            return new ChunkProviderSkyGrid(worldObj, worldObj.getSeed(), name);
        return temp;
    }

    @Override
    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        boolean temp = other.canCoordinateBeSpawn(p_76566_1_, p_76566_2_);
        updatePublicFields();
        return temp;
    }

    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        float temp = other.calculateCelestialAngle(p_76563_1_, p_76563_3_);
        updatePublicFields();
        return temp;
    }

    @Override
    public int getMoonPhase(long p_76559_1_) {
        int temp = other.getMoonPhase(p_76559_1_);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean isSurfaceWorld() {
        boolean temp = other.isSurfaceWorld();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        float[] temp = other.calcSunriseSunsetColors(p_76560_1_, p_76560_2_);
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        Vec3 temp = other.getFogColor(p_76562_1_, p_76562_2_);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean canRespawnHere() {
        boolean temp = other.canRespawnHere();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        float temp = other.getCloudHeight();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        boolean temp = other.isSkyColored();
        updatePublicFields();
        return temp;
    }

    @Override
    public ChunkCoordinates getEntrancePortalLocation() {
        ChunkCoordinates temp = other.getEntrancePortalLocation();
        updatePublicFields();
        return temp;
    }

    @Override
    public int getAverageGroundLevel() {
        int temp = other.getAverageGroundLevel();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean getWorldHasVoidParticles() {
        boolean temp = other.getWorldHasVoidParticles();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor() {
        double temp = other.getVoidFogYFactor();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        boolean temp = other.doesXZShowFog(p_76568_1_, p_76568_2_);
        updatePublicFields();
        return temp;
    }

    @Override
    public String getDimensionName() {
        String temp = other.getDimensionName();
        updatePublicFields();
        return temp;
    }

    @Override
    public void setDimension(int dim) {
        other.setDimension(dim);
        updatePublicFields();
    }

    @Override
    public String getSaveFolder() {
        String temp = other.getSaveFolder();
        updatePublicFields();
        return temp;
    }

    @Override
    public String getWelcomeMessage() {
        String temp = other.getWelcomeMessage();
        updatePublicFields();
        return temp;
    }

    @Override
    public String getDepartMessage() {
        String temp = other.getDepartMessage();
        updatePublicFields();
        return temp;
    }

    @Override
    public double getMovementFactor() {
        double temp = other.getMovementFactor();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getSkyRenderer() {
        IRenderHandler temp = other.getSkyRenderer();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setSkyRenderer(IRenderHandler skyRenderer) {
        other.setSkyRenderer(skyRenderer);
        updatePublicFields();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getCloudRenderer() {
        IRenderHandler temp = other.getCloudRenderer();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setCloudRenderer(IRenderHandler renderer) {
        other.setCloudRenderer(renderer);
        updatePublicFields();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderHandler getWeatherRenderer() {
        IRenderHandler temp = other.getWeatherRenderer();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setWeatherRenderer(IRenderHandler renderer) {
        other.setWeatherRenderer(renderer);
        updatePublicFields();
    }

    @Override
    public ChunkCoordinates getRandomizedSpawnPoint() {
        ChunkCoordinates temp = other.getRandomizedSpawnPoint();
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        boolean temp = other.shouldMapSpin(entity, x, y, z);
        updatePublicFields();
        return temp;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        int temp = other.getRespawnDimension(player);
        updatePublicFields();
        return temp;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        BiomeGenBase temp = other.getBiomeGenForCoords(x, z);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean isDaytime() {
        boolean temp = other.isDaytime();
        updatePublicFields();
        return temp;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        float temp = other.getSunBrightnessFactor(par1);
        updatePublicFields();
        return temp;
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        float temp = other.getCurrentMoonPhaseFactor();
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        Vec3 temp = other.getSkyColor(cameraEntity, partialTicks);
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 drawClouds(float partialTicks) {
        Vec3 temp = other.drawClouds(partialTicks);
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1) {
        float temp = other.getSunBrightness(par1);
        updatePublicFields();
        return temp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1) {
        float temp = other.getStarBrightness(par1);
        updatePublicFields();
        return temp;
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        other.setAllowedSpawnTypes(allowHostile, allowPeaceful);
        updatePublicFields();
    }

    @Override
    public void calculateInitialWeather() {
        other.calculateInitialWeather();
        updatePublicFields();
    }

    @Override
    public void updateWeather() {
        other.updateWeather();
        updatePublicFields();
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        boolean temp = other.canBlockFreeze(x, y, z, byWater);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        boolean temp = other.canSnowAt(x, y, z, checkLight);
        updatePublicFields();
        return temp;
    }

    @Override
    public void setWorldTime(long time) {
        other.setWorldTime(time);
        updatePublicFields();
    }

    @Override
    public long getSeed() {
        long temp = other.getSeed();
        updatePublicFields();
        return temp;
    }

    @Override
    public long getWorldTime() {
        long temp = other.getWorldTime();
        updatePublicFields();
        return temp;
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        ChunkCoordinates temp = other.getSpawnPoint();
        updatePublicFields();
        return temp;
    }

    @Override
    public void setSpawnPoint(int x, int y, int z) {
        other.setSpawnPoint(x, y, z);
        updatePublicFields();
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, int x, int y, int z) {
        boolean temp = other.canMineBlock(player, x, y, z);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        boolean temp = other.isBlockHighHumidity(x, y, z);
        updatePublicFields();
        return temp;
    }

    @Override
    public int getHeight() {
        int temp = other.getHeight();
        updatePublicFields();
        return temp;
    }

    @Override
    public int getActualHeight() {
        int temp = other.getActualHeight();
        updatePublicFields();
        return temp;
    }

    @Override
    public double getHorizon() {
        double temp = other.getHorizon();
        updatePublicFields();
        return temp;
    }

    @Override
    public void resetRainAndThunder() {
        other.resetRainAndThunder();
        updatePublicFields();
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        boolean temp = other.canDoLightning(chunk);
        updatePublicFields();
        return temp;
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        boolean temp = other.canDoRainSnowIce(chunk);
        updatePublicFields();
        return temp;
    }

    private void updatePublicFields() {
        if(other.worldObj != worldObj)
            worldObj = other.worldObj;
        if(other.field_82913_c != field_82913_c)
            field_82913_c = other.field_82913_c;
        if(other.worldChunkMgr != worldChunkMgr)
            worldChunkMgr = other.worldChunkMgr;
        if(other.isHellWorld != isHellWorld)
            isHellWorld = other.isHellWorld;
        if(other.hasNoSky != hasNoSky)
            hasNoSky = other.hasNoSky;
        if(other.lightBrightnessTable != lightBrightnessTable)
            lightBrightnessTable = other.lightBrightnessTable;
        if(other.dimensionId != dimensionId)
            dimensionId = other.dimensionId;
    }

    public static class Surface extends WorldProviderSkyGrid {
        public Surface() {
            super("Surface");
        }
    }

    public static class Hell extends WorldProviderSkyGrid {
        public Hell() {
            super("Hell");
        }
    }

    public static class End extends WorldProviderSkyGrid {
        public End() {
            super("End");
        }
    }
}
