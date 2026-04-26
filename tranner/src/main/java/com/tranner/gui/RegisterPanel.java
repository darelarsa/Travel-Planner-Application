package com.tranner.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RegisterPanel extends JPanel {
    private JLabel usernameLabel, passwordLabel, confirmPasswordLabel;
    private JTextField usernameField, passwordField, confirmPasswordField;
    private JButton submitLoginCredentials;
    private JLabel titleBold, titlePlain, logoLabel;


    private static final Color BG_TOP       = new Color(53, 80, 161);
    private static final Color BG_BOTTOM    = new Color(163, 166, 199);
    private static final Color BUTTON_BG    = new Color(42, 68, 145);
    private static final Color FIELD_BORDER = new Color(180, 190, 220, 160);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_MUTED   = new Color(200, 205, 225);

    private static final Font FONT_BOLD_LARGE  = new Font("SansSerif", Font.BOLD,  26);
    private static final Font FONT_PLAIN_MED   = new Font("SansSerif", Font.PLAIN, 18);



    public RegisterPanel() {
        this.setOpaque(false);
        this.setLayout(null);

        BufferedImage logoImg = null;
        try {
            logoImg = ImageIO.read(getClass().getResource("/com/tranner/gui/logo.png"));
        } catch (IOException | IllegalArgumentException ignored) {}

        if (logoImg != null) {
            Image scaled = logoImg.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaled));
        } else {
            logoLabel = new JLabel("\uD83C\uDF10");
            logoLabel.setFont(new Font("SansSerif", Font.PLAIN, 56));
            logoLabel.setForeground(TEXT_WHITE);
        }
        this.add(logoLabel);

        titleBold = new JLabel("TripSync");
        titleBold.setFont(new Font("SansSerif", Font.BOLD, 38));
        titleBold.setForeground(TEXT_WHITE);

        titlePlain = new JLabel(" | New User?");
        titlePlain.setFont(new Font("SansSerif", Font.PLAIN, 38));
        titlePlain.setForeground(TEXT_WHITE);

        usernameField = makePlaceholderField("Username:");
        passwordField = makePlaceholderField("Password:");
        confirmPasswordField = makePlaceholderField("Confirm Password:");

        submitLoginCredentials = makeRoundedButton("Register");

        this.add(titleBold);
        this.add(titlePlain);
        this.add(usernameField);
        this.add(passwordField);
        this.add(confirmPasswordField);
        this.add(submitLoginCredentials);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    public void doLayout() {
        int W = getWidth();
        int H = getHeight();
        if (W == 0 || H == 0) return;

        int contentW = (int)(W * 0.84);
        int marginX  = (W - contentW) / 2;
        int fieldH   = 62;
        int gap      = 16;

        Dimension tbD = titleBold.getPreferredSize();
        Dimension tpD = titlePlain.getPreferredSize();

        int logoSize = 64;
        int titleY = (int)(H * 0.12);
        int startY = (int)(H * 0.30);

        logoLabel.setBounds(marginX, titleY, logoSize, logoSize);
        titleBold.setBounds(marginX + logoSize + 8, titleY, tbD.width, tbD.height);
        titlePlain.setBounds(marginX + logoSize + 8 + tbD.width, titleY, tpD.width, tpD.height);

        usernameField.setBounds(marginX, startY, contentW, fieldH);
        passwordField.setBounds(marginX, startY + fieldH + gap, contentW, fieldH);
        confirmPasswordField.setBounds(marginX, startY + (fieldH + gap) * 2, contentW, fieldH);
        submitLoginCredentials.setBounds(marginX, startY + (fieldH + gap) * 3 + 10, contentW, fieldH);
    }

    private JTextField makePlaceholderField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(255, 255, 255, 180));
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, 24, textY);
                }
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_BOLD_LARGE);
        field.setForeground(TEXT_WHITE);
        field.setCaretColor(TEXT_WHITE);
        field.setBorder(new RoundedBorder(20, FIELD_BORDER, 2));
        field.setMargin(new Insets(0, 24, 0, 24));
        return field;
    }

    private JButton makeRoundedButton(String text) {
        Color hoverBg = new Color(
                Math.min(BUTTON_BG.getRed()   + 25, 255),
                Math.min(BUTTON_BG.getGreen() + 25, 255),
                Math.min(BUTTON_BG.getBlue()  + 40, 255));

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? BUTTON_BG.darker()
                        : getModel().isRollover() ? hoverBg
                        : BUTTON_BG;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(TEXT_WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth()  - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(FONT_BOLD_LARGE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;
        private final int thickness;

        RoundedBorder(int radius, Color color, int thickness) {
            this.radius    = radius;
            this.color     = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 2, h - 2, radius, radius));
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    public JButton getSubmitLoginCredentials() {
        return submitLoginCredentials;
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return passwordField.getText().trim();
    }

    public String getConfirmPassword() { return confirmPasswordField.getText().trim(); }
}