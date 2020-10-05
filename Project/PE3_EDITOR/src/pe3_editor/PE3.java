package pe3_editor;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
// import java.util.*; // arraylists
import java.awt.event.*; // events
import java.awt.geom.*;

//file imports

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;

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
        this.setSize(1500, 900);
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    }
}

class Paneel extends JPanel {

    private Model model;
    private View view;
    private View2 view2;
    private Controller controller;
    private JScrollPane scrollpane;
    private JScrollPane scrollpane2;

    public Paneel() {

        setLayout(new BorderLayout());
        model = new Model();
        view = new View(model);
        view.setPreferredSize(new Dimension(32 * 150, 32 * 150));  // scrollpane is groot genoeg voor maximale map van 150x150
        scrollpane = new JScrollPane(view);
        view2 = new View2(model);
        view2.setPreferredSize(new Dimension(32 * 13, 17036));  // scrollpane is groot genoeg voor maximale map van 150x150
        scrollpane2 = new JScrollPane(view2);
        controller = new Controller(model, view, view2);
        controller.setPreferredSize(new Dimension(0, 140));
        add(scrollpane, BorderLayout.CENTER);
        add(scrollpane2, BorderLayout.WEST);
        add(controller, BorderLayout.SOUTH);

    }
}

class Model {

    private Point mapwh;
    private Point screenwh;
    private Image tileset;
    private int[][] map1;
    private int[][] map2;
    private int[][] map3;
    private int[][] map4;
    private int zoom;
    private int tW;
    private int tH;
    private Point muistileset;
    private Point muismap;
    private boolean raster;
    private int layer; // layer select layer 1 -> 2 -> 3

    public Model() {

        mapwh = new Point();
        screenwh = new Point();
        muistileset = new Point();
        muismap = new Point();

    }

    //getters
    public Point getMapwh() {
        return mapwh;
    }

    public Image getTileset() {
        return tileset;
    }

    public int gettW() {
        return tW;
    }

    public int gettH() {
        return tH;
    }

    public int[][] getMap1() {
        return map1;
    }

    public int[][] getMap2() {
        return map2;
    }

    public int[][] getMap3() {
        return map3;
    }

    public int[][] getMap4() {
        return map4;
    }

    public int getZoom() {
        return zoom;
    }

    public Point getMousetileset() {
        return muistileset;
    }

    public Point getMousemap() {
        return muismap;
    }

    public boolean getRaster() {
        return raster;
    }

    public int getLayer() {
        return layer;
    }

    //setters
    public void setMap(int[][] map1, int[][] map2, int[][] map3, int[][] map4) {
        this.map1 = map1;
        this.map2 = map2;
        this.map3 = map3;
        this.map4 = map4;
    }

    public void setMapwh(Point mapwh) {
        this.mapwh = mapwh;
        // breedte screen wordt normaal niet verandert
        screenwh.x = mapwh.x;
        screenwh.y = mapwh.y;
    }

