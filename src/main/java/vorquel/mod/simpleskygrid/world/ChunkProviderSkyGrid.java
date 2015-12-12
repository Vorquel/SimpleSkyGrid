package vorquel.mod.simpleskygrid.world;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import vorquel.mod.simpleskygrid.config.Config;
import vorquel.mod.simpleskygrid.helper.Log;
import vorquel.mod.simpleskygrid.helper.RandomList;
import vorquel.mod.simpleskygrid.helper.Ref;
import vorquel.mod.simpleskygrid.world.generated.GeneratedEndPortal;
import vorquel.mod.simpleskygrid.world.generated.GeneratedUnique;
import vorquel.mod.simpleskygrid.world.generated.IGeneratedObject;

import java.util.*;

import static net.minecraft.init.Blocks.bedrock;

public class ChunkProviderSkyGrid implements IChunkProvider {

    public static WeakHashMap<ChunkProviderSkyGrid, Integer> providers = new WeakHashMap<>();

    private int dimensionId;
    private World world;
    private long seed;
    private Config.DimensionProperties dimensionProperties;
    private RandomList<IGeneratedObject> randomGenerator;
    private HashMap<BlockPos, IGeneratedObject> uniqueGenerations = new HashMap<>();
    private ArrayList<BlockPos> endPortalLocations = new ArrayList<>();

    public ChunkProviderSkyGrid(World world, long seed, int dimensionId) {
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
    public boolean chunkExists(int xChunk, int zChunk) {
        return true;
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
                extendedblockstorage.set(x, 0, z, bedrock.getDefaultState());

        chunk.generateSkylightMap();
        BiomeGenBase[] biomeGenBase = world.getWorldChunkManager().loadBlockGeneratorData(null, xChunk * 16, zChunk * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();

        for (int l = 0; l < abyte.length; ++l)
        {
            abyte[l] = (byte)biomeGenBase[l].biomeID;
        }

        return chunk;
    }

    public Chunk loadChunk(int xChunk, int zChunk) {
        return provideChunk(xChunk, zChunk);
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int xChunk, int zChunk) {
        if(dimensionProperties.isFinite() && dimensionProperties.notInRadius(xChunk, zChunk))
            return;

        Random random = new Random(seed+xChunk*1340661669L+zChunk*345978359L);

        BlockPos here ;
        for(int y=4; y<dimensionProperties.height; y+=4)
            for(int x=xChunk*16; x<xChunk*16+16; x+=4)
                for(int z=zChunk*16; z<zChunk*16+16; z+=4) {
                    here = new BlockPos(x, y, z);
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
    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(int xChunk, int zChunk) 
    {}

    @Override
    public void saveExtraData() {}

	@Override
	public Chunk provideChunk(BlockPos blockPosIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(pos);
        return biomeGenBase.getSpawnableList(creatureType);
    }

	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        if(!structureName.equals("Stronghold"))
            return null;
        double bestDistance = Double.POSITIVE_INFINITY;
        BlockPos bestLocation = null;
        for(BlockPos location : endPortalLocations) {
            double distance = location.distanceSq(position);
            if(distance < bestDistance) {
                bestDistance = distance;
                bestLocation = location;
            }
        }
        return bestLocation;
	}
    
	@Override
	public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
		// TODO Auto-generated method stub
		
	}
}
