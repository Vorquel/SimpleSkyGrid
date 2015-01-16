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
import vorquel.mod.simpleskygrid.SimpleSkyGrid;

public class WorldProviderNull extends WorldProvider {

    boolean hasWarnedGet = false;
    boolean hasWarnedSet = false;

    @Override
    public String getDimensionName() {
        warnGet();
        return "null";
    }

    public WorldProviderNull() {
        super();
    }

    @Override
    public IChunkProvider createChunkGenerator() {
        warnGet();
        return super.createChunkGenerator();
    }

    @Override
    public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
        warnGet();
        return super.canCoordinateBeSpawn(p_76566_1_, p_76566_2_);
    }

    @Override
    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
        warnGet();
        return super.calculateCelestialAngle(p_76563_1_, p_76563_3_);
    }

    @Override
    public int getMoonPhase(long p_76559_1_) {
        warnGet();
        return super.getMoonPhase(p_76559_1_);
    }

    @Override
    public boolean isSurfaceWorld() {
        warnGet();
        return super.isSurfaceWorld();
    }

    @Override
    public float[] calcSunriseSunsetColors(float p_76560_1_, float p_76560_2_) {
        warnGet();
        return super.calcSunriseSunsetColors(p_76560_1_, p_76560_2_);
    }

    @Override
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
        warnGet();
        return super.getFogColor(p_76562_1_, p_76562_2_);
    }

    @Override
    public boolean canRespawnHere() {
        warnGet();
        return super.canRespawnHere();
    }

    @Override
    public float getCloudHeight() {
        warnGet();
        return super.getCloudHeight();
    }

    @Override
    public boolean isSkyColored() {
        warnGet();
        return super.isSkyColored();
    }

    @Override
    public ChunkCoordinates getEntrancePortalLocation() {
        warnGet();
        return super.getEntrancePortalLocation();
    }

    @Override
    public int getAverageGroundLevel() {
        warnGet();
        return super.getAverageGroundLevel();
    }

    @Override
    public boolean getWorldHasVoidParticles() {
        warnGet();
        return super.getWorldHasVoidParticles();
    }

    @Override
    public double getVoidFogYFactor() {
        warnGet();
        return super.getVoidFogYFactor();
    }

    @Override
    public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
        warnGet();
        return super.doesXZShowFog(p_76568_1_, p_76568_2_);
    }

    @Override
    public void setDimension(int dim) {
        super.setDimension(dim);
    }

    @Override
    public String getSaveFolder() {
        warnGet();
        return super.getSaveFolder();
    }

    @Override
    public String getWelcomeMessage() {
        warnGet();
        return super.getWelcomeMessage();
    }

    @Override
    public String getDepartMessage() {
        warnGet();
        return super.getDepartMessage();
    }

    @Override
    public double getMovementFactor() {
        warnGet();
        return super.getMovementFactor();
    }

    @Override
    public IRenderHandler getSkyRenderer() {
        warnGet();
        return super.getSkyRenderer();
    }

    @Override
    public void setSkyRenderer(IRenderHandler skyRenderer) {
        warnSet();
        super.setSkyRenderer(skyRenderer);
    }

    @Override
    public IRenderHandler getCloudRenderer() {
        warnGet();
        return super.getCloudRenderer();
    }

    @Override
    public void setCloudRenderer(IRenderHandler renderer) {
        warnSet();
        super.setCloudRenderer(renderer);
    }

    @Override
    public IRenderHandler getWeatherRenderer() {
        return super.getWeatherRenderer();
    }

    @Override
    public void setWeatherRenderer(IRenderHandler renderer) {
        warnSet();
        super.setWeatherRenderer(renderer);
    }

    @Override
    public ChunkCoordinates getRandomizedSpawnPoint() {
        warnGet();
        return super.getRandomizedSpawnPoint();
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        warnGet();
        return super.shouldMapSpin(entity, x, y, z);
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        warnGet();
        return super.getRespawnDimension(player);
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int z) {
        warnGet();
        return super.getBiomeGenForCoords(x, z);
    }

    @Override
    public boolean isDaytime() {
        warnGet();
        return super.isDaytime();
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        warnGet();
        return super.getSunBrightnessFactor(par1);
    }

    @Override
    public float getCurrentMoonPhaseFactor() {
        warnGet();
        return super.getCurrentMoonPhaseFactor();
    }

    @Override
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        warnGet();
        return super.getSkyColor(cameraEntity, partialTicks);
    }

    @Override
    public Vec3 drawClouds(float partialTicks) {
        warnGet();
        return super.drawClouds(partialTicks);
    }

    @Override
    public float getSunBrightness(float par1) {
        warnGet();
        return super.getSunBrightness(par1);
    }

    @Override
    public float getStarBrightness(float par1) {
        warnGet();
        return super.getStarBrightness(par1);
    }

    @Override
    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        warnSet();
        super.setAllowedSpawnTypes(allowHostile, allowPeaceful);
    }

    @Override
    public void calculateInitialWeather() {
        warnSet();
        super.calculateInitialWeather();
    }

    @Override
    public void updateWeather() {
        warnSet();
        super.updateWeather();
    }

    @Override
    public boolean canBlockFreeze(int x, int y, int z, boolean byWater) {
        warnGet();
        return super.canBlockFreeze(x, y, z, byWater);
    }

    @Override
    public boolean canSnowAt(int x, int y, int z, boolean checkLight) {
        warnGet();
        return super.canSnowAt(x, y, z, checkLight);
    }

    @Override
    public void setWorldTime(long time) {
        warnSet();
        super.setWorldTime(time);
    }

    @Override
    public long getSeed() {
        warnGet();
        return super.getSeed();
    }

    @Override
    public long getWorldTime() {
        warnGet();
        return super.getWorldTime();
    }

    @Override
    public ChunkCoordinates getSpawnPoint() {
        warnGet();
        return super.getSpawnPoint();
    }

    @Override
    public void setSpawnPoint(int x, int y, int z) {
        warnSet();
        super.setSpawnPoint(x, y, z);
    }

    @Override
    public boolean canMineBlock(EntityPlayer player, int x, int y, int z) {
        warnGet();
        return super.canMineBlock(player, x, y, z);
    }

    @Override
    public boolean isBlockHighHumidity(int x, int y, int z) {
        warnGet();
        return super.isBlockHighHumidity(x, y, z);
    }

    @Override
    public int getHeight() {
        warnGet();
        return super.getHeight();
    }

    @Override
    public int getActualHeight() {
        warnGet();
        return super.getActualHeight();
    }

    @Override
    public double getHorizon() {
        warnGet();
        return super.getHorizon();
    }

    @Override
    public void resetRainAndThunder() {
        warnSet();
        super.resetRainAndThunder();
    }

    @Override
    public boolean canDoLightning(Chunk chunk) {
        warnGet();
        return super.canDoLightning(chunk);
    }

    @Override
    public boolean canDoRainSnowIce(Chunk chunk) {
        warnGet();
        return super.canDoRainSnowIce(chunk);
    }

    private void warnGet() {
        if(hasWarnedGet)
            return;
        hasWarnedGet = true;
        SimpleSkyGrid.logger.warn("WorldProvider proxy has not been set.");
    }

    private void warnSet() {
        if(hasWarnedSet)
            return;
        hasWarnedGet = true;
        SimpleSkyGrid.logger.warn("*************************************");
        SimpleSkyGrid.logger.warn("*************************************");
        SimpleSkyGrid.logger.warn("*************************************");
        SimpleSkyGrid.logger.warn("WorldProviderNull is not good enough.");
        SimpleSkyGrid.logger.warn("*************************************");
        SimpleSkyGrid.logger.warn("*************************************");
        SimpleSkyGrid.logger.warn("*************************************");
    }
}
