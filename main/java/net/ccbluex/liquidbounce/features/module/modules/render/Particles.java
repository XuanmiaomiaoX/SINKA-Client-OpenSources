/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.particles.EvictingList;
import net.ccbluex.liquidbounce.utils.particles.Particle;
import net.ccbluex.liquidbounce.utils.particles.Vec3;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.ParticleTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.entity.EntityLivingBase;

import java.util.List;
import java.util.Objects;

@ModuleInfo(name = "Particles", description = "Combat Particles.", category = ModuleCategory.RENDER)
public final class Particles extends Module {

    final IntegerValue amount = new IntegerValue("Amount", 5, 1, 20);

    final BoolValue physics = new BoolValue("Physics", true);

    final List<Particle> particles = new EvictingList<>(100);
    final ParticleTimer timer = new ParticleTimer();
    public boolean omg = false;
    public EntityLivingBase target;


    @EventTarget
    public void onAttack(final AttackEvent event) {
        if (event.getTargetEntity() instanceof EntityLivingBase)
            target = (EntityLivingBase) event.getTargetEntity();
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if (omg) {
            for (int i = 0; i < amount.get(); i++)
                particles.add(new Particle(new Vec3(((KillAura) LiquidBounce.INSTANCE.moduleManager.get(KillAura.class)).getTarget().posX + (Math.random() - 0.5) * 0.5, ((KillAura) LiquidBounce.INSTANCE.moduleManager.get(KillAura.class)).getTarget().posY + Math.random() * 1 + 0.5, ((KillAura) LiquidBounce.INSTANCE.moduleManager.get(KillAura.class)).getTarget().posZ + (Math.random() - 0.5) * 0.5)));

            omg=false;
        }
        if (target != null && target.hurtTime >= 9) {
            for (int i = 0; i < amount.get(); i++)
                particles.add(new Particle(new Vec3(target.posX + (Math.random() - 0.5) * 0.5, target.posY + Math.random() * 1 + 0.5, target.posZ + (Math.random() - 0.5) * 0.5)));

            target = null;
        }
    }

    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (particles.isEmpty())
            return;

        for (int i = 0; i <= timer.getElapsedTime() / 1E+11; i++) {
            if (physics.get())
                particles.forEach(Particle::update);
            else
                particles.forEach(Particle::updateWithoutPhysics);
        }

        particles.removeIf(particle -> mc.thePlayer.getDistanceSq(particle.position.xCoord, particle.position.yCoord, particle.position.zCoord) > 50 * 10);

        timer.reset();

        RenderUtils.renderParticles(particles);
    }
}