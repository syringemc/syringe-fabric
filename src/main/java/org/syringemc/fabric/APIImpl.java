package org.syringemc.fabric;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.syringe.api.API;
import org.syringe.api.PacketID;
import org.syringe.api.SyringeAPI;
import org.syringe.api.util.Position;

import java.util.UUID;

public class APIImpl implements API {
    @Override
    public void displayText(UUID uuid, String key, String text, Position position, org.syringe.api.text.Style style, float offset, long fadein, boolean shadow) {
        LiteralText literal = new LiteralText(text);
        Style textStyle = Style.EMPTY
                .withColor(TextColor.fromRgb(style.getRgb()))
                .withBold(style.isBold())
                .withItalic(style.isItalic())
                .withUnderline(style.isUnderlined())
                .withFont(new Identifier(style.getFont()));
        if (style.isObfuscated()) {
            textStyle = textStyle.withFormatting(Formatting.OBFUSCATED);
        }
        literal.setStyle(textStyle);

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(key);
        buf.writeText(literal);
        buf.writeString(position.name());
        buf.writeFloat(offset);
        buf.writeLong(fadein);
        buf.writeBoolean(shadow);

        PlayerEntity player = SyringeFabric.SERVER.getPlayerManager().getPlayer(uuid);
        Identifier identifier = new Identifier(SyringeAPI.NAMESPACE, PacketID.DISPLAY_TEXT);

        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, identifier, buf);
    }
}
