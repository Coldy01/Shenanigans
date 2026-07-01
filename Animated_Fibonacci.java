import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame frame = new javax.swing.JFrame("Fibonacci Animation");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            FibonacciPanel panel = new FibonacciPanel(12);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.startAnimation();
        });
    }
}

class FibonacciPanel extends JPanel {
    private final java.util.List<Integer> fib = new ArrayList<>();
    private final java.util.List<Rectangle> rects = new ArrayList<>();
    private final int maxCount;
    private int current = 0;
    private Timer timer;

    FibonacciPanel(int maxCount) {
        this.maxCount = Math.max(2, maxCount);
        fib.add(1);
        fib.add(1);
        for (int i = 2; i < this.maxCount; i++) fib.add(fib.get(i - 1) + fib.get(i - 2));
        setBackground(Color.WHITE);
    }

    void startAnimation() {
        current = 0;
        rects.clear();
        if (timer != null && timer.isRunning()) timer.stop();
        timer = new Timer(650, e -> {
            if (current < maxCount) {
                computeRectsUpTo(current + 1);
                current++;
                repaint();
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setInitialDelay(300);
        timer.start();
    }

    private void computeRectsUpTo(int count) {
        rects.clear();
        int w = getWidth();
        int h = getHeight();
        if (w <= 0 || h <= 0) return;

        java.util.List<Integer> sizes = new ArrayList<>();
        for (int i = 0; i < count; i++) sizes.add(fib.get(i));

        java.util.List<Point> pos = new ArrayList<>();
        int x = 0, y = 0;
        int dir = 0; // 0 right,1 up,2 left,3 down
        pos.add(new Point(0, 0));
        for (int i = 1; i < sizes.size(); i++) {
            switch (dir) {
                case 0: x += sizes.get(i - 1); break;
                case 1: y -= sizes.get(i - 1); break;
                case 2: x -= sizes.get(i); break;
                default: y += sizes.get(i - 1); break;
            }
            pos.add(new Point(x, y));
            dir = (dir + 1) % 4;
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (int i = 0; i < sizes.size(); i++) {
            int px = pos.get(i).x;
            int py = pos.get(i).y;
            minX = Math.min(minX, px);
            minY = Math.min(minY, py);
            maxX = Math.max(maxX, px + sizes.get(i));
            maxY = Math.max(maxY, py + sizes.get(i));
        }

        int simW = Math.max(1, maxX - minX);
        int simH = Math.max(1, maxY - minY);
        double scale = Math.min((w - 40) / (double) simW, (h - 40) / (double) simH);

        int offsetX = (int) ((w - simW * scale) / 2.0 - minX * scale);
        int offsetY = (int) ((h - simH * scale) / 2.0 - minY * scale);

        for (int i = 0; i < sizes.size(); i++) {
            int sw = (int) Math.max(1, Math.round(sizes.get(i) * scale));
            int sx = offsetX + (int) Math.round(pos.get(i).x * scale);
            int sy = offsetY + (int) Math.round(pos.get(i).y * scale);
            rects.add(new Rectangle(sx, sy, sw, sw));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < rects.size(); i++) {
            Rectangle r = rects.get(i);
            float hue = i / (float) Math.max(1, rects.size());
            g2.setColor(Color.getHSBColor(hue, 0.55f, 0.95f));
            g2.fill(r);
            g2.setColor(Color.DARK_GRAY);
            g2.draw(r);
            String s = String.valueOf(fib.get(i));
            FontMetrics fm = g2.getFontMetrics();
            int tx = r.x + (r.width - fm.stringWidth(s)) / 2;
            int ty = r.y + (r.height + fm.getAscent()) / 2 - 4;
            g2.setColor(Color.BLACK);
            g2.drawString(s, tx, ty);
        }

        // draw simple spiral arcs over placed squares
        int dir = 0;
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(30, 30, 30, 180));
        for (int i = 0; i < rects.size(); i++) {
            Rectangle r = rects.get(i);
            switch (dir) {
                case 0: g2.drawArc(r.x, r.y - r.height, r.width * 2, r.height * 2, 270, 90); break;
                case 1: g2.drawArc(r.x - r.width, r.y - r.height, r.width * 2, r.height * 2, 0, 90); break;
                case 2: g2.drawArc(r.x - r.width, r.y, r.width * 2, r.height * 2, 90, 90); break;
                default: g2.drawArc(r.x, r.y, r.width * 2, r.height * 2, 180, 90); break;
            }
            dir = (dir + 1) % 4;
        }

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }
}

