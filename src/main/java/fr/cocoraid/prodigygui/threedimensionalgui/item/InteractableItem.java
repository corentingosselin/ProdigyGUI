package fr.cocoraid.prodigygui.threedimensionalgui.item;

import org.bukkit.entity.Player;

public class InteractableItem {


    private Interactable interactable;
    private Item3D item;
    private Player player;

    public InteractableItem(Item3D item, Interactable interactable) {
        this.item = item;
        this.player = item.getPlayer();
        this.interactable = interactable;
    }

    public Item3D getItem() {
        return item;
    }

    public void setItem(Item3D item) {
        this.item = item;
    }

    public Interactable getInteractable() {
        return interactable;
    }


    public interface Interactable {
    }


    public interface InteractClickable extends Interactable {
        public static InteractClickable EMPTY = SelectorInteractable -> {
        };

        void interact(InteractableItem listener);
    }

}
