package vorquel.mod.simpleskygrid.world;

import static net.minecraft.init.Blocks.*;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.ChestGenHooks;
import vorquel.mod.simpleskygrid.helper.Config;
import vorquel.mod.simpleskygrid.helper.Ref;

import java.util.List;
import java.util.Random;

public class ChunkProviderSkyGrid implements IChunkProvider {

    private World world;
    private long seed;
    private int dimensionID;
    private RandomIGeneratedObject randomIGeneratedObject;
    private Config.WorldSettings worldSettings;
    private ChunkPosition endPortalLocation;

    public ChunkProviderSkyGrid(World world, long seed, int dimensionId) {
        this.world = world;
        this.seed = seed;
        this.dimensionID = dimensionId;
        randomIGeneratedObject = Ref.getGenerator(dimensionId);
        worldSettings = Config.getSettings(dimensionId);
        if(dimensionId == 0) {
            Random random = new Random(seed);
            double angle = random.nextDouble()*Math.PI*2;
            int x = 8 + 16*(int)(62.5*Math.cos(angle));
            int y = 4;
            int z = 8 + 16*(int)(62.5*Math.sin(angle));
            endPortalLocation = new ChunkPosition(x, y, z);
        }
    }

    @Override
    public boolean chunkExists(int xChunk, int zChunk) {
        return true;
    }

    @Override
    public Chunk provideChunk(int xChunk, int zChunk) {
        Chunk chunk = new Chunk(world, xChunk, zChunk);
        if(worldSettings.isFinite() && !worldSettings.inRadius(xChunk, zChunk))
            return chunk;
        Random random = new Random(seed+xChunk*1340661669L+zChunk*345978359L);

        for(int i=0; i<worldSettings.height>>4; ++i)
            chunk.getBlockStorageArray()[i] = new ExtendedBlockStorage(i*16, !world.provider.hasNoSky);
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[0];
        for(int x=0; x<16; x+=4)
            for(int z=0; z<16; z+=4)
                extendedblockstorage.func_150818_a(x, 0, z, bedrock);

        for(int y=4; y<worldSettings.height; y+=4) {
            for(int x=0; x<16; x+=4)
                for(int z=0; z<16; z+=4) {
                    randomIGeneratedObject.getNext(random).provideObject(world, chunk, x, y, z);
                    if(chunk.getBlock(x, y, z) == chest && chunk.getTileEntityUnsafe(x, y, z) == null) {
                        TileEntityChest te = new TileEntityChest();
                        te.xCoord = xChunk*16 + x;
                        te.yCoord = y;
                        te.zCoord = zChunk*16 + z;
                        WeightedRandomChestContent.generateChestContents(random, ChestGenHooks.getItems(ChestGenHooks.DUNGEON_CHEST,random),te,ChestGenHooks.getCount(ChestGenHooks.DUNGEON_CHEST,random));
                        chunk.addTileEntity(te);
                    }
                }
        }

        chunk.generateSkylightMap();
        BiomeGenBase[] biomeGenBase = world.getWorldChunkManager().loadBlockGeneratorData(null, xChunk * 16, zChunk * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)biomeGenBase[l].biomeID;
        }

        return chunk;
    }

    @Override
    public Chunk loadChunk(int xChunk, int zChunk) {
        return provideChunk(xChunk, zChunk);
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int xChunk, int zChunk) {
        switch(dimensionID) {
            case 0:
                populateEndPortal(xChunk, zChunk);
                break;
            case 1:
                populateEnderCrystals(xChunk, zChunk);
                populateEnderDragon(xChunk, zChunk);
                break;
        }
    }

    private void populateEndPortal(int xChunk, int zChunk) {
        if(endPortalLocation == null)
            return;
        if(xChunk != endPortalLocation.chunkPosX/16 || zChunk != endPortalLocation.chunkPosZ/16)
            return;
        int x = endPortalLocation.chunkPosX;
        int y = endPortalLocation.chunkPosY;
        int z = endPortalLocation.chunkPosZ;
        world.setBlockToAir(x, y, z);
        world.setBlock(x-1, y, z-2, end_portal_frame, 0, 3);
        world.setBlock(x  , y, z-2, end_portal_frame, 0, 3);
        world.setBlock(x+1, y, z-2, end_portal_frame, 0, 3);
        world.setBlock(x+2, y, z-1, end_portal_frame, 1, 3);
        world.setBlock(x+2, y, z  , end_portal_frame, 1, 3);
        world.setBlock(x+2, y, z+1, end_portal_frame, 1, 3);
        world.setBlock(x+1, y, z+2, end_portal_frame, 2, 3);
        world.setBlock(x  , y, z+2, end_portal_frame, 2, 3);
        world.setBlock(x-1, y, z+2, end_portal_frame, 2, 3);
        world.setBlock(x-2, y, z+1, end_portal_frame, 3, 3);
        world.setBlock(x-2, y, z  , end_portal_frame, 3, 3);
        world.setBlock(x-2, y, z-1, end_portal_frame, 3, 3);
    }

    private void populateEnderCrystals(int xChunk, int zChunk) {
        if(xChunk > 6 || xChunk < -5 || zChunk > 6 || zChunk < -5)
            return;
        Random random = new Random(seed+xChunk*1340661669L+zChunk*345978359L);
        int meanCrystals = 25;
        float threshold = meanCrystals / (meanCrystals + 144f);
        int y = worldSettings.height - 1 - (worldSettings.height - 1) % 4;
        while(random.nextFloat() < threshold) {
            int x = random.nextInt(4) * 4 + xChunk * 16;
            int z = random.nextInt(4) * 4 + zChunk * 16;
            world.setBlock(x, y, z, bedrock);
            EntityEnderCrystal enderCrystal = new EntityEnderCrystal(world);
            enderCrystal.setPosition(x + .5, y + 1, z + .5);
            world.spawnEntityInWorld(enderCrystal);
        }
    }

    private void populateEnderDragon(int xChunk, int zChunk) {
        if(xChunk != 0 || zChunk != 0)
            return;
        EntityDragon dragon = new EntityDragon(world);
        Random random = new Random();
        dragon.setLocationAndAngles(0, 128, 0, random.nextFloat()*360, 0);
        world.spawnEntityInWorld(dragon);
    }

    @Override
    public boolean saveChunks(boolean bool, IProgressUpdate progressUpdate) {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String makeString() {
        return "SkyGridLevelSource";
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType type, int x, int y, int z) {
        BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(x, z);
        return biomeGenBase.getSpawnableList(type);
    }

    @Override
    public ChunkPosition func_147416_a(World world, String structure, int x, int y, int z) {
        if(!structure.equals("Stronghold") || endPortalLocation == null)
            return null;
        return endPortalLocation;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(int xChunk, int zChunk) {}

    @Override
    public void saveExtraData() {}
}
