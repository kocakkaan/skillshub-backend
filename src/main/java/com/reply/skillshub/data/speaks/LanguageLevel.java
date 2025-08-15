package com.reply.skillshub.data.speaks;

import java.util.logging.Logger;

// CEFR level (A1, A2, B1, B2, C1, C2)
public enum LanguageLevel {
    A1, A2, B1, B2, C1, C2;

    private static final Logger logger = Logger.getLogger(LanguageLevel.class.getName());

    public static LanguageLevel fromString(String level) {
        try {
            return LanguageLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid language level: " + level + ". Returning B1.");
            return LanguageLevel.B1; // Default to B1 if the level is invalid
        }
    }
}
