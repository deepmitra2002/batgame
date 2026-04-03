import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;

public class BatEscape {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame());
    }
}

/** Window / top-level frame */
class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Bat Escape");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        panel.requestFocusInWindow();
    }
}

/** Main game surface + game loop + input + world state */
class GamePanel extends JPanel implements ActionListener, KeyListener {
    // --- Game tuning ---
    private static final int W = 900;
    private static final int H = 520;

    private static final int GROUND_MARGIN = 20;

    private static final int FPS = 60;
    private static final int TIMER_DELAY_MS = 1000 / FPS;

    private static final double GRAVITY = 0.45;
    private static final double FLAP_VY = -7.5; // upward impulse

    private static final double SCROLL_SPEED = 4.2;

    private static final int OBSTACLE_WIDTH = 70;
    private static final int GAP_HEIGHT = 170;
    private static final int OBSTACLE_SPACING = 260;

    private static final int INSECT_CHANCE_PERCENT = 55; // chance to spawn an insect per obstacle set
    private static final int INSECT_BONUS = 50;

    // --- State ---
    private final Timer timer;

    private Bat bat;
    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Insect> insects = new ArrayList<>();

    private boolean running = true;
    private boolean started = false;
    private boolean spaceHeld = false;

    private long ticks = 0;
    private int timeScore = 0;
    private int bonusScore = 0;

    private final Random rnd = new Random();

    public GamePanel() {
        setPreferredSize(new Dimension(W, H));
        setFocusable(true);
        addKeyListener(this);

        initGame();

        timer = new Timer(TIMER_DELAY_MS, this);
        timer.start();
    }

    private void initGame() {
        bat = new Bat(180, H / 2.0);

        obstacles.clear();
        insects.clear();

        // Seed a few obstacle sets off-screen to the right
        int x = W + 200;
        for (int i = 0; i < 4; i++) {
            spawnObstacleSet(x);
            x += OBSTACLE_SPACING;
        }

        running = true;
        started = false;
        spaceHeld = false;

        ticks = 0;
        timeScore = 0;
        bonusScore = 0;
    }

