package vorquel.mod.simpleskygrid.world;

import static vorquel.mod.simpleskygrid.helper.BlockCache.*;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
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
import vorquel.mod.simpleskygrid.helper.NBTString;
import vorquel.mod.simpleskygrid.helper.Ref;

import java.util.List;
import java.util.Random;

public class ChunkProviderSkyGrid implements IChunkProvider {

    private World world;
    private long seed;
    private RandomBlockGenerator randomBlockGenerator;

    public ChunkProviderSkyGrid(World world, long seed, String name) {
        this.world = world;
        this.seed = seed;
        randomBlockGenerator = Ref.getGenerator(name);
    }

    @Override
    public boolean chunkExists(int xChunk, int zChunk) {
        return true;
    }

    @Override
    public Chunk provideChunk(int xChunk, int zChunk) {
        Chunk chunk = new Chunk(world, xChunk, zChunk);
        Random random = new Random();
        random.setSeed(seed+xChunk*1340661669L+zChunk*345978359L);

        ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(0, !world.provider.hasNoSky);
        chunk.getBlockStorageArray()[0] = extendedblockstorage;
        for(int x=0; x<16; x+=4)
            for(int z=0; z<16; z+=4)
                extendedblockstorage.func_150818_a(x, 0, z, bedrock);

        for(int y=4; y<128; y+=4) {
            int y4 = y >> 4;
            extendedblockstorage = chunk.getBlockStorageArray()[y4];

            if (extendedblockstorage == null)
            {
                extendedblockstorage = new ExtendedBlockStorage(y, !world.provider.hasNoSky);
                chunk.getBlockStorageArray()[y4] = extendedblockstorage;
            }

            for(int x=0; x<16; x+=4)
                for(int z=0; z<16; z+=4) {
                    RandomBlockGenerator.BlockComplex complex = randomBlockGenerator.getNextBlock(random);
                    extendedblockstorage.func_150818_a(x, y & 15, z, complex.block);
                    extendedblockstorage.setExtBlockMetadata(x, y & 15, z, complex.metadata);
                    if(complex.nbt != null) {
                        TileEntity tileEntity = complex.block.createTileEntity(world, complex.metadata);
                        NBTString.localizeNBT(complex.nbt, xChunk*16 + x, y, zChunk*16 + z);
                        tileEntity.readFromNBT(complex.nbt);
                        chunk.addTileEntity(tileEntity);
                    } else if(complex.block == chest) {
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
    public void populate(IChunkProvider p_73153_1_, int xChunk, int zChunk) {}

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
        return null;
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
