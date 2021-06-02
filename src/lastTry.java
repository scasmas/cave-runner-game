import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;

public class lastTry extends PApplet {

    /*
    CaveRunner by Justas Stonkus Software engineering 4th group.

    Use arrow keys to move and space to shoot.
    */
    JSONObject json;
    int rows = 25;
    int cols = 96;
    int tilesize = 32;
    int[] map1 = new int[rows * cols];//Tile ID Storage
    int[] map2 = new int[rows * cols];//Tile ID Storage
    int[] map3 = new int[rows * cols];//Tile ID Storage
    PImage tileset;
    PImage[] tiles = new PImage[70];//Tileset tile storage
    int mode = 0;
    Player u;
    boolean left, right, up, down, space;
    FrameObject camera, gameWorld;
    ImageObject backImage;
    boolean[] enCreated = new boolean[3];
    PImage backgrnd;
    PImage[] spriteImages;
    int totalsprites;
    PFont font;
    Platform[] temp_platforms = new Platform[rows * cols];
    Platform[] platforms1;
    Platform[] platforms2;
    Platform[] platforms3;
    Button playButton;
    Button restartButton;
    Button editButton;
    Button[] placingButton;
    Button[] mapButton;
    Button[] tileButton;
    Button backButton;
    Timer firingTimer;
    Bullet[] bullets;
    int nextBullet;
    PImage selected;
    int selectedID;
    int selectedMap = 1;
    int[] selectedM;
    int x = 25;
    boolean blink;
    int enemyCount = 5;
    Enemy[] enemy = new Enemy[enemyCount];
    PImage[] enemySprites;
    PImage[] fireballSprites;
    Button controlButton;
    JFrame controlFrame;

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"lastTry"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    public void setup() {
        enCreated[0] = false;
        enCreated[1] = false;
        enCreated[2] = false;
        left = false;
        right = false;
        up = false;
        down = false;
        space = false;
        loadJSON("map1.json", map1);
        loadJSON("map2.json", map2);
        loadJSON("map3.json", map3);
        getTiles();

        backgrnd = loadImage("background.jpg");
        backImage = new ImageObject(0, 0, 1024, 800, backgrnd);
        gameWorld = new FrameObject(0, 0, backImage.w * 3, backImage.h);
        camera = new FrameObject(0, 0, width, height);
        font = createFont("Boxy-Bold.ttf", 25);
        textFont(font);
        camera.x = (gameWorld.x + gameWorld.w / 2) - camera.w / 2;
        camera.y = (gameWorld.y + gameWorld.h / 2) - camera.h / 2;
        playButton = new Button(width / 2, height / 2, 200, 75, "play");
        controlButton = new Button(width / 2, height / 2 + 200, 200, 75, "control");
        restartButton = new Button(width / 2, height / 2, 200, 75, "restart");
        editButton = new Button(width / 2, height / 2 + 100, 200, 75, "edit");
        backButton = new Button(width - 150, 700, 200, 75, "back");
        mapButton = new Button[3];
        mapButton[0] = new Button(150, 500, 200, 75, "map1");
        mapButton[1] = new Button(150, 600, 200, 75, "map2");
        mapButton[2] = new Button(150, 700, 200, 75, "map3");
        u = new Player();
        loadPlatforms();


        placingButton = new Button[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                placingButton[i * cols + j] = new Button(22 + j * 12 + 6, i * 12 + 6 + 50, 12, 12, " ");
            }
        }
        tileButton = new Button[12];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != 3)
                    tileButton[i * 4 + j] = new Button(width / 2 - 128 + j * 64 + 32, height / 2 + i * 64 + 32 + 128, 64, 64, tiles[30 + j + 10 * i], 30 + j + 10 * i);
                else
                    tileButton[i * 4 + j] = new Button(width / 2 - 128 + j * 64 + 32, height / 2 + i * 64 + 32 + 128, 64, 64, tiles[30 + 6 + 10 * i], 30 + j + 10 * i);
            }
        }


        totalsprites = 64;
        spriteImages = new PImage[totalsprites];
        PImage playerSpriteSheet = loadImage("playerSpriteSheet.png");
        int playerSpriteWidth = playerSpriteSheet.width / 8;
        int playerSpriteHeight = playerSpriteSheet.height / 8;
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                spriteImages[index] = playerSpriteSheet.get(j * playerSpriteWidth, i * playerSpriteHeight, playerSpriteWidth, playerSpriteHeight);
                index++;
            }
        }
        totalsprites = 12;
        enemySprites = new PImage[totalsprites];
        PImage enemySpriteSheet = loadImage("chicken.png");
        int enemySpriteWidth = enemySpriteSheet.width / 3;
        int enemySpriteHeight = enemySpriteSheet.height / 4;
        index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                enemySprites[index] = enemySpriteSheet.get(j * enemySpriteWidth, i * enemySpriteHeight, enemySpriteWidth, enemySpriteHeight);
                index++;
            }
        }
        totalsprites = 30;
        fireballSprites = new PImage[totalsprites];
        enemySpriteSheet = loadImage("fireball.png");
        enemySpriteWidth = enemySpriteSheet.width / 3;
        enemySpriteHeight = enemySpriteSheet.height / 10;
        index = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                fireballSprites[index] = enemySpriteSheet.get(j * enemySpriteWidth, i * enemySpriteHeight, enemySpriteWidth, enemySpriteHeight);
                index++;
            }
        }
        bullets = new Bullet[100];
        for (int i = 0; i < bullets.length; ++i) {
            bullets[i] = new Bullet();
        }
        nextBullet = 0;

        firingTimer = new Timer(500);
        firingTimer.start();//shift to appropriate state function once states are implemented
        x = 25;

    }
    public void createIt(String code) {
        try {
            FileWriter aWriter = new FileWriter("userInput.java", false);
            aWriter.write("import java.awt.*;\n" +
                    "import java.awt.event.KeyEvent;\n" +
                    "import java.util.concurrent.TimeUnit;\n" +
                    "\n" +
                    "public class userInput {\n" +
                    "    static Robot rbt;\n" +
                    "\n" +
                    "    public static void main(String[] args) throws InterruptedException {\n" +
                    "\n" +
                    "            try {\n" +
                    "                rbt = new Robot();\n" +
                    "            } catch (AWTException e) {\n" +
                    "                e.printStackTrace();\n" +
                    "            }\n");
            aWriter.write(code);
            aWriter.write("}\n" +
                    "    static void kaire(int x) throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_LEFT);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_LEFT);\n" +
                    "    }\n" +
                    "    static void desine(int x) throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_RIGHT);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_RIGHT);\n" +
                    "    }\n" +
                    "    static void sokti() throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_UP);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_UP);\n" +
                    "    }\n" +
                    "    static void soktiDesinen(int x) throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_RIGHT);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x/2);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_RIGHT);\n" +
                    "        rbt.keyPress(KeyEvent.VK_UP);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_UP);\n" +
                    "        rbt.keyPress(KeyEvent.VK_RIGHT);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x/2);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_RIGHT);\n" +
                    "    }\n" +
                    "    static void soktiKairen(int x) throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_LEFT);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x/2);\n" +
                    "        rbt.keyPress(KeyEvent.VK_UP);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_UP);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x/2);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_LEFT);\n" +
                    "    }\n" +
                    "    static void sauti() throws InterruptedException {\n" +
                    "        rbt.keyPress(KeyEvent.VK_SPACE);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_SPACE);\n" +
                    "    }\n" +
                    "    static void pereitiPirma() throws InterruptedException {\n" +
                    "        laukti(2);\n" +
                    "        soktiDesinen(400);\n" +
                    "        laukti(2);\n" +
                    "        sokti();\n" +
                    "        desine(250);\n" +
                    "        laukti(2);\n" +
                    "        soktiDesinen(450);\n" +
                    "        laukti(2);\n" +
                    "        soktiDesinen(400);\n" +
                    "        laukti(2);\n" +
                    "        sokti();\n" +
                    "        desine(1500);\n" +
                    "        laukti(2);\n" +
                    "        sauti();\n" +
                    "        desine(2250);\n" +
                    "        sauti();\n" +
                    "        sokti();\n" +
                    "        kaire(100);\n" +
                    "        desine(200);\n" +
                    "        laukti(2);\n" +
                    "        sokti();\n" +
                    "        desine(1000);\n" +
                    "        laukti(2);\n" +
                    "        rbt.keyPress(KeyEvent.VK_X);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_X);\n" +
                    "    }\n" +
                    "    static void pereitiAntra() throws InterruptedException {\n" +
                    "        laukti(1);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(40);\n" +
                    "        sauti();\n" +
                    "        laukti(2);\n" +
                    "        kaire(200);\n" +
                    "        laukti(1);\n" +
                    "        desine(200);\n" +
                    "        sauti();\n" +
                    "        sokti();\n" +
                    "        desine(2000);\n" +
                    "        sauti();\n" +
                    "        soktiDesinen(1000);\n" +
                    "        soktiDesinen(800);\n" +
                    "        desine(3000);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        desine(200);\n" +
                    "        laukti(2);\n" +
                    "        soktiKairen(500);\n" +
                    "        kaire(100);\n" +
                    "        laukti(2);\n" +
                    "        desine(20);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        sauti();\n" +
                    "        laukti(2);\n" +
                    "        desine(50);\n" +
                    "        sokti();\n" +
                    "        desine(2000);\n" +
                    "        laukti(2);\n" +
                    "        rbt.keyPress(KeyEvent.VK_X);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_X);\n" +
                    "    }\n" +
                    "    static void pereitiTrecia() throws InterruptedException {\n" +
                    "        laukti(1);\n" +
                    "        desine(100);\n" +
                    "        laukti(1);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        desine(200);\n" +
                    "        laukti(1);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        desine(200);\n" +
                    "        laukti(1);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        sauti();\n" +
                    "        laukti(2);\n" +
                    "        sokti();\n" +
                    "        lauktiMS(300);\n" +
                    "        desine(1000);\n" +
                    "        laukti(2);\n" +
                    "        kaire(500);\n" +
                    "        laukti(1);\n" +
                    "        desine(20);\n" +
                    "        sauti();\n" +
                    "        lauktiMS(500);\n" +
                    "        desine(2000);\n" +
                    "        sauti();\n" +
                    "        sokti();\n" +
                    "        desine(2000);\n" +
                    "        laukti(1);\n" +
                    "        sauti();\n" +
                    "        sokti();\n" +
                    "        lauktiMS(350);\n" +
                    "        desine(150);\n" +
                    "        laukti(1);\n" +
                    "        sokti();\n" +
                    "        desine(300);\n" +
                    "        sauti();\n" +
                    "        desine(600);\n" +
                    "        lauktiMS(500);\n" +
                    "        sokti();\n" +
                    "        desine(500);\n" +
                    "        rbt.keyPress(KeyEvent.VK_X);\n" +
                    "        TimeUnit.MILLISECONDS.sleep(20);\n" +
                    "        rbt.keyRelease(KeyEvent.VK_X);\n" +
                    "    }\n" +
                    "    static void pereitiVisus() throws InterruptedException {\n" +
                    "        pereitiPirma();\n" +
                    "        pereitiAntra();\n" +
                    "        pereitiTrecia();\n" +
                    "    }\n" +
                    "    static void laukti(int x) throws InterruptedException {\n" +
                    "        TimeUnit.SECONDS.sleep(x);\n" +
                    "    }\n" +
                    "    static void lauktiMS(int x) throws InterruptedException {\n" +
                    "        TimeUnit.MILLISECONDS.sleep(x);\n" +
                    "    }\n" +
                    "}\n");
            aWriter.flush();
            aWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        background(100, 40, 0);
        if (mode == 0)//intro screen
        {
            if (x <= 25)
                blink = true;
            else if (x >= 250)
                blink = false;
            if (blink)
                x += 2;
            else if (!blink)
                x -= 2;
            fill(280, 160, 0, x);
            textSize(60);
            textAlign(CENTER, CENTER);
            text("cave runner", width / 2, height / 4, 800, 200);
            playButton.update();
            playButton.render();
            controlButton.update();
            controlButton.render();
            editButton.update();
            editButton.render();

            if (controlButton.isClicked()) {
                JFrame frame = new JFrame("Control");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setOpaque(true);

                JPanel inputpanel = new JPanel();

                inputpanel.setLayout(new FlowLayout());
                JLabel label = new JLabel("<html><b>Komandos:</b><br><br>" +
                        "kaire(int x);<br>" +
                        "desine(int x);<br>" +
                        "sokti();<br>" +
                        "soktiDesinen(int x);<br>" +
                        "soktiKairen(int x);<br>" +
                        "sauti();<br>" +
                        "pereitiPirma();<br>" +
                        "pereitiAntra();<br>" +
                        "pereitiTrecia();<br>" +
                        "pereitiVisus();<br>" +
                        "laukti(int seconds);<br>" +
                        "lauktiMS(int milliseconds);<br>" +
                        "</html>");
                JTextArea input = new JTextArea();
                input.setPreferredSize(new Dimension(400, 400));
                JButton button = new JButton("Enter");

                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createIt(input.getText());
                        System.out.println(input.getText());
                        execCommand("javac userInput.java", true);
                        execCommand("java userInput", false);
                    }
                });
                inputpanel.add(label);
                inputpanel.add(input);
                inputpanel.add(button);

                panel.add(inputpanel);
                frame.getContentPane().add(BorderLayout.CENTER, panel);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(false);
                input.requestFocus();
            }
            if (playButton.isClicked()) {
                mode = 1;
            }
            if (editButton.isClicked()) {
                mode = -1;
            }

        } else if (mode == 1) {
            if (!enCreated[mode - 1])
                createEnemies(mode);
            playGame(platforms1);
            if (u.x > gameWorld.w - 200) {
                textAlign(CENTER, CENTER);
                fill(255, 0, 0);
                textSize(50);
                text("Press X", width / 2, height / 2);
                if (key == 'x') {
                    u.x = 100;
                    u.y = 500;
                    mode = 2;
                }
            }
        } else if (mode == 2) {
            if (!enCreated[mode - 1])
                createEnemies(mode);
            playGame(platforms2);
            if (u.x > gameWorld.w - 200) {
                textAlign(CENTER, CENTER);
                fill(255, 0, 0);
                textSize(50);
                text("Press X", width / 2, height / 2);
                if (key == 'x') {
                    u.x = 100;
                    u.y = 500;
                    mode = 3;
                }
            }
        } else if (mode == 3) {
            if (!enCreated[mode - 1])
                createEnemies(mode);
            playGame(platforms3);
            if (u.x > gameWorld.w - 200 && u.y < 200) {
                textAlign(CENTER, CENTER);
                fill(255, 0, 0);
                textSize(50);
                text("Press X", width / 2, height / 2);
                if (key == 'x') {
                    u.x = 100;
                    u.y = 500;
                    mode = 4;
                }
            }
        } else if (mode == -1) {
            background(140, 80, 0);
            backButton.update();
            backButton.render();
            if (backButton.isClicked())
                mode = 0;
            for (int i = 0; i < 3; i++) {
                mapButton[i].update();
                mapButton[i].render();
                if (mapButton[i].isClicked()) {
                    selectedMap = i + 1;
                }
            }
            selectedM = select(selectedMap);
            drawSmallMap(selectedM);


            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    placingButton[i * cols + j].update();
                    placingButton[i * cols + j].render();
                }
            }

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    tileButton[i * 4 + j].update();
                    tileButton[i * 4 + j].render();
                    if (tileButton[i * 4 + j].isClicked()) {
                        selected = tileButton[i * 4 + j].img;
                        selectedID = tileButton[i * 4 + j].id;
                    }
                }
            }
            if (selected != null) {
                image(selected, mouseX, mouseY);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (placingButton[i * cols + j].isClicked())
                            replace(i, j, selectedMap, selectedID);
                    }
                }
            }

        } else if (mode == -2) {
            background(0);
            fill(255);
            textSize(40);
            text("You have died", width / 2, height / 4);

            backButton.update(50);
            backButton.render(255);

            restartButton.update(50);
            restartButton.render(255);
            editButton.update(50);
            editButton.render(255);

            if (editButton.isClicked()) {
                mode = -1;
            }
            if (restartButton.isClicked()) {
                mode = 1;
            }
            if (backButton.isClicked())
                mode = 0;

        } else if (mode == 4) {
            background(0);
            fill(255);
            textSize(25);


            text("You have won!", width / 2, height / 4);
            backButton.update(50);
            backButton.render(255);

            restartButton.update(50);
            restartButton.render(255);
            if (restartButton.isClicked())
                mode = 1;
            if (backButton.isClicked())
                mode = 0;

        }
        loadPlatforms();


    }

    public String rectangleCollisions(Player r1, Platform r2) {
        ////r1 is the player
        ////r2 is the platform rectangle
        ////function returns the String collisionSide

        //allow unicorn to pass through platforms.
        //Disable if you want unicorn to bounce off bottom of platforms

        //if (r1.vy < 0) { return "none"; }
        if (!(r2.id == 40 || r2.id == 41 || r2.id == 42 ||
                r2.id == 50 || r2.id == 51 || r2.id == 52 ||
                r2.id == 60 || r2.id == 61 || r2.id == 62))
            return "none";

        float dx = (r1.x + r1.w / 2) - (r2.x + r2.w / 2);
        float dy = (r1.y + r1.h / 2) - (r2.y + r2.h / 2);

        float combinedHalfWidths = r1.halfWidth + r2.halfWidth;
        float combinedHalfHeights = r1.halfHeight + r2.halfHeight;

        if (abs(dx) < combinedHalfWidths) {
            ////collision has happened on the x axis
            ////now check on the y axis
            if (abs(dy) < combinedHalfHeights) {
                ////collision detected
                //determine the overlap on each axis
                float overlapX = combinedHalfWidths - abs(dx);
                float overlapY = combinedHalfHeights - abs(dy);
                ////the collision is on the axis with the
                ////SMALLEST overlap
                if (overlapX + 1 >= overlapY) {
                    if (dy > 0) {
                        ////move the rectangle back to eliminate overlap
                        ////before calling its display to prevent
                        ////drawing object inside each other
                        r1.y += overlapY;
                        return "top";
                    } else {
                        r1.y -= overlapY;
                        return "bottom";
                    }
                } else {
                    if (dx > 0) {
                        r1.x += overlapX;
                        return "left";
                    } else {
                        r1.x -= overlapX;
                        return "right";
                    }
                }
            } else {
                //collision failed on the y axis
                return "none";
            }
        } else {
            //collision failed on the x axis
            return "none";
        }
    }

    public void loadJSON(String s, int[] map) {
        json = loadJSONObject(s);
        JSONArray layers = json.getJSONArray("layers");
        JSONObject layer = layers.getJSONObject(0);
        JSONArray array = layer.getJSONArray("data");
        for (int i = 0; i < array.size(); i++) {
            map[i] = array.getInt(i) - 1;
        }
    }

    public void getTiles() {
        tileset = loadImage("cave_tileset.png");
        int index = 0;
        int x = 0, y = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 10; j++) {
                tiles[index] = tileset.get(x, y, tilesize, tilesize);
                x += tilesize;
                index++;
            }
            y += tilesize;
            x = 0;
        }
    }

    public void keyPressed() {

        switch (keyCode) {
            case 37://left
                left = true;
                break;
            case 39://right
                right = true;
                break;
            case 38://up
                up = true;
                break;
            case 40://down
                down = true;
                break;
            case 32: //space
                space = true;
                break;
        }
    }

    public void keyReleased() {
        switch (keyCode) {
            case 37://left
                left = false;
                break;
            case 39://right
                right = false;
                break;
            case 38://up
                up = false;
                break;
            case 40://down
                down = false;
                break;
            case 32: //space
                space = false;
                break;
        }
    }

    public void displayArray(Platform[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i].display();
        }
    }

    public void checkCollisionSide(Platform[] array) {
        for (int i = 0; i < array.length; ++i) {
            u.collisionSide = rectangleCollisions(u, array[i]);
            u.checkPlatforms();
        }
    }

    public void playGame(Platform[] platforms) {
        background(255);
        u.update();
        for (int i = 0; i < enemyCount; i++) {
            enemy[i].update();
            if (rectangleCollisionPvE(u, enemy[i])) {
                mode = -2;
                u.x = 100;
                u.y = 500;
            }
        }


        if (space && firingTimer.complete()) {
            bullets[nextBullet].fire(u.x, u.y, u.w, u.facingRight);

            nextBullet = (nextBullet + 1) % bullets.length;
            firingTimer.start();
        }
        for (int i = 0; i < bullets.length; ++i) {
            bullets[i].update();
            for (int j = 0; j < enemy.length; j++) {
                if (enemy[j].x < bullets[i].x + bullets[i].w &&
                        enemy[j].x + enemy[j].w > bullets[i].x &&
                        enemy[j].y < bullets[i].y + bullets[i].h &&
                        enemy[j].y + enemy[j].h > bullets[i].y &&
                        !enemy[j].dead) {

                    enemy[j].dead = true;
                    bullets[i].reset();
                    //health += 20;
                }
            }
        }

        camera.x = floor(u.x + (u.halfWidth) - (camera.w / 2));
        camera.y = floor(u.y + (u.halfHeight) - (camera.h / 2));

        if (camera.x < gameWorld.x) {
            camera.x = gameWorld.x;
        }
        if (camera.y < gameWorld.y) {
            camera.y = gameWorld.y;
        }
        if (camera.x + camera.w > gameWorld.x + gameWorld.w) {
            camera.x = gameWorld.x + gameWorld.w - camera.w;
        }
        if (camera.y + camera.h > gameWorld.h) {
            camera.y = gameWorld.h - camera.h;
        }
        pushMatrix();
        translate(-camera.x, -camera.y);
        backImage.display();
        checkCollisionSide(platforms);
        u.display();
        for (int i = 0; i < enemyCount; i++) {
            if (!enemy[i].dead)
                enemy[i].display();
        }

        for (int i = 0; i < bullets.length; ++i) {
            bullets[i].display();
        }

        displayArray(platforms);
        textSize(20);
        //text(u.y, camera.x+ 200,camera.y +200);
        //text(u.x, camera.x+200,camera.y+220);

        popMatrix();
    }

    public void loadPlatforms() {
        int actualPlatformCount = 0;
        int counter = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                PImage img = tiles[map1[counter]];

                Platform p = new Platform(j * tilesize, i * tilesize, tilesize, tilesize, map1[counter], img);
                temp_platforms[actualPlatformCount] = p;
                actualPlatformCount++;
                counter++;
            }
        }
        platforms1 = new Platform[actualPlatformCount];
        for (int i = 0; i < actualPlatformCount; ++i) {
            platforms1[i] = temp_platforms[i];
        }


        actualPlatformCount = 0;
        counter = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                PImage img = tiles[map2[counter]];

                Platform p = new Platform(j * tilesize, i * tilesize, tilesize, tilesize, map2[counter], img);
                temp_platforms[actualPlatformCount] = p;
                actualPlatformCount++;
                counter++;
            }
        }
        platforms2 = new Platform[actualPlatformCount];
        for (int i = 0; i < actualPlatformCount; ++i) {
            platforms2[i] = temp_platforms[i];
        }


        actualPlatformCount = 0;
        counter = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                PImage img = tiles[map3[counter]];

                Platform p = new Platform(j * tilesize, i * tilesize, tilesize, tilesize, map3[counter], img);
                temp_platforms[actualPlatformCount] = p;
                actualPlatformCount++;
                counter++;
            }
        }
        platforms3 = new Platform[actualPlatformCount];
        for (int i = 0; i < actualPlatformCount; ++i) {
            platforms3[i] = temp_platforms[i];
        }

    }

    public void drawSmallMap(int[] map) {
        fill(200);
        rectMode(CORNER);
        rect(22, 50, cols * 12, rows * 12);
        int x = 22;
        int y = 50;
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                image(tiles[map[counter]], x, y, 12, 12);
                x += 12;
                counter++;
            }
            y += 12;
            x = 22;
        }
        image(tiles[30], width / 2 - 128, height / 2 + 128, 64, 64);
        image(tiles[31], width / 2 - 64, height / 2 + 128, 64, 64);
        image(tiles[32], width / 2, height / 2 + 128, 64, 64);
        image(tiles[36], width / 2 + 64, height / 2 + 128, 64, 64);

        image(tiles[40], width / 2 - 128, height / 2 + 64 + 128, 64, 64);
        image(tiles[41], width / 2 - 64, height / 2 + 64 + 128, 64, 64);
        image(tiles[42], width / 2, height / 2 + 64 + 128, 64, 64);
        image(tiles[46], width / 2 + 64, height / 2 + 64 + 128, 64, 64);

        image(tiles[50], width / 2 - 128, height / 2 + 128 + 128, 64, 64);
        image(tiles[51], width / 2 - 64, height / 2 + 128 + 128, 64, 64);
        image(tiles[52], width / 2, height / 2 + 128 + 128, 64, 64);
        image(tiles[56], width / 2 + 64, height / 2 + 128 + 128, 64, 64);
    }

    public int[] select(int x) {
        if (x == 1)
            return map1;
        else if (x == 2)
            return map2;
        else
            return map3;
    }

    public void replace(int x, int y, int i, int id) {
        if (i == 1)
            map1[x * cols + y] = id;
        else if (i == 2)
            map2[x * cols + y] = id;
        else
            map3[x * cols + y] = id;
    }

    public boolean rectangleCollisionPvE(Player r1, Enemy r2) {
        ////r1 is the player
        ////r2 is the enemy
        if (r2.dead)
            return false;

        float dx = (r1.x + r1.w / 2) - (r2.x + r2.w / 2);
        float dy = (r1.y + r1.h / 2) - (r2.y + r2.h / 2);

        float combinedHalfWidths = r1.halfWidth + r2.halfWidth;
        float combinedHalfHeights = r1.halfHeight + r2.halfHeight;

        if (abs(dx) < combinedHalfWidths) {
            ////collision has happened on the x axis
            ////now check on the y axis
            ////collision detected
            return abs(dy) < combinedHalfHeights;
        }
        return false;
    }

    public boolean rectangleCollisionBvE(Bullet r1, Enemy r2) {
        ////r1 is the player
        ////r2 is the enemy

        float dx = (r1.x + r1.w / 2) - (r2.x + r2.w / 2);
        float dy = (r1.y + r1.h / 2) - (r2.y + r2.h / 2);

        float combinedHalfWidths = r1.halfWidth + r2.halfWidth;
        float combinedHalfHeights = r1.halfHeight + r2.halfHeight;

        if (abs(dx) < combinedHalfWidths) {
            ////collision has happened on the x axis
            ////now check on the y axis
            ////collision detected
            return abs(dy) < combinedHalfHeights;
        }
        return false;
    }

    public void createEnemies(int level) {
        if (level == 1) {

            enemy[0] = new Enemy(192, 659 + 45, 678 + 90 - 64);
            enemy[1] = new Enemy(1216, 2419 + 45, 678 + 90 - 64);
            enemy[2] = new Enemy(1472, 1779 + 45, 358 + 90 - 64);
            enemy[3] = new Enemy(2368, 2547 + 45, 358 + 90 - 64);
            enemy[4] = new Enemy(2560, 2611 + 45, 678 + 90 - 64);
            enCreated[0] = true;
            enCreated[1] = false;
            enCreated[2] = false;
        }
        if (level == 2) {
            enemy[0] = new Enemy(384, 1011 + 45, 678 + 90 - 64);
            enemy[1] = new Enemy(1312, 2387 + 45, 678 + 90 - 64);
            enemy[2] = new Enemy(1056, 1267 + 45, 454 + 90 - 64);
            enemy[3] = new Enemy(1984, 2163 + 45, 102 + 90 - 64);
            enemy[4] = new Enemy(2294, 2784, 6 + 90 - 64);
            enCreated[0] = false;
            enCreated[1] = true;
            enCreated[2] = false;
        }
        if (level == 3) {
            enemy[0] = new Enemy(288, 544, 6 + 90 - 64);
            enemy[1] = new Enemy(544, 1203 + 45, 678 + 90 - 64);
            enemy[2] = new Enemy(1280, 2035 + 45, 678 + 90 - 64);
            enemy[3] = new Enemy(2144, 3027 + 45, 678 + 90 - 64);
            enemy[4] = new Enemy(2624, 2739 + 45, 166 + 90 - 64);
            enCreated[0] = false;
            enCreated[1] = false;
            enCreated[2] = true;
        }
    }

    public void settings() {
        size(1200, 800);
    }


    public static void execCommand(String string, boolean x)
    {
        String[] command =
                {
                        "cmd",
                };
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);

            new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
            new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println(string);


            stdin.close();
            if(x)
                p.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class Bullet {
        float w, h, x, y;
        float halfWidth, halfHeight;
        float vx, vy;
        boolean inMotion;

        float leftBound, rightBound, lowerBound, upperBound;
        int frameOffset = 0;
        int frameCounter = 0;
        int framesPerAction = 6;
        boolean facingRight;

        Bullet() {
            w = 35;
            h = 10;
            x = 0;
            y = -h;
            halfWidth = w / 2;
            halfHeight = h / 2;
            vx = 0;
            vy = 0;
            inMotion = false;

            leftBound = 0;
            rightBound = 0;
            lowerBound = 0;
            upperBound = 0;
        }

        public void fire(float ux, float uy, float uw, boolean ufacingRight) {
            if (!inMotion) {
                y = uy - 3 + 30;
                inMotion = true;
                if (ufacingRight == true) {
                    facingRight = ufacingRight;
                    vx = 8;
                    x = ux + uw - 35;//shift starting point for laser to right side of unicorn
                } else {
                    vx = -8;
                    x = ux;
                }
            }
        }

        public void reset() {
            x = -100;
            y = -h;
            vx = 0;
            vy = 0;
            inMotion = false;
        }

        public void update() {
            if (inMotion) {
                x += vx;
                y += vy;
            }
            ////check boundaries
            rightBound = Math.max(camera.w, u.x + u.halfWidth + camera.w / 2);
            leftBound = camera.x;
            upperBound = camera.y;
            lowerBound = Math.max(camera.h, u.y + u.halfHeight + camera.h / 2);
            if (x < leftBound || x > rightBound || y < upperBound || y > lowerBound) {
                reset();
            }
        }

        public void display() {
            // fill(255,0,0);
            //rect(x,y,w,h);
            if (frameOffset > 14)
                frameOffset = 0;

            frameCounter++;
            if (frameCounter % framesPerAction == 0)
                frameOffset++;
            if (facingRight)
                image(fireballSprites[frameOffset], x, y, 50, 30);
            else
                image(fireballSprites[29 - frameOffset], x, y, 50, 30);

        }
    }

    class Button {
        PVector pos = new PVector(0, 0);
        float w, h;
        boolean Pressed = false;
        boolean Clicked = false;
        PImage img;
        int id;
        String text;

        Button(int x_, int y_, int w_, int h_, String t_) {
            pos.x = x_;
            pos.y = y_;
            w = w_;
            h = h_;
            text = t_;

        }

        Button(int x_, int y_, int w_, int h_, PImage img_, int id_) {
            pos.x = x_;
            pos.y = y_;
            w = w_;
            h = h_;
            img = img_;
            id = id_;
        }

        public void update() {
            if (mousePressed == true && mouseButton == LEFT && !Pressed) {
                Pressed = true;
                if (mouseX <= pos.x + w / 2 && mouseX >= pos.x - w / 2 && mouseY >= pos.y - h / 2 && mouseY <= pos.y + h / 2) {
                    Clicked = true;
                }
            } else {
                Clicked = false;
                Pressed = false;
            }
            if (mouseX <= pos.x + w / 2 && mouseX >= pos.x - w / 2 && mouseY >= pos.y - h / 2 && mouseY <= pos.y + h / 2) {
                stroke(255);
            } else stroke(0);
        }

        public void update(int x) {
            if (mousePressed == true && mouseButton == LEFT) {
                Pressed = true;
                if (mouseX <= pos.x + w / 2 && mouseX >= pos.x - w / 2 && mouseY >= pos.y - h / 2 && mouseY <= pos.y + h / 2) {
                    Clicked = true;
                }
            } else {
                Clicked = false;
                Pressed = false;
            }
            if (mouseX <= pos.x + w / 2 && mouseX >= pos.x - w / 2 && mouseY >= pos.y - h / 2 && mouseY <= pos.y + h / 2) {
                stroke(255);
            } else stroke(x);
        }

        public void render() {
            rectMode(CENTER);
            fill(140, 80, 0, 0);
            rect(pos.x, pos.y, w, h);
            textAlign(CENTER, CENTER);
            fill(0);
            if (text != null) {
                textSize(20);
                text(text, pos.x, pos.y);
            }
        }

        public void render(int x) {
            rectMode(CENTER);
            fill(140, 80, 0, 0);
            rect(pos.x, pos.y, w, h);
            textAlign(CENTER, CENTER);
            fill(x);
            if (text != null) {
                textSize(20);
                text(text, pos.x, pos.y);
            }
        }

        public boolean isClicked() {
            return Clicked;
        }
    }

    class Enemy {

        float w, h, x, y, vx, vy, accelerationX, accelerationY, speedLimit;

        float rightEdge, leftEdge, ground, gravity;

        float halfWidth, halfHeight;

        boolean facingRight = true, dead;
        int frameOffset = 0;
        int frameCounter = 0;
        int framesPerAction = 6;


        Enemy(int lEdge, int rEdge, int y_) {
            w = 64;
            h = 64;
            vx = 2;
            vy = 0;
            x = (rEdge + lEdge) / 2;
            y = y_;

            leftEdge = lEdge;
            rightEdge = rEdge;

        }

        public void update() {
            x += vx;
            checkBoundaries();

        }

        public void checkBoundaries() {
            if (x <= leftEdge) //left
            {
                vx *= -1;
                x = leftEdge;
                facingRight = true;
            }
            if (x >= rightEdge - w) //right
            {
                vx *= -1;
                x = rightEdge - w;
                facingRight = false;
            }
        }

        public void display() {
            //fill(0,255,0,128);
            //rect(x,y,w,h);
            if (frameOffset > 1)
                frameOffset = 0;

            frameCounter++;
            if (frameCounter % framesPerAction == 0)
                frameOffset++;
            if (facingRight)
                image(enemySprites[3 + frameOffset], x, y);
            else
                image(enemySprites[9 + frameOffset], x, y);


        }


    }

    class FrameObject {
        float x, y, w, h;

        FrameObject(float _x, float _y, float _w, float _h) {
            x = _x;
            y = _y;
            w = _w;
            h = _h;
        }
    }

    class ImageObject {
        float w, h, x, y;
        PImage img;
        float halfWidth, halfHeight;

        ImageObject(float _x, float _y, float _w, float _h, PImage _img) {
            x = _x;
            y = _y;
            w = _w;
            h = _h;
            img = _img;
            halfWidth = w / 2;
            halfHeight = h / 2;
        }

        public void display() {
            image(img, x, y);
            image(img, x + w, y);
            image(img, x + 2 * w, y);
        }
    }

    class Platform {
        float w, h, x, y;
        float halfWidth, halfHeight;
        int id;
        PImage tile;

        Platform(float _x, float _y, float _w, float _h, int _id, PImage _tile) {
            w = _w;
            h = _h;
            x = _x;
            y = _y;
            id = _id;
            tile = _tile;

            halfWidth = w / 2;
            halfHeight = h / 2;
        }

        public void display() {

            image(tile, x, y, w, h);
        }
    }

    class Player {

        float w, h, x, y, vx, vy,
                accelerationX, accelerationY,
                speedLimit;

        //world variables
        float friction, bounce, gravity;

        boolean isOnGround;
        float jumpForce;

        float halfWidth, halfHeight;
        String collisionSide;
        int frameOffset;
        int frameCounter;
        int framesPerAction;
        boolean facingRight;
        boolean dead = false;


        Player() {
            w = 45;//was 140. shrink to be centered on body
            h = 90;//was 95. shrink to be centered on body
            x = 100;
            y = 500;
            vx = 0;
            vy = 0;
            accelerationX = 0;
            accelerationY = 0;
            speedLimit = 7.5f;
            isOnGround = false;
            jumpForce = -15;

            //world values
            friction = 0.96f;
            bounce = -0.7f;
            gravity = .3f;

            frameOffset = 0;
            frameCounter = 0;
            framesPerAction = 6;

            halfWidth = w / 2;
            halfHeight = h / 2;

            collisionSide = "";
            facingRight = true;
        }

        public void update() {
            //start all moves off with friction at 1
            if (left && !right) {
                accelerationX = -0.2f;
                friction = 1;
                facingRight = false;
            }
            if (right && !left) {
                accelerationX = 0.2f;
                friction = 1;
                facingRight = true;
            }
            if (!left && !right) {
                accelerationX = 0;
            }

            if (up && !down && isOnGround) {
                //accelerationY = -0.2;
                //gravity = 0;
                vy = jumpForce;
                isOnGround = false;
                friction = 1;
            }
            if (down && !up) {
                //accelerationY = 0.2;
                //friction = 1;
            }
            if (!up && !down) {
                //accelerationY = 0;
            }
            //removing impulse reintroduces friction
            if (!up && !down && !left && !right) {
                friction = 0.96f;
                //gravity = 0.3;
            }

            vx += accelerationX;
            vy += accelerationY;

            //friction 1 = no friction
            vx *= friction;

            //apply gravity
            vy += gravity;

            ////correct for maximum speeds
            if (vx > speedLimit) {
                vx = speedLimit;
            }
            if (vx < -speedLimit) {
                vx = -speedLimit;
            }
            //need to let gravity ramp it up
            if (vy > 3 * speedLimit) {
                vy = 3 * speedLimit;
            }
            //dont need when jumping
            if (vy < -speedLimit) {
                //vy = -speedLimit;
            }
            if (abs(vx) < 0.2f) {
                vx = 0;
            }
            ////move the player

            x += vx;
            y += vy;
            checkBoundaries();
            //checkPlatforms();
        }

        public void checkBoundaries() {
            ////check boundaries
            ////left
            if (x < 0) {
                vx = 0;
                x = 0;
            }
            //// right
            if (x + w > gameWorld.w) {
                vx = 0;
                x = gameWorld.w - w - 0.2f;
            }
            ////top
            if (y <= 0.1f) {
                vy = 0;
                y = 0;
            }
            if (y + h > gameWorld.h) {
                isOnGround = true;
                vy = 0;
                y = gameWorld.h - h;
            }
        }

        public void checkPlatforms() {
            ////update for platform collisions
            if (collisionSide == "bottom" && vy >= 0) {
                if (vy < 1) {
                    isOnGround = true;
                    vy = 0;
                } else {
                    //reduced bounce for floor bounce
                    vy = 0;
                }
            } else if (collisionSide == "top" && vy <= 0) {
                vy = 0;
            } else if (collisionSide == "right" && vx >= 0) {
                vx = 0;
            } else if (collisionSide == "left" && vx <= 0) {
                vx = 0;
            }
            if (collisionSide != "bottom" && vy > 0) {
                isOnGround = false;
            }
        }

        public void display() {
            rectMode(CORNER);
            fill(0, 255, 0, 128);
            //rect(x, y, w, h);
            if (frameOffset > 6)
                frameOffset = 0;

            frameCounter++;
            if (frameCounter % framesPerAction == 0)
                frameOffset++;

            if (facingRight)
                if (right) {
                    image(spriteImages[24 + frameOffset], x - 23, y - 5);
                } else if (space && facingRight)
                    image(spriteImages[3], x - 23, y - 5);
                else image(spriteImages[0], x - 23, y - 5);

            else if (left) {
                image(spriteImages[63 - frameOffset], x - 23, y - 5);
            } else if (space && !facingRight)
                image(spriteImages[35], x - 23, y - 5);
            else
                image(spriteImages[39], x - 23, y - 5);
        }

    }

    class Timer {
        float startTime;
        int interval;

        Timer(int timeInterval) {
            interval = timeInterval;
        }

        public void start() {
            startTime = millis();
        }

        public float elapsed() {
            return startTime;
        }

        public void resetTimer() {

        }

        public boolean complete() {
            float elapsedTime = millis() - startTime;
            return elapsedTime > interval;
        }
    }


}

