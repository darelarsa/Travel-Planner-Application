package com.tranner.gui;

import com.tranner.Map.MapViewClient;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

import com.tranner.Map.PlacesApiClient;
import org.json.simple.JSONObject;


public class TripPanel extends JPanel {
    // For testing layout only - remove later
    public static void main(String[] args) {
    JFrame frame = new JFrame("Trip Panel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);

    frame.add(new TripPanel()); 

    frame.setVisible(true);
    }

    // ── Palette ───────────────────────────────────────────────────────────────
    private static final Color BG_TOP           = new Color(53,  80,  161);
    private static final Color BG_BOTTOM        = new Color(163, 166, 199);
    private static final Color CARD_BG_TOP      = new Color(240, 242, 248);
    private static final Color CARD_BG_BOT      = new Color(220, 224, 238);
    private static final Color BUTTON_BG        = new Color(42,  68,  145);
    private static final Color TEXT_DARK        = new Color(30,  55,  130);
    private static final Color TEXT_PLACEHOLDER = new Color(160, 165, 185);
    private static final Color FIELD_BORDER     = new Color(200, 205, 220);
    private static final Color SEPARATOR        = new Color(210, 212, 222);
    private static final Color CAL_RANGE_BG     = new Color(200, 210, 235);
    private static final Color CAL_SELECTED_BG  = new Color(42,  68,  145);
    private static final Color PLACEHOLDER_PANEL= new Color(180, 185, 205);
    private static final Color ITINERARY_LABEL  = new Color(60,  70,  100);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TRIP_NAME  = new Font("SansSerif", Font.BOLD,  26);
    private static final Font FONT_SECTION    = new Font("SansSerif", Font.BOLD,  20);
    private static final Font FONT_BODY       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BUTTON     = new Font("SansSerif", Font.PLAIN, 20);
    private static final Font FONT_DROPDOWN   = new Font("SansSerif", Font.BOLD,  14);
    private static final Font FONT_ITEM       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_CAL_HDR    = new Font("SansSerif", Font.BOLD,  12);
    private static final Font FONT_CAL_DAY    = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_SEARCH     = new Font("SansSerif", Font.PLAIN, 16);
    private static final Font FONT_ATTRACT    = new Font("SansSerif", Font.BOLD,  15);

    // ── Child components ──────────────────────────────────────────────────────
    private JButton         backButton;
    private JTextField      tripNameField;
    private JPanel          searchBar;
    private JTextField      searchField;
    private MapViewClient   mapView;
    private AttractionPanel attractionPanel;
    private MiniCalendar    calendar;
    private MultiDropdown   companionsDropdown;
    private ItineraryPanel  itineraryPanel;
    private JButton         saveButton;

    // ── State ─────────────────────────────────────────────────────────────────
    private String             selectedAttractionName = null;
    private String             selectedAttractionType = null;
    private final List<String> itineraryAttractions   = new ArrayList<>();

    // ─────────────────────────────────────────────────────────────────────────
    public TripPanel() {
        setOpaque(false);
        setLayout(null);

        backButton = makeBackButton();

        tripNameField = makeTripNameField("Trip Name");

        searchField = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setFont(FONT_SEARCH);
                    g2.setColor(TEXT_PLACEHOLDER);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString("Search", 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                    g2.dispose();
                }
            }
        };
        searchField.setOpaque(false);
        searchField.setFont(FONT_SEARCH);
        searchField.setForeground(TEXT_DARK);
        searchField.setCaretColor(TEXT_DARK);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 8));
        searchField.addActionListener(e -> handleSearch(searchField.getText().trim()));

        JLabel iconLabel = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cy = getHeight() / 2, r = 7;
                int ox = (getWidth() - r * 2 - 4) / 2;
                g2.setColor(TEXT_PLACEHOLDER);
                g2.setStroke(new BasicStroke(1.9f));
                g2.drawOval(ox, cy - r, r * 2, r * 2);
                g2.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(ox + r * 2 - 1, cy + r - 3, ox + r * 2 + 5, cy + r + 3);
                g2.dispose();
            }
        };
        iconLabel.setPreferredSize(new Dimension(38, 42));
        iconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { searchField.requestFocusInWindow(); }
        });

        searchBar = new JPanel(new BorderLayout(0, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        searchBar.setOpaque(false);
        searchBar.add(iconLabel, BorderLayout.WEST);
        searchBar.add(searchField, BorderLayout.CENTER);

        mapView = new MapViewClient();
        mapView.getConnector().setOnMarkerClicked((name, type) ->
            SwingUtilities.invokeLater(() -> {
                selectedAttractionName = name;
                selectedAttractionType = type;
                attractionPanel.setAttraction(name, type);
            })
        );

        attractionPanel = new AttractionPanel();
        attractionPanel.setAddListener(() -> {
            if (selectedAttractionName != null) {
                itineraryAttractions.add(selectedAttractionName);
                itineraryPanel.refresh();
            }
        });

        calendar = new MiniCalendar();
        calendar.setRangeListener((s, e) -> itineraryPanel.refresh());

        //placeholder companion names 
        companionsDropdown = new MultiDropdown("Companions",
                new String[]{"Alice", "Bob", "person3", "person4", "person5"});
        companionsDropdown.setChangeListener(() -> itineraryPanel.refresh());

        itineraryPanel = new ItineraryPanel();

        saveButton = makeRoundedButton("Save Trip", FONT_BUTTON, Color.WHITE, BUTTON_BG);

        add(backButton);
        add(tripNameField);
        add(searchBar);
        add(mapView);
        add(attractionPanel);
        add(calendar);
        add(itineraryPanel);
        add(saveButton);
        add(companionsDropdown); 

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) { doLayout(); }
        });
    }

    // ── Layout ─────────────────────────────────────────────────────────────────
    @Override
    public void doLayout() {
        int W = getWidth(), H = getHeight();
        if (W == 0 || H == 0) return;

        int cardX = (int)(W * 0.025);
        int cardY = (int)(H * 0.10);
        int cardW = (int)(W * 0.950);
        int cardH = (int)(H * 0.855);
        int pad   = 24;

        int innerX = cardX + pad;
        int innerY = cardY + pad;
        int innerW = cardW - pad * 2;

        // Right column — wider so Companions dropdown has room (~38% of inner)
        int rightColW = (int)(innerW * 0.38);
        int rightColX = innerX + innerW - rightColW;
        int leftW     = innerW - rightColW - pad;

        // ── Row 1: back btn + trip name  |  calendar + companions ─────────────
        int row1H = 56;
        int btnSz = 44;
        backButton.setBounds(innerX, innerY + (row1H - btnSz) / 2, btnSz, btnSz);
        int nameX = innerX + btnSz + 12;
        int nameW = leftW - btnSz - 12;
        tripNameField.setBounds(nameX, innerY, nameW, row1H);

        // Calendar takes ~55% of right col; companions gets the rest
        int calW = (int)(rightColW * 0.55);
        int calH = 148;
        calendar.setBounds(rightColX, innerY, calW, calH);

        int compX = rightColX + calW + 8;
        int compW = rightColW - calW - 8;  // ~45% of right col — plenty of room
        companionsDropdown.setBounds(compX, innerY, compW, 40);

        // ── Row 2: search bar — unified field with icon label inside panel
        int searchY = innerY + row1H + 14;
        int searchH = 42;
        searchBar.setBounds(innerX, searchY, leftW, searchH);

        // ── Row 3: map | attraction panel  and  itinerary ────────────────────
        int contentY = searchY + searchH + 14;
        int contentH = cardY + cardH - contentY - pad;

        // Map gets 65%, attraction panel gets 35% — less cramped for both
        int mapW  = (int)(leftW * 0.65);
        int attrW = leftW - mapW - 10;
        mapView.setBounds(innerX, contentY, mapW, contentH);
        attractionPanel.setBounds(innerX + mapW + 10, contentY, attrW, contentH);

        // Itinerary 
        int itinY = innerY + calH + 14;
        int itinH = cardY + cardH - itinY - pad - 72;
        itineraryPanel.setBounds(rightColX, itinY, rightColW, itinH);

        // Save Trip button — bottom-right
        int savW = 200, savH = 58;
        saveButton.setBounds(cardX + cardW - savW - pad,
                             cardY + cardH - savH - 20, savW, savH);
    }

    // ── Background + card ──────────────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
        g2.fillRect(0, 0, getWidth(), getHeight());
        drawLogo(g2);

        int cX = (int)(getWidth()  * 0.025), cY = (int)(getHeight() * 0.10);
        int cW = (int)(getWidth()  * 0.950), cH = (int)(getHeight() * 0.855);

        g2.setColor(new Color(0, 0, 0, 20));
        g2.fillRoundRect(cX + 4, cY + 6, cW, cH, 28, 28);

        g2.setPaint(new GradientPaint(0, cY, CARD_BG_TOP, 0, cY + cH, CARD_BG_BOT));
        g2.fillRoundRect(cX, cY, cW, cH, 28, 28);

        g2.dispose();
        super.paintComponent(g);
    }

    private void drawLogo(Graphics2D g2) {
        BufferedImage img = null;
        try { img = ImageIO.read(getClass().getResource("/com/tranner/gui/logo.png")); }
        catch (Exception ignored) {}
        int lx = (int)(getWidth() * 0.025), ly = (int)(getHeight() * 0.02);
        if (img != null)
            g2.drawImage(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH), lx, ly, null);
        else {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 26));
            g2.setColor(Color.WHITE);
            g2.drawString("\uD83C\uDF10", lx, ly + 26);
        }
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("TripSync", lx + 40, ly + 23);
    }

    private void handleSearch(String query) {
        if (query.isEmpty()) return;
        System.out.println("[TripPanel] Search: " + query);

        new Thread(() -> {
            double [] coords = geocodeCity(query);
            if (coords == null) return;
            List<JSONObject> attractions = PlacesApiClient.searchAttraction(query);
            System.out.println("[TripPanel] Got " + attractions.size() + " attractions");

            
            SwingUtilities.invokeLater(() -> {
                mapView.clearMarkers();
                if (coords != null) {
                    mapView.setCity(coords[0], coords[1], 12);
                }
                for (JSONObject p : attractions) {
                    mapView.addMarker(PlacesApiClient.getLat(p),
                    PlacesApiClient.getLng(p),
                    PlacesApiClient.getName(p), "attraction");
                }
            });
        }).start();
    }
    private double[] geocodeCity(String city) {
    try {
        String urlStr = "https://geocoding-api.open-meteo.com/v1/search?name="
                + city.replace(" ", "+") + "&count=1&language=en&format=json";
        java.net.HttpURLConnection conn =
                (java.net.HttpURLConnection) new java.net.URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        java.util.Scanner sc = new java.util.Scanner(conn.getInputStream());
        StringBuilder sb = new StringBuilder();
        while (sc.hasNextLine()) sb.append(sc.nextLine());
        sc.close();

        org.json.simple.JSONObject root =
                (org.json.simple.JSONObject) new org.json.simple.parser.JSONParser().parse(sb.toString());
        org.json.simple.JSONArray results =
                (org.json.simple.JSONArray) root.get("results");

        if (results == null || results.isEmpty()) {
            System.err.println("[TripPanel] City not found: " + city);
            return null;
        }
        org.json.simple.JSONObject loc = (org.json.simple.JSONObject) results.get(0);
        return new double[]{
            ((Number) loc.get("latitude")).doubleValue(),
            ((Number) loc.get("longitude")).doubleValue()
        };
    } catch (Exception e) {
        System.err.println("[TripPanel] Geocode error: " + e.getMessage());
        return null;
    }
}

    // ══════════════════════════════════════════════════════════════════════════
    //  MiniCalendar
    // ══════════════════════════════════════════════════════════════════════════
    private class MiniCalendar extends JComponent {
        private YearMonth displayMonth = YearMonth.now();
        private LocalDate rangeStart   = null;
        private LocalDate rangeEnd     = null;
        private LocalDate hoverDate    = null;
        private RangeListener rangeListener;

        private int cellW, cellH, gridX0, gridY0;
        private static final int HEADER_H = 22, DOW_H = 18, ROWS = 6, COLS = 7;

        interface RangeListener { void changed(LocalDate s, LocalDate e); }

        MiniCalendar() {
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { handleClick(e.getX(), e.getY()); }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    LocalDate d = dateAt(e.getX(), e.getY());
                    if (!Objects.equals(d, hoverDate)) { hoverDate = d; repaint(); }
                }
            });
        }

        void setRangeListener(RangeListener l) { this.rangeListener = l; }
        LocalDate getRangeStart() { return rangeStart; }
        LocalDate getRangeEnd()   { return rangeEnd; }

        private void handleClick(int mx, int my) {
            if (my < HEADER_H) {
                if (mx < getWidth() / 2) displayMonth = displayMonth.minusMonths(1);
                else                     displayMonth = displayMonth.plusMonths(1);
                repaint(); return;
            }
            LocalDate d = dateAt(mx, my);
            if (d == null) return;
            if (rangeStart == null || rangeEnd != null) {
                rangeStart = d; rangeEnd = null;
            } else {
                if (d.isBefore(rangeStart)) { rangeEnd = rangeStart; rangeStart = d; }
                else rangeEnd = d;
                if (rangeListener != null) rangeListener.changed(rangeStart, rangeEnd);
            }
            repaint();
        }

        private LocalDate dateAt(int mx, int my) {
            if (cellW == 0) return null;
            int col = (mx - gridX0) / cellW, row = (my - gridY0) / cellH;
            if (col < 0 || col >= COLS || row < 0 || row >= ROWS) return null;
            int startDow = displayMonth.atDay(1).getDayOfWeek().getValue() % 7;
            int dayNum   = row * COLS + col - startDow + 1;
            if (dayNum < 1 || dayNum > displayMonth.lengthOfMonth()) return null;
            return displayMonth.atDay(dayNum);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int W = getWidth(), H = getHeight();
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, W, H, 12, 12);
            g2.setColor(FIELD_BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, W - 1, H - 1, 12, 12);

            // Month header
            g2.setFont(FONT_CAL_HDR);
            g2.setColor(TEXT_DARK);
            String hdr = displayMonth.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                       + " " + displayMonth.getYear();
            FontMetrics hfm = g2.getFontMetrics();
            g2.drawString(hdr, (W - hfm.stringWidth(hdr)) / 2, HEADER_H - 5);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString("<", 8, HEADER_H - 5);
            FontMetrics afm = g2.getFontMetrics();
            g2.drawString(">", W - afm.stringWidth(">") - 8, HEADER_H - 5);

            // Day-of-week labels
            String[] dows = {"S","M","T","W","T","F","S"};
            cellW  = W / COLS;
            cellH  = (H - HEADER_H - DOW_H) / ROWS;
            gridX0 = 0;
            gridY0 = HEADER_H + DOW_H;

            g2.setFont(FONT_CAL_HDR);
            g2.setColor(new Color(120, 130, 160));
            for (int c = 0; c < COLS; c++) {
                FontMetrics dfm = g2.getFontMetrics();
                g2.drawString(dows[c],
                        gridX0 + c * cellW + (cellW - dfm.stringWidth(dows[c])) / 2,
                        HEADER_H + DOW_H - 3);
            }

            // Cells
            int startDow    = displayMonth.atDay(1).getDayOfWeek().getValue() % 7;
            int daysInMonth = displayMonth.lengthOfMonth();
            LocalDate effEnd = rangeEnd;
            if (rangeStart != null && rangeEnd == null && hoverDate != null)
                effEnd = hoverDate.isBefore(rangeStart) ? rangeStart : hoverDate;

            g2.setFont(FONT_CAL_DAY);
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    int dayNum = row * COLS + col - startDow + 1;
                    if (dayNum < 1 || dayNum > daysInMonth) continue;
                    LocalDate d  = displayMonth.atDay(dayNum);
                    int cx = gridX0 + col * cellW;
                    int cy = gridY0 + row * cellH;

                    boolean isStart = d.equals(rangeStart);
                    boolean isEnd   = d.equals(rangeEnd);
                    boolean inRange = rangeStart != null && effEnd != null
                                      && !d.isBefore(rangeStart) && !d.isAfter(effEnd);

                    if (inRange && !isStart && !isEnd) {
                        g2.setColor(CAL_RANGE_BG);
                        g2.fillRect(cx, cy + 2, cellW, cellH - 4);
                    }
                    if (isStart || isEnd) {
                        g2.setColor(CAL_SELECTED_BG);
                        int dia = Math.min(cellW, cellH) - 4;
                        g2.fillOval(cx + (cellW - dia) / 2, cy + (cellH - dia) / 2, dia, dia);
                    }

                    FontMetrics fm = g2.getFontMetrics();
                    String ds = String.valueOf(dayNum);
                    int tx = cx + (cellW - fm.stringWidth(ds)) / 2;
                    int ty = cy + (cellH + fm.getAscent() - fm.getDescent()) / 2;
                    g2.setColor((isStart || isEnd) ? Color.WHITE : TEXT_DARK);
                    g2.drawString(ds, tx, ty);
                }
            }
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  MultiDropdown 
    // ══════════════════════════════════════════════════════════════════════════
    private class MultiDropdown extends JComponent {
        private final String      placeholder;   // always shown in header
        private String[]          items;
        private final Set<String> selected = new LinkedHashSet<>();
        private JWindow           popup    = null;
        private PopupPanel        popupPanel = null;
        private Runnable          changeListener;

        private static final int ITEM_H  = 40;
        private static final int RADIUS  = 10;
        private int headerH = 40;

        MultiDropdown(String placeholder, String[] items) {
            this.placeholder = placeholder;
            this.items       = items;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { togglePopup(); }
                @Override public void mouseEntered(MouseEvent e) { repaint(); }
                @Override public void mouseExited(MouseEvent e)  { repaint(); }
            });
        }

        void setChangeListener(Runnable r) { changeListener = r; }
        Set<String> getSelected() { return selected; }

        @Override public void setBounds(int x, int y, int w, int h) {
            headerH = h;
            super.setBounds(x, y, w, h); // always only header height — popup is a JWindow
        }

        private boolean isOpen() { return popup != null && popup.isVisible(); }

        private void togglePopup() {
            if (isOpen()) closePopup();
            else          openPopup();
        }

        private void openPopup() {
            Window owner = SwingUtilities.getWindowAncestor(this);
            if (owner == null) return;

            int popW = getWidth();
            int popH = RADIUS + items.length * ITEM_H + RADIUS;

            popupPanel = new PopupPanel();
            popup = new JWindow(owner);
            popup.setBackground(new Color(0, 0, 0, 0));
            popup.add(popupPanel);
            popup.setSize(popW, popH);

            Point loc = getLocationOnScreen();
            popup.setLocation(loc.x, loc.y + headerH);
            popup.setVisible(true);

            // Close when clicking outside
            Toolkit.getDefaultToolkit().addAWTEventListener(evt -> {
                if (evt instanceof MouseEvent me && me.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (isOpen()) {
                        Point click = me.getLocationOnScreen();
                        if (!popup.getBounds().contains(click)
                                && !getBounds().contains(SwingUtilities.convertPoint(
                                        (Component) me.getSource(), me.getPoint(), MultiDropdown.this))) {
                            closePopup();
                        }
                    }
                }
            }, AWTEvent.MOUSE_EVENT_MASK);

            repaint();
        }

        private void closePopup() {
            if (popup != null) {
                popup.setVisible(false);
                popup.dispose();
                popup = null;
                popupPanel = null;
            }
            repaint();
        }

        // Inner panel drawn inside the JWindow
        private class PopupPanel extends JPanel {
            PopupPanel() {
                setOpaque(false);
                setPreferredSize(new Dimension(getWidth(), RADIUS + items.length * ITEM_H + RADIUS));
                addMouseListener(new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) {
                        int idx = (e.getY() - RADIUS) / ITEM_H;
                        if (idx >= 0 && idx < items.length) {
                            String item = items[idx];
                            if (selected.contains(item)) selected.remove(item);
                            else selected.add(item);
                            if (changeListener != null) changeListener.run();
                            repaint();
                            MultiDropdown.this.repaint();
                        }
                    }
                });
            }

            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();

                // Rounded white card
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, w, h, RADIUS * 1, RADIUS * 1);
                g2.setColor(TEXT_DARK);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, RADIUS * 1, RADIUS * 1);

                for (int i = 0; i < items.length; i++) {
                    int iy = RADIUS + i * ITEM_H;

                    if (i > 0) {
                        g2.setColor(SEPARATOR);
                        g2.setStroke(new BasicStroke(1f));
                        g2.drawLine(10, iy, w - 10, iy);
                    }

                    boolean isSel = selected.contains(items[i]);

                    // Checkbox circle
                    g2.setColor(isSel ? BUTTON_BG : FIELD_BORDER);
                    g2.setStroke(new BasicStroke(1.4f));
                    g2.drawOval(10, iy + (ITEM_H - 14) / 2, 14, 14);
                    if (isSel) {
                        g2.setColor(BUTTON_BG);
                        g2.fillOval(13, iy + (ITEM_H - 14) / 2 + 3, 8, 8);
                    }

                    // Item label — full "First Last" name
                    g2.setFont(isSel ? FONT_DROPDOWN : FONT_ITEM);
                    g2.setColor(isSel ? BUTTON_BG : TEXT_DARK);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(items[i], 32,
                            iy + (ITEM_H - fm.getHeight()) / 2 + fm.getAscent());
                }

                g2.dispose();
            }
        }

        // ── Header painting — ALWAYS shows placeholder title "Companions" ──────
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = headerH;

            // White rounded header
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, w, h, 12, 12);

            // Border
            g2.setColor(TEXT_DARK);
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);

            // Show title "Companions"
            g2.setFont(FONT_DROPDOWN);
            g2.setColor(TEXT_DARK);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(placeholder, 14, (h - fm.getHeight()) / 2 + fm.getAscent());

            // Show count badge if any selected
            if (!selected.isEmpty()) {
                String badge = "(" + selected.size() + ")";
                g2.setFont(FONT_ITEM);
                fm = g2.getFontMetrics();
                g2.setColor(new Color(100, 120, 170));
                g2.drawString(badge, w - fm.stringWidth(badge) - 30,
                        (h - fm.getHeight()) / 2 + fm.getAscent());
            }

            // Chevron
            int cx = w - 22, cy = isOpen() ? h / 2 + 2 : h / 2 - 2;
            g2.setColor(TEXT_DARK);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            if (isOpen()) {
                g2.drawPolyline(new int[]{cx, cx + 6, cx + 12},
                                new int[]{cy + 5, cy - 1, cy + 5}, 3);
            } else {
                g2.drawPolyline(new int[]{cx, cx + 6, cx + 12},
                                new int[]{cy, cy + 6, cy}, 3);
            }

            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  AttractionPanel
    // ══════════════════════════════════════════════════════════════════════════
    private class AttractionPanel extends JComponent {
        private String   name = null;
        private String   type = null;
        private JButton  addBtn;
        private Runnable addListener;

        AttractionPanel() {
            setOpaque(false);
            setLayout(null);
            addBtn = makeRoundedButton("Add to Itinerary",
                    new Font("SansSerif", Font.BOLD, 13), Color.WHITE, BUTTON_BG);
            add(addBtn);
            addBtn.addActionListener(e -> { if (addListener != null) addListener.run(); });
            addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    int btnW = getWidth() - 24, btnH = 36;
                    addBtn.setBounds(12, getHeight() - btnH - 14, btnW, btnH);
                }
            });
        }

        void setAttraction(String n, String t) { name = n; type = t; repaint(); }
        void setAddListener(Runnable r)         { addListener = r; }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int W = getWidth(), H = getHeight();
            g2.setColor(PLACEHOLDER_PANEL);
            g2.fillRoundRect(0, 0, W, H, 14, 14);
            if (name == null) {
                g2.setFont(FONT_BODY);
                g2.setColor(new Color(255, 255, 255, 160));
                String msg = "attraction info";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (W - fm.stringWidth(msg)) / 2, (H - 40) / 2 + fm.getAscent());
            } else {
                g2.setFont(FONT_ATTRACT);
                g2.setColor(Color.WHITE);
                g2.drawString(name, 16, 40);
                g2.setFont(FONT_BODY);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.drawString("Type: " + (type != null ? type : "—"), 16, 68);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ItineraryPanel
    // ══════════════════════════════════════════════════════════════════════════
    private class ItineraryPanel extends JComponent {
        ItineraryPanel() { setOpaque(false); }
        void refresh()   { repaint(); }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int W = getWidth();
            int pad = 14, y = pad;

            g2.setFont(FONT_SECTION);
            g2.setColor(TEXT_DARK);
            FontMetrics tfm = g2.getFontMetrics();
            g2.drawString("Your Itinerary", pad, y + tfm.getAscent());
            y += tfm.getHeight() + 14;

            g2.setFont(FONT_BODY);
            g2.setColor(ITINERARY_LABEL);
            String dateStr = "Dates:";
            if (calendar.getRangeStart() != null) {
                dateStr = "Dates: " + calendar.getRangeStart();
                if (calendar.getRangeEnd() != null)
                    dateStr += " → " + calendar.getRangeEnd();
            }
            y = drawWrapped(g2, dateStr, pad, y, W - pad * 2);
            y += 10;

            Set<String> comps = companionsDropdown.getSelected();
            y = drawWrapped(g2, "Companions: " +
                    (comps.isEmpty() ? "—" : String.join(", ", comps)), pad, y, W - pad * 2);
            y += 10;

            if (!itineraryAttractions.isEmpty()) {
                y = drawWrapped(g2, String.join(", ", itineraryAttractions),
                        pad, y, W - pad * 2);
            }

            g2.dispose();
        }

        private int drawWrapped(Graphics2D g2, String text, int x, int y, int maxW) {
            FontMetrics fm = g2.getFontMetrics();
            int lineH = fm.getHeight();
            StringBuilder line = new StringBuilder();
            for (String word : text.split(" ")) {
                String test = line.isEmpty() ? word : line + " " + word;
                if (fm.stringWidth(test) > maxW && !line.isEmpty()) {
                    g2.drawString(line.toString(), x, y + fm.getAscent());
                    y += lineH; line = new StringBuilder(word);
                } else line = new StringBuilder(test);
            }
            if (!line.isEmpty()) {
                g2.drawString(line.toString(), x, y + fm.getAscent());
                y += lineH;
            }
            return y;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Field + button helpers
    // ══════════════════════════════════════════════════════════════════════════

    private JTextField makeTripNameField(String placeholder) {
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setFont(getFont());
                    g2.setColor(TEXT_DARK);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, 18, (getHeight()-fm.getHeight())/2 + fm.getAscent());
                }
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_TRIP_NAME);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(new RoundedBorder(14, FIELD_BORDER, 1));
        field.setMargin(new Insets(0, 18, 0, 18));
        return field;
    }

    // ── Unified search field — icon drawn inside left side, text never overlaps ─
    private JTextField makePlainSearchField(String placeholder) {
        final int ICON_ZONE = 36; // pixels reserved for the icon on the left
        JTextField field = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // White rounded background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                // Draw Swing's text/caret (clipped to insets set by setMargin)
                super.paintComponent(g);
                // Placeholder drawn in the inset area, after super so caret shows on focus
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setFont(getFont());
                    g2.setColor(TEXT_PLACEHOLDER);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, ICON_ZONE + 4,
                            (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }
                // Magnifier icon — drawn on top, always in the reserved zone
                int cy = getHeight() / 2;
                int r  = 7;
                int ox = (ICON_ZONE - r * 2 - 4) / 2; // centered in icon zone
                g2.setColor(TEXT_PLACEHOLDER);
                g2.setStroke(new BasicStroke(1.9f));
                g2.drawOval(ox, cy - r, r * 2, r * 2);
                g2.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(ox + r * 2 - 1, cy + r - 3, ox + r * 2 + 5, cy + r + 3);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_SEARCH);
        field.setForeground(TEXT_DARK);
        field.setCaretColor(TEXT_DARK);
        field.setBorder(new RoundedBorder(14, FIELD_BORDER, 1));
        // Left margin = ICON_ZONE so Swing clips typed text to start after icon
        field.setMargin(new Insets(0, ICON_ZONE + 4, 0, 12));
        return field;
    }

    // ── Search icon button (magnifier, sits to the LEFT of the text field) ───
    private JButton makeSearchIconButton() {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Rounded white background matching field style
                Color bg = getModel().isRollover() ? new Color(235, 238, 248) : Color.WHITE;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(FIELD_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                // Magnifier circle
                int ic = getHeight() / 2, r = 8;
                int ox = (getWidth() - r * 2 - 6) / 2;
                g2.setColor(getModel().isRollover() ? TEXT_DARK : TEXT_PLACEHOLDER);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(ox, ic - r, r * 2, r * 2);
                // Handle
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(ox + r * 2 - 1, ic + r - 3, ox + r * 2 + 5, ic + r + 3);
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

    private JButton makeBackButton() {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(210,215,230) : Color.WHITE);
                g2.fillOval(0, 0, getWidth()-1, getHeight()-1);
                g2.setColor(TEXT_DARK);
                g2.setStroke(new BasicStroke(1.8f));
                g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
                int cx = getWidth()/2, cy = getHeight()/2, arm = 7;
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx+2, cy-arm, cx-4, cy);
                g2.drawLine(cx-4, cy,     cx+2, cy+arm);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private JButton makeRoundedButton(String text, Font font, Color fg, Color bg) {
        Color hover = new Color(Math.min(bg.getRed()+20,255),
                                Math.min(bg.getGreen()+20,255),
                                Math.min(bg.getBlue()+35,255));
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() : getModel().isRollover() ? hover : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(fg); g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth()-fm.stringWidth(getText()))/2,
                        (getHeight()-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        };
        btn.setFont(font); btn.setContentAreaFilled(false);
        btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.repaint(); }
            @Override public void mouseExited(MouseEvent e)  { btn.repaint(); }
        });
        return btn;
    }

    private static class RoundedBorder extends AbstractBorder {
        private final int radius, thickness; private final Color color;
        RoundedBorder(int r, Color c, int t) { radius=r; color=c; thickness=t; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, w-1, h-1, radius*2, radius*2); g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────
    public JButton       getBackButton()             { return backButton; }
    public JButton       getSaveButton()             { return saveButton; }
    public String        getTripName()               { return tripNameField.getText().trim(); }
    public MapViewClient getMapView()                { return mapView; }
    public LocalDate     getStartDate()              { return calendar.getRangeStart(); }
    public LocalDate     getEndDate()                { return calendar.getRangeEnd(); }
    public Set<String>   getSelectedCompanions()     { return companionsDropdown.getSelected(); }
    public List<String>  getItineraryAttractions()   { return Collections.unmodifiableList(itineraryAttractions); }

    public void setTripName(String name) { tripNameField.setText(name); }

    /**
     Call companions???
     */
    public void setCompanionOptions(String[] fullNames) {
        remove(companionsDropdown);
        companionsDropdown = new MultiDropdown("Companions", fullNames);
        companionsDropdown.setChangeListener(() -> itineraryPanel.refresh());
        add(companionsDropdown);
        doLayout(); repaint();
    }
}