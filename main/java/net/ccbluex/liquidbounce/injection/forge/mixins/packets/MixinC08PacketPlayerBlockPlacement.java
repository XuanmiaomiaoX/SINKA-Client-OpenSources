package net.ccbluex.liquidbounce.injection.forge.mixins.packets;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.misc.HytPacketFix;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(C08PacketPlayerBlockPlacement.class)
public abstract class MixinC08PacketPlayerBlockPlacement {
    @Shadow private BlockPos position;

    @Shadow private int placedBlockDirection;

    @Shadow public ItemStack stack;

    @Shadow public float facingX;

    @Shadow public float facingY;

    @Shadow public float facingZ;

    //发包修复已经绕不过了 不用看了 和谐了 NewFix都不行 OldFix还能在天坑和空岛用一用

    /**
     * @author
     */
    @Overwrite()
    public void readPacketData(PacketBuffer p_readPacketData_1_)throws IOException{
        this.position = p_readPacketData_1_.readBlockPos();
        this.placedBlockDirection = p_readPacketData_1_.readUnsignedByte();
        this.stack = p_readPacketData_1_.readItemStackFromBuffer();
        if (LiquidBounce.moduleManager.getModule(HytPacketFix.class).getState()) {
            if(new HytPacketFix().getPacketmode().get().equals("OldFix")) {
                this.facingX = (float) p_readPacketData_1_.readUnsignedByte() / 1.0F;
                this.facingY = (float) p_readPacketData_1_.readUnsignedByte() / 1.0F;
                this.facingZ = (float) p_readPacketData_1_.readUnsignedByte() / 1.0F;
            }else{
                this.facingX = p_readPacketData_1_.readFloat() / 15.0F;
                this.facingY = p_readPacketData_1_.readFloat() / 15.0F;
                this.facingZ = p_readPacketData_1_.readFloat() / 15.0F;
            }
        }else{
            this.facingX = (float) p_readPacketData_1_.readUnsignedByte() / 16.0F;
            this.facingY = (float) p_readPacketData_1_.readUnsignedByte() / 16.0F;
            this.facingZ = (float) p_readPacketData_1_.readUnsignedByte() / 16.0F;
        }
    }
    /**
     * @author
     */
    @Overwrite()
    public void writePacketData(PacketBuffer p_writePacketData_1_) throws IOException{
        p_writePacketData_1_.writeBlockPos(this.position);
        p_writePacketData_1_.writeByte(this.placedBlockDirection);
        p_writePacketData_1_.writeItemStackToBuffer(this.stack);
        if (LiquidBounce.moduleManager.getModule(HytPacketFix.class).getState()) {
            if(new HytPacketFix().getPacketmode().get().equals("OldFix")) {
                p_writePacketData_1_.writeByte((int) (this.facingX * 1.0F));
                p_writePacketData_1_.writeByte((int) (this.facingY * 1.0F));
                p_writePacketData_1_.writeByte((int) (this.facingZ * 1.0F));
            }else{
                p_writePacketData_1_.writeByte((int) this.facingX);
                p_writePacketData_1_.writeByte((int) this.facingY);
                p_writePacketData_1_.writeByte((int) this.facingZ);
            }
        }else{
            p_writePacketData_1_.writeByte((int) (this.facingX * 16.0F));
            p_writePacketData_1_.writeByte((int) (this.facingY * 16.0F));
            p_writePacketData_1_.writeByte((int) (this.facingZ * 16.0F));
        }
    }
}