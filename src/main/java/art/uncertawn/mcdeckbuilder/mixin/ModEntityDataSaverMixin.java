package art.uncertawn.mcdeckbuilder.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.storage.WriteView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
//public abstract class ModEntityDataSaverMixin implements IEntityDataSaver {
public abstract class ModEntityDataSaverMixin {
    private NbtCompound persistentData;

//    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null)
            this.persistentData = new NbtCompound();
        return persistentData;
    }

//    @Inject(method = "writeData", at = @At("HEAD"))
//    protected void injectWriteMethod(WriteView view, CallbackInfo info) {
//
//    }
}
