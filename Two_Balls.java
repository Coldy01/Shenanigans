import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Physics Box - Animated Balls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new PhysicsPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class PhysicsPanel extends JPanel implements java.awt.event.ActionListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
        private final java.util.List<Ball> balls = new java.util.ArrayList<>();
        private final Rectangle box = new Rectangle(50, 50, 700, 600);
        private final javax.swing.Timer timer;
        private Ball grabbed = null;
        private int grabOffsetX, grabOffsetY;
        // for computing throw momentum
        private int lastMouseX, lastMouseY;
        private long lastMouseTime;
        private double pendingVx, pendingVy;

        public PhysicsPanel() {
            setPreferredSize(new Dimension(800, 700));
            setBackground(Color.WHITE);

            // create two balls
            balls.add(new Ball(200, 100, 25, Color.RED));
            balls.add(new Ball(300, 50, 30, Color.BLUE));

            addMouseListener(this);
            addMouseMotionListener(this);

            timer = new javax.swing.Timer(16, this); // ~60fps
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // draw box
            g2.setColor(Color.LIGHT_GRAY);
            g2.fill(box);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.draw(box);

            // draw balls
            for (Ball b : balls) b.draw(g2);
        }

        // physics and animation
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            double gravity = 0.6;
            for (Ball b : balls) {
                if (b == grabbed) continue; // skip physics while dragging
                b.vy += gravity;
                b.x += b.vx;
                b.y += b.vy;

                // collision with box walls
                if (b.x - b.r < box.x) { b.x = box.x + b.r; b.vx = -b.vx * 0.8; }
                if (b.x + b.r > box.x + box.width) { b.x = box.x + box.width - b.r; b.vx = -b.vx * 0.8; }
                if (b.y - b.r < box.y) { b.y = box.y + b.r; b.vy = -b.vy * 0.8; }
                if (b.y + b.r > box.y + box.height) { b.y = box.y + box.height - b.r; b.vy = -b.vy * 0.8; }
            }

            // simple ball-ball collisions (elastic-ish)
            for (int i = 0; i < balls.size(); i++) {
                for (int j = i + 1; j < balls.size(); j++) {
                    Ball a = balls.get(i);
                    Ball b = balls.get(j);
                    double dx = b.x - a.x;
                    double dy = b.y - a.y;
                    double dist = Math.hypot(dx, dy);
                    double minDist = a.r + b.r;
                    if (dist > 0 && dist < minDist) {
                        double nx = dx / dist, ny = dy / dist;
                        double overlap = 0.5 * (minDist - dist);
                        a.x -= nx * overlap;
                        a.y -= ny * overlap;
                        b.x += nx * overlap;
                        b.y += ny * overlap;

                        // swap velocities along normal
                        double va = a.vx * nx + a.vy * ny;
                        double vb = b.vx * nx + b.vy * ny;
                        double pa = va;
                        double pb = vb;
                        double exchange = (pa - pb) * 0.9;
                        a.vx -= exchange * nx;
                        a.vy -= exchange * ny;
                        b.vx += exchange * nx;
                        b.vy += exchange * ny;
                    }
                }
            }

            repaint();
        }

        // mouse events
        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            for (Ball b : balls) {
                if (b.contains(e.getX(), e.getY())) {
                    grabbed = b;
                    grabOffsetX = e.getX() - (int) b.x;
                    grabOffsetY = e.getY() - (int) b.y;
                    // stop movement while grabbed
                    b.vx = b.vy = 0;
                    // initialize mouse tracking
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                    lastMouseTime = System.currentTimeMillis();
                    pendingVx = pendingVy = 0;
                    return;
                }
            }
        }

        @Override
        public void mouseReleased(java.awt.event.MouseEvent e) {
            if (grabbed != null) {
                // impart velocity based on recent mouse movement (throw)
                // pendingVx/pendingVy computed during drag
                grabbed.vx = pendingVx;
                grabbed.vy = pendingVy;
            }
            grabbed = null;
        }

        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            if (grabbed != null) {
                int mx = e.getX();
                int my = e.getY();
                long now = System.currentTimeMillis();
                long dt = now - lastMouseTime;
                if (dt <= 0) dt = 1;
                // compute mouse velocity in pixels per ms
                double vxMs = (mx - lastMouseX) / (double) dt;
                double vyMs = (my - lastMouseY) / (double) dt;
                // scale to simulation velocity (pixels per frame)
                double scale = 16.0 * 0.7; // ~frames per 16ms * damping
                pendingVx = vxMs * scale;
                pendingVy = vyMs * scale;

                grabbed.x = mx - grabOffsetX;
                grabbed.y = my - grabOffsetY;
                lastMouseX = mx;
                lastMouseY = my;
                lastMouseTime = now;
                repaint();
            }
        }

        @Override
        public void mouseMoved(java.awt.event.MouseEvent e) {
            boolean over = false;
            for (Ball b : balls) {
                if (b.contains(e.getX(), e.getY())) { over = true; break; }
            }
            setCursor(over ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        }

        @Override public void mouseClicked(java.awt.event.MouseEvent e) {}
        @Override public void mouseEntered(java.awt.event.MouseEvent e) {}
        @Override public void mouseExited(java.awt.event.MouseEvent e) {}

        // Ball inner class
        static class Ball {
            double x, y; // center
            int r;
            double vx = 0, vy = 0;
            Color color;

            Ball(double x, double y, int r, Color color) { this.x = x; this.y = y; this.r = r; this.color = color; }

            void draw(Graphics2D g) {
                int drawX = (int) (x - r);
                int drawY = (int) (y - r);
                if (containsMouse()) {
                    g.setColor(color.brighter());
                } else g.setColor(color);
                g.fillOval(drawX, drawY, r * 2, r * 2);
                g.setColor(Color.BLACK);
                g.drawOval(drawX, drawY, r * 2, r * 2);
            }

            boolean contains(int mx, int my) {
                double dx = mx - x, dy = my - y;
                return dx * dx + dy * dy <= r * r;
            }

            // placeholder for hover detection in draw (not used here)
            boolean containsMouse() { return false; }
        }
    }
}

