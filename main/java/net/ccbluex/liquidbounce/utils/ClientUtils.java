/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.utils;

import com.google.gson.JsonObject;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.server.S01PacketEncryptionRequest;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.awt.*;
import java.lang.reflect.Field;
import java.security.PublicKey;

@SideOnly(Side.CLIENT)
public final class ClientUtils extends MinecraftInstance {
    public static final ClientUtils INSTANCE = new ClientUtils();

    public static FMLEventChannel germ;

    private static final Logger logger = LogManager.getLogger("LiquidBounce");

    private static Field fastRenderField;

    static {
        try {
            fastRenderField = GameSettings.class.getDeclaredField("ofFastRender");

            if(!fastRenderField.isAccessible())
                fastRenderField.setAccessible(true);
        }catch(final NoSuchFieldException ignored) {
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void disableFastRender() {
        try {
            if(fastRenderField != null) {
                if(!fastRenderField.isAccessible())
                    fastRenderField.setAccessible(true);

                fastRenderField.setBoolean(mc.gameSettings, false);
            }
        }catch(final IllegalAccessException ignored) {
        }
    }

    public static void sendEncryption(final NetworkManager networkManager, final SecretKey secretKey, final PublicKey publicKey, final S01PacketEncryptionRequest encryptionRequest) {
        networkManager.sendPacket(new C01PacketEncryptionResponse(secretKey, publicKey, encryptionRequest.getVerifyToken()), p_operationComplete_1_ -> networkManager.enableEncryption(secretKey));
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void logError(String msg, Exception ex){
        logger.error(msg);
    }

    public static Vec3 getVectorForRotation(float p_getVectorForRotation_1_, float p_getVectorForRotation_2_) {
        float f = MathHelper.cos(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-p_getVectorForRotation_2_ * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-p_getVectorForRotation_1_ * 0.017453292F);
        float f3 = MathHelper.sin(-p_getVectorForRotation_1_ * 0.017453292F);
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public static void logInfo(final String msg) {
        logger.info(msg);
    }

    public static void logWarn(final String msg) {
        logger.warn(msg);
    }

    public static void displayChatMessage(final String message) {
        if (mc.thePlayer == null) {
            getLogger().info("(MCChat)" + message);
            return;
        }

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("text", message);

        mc.thePlayer.addChatMessage(IChatComponent.Serializer.jsonToComponent(jsonObject.toString()));
    }
}