    public void setTileset(Image tileset) {
        this.tileset = tileset;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public void setTiles(int tW, int tH) {
        this.tW = tW;
        this.tH = tH;
    }

    public void setMousetileset(Point muistileset) {
        this.muistileset = muistileset;
    }

    public void setMousemap(Point muismap) {
        this.muismap = muismap;
    }

    public void setRaster(boolean raster) {
        this.raster = raster;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}

class View extends JPanel {

    private Model model;
    private int tW;
    private int tH;
    //private BufferedImage rectsImage;
    private Graphics g2;
    private Image backbuffer;
    private ArrayList<Punten> geselecteerde_tiles;
    private Punten punt;

    public View(Model model) {
        this.model = model;
        geselecteerde_tiles = new ArrayList<Punten>();
        upDate();
    }

    public void rePaint() {

        if (this.isDisplayable() == true) {

            tW = model.gettW();
            tH = model.gettH();

            //int w = component_width, h = component_height;
            //int type = BufferedImage.TYPE_INT_ARGB_PRE;
            //rectsImage = new BufferedImage(32 * tW,32 * tH, type);

            backbuffer = createImage(32 * 70, 32 * 55);
            g2 = backbuffer.getGraphics();

            //Graphics g2 = rectsImage.createGraphics();

            // draw your rectangles here using g2...
            kleurIn(g2);
            tekenMap(g2);


            if (model.getRaster() == true) {

                for (int i = 0; i <= model.getMapwh().x; i++) {
                    g2.drawLine(i * tW, 0, i * tW, tW * model.getMapwh().y);
                }

                for (int ii = 0; ii <= model.getMapwh().y; ii++) {
                    g2.drawLine(0, ii * tH, tH * model.getMapwh().x, ii * tH);
                }

            }

            repaint();

            g2.dispose();
            // make this image once, keep it as a member variable and draw it in your
            // paintComponent method


        }



    }

    @Override
    public void paintComponent(Graphics g) {

        rePaint();
        //g.drawImage(img, 0, 0, null);

        g.drawImage(backbuffer, 0, 0, this);

        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, getWidth(), getHeight());
        //g.setColor(Color.BLACK);



    }

    public void drawTile(Graphics g, int t, int x, int y) {

        int mx = t % 12;
        int my = t / 12;
        g.drawImage(model.getTileset(), x, y, x + tW, y + tH,
                mx * tW, my * tH, mx * tW + tW, my * tH + tH, this);

    }

    public void drawWalkable(Graphics g, int x, int y) {

        g.drawImage( new ImageIcon("Images/Levels/walk.png").getImage(), x, y,this);

    }

    public void drawNonwalkable(Graphics g, int x, int y) {

        g.drawImage( new ImageIcon("Images/Levels/nonwalk.png").getImage(), x, y,this);

    }

    public void tekenMap(Graphics g) {

        for (int i = 0; i < model.getMapwh().x; i++) {
            for (int j = 0; j < model.getMapwh().y; j++) {
                drawTile(g, model.getMap1()[j][i], i * tW, j * tH);
                drawTile(g, model.getMap2()[j][i], i * tW, j * tH);
                drawTile(g, model.getMap3()[j][i], i * tW, j * tH);
                if ( model.getLayer() == 4) {
                    if ( model.getMap4()[j][i] == 0000 ) {
                    drawWalkable(g, i * tW, j * tH);
                    } else {
                        drawNonwalkable(g, i * tW, j *tH);
                    }
                }
            }
        }

    }

    public void kleurIn(Graphics g) {

        Punten basis = geselecteerde_tiles.get(0);

        for (Punten p : geselecteerde_tiles) { // teken tegels
            if (model.getLayer() == 1) {                                    // teken layer 1 tegel
                model.getMap1()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())] = (12 * (int) p.getY() + (int) p.getX());
                drawTile(g, model.getMap1()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())], (model.getMousemap().x + ((int) p.getX() - (int) basis.getX())) * tW, (model.getMousemap().y + ((int) p.getY() - (int) basis.getY())) * tH);
            }
            if (model.getLayer() == 2) {                                    // teken layer 2 tegel
                model.getMap2()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())] = (12 * (int) p.getY() + (int) p.getX());
                drawTile(g, model.getMap2()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())], (model.getMousemap().x + ((int) p.getX() - (int) basis.getX())) * tW, (model.getMousemap().y + ((int) p.getY() - (int) basis.getY())) * tH);
            }
            if (model.getLayer() == 3) {                                    // teken layer 3 tegel
                model.getMap3()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())] = (12 * (int) p.getY() + (int) p.getX());
                drawTile(g, model.getMap3()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())], (model.getMousemap().x + ((int) p.getX() - (int) basis.getX())) * tW, (model.getMousemap().y + ((int) p.getY() - (int) basis.getY())) * tH);
            }
            if (model.getLayer() == 4) {                                    // teken layer 4 "tegel"
                model.getMap4()[model.getMousemap().y + ((int) p.getY() - (int) basis.getY())][model.getMousemap().x + ((int) p.getX() - (int) basis.getX())] = 1111;
                //niets tekenen want er mag niet op gelopen worden
                drawNonwalkable(g, (int) p.getX() * tW,(int) p.getY() * tH);
            }
        }
        model.setMousemap(new Point(100, 100));
    }

    public void clearTiles() {
        geselecteerde_tiles.clear();
    }

    public void upDate() {
        if (punt == null) {

            punt = new Punten(model.getMousetileset().x, model.getMousetileset().y);

        } else {
            punt.setLocation(model.getMousetileset().x, model.getMousetileset().y);
        }

        if (punt != null) {

            Punten p2 = new Punten(punt.getX(), punt.getY());
            geselecteerde_tiles.add(p2);
        }

        for (Punten p : geselecteerde_tiles) {
            System.out.println("" + p.getX() + " " + p.getY());
        }

    }
}

