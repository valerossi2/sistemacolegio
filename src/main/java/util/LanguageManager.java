package util;

import java.util.*;

public class LanguageManager {

    private static LanguageManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private final List<Runnable> listeners = new ArrayList<>();

    private LanguageManager() {
        currentLocale = new Locale("es");
        bundle = ResourceBundle.getBundle("lang.lang", currentLocale);
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "?" + key;
        }
    }

    public String get(String key, String defaultValue) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    public void setLanguage(String langCode) {
        Locale newLocale;
        switch (langCode) {
            case "en":
                newLocale = new Locale("en");
                break;
            default:
                newLocale = new Locale("es");
        }
        if (newLocale.equals(currentLocale)) return;
        currentLocale = newLocale;
        bundle = ResourceBundle.getBundle("lang.lang", currentLocale);
        notifyListeners();
    }

    public String getCurrentLanguageCode() {
        return currentLocale.getLanguage();
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable r : listeners) {
            r.run();
        }
    }
}
