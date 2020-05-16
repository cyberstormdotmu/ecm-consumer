package uk.gov.hmcts.reform.ethos.ecm.consumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGeneratorFactory;
import uk.gov.hmcts.reform.ethos.ecm.consumer.idam.IdamApi;

@Configuration
@EnableFeignClients(clients = IdamApi.class)
public class ServiceTokenGeneratorConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServiceTokenGeneratorConfiguration.class);

    @Bean
    public AuthTokenGenerator authTokenGenerator(
            @Value("${idam.s2s-auth.totp_secret}") String secret,
            @Value("${idam.s2s-auth.microservice}") String microService,
        ServiceAuthorisationApi serviceAuthorisationApi
    ) {
        log.info("SECRET: " + secret);
        log.info("MICRO SERVICE: " + microService);
        return AuthTokenGeneratorFactory.createDefaultGenerator(secret, microService, serviceAuthorisationApi);
    }

}
