package com.floweytf.tfcsupportindicator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.dries007.tfc.util.Support;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
    modid = "tfc_support_indicator",
    value = Dist.CLIENT
)
public final class SupportHighlightRenderer {

    @SubscribeEvent
    public static void onBlockHighlight(RenderHighlightEvent.Block event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        BlockPos supportPos = event.getTarget().getBlockPos();
        BlockState supportState =
            minecraft.level.getBlockState(supportPos);

        Support support = Support.get(supportState);

        if (support == null) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();

        VertexConsumer lines =
            event.getMultiBufferSource()
                .getBuffer(RenderType.lines());

        Vec3 camera = event.getCamera().getPosition();

        for (BlockPos pos : support.getSupportedArea(supportPos)) {

            if (minecraft.level.isEmptyBlock(pos)) {
                continue;
            }

            AABB box = new AABB(pos)
                .move(
                    -camera.x,
                    -camera.y,
                    -camera.z
                )
                .inflate(0.002D);

            LevelRenderer.renderLineBox(
                poseStack,
                lines,
                box,
                0.1F,
                1.0F,
                0.1F,
                0.65F
            );
        }
    }

    private SupportHighlightRenderer() {
    }
}
