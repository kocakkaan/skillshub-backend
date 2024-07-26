package com.reply.skillshub.data.language;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.LanguageAlpha3Code;
import com.neovisionaries.i18n.LanguageCode;
import com.neovisionaries.i18n.LocaleCode;

import lombok.Data;

@Node
@Data
public class Language {
    
    // ISO 639-1 language code.
    @Id
    private LanguageCode languageCode;

    // ISO 639-2 language code.
    private LanguageAlpha3Code languageAlpha3Code;
    
}
