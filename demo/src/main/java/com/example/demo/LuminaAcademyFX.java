package com.example.demo;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class LuminaAcademyFX extends Application {

    // --- PALETA DE COLORES EXACTA DEL DESIGN.MD ---
    private final String COLOR_PRIMARY = "#004ac6";
    private final String COLOR_PRIMARY_CONTAINER = "#2563eb";
    private final String COLOR_BACKGROUND = "#f9f9ff";
    private final String COLOR_SURFACE = "#f9f9ff";
    private final String COLOR_SURFACE_LOW = "#f0f3ff";
    private final String COLOR_SURFACE_CONTAINER = "#e7eeff";
    private final String COLOR_SURFACE_CONTAINER_HIGH = "#dee8ff";
    private final String COLOR_ON_SURFACE = "#111c2d";
    private final String COLOR_ON_SURFACE_VARIANT = "#434655";
    private final String COLOR_OUTLINE = "#737686";
    private final String COLOR_OUTLINE_VARIANT = "#c3c6d7";
    private final String COLOR_WHITE = "#ffffff";
    private final String COLOR_PRIMARY_FIXED = "#dbe1ff";
    private final String COLOR_PRIMARY_FIXED_DIM = "#b4c5ff";
    private final String COLOR_SECONDARY = "#006686";
    private final String COLOR_SECONDARY_FIXED = "#c0e8ff";
    private final String COLOR_TERTIARY = "#4e565b";
    private final String COLOR_TERTIARY_CONTAINER = "#666f74";
    private final String COLOR_TERTIARY_FIXED = "#dbe4ea";
    private final String COLOR_ERROR = "#ba1a1a";

    // --- MATERIAL SYMBOLS SVG PATHS ---
    private final String ICON_HOME = "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z";
    private final String ICON_SCHOOL = "M12 3L1 9l4 2.18v6L12 21l7-3.82v-6l2-1.09V17h2V9L12 3zm6.82 6L12 12.72 5.18 9 12 5.28 18.82 9zM17 15.99l-5 2.73-5-2.73v-3.72L12 15l5-2.73v3.72z";
    private final String ICON_BOOK = "M19 2l-5 4.5v11l5-4.5V2zM6.5 5C4.55 5 2.45 5.4 1 6.5v11.5c1.45-1.1 3.55-1.5 5.5-1.5s4.05.4 5.5 1.5V6.5C14.95 5.4 12.85 5 10.5 5S8.05 5.4 6.5 5z";
    private final String ICON_GROUP = "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z";
    private final String ICON_CALENDAR = "M19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99.9-1.99 2L3 19c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V8h14v11z";
    private final String ICON_SETTINGS = "M19.14 12.94c.04-.3.06-.61.06-.94 0-.32-.02-.64-.07-.94l2.03-1.58c.18-.14.23-.41.12-.61l-1.92-3.32c-.12-.22-.37-.29-.59-.22l-2.39.96c-.5-.38-1.03-.7-1.62-.94l-.36-2.54c-.04-.24-.24-.41-.48-.41h-3.84c-.24 0-.43.17-.47.41l-.36 2.54c-.59.24-1.13.57-1.62.94l-2.39-.96c-.22-.08-.47 0-.59.22L2.74 8.87c-.12.21-.08.47.12.61l2.03 1.58c-.05.3-.07.62-.07.94s.02.64.07.94l-2.03 1.58c-.18.14-.23.41-.12.61l1.92 3.32c.12.22.37.29.59.22l2.39-.96c.5.38 1.03.7 1.62.94l.36 2.54c.05.24.24.41.48.41h3.84c.24 0 .44-.17.47-.41l.36-2.54c.59-.24 1.13-.56 1.62-.94l2.39.96c.22.08.47 0 .59-.22l1.92-3.32c.12-.22.07-.47-.12-.61l-2.01-1.58zM12 15.6c-1.98 0-3.6-1.62-3.6-3.6s1.62-3.6 3.6-3.6 3.6 1.62 3.6 3.6-1.62 3.6-3.6 3.6z";
    private final String ICON_SEARCH = "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z";
    private final String ICON_NOTIFICATIONS = "M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z";
    private final String ICON_HELP = "M11 18h2v-2h-2v2zm1-16C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm0-14c-2.21 0-4 1.79-4 4h2c0-1.1.9-2 2-2s2 .9 2 2c0 2-3 1.75-3 5h2c0-2.25 3-2.5 3-5 0-2.21-1.79-4-4-4z";
    private final String ICON_PERSON_PIN = "M19 2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h4l3 3 3-3h4c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-7 3.3c1.49 0 2.7 1.21 2.7 2.7s-1.21 2.7-2.7 2.7S9.3 9.49 9.3 8s1.21-2.7 2.7-2.7zM18 16H6v-.9c0-2 4-3.1 6-3.1s6 1.1 6 3.1v.9z";
    private final String ICON_TRENDING_UP = "M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z";
    private final String ICON_CHECK_CIRCLE = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z";
    private final String ICON_MORE_HORIZ = "M6 10c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm12 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm-6 0c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z";
    private final String ICON_SCHEDULE = "M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67z";
    private final String ICON_CHEVRON_RIGHT = "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z";
    private final String ICON_BOLT = "M14.69 2.21c-.29-.27-.71-.27-1.01 0l-10 9.52c-.29.28-.35.73-.14 1.07.2.34.58.51.98.44l5.93-.85-2.46 8.38c-.12.41.08.85.46 1.03.23.11.49.12.71.03.09-.04.17-.09.24-.16l10-9.52c.29-.28.35-.73.14-1.07-.2-.34-.58-.51-.98-.44l-5.93.85 2.46-8.38c.12-.41-.08-.85-.46-1.03-.11-.05-.23-.07-.34-.06z";
    private final String ICON_AVATAR = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z";

    // --- LOGO SVG (Brand) ---
    private final String LOGO_PATH = "M21.5 4.2c-.9 1.3-2.3 4.8-3.2 7.8-3.5 11.2-1.2 47.9 4.7 76.5 7.6 37 28.7 74.2 61.2 108.1 11.7 12.1 29.1 27.2 32.5 28.1 2.8.6 9.2-4.5 23.2-18.7 18.7-19 33.5-39.8 44.7-62.9C200.3 111 206.3 81.8 206.4 37c.1-19.7-.9-27.7-4-32.8L201 2H23.1zm172.2 13.5c.5 1.6.8 13.8.8 27.3 0 25.5-1 35.3-5.6 54.7-6.1 25.7-20.9 55.1-39.3 77.9-9.1 11.3-28.9 30.4-31.5 30.4-2.2 0-16.3-11.2-25.9-20.7C59.6 155 40.6 117.1 32.3 67.5c-1.8-11.3-2.6-47-1.1-50.8.7-1.6 5.5-1.7 81.2-1.7H193z M62.2 19.7C57.4 24 59.9 32 66 32c5.8 0 8.1-8.2 3.4-11.9-3.2-2.6-4.6-2.6-7.2-.4m6 2c3.7 3.3 1.4 8.8-3.4 8.1-3-.4-3.9-6.5-1.2-8.5 2.3-1.6 2.4-1.6 4.6.4M122 19.3c-3.1 1.6-4.1 8.5-1.5 11.1 2.1 2.1 5.9 2.1 8 0 2.2-2.2 1.1-3-1.9-1.4-3.6 1.9-5.6.7-5.6-3.4s2.4-6.1 5.4-4.4c2.5 1.4 3.5-.1 1.2-1.8-2.4-1.7-2.5-1.7-5.6-.1M157.2 19c-4.1 1.7-5.1 8.3-1.6 11.7 1.7 1.8 5.9 1.6 7.4-.2 1.7-2.1.7-2.9-1.8-1.4-3.6 2.2-6.5-1.1-5.4-6.2.4-2.4 4.4-3.2 5.9-1.3.8 1.1 1.3 1.2 1.9.3.4-.7-.1-1.8-1.1-2.6-2.1-1.5-2.2-1.5-5.3-.3M171 19.3c-3.2 1.6-4 7.9-1.4 10.8 3.8 4.2 10.4 1.2 10.4-4.7 0-3.5-.8-4.7-3.7-6.3-2.6-1.3-2.2-1.3-5.3.2m5.4 2.3c3 2.9 1.1 8.4-2.8 8.4-5.2 0-4.6-9.7.6-10 .4 0 1.4.7 2.2 1.6M46.9 21.2C46.5 29.7 46.8 32 48 32c.6 0 1-1.1 1-2.5 0-2 .5-2.5 2.4-2.5 3 0 4.9-2.2 4.4-5.2-.2-1.9-1-2.3-4.5-2.6-3.9-.3-4.3-.1-4.4 2m6-.3c1.9 1.2.6 4.1-1.9 4.1-1.5 0-2-.7-2-2.5 0-2.6 1.4-3.1 3.9-1.6M75.6 24.3c.1 2.8.2 5.8.3 6.4.1 1.6 8.1 1.8 8.1.3 0-.6-1.3-1-3-1-3 0-3-.1-3-5.5 0-3.7-.4-5.5-1.2-5.5-.9 0-1.3 1.7-1.2 5.3M88 25.5c0 3.7.4 6.5 1 6.5s1-2.8 1-6.5-.4-6.5-1-6.5-1 2.8-1 6.5M93 20c0 .5.3.9.8.9 1.6-.3 3.2.1 3.1.8-.1.5-.1 2.9 0 5.5.1 6.7 2.1 6 2.1-.7 0-4.8.3-5.7 2.3-6.5 1.7-.7 1.1-.9-3-.9-2.9-.1-5.3.3-5.3.9M106 25.5V32h5.1c3.1 0 4.8-.4 4.4-1-.3-.6-2.2-1-4.1-1-2.7 0-3.4-.4-3.4-2s.7-2 3.5-2c1.9 0 3.5-.5 3.5-1 0-.6-1.6-1-3.6-1-2.8 0-3.5-.3-3.2-1.8.2-1.2 1.3-1.7 3.6-1.7 1.7 0 3.2-.3 3.2-.7 0-.5-2-.8-4.5-.8H106zM133.5 25.5c0 3.6.3 6.5.8 6.5.4 0 .7-1.5.7-3.4 0-5 1.5-5.2 4.1-.6 3.4 6 4.9 5.3 4.9-2.5 0-3.7-.4-6.5-1-6.5-.5 0-1 1.8-1 4s-.3 4-.7 4c-.5 0-1.8-1.8-3.1-4-3.4-5.9-4.7-5.2-4.7 2.5M148 25.5c0 3.7.4 6.5 1 6.5s1-2.8 1-6.5-.4-6.5-1-6.5-1 2.8-1 6.5M49 44.3c0 18 2.6 34.9 8.2 55.2 5.9 20.7 12.5 33.7 29 57 7.4 10.4 28 31 31.1 31 2.5 0 11.8-8.9 19.9-19 22.1-27.3 31.6-49.2 36.8-84.7 2.4-16.4 3.7-44.6 2.2-47.4-1-1.8-3.2-1.9-64.1-2L49 34.2zM92 46c-2.9 3.3-5.6 6.3-6.1 6.6s-2.8-.8-5.2-2.5c-4.4-3.1-11.8-5.4-14.6-4.4-5.8 2.1-14.1 9.3-9.1 8 1.1-.3 2 0 2 .5 0 .6.8.4 1.7-.3 1.5-1.2 1.6-1.2 1 .4-.5 1.5-.2 1.7 2.7 1.3 4-.5 6.9.8 3.9 1.8-1 .3-3.1 3.1-4.6 6.1-2.9 6-3.5 9.2-1.2 6.1 1.3-1.7 1.5-1.8 1.5-.3 0 3 2.9 1.7 4.9-2.1.7-1.3 1.4-2.2 1.6-2s-1 3.1-2.6 6.3c-2.4 4.6-2.9 6.9-2.6 10 1.1 9.2 2.7 18.9 3.8 22.4.9 3.2.9 4.2-.5 6.4-2.1 3.1-1 3.9 3.7 2.6 4.1-1.1 4.7-2.3 1.7-3.9-1.3-.7-2-2.1-2-3.8l.1-2.7 1.6 3c1.2 2.1 1.8 2.6 2.1 1.5.3-.8 1-1.8 1.6-2.2.8-.5.8-1.3-.1-2.7-1.1-1.7-1-1.9.3-1.4 1.2.4 1.4 0 .8-2.1-.5-2.3-.4-2.5 1.2-2 1.6.6 1.7.5.7-.7-1.5-1.6-1.7-3.3-.3-2.4 1.5.9 1.2-.2-.7-3.3-1.7-2.6-1.7-2.6 0-1.3 1.9 1.5 2.2.9.7-1.9-.8-1.6-.7-1.8.4-1.4 2.3.9 2.9-1.5 1-4.3-1.2-1.9-1.3-2.8-.5-3.1.6-.2.8-.8.5-1.3-.3-.6-.1-1.6.5-2.3 1.2-1.4 1.9-4.6 2-8.9.1-1.5.5-2.7 1.1-2.7s.7 3.2.4 8.2c-.6 8.5 1.7 24.4 4.9 33.7l1.4 4-3.2 1.1c-4.7 1.5-6 3.4-4 5.4 1.5 1.5 1.3 1.6-2 1.6-2.2 0-3.8.6-4.1 1.5-.4.8-1.1 1.2-1.7.8-.5-.3-2.5-.6-4.4-.5-3.8.2-3.5.7-8.5-13.8C57.9 87.9 54 65.2 54 47.8V40h43.2zm79.7 8.2c-1.2 25.5-3.6 40.9-8.7 57.8-4.1 13.1-4 13-8.9 8.4-3.1-3-4.1-4.6-4.1-7.1 0-3.8 2.1-4.8 8.1-3.8 3.6.6 4 .5 3.7-1.1-.2-1-1.4-1.9-2.8-2-1.4-.2-3.7-.5-5.2-.6-1.5-.2-3.6-1.2-4.7-2.2s-2.5-1.5-3-1.1c-1.4.8-1.4 4.8 0 6.2.9.9.3 1.3-2.4 1.8-4.8.9-7.7-.1-7.8-2.8s-.6-5.4-1.3-7.4c-.5-1.3-1-1.3-3.4.3-1.5.9-3.2 2.4-3.8 3.2-.5.8-3.7 1.9-7 2.6-4.8 1-6 1.6-6.2 3.3-.4 2.4-2.9 3.8-4.6 2.4-.6-.5-2.4-1.1-3.9-1.3-2.6-.3-3.2-1.2-6.1-8.5-2.4-6-3.1-9.3-2.7-12.5l.4-4.3 1.7 6.3c1.9 6.8 2.7 8.3 3.8 6.5.5-.7 1.7 1.3 3.1 4.8 2.1 5.4 3.7 7 5.4 5.2.4-.3-.3-2.1-1.4-4-1.2-1.9-1.9-3.7-1.6-4 .4-.3 1.4 1.1 2.3 3.1 1 2 2.1 3.6 2.6 3.6 1.2 0 1-1-1.4-4.9l-2.1-3.6 2.9 2.7c1.7 1.6 3.3 2.8 3.6 2.8 1.5 0-.1-5.2-2.3-7.7l-2.4-2.7 3.2 2.2c1.9 1.4 3.4 1.8 3.8 1.2.3-.5-1-2-3-3.2-3.6-2.1-3.7-2.1-1.5-3.3 2.1-1.1 2.1-1.2-2.2-4.8-3.6-3-3.9-3.5-1.8-3 1.4.3 3 .9 3.5 1.3.6.4 1.8 1 2.9 1.4 1.6.5 1.7.3 1.1-2.1l-.6-2.7 2.1 2.5c1.5 1.7 2 1.9 1.6.6-.5-1.5-.1-1.4 2.7.5 1.7 1.3 3.4 2.6 3.5 3 .2.5.8.8 1.4.8s.3-.9-.6-1.9c-1-1.1-1.5-2.5-1.1-3.1.4-.7-.3-2.5-1.5-4.2l-2.2-3 4.4 2.6c6.6 3.8 6.9 3.2.8-1.4-5.6-4.2-5.8-4.6-1.6-3 3.5 1.3 3.3.6-.6-2-2.6-1.7-2.8-2.1-1-1.7 3.5.8 2.6-.2-2-2.3-3.2-1.5-3.7-2-2-2 1.2 0 2.2-.5 2.2-1.1 0-.8.6-.7 1.7.2 1.1 1 1.7 1 2 .3.2-.6 1.5-.9 2.8-.6 1.4.2 2.5.2 2.5 0 0-1-13.8-11.5-16.6-12.7-3.9-1.5-8.4-1.8-15.1-.8-6.5.9-6.8-.3-.9-2.7 4.5-1.8 6.2-3.9 5-5.8-.4-.6.2-.6 1.4.1 2.7 1.5 3.2 1.4 2.6-.3-.3-.8.2-1.6 1.3-1.9 1-.3 15.5-.5 32.2-.6l30.4-.1zm-90.6-1.5c1.3 1.6 1.2 1.7-.3.4-1-.7-1.8-1.5-1.8-1.7 0-.8.8-.3 2.1 1.3m21.9.3c0 .5-.5 1-1.1 1-.5 0-.7-.5-.4-1 .3-.6.8-1 1.1-1 .2 0 .4.4.4 1m0 5.1c0 .5-.7.6-1.5.3-.8-.4-1.5-1.3-1.5-2.1 0-1.3.3-1.3 1.5-.3.8.7 1.5 1.6 1.5 2.1m13.2 3.3c.3-.2.8.2 1.1 1.1.7 1.8.3 1.9-2.4.5-1-.6-1.9-1.6-1.9-2.2 0-.7.4-.7 1.3.1.8.6 1.6.8 1.9.5m-39.7.6c-.3.5-1.2 1-1.8 1-.7 0-.6-.4.3-1 1.9-1.2 2.3-1.2 1.5 0m2.2 3.5c-1.2 1.2-2.4 2-2.6 1.8-.3-.2-1.6.4-3 1.4l-2.6 1.6 1.9-2.1c1.1-1.2 2.5-2.2 3.3-2.2.7 0 1.3-.7 1.3-1.5 0-.9.6-1.2 1.5-.9q1.65.75.9-1.2c-.5-1.5-.4-1.6.4-.4.8 1 .5 1.9-1.1 3.5m14.8-.5c-.3.5.1 1.1.7 1.3.9.4 1 .6.1.6-1.3.1-2.6-1.6-2.9-3.7-.2-1 .1-1 1.2-.1.7.6 1.2 1.5.9 1.9m15.1-1.5c2.5 1.9 1.4 1.9-2.2 0-1.6-.8-2.2-1.5-1.3-1.5.8 0 2.4.7 3.5 1.5M75 70c0 .5-.5 1-1.1 1-.5 0-.7-.5-.4-1 .3-.6.8-1 1.1-1 .2 0 .4.4.4 1m22.4.6c.3.8.2 1.2-.4.9s-1-1-1-1.6c0-1.4.7-1.1 1.4.7m-24.5 2.5c.4 1.2.2 1.2-1.3 0s-1.6-1.2-1 .5c.6 1.5.4 1.7-.6.7-1.4-1.3-.4-3.6 1.3-3 .6.2 1.3 1 1.6 1.8M70 78.6c0 1.5-.6 2.4-1.5 2.4-.8 0-1.5-.8-1.5-1.8 0-1.5.2-1.5 1.1-.2.8 1.2 1 .9.6-1.2-.2-1.5 0-2.5.4-2.2.5.3.9 1.6.9 3m30.5-.6c.3.5.1 1-.4 1-.6 0-1.1-.5-1.1-1 0-.6.2-1 .4-1 .3 0 .8.4 1.1 1m1.6 3.7c1.3 1.6 1.2 1.7-.3.4-1-.7-1.8-1.5-1.8-1.7 0-.8.8-.3 2.1 1.3m23.9.3c0 .5-.2 1-.4 1-.3 0-.8-.5-1.1-1-.3-.6-.1-1 .4-1 .6 0 1.1.4 1.1 1m9 44.5c.1 6 .1 11.3.1 11.7-.1.4-2.2-.2-4.8-1.3-2.8-1.3-7.2-2.2-10.6-2.3l-5.8-.1.6-4c1.8-11.8 7-20.7 15.8-27.2l3.2-2.4.6 7.3c.4 4 .8 12.2.9 18.3m15-19.5c.8.5 1.1 1 .5 1-.5 0-1.7-.5-2.5-1s-1-1-.5-1c.6 0 1.7.5 2.5 1m-30.7 5.2c-2.1 2.4-3.3 2.3-3.3-.2 0-1.5.6-1.9 2.5-1.8 2.3.2 2.3.3.8 2m-27.4.1c.8 1.1.4 1.3-2.2.9-2.7-.4-2.9-.6-1.2-1.3 1.1-.4 2.1-.8 2.1-.8.1-.1.6.5 1.3 1.2m54.4.4c-.7.3-1.3 1.2-1.4 2.1 0 1.3-.2 1.3-.8-.3-.6-1.4-.4-2.2.8-2.6.9-.4 1.8-.4 2.1-.1.3.2-.1.7-.7.9m-36.9 1.8c1.4 1.3 2.6 3 2.5 3.7q0 1.5-.6 0c-.2-.6-1.5-1.2-2.8-1.2-2.6 0-5-2.4-4-4 .9-1.5 1.9-1.2 4.9 1.5m32.5-.9c.2.2.2.6 0 .9-.8.7-4.9-.7-4.9-1.7 0-.8 3.8-.2 4.9.8m-49.5 3.9c1.8 1.8 2.1 2.5 1 2.5-.8 0-1.7-.7-2.1-1.6-.3-.9-1.5-2-2.7-2.4-1.6-.7-1.7-.9-.4-.9.9-.1 2.8 1 4.2 2.4m47.9-.8c-.7.2-2.1.2-3 0-1-.3-.4-.5 1.2-.5 1.7 0 2.4.2 1.8.5m-25.4 3c0 2.5-.1 2.6-.9.8-1.2-2.7-1.2-3.5 0-3.5.6 0 1 1.2.9 2.7m34.7 0c-.1.1-1.2 0-2.5-.4-1.5-.4-1.9-.8-1-1.3 1-.7 4.2 1 3.5 1.7m-40.6 2.5c0 1.7 3 .7 3.1-1 0-1.3.2-1.2.9.6 1 2.3.2 11.2-.9 11.2-.4 0-1.3-2.1-2-4.8-.8-2.6-2-6.1-2.8-7.7-1.3-2.7-1.3-2.8.2-1.1.8 1.1 1.5 2.3 1.5 2.8m2-.7c0 .5-.5.3-1-.5s-1-2-1-2.5c0-.6.5-.3 1 .5s1 1.9 1 2.5m-30.6-1.4c-.3.6-.7 3-.8 5.3l-.1 4.2 4.5-.6c2.5-.3 5-.7 5.5-.8.6-.1 1.1-1.3 1.3-2.7.3-1.9 0-2.4-1.2-1.9-.9.3-1.6.1-1.6-.5 0-.8 1.2-1.1 3.3-.8 2.6.3 3.3.8 3.5 3 .2 1.5.8 2.7 1.4 2.7.5 0 .8.6.5 1.4-.3.7 0 1.9.6 2.5.8.8.8 1.1 0 1.1-.7 0-1.5-.5-1.9-1.1-.4-.8-.9-.7-1.6.2-.8 1.1-1.1 1.1-1.5.1-.3-.8-.9-1-1.6-.4-.7.5-3.6.6-6.7.2-3-.4-6-.5-6.7-.3-1.6.6-3.6-3.4-3-6.3.2-1.3-.1-2.5-.7-2.7s-.8-.8-.4-1.2c.5-.5 1.7 0 2.8 1 1.9 1.7 2 1.7 2.9-.9.6-1.4 1.3-2.6 1.6-2.6.4 0 .3.5-.1 1.1m6.6-.1c1.1.8.7.9-1.5.4-3.7-.8-4.5-1.4-1.9-1.4 1 0 2.6.5 3.4 1m51.9 6.2c.1 1-.3 1.8-.9 1.8-.5 0-1-1.9-.9-4.3.1-3.6.2-3.8.9-1.7.4 1.4.8 3.3.9 4.2m1.8-3c-.3.7-.5.2-.5-1.2s.2-1.9.5-1.3c.2.7.2 1.9 0 2.5M72.9 125c0 1-.3.8-.9-.5-1.2-2.7-1.2-4.3 0-2.5.5.8.9 2.2.9 3m81.1-.5c.5-.3 1.1-.2 1.4.3.2.4-.4.9-1.5.9-1.4.1-1.9-.4-1.8-2 .1-1.7.2-1.8.6-.4.2.9.8 1.5 1.3 1.2m-66.6.7c-.5.8-.2.9.9.5 1-.4 1.7-.1 1.7.6s-1.3 1.4-3 1.5c-3.2.2-3.8-.6-1.8-2.6 1.5-1.5 3.1-1.6 2.2 0m54.5 4.2c-1.1.5-1.9 1.5-1.9 2.3 0 .7-.7 1.3-1.5 1.3s-1.5-.9-1.5-2.1c0-1.1.4-1.8.8-1.5.5.3 1.7-.7 2.7-2 1.7-2.4 2-2.4 2.6-.7.4 1.2.1 2.1-1.2 2.7m13.1-1.2c0-.6.4-1.2.9-1.2s.4 1.1-.2 2.3c-.8 1.8-1.4 2.1-2.3 1.3-.6-.6-.9-1.7-.6-2.4.4-1.1.6-1.1 1.3 0 .7 1 .9 1 .9 0m-2.5 3.9c-.6.9-10.5.8-10.5-.1 0-1.3 3.3-2.1 4-1 .5.7 1.1.6 1.9-.4 1-1.2 1.7-1.3 3.1-.4 1 .7 1.7 1.5 1.5 1.9m-13.7 8.8c.1.1.2.8.2 1.6 0 2.5-12 2-17.3-.6-3.4-1.8-3.9-2.3-2.4-2.8 1.4-.5 15.9.9 19.5 1.8 M79.7 110.1c-1.3 1.3-3.7 3.1-5.2 3.9-1.6.8-3 2.1-3.3 2.8-.3 1.1 0 1.2 1.6.3 1.1-.6 3-1.1 4-1.1 1.2 0 2.8-1.4 4.1-3.6 2.6-4.4 2.1-5.4-1.2-2.3M152.5 56.4c-1.2.8-3.3 1.2-5.5.9-2.5-.4-4 0-4.9 1.1-1.9 2.3-.9 2.9 2.3 1.5 2.2-1 2.9-.9 4.4.4 1.7 1.6 2 1.5 4.2-.8 1.3-1.4 2.9-2.5 3.7-2.5.7 0 1.3-.5 1.3-1 0-1.5-3.3-1.2-5.5.4M149.3 79c-1 1.1-2.9 2-4.4 2-2.7 0-7.9 3.3-6.8 4.4.3.4 1.9 0 3.4-.8 2.1-1.1 3.4-1.2 4.8-.5 1 .6 2 .9 2.1.7s1-1.5 1.9-3.1c1-1.5 2.4-2.7 3.2-2.7s1.5-.5 1.5-1c0-1.8-4-1.1-5.7 1M137.7 92.2c-.7.5.1.8 2 .8 1.7 0 3.9.7 4.7 1.6 1.5 1.4 2 1.4 5.9-.4 2.3-1.1 4.9-2.2 5.7-2.5.8-.2-2.7-.4-7.8-.4-5.1.1-9.8.5-10.5.9M152.6 111.8c-.8 1.3 1.2 2.8 4.7 3.6 2.6.6 2.8.4 2.5-1.6-.2-1.8-1-2.4-3.5-2.6-1.7-.2-3.4.1-3.7.6M121.9 119.7c-.3 2.1.4 4.9 1.4 5.5.6.4.8 1.1.3 1.5s-1.1.2-1.3-.5c-.3-.6-1.3-1.2-2.4-1.2-1.5 0-1.9.7-1.9 3.5s.4 3.5 1.9 3.5c1.1 0 2.1-.6 2.4-1.3.3-.9.7-.9 2 .2 1.5 1.2 1.7 1 1.7-2.3 0-2-.5-3.6-1.1-3.6s-.9-1.2-.7-2.7-.2-2.8-.9-3c-.7-.3-1.4-.1-1.4.4M127.4 126.2c-.4.6.1 1.7 1 2.4 1.6 1.2 1.6 1.3-.1 1.7s-1.7.4 0 1.2c1.1.5 2.3.3 3-.4.9-1 .8-1.6-.7-2.7-1.8-1.3-1.8-1.4-.2-1.4.9 0 1.4-.5 1.1-1-.8-1.4-3.2-1.3-4.1.2M106 115c2 1.3 3.3 1.3 2.5 0-.3-.6-1.4-1-2.3-1-1.5 0-1.5.2-.2 1M75.6 124.9c-.5.8-.3 1.6.4 2.1.7.4.9 1.3.6 1.9-.4.6-.2 1.1.3 1.1.6 0 1.1-.4 1.1-.9s.3-1.7.7-2.7c.4-1.1.3-1.5-.4-1.1-.6.4-1.3.2-1.5-.5-.3-.9-.6-.9-1.2.1M92 129.1c0 .5.9.9 2 .9s2-.2 2-.4-.9-.6-2-.9-2-.1-2 .4M184.1 54.1c-2.7 1.2-4.1 2.3-3.5 2.9 1.6 1.6 10.4 5 10.4 4 0-.6-.4-1-1-1-.5 0-1-1.4-1-3 0-1.7.5-3 1-3 .6 0 1-.5 1-1 0-1.4-1.9-1.1-6.9 1.1m2.9 2.4c0 .8-.6 1.5-1.4 1.5-2 0-2.9-1.7-1.4-2.3 2.2-.9 2.8-.7 2.8.8M39.3 55.7c-6.2 3.6-5.8 4.3 3.1 5.9 2.8.5 3.7.4 3.2-.5-.4-.6-2-1.1-3.6-1.1-4.3 0-4.6-1.8-.6-3.7 2-.9 3.6-2 3.6-2.5 0-1.2-1.1-.9-5.7 1.9M38.3 65.7c-1.3.2-2.3.8-2.3 1.3 0 1.1 9.1.1 10.5-1.1.9-.9-3.9-1-8.2-.2M180 66c0 .5 2.5 1 5.5 1s5.5-.5 5.5-1c0-.6-2.5-1-5.5-1s-5.5.4-5.5 1M39.8 70.7c-1.6.2-2.8.9-2.8 1.5s.9.8 2 .5c2.9-.8 3.5 1.1 1.1 3.7-2.7 2.8-2.8 4.5-.1 2.1 1.5-1.4 2.3-1.5 3.4-.6 2.5 2.1 4.7.2 4.4-3.8-.3-3.9-1.7-4.4-8-3.4m6.5 2.6c.7 1.9-.8 4.1-2.2 3.3-1.6-1-1.4-4.6.3-4.6.8 0 1.6.6 1.9 1.3M180.2 72.2c-1.7 1.7-1.5 4.2.6 6.1 2.2 2 6.3 2.2 8 .5s1.5-5.3-.3-6.8c-1.7-1.4-2.1 0-.5 1.6.6.6.8 1.8.4 2.7-.8 2.1-5.7 2.2-7.4.2-1-1.3-1-1.9.5-3.5 1.9-2.1.7-2.8-1.3-.8M40.5 84.4c-2.1 3.2-.3 7.6 3.2 7.6 2.3 0 3.2-2.3 1.3-3.5-.5-.3-1 .1-1 .9 0 1-.6 1.3-1.6.9-.9-.3-1.4-1.5-1.2-3.2.3-2.3.8-2.6 3.8-2.6 3.4 0 3.5.1 3.3 3.5-.1 3.2 0 3.3 1.2 1.7 2.4-3.3-.3-7.7-4.6-7.7-1.9 0-3.4.8-4.4 2.4M182.3 85.2c-2.9.5-5.3 1.3-5.3 1.8 0 1.4 9 7.3 9.8 6.5.4-.4.1-1-.5-1.2-2.1-.8-1.6-5.4.7-6 1.8-.5 3-2.5 1.3-2.2-.5.1-3.2.6-6 1.1m1.7 3.3c0 1.7-.2 1.8-2.4.9-2.3-.9-2-2.4.4-2.4 1.1 0 2 .7 2 1.5M45.1 95.1c-3.1.9-3.3 1.2-2.7 4.2.4 1.7 1.1 3.6 1.7 4.2.8.7.9.3.4-1.5-1.1-3.9-1.1-4.4.5-4.7 1-.2 2 .7 2.7 2.4l1.1 2.8.1-3.3c.1-3.8 2.4-4 2.9-.4.2 1.2.8 2.2 1.3 2.2.8 0 .3-4.3-.8-6.3-.5-.9-2.9-.8-7.2.4M174.4 98.5c-1.7 4.2-1.4 5.1 2.3 5.8 1.6.3 3.6.9 4.6 1.2 1 .4 1.7.3 1.7-.4 0-.6-.7-1.1-1.5-1.1-2.4 0-1.7-3 .9-4 1.3-.5 2.7-1.4 3.1-1.9q1.2-2.1-2.7 0c-1.6.9-2.3.7-3.5-1-2-3-3.2-2.6-4.9 1.4m4 2.4c-.6 1.4-1.3 1.8-2.3 1.2-1-.7-1.1-1.3-.1-3.1 1.5-2.9 3.8-1.1 2.4 1.9M49.6 105.6c-5.4 2-5.9 2.8-1.1 1.9l4-.7-2.7 2.9c-5 5.2-3.3 6.3 4.7 3.3 3.7-1.4 3-2.2-1.2-1.4l-3.7.7 2.7-3.2c4.3-5.2 3.8-5.9-2.7-3.5M172 109.9c-2.2 4.3.1 8.1 4.9 8.1 3.6 0 5.3-2.1 4.9-5.7-.4-3.4-4.1-4-4.6-.8-.2 1.6-.1 1.7.7.5 1.7-2.4 3.6.2 2.2 2.9-1.4 2.5-2.9 2.6-5.9.5-2.8-1.9-2.8-2.8 0-5.4 1.8-1.6 1.9-2 .5-2-.9 0-2.1.9-2.7 1.9M56.8 121.2c-5.2 2.2-5.6 3.2-3.2 6.6 1.8 2.6 5 2.8 8.2.6 2.5-1.7 2.6-2.7.9-6.6l-1.2-2.7zm4.8 2.3c.9 2.3-1.6 4.6-4.5 4.3-3.3-.4-3.9-3.1-.9-4.6 3.2-1.5 4.8-1.4 5.4.3M167.4 123.2c-.4.6 1.2 2.9 3.5 5.1 3 3 4.1 3.7 4.1 2.4 0-.9-.4-1.7-1-1.7s-.5-1 .2-2.5c.6-1.5 2-2.5 3.1-2.5s1.7-.3 1.3-.7c-1.1-1.1-10.6-1.2-11.2-.1m6 2.3c-.4.8-1 1.5-1.4 1.5s-1.3-.7-2-1.5c-1.1-1.3-.8-1.5 1.3-1.5 1.9 0 2.5.4 2.1 1.5M60.9 132.6l-4.7 2.3 1.6 3.6c1.6 3.5 4.1 5.1 2.7 1.7-1.7-4-1.7-5.2-.2-5.2.9 0 1.9.9 2.2 2s1.1 2 1.7 2c.5 0 .7-.7.4-1.5s-.9-2.2-1.2-3c-.4-1 0-1.5 1.1-1.5.9 0 1.9.9 2.2 2s.9 2 1.4 2c.9 0-.9-5.7-1.9-6.4-.4-.2-2.8.7-5.3 2M162.7 131.7c-.2 1-.8 2.7-1.3 3.8-.7 1.7-.6 1.8.8.7s2.3-1 5.7.8c2.3 1.1 4.1 1.6 4.1 1.2 0-.5-1.8-1.7-4-2.7-3.1-1.4-3.9-2.2-3.4-3.7.3-1 .1-1.8-.4-1.8s-1.2.8-1.5 1.7M69.4 145.6c-2.1 1.4-4.1 2.8-4.3 3s.6 1.8 1.8 3.6c1.1 1.8 2.1 2.6 2.1 1.7 0-.8-.6-2.1-1.2-2.8-1.3-1.3-.1-2.8 4.8-5.7 1.1-.7 1.6-1.4 1.3-1.8-.3-.3-2.3.6-4.5 2M167.2 145.5l-1.2 2.6-4.1-2.1c-2.2-1.2-4.4-1.9-4.7-1.6-.6.7-.7.7 4.3 3.5l3.9 2.3 1.8-2.2c1-1.2 1.8-2.8 1.8-3.6 0-2-.5-1.7-1.8 1.1M151.5 153c-.5.7 6.1 10 7.1 10 .2 0 .2-1.1-.2-2.5-.7-2.9 1.2-5.9 3.3-5.1.7.3 1.3.1 1.3-.4 0-.6-.6-1-1.2-1-.7-.1-3.2-.5-5.6-.9-2.3-.5-4.5-.5-4.7-.1m5.8 1.6c.3.3.3 1.2 0 2-.4 1.1-.9 1.2-1.9.3-.8-.6-1.4-1.5-1.4-2 0-1 2.4-1.2 3.3-.3M75.2 155.9c-3 .9-5.1 1.9-4.7 2.3s1.3.4 1.9.2c1.6-.6 3.3 1.4 3.8 4.6.3 2.3.6 2.1 3.2-2.3 1.6-2.8 2.5-5.2 2-5.7-.5-.4-3.3-.1-6.2.9m2.9 3.4c-.9.9-1.4.8-2.1-.3s-.4-1.7.8-2.1c2.1-.8 2.9.8 1.3 2.4M112 190.5c-1.3 1.5-.6 3.5 1.3 3.5.8 0 .7-.4-.2-.9-1.2-.8-1.2-1.2-.2-2.5.8-.9 1.1-1.6.8-1.6-.2 0-1 .7-1.7 1.5M127.6 190.3c.3 1 .9 1.5 1.2 1.2s0-1.1-.7-1.8c-1-.9-1.1-.8-.5.6M106.5 191c.3.5.8 1 1.1 1 .2 0 .4-.5.4-1 0-.6-.5-1-1.1-1-.5 0-.7.4-.4 1M115.2 193c0 1.4.2 1.9.5 1.2.2-.6.2-1.8 0-2.5-.3-.6-.5-.1-.5 1.3M107.1 194.6c0 1.1.3 1.4.6.6.3-.7.2-1.6-.1-1.9-.3-.4-.6.2-.5 1.3";

    private SVGPath createIcon(String pathData, double size, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(pathData);
        icon.setFill(Color.web(fill));
        double scale = size / 24.0;
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }

    private SVGPath createLogo(double size, String fill) {
        SVGPath logo = new SVGPath();
        logo.setContent(LOGO_PATH);
        logo.setFill(Color.web(fill));
        double scale = size / 225.0;
        logo.setScaleX(scale);
        logo.setScaleY(scale);
        return logo;
    }

    private Image createLogoImage(double size, String fill) {
        SVGPath logo = createLogo(size, fill);
        WritableImage img = new WritableImage((int)size, (int)size);
        logo.snapshot(null, img);
        return img;
    }

    private void addHoverScale(Region node, double scaleTo, double seconds) {
        ScaleTransition stIn = new ScaleTransition(Duration.seconds(seconds), node);
        stIn.setToX(scaleTo);
        stIn.setToY(scaleTo);
        ScaleTransition stOut = new ScaleTransition(Duration.seconds(seconds), node);
        stOut.setToX(1.0);
        stOut.setToY(1.0);
        node.setOnMouseEntered(e -> stIn.playFromStart());
        node.setOnMouseExited(e -> stOut.playFromStart());
    }

    private BorderPane root;
    private StackPane contentArea;

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: " + COLOR_BACKGROUND + ";");

        // 1. Sidebar
        VBox sidebar = createSidebar();
        root.getChildren().add(sidebar);

        // 2. Main Content Area (Header + Canvas)
        VBox contentArea = new VBox();
        contentArea.setPadding(new Insets(20, 0, 0, 0)); // Added top padding to lower the header slightly
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        // TopAppBar
        HBox header = createHeader();
        contentArea.getChildren().add(header);

        // Main Canvas
        VBox mainCanvas = new VBox(16);
        // Reduced top padding to bring everything up
        mainCanvas.setPadding(new Insets(10, 40, 40, 40));
        mainCanvas.setAlignment(Pos.TOP_CENTER);
        mainCanvas.setMaxWidth(1200);

        // --- SECCIÓN 1: HEADER ---
        VBox headerSec = new VBox(8);
        Text h1 = new Text("Panel de Administración Central");
        h1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 32));
        h1.setFill(Color.web(COLOR_ON_SURFACE));
        
        Text sub = new Text("Bienvenido de nuevo. Aquí tienes un resumen del estado institucional hoy.");
        sub.setFont(Font.font("Plus Jakarta Sans", 16));
        sub.setFill(Color.web(COLOR_OUTLINE));
        headerSec.getChildren().addAll(h1, sub);

        // --- SECCIÓN 2: KPI BENTO GRID ---
        HBox kpiGrid = new HBox(16);
        kpiGrid.setAlignment(Pos.CENTER);
        kpiGrid.getChildren().addAll(
            createKpiCard("Total Estudiantes", "1,250", COLOR_SECONDARY_FIXED, COLOR_SECONDARY, ICON_PERSON_PIN),
            createKpiCard("Total Cursos", "35", COLOR_PRIMARY_FIXED, COLOR_PRIMARY, ICON_TRENDING_UP),
            createKpiCard("Profesores", "42", COLOR_TERTIARY_FIXED, COLOR_TERTIARY, ICON_SCHOOL),
            createKpiCard("Asistencia Estudiantes", "92%", COLOR_SECONDARY_FIXED, COLOR_SECONDARY, ICON_CHECK_CIRCLE)
        );

        // --- SECCIÓN 3: DUAL COLUMN ---
        HBox middleSection = new HBox(24);
        
        // Gestión de Cursos (8/12)
        VBox coursesBox = createCourseManagement();
        // Removed Hgrow to prevent vertical expansion and white space
        
        // Desempeño (4/12)
        VBox performanceBox = createPerformancePanel();
        performanceBox.setPrefWidth(320);

        middleSection.getChildren().addAll(coursesBox, performanceBox);

        // --- SECCIÓN 4: HORARIO DE HOY ---
        VBox scheduleBox = createSchedulePanel();

        mainCanvas.getChildren().addAll(headerSec, kpiGrid, middleSection, scheduleBox);
        
        // Inicializar el controlador para agregar secciones dinámicamente
        DashboardController controller = new DashboardController(mainCanvas, kpiGrid, middleSection, scheduleBox);
        
        // Ejemplo: Agregar una nueva KPI Card dinámicamente
        // controller.addKpiCard("Nueva Métrica", "100", COLOR_PRIMARY_FIXED, "⭐");
        
        StackPane centerWrapper = new StackPane(mainCanvas);
        centerWrapper.setAlignment(Pos.TOP_CENTER);
        contentArea.getChildren().add(centerWrapper);

        root.getChildren().add(contentArea);

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add("data:text/css," + getInternalCSS());
        
        primaryStage.setTitle("  GESTIÓN DE NOTAS");
        primaryStage.setScene(scene);
        
        root.setFocusTraversable(true);
        root.requestFocus();
        
        // Stage icon
        primaryStage.getIcons().add(createLogoImage(32, COLOR_PRIMARY));

        primaryStage.show();
    }

    private StackPane stackWithFab(Region center) {
        StackPane stack = new StackPane(center);
        stack.setAlignment(Pos.TOP_CENTER);

        // FAB Bolt
        StackPane fab = new StackPane();
        fab.setPrefSize(64, 64);
        fab.getStyleClass().add("fab-button");
        SVGPath bolt = createIcon(ICON_BOLT, 28, COLOR_WHITE);
        fab.getChildren().add(bolt);

        StackPane.setMargin(fab, new Insets(0, 0, 40, 0));
        StackPane.setAlignment(fab, Pos.BOTTOM_RIGHT);
        
        // El FAB debe estar en una capa superior
        Pane fabLayer = new Pane(fab);
        fabLayer.setPrefSize(1300, 850);
        
        // Envolver todo
        StackPane rootStack = new StackPane(center, fabLayer);
        return rootStack;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(24);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(0, 20, 32, 20));
        sidebar.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent " + COLOR_SURFACE_CONTAINER_HIGH + " transparent transparent;");

        // Logo
        StackPane logoWrapper = new StackPane();
        logoWrapper.setAlignment(Pos.CENTER_LEFT);
        logoWrapper.setPrefWidth(Double.MAX_VALUE);
        SVGPath logo = createLogo(70, COLOR_PRIMARY);
        logoWrapper.getChildren().add(logo);
        sidebar.getChildren().add(logoWrapper);

        // Navegación con Iconos
        String[][] items = {
            {"Inicio", ICON_HOME},
            {"Estudiantes", ICON_SCHOOL},
            {"Profesores", ICON_GROUP},
            {"Cursos", ICON_BOOK},
            {"Horario", ICON_CALENDAR},
            {"Configuración", ICON_SETTINGS}
        };

        for (int i = 0; i < items.length; i++) {
            HBox btnContainer = new HBox(12);
            btnContainer.setAlignment(Pos.CENTER_LEFT);
            btnContainer.setPadding(new Insets(12, 16, 12, 16));
            btnContainer.setPrefWidth(Double.MAX_VALUE);
            
            SVGPath icon = createIcon(items[i][1], 20, COLOR_WHITE);
            
            Text label = new Text(items[i][0]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.MEDIUM, 15));
            
            if (i == 0) {
                btnContainer.getStyleClass().add("sidebar-active");
                label.setFill(Color.WHITE);
                icon.setFill(Color.WHITE);
            } else {
                btnContainer.getStyleClass().add("nav-item");
                icon.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                label.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                
                int idx = i;
                btnContainer.setOnMouseEntered(e -> {
                    label.setFill(Color.web(COLOR_PRIMARY));
                    icon.setFill(Color.web(COLOR_PRIMARY));
                });
                btnContainer.setOnMouseExited(e -> {
                    label.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                    icon.setFill(Color.web(COLOR_ON_SURFACE_VARIANT));
                });
            }
            
            btnContainer.getChildren().addAll(icon, label);
            sidebar.getChildren().add(btnContainer);
        }

        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 32, 0, 32));
        header.setPrefHeight(70);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: " + COLOR_WHITE + "; -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER_HIGH + " transparent;");

        // Search Bar
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(10, 20, 10, 20));
        searchBox.getStyleClass().add("search-container");
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPrefWidth(400);
        
        SVGPath searchIcon = createIcon(ICON_SEARCH, 20, COLOR_OUTLINE);

        TextField searchField = new TextField();
        searchField.setPromptText("Buscar expedientes, cursos o reportes...");
        searchField.getStyleClass().add("search-input");
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        searchBox.getChildren().addAll(searchIcon, searchField);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right Side Container
        HBox rightContainer = new HBox(24);
        rightContainer.setAlignment(Pos.CENTER_RIGHT);

        // Notification Bell
        StackPane notifBtn = new StackPane();
        notifBtn.setPrefSize(32, 32);
        notifBtn.getStyleClass().add("icon-button");
        SVGPath bellIcon = createIcon(ICON_NOTIFICATIONS, 20, COLOR_ON_SURFACE_VARIANT);
        notifBtn.getChildren().add(bellIcon);
        addHoverScale(notifBtn, 1.1, 0.2);

        // Help Button
        StackPane helpBtn = new StackPane();
        helpBtn.setPrefSize(32, 32);
        helpBtn.getStyleClass().add("icon-button");
        SVGPath helpIcon = createIcon(ICON_HELP, 20, COLOR_ON_SURFACE_VARIANT);
        helpBtn.getChildren().add(helpIcon);
        addHoverScale(helpBtn, 1.1, 0.2);

        // Vertical Separator
        Rectangle separator = new Rectangle(1, 32, Color.web(COLOR_SURFACE_CONTAINER_HIGH));
        
        // User Profile
        HBox userBox = new HBox(12);
        userBox.setAlignment(Pos.CENTER_RIGHT);
        userBox.setCursor(Cursor.HAND);
        VBox uText = new VBox(2);
        uText.setAlignment(Pos.CENTER_RIGHT);
        Text uName = new Text("Admin User");
        uName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 14));
        Text uEmail = new Text("ADMIN@LUMINA.EDU");
        uEmail.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        uEmail.setFill(Color.web(COLOR_OUTLINE));
        uText.getChildren().addAll(uName, uEmail);
        
        Circle avatar = new Circle(20, Color.web(COLOR_PRIMARY_FIXED));
        avatar.setStroke(Color.web(COLOR_PRIMARY_FIXED));
        avatar.setStrokeWidth(2);
        SVGPath avatarSvg = createIcon(ICON_AVATAR, 20, COLOR_PRIMARY);
        StackPane avatarStack = new StackPane(avatar, avatarSvg);
        
        userBox.setOnMouseEntered(e -> {
            uName.setFill(Color.web(COLOR_PRIMARY));
            avatar.setStroke(Color.web(COLOR_PRIMARY));
        });
        userBox.setOnMouseExited(e -> {
            uName.setFill(Color.web(COLOR_ON_SURFACE));
            avatar.setStroke(Color.web(COLOR_PRIMARY_FIXED));
        });
        
        userBox.getChildren().addAll(uText, avatarStack);

        rightContainer.getChildren().addAll(notifBtn, helpBtn, separator, userBox);

        header.getChildren().addAll(searchBox, spacer, rightContainer);
        return header;
    }

    private VBox createKpiCard(String label, String value, String bgColor, String iconColor, String iconPath) {
        HBox card = new HBox(12);
        card.setPadding(new Insets(12));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("glass-card");

        // Icon in a colored circle
        Circle iconCircle = new Circle(16, Color.web(bgColor));
        SVGPath iconSvg = createIcon(iconPath, 16, iconColor);
        StackPane iconStack = new StackPane(iconCircle, iconSvg);
        
        // Hover: scale icon with transition
        ScaleTransition stIn = new ScaleTransition(Duration.seconds(0.2), iconStack);
        stIn.setToX(1.1);
        stIn.setToY(1.1);
        ScaleTransition stOut = new ScaleTransition(Duration.seconds(0.2), iconStack);
        stOut.setToX(1.0);
        stOut.setToY(1.0);
        card.setOnMouseEntered(e -> stIn.playFromStart());
        card.setOnMouseExited(e -> stOut.playFromStart());
        
        VBox text = new VBox(2);
        Text lbl = new Text(label.toUpperCase());
        lbl.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        lbl.setFill(Color.web(COLOR_OUTLINE));
        Text val = new Text(value);
        val.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        val.setFill(Color.web(COLOR_ON_SURFACE));
        text.getChildren().addAll(lbl, val);

        card.getChildren().addAll(iconStack, text);
        VBox wrapper = new VBox(card);
        HBox.setHgrow(wrapper, Priority.ALWAYS);
        return wrapper;
    }

    private VBox createCourseManagement() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(0));

        // Header
        HBox head = new HBox();
        head.setPadding(new Insets(12));
        head.setAlignment(Pos.CENTER_LEFT);
        Text title = new Text("Gestión de Cursos");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button btnAll = new Button("Ver todos");
        btnAll.getStyleClass().add("text-button");
        head.getChildren().addAll(title, spacer, btnAll);

        // Tabla
        VBox table = new VBox();
        table.setPadding(new Insets(0, 12, 0, 12));
        
        HBox cols = new HBox();
        cols.setPadding(new Insets(8, 0, 8, 0));
        cols.setStyle("-fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-background-radius: 8;");
        cols.getChildren().addAll(
            createColH("CURSO", 180), 
            createColH("PROFESOR", 180), 
            createColH("ALUMNOS", 120), 
            createColH("RENDIMIENTO", 120)
        );

        table.getChildren().addAll(cols);
        table.getChildren().addAll(
            createCourseRow("Introducción a la IA", "Dr. Roberto Sánchez", "32 Estudiantes", 0.92, "9.2"),
            createCourseRow("Cálculo Avanzado", "Dra. Elena Méndez", "28 Estudiantes", 0.78, "7.8"),
            createCourseRow("Literatura Moderna", "Prof. Juan Carlos Rico", "40 Estudiantes", 0.85, "8.5"),
            createCourseRow("Diseño UX/UI", "Mtra. Sofía Valdéz", "24 Estudiantes", 0.88, "8.8")
        );

        panel.getChildren().addAll(head, table);
        return panel;
    }

    private HBox createColH(String t, double w) {
        Text txt = new Text(t);
        txt.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
        txt.setFill(Color.web(COLOR_OUTLINE));
        HBox box = new HBox(txt);
        box.setPrefWidth(w);
        return box;
    }

    private HBox createCourseRow(String name, String prof, String st, double prog, String score) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10, 0, 0, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("course-row");

        Text tName = new Text(name);
        tName.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 13));
        tName.setFill(Color.web(COLOR_ON_SURFACE));
        
        HBox progBox = new HBox(10);
        progBox.setAlignment(Pos.CENTER);
        
        Rectangle bg = new Rectangle(50, 5, Color.web(COLOR_SURFACE_CONTAINER));
        bg.setArcWidth(5); bg.setArcHeight(5);
        Rectangle fill = new Rectangle(50 * prog, 5, Color.web(COLOR_PRIMARY));
        fill.setArcWidth(5); fill.setArcHeight(5);
        
        Text tScore = new Text(score);
        tScore.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        tScore.setFill(Color.web(COLOR_PRIMARY));

        progBox.getChildren().addAll(new Pane(bg, fill), tScore);

        // Stacked text for Students: Number (Bold) + "Estudiantes" (Smaller)
        String[] stParts = st.split(" ");
        VBox stBox = new VBox(2);
        stBox.setAlignment(Pos.CENTER_LEFT);
        Text stNum = new Text(stParts[0]);
        stNum.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 12));
        Text stLabel = new Text(stParts[1]);
        stLabel.setFont(Font.font("Plus Jakarta Sans", 10));
        stLabel.setFill(Color.web(COLOR_OUTLINE));
        stBox.getChildren().addAll(stNum, stLabel);

        HBox hName = new HBox(tName);
        hName.setPrefWidth(180);
        HBox hProf = new HBox(new Text(prof));
        hProf.setPrefWidth(180);
        HBox hStud = new HBox(stBox);
        hStud.setPrefWidth(120);
        HBox hPerf = progBox;
        hPerf.setPrefWidth(120);

        row.getChildren().addAll(hName, hProf, hStud, hPerf);

        return row;
    }

    private VBox createPerformancePanel() {
        VBox panel = new VBox(8);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(12));

        HBox head = new HBox();
        Text title = new Text("Desempeño");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        StackPane moreBtn = new StackPane();
        moreBtn.setPrefSize(32, 32);
        moreBtn.getStyleClass().add("icon-button");
        SVGPath moreDots = createIcon(ICON_MORE_HORIZ, 20, COLOR_OUTLINE);
        moreBtn.getChildren().add(moreDots);
        head.getChildren().addAll(title, spacer, moreBtn);

        Text sub = new Text("Promedio general mensual (6 meses)");
        sub.setFont(Font.font("Plus Jakarta Sans", 12));
        sub.setFill(Color.web(COLOR_OUTLINE));

        // Gráfico
        HBox chart = new HBox(12);
        chart.setAlignment(Pos.BOTTOM_CENTER);
        chart.setPrefHeight(120);
        
        String[] labels = {"5to E", "6to A", "4to B", "4to C", "5to A", "6to B"};
        int[] heights = {60, 70, 80, 60, 110, 90};

        for (int i = 0; i < labels.length; i++) {
            VBox barBox = new VBox(8);
            barBox.setAlignment(Pos.BOTTOM_CENTER);
            Color barColor = (i == 4) ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_PRIMARY_FIXED);
            Rectangle bar = new Rectangle(20, heights[i], barColor);
            bar.setArcWidth(10); bar.setArcHeight(10);
            // Make top rounded, bottom flat
            bar.setArcWidth(10);
            bar.setArcHeight(10);
            Text label = new Text(labels[i]);
            label.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 10));
            label.setFill(i == 4 ? Color.web(COLOR_PRIMARY) : Color.web(COLOR_OUTLINE));
            barBox.getChildren().addAll(bar, label);
            chart.getChildren().add(barBox);
        }

        HBox footer = new HBox();
        Text fT = new Text("Crecimiento Semestral");
        fT.setFont(Font.font("Plus Jakarta Sans", 12));
        fT.setFill(Color.web(COLOR_OUTLINE));
        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);
        Label fV = new Label("+12.4%");
        fV.getStyleClass().add("growth-badge");
        footer.getChildren().addAll(fT, s2, fV);

        panel.getChildren().addAll(head, sub, chart, footer);
        return panel;
    }

    private VBox createSchedulePanel() {
        VBox panel = new VBox(8);
        panel.getStyleClass().add("glass-card");
        panel.setPadding(new Insets(12));

        Text title = new Text("Horario de Hoy");
        title.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 18));
        title.setFill(Color.web(COLOR_ON_SURFACE));

        // LISTA
        VBox list = new VBox(6);
        list.getChildren().addAll(
            createScheduleRow("08:00", "Matemáticas Avanzadas", "Salón 402 • Prof. Sánchez", false),
            createScheduleRow("10:00", "Historia Universal", "Biblioteca • Dra. Méndez", false),
            createScheduleRow("12:00", "Química Orgánica", "Laboratorio B • Prof. Rico", false),
            createScheduleRow("14:00", "Física Cuántica", "Laboratorio A • Prof. Einstein", false),
            createScheduleRow("16:00", "Arte Moderno", "Galería • Prof. Picasso", false),
            createScheduleRow("18:00", "Inglés Técnico", "Aula 10 • Prof. Smith", false)
        );

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        sp.setPrefHeight(220);

        panel.getChildren().addAll(title, sp);
        return panel;
    }

    private HBox createScheduleRow(String time, String subj, String det, boolean isFirst) {
        HBox row = new HBox(16);
        row.setPadding(new Insets(8, 0, 8, 0));
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("schedule-row");

        // Clock Icon
        Circle iconCircle = new Circle(24, Color.web(COLOR_SURFACE_LOW));
        SVGPath clockIcon = createIcon(ICON_SCHEDULE, 20, COLOR_ON_SURFACE);
        StackPane iconStack = new StackPane(iconCircle, clockIcon);
        
        // Hover effect for schedule icon
        Color hoverBg, hoverText;
        if (subj.contains("Matemáticas") || subj.contains("Física")) {
            hoverBg = Color.web(COLOR_PRIMARY);
            hoverText = Color.WHITE;
        } else if (subj.contains("Historia") || subj.contains("Inglés")) {
            hoverBg = Color.web(COLOR_SECONDARY);
            hoverText = Color.WHITE;
        } else {
            hoverBg = Color.web(COLOR_TERTIARY);
            hoverText = Color.WHITE;
        }
        row.setOnMouseEntered(e -> {
            iconCircle.setFill(hoverBg);
            clockIcon.setFill(hoverText);
        });
        row.setOnMouseExited(e -> {
            iconCircle.setFill(Color.web(COLOR_SURFACE_LOW));
            clockIcon.setFill(Color.web(COLOR_ON_SURFACE));
        });
        
        VBox text = new VBox(4);
        Text t1 = new Text(time + " - " + subj);
        t1.setFont(Font.font("Plus Jakarta Sans", FontWeight.BOLD, 15));
        t1.setFill(Color.web(COLOR_ON_SURFACE));
        Text t2 = new Text(det);
        t2.setFont(Font.font("Plus Jakarta Sans", 13));
        t2.setFill(Color.web(COLOR_OUTLINE));
        text.getChildren().addAll(t1, t2);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (isFirst) {
            // FAB Bolt for the first row
            StackPane fab = new StackPane();
            fab.setPrefSize(40, 40);
            fab.getStyleClass().add("fab-button");
            SVGPath bolt = createIcon(ICON_BOLT, 18, COLOR_WHITE);
            fab.getChildren().add(bolt);
            row.getChildren().addAll(iconStack, text, spacer, fab);
        } else {
            StackPane arrowBtn = new StackPane();
            arrowBtn.setPrefSize(40, 40);
            arrowBtn.getStyleClass().add("icon-button");
            SVGPath arrow = createIcon(ICON_CHEVRON_RIGHT, 20, COLOR_OUTLINE);
            arrowBtn.getChildren().add(arrow);
            row.getChildren().addAll(iconStack, text, spacer, arrowBtn);
        }
        return row;
    }

    private String getInternalCSS() {
        return ".nav-item { -fx-background-color: transparent; -fx-cursor: hand; -fx-text-fill: " + COLOR_ON_SURFACE_VARIANT + "; } " +
                ".nav-item:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-cursor: hand; -fx-background-radius: 12; -fx-text-fill: " + COLOR_PRIMARY + "; } " +
               ".sidebar-active { -fx-background-color: " + COLOR_PRIMARY_CONTAINER + "; -fx-cursor: hand; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(37,99,235,0.3), 16, 0, 0, 8); } " +
               ".search-container { -fx-background-color: " + COLOR_SURFACE_LOW + "; -fx-background-radius: 32; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 32; } " +
               ".search-input { -fx-background-color: transparent; -fx-border-color: transparent; -fx-font-family: 'Plus Jakarta Sans'; -fx-text-fill: " + COLOR_ON_SURFACE + "; -fx-prompt-text-fill: " + COLOR_OUTLINE + "; } " +
               ".glass-card { -fx-background-color: " + COLOR_WHITE + "; -fx-background-radius: 16; -fx-border-color: " + COLOR_SURFACE_CONTAINER + "; -fx-border-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.05), 20, 0, 0, 4); } " +
               ".course-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".course-row:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; } " +
               ".schedule-row { -fx-border-color: transparent transparent " + COLOR_SURFACE_CONTAINER + " transparent; } " +
               ".schedule-row:hover { -fx-background-color: " + COLOR_SURFACE_LOW + "; } " +
               ".text-button { -fx-text-fill: " + COLOR_PRIMARY + "; -fx-background-color: transparent; -fx-underline: false; -fx-cursor: hand; -fx-font-weight: bold; } " +
               ".text-button:hover { -fx-underline: true; } " +
               ".icon-button { -fx-background-radius: 32; -fx-cursor: hand; } " +
                ".growth-badge { -fx-background-color: " + COLOR_PRIMARY_FIXED + "; -fx-text-fill: " + COLOR_PRIMARY + "; -fx-font-weight: bold; -fx-padding: 4 12 4 12; -fx-background-radius: 16; } " +
               ".fab-button { -fx-background-color: " + COLOR_PRIMARY + "; -fx-background-radius: 32; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,74,198,0.4), 15, 0, 0, 8); } " +
               ".fab-button:hover { -fx-scale-x: 1.1; -fx-scale-y: 1.1; }";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
