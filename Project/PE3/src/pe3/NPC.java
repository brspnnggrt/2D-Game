package pe3;

import java.util.ArrayList;

// Wordt niet gebruikt, kan je gebruiken voor externe characters die rondlopen in de map

public class NPC extends Theliving {

    private ArrayList<Items> inventory;
    private String name;
    private Items[] equipment;

    public NPC(int max_hitpoints, int money, int strength, int endurance, int level, String name) {
        super(max_hitpoints, money, strength, endurance, level);
        this.name = name;
        equipment = new Items[3];
        equipment[0] = new Weapon("Empty", 0, 0, 0,"Empty");
        equipment[1] = new Armor("Empty", 0, 0, 0,"Empty");
        equipment[2] = new Diverse("Empty", 0, 0, 0,0,"Empty");
    }

    public NPC() {
        super(100, 100, 5, 5, 1);
        this.name = "Just a random guy";
        equipment = new Items[3];
        equipment[0] = new Weapon("Empty", 0, 0, 0,"Empty");
        equipment[1] = new Armor("Empty", 0, 0, 0,"Empty");
        equipment[2] = new Diverse("Empty", 0, 0, 0,0,"Empty");
    }

    public void eQuip(Items item) {
        if (item instanceof Weapon) {
            equipment[0] = item;
        }
        if (item instanceof Armor) {
            equipment[1] = item;
        }
        if (item instanceof Diverse) {
            equipment[2] = item;
        }
    }

    public void addtoInventory(Items item) {
        inventory.add(item);
    }
}