class View2 extends JPanel {

    private ArrayList<Punten> geselecteerde_tiles;
    private Punten punt;
    private Model model;
    private Image img;
    private Image tileset;

    public View2(Model model) {
        this.model = model;
        geselecteerde_tiles = new ArrayList<Punten>();
    }

    public void clearTiles() {
        geselecteerde_tiles.clear();
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, this);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);

        g.drawImage(tileset, 0, 0, this);

        int tW = model.gettW();
        int tH = model.gettH();

        for (int ii = 0; ii <= (model.getTileset().getHeight(this) / 32); ii++) {
            g.drawLine(0, ii * tH, tH * model.getTileset().getWidth(this), ii * tH);
        }

        if (punt == null) {

            punt = new Punten(model.getMousetileset().x, model.getMousetileset().y);

        } else {
            punt.setLocation(model.getMousetileset().x, model.getMousetileset().y);
        }

        if (punt != null) {

            for (Punten p : geselecteerde_tiles) {
                System.out.println("teken");
                p.teKen(g);
            }
            punt.teKen(g);

            Punten p2 = new Punten(punt.getX(), punt.getY());
            geselecteerde_tiles.add(p2);
        }

    }

    public void setTileset(Image tileset) {
        this.tileset = tileset;
    }
}

class Punten extends Point2D {

    private double x;
    private double y;
    private int tH = 32;
    private int tW = 32;

    public Punten(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void teKen(Graphics g) {

        //linkerkant vierkant
        g.drawLine((int) x * tW, (int) y * tH, ((int) x * tW), (int) y * tH + tH);
        //bovenkant vierkant
        g.drawLine((int) x * tW, ((int) y * tH), ((int) x * tW) + tW, (int) y * tH);
        //onderkant vierkant
        g.drawLine((int) x * tW, ((int) y * tH + tH), ((int) x * tW) + tW, ((int) y * tH) + tH);
        //rechterkant vierkant
        g.drawLine((int) x * tW + tW, (int) y * tH, ((int) x * tW) + tW, ((int) y * tH) + tH);

        //linkerkant2 vierkant
        g.drawLine((int) x * tW + tW - 5, (int) y * tH, ((int) x * tW) + tW - 5, ((int) y * tH) + tH);
        //rechterkant2 vierkant
        g.drawLine((int) x * tW + 5, (int) y * tH, ((int) x * tW) + 5, ((int) y * tH) + tH);

    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }
}

class Controller extends JPanel implements ActionListener, MouseListener, ItemListener, MouseMotionListener, KeyListener {

