package salt_models;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for the mod's custom creative mode tabs.
 */
public class CustomAnvilsTab
{
    /**
     * The DeferredRegister for creative mode tabs.
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            "salt_models"
        );
    
    /**
     * The custom creative mode tab for anvils.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<CreativeModeTab> CUSTOM_ANVILS_TAB = 
        CREATIVE_MODE_TABS.register("custom_anvils_tab",
            () -> CreativeModeTab.builder()
                .icon(
                    () -> new ItemStack(
                        CustomAnvils.GIANT_FORGE_ITEM.get()
                    )
                )
                .title(Component.translatable("itemGroup.custom_anvils_tab"))
                .displayItems((pParameters, pOutput) -> 
                {
                    pOutput.accept(CustomAnvils.GIANT_FORGE_ITEM.get());
                    pOutput.accept(CustomAnvils.JUNGLE_FORGE_ITEM.get());
                    pOutput.accept(CustomAnvils.FROST_FORGE_ITEM.get());
                    pOutput.accept(CustomAnvils.GOLDEN_FORGE_ITEM.get());
                })
                .build()
        );

    /**
     * Registers the creative mode tabs with the event bus.
     *
     * @param eventBus The mod event bus.
     */
    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
