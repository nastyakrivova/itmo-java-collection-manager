package com.myorg.lab5.client.gui.utils_gui;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    private static LocalizationManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    
    private LocalizationManager() {
        currentLocale = new Locale("ru");
        bundle = ResourceBundle.getBundle("i18n/messages", currentLocale);
        
    }
    
    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }
    
    public void changeLocale(Locale locale) {
        this.currentLocale = locale;
        try {
            bundle = ResourceBundle.getBundle("i18n/messages", locale);
        } catch (Exception e) {
            System.err.println("Failed to load locale: " + locale);
            bundle = ResourceBundle.getBundle("i18n/messages", new Locale("ru"));
        }
    }
    
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }
    
    public ResourceBundle getBundle() {
        return bundle;
    }
    
    public Locale getCurrentLocale() {
        return currentLocale;
    }
}