    private static final int tW = 32; // tile width
    private static final int tH = 32; // tile heigth
    private Model model;
    private View view;
    private View2 view2;
    private ButtonGroup layers;
    private JRadioButton layer1;
    private JRadioButton layer2;
    private JRadioButton layer3;
    private JRadioButton layer4;
    private JLabel label_raster;
    private JCheckBox insert_raster;
    private JLabel label_zoom;
    private JTextField insert_zoom;
    private JLabel label_breedte;
    private JTextField insert_breedte;
    private JLabel label_hoogte;
    private JTextField insert_hoogte;
    private JLabel label_tileset;
    private JTextField insert_tileset;
    private JLabel label_openen;
    private JTextField insert_openen;
    private JLabel label_opslaan;
    private JTextField insert_opslaan;
    private JButton opslaan;
    private JButton openen;
    private int zoom;
    private Point mapwh;
    private JButton bereken;
    private int[][] map1;
    private int[][] map2;
    private int[][] map3;
    private int[][] map4;
    private Image tileset;
    private Point muistileset;
    private Point muismap;
    private boolean raster;
    private JButton clear;
    private boolean shift;

    public Controller(Model model, View view, View2 view2) {

        this.model = model;
        this.view = view;
        this.view2 = view2;

        map1 = new int[150][150];
        map2 = new int[150][150];
        map3 = new int[150][150];
        map4 = new int[150][150];

        mapwh = new Point();

        muismap = new Point(149, 149);
        model.setMousemap(muismap);
        muistileset = new Point();

        this.setBackground(Color.red);

        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();

        zoom = 1;

        mapwh.x = 25;
        mapwh.y = 21;

        layer1 = new JRadioButton("Layer 1", true);
        layer1.addItemListener(this);
        layer2 = new JRadioButton("Layer 2");
        layer2.addItemListener(this);
        layer3 = new JRadioButton("Layer 3");
        layer3.addItemListener(this);
        layer4 = new JRadioButton("Layer 4 - Walkable");
        layer4.addItemListener(this);

        layers = new ButtonGroup();

        layers.add(layer1);
        layers.add(layer2);
        layers.add(layer3);
        layers.add(layer4);

        bereken = new JButton("Process!");
        bereken.addActionListener(this);
        opslaan = new JButton("Opslaan!");
        opslaan.addActionListener(this);
        openen = new JButton("Openen!");
        openen.addActionListener(this);

        label_zoom = new JLabel("Zoom factor(even) :");
        label_breedte = new JLabel("Breedte(max 150) :");
        label_hoogte = new JLabel("Hoogte(max 150) :");
        label_tileset = new JLabel("URL tileset (projectmap = root) :");
        label_raster = new JLabel("Raster weergeven :");
        label_opslaan = new JLabel("Sla map op in(url) :");
        label_openen = new JLabel("Open map (url) :");

        insert_zoom = new JTextField(5);
        insert_zoom.setText("1");
        insert_breedte = new JTextField(10);
        insert_breedte.setText("25");
        insert_hoogte = new JTextField(10);
        insert_hoogte.setText("21");
        insert_tileset = new JTextField(50);
        insert_tileset.setText("Images/Levels/outdoortileset.png");
        insert_raster = new JCheckBox("", true);
        insert_raster.addActionListener(this);
        insert_opslaan = new JTextField(50);
        insert_opslaan.setText("Images/Levels/map.txt");
        insert_openen = new JTextField(50);
        insert_openen.setText("Images/Levels/map.txt");

        Color darkred = new Color(255,100,100);
        Color darkred2 = new Color(255,150,50);
        layer1.setBackground(darkred);
        layer2.setBackground(darkred);
        layer3.setBackground(darkred);
        layer4.setBackground(darkred2);

        clear = new JButton("Clear!");
        clear.addActionListener(this);

        add(layer1);
        add(layer2);
        add(layer3);
        add(layer4);

        add(label_raster);
        add(insert_raster);

        //add(clear);

        //add(label_zoom);
        //add(insert_zoom);
        add(label_breedte);
        add(insert_breedte);
        add(label_hoogte);
        add(insert_hoogte);
        add(label_tileset);
        add(insert_tileset);
        add(bereken);

        add(label_opslaan);
        add(insert_opslaan);
        add(opslaan);

        add(label_openen);
        add(insert_openen);
        add(openen);

        raster = insert_raster.isSelected();

        // standaard tileseturl

        tileset = new ImageIcon(insert_tileset.getText()).getImage();

        model.setTiles(tW, tH);
        model.setLayer(1);

        // ik vul alles met gras zodat men OOK gras krijgt als men de map vergroot.

        for (int i = 0; i < 150; i++) {
            for (int j = 0; j < 150; j++) {
                map1[j][i] = 17;
                map2[j][i] = 11;
                map3[j][i] = 11;
                map4[j][i] = 0000;
            }
        }

        view2.setTileset(tileset);

        view2.addMouseListener(this);
        view.addMouseListener(this);
        view.addMouseMotionListener(this);

        verStuurraster();
        verStuur();
    }

