package community.harpyvents;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/** Independent retracting grille; no WATHE or vanilla protected hatch inheritance. */
public final class VentBlock extends Block {
    public static final MapCodec<VentBlock> CODEC = createCodec(VentBlock::new);
    public static final BooleanProperty OPEN = Properties.OPEN;
    /** The side of the block occupied by the grille, against its placement surface. */
    public static final DirectionProperty FACING = Properties.FACING;

    private static final VoxelShape DOWN = createCuboidShape(0, 0, 0, 16, 2, 16);
    private static final VoxelShape UP = createCuboidShape(0, 14, 0, 16, 16, 16);
    private static final VoxelShape NORTH = createCuboidShape(0, 0, 0, 16, 16, 2);
    private static final VoxelShape SOUTH = createCuboidShape(0, 0, 14, 16, 16, 16);
    private static final VoxelShape WEST = createCuboidShape(0, 0, 0, 2, 16, 16);
    private static final VoxelShape EAST = createCuboidShape(14, 0, 0, 16, 16, 16);

    public VentBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(OPEN, false).with(FACING, Direction.DOWN));
    }

    @Override
    public MapCodec<VentBlock> getCodec() { return CODEC; }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getSide().getOpposite());
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(OPEN) ? VoxelShapes.empty() : getOutlineShape(state, world, pos, context);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos,
                                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        return toggle(state, world, pos, player) == ActionResult.FAIL
                ? ItemActionResult.FAIL : ItemActionResult.success(world.isClient);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        return toggle(state, world, pos, player);
    }

    private ActionResult toggle(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (player.isSpectator()) return ActionResult.FAIL;
        String mainHandId = Registries.ITEM.getId(player.getMainHandStack().getItem()).toString();
        String offHandId = Registries.ITEM.getId(player.getOffHandStack().getItem()).toString();
        if (!VentAccess.canToggle(state.get(OPEN), mainHandId, offHandId)) return ActionResult.FAIL;

        if (!world.isClient) {
            boolean opening = !state.get(OPEN);
            world.setBlockState(pos, state.with(OPEN, opening), Block.NOTIFY_ALL);
            world.playSound(null, pos, opening ? SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN
                    : SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.8F, 0.9F);
            world.emitGameEvent(player, opening ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
        return ActionResult.success(world.isClient);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return rotate(state, mirror.getRotation(state.get(FACING)));
    }
}