    private void spawnObstacleSet(int x) {
        // Create a vertical gap at a random y
        int minTop = 40;
        int maxTop = H - GROUND_MARGIN - GAP_HEIGHT - 40;
        int gapTop = minTop + rnd.nextInt(Math.max(1, maxTop - minTop));

        // Top obstacle: from ceiling to gapTop
        Obstacle top = new Obstacle(x, 0, OBSTACLE_WIDTH, gapTop);

        // Bottom obstacle: from gapTop + GAP_HEIGHT to floor
        int bottomY = gapTop + GAP_HEIGHT;
        int bottomH = (H - GROUND_MARGIN) - bottomY;
        Obstacle bottom = new Obstacle(x, bottomY, OBSTACLE_WIDTH, bottomH);

        obstacles.add(top);
        obstacles.add(bottom);

        // Bonus insect near the middle of the gap sometimes
        if (rnd.nextInt(100) < INSECT_CHANCE_PERCENT) {
            double insectX = x + OBSTACLE_WIDTH / 2.0 - 10;
            double insectY = gapTop + GAP_HEIGHT / 2.0 - 10;
            insects.add(new Insect(insectX, insectY));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Game loop tick
        if (!running) {
            repaint();
            return;
        }

        ticks++;

        if (started) {
            // Score increases over time while alive
            // (scaled a bit so it feels natural)
            if (ticks % 3 == 0) timeScore++;
        }

        // Physics update
        if (!started) {
            // idle bobbing before start
            bat.idleBob(ticks);
        } else {
            if (spaceHeld) {
                // Make flap on hold feel responsive but not too strong:
                // only apply a flap impulse if bat is falling or near zero vy.
                if (bat.getVy() > -1.0) {
                    bat.flap(FLAP_VY);
                    beepSoft();
                }
            }
            bat.applyGravity(GRAVITY);
            bat.update();

            // Move obstacles/insects left
            for (Obstacle o : obstacles) {
                o.moveLeft(SCROLL_SPEED);
            }
            for (Insect i : insects) {
                i.moveLeft(SCROLL_SPEED);
            }

            // Recycle obstacles once they go off-screen
            // We recycle per *pair* (top+bottom): find leftmost x among obstacle sets.
            recycleIfNeeded();

            // Collision with bounds (ceiling/floor)
            if (bat.getBounds().y < 0 || bat.getBounds().y + bat.getBounds().height > (H - GROUND_MARGIN)) {
                gameOver();
            }

            // Collision with obstacles
            Rectangle batRect = bat.getBounds();
            for (Obstacle o : obstacles) {
                if (batRect.intersects(o.getBounds())) {
                    gameOver();
                    break;
                }
            }

            // Collect insects
            Iterator<Insect> it = insects.iterator();
            while (it.hasNext()) {
                Insect insect = it.next();
                if (!insect.isCollected() && batRect.intersects(insect.getBounds())) {
                    insect.collect();
                    bonusScore += INSECT_BONUS;
                    Toolkit.getDefaultToolkit().beep();
                    it.remove(); // remove after collecting
                }
            }
        }

        repaint();
    }

    private void recycleIfNeeded() {
        // Find the maximum x among obstacles (rightmost), so we can place new ones after it.
        double maxX = Double.NEGATIVE_INFINITY;
        for (Obstacle o : obstacles) {
            maxX = Math.max(maxX, o.getX());
        }

        // We manage obstacles in pairs: (top, bottom) share same x.
        // Find any obstacle pair that has moved completely off-screen (x + width < 0),
        // then remove both and spawn a new set at maxX + spacing.
        // Because obstacles list includes both top and bottom, we detect by scanning unique x's.
        Map<Integer, List<Obstacle>> byX = new HashMap<>();
        for (Obstacle o : obstacles) {
            int key = (int) Math.round(o.getX());
            byX.computeIfAbsent(key, k -> new ArrayList<>()).add(o);
        }

        // Collect candidate x keys that are off-screen
        List<Integer> offscreenKeys = new ArrayList<>();
        for (Map.Entry<Integer, List<Obstacle>> en : byX.entrySet()) {
            List<Obstacle> list = en.getValue();
            if (!list.isEmpty()) {
                Obstacle any = list.get(0);
                if (any.getX() + any.getWidth() < 0) offscreenKeys.add(en.getKey());
            }
        }

        // For each off-screen set, remove its obstacles and spawn a new set.
        // Usually only one set per tick.
        Collections.sort(offscreenKeys);
        for (Integer key : offscreenKeys) {
            // remove obstacles with approx that x (within a small tolerance)
            final double targetX = key;
            obstacles.removeIf(o -> Math.abs(o.getX() - targetX) < 2.0);

            // also remove insects that are far off-screen
            insects.removeIf(i -> i.getX() + i.getSize() < 0);

            int newX = (int) Math.round(maxX + OBSTACLE_SPACING);
            spawnObstacleSet(newX);

            // update maxX after spawning
            maxX = newX;
        }
    }

    private void gameOver() {
        running = false;
        Toolkit.getDefaultToolkit().beep();
    }

    private void beepSoft() {
        // optional: avoid too many beeps; only beep occasionally
        if (ticks % 8 == 0) Toolkit.getDefaultToolkit().beep();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Better rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background cave
        paintCaveBackground(g2);

        // Floor strip
        g2.setColor(new Color(30, 25, 30));
        g2.fillRect(0, H - GROUND_MARGIN, W, GROUND_MARGIN);

        // Draw obstacles
        for (Obstacle o : obstacles) {
            o.draw(g2);
        }

        // Draw insects
        for (Insect i : insects) {
            i.draw(g2);
        }

        // Draw bat
        bat.draw(g2);

        // HUD
        paintHud(g2);

        g2.dispose();
    }

    private void paintCaveBackground(Graphics2D g2) {
        // Dark gradient background
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 20),
                                             0, H, new Color(3, 3, 8));
        g2.setPaint(gp);
        g2.fillRect(0, 0, W, H);

        // Subtle cave "texture" dots
        g2.setColor(new Color(255, 255, 255, 18));
        for (int i = 0; i < 120; i++) {
            int x = (int) ((i * 97L + ticks * 3) % W);
            int y = (int) ((i * 43L + ticks * 2) % (H - GROUND_MARGIN));
            int r = 1 + (i % 2);
            g2.fillOval(x, y, r, r);
        }
    }

    private void paintHud(Graphics2D g2) {
        int total = timeScore + bonusScore;

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(new Color(230, 230, 240));
        g2.drawString("Score: " + total, 18, 28);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.setColor(new Color(190, 190, 205));
        g2.drawString("Bonus: " + bonusScore, 18, 48);

        if (!started && running) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 26));
            g2.setColor(new Color(240, 240, 255));
            centerText(g2, "BAT ESCAPE", H / 2 - 40);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.setColor(new Color(200, 200, 215));
            centerText(g2, "Press SPACE to start (hold/press to flap)", H / 2);
        }

        if (!running) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 34));
            g2.setColor(new Color(255, 120, 120));
            centerText(g2, "GAME OVER", H / 2 - 30);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 18));
            g2.setColor(new Color(230, 230, 240));
            centerText(g2, "Final Score: " + (timeScore + bonusScore), H / 2 + 6);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g2.setColor(new Color(200, 200, 215));
            centerText(g2, "Press R to Restart", H / 2 + 34);
        }
    }

    private void centerText(Graphics2D g2, String text, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (W - fm.stringWidth(text)) / 2;
        g2.drawString(text, x, y);
    }

    // --- Input ---
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            spaceHeld = true;
            if (running && !started) {
                started = true;
                bat.flap(FLAP_VY);
                Toolkit.getDefaultToolkit().beep();
            }
        }

        if (code == KeyEvent.VK_R) {
            if (!running) {
                initGame();
            }
        }

        if (code == KeyEvent.VK_ESCAPE) {
            // optional quick exit
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceHeld = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }
}

