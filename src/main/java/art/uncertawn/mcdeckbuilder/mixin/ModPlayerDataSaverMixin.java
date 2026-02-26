package art.uncertawn.mcdeckbuilder.mixin;

import art.uncertawn.mcdeckbuilder.Mcdeckbuilder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ModPlayerDataSaverMixin {

    @Unique
    private NbtCompound modPlayerData = new NbtCompound();

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    protected void injectWriteMethod(WriteView view, CallbackInfo info) {
        view.putString(Mcdeckbuilder.MODID+".test", "Testing");

    }

}
