package pe3;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import java.io.*;
import javax.swing.event.*;

// Deze klasse vormt het algemeen programma.
// De klasse model vormt de backbone van het spel waar alle verwerkingen gebeuren.
// Ook is er een klasse view die het gene verwerkt dat getoond moet worden
// De klasse controller gaat het keyboard na.
// En in de klasse map worden de mappen bijgehouden.
// ActionJList en JListExtended zorgen beide voor uitbreiding van de JLists
// ActionJList zorgt voor verwerking gegevens. (+ Dubbelklik ip enkelklik)
// JListExtended zorgt voor uiterilijk JList + Tooltips


public class PE3 extends JFrame {

    public static void main(String[] args) {
        new PE3();
    }

    public PE3() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("PE3");
        Paneel panel = new Paneel();
        this.setContentPane(panel);
        this.setVisible(true);
        this.setSize(816, 708); // view van spel eindigt op 816 x 735
    }
}

class Paneel extends JPanel {

    private Model model;
    private View view;
    private Controller controller;

    public Paneel() {

        setLayout(new BorderLayout());
        model = new Model();
        view = new View(model);
        view.setPreferredSize(new Dimension(816, 708));
        controller = new Controller(model, view);
        controller.setPreferredSize(new Dimension(0, 0));
        controller.setBackground(Color.black);
        add(view, BorderLayout.CENTER);
        add(controller, BorderLayout.SOUTH);

    }
}

class Model {

    private static final int tW = 32; // tile width
    private static final int tH = 32; // tile height
    //later inladen uit file die gemaakt is met editor
    private int map_1[][];
    private int map_2[][];
    private int map_3[][];
    private int map_4[][]; // deze map duid de tiles aan waarop je mag lopen
    private Boolean dialog = false;
    // op voorhand ingestelde variables charpositie/map/scherm
    private Point startwh;
    private Point mapwh;
    private Point screenwh;
    // variables die characterpositie op scherm en op de map en de locatie van de map tegenover het scherm bijhouden
    private Point charlocatie_screen;
    private Point charlocatie_map;
    private Point maplocatie;
    // richting dat het character opgaat
    private int richting;
    // gebruikte afbeeldingen als tileset
    private Image tilesetchar;
    private Image tilesetmap;
    private boolean battle;  // true, battle aan de gang.
    private boolean stopbattle;
    private boolean menu = false; // true, menu is open
    private int battle_random_getal; // getal dat aanduit hoeveel keer je je nog kan verplaatsen. Dit getal ligt tussen 7 en 25 , wordt gewijzigd bij stilstand.
    private Map worldmap; //instantie van de wereldmap
    private Map testmap;
    private Point[] eventlocaties;
    private boolean hostilearea;
    private Map geladenmap;
    /////////////////////////////////////////////////// Hier stopt de zever over de "engine"
    /////////////////////////////////////////////////// En start de zever over het "Statistics System"
    private Hero hero; // hero aanmaken
    private Monster monster;
    private int damagetaken;
    private int damagegiven;
    private int healingpoints;

    public Model() {

        // op 50,50 zetten zodat er geen probleem is als er een 2de map wordt geladen die groter is. ... arrayoutofboundsexception
        map_1 = new int[75][75];
        map_2 = new int[75][75];
        map_3 = new int[75][75];
        map_4 = new int[75][75];

        tilesetchar = new ImageIcon("Images/Character/character.png").getImage();

        mapwh = new Point();
        screenwh = new Point(25, 21);   //25 breed 21 hoog

        richting = 0; // zodat je niet ineens ergens naartoe loopt

        eventlocaties = new Point[10]; // maximum van 10 events op 1 map
        for (int i = 0; i < 10; i++) {
            eventlocaties[i] = new Point(-1, -1);  // heel de array initializeren en vullen met een slechte waarde.
        }
        eventlocaties[0].x = 13; // op 4,4 zal er een event zijn.
        eventlocaties[0].y = 15;
        worldmap = new Map("Images/Levels/tileset.png", "Images/Levels/supermap23.txt", 13, 16, true, eventlocaties);  // startpos 4 ,4

        eventlocaties = null;
        eventlocaties = new Point[10]; // maximum van 10 events op 1 map
        for (int i = 0; i < 10; i++) {
            eventlocaties[i] = new Point(-1, -1);  // heel de array initializeren en vullen met een slechte waarde.
        }
        eventlocaties[0].x = 16; // op 5,4 zal er een event zijn.
        eventlocaties[0].y = 7;
        testmap = new Map("Images/Levels/outdoortileset.png", "Images/Levels/mapoutdoor.txt", 12, 19, true, eventlocaties); // startpos 5,4

        charlocatie_screen = new Point();   // locatie van character op het scherm
        charlocatie_map = new Point();      // dit wordt gewoon berekend. Moet niet zelf ingesteld worden
        maplocatie = new Point(0, 0);   // 0 breed 0 hoog , locatie van de map op het scherm, hoeveel hij verschoven is naar rechts en naar onder

        loadMap(worldmap);

        //charlocatie_screen.x = startwh.x;
        //charlocatie_screen.y = startwh.y;


        /////////////////////////////////////////////////// Hier stopt de zever over de "engine"
        /////////////////////////////////////////////////// En start de zever over het "Statistics System"

        hero = new Hero(); // geen argumenten, default hero.
        hero.addtoInventory(new Weapon()); // ik geef hem een zwaardµ
        hero.eQuip(hero.getInventory().get(0)); // hij gebruikt het zwaard om mee te vechten

    }

