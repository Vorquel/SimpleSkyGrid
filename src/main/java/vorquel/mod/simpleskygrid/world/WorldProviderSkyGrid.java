package vorquel.mod.simpleskygrid.world;

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
import vorquel.mod.simpleskygrid.helper.Ref;

public abstract class WorldProviderSkyGrid extends WorldProvider {

    private WorldProvider other;
    private boolean isWorldProviderNull = true;
    private boolean hasBeenRegistered = false;

    public WorldProviderSkyGrid() {
        other = new WorldProviderNull();
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        IChunkProvider temp = other.createChunkGenerator();
        if(worldObj.getWorldInfo().getTerrainType() == Ref.worldType)
            return new ChunkProviderSkyGrid(worldObj, worldObj.getSeed(), dimensionId);
        return temp;
    }

    @Override
    protected void generateLightBrightnessTable() {
        if(hasBeenRegistered)
            return;
        hasBeenRegistered = true;
        other.registerWorld(worldObj);
        lightBrightnessTable = other.lightBrightnessTable;
        worldChunkMgr = other.worldChunkMgr;
    }

    @Override
    protected void registerWorldChunkManager() {
        if(hasBeenRegistered)
            return;
        hasBeenRegistered = true;
        other.registerWorld(worldObj);
        lightBrightnessTable = other.lightBrightnessTable;
        worldChunkMgr = other.worldChunkMgr;
    }

    @Override
    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        return other.canCoordinateBeSpawn(p_76566_1_, p_76566_2_);
    }

    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        return other.calculateCelestialAngle(p_76563_1_, p_76563_3_);
    }

    @Override
    public int getMoonPhase(long p_76559_1_) {
        return other.getMoonPhase(p_76559_1_);
    }

    @Override
    public boolean isSurfaceWorld() {
        return other.isSurfaceWorld();
    }

    @Override
    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        return other.calcSunriseSunsetColors(p_76560_1_, p_76560_2_);
    }

    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        return other.getFogColor(p_76562_1_, p_76562_2_);
    }

    @Override
    public boolean canRespawnHere() {
        return other.canRespawnHere();
    }

    @Override
    public float getCloudHeight() {
        return other.getCloudHeight();
    }

    @Override
    public boolean isSkyColored() {
        return other.isSkyColored();
    }

    @Override
    public ChunkCoordinates getEntrancePortalLocation() {
        return other.getEntrancePortalLocation();
    }

    @Override
    public int getAverageGroundLevel() {
        return other.getAverageGroundLevel();
    }

    @Override
    public boolean getWorldHasVoidParticles() {
        return other.getWorldHasVoidParticles();
    }

    @Override
    public double getVoidFogYFactor() {
        return other.getVoidFogYFactor();
    }

    @Override
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        return other.doesXZShowFog(p_76568_1_, p_76568_2_);
    }

    @Override
    public String getDimensionName() {
        return other.getDimensionName();
    }

    @Override
    public void setDimension(int dim) {
        other.setDimension(dim);
        dimensionId = dim;
        if(isWorldProviderNull) {
            isWorldProviderNull = false;
            other = Ref.createWorldProviderProxy(dim);
            other.setDimension(dim);
        }
    }

    @Override
    public String getSaveFolder() {
        return other.getSaveFolder();
    }

    @Override
    public String getWelcomeMessage() {
        return other.getWelcomeMessage();
    }

    @Override
    public String getDepartMessage() {
        return other.getDepartMessage();
    }

    @Override
    public double getMovementFactor() {
        return other.getMovementFactor();
    }

    @Override
    public IRenderHandler getSkyRenderer() {
        return other.getSkyRenderer();
    }

    @Override
    public void setSkyRenderer(IRenderHandler skyRenderer) {
        other.setSkyRenderer(skyRenderer);
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        return other.getCloudRenderer();
    }

    @Override
    public void setCloudRenderer(IRenderHandler renderer) {
        other.setCloudRenderer(renderer);
    }

    @Override
    public IRenderHandler getWeatherRenderer() {
        return other.getWeatherRenderer();
    }

    @Override
    public void setWeatherRenderer(IRenderHandler renderer) {
        other.setWeatherRenderer(renderer);
    }

    @Override
    public ChunkCoordinates getRandomizedSpawnPoint() {
        return other.getRandomizedSpawnPoint();
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return other.shouldMapSpin(entity, x, y, z);
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return other.getRespawnDimension(player);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        return other.getBiomeGenForCoords(x, z);
    }

    @Override
    public boolean isDaytime() {
        return other.isDaytime();
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return other.getSunBrightnessFactor(par1);
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        return other.getCurrentMoonPhaseFactor();
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        return other.getSkyColor(cameraEntity, partialTicks);
    }

    @Override
    public Vec3 drawClouds(float partialTicks) {
        return other.drawClouds(partialTicks);
    }

    @Override
    public float getSunBrightness(float par1) {
        return other.getSunBrightness(par1);
    }

    @Override
    public float getStarBrightness(float par1) {
        return other.getStarBrightness(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        other.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    }

    @Override
    public void calculateInitialWeather() {
        other.calculateInitialWeather();
    }

    @Override
    public void updateWeather() {
        other.updateWeather();
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        return other.canBlockFreeze(x, y, z, byWater);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        return other.canSnowAt(x, y, z, checkLight);
    }

    @Override
    public void setWorldTime(long time) {
        other.setWorldTime(time);
    }

    @Override
    public long getSeed() {
        return other.getSeed();
    }

    @Override
    public long getWorldTime() {
        return other.getWorldTime();
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        return other.getSpawnPoint();
    }

    @Override
    public void setSpawnPoint(int x, int y, int z) {
        other.setSpawnPoint(x, y, z);
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, int x, int y, int z) {
        return other.canMineBlock(player, x, y, z);
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        return other.isBlockHighHumidity(x, y, z);
    }

    @Override
    public int getHeight() {
        return other.getHeight();
    }

    @Override
    public int getActualHeight() {
        return other.getActualHeight();
    }

    @Override
    public double getHorizon() {
        return other.getHorizon();
    }

    @Override
    public void resetRainAndThunder() {
        other.resetRainAndThunder();
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        return other.canDoLightning(chunk);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        return other.canDoRainSnowIce(chunk);
    }

    public static class LoadedTrue extends WorldProviderSkyGrid {}

    public static class LoadedFalse extends WorldProviderSkyGrid {}
}
