package salt_models;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Represents a part of a multi-block giant anvil.
 */
public class GiantAnvilPartBlock extends Block
{
    /**
     * The X offset relative to the master block.
     */
    public static final IntegerProperty X_OFFSET = 
        IntegerProperty.create("x_offset", 0, 2);

    /**
     * The Y offset relative to the master block.
     */
    public static final IntegerProperty Y_OFFSET = 
        IntegerProperty.create("y_offset", 0, 2);

    /**
     * The Z offset relative to the master block.
     */
    public static final IntegerProperty Z_OFFSET = 
        IntegerProperty.create("z_offset", 0, 2);

    /**
     * Constructs a GiantAnvilPartBlock.
     *
     * @param properties The block properties.
     */
    public GiantAnvilPartBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(X_OFFSET, 1)
            .setValue(Y_OFFSET, 1)
            .setValue(Z_OFFSET, 1));
    }

    /**
     * Configures the state definition for block properties.
     *
     * @param builder The state definition builder.
     */
    @Override
    protected void createBlockStateDefinition(
        StateDefinition.Builder<Block, BlockState> builder
    )
    {
        builder.add(X_OFFSET, Y_OFFSET, Z_OFFSET);
    }

    /**
     * Calculates the position of the master block.
     *
     * @param pos   The current block position.
     * @param state The current block state.
     * @return The position of the master block.
     */
    public BlockPos getMasterPos(BlockPos pos, BlockState state)
    {
        int dx = state.getValue(X_OFFSET) - 1;
        int dy = state.getValue(Y_OFFSET) - 1;
        int dz = state.getValue(Z_OFFSET) - 1;
        return pos.offset(-dx, -dy, -dz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.INVISIBLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VoxelShape getShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context
    )
    {
        BlockPos masterPos = getMasterPos(pos, state);
        BlockState masterState = level.getBlockState(masterPos);
        if (masterState.getBlock() instanceof GiantAnvilBlock masterBlock)
        {
            VoxelShape masterShape = masterBlock.getFullShape(masterState);
            int dx = state.getValue(X_OFFSET) - 1;
            int dy = state.getValue(Y_OFFSET) - 1;
            int dz = state.getValue(Z_OFFSET) - 1;
            VoxelShape moved = masterShape.move(-dx, -dy, -dz);
            return Shapes.join(moved, Shapes.block(), BooleanOp.AND);
        }
        return Shapes.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VoxelShape getCollisionShape(
        BlockState state,
        BlockGetter level,
        BlockPos pos,
        CollisionContext context
    )
    {
        return this.getShape(state, level, pos, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playerWillDestroy(
        Level level,
        BlockPos pos,
        BlockState state,
        Player player
    )
    {
        if (!level.isClientSide)
        {
            BlockPos masterPos = getMasterPos(pos, state);
            BlockState masterState = level.getBlockState(masterPos);
            if (masterState.getBlock() instanceof GiantAnvilBlock)
            {
                level.destroyBlock(masterPos, !player.isCreative());
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InteractionResult use(
        BlockState state,
        Level level,
        BlockPos pos,
        Player player,
        InteractionHand hand,
        BlockHitResult hit
    )
    {
        BlockPos masterPos = getMasterPos(pos, state);
        BlockState masterState = level.getBlockState(masterPos);
        if (masterState.getBlock() instanceof GiantAnvilBlock)
        {
            return masterState.use(
                level,
                player,
                hand,
                hit.withPosition(masterPos)
            );
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
