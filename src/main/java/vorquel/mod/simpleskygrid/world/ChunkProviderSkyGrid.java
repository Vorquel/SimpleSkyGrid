package vorquel.mod.simpleskygrid.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.SimpleSkyGrid;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.helper.RandomList;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedEndPortal;
import vorquel.mod.simpleskygrid.world.igenerated.GeneratedUnique;
import vorquel.mod.simpleskygrid.world.igenerated.IGeneratedObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static net.minecraft.init.Blocks.bedrock;

public class ChunkProviderSkyGrid implements IChunkProvider {

    private World world;
    private long seed;
    private Config.DimensionProperties dimensionProperties;
    private RandomList<IGeneratedObject> randomGenerator;
    private HashMap<ChunkCoordinates, IGeneratedObject> uniqueGenerations = new HashMap<>();
    private ArrayList<ChunkCoordinates> endPortalLocations = new ArrayList<>();

    public ChunkProviderSkyGrid(World world, long seed, int dimensionId) {
        this.world = world;
        this.seed = seed;
        dimensionProperties = Config.dimensionPropertiesMap.get(dimensionId);
        randomGenerator = Ref.getRandomGenerator(dimensionId);
        Random random = new Random(seed);
        for(GeneratedUnique unique : Ref.getUniqueGenerator(dimensionId)) {
            int count = unique.getCount(random);
            for(int i=0; i<count; ++i) {
                int j=0;
                for(; j<1000; ++j) {
                    ChunkCoordinates location = unique.getLocation(random);
                    if(!uniqueGenerations.containsKey(location)) {
                        uniqueGenerations.put(location, unique);
                        if(unique.getGeneratedObject() instanceof GeneratedEndPortal)
                            endPortalLocations.add(location);
                        break;
                    }
                }
                if(j == 1000)
                    SimpleSkyGrid.logger.warn("Unable to place uniqueGen object");
            }
        }
    }

    @Override
    public boolean chunkExists(int xChunk, int zChunk) {
        return true;
    }

    @Override
    public Chunk provideChunk(int xChunk, int zChunk) {
        Chunk chunk = new Chunk(world, xChunk, zChunk);
        if(dimensionProperties.isFinite() && !dimensionProperties.inRadius(xChunk, zChunk))
            return chunk;
        for(int i=0; i< dimensionProperties.height>>4; ++i)
            chunk.getBlockStorageArray()[i] = new ExtendedBlockStorage(i*16, !world.provider.hasNoSky);
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[0];
        for(int x=0; x<16; x+=4)
            for(int z=0; z<16; z+=4)
                extendedblockstorage.func_150818_a(x, 0, z, bedrock);

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
        if(dimensionProperties.isFinite() && !dimensionProperties.inRadius(xChunk, zChunk))
            return;

        Random random = new Random(seed+xChunk*1340661669L+zChunk*345978359L);

        ChunkCoordinates here = new ChunkCoordinates();
        for(int y=4; y<dimensionProperties.height; y+=4)
            for(int x=xChunk*16; x<xChunk*16+16; x+=4)
                for(int z=zChunk*16; z<zChunk*16+16; z+=4) {
                    here.set(x, y, z);
                    if(uniqueGenerations.containsKey(here))
                        uniqueGenerations.get(here).provideObject(random, world, x, y, z);
                    else
                        randomGenerator.getNext(random).provideObject(random, world, x, y, z);
                }
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
        if(!structure.equals("Stronghold"))
            return null;
        double bestDistance = Double.POSITIVE_INFINITY;
        ChunkCoordinates bestLocation = new ChunkCoordinates();
        for(ChunkCoordinates location : endPortalLocations) {
            double distance = location.getDistanceSquared(x, y, z);
            if(distance < bestDistance) {
                bestDistance = distance;
                bestLocation = location;
            }
        }
        return new ChunkPosition(bestLocation.posX, bestLocation.posY, bestLocation.posZ);
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
