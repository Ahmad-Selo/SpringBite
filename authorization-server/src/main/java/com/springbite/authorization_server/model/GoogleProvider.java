package com.springbite.authorization_server.model;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.springbite.authorization_server.security.oauth2.JwtAudValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Component
public class GoogleProvider implements Provider {

    private final Log logger = LogFactory.getLog(GoogleProvider.class);

    public static final String GOOGLE_URI = "/auth/google";

    @Value("${provider.google.jwk_set_uri}")
    private String JWK_SET_URI;

    @Value("${provider.google.issuer}")
    private String ISSUER;

    @Value("${provider.google.aud}")
    private String AUD;

    private final ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

    private final Set<SignatureAlgorithm> signatureAlgorithms = new HashSet<>();

    @Override
    public JwtDecoder jwtDecoder() {
        jwtProcessor.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(JOSEObjectType.JWT));

        URL url;

        try {
            url = URI.create(JWK_SET_URI).toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        JWKSource<SecurityContext> jwkSource = JWKSourceBuilder
                .create(url)
                .retrying(true)
                .build();

        Set<JWSAlgorithm> jwsAlgs = new HashSet<>();
        jwsAlgs.addAll(JWSAlgorithm.Family.SIGNATURE);
        jwsAlgs.addAll(JWSAlgorithm.Family.HMAC_SHA);

        for (JWSAlgorithm jwsAlgorithm : jwsAlgs) {
            signatureAlgorithms.add(SignatureAlgorithm.from(jwsAlgorithm.getName()));
        }

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(jwsAlgs, jwkSource);

        jwtProcessor.setJWSKeySelector(keySelector);

        Consumer<ConfigurableJWTProcessor<SecurityContext>> jwtProcessorCustomizer = jwtProcessor -> {
        };
        Consumer<Set<SignatureAlgorithm>> signatureAlgorithmsConsumer = signatureAlgorithms -> {
        };

        jwtProcessorCustomizer.accept(jwtProcessor);
        signatureAlgorithmsConsumer.accept(signatureAlgorithms);

        OAuth2TokenValidator<Jwt> jwtValidator = new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(ISSUER),
                new JwtAudValidator(AUD)
        );

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(JWK_SET_URI)
                .jwtProcessorCustomizer(jwtProcessorCustomizer)
                .jwsAlgorithms(signatureAlgorithmsConsumer)
                .build();

        jwtDecoder.setJwtValidator(jwtValidator);

        return jwtDecoder;
    }
}