    public void loadMap(Map laaddezemap) {

        geladenmap = laaddezemap;

        if (geladenmap == worldmap) {
            System.out.println("worldmap");
        }
        if (geladenmap == testmap) {
            System.out.println("testmap !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        map_1 = null;
        map_2 = null;
        map_3 = null;
        map_4 = null;
        map_1 = new int[75][75];
        map_2 = new int[75][75];
        map_3 = new int[75][75];
        map_4 = new int[75][75];

        startwh = new Point();
        startwh.x = laaddezemap.getStartposition().x;                              // startpositie op x-as 12 (midden) -- alleen bij de worldmap
        startwh.y = laaddezemap.getStartposition().y;                              // startpositie op y-as 10 (midden) -- bij de worldmap dus

        mapwh = new Point();
        mapwh.x = laaddezemap.getMapwh().x;                                        // hoe breedt de totale map is
        mapwh.y = laaddezemap.getMapwh().y;                                        // hoe hoog de totale map is

        // volgende irriterende code zorgt ervoor dat de character juist op de map komt
        
        if ((startwh.x >= 12) && (startwh.x <= 20)) {  // zit hij in de zone waarop de character in het midden van het scherm zit en de map verplaatst is.
            charlocatie_screen.x = 12; // character in midden van scherm.
            maplocatie.x = startwh.x - charlocatie_screen.x;
        } else {
            if (startwh.x < 12) {
                charlocatie_screen.x = startwh.x;
                maplocatie.x = 0;
            } else {
                if (startwh.x > 20) {
                    maplocatie.x = 8;
                    charlocatie_screen.x = startwh.x - maplocatie.x;
                }
            }
        }
        if ((startwh.y >= 10) && (startwh.y <= 22)) { //  zelfde op Y-as
            charlocatie_screen.y = 10; // character in midden van scherm.
            maplocatie.y = startwh.y - charlocatie_screen.y; // maplocatie berekenen
        } else {
            if (startwh.y < 10) {
                charlocatie_screen.y = startwh.y;  // characterlocatie is nu gewoon de startpositie.
                maplocatie.y = 0; // maplocatie = 0
            } else {
                if (startwh.y > 22) {
                    maplocatie.y = 12; // hopelijk wel.
                    charlocatie_screen.y = startwh.y - maplocatie.y; // geen idee of dit werkt
                }
            }
        }

        // map opvragen uit mapklasse, die het uit bestand haalt.
        map_1 = laaddezemap.getLayer1();
        map_2 = laaddezemap.getLayer2();
        map_3 = laaddezemap.getLayer3();
        map_4 = laaddezemap.getCollision();

        // tileset opvragen.
        tilesetmap = null;
        tilesetmap = new ImageIcon(laaddezemap.getTileset()).getImage();
        hostilearea = laaddezemap.getHostilearea();


        // events doorsturen
        // eerst eventarray leegmaken. ( -1 zorgt ervoor dat er nerges een event zal voorkomen)
        eventlocaties = null;
        eventlocaties = new Point[10]; // maximum van 10 events op 1 map
        for (int i = 0; i < 10; i++) {
            eventlocaties[i] = new Point();  // heel de array initializeren en vullen met een slechte waarde.
        }

        // eventlocaties ophalen, dit zijn coordinaten waarop dingen kunnen gebeuren. Meestal een mapchange, dit is nog NIET goed iutgewerkt.
        try {
            if (geladenmap == worldmap) {
                System.out.println("worldmap");
                System.arraycopy(worldmap.getEventlocaties(), 0, eventlocaties, 0, 10);
                for (int i = 0; i < 10; i++) {
                    System.out.println("super" + eventlocaties[i].x);
                    System.out.println("super" + eventlocaties[i].y);
                }
            }
            if (geladenmap == testmap) {
                System.out.println("testmap !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.arraycopy(testmap.getEventlocaties(), 0, eventlocaties, 0, 10);
                for (int i = 0; i < 10; i++) {
                    System.out.println("super" + eventlocaties[i].x);
                    System.out.println("super" + eventlocaties[i].y);
                }
            }

        } catch (Exception e2) {
            System.out.println("error bij events laden");
        }

        if (geladenmap == worldmap) {
            System.out.println("worldmap");
        }
        if (geladenmap == testmap) {
            System.out.println("testmap");
        }

    }

    // Hierbij wordt er een event gestart, dit gebeurd na een verplaatsing op een Eventlocatie coordinaat.
    // Momenteel allen change levels. Ik zou het systeem ook helemaal moeten veranderen iegelijk...
    public void startEvent() {
        System.out.println("Change level:");
        if (geladenmap == worldmap) {
            loadMap(testmap);
        } else {
            if (geladenmap == testmap) {
                loadMap(worldmap);
            }
        }
    }

    // Deze test of er een event moet gebeuren.
    public boolean getEvent() {

        Point p = getCharlocatie_map();
        for (int i = 0; i < 10; i++) {
            if ((eventlocaties[i].y == p.y) && (eventlocaties[i].x == p.x)) {
                startEvent();
                return true;
            }
        }
        return false;
    }

    // Haalt inventory op van de character
    public ArrayList<Items> getInventory() {
        return hero.getInventory();
    }

    // haalt equipment op van character
    public Items[] getEquipment() {
        return hero.getEquipment();
    }

    // Gaat na of er een verplaatsing mag gebeuren of niet(bvb loopt tegen kant van map)
    public boolean getCollision() {

        // deze exceptie wordt opgeroepen als de tegel buiten de grenzen van de array valt, hier mag je uiteraard niet op lopen dus stuur ik een true terug bij exception.
        try {
            Point p = getCharlocatie_map();
            if (map_4[p.y][p.x] == 1111) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    //verplaats character 1 plaats naar boven
    public void char_naarboven() {

        if (getBattle() == false) {
            richting = 2;
            charlocatie_screen.y -= 1;
            if (getCollision() == false) {
                charlocatie_screen.y += 1;
                if (maplocatie.y == 0) {
                    if (charlocatie_screen.y > 0) {
                        charlocatie_screen.y -= 1;
                    }
                }
                if (charlocatie_screen.y > (screenwh.y / 2)) {
                    charlocatie_screen.y -= 1;
                }
            } else {
                charlocatie_screen.y += 1;
            }
        }
    }

    // naar links
    public void char_naarlinks() {

        if (getBattle() == false) {
            richting = 1;
            charlocatie_screen.x -= 1;
            if (getCollision() == false) {
                charlocatie_screen.x += 1;
                if (maplocatie.x == 0) {
                    if (charlocatie_screen.x > 0) {
                        charlocatie_screen.x -= 1;
                    }
                }
                if (charlocatie_screen.x > (screenwh.x / 2)) {
                    charlocatie_screen.x -= 1;
                }
            } else {
                charlocatie_screen.x += 1;
            }
        }
    }

    // naar onder
    public void char_naaronder() {
        if (getBattle() == false) {
            richting = 4;
            charlocatie_screen.y += 1;
            if (getCollision() == false) {
                charlocatie_screen.y -= 1;
                if (maplocatie.y == (mapwh.y - screenwh.y)) {
                    if (charlocatie_screen.y < (screenwh.y - 1)) {
                        charlocatie_screen.y += 1;
                    }
                }
                if (charlocatie_screen.y < (screenwh.y / 2)) {
                    charlocatie_screen.y += 1;
                }
            } else {
                charlocatie_screen.y -= 1;
            }
        }
    }

    // naar rechts
    public void char_naarrechts() {
        if (getBattle() == false) {
            richting = 3;
            charlocatie_screen.x += 1;
            if (getCollision() == false) {
                charlocatie_screen.x -= 1;
                if (maplocatie.x == (mapwh.x - screenwh.x)) {
                    if (charlocatie_screen.x < (screenwh.x - 1)) {
                        charlocatie_screen.x += 1;
                    }
                }
                if (charlocatie_screen.x < (screenwh.x / 2)) {
                    charlocatie_screen.x += 1;
                }
            } else {
                charlocatie_screen.x -= 1;
            }
        }
    }

    //Hetzelfde, maar voor de map, hierbij verplaats de character niet op het scherm, wel algemene coordinaat. De animatie wordt ook getoond.
    public boolean map_naarboven() {
        richting = 2;
        maplocatie.y -= 1;
        if (getCollision() == false) {
            maplocatie.y += 1;
            if (maplocatie.y != 0) {
                if (charlocatie_screen.y == (screenwh.y / 2)) {
                    maplocatie.y -= 1;
                    return true;
                }
            }
        } else {
            maplocatie.y += 1;
        }
        return false;
    }

    public boolean map_naarlinks() {
        richting = 1;
        maplocatie.x -= 1;
        if (getCollision() == false) {
            maplocatie.x += 1;
            if (maplocatie.x != 0) {
                if (charlocatie_screen.x == (screenwh.x / 2)) {
                    maplocatie.x -= 1;
                    return true;
                }
            }
        } else {
            maplocatie.x += 1;
        }
        return false;
    }

    public boolean map_naaronder() {
        richting = 4;
        maplocatie.y += 1;
        if (getCollision() == false) {
            maplocatie.y -= 1;
            if (maplocatie.y != mapwh.y - screenwh.y) {
                if (charlocatie_screen.y == (screenwh.y / 2)) {
                    maplocatie.y += 1;
                    return true;
                }
            }
        } else {
            maplocatie.y -= 1;
        }
        return false;
    }

    public boolean map_naarrechts() {
        richting = 3;
        maplocatie.x += 1;
        if (getCollision() == false) {
            maplocatie.x -= 1;
            if (maplocatie.x != mapwh.x - screenwh.x) {
                if (charlocatie_screen.x == (screenwh.x / 2)) {
                    maplocatie.x += 1;
                    return true;
                }
            }
        } else {
            maplocatie.x -= 1;
        }
        return false;
    }

    // Dit geeft de algemene positie van de character op de TOTALE MAP
    public Point getCharlocatie_map() {
        charlocatie_map.x = charlocatie_screen.x + maplocatie.x;
        charlocatie_map.y = charlocatie_screen.y + maplocatie.y;
        return charlocatie_map;
    }

    // Test welke richting er uitgegaan is.
    public int getRichting() {
        return richting;
    }

    // Wordt niet gebriukt maar dient voor te testen of er een gesprek bezig is.
    public boolean getDialog() {
        return dialog;
    }

    // Haalt de tilesetmap op.
    public Image getTilesetmap() {
        return tilesetmap;
    }

    // Haalt de tileset op voor de character
    public Image getTilesetchar() {
        return tilesetchar;
    }

    // Waar zit de character op het scherm? 
    public Point getCharLocatie_screen() {
        return charlocatie_screen;
    }

    // Tile width
    public int gettW() {
        return tW;
    }

    // Tile Height
    public int gettH() {
        return tH;
    }

    // Positie van de map (hoeveel verschoven tenopzichte van LINKSBOVEN = 0,0)
    public Point getMapLocatie() {
        return maplocatie;
    }

    public int[][] getMap1() {
        return map_1;
    }

    public int[][] getMap2() {
        return map_2;
    }

    public int[][] getMap3() {
        return map_3;
    }

    public Point getMapwh() {
        return mapwh;
    }

    public Point getScreenwh() {
        return screenwh;
    }

    public void setRichting(int integer) {
        richting = integer;
    }

    // tel random getal af voor battle
    public void setBattleteller(int integer) {
        battle_random_getal = integer;
    }

    // Is er al een battle ? 
    public int getBattleteller() {
        return battle_random_getal;
    }

    // Er is een battle!
    public void setBattle(boolean bool) {
        battle = bool;
    }

    // Maar de battle is gedaan.
    public boolean stopBattle() {
        if (stopbattle == true) {
            stopbattle = false;
            setBattle(false);
            unloadBattle();
            return true;
        } else {
            return false;
        }
    }

    // Stop de battle
    public void setStopBattle() {
        stopbattle = true;
    }

    // Laad de battle
    public void loadBattle() {

        if (monster == null) {
            monster = new Monster();
        }

    }

    // Onlaad de battle
    public void unloadBattle() {
        monster = null;
    }

    // Is er een battle bezig?
    public boolean getBattle() {
        return battle;
    }

    // Je valt iemand aan en er gebeurt:
    public boolean tryKill() {
        if (monster.hitpoints > 0) {
            Weapon temp1 = (Weapon) hero.getEquipment()[0];
            Diverse temp3 = (Diverse) hero.getEquipment()[2];
            int attackpower = temp1.getAttack();
            int attackboost = temp3.getAttack();
            
            monster.hitpoints -= (hero.strength + attackpower + attackboost);
            damagegiven = hero.strength + attackpower + attackboost;
            if (monster.hitpoints > 0) {
                System.out.println("\n Try to kill, hp over : " + monster.hitpoints);
                return false;
            } else {
                notDead();
                return true;
            }
        } else {
            notDead();
            return true;
        }
    }

    // Ben je nog levend?
    public void notDead() {
        System.out.println("\n Try to kill, hp over : " + monster.hitpoints);
        if (hero.levelUp() == true) {
            System.out.println("\n Level up! : " + hero.level);
        }
        //gather the loot
        for (Items item : monster.gatherLoot()) {
            hero.addtoInventory(item);
        }
    }

    // Als je dood bent zal dit gebeuren
    public boolean ifDead() {
        if (hero.hitpoints > 0) {
            Armor temp2 = (Armor) hero.getEquipment()[1];
            Diverse temp3 = (Diverse) hero.getEquipment()[2];
            int defencepower = temp2.getDefence();
            int defenceboost = temp3.getDefence();

            hero.hitpoints -= (monster.strength - defencepower - defenceboost);
            damagetaken = monster.strength;
            if (hero.hitpoints > 0) {
                System.out.println("\n The monster attacked you in surprise! HP : " + hero.hitpoints);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    // Laat de image zien van het monster waar je tegen moet vechten
    public Image getmonsterImage() {
        if (monster != null) {
            return monster.getImage();
        } else {
            monster = new Monster();
            return monster.getImage();
        }
    }

    public int getDamagetaken() {
        return damagetaken;
    }

    public int getDamagegiven() {
        return damagegiven;
    }

    public void setHealingpoints(int hp) {
        healingpoints = hp;
    }

    public int getHealingpoints() {
        return healingpoints;
    }

    // Moet het menu getoond worden?
    public boolean getMenu() {
        return menu;
    }

    // Het menu moet getoond worden.
    public void setMenu(boolean bool) {
        menu = bool;
    }

    // Laad heel de character in... 
    public Hero getCharacter() {
        return hero;
    }
}

class View extends JPanel implements Runnable, ListSelectionListener {

    private Model model;
    private int[] links2 = {18, 19, 20, 21, 22, 23};
    private int[] rechts2 = {6, 7, 8, 9, 10, 11};
    private int[] onder2 = {12, 13, 14, 15, 16, 17};
    private int[] boven2 = {0, 1, 2, 3, 4, 5};
    private Image img;
    Thread thread;
    private boolean threadsuspended;
    private int count = 6;
    private boolean mapmovement = false;
    private int charactergif1;
    private int charactergif2;
    private boolean run = true;
    private int battleteller;
    private Graphics g2;
    private Image backbuffer;
    private boolean damage;
    private boolean healing;
    private JList list_menu;
    private JList list_battle;
    private DefaultListModel listmodel;
    private JScrollPane JSP;
    private JScrollPane JSP2;
    private JScrollBar scrollbar;
    private String[] battlemenu = {"Attack", "Potion"};

    // In de controller kan ik geen JList laten zien op deze view  dus moest dit hier
    // dit leverde enkele problemen op zoals de Focus... Maar uiteindelijk werkt het wel.
    // De JList heeft talloze dingen om het uiterlijk te beheren.
    // OOk wordt hier de thread beheerd, dit is een runnable dus de klasse view is de thread.

    public View(Model model) {
        this.setLayout(null);
        this.model = model;
        this.setBackground(Color.BLACK);
        thread = new Thread(this);
        thread.start();

        listmodel = new DefaultListModel();
        list_menu = new JListExtended(listmodel, model);

        list_battle = new JList(battlemenu);

        JSP = new JScrollPane(list_menu, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JSP2 = new JScrollPane(list_menu, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(list_menu);
        add(list_battle);
        add(JSP);
        add(JSP2);

        JSP.getViewport().add(list_menu);
        JSP2.getViewport().add(list_battle);

        list_menu.setBounds(580, 60, 180, 270);
        JSP.setBounds(580, 60, 180, 270);
        list_battle.setBounds(75, 530, 150, 100);
        JSP2.setBounds(75, 530, 150, 100);

        list_battle.setFont(new Font("Verdana", Font.ITALIC, 20));

        list_menu.setBackground(new Color(98, 97, 11));
        list_battle.setBackground(new Color(98, 97, 11));
        JSP.setBorder(BorderFactory.createLineBorder(new Color(73, 72, 9)));
        JSP2.setBorder(BorderFactory.createLineBorder(new Color(73, 72, 9)));
        list_menu.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(73, 72, 9)));
        list_menu.setCellRenderer(new LocaleRenderer());
        list_menu.addListSelectionListener(this);
        list_menu.addMouseListener(new ActionJList(list_menu, model, this, listmodel));
        list_battle.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(73, 72, 9)));
        list_battle.setCellRenderer(new LocaleRenderer());
        list_battle.addListSelectionListener(this);
        list_battle.addMouseListener(new ActionJList(list_battle, model, this));

        scrollbar = new JScrollBar(Scrollbar.VERTICAL);
        scrollbar.setBackground(new Color(73, 72, 9));
        scrollbar.setForeground(new Color(98, 97, 11));
        scrollbar.setBorder(BorderFactory.createEmptyBorder());

        scrollbar.setUI(new BasicScrollBarUIExtended());
        JSP.setVerticalScrollBar(scrollbar);

        list_menu.setEnabled(false);
        list_menu.setVisible(false);
        JSP.setVisible(false);
        list_battle.setEnabled(false);
        list_battle.setVisible(false);
        JSP2.setVisible(false);

    }

    public void valueChanged(ListSelectionEvent e) {
        repaint();
    }

    public boolean getThreadsuspended() {
        if (threadsuspended == true) {
            return true;
        }
        return false;
    }

    // 2 verschillende manieren om de thread te starten afhankelijk van wat er precies getoond moet worden

    public void startAnimatie(boolean mapmovement) {

        if (model.getBattle() == false) {
            this.mapmovement = mapmovement;

            threadsuspended = true;
            if (threadsuspended == true) {
                synchronized (this) {
                    notify();
                }
            }
        }

    }

    public void startAnimatiebattle() {

        threadsuspended = true;
        if (threadsuspended == true) {
            synchronized (this) {
                notify();
            }
        }

    }

    public void resetTeller() {
        battleteller = 0;
    }


    // Thread starten!
    public void run() {

        if (model.getBattle() == true) {
            while (true) {
                try {
                    for (count = 14; count != 0; count -= 2) {
                        thread.sleep(30);
                        repaint();
                    }
                    synchronized (this) {
                        threadsuspended = false;
                        wait();
                    }
                } catch (InterruptedException e) {
                }

            }
        } else {
            while (true) {
                try {
                    battleteller += 1;
                    for (count = 14; count != 0; count -= 2) {
                        if (run == false) {
                            thread.sleep(70);
                        } else {
                            thread.sleep(30);
                        }
                        if (count == 14 || count == 12 || count == 10) {
                            charactergif1 = 3;
                            charactergif2 = 0;
                        }
                        if (count == 8 || count == 6 || count == 4) {
                            charactergif1 = 5;
                            charactergif2 = 2;
                        }
                        if (count == 2) {
                            charactergif1 = 4;
                            charactergif2 = 1;
                        }
                        repaint();
                    }
                    if (model.getEvent() == false) {
                        if ((battleteller == model.getBattleteller()) || (battleteller > 25)) {
                            model.setBattle(true);
                            model.loadBattle();
                            repaint();
                            System.out.println("Battle!");
                        }
                    } else {
                        repaint();
                    }
                    synchronized (this) {
                        threadsuspended = false;
                        wait();
                    }
                } catch (InterruptedException e) {
                }

            }
        }
    }

    //Wordt niet gebruikt
    public void loop(boolean bool) {
        if (bool == true) {
            run = true;
        } else {
            run = false;
        }
    }

    //Laad de map zien!
    public void rePaintmap() {

        if (this.isDisplayable() == true) {

            backbuffer = createImage(32 * 50, 32 * 50);
            g2 = backbuffer.getGraphics();

            g2.drawImage(img, 0, 0, null);

            g2.setColor(Color.BLACK);

            if (model.getDialog() == true) {
                g2.drawImage(new ImageIcon("Images/Dialogbox/dialogbox.png").getImage(), 20, 300, null);
            }

            tekenMap(g2);
            tekenChar(g2);

            g2.dispose();
        }
    }

    // Laat het vechstcherm zien!
    public void rePaintbattle() {

        if (this.isDisplayable() == true) {

            backbuffer = createImage(32 * 25, 32 * 21);
            g2 = backbuffer.getGraphics();

            g2.drawImage(new ImageIcon("Images/Battles/forest.png").getImage(), 0, 0, null);
            g2.drawImage(model.getmonsterImage(), 150, 150, 150, 150, null);
            g2.drawImage(new ImageIcon("Images/Character/ingame.png").getImage(), 500, 300, 186, 280, null);

            if (damage == true) {
                g2.setColor(Color.red);
                g2.setFont(new Font("Verdana", 1, 20));
                g2.drawString("-" + model.getDamagegiven(), 225, 150 - (2 * count));
                g2.drawString("-" + model.getDamagetaken(), 575, 300 - (2 * count));
            }
            if (healing == true) {
                g2.setColor(Color.green);
                g2.setFont(new Font("Verdana", 1, 20));
                g2.drawString("-" + model.getHealingpoints(), 575, 300 + (2 * count));
            }

            g2.setColor(Color.BLACK);
            g2.drawRoundRect(485, 625, model.getCharacter().max_hitpoints * 2, 8, 5, 5);
            g2.setColor(new Color(73, 72, 9));
            g2.fillRoundRect(485, 625, model.getCharacter().max_hitpoints * 2, 8, 5, 5);
            g2.setColor(new Color(98, 97, 11));
            g2.fillRoundRect(485, 623, model.getCharacter().hitpoints * 2, 5, 5, 5);


            g2.dispose();

            list_battle.setEnabled(true);
            list_battle.setVisible(true);
            JSP2.setVisible(true);
        }
    }

    // Laat het menu zien!
    public void rePaintmenu() {

        if (this.isDisplayable() == true) {

            backbuffer = createImage(32 * 25, 32 * 21);
            g2 = backbuffer.getGraphics();

            g2.fillRect(0, 0, 25 * 32, 21 * 32);
            g2.drawImage(new ImageIcon("Images/Menu/menubg.png").getImage(), 0, 0, 25 * 32, 21 * 32, null);
            g2.setColor(Color.black);
            int teller = 0;
            for (Items i : model.getInventory()) {
                if ((teller + 1) <= listmodel.size()) {
                    listmodel.set(teller, i.name);
                } else {
                    listmodel.add(teller, i.name);
                }
                teller += 1;
            }
            list_menu.setEnabled(true);
            list_menu.setVisible(true);
            JSP.setVisible(true);

            g2.drawImage(new ImageIcon("Images/Character/ingame.png").getImage(), 45, 80, 186, 280, this);

            g2.setFont(new Font(Font.MONOSPACED, 1, 22));
            g2.drawString("Name      : " + model.getCharacter().getName(), 250, 80);
            g2.drawString("Hitpoints : " + model.getCharacter().hitpoints + " / " + model.getCharacter().max_hitpoints, 250, 120);
            g2.drawString("Level     : " + model.getCharacter().level, 250, 160);
            g2.drawString("Strength  : " + model.getCharacter().strength, 250, 200);
            g2.drawString("Endurance : " + model.getCharacter().endurance, 250, 240);
            g2.drawString("EXP       : " + model.getCharacter().getExp(), 250, 280);
            g2.drawString("EXP to lvl: " + model.getCharacter().getExpneeded(), 250, 320);
            g2.drawString("Money     : " + model.getCharacter().getMoney(), 250, 360);

            g2.setFont(new Font(Font.MONOSPACED, 1, 12));
            g2.drawString("--Controls -----------", 55, 390);
            g2.drawString("______________________", 55, 395);
            g2.drawString("- Space : Attack     -", 55, 420);
            g2.drawString("-                    -", 55, 435);
            g2.drawString("- F2    : Open Menu  -", 55, 450);
            g2.drawString("-                    -", 55, 465);
            g2.drawString("- F3    : Close Menu -", 55, 480);
            g2.drawString("----------------------", 55, 495);
            g2.drawLine(50, 385, 50, 490);
            g2.drawLine(210, 385, 210, 490);


            g2.setFont(new Font(Font.MONOSPACED, 1, 35));
            g2.drawString("Inventory", 575, 45);
            g2.drawString("Equipment", 575, 375);
            g2.setFont(new Font(Font.MONOSPACED, 1, 18));
            g2.drawString("Weapon", 575, 420);
            g2.drawString("Armor", 575, 500);
            g2.drawString("Diverse", 575, 580);
            g2.setColor(new Color(100, 150, 50));
            g2.drawString(model.getEquipment()[0].name, 600, 460);
            g2.drawString(model.getEquipment()[1].name, 600, 540);
            g2.drawString(model.getEquipment()[2].name, 600, 620);
            g2.setColor(Color.black);

            g2.dispose();
        }
    }

    // Console output gevecht
    public void showConsole() {
        String text = new String("\n Inventory: \n");
        System.out.println(text);
        for (Items item : model.getInventory()) {
            text = item.name;
            System.out.println(text);
        }
        text = "\n Equipment: \n";
        System.out.println(text);
        for (Items item : model.getEquipment()) {
            text = item.name;
            System.out.println(text);
        }
    }

    // Volgende bools zorgen ervoor dat er damage/healing wordt getoond bij aanval.

    public void setDamage() {
        damage = true;
    }

    public void unsetDamage() {
        damage = false;
    }

    public void setHealing() {
        healing = true;
    }

    public void unsetHealing() {
        healing = false;
    }

    // De paintcomponent...
    // Backbuffer besproken in document
    @Override
    public void paintComponent(Graphics g) {

        if (model.getBattle() == true) {
            rePaintbattle();
        } else {
            if (model.getMenu() == true) {
                rePaintmenu();
            } else {
                list_menu.setVisible(false);
                list_menu.setEnabled(false);
                JSP.setVisible(false);
                list_battle.setVisible(false);
                list_battle.setEnabled(false);
                JSP2.setVisible(false);
                rePaintmap();
            }
        }
        g.drawImage(backbuffer, 0, 0, this);

        //Kader in en rond map
        /*
        for (int i = 0; i <= 25; i++) {
        g.drawLine(i * 32, 0, i * 32, 673);
        }

        for (int ii = 0; ii <= 21; ii++) {
        g.drawLine(0, ii * 32, 800, ii * 32);
        }
         */
    }

    // Teken de map tile per tile afhankelijk van tileset image en txt bestand met de arrangement van deze.

    public void drawMap(Graphics g, int t, int x, int y, Image tileset) {

        int mx = t % 12;
        int my = t / 12;

        if (mapmovement == false) {
            switch (model.getRichting()) {
                case 0:
                    g.drawImage(tileset, x, y, x + model.gettW(), y + model.gettH(),
                            mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
                    break;
                case 1: // links
                    g.drawImage(tileset, x - 2 * count, y, x + model.gettW() - 2 * count, y + model.gettH(),
                            mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
                    break;
                case 2: // boven
                    g.drawImage(tileset, x, y - 2 * count, x + model.gettW(), y + model.gettH() - 2 * count,
                            mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
                    break;
                case 3: // rechts
                    g.drawImage(tileset, x + 2 * count, y, x + model.gettW() + 2 * count, y + model.gettH(),
                            mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
                    break;
                case 4: // onder
                    g.drawImage(tileset, x, y + 2 * count, x + model.gettW(), y + model.gettH() + 2 * count,
                            mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
                    break;
                default:
                    break;
            }
        } else {
            g.drawImage(tileset, x, y, x + model.gettW(), y + model.gettH(),
                    mx * model.gettW(), my * model.gettH(), mx * model.gettW() + model.gettW(), my * model.gettH() + model.gettH(), this);
        }
    }

    // Draw character afhankelijk van tileset en positie character.
    public void drawCharacter(Graphics g, int[] t, Image tileset, int x, int y) {

        g.drawImage(img, 0, 0, null);

        int mx[] = new int[t.length];
        int my[] = new int[t.length];

        for (int i = 0; i < t.length; i++) {

            mx[i] = t[i] % 3;
            my[i] = t[i] / 3;

        }

        //if (t[0].equals(Tile.FRONTHEAD1)){


        //op basis van bovenste - verkeerd
        //g.drawImage(tileset, x, y, x + 32, y + 32,
        //mx[1]*tW, my[1]*tH,  mx[1]*tW+tW, my[1]*tH+tH, this);
        //g.drawImage(tileset, x, y + 32, x + 32, y + 64,
        //mx[4]*tW, my[4]*tH,  mx[4]*tW+tW, my[4]*tH+tH, this);

        //op basis van voeten - juist (logische)

        //voeten gebruik maken van myx[3] -> myx[5]

        if (mapmovement == false) {
            g.drawImage(tileset, x, y, x + 32, y + 32,
                    mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
            g.drawImage(tileset, x, y - 32, x + 32, y,
                    mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
            try {
                thread.sleep(50);
            } catch (Exception ex) {
            }
        } else {
            switch (model.getRichting()) {
                case 0:
                    g.drawImage(tileset, x, y, x + 32, y + 32,
                            mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
                    break;
                case 1:
                    g.drawImage(tileset, x + 2 * count, y, x + 32 + 2 * count, y + 32,
                            mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
                    break;
                case 2:
                    g.drawImage(tileset, x, y + 2 * count, x + 32, y + 32 + 2 * count,
                            mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
                    break;
                case 3:
                    g.drawImage(tileset, x - 2 * count, y, x + 32 - 2 * count, y + 32,
                            mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
                    break;
                case 4:
                    g.drawImage(tileset, x, y - 2 * count, x + 32, y + 32 - 2 * count,
                            mx[charactergif1] * model.gettW(), my[charactergif1] * model.gettH(), mx[charactergif1] * model.gettW() + model.gettW(), my[charactergif1] * model.gettH() + model.gettH(), this);
                    break;
                default:
                    break;
            }

            //lichaam gebruik maken van myx[0] -> myx [2] (wandeled lichaam)

            switch (model.getRichting()) {
                case 0:
                    g.drawImage(tileset, x, y - 32, x + 32, y,
                            mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
                    break;
                case 1:
                    g.drawImage(tileset, x + 2 * count, y - 32, x + 32 + 2 * count, y,
                            mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
                    break;
                case 2:
                    g.drawImage(tileset, x, y - 32 + 2 * count, x + 32, y + 2 * count,
                            mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
                    break;
                case 3:
                    g.drawImage(tileset, x - 2 * count, y - 32, x + 32 - 2 * count, y,
                            mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
                    break;
                case 4:
                    g.drawImage(tileset, x, y - 32 - 2 * count, x + 32, y - 2 * count,
                            mx[charactergif2] * model.gettW(), my[charactergif2] * model.gettH(), mx[charactergif2] * model.gettW() + model.gettW(), my[charactergif2] * model.gettH() + model.gettH(), this);
                    break;
                default:
                    break;
            }
        }



    }

    // Teken de Char...
    public void tekenChar(Graphics g) {
        g.setColor(Color.GREEN);
        switch (model.getRichting()) {
            case 0:
                drawCharacter(g, onder2, model.getTilesetchar(), model.getCharLocatie_screen().x * model.gettW(), model.getCharLocatie_screen().y * model.gettH());
                break;
            case 1:
                drawCharacter(g, links2, model.getTilesetchar(), model.getCharLocatie_screen().x * model.gettW(), model.getCharLocatie_screen().y * model.gettH());
                break;
            case 2:
                drawCharacter(g, boven2, model.getTilesetchar(), model.getCharLocatie_screen().x * model.gettW(), model.getCharLocatie_screen().y * model.gettH());
                break;
            case 3:
                drawCharacter(g, rechts2, model.getTilesetchar(), model.getCharLocatie_screen().x * model.gettW(), model.getCharLocatie_screen().y * model.gettH());
                break;
            case 4:
                drawCharacter(g, onder2, model.getTilesetchar(), model.getCharLocatie_screen().x * model.gettW(), model.getCharLocatie_screen().y * model.gettH());
                break;
            default:
                break;

        }
    }

    // Teken de map...
    public void tekenMap(Graphics g) {

        int[][] map1 = model.getMap1();
        int[][] map2 = model.getMap2();
        int[][] map3 = model.getMap3();
        int width = model.getScreenwh().x;
        int height = model.getScreenwh().y;
        Image tileset = model.getTilesetmap();

        //layer1
        if (mapmovement == false) {
            switch (model.getRichting()) {
                case 0:
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            int mapy = j + model.getMapLocatie().y;
                            int mapx = i + model.getMapLocatie().x;
                            if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                                drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                            }
                        }
                    }
                case 1: // links
                    for (int i = 0; i < model.getScreenwh().x; i++) {
                        for (int j = 0; j < model.getScreenwh().y; j++) {
                            int mapy = j + model.getMapLocatie().y;
                            int mapx = i + model.getMapLocatie().x;
                            if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                                drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                try {
                                    if (i == model.getScreenwh().x - 1) {
                                        drawMap(g, map1[mapy][mapx + 1], i * model.gettW() + 32, j * model.gettH(), tileset);
                                        drawMap(g, map2[mapy][mapx + 1], i * model.gettW() + 32, j * model.gettH(), tileset);
                                        drawMap(g, map3[mapy][mapx + 1], i * model.gettW() + 32, j * model.gettH(), tileset);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    break;
                case 2: // boven
                    for (int i = 0; i < model.getScreenwh().x; i++) {
                        for (int j = 0; j < model.getScreenwh().y; j++) {
                            int mapy = j + model.getMapLocatie().y;
                            int mapx = i + model.getMapLocatie().x;
                            if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                                drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                try {
                                    if (j == model.getScreenwh().y - 1) {
                                        drawMap(g, map1[mapy + 1][mapx], i * model.gettW(), j * model.gettH() + 32, tileset);
                                        drawMap(g, map2[mapy + 1][mapx], i * model.gettW(), j * model.gettH() + 32, tileset);
                                        drawMap(g, map3[mapy + 1][mapx], i * model.gettW(), j * model.gettH() + 32, tileset);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    break;
                case 3: // rechts
                    for (int i = 0; i < model.getScreenwh().x; i++) {
                        for (int j = 0; j < model.getScreenwh().y; j++) {
                            int mapy = j + model.getMapLocatie().y;
                            int mapx = i + model.getMapLocatie().x;
                            if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                                drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                try {
                                    if (i == 0) {
                                        drawMap(g, map1[mapy][mapx - 1], i * model.gettW() - 32, j * model.gettH(), tileset);
                                        drawMap(g, map2[mapy][mapx - 1], i * model.gettW() - 32, j * model.gettH(), tileset);
                                        drawMap(g, map3[mapy][mapx - 1], i * model.gettW() - 32, j * model.gettH(), tileset);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    break;
                case 4: // onder
                    for (int i = 0; i < model.getScreenwh().x; i++) {
                        for (int j = 0; j < model.getScreenwh().y; j++) {
                            int mapy = j + model.getMapLocatie().y;
                            int mapx = i + model.getMapLocatie().x;
                            if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                                drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                                try {
                                    if (j == 0) {
                                        drawMap(g, map1[mapy - 1][mapx], i * model.gettW(), j * model.gettH() - 32, tileset);
                                        drawMap(g, map2[mapy - 1][mapx], i * model.gettW(), j * model.gettH() - 32, tileset);
                                        drawMap(g, map3[mapy - 1][mapx], i * model.gettW(), j * model.gettH() - 32, tileset);
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int mapy = j + model.getMapLocatie().y;
                    int mapx = i + model.getMapLocatie().x;
                    if (((mapx > -1) && (mapx < model.getMapwh().x)) && ((mapy > -1) && (mapy < model.getMapwh().y))) {
                        drawMap(g, map1[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                        drawMap(g, map2[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                        drawMap(g, map3[mapy][mapx], i * model.gettW(), j * model.gettH(), tileset);
                    }
                }
            }
        }
    }
}

// Controller beheerd keybaord

class Controller extends JPanel implements KeyListener, ActionListener {

    private Model model;
    private View view;
    private boolean mapmovement = false;
    private javax.swing.Timer timer;
    private javax.swing.Timer timer2;
    private int key;

    public Controller(Model model, View view) {

        this.model = model;
        this.view = view;

        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();

        timer = new javax.swing.Timer(10, this);
        timer2 = new javax.swing.Timer(500, this);
        timer2.start();

    }

    // timerhandler, alles wat hier in zit zal worden herhaald met delay van 50 ms
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timer) {
            if (view.getThreadsuspended() == false) {

                switch (key) {
                    case KeyEvent.VK_LEFT:
                        if (model.map_naarlinks() == false) // test errond omdat hij anders af en toe de map verplaatst en de character
                        {
                            model.char_naarlinks();
                            mapmovement = true;
                        }
                        view.startAnimatie(mapmovement);
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (model.map_naarrechts() == false) // test errond omdat hij anders af en toe de map verplaatst en de character
                        {
                            model.char_naarrechts();
                            mapmovement = true;
                        }
                        view.startAnimatie(mapmovement);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (model.map_naaronder() == false) // test errond omdat hij anders af en toe de map verplaatst en de character
                        {
                            model.char_naaronder();
                            mapmovement = true;
                        }
                        view.startAnimatie(mapmovement);
                        break;
                    case KeyEvent.VK_UP:
                        if (model.map_naarboven() == false) // test errond omdat hij anders af en toe de map verplaatst en de character
                        {
                            model.char_naarboven();
                            mapmovement = true;
                        }
                        view.startAnimatie(mapmovement);
                        break;

                }
            }
            mapmovement = false;
        }
        if (e.getSource() == timer2) {
            this.requestFocusInWindow();
            if (model.getBattle() == false) {
                view.unsetDamage();
                view.unsetHealing();
            }
        }

    }

    public void keyReleased(KeyEvent e) {

        if (model.getBattle() == false) {
            timer.stop();
            model.setBattleteller((int) (25 * Math.random()) + 7); // random getal tussen 7 en 25;
        }

    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        System.out.printf(e.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == KeyEvent.VK_F1) {
            view.showConsole();  // consoleoutput
        } else {
            key = e.getKeyCode();

            if (model.getBattle() == true) {
                timer.stop();
                if (model.stopBattle() == false) {
                    verwerkBattle();
                }
            } else {
                if ((e.getKeyCode() == KeyEvent.VK_F2) || (model.getMenu() == true)) {
                    verwerkMenu();
                } else {
                    timer.start();
                }
            }
        }
    }

    public void verwerkMenu() {

        if (model.getMenu() == false) {   // initialize menu
            model.setMenu(true);
            //model.loadMenu();
        } else {                            // verwerk veranderingen
            switch (key) {
                case KeyEvent.VK_DOWN:
                    //
                    break;
                case KeyEvent.VK_UP:
                    //
                    break;
                case KeyEvent.VK_F3:
                    model.setMenu(false);
                    break;
            }
        }
        view.repaint();

    }

    public void verwerkBattle() {

        if (key == KeyEvent.VK_SPACE) {
            view.unsetHealing();
            if (model.tryKill() == true) {
                view.startAnimatiebattle();
                model.setStopBattle();
                view.resetTeller();
                //view.unsetDamage();
            } else {
                if (model.ifDead() == true) {
                    System.out.println("You got killed");
                    long t0, t1;
                    t0 = System.currentTimeMillis();
                    do {
                        t1 = System.currentTimeMillis();
                    } while (t1 - t0 < 2000);
                    System.exit(1);
                }
                view.setDamage();
                view.startAnimatiebattle();
            }
        }
    }
}

// opgeslagen mappen worden geinstantieerd in het model, daarna worden de gegevens van 1 map ingeladen in het model zelf.
class Map {

    private String tileset;
    private String urlmap;
    private Point[] eventlocations;
    private boolean hostilearea;
    private int startlocationx;
    private int startlocationy;
    private Point mapwh;
    private int[][] map_1;
    private int[][] map_2;
    private int[][] map_3;
    private int[][] map_4;

    public Map(String tileset, String urlmap, int startlocationx, int startlocationy, boolean hostilearea, Point[] eventlocaties) {

        this.tileset = tileset;
        this.urlmap = urlmap;
        eventlocations = new Point[10];
        for (int i = 0; i < 10; i++) {
            eventlocations[i] = new Point(-1, -1);
        }

        System.arraycopy(eventlocaties, 0, eventlocations, 0, 10);

        for (int i = 0; i < 10; i++) {
            System.out.println("indemap" + eventlocaties[i].x);
            System.out.println("indemap" + eventlocaties[i].y);
        }

        this.hostilearea = hostilearea;        // monster encounters? NPC's?
        this.startlocationx = startlocationx;
        this.startlocationy = startlocationy;

        mapwh = new Point();

        try {
            openMap(this.urlmap);

        } catch (Exception e) {
            System.out.println("Error while opening map!");
        }

    }

    public Point getMapwh() {
        return mapwh;
    }

    public Point getStartposition() {
        Point start = new Point(startlocationx, startlocationy);
        return start;
    }

    public int[][] getLayer1() {
        return map_1;
    }

    public int[][] getLayer2() {
        return map_2;
    }

    public int[][] getLayer3() {
        return map_3;
    }

    public int[][] getCollision() {
        return map_4;
    }

    public boolean getHostilearea() {
        return hostilearea;
    }

    public String getTileset() {
        return tileset;
    }

    public Point[] getEventlocaties() {
        return eventlocations;
    }

    public void openMap(String mapurl) throws IOException {

        File file = new File(mapurl);

        FileReader txt = new FileReader(file);

        BufferedReader br = new BufferedReader(txt);

        char[] s;
        s = new char[150 * 150];

        br.read(s);

        String width;
        String heigth;

        width = "" + s[0] + s[1] + s[2] + s[3];
        heigth = "" + s[4] + s[5] + s[6] + s[7];

        System.out.printf(" --width_map:" + width + ":-- ");
        System.out.printf(" --heigth_map:" + heigth + ":-- ");

        this.mapwh.x = Integer.parseInt(width);
        this.mapwh.y = Integer.parseInt(heigth);

        map_1 = new int[this.mapwh.y][this.mapwh.x];
        map_2 = new int[this.mapwh.y][this.mapwh.x];
        map_3 = new int[this.mapwh.y][this.mapwh.x];
        map_4 = new int[this.mapwh.y][this.mapwh.x];

        int i = 8;    // start lezen van map op 8ste character
        int f = 8;    // start lezen van map op 8ste character
        String ii;
        do {
            int x = ((i - 8) / 4) % mapwh.x;    // character 8 moet op plaats 0 komen ( i(=8) - 8 op begin)
            int y = ((i - 8) / 4) / mapwh.x;    // /4 omdat de positiegetallen 4 breed zijn
            String temp = "" + s[i] + s[i + 1] + s[i + 2] + s[i + 3];
            map_1[y][x] = Integer.parseInt(temp);

            i += 4;  // 4 characters verder, 1 positiegetal verder
            f += 4;  // 4 characters verder, 1 positiegetal verder
            ii = "" + s[i];
        } while (ii.equals("*") == false);

        i += 1;
        f = 8;

        do {
            int x = ((f - 8) / 4) % mapwh.x;
            int y = ((f - 8) / 4) / mapwh.x;
            String temp = "" + s[i] + s[i + 1] + s[i + 2] + s[i + 3];
            map_2[y][x] = Integer.parseInt(temp);

            i += 4;
            f += 4;
            ii = "" + s[i];
        } while (ii.equals("$") == false);

        i += 1;
        f = 8;
        do {
            int x = ((f - 8) / 4) % mapwh.x;
            int y = ((f - 8) / 4) / mapwh.x;
            String temp = "" + s[i] + s[i + 1] + s[i + 2] + s[i + 3];
            map_3[y][x] = Integer.parseInt(temp);

            i += 4;
            f += 4;
            ii = "" + s[i];
        } while (ii.equals("^") == false);

        i += 1;
        f = 8;
        do {
            int x = ((f - 8) / 4) % mapwh.x;
            int y = ((f - 8) / 4) / mapwh.x;
            String temp = "" + s[i] + s[i + 1] + s[i + 2] + s[i + 3];
            map_4[y][x] = Integer.parseInt(temp);

            i += 4;
            f += 4;
            ii = "" + s[i];
        } while (ii.equals("µ") == false);
        System.out.println("Map opened succes!");
        txt.close();

    }
}

class ActionJList extends MouseAdapter {

    private JList list;
    private DefaultListModel listmodel;
    private Model model;
    private View view;

    public ActionJList(JList list, Model model, View view, DefaultListModel listmodel) {
        this.list = list;
        this.model = model;
        this.listmodel = listmodel;
        this.view = view;
    }

    public ActionJList(JList list, Model model, View view) {
        this.list = list;
        this.model = model;
        this.view = view;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listmodel == null) {
            if (e.getClickCount() == 2) {
                int index = list.locationToIndex(e.getPoint());
                list.ensureIndexIsVisible(index);
                if (index == 0) { // attack
                    if (model.stopBattle() == false) {
                        view.unsetHealing();
                        if (model.tryKill() == true) {
                            view.startAnimatiebattle();
                            model.setStopBattle();
                            view.resetTeller();
                        } else {
                            if (model.ifDead() == true) {
                                System.out.println("You got killed");
                                long t0, t1;
                                t0 = System.currentTimeMillis();
                                do {
                                    t1 = System.currentTimeMillis();
                                } while (t1 - t0 < 2000);
                                System.exit(1);
                            }
                            view.setDamage();
                            view.startAnimatiebattle();
                        }
                    }
                }
                if (index == 1) { // potion
                    boolean alreadyhadpotion = false;
                    Potion temp1 = new Potion("Empty", 0, 0, 0, "Empty Potion");
                    for (Items p : model.getCharacter().getInventory()) {
                        if (p instanceof Potion) {
                            if (alreadyhadpotion == false) {
                                temp1 = (Potion) p;
                                alreadyhadpotion = true;
                            }
                        }
                    }
                    if (temp1.getRestore() != 0) {
                        model.getCharacter().hitpoints += temp1.getRestore();
                        model.getCharacter().getInventory().remove(index);
                        if (model.getCharacter().hitpoints > model.getCharacter().max_hitpoints) {
                            model.getCharacter().hitpoints = model.getCharacter().max_hitpoints;
                        }
                        view.unsetDamage();
                        view.setHealing();
                        model.setHealingpoints(temp1.getRestore());
                        view.startAnimatiebattle();
                    }
                    if (alreadyhadpotion == false) {
                        JOptionPane.showMessageDialog(null, "You don't have any potions!", "Bad luck!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } else {
            if (e.getClickCount() == 2) {
                int index = list.locationToIndex(e.getPoint());
                list.ensureIndexIsVisible(index);
                if (model.getInventory().get(index) instanceof Potion) {
                    int antwoord = JOptionPane.showConfirmDialog(null, "Wil je een " + listmodel.getElementAt(index) + " gebruiken om je te herstellen?", "Ben je wel zeker?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (antwoord == JOptionPane.YES_OPTION) {

                        Potion temp1 = (Potion) model.getCharacter().getInventory().get(index);
                        model.getCharacter().hitpoints += temp1.getRestore();
                        listmodel.remove(index);
                        model.getCharacter().getInventory().remove(index);
                        if (model.getCharacter().hitpoints > model.getCharacter().max_hitpoints) {
                            model.getCharacter().hitpoints = model.getCharacter().max_hitpoints;
                        }
                    }
                } else {
                    int antwoord = JOptionPane.showConfirmDialog(null, "Wil je je " + listmodel.getElementAt(index) + " gebruiken in een gevecht?", "Ben je wel zeker?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (antwoord == JOptionPane.YES_OPTION) {
                        model.getCharacter().eQuip((Items) model.getInventory().get(index));
                    }
                }
            }

        }
        view.repaint();
    }
}

class JListExtended extends JList {

    Model model;

    public JListExtended(DefaultListModel listmodel, Model model) {
        super(listmodel);
        this.model = model;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        int index = locationToIndex(e.getPoint());
        Items item = (Items) model.getCharacter().getInventory().get(index);
        return item.tooltipdescription;
    }

    @Override
    public Point getToolTipLocation(MouseEvent e) {
        Point p = new Point();
        p.x = 15;
        p.y = 16 * locationToIndex(e.getPoint()) + 16;
        return p;
    }

    @Override
    public JToolTip createToolTip() {
        JToolTip tooltip = new JToolTip();
        tooltip.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, new Color(73, 72, 9)));
        tooltip.setBackground(new Color(98, 97, 11));
        return tooltip;
    }
}
