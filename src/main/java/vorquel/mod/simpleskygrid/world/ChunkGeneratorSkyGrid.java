package vorquel.mod.simpleskygrid.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.RandomList;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEndPortal;
import vorquel.mod.simpleskygrid.world.generated.GeneratedUnique;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.*;

import static net.minecraft.init.Blocks.BEDROCK;

public class ChunkGeneratorSkyGrid implements IChunkGenerator {
    
    public static WeakHashMap<ChunkGeneratorSkyGrid, Integer> providers = new WeakHashMap<>();
    
    private int dimensionId;
    private World world;
    private long seed;
    private Config.DimensionProperties dimensionProperties;
    private RandomList<IGeneratedObject> randomGenerator;
    private HashMap<BlockPos, IGeneratedObject> uniqueGenerations = new HashMap<>();
    private ArrayList<BlockPos> endPortalLocations = new ArrayList<>();
    
    public ChunkGeneratorSkyGrid(World world, long seed, int dimensionId) {
        providers.put(this, dimensionId);
        this.dimensionId = dimensionId;
        this.world = world;
        this.seed = seed;
        resetProperties();
        Random random = new Random(seed);
        for(GeneratedUnique unique : Ref.getUniqueGenerator(dimensionId)) {
            int count = unique.getCount(random);
            for(int i=0; i<count; ++i) {
                int j=0;
                for(; j<1000; ++j) {
                    BlockPos location = unique.getLocation(random);
                    if(!uniqueGenerations.containsKey(location)) {
                        uniqueGenerations.put(location, unique);
                        if(unique.getGeneratedObject() instanceof GeneratedEndPortal)
                            endPortalLocations.add(location);
                        break;
                    }
                }
                if(j == 1000)
                    Log.warn("Unable to place uniqueGen object");
            }
        }
    }
    
    public void resetProperties() {
        dimensionProperties = Config.dimensionPropertiesMap.get(dimensionId);
        randomGenerator = Ref.getRandomGenerator(dimensionId);
    }
    
    @Override
    public Chunk provideChunk(int xChunk, int zChunk) {
        Chunk chunk = new Chunk(world, xChunk, zChunk);
        if(dimensionProperties.isFinite() && dimensionProperties.notInRadius(xChunk, zChunk))
            return chunk;
        for(int i=0; i< dimensionProperties.height>>4; ++i)
            chunk.getBlockStorageArray()[i] = new ExtendedBlockStorage(i*16, !world.provider.getHasNoSky());
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[0];
        for(int x=0; x<16; x+=4)
            for(int z=0; z<16; z+=4)
                extendedblockstorage.set(x, 0, z, BEDROCK.getDefaultState());
        
        chunk.generateSkylightMap();
        BiomeGenBase[] abiomegenbase = this.world.getBiomeProvider().loadBlockGeneratorData(null, xChunk * 16, zChunk * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();
        
        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)BiomeGenBase.getIdForBiome(abiomegenbase[l]);
        }
        
        return chunk;
    }
    
    @Override
    public void populate(int xChunk, int zChunk) {
        if(dimensionProperties.isFinite() && dimensionProperties.notInRadius(xChunk, zChunk))
            return;
        
        Random random = new Random(seed+xChunk*1340661669L+zChunk*345978359L);
        
        BlockPos.MutableBlockPos here = new BlockPos.MutableBlockPos();
        for(int y=4; y<dimensionProperties.height; y+=4)
            for(int x=xChunk*16; x<xChunk*16+16; x+=4)
                for(int z=zChunk*16; z<zChunk*16+16; z+=4) {
                    here.set(x, y, z);
                    if(uniqueGenerations.containsKey(here))
                        uniqueGenerations.get(here).provideObject(random, world, here);
                    else
                        randomGenerator.getNext(random).provideObject(random, world, here);
                }
    }
    
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(pos);
        return biomeGenBase.getSpawnableList(type);
    }
    
    @Override
    public BlockPos getStrongholdGen(World world, String structure, BlockPos pos) {
        if(!"Stronghold".equals(structure))
            return null;
        double bestDistance = Double.POSITIVE_INFINITY;
        BlockPos bestLocation = new BlockPos(0, 0, 0);
        for(BlockPos location : endPortalLocations) {
            double distance = location.distanceSq(pos);
            if(distance < bestDistance) {
                bestDistance = distance;
                bestLocation = location;
            }
        }
        return bestLocation;
    }
    
    @Override
    public void recreateStructures(Chunk chunk, int xChunk, int zChunk) {}
}
