package com.tranner.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginPanel extends JPanel {
    private JButton loginButton, registerButton;
    private JTextField loginUsernameField, loginPasswordField;
    private JLabel loginlbl, passwordlbl, tempLogo;
    private JLabel forgotPasswordLabel, newAccountLabel;

    // Brand colors from the design
    private static final Color BG_TOP      = new Color(53, 80, 161);
    private static final Color BG_BOTTOM   = new Color(163, 166, 199);
    private static final Color BUTTON_BG   = new Color(42, 68, 145);
    private static final Color FIELD_BORDER= new Color(180, 190, 220, 160);
    private static final Color TEXT_WHITE  = Color.WHITE;
    private static final Color TEXT_MUTED  = new Color(200, 205, 225);

    private static final Font FONT_BOLD_LARGE  = new Font("SansSerif", Font.BOLD,   26);
    private static final Font FONT_PLAIN_LARGE = new Font("SansSerif", Font.PLAIN,  26);
    private static final Font FONT_PLAIN_MED   = new Font("SansSerif", Font.PLAIN,  18);

    public LoginPanel() {
        this.setOpaque(false);
        this.setLayout(null); // absolute layout to match design

        // ── Logo ──────────────────────────────────────────────────────────────
        BufferedImage logoImg = null;
        try {
            logoImg = ImageIO.read(getClass().getResource("/com/tranner/gui/logo.png"));
        } catch (IOException | IllegalArgumentException ignored) {}

        if (logoImg != null) {
            Image scaled = logoImg.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            tempLogo = new JLabel(new ImageIcon(scaled));
        } else {
            // Fallback globe-like icon rendered in text
            tempLogo = new JLabel("\uD83C\uDF10"); // 🌐
            tempLogo.setFont(new Font("SansSerif", Font.PLAIN, 56));
            tempLogo.setForeground(TEXT_WHITE);
        }

        // ── "TripSync | Welcome!" headline ───────────────────────────────────
        // We compose this as two labels side by side for bold/plain mix
        JLabel titleBold  = new JLabel("TripSync");
        titleBold.setFont(new Font("SansSerif", Font.BOLD,  38));
        titleBold.setForeground(TEXT_WHITE);

        JLabel titlePlain = new JLabel(" | Welcome!");
        titlePlain.setFont(new Font("SansSerif", Font.PLAIN, 38));
        titlePlain.setForeground(TEXT_WHITE);

        // ── Username field ────────────────────────────────────────────────────
        loginUsernameField = makePlaceholderField("Username:");
        loginlbl = new JLabel(); // kept for API compatibility; not shown separately

        // ── Password field ────────────────────────────────────────────────────
        loginPasswordField = makePlaceholderField("Password:");
        passwordlbl = new JLabel(); // kept for API compatibility; not shown separately

        // ── Login button ──────────────────────────────────────────────────────
        loginButton = makeRoundedButton("Log in", FONT_BOLD_LARGE, TEXT_WHITE, BUTTON_BG);

        // ── Forgot Password link ──────────────────────────────────────────────
        forgotPasswordLabel = new JLabel("Forgot Password?", SwingConstants.CENTER);
        forgotPasswordLabel.setFont(FONT_PLAIN_MED);
        forgotPasswordLabel.setForeground(TEXT_MUTED);
        forgotPasswordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // ── "Making a new account?" ───────────────────────────────────────────
        newAccountLabel = new JLabel("Making a new account?", SwingConstants.CENTER);
        newAccountLabel.setFont(FONT_PLAIN_MED);
        newAccountLabel.setForeground(TEXT_MUTED);

        // ── Register button ───────────────────────────────────────────────────
        registerButton = makeRoundedButton("Register", FONT_BOLD_LARGE, TEXT_WHITE, BUTTON_BG);

        // ── Add components ────────────────────────────────────────────────────
        this.add(tempLogo);
        this.add(titleBold);
        this.add(titlePlain);
        this.add(loginlbl);
        this.add(passwordlbl);
        this.add(loginUsernameField);
        this.add(loginPasswordField);
        this.add(loginButton);
        this.add(forgotPasswordLabel);
        this.add(newAccountLabel);
        this.add(registerButton);

        // ── Layout on resize ─────────────────────────────────────────────────
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                doLayout();
            }
        });

        // ── Prevent auto-focus on username field ──────────────────────────────
        // Transfer focus away from the first focusable field after the panel is shown
        loginUsernameField.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override public void ancestorAdded(javax.swing.event.AncestorEvent e) {
                SwingUtilities.invokeLater(() -> LoginPanel.this.requestFocusInWindow());
            }
            @Override public void ancestorRemoved(javax.swing.event.AncestorEvent e) {}
            @Override public void ancestorMoved(javax.swing.event.AncestorEvent e) {}
        });
    }

    @Override
    public void doLayout() {
        int W = getWidth();
        int H = getHeight();
        if (W == 0 || H == 0) return;

        // Center the content block horizontally
        int contentW  = (int)(W * 0.84);
        int marginX   = (W - contentW) / 2;

        // ── Logo + title row ──────────────────────────────────────────────────
        int logoSize  = 64;
        int topY      = (int)(H * 0.18);

        tempLogo.setBounds(marginX, topY, logoSize, logoSize);

        Dimension tbD = ((JLabel)this.getComponent(1)).getPreferredSize(); // titleBold
        Dimension tpD = ((JLabel)this.getComponent(2)).getPreferredSize(); // titlePlain
        int titleH    = Math.max(tbD.height, tpD.height);
        int titleY    = topY + (logoSize - titleH) / 2;

        JLabel titleBold  = (JLabel) this.getComponent(1);
        JLabel titlePlain = (JLabel) this.getComponent(2);
        titleBold.setBounds(marginX + logoSize + 8, titleY, tbD.width, titleH);
        titlePlain.setBounds(marginX + logoSize + 8 + tbD.width, titleY, tpD.width, titleH);

        // ── Fields + login button row ─────────────────────────────────────────
        int fieldRowY  = topY + logoSize + 28;
        int fieldH     = 62;
        int gap        = 16;

        // Left column: ~65% of contentW
        int leftW      = (int)(contentW * 0.65);
        int rightW     = contentW - leftW - gap;
        int rightX     = marginX + leftW + gap;

        loginUsernameField.setBounds(marginX, fieldRowY, leftW, fieldH);
        loginButton.setBounds(rightX, fieldRowY, rightW, fieldH);

        loginPasswordField.setBounds(marginX, fieldRowY + fieldH + gap, leftW, fieldH);
        forgotPasswordLabel.setBounds(rightX, fieldRowY + fieldH + gap + (fieldH - 22) / 2, rightW, 22);

        // ── Register section ──────────────────────────────────────────────────
        int regW       = (int)(contentW * 0.40);
        int regX       = marginX + (contentW - regW) / 2;
        int regY       = fieldRowY + fieldH * 2 + gap * 2 + 40;
        int regBtnH    = 68;

        newAccountLabel.setBounds(regX, regY, regW, 24);
        registerButton.setBounds(regX, regY + 28, regW, regBtnH);
    }

    // ── Gradient background ────────────────────────────────────────────────────
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

    // ── Helpers ────────────────────────────────────────────────────────────────

    /** Text field with placeholder text that clears on focus. */
    private JTextField makePlaceholderField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Rounded transparent background
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                // Draw placeholder when empty and not focused
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
        field.setBorder(new RoundedBorder(20, FIELD_BORDER, 2)); // ← FIELD CORNER RADIUS (20 = arc size)
        // Padding via empty border inside compound
        field.setMargin(new Insets(0, 24, 0, 24));
        return field;
    }

    /** Rounded button matching the design. */
    private JButton makeRoundedButton(String text, Font font, Color fg, Color bg) {
        // HOVER colors — lighten bg on hover, darken on press
        Color hoverBg = new Color(
                Math.min(bg.getRed()   + 25, 255),
                Math.min(bg.getGreen() + 25, 255),
                Math.min(bg.getBlue()  + 40, 255));

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? bg.darker()
                           : getModel().isRollover() ? hoverBg
                           : bg;
                g2.setColor(base);
                // ↓ CORNER RADIUS — change the two "22" values to adjust rounding
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth()  - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(font);
        btn.setForeground(fg);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Trigger repaint on hover so the color change is visible
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    // ── Rounded border ─────────────────────────────────────────────────────────
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
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 2, h - 2, radius * 1, radius * 1)); // radius set via RoundedBorder constructor
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(thickness, thickness, thickness, thickness); }
    }

    // ── Public API (unchanged) ─────────────────────────────────────────────────
    public JButton getRegisterButton() { return registerButton; }

    // Bonus getters in case the controller needs them
    public JButton getLoginButton()        { return loginButton; }
    public String getUsername()   { return loginUsernameField.getText().trim(); }
    public String getPassword()   { return loginPasswordField.getText().trim(); }
    public JLabel getForgotPasswordLabel() { return forgotPasswordLabel; }
}