package vorquel.mod.simpleskygrid.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class MessageHandlerClipboard implements IMessageHandler<MessageClipboard, IMessage> {
    @Override
    public IMessage onMessage(MessageClipboard message, MessageContext ctx) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(message.message), null);
        return null;
    }
}