    public void keyReleased(KeyEvent e) {
        if ( e.getKeyCode() == KeyEvent.VK_SHIFT )
            shift = false;
    }

    public void keyPressed(KeyEvent e) {
        if ( e.getKeyCode() == KeyEvent.VK_SHIFT )
            shift = true;
    }
    public void keyTyped(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        int X = e.getX() / tW;
        int Y = e.getY() / tH;
        muismap.setLocation(X, Y);
        model.setMousemap(muismap);
        view.repaint();
        System.out.println("Mousedragged");

    }

    public void mousePressed(MouseEvent e) {

        int X = e.getX() / tW;
        int Y = e.getY() / tH;
        System.out.println(" --x:" + X + ":-- " + " --y:" + Y + ":-- ");

        if (e.getSource() == view2) {

            if ( shift == false) {
                view.clearTiles();
                view2.clearTiles();
            }
            muistileset.setLocation(X, Y);
            model.setMousetileset(muistileset);
            view.upDate();
            view2.repaint();

        }

        if (e.getSource() == view) {

            muismap.setLocation(X, Y);
            model.setMousemap(muismap);
            view.repaint();
        }


    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        this.requestFocusInWindow();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == bereken) {

            zoom = Integer.parseInt(insert_zoom.getText());
            mapwh.x = Integer.parseInt(insert_breedte.getText());
            mapwh.y = Integer.parseInt(insert_hoogte.getText());
            tileset = new ImageIcon(insert_tileset.getText()).getImage();

            view2.setTileset(tileset);


            verStuur();

        }

        if (e.getSource() == insert_raster) {
            raster = insert_raster.isSelected();
            verStuurraster();
        }

