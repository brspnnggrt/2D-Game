package pe3;

import java.awt.*;
import javax.swing.*;

// Monster gegevens
// Meeste gebeurd random

class Monster extends Theliving {

    private Items[] dropitems;
    private Image image;

    public Monster(int max_hitpoints, int money, int strength, int endurance, int level) {
        super(max_hitpoints, money, strength, endurance, level);

        dropitems = new Items[(int) (3 * Math.random()) + 1];  // maximum 3 items, minimum 1
        int rand;
        int teller = 0;
        for (Items item : dropitems) {
            rand = (int) (4 * Math.random()) + 1;
            switch (rand) {
                case 1:
                    dropitems[teller] = new Weapon();
                    break;
                case 2:
                    dropitems[teller] = new Armor();
                    break;
                case 3:
                    dropitems[teller] = new Potion();
                    break;
                case 4:
                    dropitems[teller] = new Diverse();
                    break;
                default:
                    break;
            }
            teller += 1;
        }

        rand = (int) (2 * Math.random()) + 1;
        if (rand == 1) {
            image = new ImageIcon("Images/Monsters/dragon.png").getImage();
        }
        if (rand == 2) {
            image = new ImageIcon("Images/Monsters/troll.png").getImage();
        }
    }

    public Monster() {
        //door deze regel is er toch al zeker alles ingevuld, maar wordt overschreven
        super(25, 25, 5, 5, 1);

        dropitems = new Items[(int) (2 * Math.random()) + 1];  // maximum 3 items, minimum 1
        int rand;
        int teller = 0;
        for (Items item : dropitems) {
            rand = (int) (100 * Math.random()) + 1;
            if ( rand <= 70 ) {
                dropitems[teller] = new Potion();
            } else {
            if ( rand <= 80 ) {
                dropitems[teller] = new Weapon();
            } else {
            if ( rand <= 90 ) {
                dropitems[teller] = new Armor();
            } else {
            if ( rand <= 100 ) {
                dropitems[teller] = new Diverse();
            } } } } 
            teller += 1;
        }

        rand = (int) (6 * Math.random()) + 1;
        if (rand <= 4) {
            //troll instellen
            image = new ImageIcon("Images/Monsters/troll.png").getImage();
            max_hitpoints = 50;
            money = 150;
            strength = 5;
            endurance = 5;
            level = 1;
        } else {
			if (rand <= 6) {
				//draak instellen
				image = new ImageIcon("Images/Monsters/dragon.png").getImage();
				max_hitpoints = 100;
				money = 300;
				strength = 10;
				endurance = 10;
				level = 2;
			} 
		}

    }

    public Items[] gatherLoot() {
        return dropitems;
    }

    public Image getImage() {
        return image;
    }
}

