package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    public static final String COLOR_PRIMARY = "#2D6AEE";
    public static final String COLOR_RED = "#DC2626";
    public static final String COLOR_RED_HOVER = "#B91C1C";
    public static final String COLOR_GREEN = "#22C55E";

    public static final String LIGHT_BG = "#F4F7FE";
    public static final String LIGHT_CARD = "#ffffff";
    public static final String LIGHT_CARD_BORDER = "#E2E8F0";
    public static final String LIGHT_TEXT = "#1E293B";
    public static final String LIGHT_TEXT_SEC = "#64748B";
    public static final String LIGHT_TEXT_MUTED = "#94A3B8";
    public static final String LIGHT_INPUT_BG = "#F8FAFC";
    public static final String LIGHT_INPUT_BORDER = "#E2E8F0";
    public static final String LIGHT_TOGGLE_BG = "#F1F5F9";
    public static final String LIGHT_DIVIDER = "#E2E8F0";
    public static final String LIGHT_HEADER_BG = "#F8FAFC";
    public static final String LIGHT_SIDEBAR_BG = "#ffffff";
    public static final String LIGHT_ROW_HOVER = "#F8FAFC";
    public static final String LIGHT_ICON_CIRCLE_BG = "#F4F4F5";

    public static final String DARK_BG = "#0F172A";
    public static final String DARK_CARD = "#1E293B";
    public static final String DARK_CARD_BORDER = "#334155";
    public static final String DARK_TEXT = "#F8FAFC";
    public static final String DARK_TEXT_SEC = "#CBD5E1";
    public static final String DARK_TEXT_MUTED = "#64748B";
    public static final String DARK_INPUT_BG = "#334155";
    public static final String DARK_INPUT_BORDER = "#475569";
    public static final String DARK_TOGGLE_BG = "#475569";
    public static final String DARK_DIVIDER = "#334155";
    public static final String DARK_HEADER_BG = "#1E293B";
    public static final String DARK_SIDEBAR_BG = "#1E293B";
    public static final String DARK_ROW_HOVER = "#334155";
    public static final String DARK_ICON_CIRCLE_BG = "#475569";

    private boolean darkMode = false;
    private List<Runnable> listeners = new ArrayList<>();

    public boolean isDark() { return darkMode; }

    public void setDark(boolean dark) {
        if (darkMode == dark) return;
        darkMode = dark;
        for (Runnable r : listeners) r.run();
    }

    public void toggle() { setDark(!darkMode); }

    public void addListener(Runnable r) { listeners.add(r); }

    public String bg() { return darkMode ? DARK_BG : LIGHT_BG; }
    public String card() { return darkMode ? DARK_CARD : LIGHT_CARD; }
    public String border() { return darkMode ? DARK_CARD_BORDER : LIGHT_CARD_BORDER; }
    public String text() { return darkMode ? DARK_TEXT : LIGHT_TEXT; }
    public String textSec() { return darkMode ? DARK_TEXT_SEC : LIGHT_TEXT_SEC; }
    public String muted() { return darkMode ? DARK_TEXT_MUTED : LIGHT_TEXT_MUTED; }
    public String inputBg() { return darkMode ? DARK_INPUT_BG : LIGHT_INPUT_BG; }
    public String inputBorder() { return darkMode ? DARK_INPUT_BORDER : LIGHT_INPUT_BORDER; }
    public String toggleGroupBg() { return darkMode ? DARK_TOGGLE_BG : LIGHT_TOGGLE_BG; }
    public String divider() { return darkMode ? DARK_DIVIDER : LIGHT_DIVIDER; }
    public String headerBg() { return darkMode ? DARK_HEADER_BG : LIGHT_HEADER_BG; }
    public String sidebarBg() { return darkMode ? DARK_SIDEBAR_BG : LIGHT_SIDEBAR_BG; }
    public String rowHover() { return darkMode ? DARK_ROW_HOVER : LIGHT_ROW_HOVER; }
    public String iconCircleBg() { return darkMode ? DARK_ICON_CIRCLE_BG : LIGHT_ICON_CIRCLE_BG; }
}
