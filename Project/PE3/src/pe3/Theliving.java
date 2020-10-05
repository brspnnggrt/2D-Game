package pe3;

// TheLiving is de superklasse voor wat alle levende wezens gemeen hebben.
// Abstract.

public abstract class Theliving {

    protected int hitpoints;
    protected int max_hitpoints;
    protected int money;
    protected int strength;
    protected int endurance;
    protected int level;

    public Theliving(int max_hitpoints, int money, int strength, int endurance, int level) {
        this.max_hitpoints = max_hitpoints;
        this.hitpoints = max_hitpoints;
        this.money = money;
        this.strength = strength;
        this.endurance = endurance;
        this.level = level;
    }
}

