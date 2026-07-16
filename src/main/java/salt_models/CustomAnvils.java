package salt_models;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registry class for all custom blocks and items in the mod.
 */
public class CustomAnvils
{
    /**
     * The DeferredRegister for blocks.
     */
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(ForgeRegistries.BLOCKS, "salt_models");

    /**
     * The DeferredRegister for items.
     */
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, "salt_models");

    /**
     * The anvil part block registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Block> ANVIL_PART = 
        BLOCKS.register("anvil_part", () -> 
            new GiantAnvilPartBlock(
                BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .sound(SoundType.ANVIL)
                    .noLootTable()
            )
        );

    /**
     * The giant forge block registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Block> GIANT_FORGE = 
        BLOCKS.register("giant_forge", () -> 
        {
            BlockBehaviour.Properties properties = 
                BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops();
            return new GiantAnvilBlock(properties);
        });

    /**
     * The giant forge item registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Item> GIANT_FORGE_ITEM =
        ITEMS.register("giant_forge", () -> 
            new BlockItem(
                GIANT_FORGE.get(),
                new Item.Properties()
            )
        );

    /**
     * The jungle forge block registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Block> JUNGLE_FORGE = 
        BLOCKS.register("jungle_forge", () -> 
        {
            BlockBehaviour.Properties properties = 
                BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops();
            return new GiantAnvilBlock(properties);
        });

    /**
     * The jungle forge item registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Item> JUNGLE_FORGE_ITEM =
        ITEMS.register("jungle_forge", () -> 
            new BlockItem(
                JUNGLE_FORGE.get(),
                new Item.Properties()
            )
        );

    /**
     * The frost forge block registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Block> FROST_FORGE = 
        BLOCKS.register("frost_forge", () -> 
        {
            BlockBehaviour.Properties properties = 
                BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops();
            return new GiantAnvilBlock(properties);
        });

    /**
     * The frost forge item registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Item> FROST_FORGE_ITEM =
        ITEMS.register("frost_forge", () -> 
            new BlockItem(
                FROST_FORGE.get(),
                new Item.Properties()
            )
        );

    /**
     * The golden forge block registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Block> GOLDEN_FORGE = 
        BLOCKS.register("golden_forge", () -> 
        {
            BlockBehaviour.Properties properties = 
                BlockBehaviour.Properties.copy(Blocks.ANVIL)
                    .sound(SoundType.ANVIL)
                    .requiresCorrectToolForDrops();
            return new GiantAnvilBlock(properties);
        });

    /**
     * The golden forge item registry object.
     */
    @SuppressWarnings("null")
    public static final RegistryObject<Item> GOLDEN_FORGE_ITEM =
        ITEMS.register("golden_forge", () -> 
            new BlockItem(
                GOLDEN_FORGE.get(),
                new Item.Properties()
            )
        );

    /**
     * Registers all blocks and items with the event bus.
     *
     * @param eventBus The mod event bus.
     */
    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
