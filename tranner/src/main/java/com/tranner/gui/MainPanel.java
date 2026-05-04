package com.tranner.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import com.tranner.model.itinerary.Itinerary;
import com.tranner.model.person.Person;
import com.tranner.model.weather.Weather;
import com.tranner.api.weatherApiClient;

public class MainPanel extends JPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Main Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 720);

        MainPanel mainPanel = new MainPanel();
        TripPanel tripPanel = new TripPanel();

        mainPanel.getAddTripButton().addActionListener(e -> {
            frame.setContentPane(tripPanel);
            frame.revalidate();
            frame.repaint();
        });

        // Save Trip → build Itinerary, add card, go back, then fetch weather in background
        tripPanel.getSaveButton().addActionListener(e -> {
            Itinerary it = tripPanel.buildItinerary(1);
            mainPanel.addTrip(it);
            frame.setContentPane(mainPanel);
            frame.revalidate();
            frame.repaint();

            String city = tripPanel.getSearchedCity();
            if (!city.isEmpty()) {
                new Thread(() -> {
                    weatherApiClient client = new weatherApiClient();
                    Weather w = client.fetchWeather(city);
                    if (w != null) {
                        it.setWeather(w);
                        SwingUtilities.invokeLater(mainPanel::refreshTrips);
                    }
                }).start();
            }
        });

        // Back → return to MainPanel without saving
        tripPanel.getBackButton().addActionListener(e -> {
            frame.setContentPane(mainPanel);
            frame.revalidate();
            frame.repaint();
        });

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static final Color BG_TOP = new Color(53, 80, 161);
    private static final Color BG_BOTTOM = new Color(163, 166, 199);
    private static final Color BUTTON_BG = new Color(42, 68, 145);
    private static final Color TEXT_BLUE = new Color(30, 55, 130);
    private static final Color TEXT_GRAY = new Color(60, 70, 100);
    private static final Color TEXT_PLACEHOLDER = new Color(160, 165, 185);
    private static final Color FIELD_BORDER = new Color(200, 205, 220);
    private static final Color ITEM_BG = new Color(180, 185, 205);
    private static final Color AVATAR_BG = new Color(200, 210, 235);
    private static final Color PLACEHOLDER_BG = new Color(220, 224, 238);

    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 29);
    private static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_SEARCH = new Font("SansSerif", Font.PLAIN, 16);

    private JPanel tripsCard;
    private JPanel peopleCard;

    private JTextField tripSearchField;
    private JTextField peopleSearchField;

    private JButton addTripButton;
    private JButton addPersonButton;

    private JPanel tripsListPanel;
    private JPanel peopleListPanel;

    private JScrollPane tripsScrollPane;
    private JScrollPane peopleScrollPane;

    private final List<JButton> tripOpenButtons = new ArrayList<>();
    private final List<JButton> personEditButtons = new ArrayList<>();
    private final List<JButton> personDeleteButtons = new ArrayList<>();
    private final List<Itinerary> savedTrips = new ArrayList<>();

    private List<Person> companions;

    public MainPanel() {
        setOpaque(false);
        setLayout(null);

        tripsCard = new RoundedCardPanel();
        tripsCard.setLayout(null);

        peopleCard = new RoundedCardPanel();
        peopleCard.setLayout(null);

        tripSearchField = makeSearchField("Search");
        peopleSearchField = makeSearchField("Search");

        addTripButton = makeSquarePlusButton();
        addPersonButton = makeSquarePlusButton();

        tripsListPanel = new JPanel();
        tripsListPanel.setOpaque(false);
        tripsListPanel.setLayout(null);

        peopleListPanel = new JPanel();
        peopleListPanel.setOpaque(false);
        peopleListPanel.setLayout(null);

        tripsScrollPane = makeScrollPane(tripsListPanel);
        peopleScrollPane = makeScrollPane(peopleListPanel);

        add(tripsCard);
        add(peopleCard);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buildLayout();
            }
        });

        SwingUtilities.invokeLater(this::buildLayout);
    }

    private void buildLayout() {
        int W = getWidth();
        int H = getHeight();
        if (W == 0 || H == 0) return;

        tripsCard.removeAll();
        peopleCard.removeAll();

        tripOpenButtons.clear();
        personEditButtons.clear();
        personDeleteButtons.clear();

        int cardX = (int)(W * 0.025);
        int cardY = (int)(H * 0.10);
        int cardW = (int)(W * 0.950);
        int cardH = (int)(H * 0.855);

        int pad = 24;

        int innerX = cardX + pad;
        int innerW = cardW - pad * 2;
        int innerY = cardY + pad + 35;
        int innerH = cardH - pad * 2 - 50;

        int gap = 20;
        int leftW = (int)(innerW * 0.62);
        int rightW = innerW - leftW - gap;

        tripsCard.setBounds(innerX, innerY, leftW, innerH);
        peopleCard.setBounds(innerX + leftW + gap, innerY, rightW, innerH);

        layoutTripsCard(leftW, innerH);
        layoutPeopleCard(rightW, innerH);

        revalidate();
        repaint();
    }

    private void layoutTripsCard(int W, int H) {
        int pad = 22;

        JLabel title = makeTitleLabel("My Trips");
        title.setBounds(pad, 22, 160, 38);
        addTripButton.setBounds(pad + 145, 25, 28, 28);

        tripsCard.add(title);
        tripsCard.add(addTripButton);

        tripSearchField.setBounds(pad, 75, W - pad * 2, 42);
        tripsCard.add(tripSearchField);

        tripsListPanel.removeAll();

        int listW = W - pad * 2;
        int listH = H - 134 - 18;

        tripsScrollPane.setBounds(pad, 134, listW, listH);
        tripsCard.add(tripsScrollPane);

        int y = 0;
        for (Itinerary trip : savedTrips) {
            String subtitle = trip.getStartDate() + " – " + trip.getEndDate();
            TripPreviewCard card = new TripPreviewCard(trip.getTripName(), subtitle, true, trip.getWeather());
            card.setBounds(0, y, listW - 8, 140);
            tripsListPanel.add(card);
            tripOpenButtons.add(card.getOpenButton());
            y += 150;
        }

        tripsListPanel.setPreferredSize(new Dimension(listW, Math.max(y, 10)));
        tripsListPanel.revalidate();
        tripsListPanel.repaint();
    }

    private void layoutPeopleCard(int W, int H) {
        int pad = 18;

        JLabel title = makeTitleLabel("My People");
        title.setBounds(pad, 22, 190, 38);
        addPersonButton.setBounds(pad + 172, 25, 28, 28);

        peopleCard.add(title);
        peopleCard.add(addPersonButton);

        peopleSearchField.setBounds(pad, 75, W - pad * 2, 42);
        peopleCard.add(peopleSearchField);

        peopleListPanel.removeAll();

        int listW = W - pad * 2;
        int listH = H - 134 - 18;

        peopleScrollPane.setBounds(pad, 134, listW, listH);
        peopleCard.add(peopleScrollPane);

        int y = 0;

        for (int i = 0; i < companions.size(); i++) {
            String name = companions.get(i).getFirstName() + " " + companions.get(i).getLastName();
            String detail = "Budget: " + companions.get(i).getPreference().getBudget()
                    + ", Transport: " + companions.get(i).getPreference().getTransport()
                    + ", Intensity: " + companions.get(i).getPreference().getIntensity();
            PersonRow row = new PersonRow(name, detail);
            row.setBounds(0, y, listW - 8, 64);
            peopleListPanel.add(row);

            personEditButtons.add(row.getEditButton());
            personDeleteButtons.add(row.getDeleteButton());

            y += 77;
        }

        peopleListPanel.setPreferredSize(new Dimension(listW, y));
        peopleListPanel.revalidate();
        peopleListPanel.repaint();
    }

    private JScrollPane makeScrollPane(JPanel contentPanel) {
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setUnitIncrement(16);
        bar.setPreferredSize(new Dimension(6, 0));
        bar.setOpaque(false);

        return scrollPane;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawLogo(g2);

        int cX = (int)(getWidth() * 0.025);
        int cY = (int)(getHeight() * 0.10);
        int cW = (int)(getWidth() * 0.950);
        int cH = (int)(getHeight() * 0.855);

        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(cX + 4, cY + 6, cW, cH, 28, 28);

        g2.setColor(new Color(245, 247, 252, 210));
        g2.fillRoundRect(cX, cY, cW, cH, 28, 28);

        g2.dispose();
        super.paintComponent(g);
    }

    private void drawLogo(Graphics2D g2) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(getClass().getResource("/com/tranner/gui/logo.png"));
        } catch (Exception ignored) {}

        int lx = (int)(getWidth() * 0.025);
        int ly = (int)(getHeight() * 0.02);

        if (img != null) {
            g2.drawImage(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH), lx, ly, null);
        } else {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 26));
            g2.setColor(Color.WHITE);
            g2.drawString("\uD83C\uDF10", lx, ly + 26);
        }

        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("TripSync", lx + 40, ly + 23);
    }

    private JLabel makeTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_BLUE);
        return label;
    }

    private JTextField makeSearchField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                super.paintComponent(g);

                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setFont(FONT_SEARCH);
                    g2.setColor(TEXT_PLACEHOLDER);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, 40,
                            (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }

                drawSearchIcon(g2, 14, getHeight() / 2);

                g2.dispose();
            }
        };

        field.setOpaque(false);
        field.setFont(FONT_SEARCH);
        field.setForeground(TEXT_BLUE);
        field.setCaretColor(TEXT_BLUE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 12));
        return field;
    }

    private JButton makeSquarePlusButton() {
        JButton btn = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()
                        ? BUTTON_BG.darker()
                        : getModel().isRollover()
                        ? new Color(62, 88, 165)
                        : BUTTON_BG;

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 28));

                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("+",
                        (getWidth() - fm.stringWidth("+")) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent() - 1);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton makeArrowButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 75));
                    g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }

                g2.setColor(TEXT_BLUE);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int cx = getWidth() / 2 - 3;
                int cy = getHeight() / 2;

                g2.drawLine(cx, cy - 6, cx + 7, cy);
                g2.drawLine(cx + 7, cy, cx, cy + 6);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton makeEditButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(7, 19, 20, 6);

                g2.fillPolygon(
                        new int[]{19, 24, 21},
                        new int[]{5, 10, 13},
                        3
                );

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton makeTrashButton() {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 70));
                    g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }

                g2.setColor(TEXT_BLUE);
                g2.setStroke(new BasicStroke(2f));

                int x = 8;
                int y = 8;

                g2.drawRect(x, y + 5, 11, 14);
                g2.drawLine(x - 1, y + 2, x + 12, y + 2);
                g2.drawLine(x + 3, y, x + 8, y);
                g2.drawLine(x + 4, y + 8, x + 4, y + 16);
                g2.drawLine(x + 8, y + 8, x + 8, y + 16);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void drawSearchIcon(Graphics2D g2, int x, int cy) {
        g2.setColor(TEXT_PLACEHOLDER);
        g2.setStroke(new BasicStroke(1.9f));
        g2.drawOval(x, cy - 7, 14, 14);
        g2.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + 12, cy + 5, x + 18, cy + 11);
    }

    private void drawTinyPeople(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1.3f));

        for (int i = 0; i < 4; i++) {
            int px = x + i * 18;
            g2.drawOval(px + 4, y, 7, 7);
            g2.drawArc(px, y + 6, 15, 15, 25, 130);
        }
    }

    private class RoundedCardPanel extends JPanel {
        RoundedCardPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(5, 7, getWidth() - 5, getHeight() - 7, 24, 24);

            g2.setColor(new Color(245, 247, 252, 235));
            g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 7, 24, 24);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private class TripPreviewCard extends JPanel {
        private final String name;
        private final String subtitle;
        private final boolean oneBox;
        private final JButton openButton;
        private final Weather weather;

        TripPreviewCard(String name, String subtitle, boolean oneBox, Weather weather) {
            this.name = name;
            this.subtitle = subtitle;
            this.oneBox = oneBox;
            this.weather = weather;

            setOpaque(false);
            setLayout(null);

            openButton = makeArrowButton();
            add(openButton);
        }

        JButton getOpenButton() {
            return openButton;
        }

        @Override
        public void doLayout() {
            openButton.setBounds(getWidth() - 30, 8, 24, 24);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setColor(ITEM_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            g2.setFont(FONT_BOLD);
            g2.setColor(TEXT_GRAY);
            g2.drawString(name, 13, 28);

            g2.setFont(FONT_BODY);
            g2.drawString(subtitle, 13, 50);

            g2.setFont(FONT_BODY);
            g2.drawString("People", getWidth() - 175, 28);
            drawTinyPeople(g2, getWidth() - 175, 41);

            if (oneBox) {
                // weather box (13, 70, 165x65)
                g2.setColor(PLACEHOLDER_BG);
                g2.fillRoundRect(13, 70, 165, 65, 8, 8);
                if (weather != null) {
                    g2.setFont(new Font("SansSerif", Font.BOLD, 18));
                    g2.setColor(TEXT_GRAY);
                    g2.drawString(String.format("%.0f°C", weather.getTempCelsius()), 20, 100);
                    g2.setFont(FONT_BODY);
                    g2.drawString(weather.getConditionText(), 20, 120);
                } else {
                    g2.setFont(FONT_BODY);
                    g2.setColor(TEXT_PLACEHOLDER);
                    g2.drawString("weather loading...", 20, 105);
                }

                g2.setFont(FONT_BOLD);
                g2.setColor(Color.BLACK);
                g2.drawString("Recommendations", 195, 93);
            } else {
                drawPlaceholder(g2, 13, 65, 250, 72, "map");
                drawPlaceholder(g2, 278, 65, getWidth() - 291, 72, "weather");
            }

            g2.dispose();
            super.paintComponent(g);
        }

        private void drawPlaceholder(Graphics2D g2, int x, int y, int w, int h, String text) {
            g2.setColor(PLACEHOLDER_BG);
            g2.fillRoundRect(x, y, w, h, 8, 8);

            g2.setFont(FONT_BODY);
            g2.setColor(Color.BLACK);

            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text,
                    x + (w - fm.stringWidth(text)) / 2,
                    y + (h - fm.getHeight()) / 2 + fm.getAscent());
        }
    }

    private class PersonRow extends JPanel {
        private final String name;
        private final String detail;
        private final JButton editButton;
        private final JButton deleteButton;

        PersonRow(String name, String detail) {
            this.name = name;
            this.detail = detail;

            setOpaque(false);
            setLayout(null);

            editButton = makeEditButton();
            deleteButton = makeTrashButton();

            add(editButton);
            add(deleteButton);
        }

        JButton getEditButton() {
            return editButton;
        }

        JButton getDeleteButton() {
            return deleteButton;
        }

        @Override
        public void doLayout() {
            editButton.setBounds(getWidth() - 42, 22, 23, 23);
            deleteButton.setBounds(getWidth() - 21, 22, 23, 23);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setColor(ITEM_BG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            drawAvatar(g2, 14, 11, 43);

            g2.setFont(FONT_BODY);
            g2.setColor(TEXT_GRAY);
            g2.drawString(name, 68, 26);

            g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
            g2.drawString(detail, 68, 48);

            g2.dispose();
            super.paintComponent(g);
        }

        private void drawAvatar(Graphics2D g2, int x, int y, int size) {
            g2.setColor(AVATAR_BG);
            g2.fillOval(x, y, size, size);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(x + 15, y + 9, 13, 13);
            g2.drawArc(x + 9, y + 22, 25, 22, 20, 140);
        }
    }

    public JButton getAddTripButton() {
        return addTripButton;
    }

    public JButton getAddPersonButton() {
        return addPersonButton;
    }

    public List<JButton> getTripOpenButtons() {
        return Collections.unmodifiableList(tripOpenButtons);
    }

    public List<JButton> getPersonEditButtons() {
        return Collections.unmodifiableList(personEditButtons);
    }

    public List<JButton> getPersonDeleteButtons() {
        return Collections.unmodifiableList(personDeleteButtons);
    }

    public void addTrip(Itinerary trip) {
        savedTrips.add(trip);
        buildLayout();
    }

    public void refreshTrips() {
        buildLayout();
    }

    public List<Itinerary> getSavedTrips() {
        return Collections.unmodifiableList(savedTrips);
    }

    public List<Person> getCompanions() {
        return Collections.unmodifiableList(companions);
    }

    public void setCompanions(List<Person> companions) {
        this.companions = new ArrayList<>(companions);
    }
}