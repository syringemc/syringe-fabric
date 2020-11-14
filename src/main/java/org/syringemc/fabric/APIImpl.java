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
import org.syringemc.fabric.mixin.StyleAccessor;

import java.util.UUID;

public class APIImpl implements API {
    @Override
    public void displayText(UUID uuid, String key, String text, Position position, org.syringe.api.text.Style style, float offsetX, float offsetY, float scale, long fadein, boolean shadow) {
        LiteralText literal = new LiteralText(text);
        Style textStyle = Style.EMPTY
                .withColor(TextColor.fromRgb(style.getRgb()))
                .withBold(style.isBold())
                .withItalic(style.isItalic());
        if (style.isUnderlined()) {
            textStyle = textStyle.withFormatting(Formatting.UNDERLINE);
        }
        if (style.isObfuscated()) {
            textStyle = textStyle.withFormatting(Formatting.OBFUSCATED);
        }
        ((StyleAccessor) textStyle).setFont(new Identifier(style.getFont()));
        literal.setStyle(textStyle);

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(key);
        buf.writeText(literal);
        buf.writeString(position.name());
        buf.writeFloat(offsetX);
        buf.writeFloat(offsetY);
        buf.writeFloat(scale);
        buf.writeLong(fadein);
        buf.writeBoolean(shadow);

        PlayerEntity player = SyringeFabric.SERVER.getPlayerManager().getPlayer(uuid);
        Identifier identifier = new Identifier(SyringeAPI.NAMESPACE, PacketID.DISPLAY_TEXT);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, identifier, buf);
    }
}