        if (e.getSource() == opslaan) {
            try {
                slaOp();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error while saving map.", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == openen) {
            try {
                openMap();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error while opening map.", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == clear) {
            view2.clearTiles();
            view.clearTiles();
            view.upDate();
            view2.repaint();
        }

    }

    public void itemStateChanged(ItemEvent e) {

        int layer = 1;
        if (layer1.isSelected() == true) {
            System.out.println(" --Layer 1 selected-- ");
        }
        if (layer2.isSelected() == true) {
            layer = 2;
            System.out.println(" --Layer 2 selected-- ");
        }
        if (layer3.isSelected() == true) {
            layer = 3;
            System.out.println(" --Layer 3 selected-- ");
        }
        if (layer4.isSelected() == true) {
            layer = 4;
            System.out.println(" --Layer 4 selected-- ");
        }

        model.setLayer(layer);

        view.repaint();

    }

    public void verStuur() {

        model.setMap(map1, map2, map3, map4);
        model.setMapwh(mapwh);
        model.setTileset(tileset);
        model.setZoom(zoom);

        view.repaint();
        view2.repaint();
    }

    public void verStuurraster() {

        model.setRaster(raster);

        view.repaint();
        view2.repaint();
    }

    public void slaOp() throws IOException {

        File file = new File(insert_opslaan.getText());

        FileWriter txt = new FileWriter(file);
        PrintWriter out = new PrintWriter(txt);

        if (mapwh.x < 10) {
            out.print("000" + mapwh.x);
        } else {
            if (mapwh.x < 100) {
                out.print("00" + mapwh.x);
            } else {
                if (mapwh.y < 1000) {
                    out.print("0" + mapwh.y);
                } else {
                    {
                        out.print(mapwh.y);
                    }
                }
            }
        }

        if (mapwh.y < 10) {
            out.print("000" + mapwh.y);
        } else {
            if (mapwh.y < 100) {
                out.print("00" + mapwh.y);
            } else {
                if (mapwh.y < 1000) {
                    out.print("0" + mapwh.y);
                } else {
                    {
                        out.print(mapwh.y);
                    }
                }
            }
        }

        for (int i = 0; i < mapwh.y; i++) {
            for (int j = 0; j < mapwh.x; j++) {
                if (map1[i][j] < 10) {
                    out.print("000" + map1[i][j]);
                } else {
                    if (map1[i][j] < 100) {
                        out.print("00" + map1[i][j]);
                    } else {
                        if (map1[i][j] < 1000) {
                            out.print("0" + map1[i][j]);
                        } else {
                            out.print(map1[i][j]);
                        }
                    }
                }
            }
        }

        out.print("*");

        for (int i = 0; i < mapwh.y; i++) {
            for (int j = 0; j < mapwh.x; j++) {
                if (map2[i][j] < 10) {
                    out.print("000" + map2[i][j]);
                } else {
                    if (map2[i][j] < 100) {
                        out.print("00" + map2[i][j]);
                    } else {
                        if (map2[i][j] < 1000) {
                            out.print("0" + map2[i][j]);
                        } else {
                            out.print(map2[i][j]);
                        }
                    }
                }
            }
        }

        out.print("$");

        for (int i = 0; i < mapwh.y; i++) {
            for (int j = 0; j < mapwh.x; j++) {
                if (map3[i][j] < 10) {
                    out.print("000" + map3[i][j]);
                } else {
                    if (map3[i][j] < 100) {
                        out.print("00" + map3[i][j]);
                    } else {
                        if (map3[i][j] < 1000) {
                            out.print("0" + map3[i][j]);
                        } else {
                            out.print(map3[i][j]);
                        }
                    }
                }
            }
        }

        out.print("^");

        for (int i = 0; i < mapwh.y; i++) {
            for (int j = 0; j < mapwh.x; j++) {
                if ( map4[i][j] == 1111) {
                    out.print("1111");
                } else {
                    out.print("0000");
                }
            }
        }

        out.print("µ");

        out.close();

    }

    public void openMap() throws IOException {

        File file = new File(insert_openen.getText());

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
        insert_breedte.setText("" + this.mapwh.x);
        this.mapwh.y = Integer.parseInt(heigth);
        insert_hoogte.setText("" + this.mapwh.y);

        int i = 8;    // start lezen van map op 8ste character
        int f = 8;    // start lezen van map op 8ste character
        String ii;
        do {
            int x = ((i - 8) / 4) % mapwh.x;    // character 8 moet op plaats 0 komen ( i(=8) - 8 op begin)
            int y = ((i - 8) / 4) / mapwh.x;    // /4 omdat de positiegetallen 4 breed zijn
            String temp = "" + s[i] + s[i + 1] + s[i + 2] + s[i + 3];
            map1[y][x] = Integer.parseInt(temp);

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
            map2[y][x] = Integer.parseInt(temp);

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
            map3[y][x] = Integer.parseInt(temp);

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
            map4[y][x] = Integer.parseInt(temp);

            i += 4;
            f += 4;
            ii = "" + s[i];
        } while (ii.equals("µ") == false);

        txt.close();

        view.repaint();
    }
}


