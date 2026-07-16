package salt_models;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Represents the main block of the multi-block Giant Anvil.
 */
public class GiantAnvilBlock extends AnvilBlock
{
    private static final VoxelShape BASE = 
        Block.box(-10.0D, -16.0D, -10.0D, 26.0D, -4.0D, 26.0D);
    
    private static final VoxelShape TONGUE_Z = 
        Block.box(-4.0D, -4.0D, -7.0D, 20.0D, -1.0D, 23.0D);
    private static final VoxelShape SHAFT_Z = 
        Block.box(2.0D, -1.0D, -4.0D, 14.0D, 14.0D, 20.0D);
    private static final VoxelShape TOP_Z = 
        Block.box(-7.0D, 14.0D, -16.0D, 23.0D, 32.0D, 32.0D);
    
    private static final VoxelShape TONGUE_X = 
        Block.box(-7.0D, -4.0D, -4.0D, 23.0D, -1.0D, 20.0D);
    private static final VoxelShape SHAFT_X = 
        Block.box(-4.0D, -1.0D, 2.0D, 20.0D, 14.0D, 14.0D);
    private static final VoxelShape TOP_X = 
        Block.box(-16.0D, 14.0D, -7.0D, 32.0D, 32.0D, 23.0D);

    private static final VoxelShape Z_SHAPE = 
        Shapes.or(BASE, TONGUE_Z, SHAFT_Z, TOP_Z);
    private static final VoxelShape X_SHAPE = 
        Shapes.or(BASE, TONGUE_X, SHAFT_X, TOP_X);

    /**
     * Constructs a GiantAnvilBlock.
     *
     * @param properties The block properties.
     */
    public GiantAnvilBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
    }

    /**
     * Returns the full shape of the giant anvil block.
     *
     * @param state The block state.
     * @return The VoxelShape of the full anvil.
     */
    public VoxelShape getFullShape(BlockState state)
    {
        Direction direction = state.getValue(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
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
        return Shapes.join(getFullShape(state), Shapes.block(), BooleanOp.AND);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos masterPos = clickedPos.above();

        Direction direction = context.getHorizontalDirection().getClockWise();
        Direction.Axis axis = direction.getAxis();

        for (int dOffset = -1; dOffset <= 1; dOffset++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                int dx = (axis == Direction.Axis.X) ? dOffset : 0;
                int dz = (axis == Direction.Axis.Z) ? dOffset : 0;
                
                BlockPos checkPos = masterPos.offset(dx, dy, dz);
                if (checkPos.equals(clickedPos)) continue;
                BlockState checkState = level.getBlockState(checkPos);
                if (!checkState.canBeReplaced(context))
                {
                    return null;
                }
            }
        }
        return super.getStateForPlacement(context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlacedBy(
        Level level,
        BlockPos pos,
        BlockState state,
        LivingEntity placer,
        ItemStack stack
    )
    {
        log.Me("[GiantAnvil Debug] setPlacedBy called at " + pos 
            + " | client=" + level.isClientSide);
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide)
        {
            BlockPos masterPos = pos.above();
            log.Me("[GiantAnvil Debug] Placing master at " + masterPos);
            level.setBlock(masterPos, state, 3);

            Direction.Axis axis = state.getValue(FACING).getAxis();

            for (int dOffset = -1; dOffset <= 1; dOffset++)
            {
                for (int dy = -1; dy <= 1; dy++)
                {
                    if (dOffset == 0 && dy == 0) continue;
                    
                    int dx = (axis == Direction.Axis.X) ? dOffset : 0;
                    int dz = (axis == Direction.Axis.Z) ? dOffset : 0;
                    
                    BlockPos partPos = masterPos.offset(dx, dy, dz);
                    Block part = CustomAnvils.ANVIL_PART.get();
                    BlockState partState = part.defaultBlockState()
                        .setValue(GiantAnvilPartBlock.X_OFFSET, dx + 1)
                        .setValue(GiantAnvilPartBlock.Y_OFFSET, dy + 1)
                        .setValue(GiantAnvilPartBlock.Z_OFFSET, dz + 1);
                    log.Me("[GiantAnvil Debug] Placing part at " + partPos 
                        + " offset=" + dx + "," + dy + "," + dz);
                    level.setBlock(partPos, partState, 3);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRemove(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockState newState,
        boolean isMoving
    )
    {
        log.Me("[GiantAnvil Debug] onRemove called at " + pos 
            + " | newState=" + newState.getBlock() 
            + " | client=" + level.isClientSide);
        
        Block newBlock = newState.getBlock();
        boolean sameBlock = state.is(newBlock);
        boolean isPart = newBlock instanceof GiantAnvilPartBlock;

        if (!sameBlock && !isPart)
        {
            log.Me("[GiantAnvil Debug] Triggering cleanup from " + pos 
                + " | client=" + level.isClientSide);
            
            Direction.Axis axis = state.getValue(FACING).getAxis();
            
            for (int dOffset = -1; dOffset <= 1; dOffset++)
            {
                for (int dy = -1; dy <= 1; dy++)
                {
                    if (dOffset == 0 && dy == 0) continue;
                    
                    int dx = (axis == Direction.Axis.X) ? dOffset : 0;
                    int dz = (axis == Direction.Axis.Z) ? dOffset : 0;
                    
                    BlockPos partPos = pos.offset(dx, dy, dz);
                    BlockState partState = level.getBlockState(partPos);
                    if (partState.is(CustomAnvils.ANVIL_PART.get()))
                    {
                        log.Me("[GiantAnvil Debug] Removing part at " + partPos 
                            + " during cleanup | client=" + level.isClientSide);
                        level.setBlock(partPos, Blocks.AIR.defaultBlockState(), 35);
                    }
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    /**
     * Overridden to disable standard falling behavior.
     */
    @Override
    public void tick(
        BlockState state,
        ServerLevel level,
        BlockPos pos,
        RandomSource random
    )
    {
    }
}