package com.reply.skillshub.base.authentification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void testTokenGeneration() {
        Assertions.assertDoesNotThrow(() -> jwtService.createJwtToken(null));
        Assertions.assertDoesNotThrow(() -> jwtService.createJwtToken("test"));
    }

    @Test
    void testTokenValidationValid() {
        var generatedToken = jwtService.createJwtToken("hallo");
        Assertions.assertTrue(jwtService.validateJwtToken(generatedToken));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"hey"})
    void testTokenValidationInvalid(String falseToken) {
        Assertions.assertFalse(jwtService.validateJwtToken(falseToken));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"myUsername"})
    void testTokenReturnSubject(String subjectToTest) {
        Assertions.assertEquals(subjectToTest, jwtService.getSubjectFromToken(jwtService.createJwtToken(subjectToTest)));
        
    }
    
}
