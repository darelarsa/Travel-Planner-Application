package com.tranner.gui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import com.tranner.model.person.Preference;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class createProfilePanel extends JPanel {

    // ── Existing fields (API unchanged) ───────────────────────────────────────
    private JLabel regPlaceLabel;
    private JLabel regBudgetLabel;
    private JLabel regTransportLabel;
    private JLabel regIntensityLabel;
    private JTextField regPlaceField;
    private JRadioButton budgetLow, budgetModerate, budgetHigh, budgetLuxury;
    private ButtonGroup budgetGroup;
    private JRadioButton transportWalking, transportTransit, transportRideshare,
                         transportCar, transportBike;
    private ButtonGroup transportGroup;
    private JRadioButton intensityRelaxed, intensityModerate, intensityActive, intensityExtreme;
    private ButtonGroup intensityGroup;
    private JButton submitProfileButton;
    private JButton backToLoginButton;

    // ── UI components ─────────────────────────────────────────────────────────
    private JTextField firstNameField, lastNameField;
    private DropdownMenu budgetDropdown, transportDropdown, intensityDropdown;

    // ── Colors ────────────────────────────────────────────────────────────────
    private static final Color BG_TOP           = new Color(53,  80,  161);
    private static final Color BG_BOTTOM        = new Color(163, 166, 199);
    private static final Color CARD_BG          = new Color(235, 237, 245);
    private static final Color BUTTON_BG        = new Color(42,  68,  145);
    private static final Color TEXT_DARK        = new Color(30,  55,  130);
    private static final Color TEXT_PLACEHOLDER = new Color(160, 165, 185);
    private static final Color FIELD_BORDER     = new Color(200, 205, 220);
    private static final Color DROPDOWN_BORDER  = new Color(30,  55,  130);
    private static final Color PREF_LABEL_COLOR = new Color(90,  95,  115);
    private static final Color SEPARATOR_COLOR  = new Color(210, 212, 222);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD,  28);
    private static final Font FONT_SECTION  = new Font("SansSerif", Font.BOLD,  22);
    private static final Font FONT_FIELD    = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_DROPDOWN = new Font("SansSerif", Font.BOLD,  16);
    private static final Font FONT_ITEM     = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_BUTTON   = new Font("SansSerif", Font.PLAIN, 22);

    // ── Layout constants ──────────────────────────────────────────────────────
    private static final int ITEM_H = 44;

    public createProfilePanel() {
        this.setOpaque(false);
        this.setLayout(null);

        // Hidden radio buttons for data model
        budgetLow      = new JRadioButton(); budgetModerate = new JRadioButton();
        budgetHigh     = new JRadioButton(); budgetLuxury   = new JRadioButton();
        budgetGroup    = new ButtonGroup();
        budgetGroup.add(budgetLow); budgetGroup.add(budgetModerate);
        budgetGroup.add(budgetHigh); budgetGroup.add(budgetLuxury);

        transportWalking   = new JRadioButton(); transportTransit   = new JRadioButton();
        transportRideshare = new JRadioButton(); transportCar       = new JRadioButton();
        transportBike      = new JRadioButton();
        transportGroup     = new ButtonGroup();
        transportGroup.add(transportWalking); transportGroup.add(transportTransit);
        transportGroup.add(transportRideshare); transportGroup.add(transportCar);
        transportGroup.add(transportBike);

        intensityRelaxed  = new JRadioButton(); intensityModerate = new JRadioButton();
        intensityActive   = new JRadioButton(); intensityExtreme  = new JRadioButton();
        intensityGroup    = new ButtonGroup();
        intensityGroup.add(intensityRelaxed); intensityGroup.add(intensityModerate);
        intensityGroup.add(intensityActive);  intensityGroup.add(intensityExtreme);

        // API-compat labels/field
        regPlaceLabel     = new JLabel("Destination:");
        regBudgetLabel    = new JLabel("Budget:");
        regTransportLabel = new JLabel("Transport Mode:");
        regIntensityLabel = new JLabel("Trip Intensity:");
        regPlaceField     = new JTextField(20);

        // ── Back button ───────────────────────────────────────────────────────
        backToLoginButton = makeBackButton();

        // ── Name fields ───────────────────────────────────────────────────────
        firstNameField = makePlaceholderField("First Name");
        lastNameField  = makePlaceholderField("Last Name");

        // ── Dropdown menus ────────────────────────────────────────────────────
        budgetDropdown = new DropdownMenu("Budget",
                new String[]{"Low", "Moderate", "High", "Luxury"});
        budgetDropdown.addSelectionListener(val -> {
            budgetGroup.clearSelection();
            switch (val) {
                case "Low"      -> budgetLow.setSelected(true);
                case "Moderate" -> budgetModerate.setSelected(true);
                case "High"     -> budgetHigh.setSelected(true);
                case "Luxury"   -> budgetLuxury.setSelected(true);
                // default         -> { budgetDropdown. }
            }
        });

        transportDropdown = new DropdownMenu("Transport",
                new String[]{"Walking", "Public Transit", "Rideshare", "Rental Car", "Bicycle"});
        transportDropdown.addSelectionListener(val -> {
            transportGroup.clearSelection();
            switch (val) {
                case "Walking"        -> transportWalking.setSelected(true);
                case "Public Transit" -> transportTransit.setSelected(true);
                case "Rideshare"      -> transportRideshare.setSelected(true);
                case "Rental Car"     -> transportCar.setSelected(true);
                case "Bicycle"        -> transportBike.setSelected(true);
            }
        });

        intensityDropdown = new DropdownMenu("Intensity",
                new String[]{"Relaxed", "Moderate", "Active", "Extreme"});
        intensityDropdown.addSelectionListener(val -> {
            intensityGroup.clearSelection();
            switch (val) {
                case "Relaxed"  -> intensityRelaxed.setSelected(true);
                case "Moderate" -> intensityModerate.setSelected(true);
                case "Active"   -> intensityActive.setSelected(true);
                case "Extreme"  -> intensityExtreme.setSelected(true);
            }
        });

        // ── Submit button ─────────────────────────────────────────────────────
        submitProfileButton = makeRoundedButton("Save Profile", FONT_BUTTON, Color.WHITE, BUTTON_BG);

        // ── Add to panel ──────────────────────────────────────────────────────
        this.add(backToLoginButton);
        this.add(firstNameField);
        this.add(lastNameField);
        // Dropdowns must be added last so they paint on top of other components
        this.add(submitProfileButton);
        this.add(intensityDropdown);
        this.add(transportDropdown);
        this.add(budgetDropdown);

        this.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { doLayout(); }
        });
    }

    // ── Layout ─────────────────────────────────────────────────────────────────
    @Override
    public void doLayout() {
        int W = getWidth(), H = getHeight();
        if (W == 0 || H == 0) return;

        int cardX = (int)(W * 0.05);
        int cardY = (int)(H * 0.13);
        int cardW = (int)(W * 0.90);
        int cardH = (int)(H * 0.75);

        int innerX  = cardX + 36;
        int innerY  = cardY + 30;
        int innerW  = cardW - 72;
        int btnSz   = 44;

        // Back button
        backToLoginButton.setBounds(innerX, innerY, btnSz, btnSz);

        // Name fields
        int fieldY  = innerY + btnSz + 28;
        int fieldH  = 48;
        int gap     = 16;
        int halfFW  = (innerW - gap) / 2;
        firstNameField.setBounds(innerX, fieldY, halfFW, fieldH);
        lastNameField.setBounds(innerX + halfFW + gap, fieldY, halfFW, fieldH);

        // "Preferences" label — painted in paintComponent
        // Dropdowns sit below the label (label ~28px, then 12px gap)
        int dropY   = fieldY + fieldH + 56;
        int dropH   = 52; // header height only; popup extends downward
        int dropGap = 16;
        int dropW   = (innerW - dropGap * 2) / 3;

        budgetDropdown.setBounds(innerX, dropY, dropW, dropH);
        transportDropdown.setBounds(innerX + dropW + dropGap, dropY, dropW, dropH);
        intensityDropdown.setBounds(innerX + (dropW + dropGap) * 2, dropY, dropW, dropH);

        // Save Profile button — bottom-right of card
        int savW = 240, savH = 62;
        int savX = cardX + cardW - savW - 36;
        int savY = cardY + cardH - savH - 30;
        submitProfileButton.setBounds(savX, savY, savW, savH);
    }

    // ── Background + card painting ─────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Gradient background
        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Logo
        drawLogo(g2);

        int cardX = (int)(getWidth()  * 0.05);
        int cardY = (int)(getHeight() * 0.13);
        int cardW = (int)(getWidth()  * 0.90);
        int cardH = (int)(getHeight() * 0.75);

        // Card shadow
        g2.setColor(new Color(0, 0, 0, 18));
        g2.fillRoundRect(cardX + 4, cardY + 6, cardW, cardH, 28, 28);

        // Card body (gradient: light top → slightly more saturated bottom)
        GradientPaint cardGrad = new GradientPaint(
                0, cardY,        new Color(240, 242, 248),
                0, cardY + cardH, new Color(220, 224, 238));
        g2.setPaint(cardGrad);
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 28, 28);

        int innerX = cardX + 36;
        int innerY = cardY + 30;
        int btnSz  = 44;

        // "Make a Profile" title
        g2.setFont(FONT_TITLE);
        g2.setColor(TEXT_DARK);
        FontMetrics tfm = g2.getFontMetrics();
        int titleY = innerY + (btnSz + tfm.getAscent() - tfm.getDescent()) / 2;
        g2.drawString("Make a Profile", innerX + btnSz + 18, titleY);

        // "Preferences" section label
        int fieldH = 48;
        int fieldY = innerY + btnSz + 28;
        int prefLabelY = fieldY + fieldH + 14;
        g2.setFont(FONT_SECTION);
        g2.setColor(PREF_LABEL_COLOR);
        g2.drawString("Preferences", innerX, prefLabelY + g2.getFontMetrics().getAscent());

        g2.dispose();
        super.paintComponent(g);
    }

    private void drawLogo(Graphics2D g2) {
        BufferedImage logoImg = null;
        try {
            logoImg = ImageIO.read(getClass().getResource("/com/tranner/gui/logo.png"));
        } catch (IOException | IllegalArgumentException | NullPointerException ignored) {}

        int lx = (int)(getWidth() * 0.05);
        int ly = (int)(getHeight() * 0.03);

        if (logoImg != null) {
            g2.drawImage(logoImg.getScaledInstance(36, 36, Image.SCALE_SMOOTH), lx, ly, null);
        } else {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 28));
            g2.setColor(Color.WHITE);
            g2.drawString("\uD83C\uDF10", lx, ly + 28);
        }
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("TripSync", lx + 44, ly + 26);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DropdownMenu — self-contained expanding dropdown matching the wireframe
    // ══════════════════════════════════════════════════════════════════════════
    private class DropdownMenu extends JComponent {

        private final String   placeholder;
        private final String[] items;
        private String  selected   = null;
        private boolean open       = false;
        private Runnable onOpen    = null;
        private java.util.function.Consumer<String> listener;

        /** Height of the header (closed) portion. Set by setBounds. */
        private int headerH = 52;

        DropdownMenu(String placeholder, String[] items) {
            this.placeholder = placeholder;
            this.items       = items;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    int y = e.getY();
                    if (y <= headerH) {
                        // Toggle
                        if (!open) {
                            if (onOpen != null) onOpen.run();
                            open = true;
                        } else {
                            open = false;
                        }
                        repaint();
                        // Repaint parent so overlapping siblings redraw
                        if (getParent() != null) getParent().repaint();
                    } else if (open) {
                        // Click inside popup list
                        int idx = (y - headerH) / ITEM_H;
                        if (idx >= 0 && idx < items.length) {
                            selected = items[idx];
                            open = false;
                            if (listener != null) listener.accept(selected);
                            repaint();
                            if (getParent() != null) getParent().repaint();
                        }
                    }
                }
                @Override public void mouseEntered(MouseEvent e) { repaint(); }
                @Override public void mouseExited(MouseEvent e)   { repaint(); }
            });
        }

        void addSelectionListener(java.util.function.Consumer<String> l) { this.listener = l; }
        void setOnOpen(Runnable r) { this.onOpen = r; }
        String getSelected() { return selected; }

        void close() {
            if (open) { open = false; repaint(); }
        }

        @Override
        public void setBounds(int x, int y, int w, int h) {
            headerH = h; // h is the header height from doLayout
            // Expand the component's actual height to cover popup area
            int totalH = open ? headerH + items.length * ITEM_H : headerH;
            super.setBounds(x, y, w, totalH);
        }

        @Override
        public boolean contains(int x, int y) {
            int totalH = open ? headerH + items.length * ITEM_H : headerH;
            return x >= 0 && x < getWidth() && y >= 0 && y < totalH;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();

            // ── Header ────────────────────────────────────────────────────────
            // White fill
            g2.setColor(Color.WHITE);
            if (open) {
                // Rounded top only; square bottom where popup attaches
                g2.fillRoundRect(0, 0, w, headerH + 14, 14, 14);
                g2.fillRect(0, headerH - 1, w, 15); // flatten the bottom
            } else {
                g2.fillRoundRect(0, 0, w, headerH, 14, 14);
            }

            // Header border
            g2.setColor(DROPDOWN_BORDER);
            g2.setStroke(new BasicStroke(1.8f));
            if (open) {
                // Left, top, right — no bottom line
                g2.drawLine(1, 1, 1, headerH);
                g2.drawLine(1, 1, w - 1, 1);
                g2.drawLine(w - 1, 1, w - 1, headerH);
                // Rounded top corners only
                g2.drawArc(1, 1, 24, 24, 90, 90);
                g2.drawArc(w - 25, 1, 24, 24, 0, 90);
                g2.drawLine(13, 1, w - 13, 1);
                g2.drawLine(1, 13, 1, headerH);
                g2.drawLine(w - 1, 13, w - 1, headerH);
            } else {
                g2.drawRoundRect(1, 1, w - 2, headerH - 2, 14, 14);
            }

            // Label text (placeholder or selected value)
            String label = selected != null ? selected : placeholder;
            g2.setFont(FONT_DROPDOWN);
            g2.setColor(TEXT_DARK);
            FontMetrics fm = g2.getFontMetrics();
            int textY = (headerH - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(label, 16, textY);

            // Chevron ∨ / ∧
            int cx = w - 34, cy = open ? headerH / 2 + 2 : headerH / 2 - 3;
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(TEXT_DARK);
            if (open) {
                // ∧ pointing up
                g2.drawPolyline(
                        new int[]{cx, cx + 9, cx + 18},
                        new int[]{cy + 6, cy - 3, cy + 6}, 3);
            } else {
                // ∨ pointing down
                g2.drawPolyline(
                        new int[]{cx, cx + 9, cx + 18},
                        new int[]{cy, cy + 9, cy}, 3);
            }

            // ── Popup list ────────────────────────────────────────────────────
            if (open) {
                int popupH = items.length * ITEM_H;
                int py     = headerH;

                // White background for popup
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, py, w, popupH, 14, 14);
                g2.fillRect(0, py, w, 14); // flatten top of popup

                // Popup border (left, right, bottom-rounded)
                g2.setColor(DROPDOWN_BORDER);
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawLine(1, py, 1, py + popupH - 8);
                g2.drawLine(w - 1, py, w - 1, py + popupH - 8);
                g2.drawArc(1, py + popupH - 16, 14, 14, 180, 90);
                g2.drawArc(w - 15, py + popupH - 16, 14, 14, 270, 90);
                g2.drawLine(8, py + popupH - 1, w - 8, py + popupH - 1);

                // Items
                for (int i = 0; i < items.length; i++) {
                    int iy = py + i * ITEM_H;

                    // Separator
                    if (i > 0) {
                        g2.setColor(SEPARATOR_COLOR);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawLine(16, iy, w - 16, iy);
                    }

                    // Item text
                    g2.setFont(FONT_ITEM);
                    boolean isSelected = items[i].equals(selected);
                    g2.setColor(isSelected ? BUTTON_BG : TEXT_DARK);
                    if (isSelected) g2.setFont(FONT_DROPDOWN); // bold if selected
                    FontMetrics ifm = g2.getFontMetrics();
                    int itemTextY = iy + (ITEM_H - ifm.getHeight()) / 2 + ifm.getAscent();
                    g2.drawString(items[i], 16, itemTextY);
                }
            }

            g2.dispose();
        }

        @Override
        public void repaint() {
            // When open, we need a taller area repainted
            if (open) {
                int totalH = headerH + items.length * ITEM_H;
                super.setBounds(getX(), getY(), getWidth(), totalH);
            } else {
                super.setBounds(getX(), getY(), getWidth(), headerH);
            }
            super.repaint();
        }

        public void reset() {
            selected = null;
            close();
        }
    }

    // ── Helper: placeholder text field ────────────────────────────────────────
    private JTextField makePlaceholderField(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // White rounded background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(TEXT_PLACEHOLDER);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, 16, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_FIELD);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(new RoundedBorder(14, FIELD_BORDER, 1));
        field.setMargin(new Insets(0, 16, 0, 16));
        return field;
    }

    // ── Helper: back button ─────────────────────────────────────────────────
    private JButton makeBackButton() {
    JButton btn = new JButton() {
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // Circle fill
            g2.setColor(getModel().isRollover() ? new Color(210, 215, 230) : Color.WHITE);
            g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

            // Circle border
            g2.setColor(TEXT_DARK);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

            // Draw chevron < manually with two lines
            int cx = getWidth()  / 2;
            int cy = getHeight() / 2;
            int armLen = 7;  // length of each arm — tweak to taste
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(TEXT_DARK);
            // Top arm: from tip going up-right
            g2.drawLine(cx + 2, cy - armLen, cx - 4, cy);
            // Bottom arm: from tip going down-right
            g2.drawLine(cx - 4, cy, cx + 2, cy + armLen);

            g2.dispose();
            }
        };
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

    // ── Helper: rounded action button ─────────────────────────────────────────
    private JButton makeRoundedButton(String text, Font font, Color fg, Color bg) {
        Color hoverBg = new Color(
                Math.min(bg.getRed()   + 20, 255),
                Math.min(bg.getGreen() + 20, 255),
                Math.min(bg.getBlue()  + 35, 255));
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? bg.darker()
                           : getModel().isRollover() ? hoverBg : bg;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(fg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()  - fm.stringWidth(getText())) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        btn.setFont(font);
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

    // ── Rounded border ─────────────────────────────────────────────────────────
    private static class RoundedBorder extends AbstractBorder {
        private final int radius, thickness;
        private final Color color;
        RoundedBorder(int r, Color c, int t) { radius = r; color = c; thickness = t; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w - 1, h - 1, radius * 1, radius * 1);
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Public API — all original methods preserved
    // ══════════════════════════════════════════════════════════════════════════
    public JButton getSubmitProfileButton() { return submitProfileButton; }
    public JButton getBackToLoginButton()   { return backToLoginButton;   }

    public void resetFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        budgetGroup.clearSelection(); // TODO: fix null selection 
        transportGroup.clearSelection();
        intensityGroup.clearSelection();
        budgetDropdown.reset();
        transportDropdown.reset();
        intensityDropdown.reset();
    }

    /** Legacy: returns firstName for controller compatibility. */
    public String getPlace() { return firstNameField.getText().trim(); }

    public String getFirstName() { return firstNameField.getText().trim(); }
    public String getLastName()  { return lastNameField.getText().trim();  }

    public Preference.Budget getBudget() {
        if (budgetLow.isSelected())      return Preference.Budget.LOW;
        if (budgetModerate.isSelected()) return Preference.Budget.MODERATE;
        if (budgetHigh.isSelected())     return Preference.Budget.HIGH;
        if (budgetLuxury.isSelected())   return Preference.Budget.LUXURY;
        return null;
    }

    public Preference.TransportMode getTransportMode() {
        if (transportWalking.isSelected())   return Preference.TransportMode.WALKING;
        if (transportTransit.isSelected())   return Preference.TransportMode.PUBLIC_TRANSIT;
        if (transportRideshare.isSelected()) return Preference.TransportMode.RIDESHARE;
        if (transportCar.isSelected())       return Preference.TransportMode.RENTAL_CAR;
        if (transportBike.isSelected())      return Preference.TransportMode.BICYCLE;
        return null;
    }

    public Preference.Intensity getIntensity() {
        if (intensityRelaxed.isSelected())  return Preference.Intensity.RELAXED;
        if (intensityModerate.isSelected()) return Preference.Intensity.MODERATE;
        if (intensityActive.isSelected())   return Preference.Intensity.ACTIVE;
        if (intensityExtreme.isSelected())  return Preference.Intensity.EXTREME;
        return null;
    }

    //public Preference getPreference() {
    //    return new Preference(getBudget(), getPlace(), getTransportMode(), getIntensity());
    //}
}