/** Player bat: position, velocity, movement, drawing */
class Bat {
    private double x;
    private double y;
    private double vy;

    private final int w = 42;
    private final int h = 28;

    public Bat(double x, double y) {
        this.x = x;
        this.y = y;
        this.vy = 0;
    }

    public void applyGravity(double g) {
        vy += g;
        // clamp fall speed
        vy = Math.min(vy, 10.5);
    }

    public void flap(double impulseVy) {
        vy = impulseVy;
    }

    public void update() {
        y += vy;
    }

    public void idleBob(long ticks) {
        // tiny idle animation before the game starts
        y += Math.sin(ticks / 10.0) * 0.4;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), w, h);
    }

    public double getVy() { return vy; }

    public void draw(Graphics2D g2) {
        Rectangle r = getBounds();

        // body
        g2.setColor(new Color(120, 120, 160));
        g2.fillOval(r.x, r.y, r.width, r.height);

        // wing (simple triangle-ish polygon)
        g2.setColor(new Color(90, 90, 130));
        int[] xs = { r.x + 8, r.x - 14, r.x + 8 };
        int[] ys = { r.y + 8, r.y + r.height / 2, r.y + r.height - 6 };
        g2.fillPolygon(xs, ys, 3);

        // eye
        g2.setColor(new Color(240, 240, 255));
        g2.fillOval(r.x + r.width - 14, r.y + 7, 7, 7);
        g2.setColor(new Color(20, 20, 30));
        g2.fillOval(r.x + r.width - 12, r.y + 9, 3, 3);

        // outline
        g2.setColor(new Color(30, 30, 50, 140));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(r.x, r.y, r.width, r.height);
    }
}

/** Obstacle: cave rock/spike rectangle */
class Obstacle {
    private double x;
    private double y;
    private final int w;
    private final int h;

    public Obstacle(double x, double y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void moveLeft(double dx) {
        x -= dx;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), w, h);
    }

    public void draw(Graphics2D g2) {
        Rectangle r = getBounds();

        // rock base
        g2.setColor(new Color(55, 55, 70));
        g2.fillRect(r.x, r.y, r.width, r.height);

        // slight shading
        g2.setColor(new Color(75, 75, 95));
        g2.fillRect(r.x, r.y, Math.max(6, r.width / 6), r.height);

        // jagged "spikes" along the edge facing the bat
        g2.setColor(new Color(85, 85, 110));
        int spikeCount = Math.max(4, r.height / 26);
        for (int i = 0; i < spikeCount; i++) {
            int sy = r.y + i * (r.height / spikeCount);
            int[] xs = { r.x, r.x - 12, r.x };
            int[] ys = { sy, sy + 10, sy + 20 };
            g2.fillPolygon(xs, ys, 3);
        }

        // outline
        g2.setColor(new Color(20, 20, 35, 160));
        g2.drawRect(r.x, r.y, r.width, r.height);
    }

    public double getX() { return x; }
    public int getWidth() { return w; }
}

/** Bonus collectible insect (simple small circle) */
class Insect {
    private double x;
    private double y;
    private final int size = 18;
    private boolean collected = false;

    public Insect(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft(double dx) {
        x -= dx;
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() { return collected; }

    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), size, size);
    }

    public void draw(Graphics2D g2) {
        if (collected) return;

        Rectangle r = getBounds();

        // glow
        g2.setColor(new Color(255, 220, 120, 60));
        g2.fillOval(r.x - 6, r.y - 6, r.width + 12, r.height + 12);

        // body
        g2.setColor(new Color(255, 210, 90));
        g2.fillOval(r.x, r.y, r.width, r.height);

        // tiny wings
        g2.setColor(new Color(240, 240, 255, 120));
        g2.fillOval(r.x - 6, r.y + 2, 10, 8);
        g2.fillOval(r.x + r.width - 4, r.y + 2, 10, 8);

        // outline
        g2.setColor(new Color(60, 50, 40, 120));
        g2.drawOval(r.x, r.y, r.width, r.height);
    }

    public double getX() { return x; }
    public int getSize() { return size; }
}