package salt_models;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main mod class for Salt Models.
 */
@Mod("salt_models")
public class SaltModels
{
    /**
     * Initializes the mod, setting up event buses and registries.
     */
    public SaltModels()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CustomAnvils.register(modEventBus);
        CustomAnvilsTab.register(modEventBus);
    }
}
