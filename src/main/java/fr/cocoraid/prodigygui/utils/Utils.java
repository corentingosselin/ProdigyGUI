package fr.cocoraid.prodigygui.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.cocoraid.prodigygui.nms.wrapper.packet.WrapperPlayServerChat;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class Utils {

    public static void sendActionMessage(Player p, String msg) {
        WrappedChatComponent chatComponent = WrappedChatComponent.fromText(msg);
        if(VersionChecker.isLowerOrEqualThan(VersionChecker.v1_11_R1)) {
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            PacketContainer chatPacket = protocolManager.createPacket(PacketType.Play.Server.CHAT);
            chatPacket.getChatComponents().write(0, chatComponent);
            chatPacket.getBytes().write(0, (byte) 2);
            try {
                protocolManager.sendServerPacket(p, chatPacket);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            WrapperPlayServerChat chatPacket = new WrapperPlayServerChat();
            chatPacket.setMessage(chatComponent);
            chatPacket.setChatType(EnumWrappers.ChatType.GAME_INFO);
            chatPacket.sendPacket(p);
        }

    }

}
