package stellanera.test.common.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import stellanera.test.common.block.ModBlocks;
import stellanera.test.common.tile.ARCTile;

public class ARCMenu extends AbstractContainerMenu {

    public ARCMenu(int containerId, Inventory playerInventory, ARCTile tile) {
        super(ModMenus.ARC.get(), containerId);

        this.addSlot(new SlotItemHandler(ARCTile.getItemHandler(tile, null), ARCTile.TOOL_SLOT, 35, 51));
        this.addSlot(new SlotItemHandler(ARCTile.getItemHandler(tile, null), ARCTile.INPUT_BUCKET_SLOT, 8, 15));
        this.addSlot(new SlotItemHandler(ARCTile.getItemHandler(tile, null), ARCTile.OUTPUT_BUCKET_SLOT, 152, 87));
        this.addSlot(new SlotItemHandler(ARCTile.getItemHandler(tile, null), ARCTile.INPUT_SLOT, 71, 15));

        for (int i = 0; i < ARCTile.NUM_OUTPUTS; i++) {
            this.addSlot(new SlotItemHandler(ARCTile.getItemHandler(tile, null), ARCTile.OUTPUT_SLOT + i, 116, 15 + i * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }

        // player inv
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
            }
        }

        // player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 181));
        }
    }

    public ARCMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, null);
    }

    private int playerInvStart = 9;
    private int playerInvEnd = playerInvStart + 30;
    private int hotbarStart = playerInvEnd + 1;
    private int hotbarEnd = hotbarStart + 9;
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = ItemStack.EMPTY;
        Slot movedSlot = this.slots.get(index);

        if (movedSlot != null && movedSlot.hasItem()) {
            ItemStack rawStack = movedSlot.getItem();
            movedStack = rawStack.copy();

            if (index >= ARCTile.OUTPUT_SLOT && index <= ARCTile.OUTPUT_SLOT + ARCTile.NUM_OUTPUTS) {
                if (!this.moveItemStackTo(rawStack, 8, 8+30+9, true));
            }
        }

        return movedStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(ContainerLevelAccess.NULL, player, ModBlocks.ARC.get());
    }
}
