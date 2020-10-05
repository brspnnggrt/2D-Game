package pe3;

import java.util.ArrayList;

// Hero class, alles wat bij de statistieken van de held heeft te maken wordt hier bijgehouden.
// Ik denk dat het meeste voor zich spreekt.

public class Hero extends Theliving {

    private ArrayList<Items> inventory;
    private String name;
    private Items[] equipment;
    private int experience;
    private int experience_needed;

    public Hero(int max_hitpoints, int money, int strength, int endurance, int level, String name, int experience, int experience_needed) {
        super(max_hitpoints, money, strength, endurance, level);
        this.name = name;
        this.experience = experience;
        this.experience_needed = experience_needed;
        inventory = new ArrayList<Items>();
        equipment = new Items[3];
        equipment[0] = new Weapon("Empty", 0, 0, 0,"Empty");
        equipment[1] = new Armor("Empty", 0, 0, 0,"Empty");
        equipment[2] = new Diverse("Empty", 0, 0, 0,0,"Empty");
    }

    public Hero() {
        super(100, 100, 5, 5, 1);
        this.name = "Hero";
        this.experience = 0;
        this.experience_needed = 100;
        inventory = new ArrayList<Items>();
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

    public ArrayList<Items> getInventory() {
        return inventory;
    }

    public Items[] getEquipment() {
        return equipment;
    }

    public boolean levelUp() {
        experience += 50;
        if (experience >= experience_needed) {
            experience_needed = 2 * experience_needed;
            level += 1;
            strength += 1;
            max_hitpoints += 5;
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getExp() {
        return experience;
    }

    public int getExpneeded() {
        return experience_needed;
    }
}