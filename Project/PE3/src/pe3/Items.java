package pe3;

// Klasse items en extends zijn dingen die voorkomen in het spel en gebruikt kunnen worden door de held.
// Ze worden bijgehouden in de Inventory van de held/npc.
// Chancefordrop is een waarde die laat zien hoe snel de item gedropt zal worden door monsters, maar deze is nog niet geimplementeerd.
// Meeste spreekt oook voor zich.

public abstract class Items {

    protected String name;
    protected int value;
    protected int chancefordrop;
    protected String tooltipdescription;

    public Items(String name, int value, int chancefordrop, String tooltipdescription) {
        this.name = name;
        this.value = value;
        this.chancefordrop = chancefordrop;
        this.tooltipdescription = tooltipdescription;
    }
}

class Weapon extends Items {

    private int attack;
    int rand;

    public Weapon(String name, int value, int chancefordrop, int attack, String tooltipdescription) {
        super(name, value, chancefordrop, tooltipdescription);
        this.attack = attack;
    }

    public Weapon() {
        // call to super
        super("Broadsword", 50, 4,"Medieval sword used by Knights.");
        attack = 10;
        rand = (int) (100 * Math.random()) + 1;
        if ( (rand <= 40) && (rand >= 20) ) {
            name = "Iron Axe";
            value = 100;
            attack = 20;
            tooltipdescription = "Iron Axe used by the Dwarfs.";
        } else {
            if ( rand <= 80 ) {
                name = "Ice Sword";
                value = 1000;
                attack = 50;
                tooltipdescription = "Ice Sword forged by the Elves.";
            }
        else {
            if ( rand <= 100 ) {
                name = "Ultima Weapon";
                value = 10000;
                attack = 100;
                tooltipdescription = "Legendary Sword.";
            }
        } }
    }

    public int getAttack() {
        return attack;
    }
}

class Armor extends Items {

    private int defence;
    int rand;

    public Armor(String name, int value, int chancefordrop, int defence, String tooltipdescription) {
        super(name, value, chancefordrop, tooltipdescription);
        this.defence = defence;
    }

    public Armor() {
        super("Leather Armor", 50, 4, "Regular, cheap armor.");
        defence = 10;
        rand = (int) (100 * Math.random()) + 1;
        if ( (rand <= 40) && (rand >= 20) ) {
            name = "Light Armor";
            value = 100;
            defence = 20;
            tooltipdescription = "Light Armor for high agility.";
        } else {
            if ( rand <= 80 ) {
                name = "Ice Armor";
                value = 1000;
                defence = 50;
                tooltipdescription = "Ice Armor forged by the Elves";
            }
        else {
            if ( rand <= 100 ) {
                name = "Ultima Armor";
                value = 10000;
                defence = 100;
                tooltipdescription = "Legendary Armor";
            }
        } }
    }

    public int getDefence() {
        return defence;
    }
}

class Potion extends Items {

    private int restore;
    int rand;

    public Potion(String name, int value, int chancefordrop, int restore, String tooltipdescription) {
        super(name, value, chancefordrop, tooltipdescription);
        this.restore = restore;
    }

    public Potion() {
        super("Potion", 20, 50, "Regular potion, restores 25 Hitpoints.");
        restore = 25;
        rand = (int) (100 * Math.random()) + 1;
        if ( (rand <= 40) && (rand >= 20) ) {
            name = "High-Potion";
            value = 100;
            restore = 50;
            tooltipdescription = "High-Potion, restores 50 Hitpoints";
        } else {
            if ( rand <= 80 ) {
                name = "Ultra-Potion";
                value = 1000;
                restore = 100;
                tooltipdescription = "Ultra-Potion, restores 100 Hitpoints.";
            }
        else {
            if ( rand <= 100 ) {
                name = "Super-Potion";
                value = 10000;
                restore = 200;
                tooltipdescription = "Super-Potion, restores 200 Hitpoints.";
            }
        } }
    }

    public int getRestore() {
        return restore;
    }
}

class Diverse extends Items {

    private int attack;
    private int defence;
    int rand;

    public Diverse(String name, int value, int chancefordrop, int attack, int defence, String tooltipdescription) {
        super(name, value, chancefordrop, tooltipdescription);
        this.attack = attack;
        this.defence = defence;
    }

    public Diverse() {
        super("Iron Ring", 50, 2, "Ring made of Iron");
        attack = 2;
        defence = 2;
        rand = (int) (100 * Math.random()) + 1;
        if ( (rand <= 40) && (rand >= 20) ) {
            name = "Tiara";
            value = 100;
            attack = 5;
            defence = 5;
            tooltipdescription = "Worn Tiara";
        } else {
            if ( rand <= 80 ) {
                name = "Amethyst";
                value = 500;
                attack = 10;
                defence = 10;
                tooltipdescription = "Purple rock with magical powers.";
            }
        else {
            if ( rand <= 100 ) {
                name = "Ribbon";
                value = 1000;
                attack = 20;
                defence = 20;
                tooltipdescription = "Ribbon.";
            }
        } }
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }
}
