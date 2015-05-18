package vorquel.mod.simpleskygrid.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MessageIdentify implements IMessage {

    public int x, y, z, damage;

    public MessageIdentify(int x, int y, int z, int damage) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.damage = damage;
    }

    @SuppressWarnings("unused")
    public MessageIdentify() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        damage = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(damage);
    }
}
