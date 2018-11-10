package Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DoublePendulum extends JFrame {
    private static final double r1 = 130, r2 = 130, G = 1, m1 = 15, m2 = 15, baseFriction = 1, frameRate = 60;
    private static final Color[] colors = {Color.RED, Color.YELLOW, Color.BLACK};
    private int x;
    private int y;
    private double a1 = (float)(Math.PI), a2 = (float)(Math.PI);
    private double a1_v, a2_v;
    private JPanel panel;
    private ArrayList<Integer[]> points1 = new ArrayList<>();
    private ArrayList<Integer[]> points2 = new ArrayList<>();

    private DoublePendulum() {
        createWindow();
        createPanel();
        run();
    }

    private void createWindow() {
        Dimension windowSize = new Dimension(800, 800);
        setSize(windowSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowPositionX = (screenSize.width - windowSize.width) / 2;
        int windowPositionY = (screenSize.height - windowSize.height) / 2;
        Point windowPosition = new Point(windowPositionX, windowPositionY);
        setLocation(windowPosition);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        x = windowSize.width / 2;
        y = windowSize.height / 2;
    }

    private void createPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        panel.setBackground(colors[2]);
        add(panel);
    }

    private void run() {
        panel.repaint();
        while (true) {
            panel.repaint();
            try {
                Thread.sleep((long)(1000 / frameRate));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw(Graphics g) {
        double num1 = -G * (2 * m1 + m2) * Math.sin(a1);
        double num2 = -m2 * G * Math.sin(a1-2*a2);
        double num3 = -2*Math.sin(a1-a2)*m2;
        double num4 = a2_v*a2_v*r2+a1_v*a1_v*r1*Math.cos(a1-a2);
        double den = r1 * (2*m1+m2-m2*Math.cos(2*a1-2*a2));
        double a1_a = (num1 + num2 + num3*num4) / den;

        num1 = 2 * Math.sin(a1-a2);
        num2 = (a1_v*a1_v*r1*(m1+m2));
        num3 = G * (m1 + m2) * Math.cos(a1);
        num4 = a2_v*a2_v*r2*m2*Math.cos(a1-a2);
        den = r2 * (2*m1+m2-m2*Math.cos(2*a1-2*a2));
        double a2_a = (num1*(num2+num3+num4)) / den;

        int x1 = (int) (r1 * Math.sin(a1)) + x;
        int y1 = (int) (r1 * Math.cos(a1)) + y;
        int x2 = (int) (x1 + r2 * Math.sin(a2));
        int y2 = (int) (y1 + r2 * Math.cos(a2));
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(colors[0]);
        g2.drawLine(x, y, x1, y1);
        g2.fillOval((int)(x1 - m1 / 2), (int)(y1 - m1 / 2), (int)m1, (int)m1);
        for (Integer[] i : points1) {
            g2.fillOval(i[0], i[1], 2, 2);
        }
        g2.setColor(colors[1]);
        g2.drawLine(x1, y1, x2, y2);
        g2.fillOval((int)(x2 - m2 / 2), (int)(y2 - m2 / 2), (int)m2, (int)m2);
        for (Integer[] i : points2) {
            g2.fillOval(i[0], i[1], 3, 3);
        }
        points1.add(new Integer[] {x1, y1});
        points2.add(new Integer[] {x2, y2});
        g2.setColor(Color.RED);
        a1_v += a1_a;
        a2_v += a2_a;
        a1 += a1_v;
        a2 += a2_v;
        double a1_friction = 1 - (baseFriction * a1_v * a1_v) - 0.00;
        double a2_friction = 1 - (baseFriction * a2_v * a2_v) - 0.00;
        a1_v *= a1_friction;
        a2_v *= a2_friction;
    }

    public static void main(String[] args) {
        new DoublePendulum();
    }
}
