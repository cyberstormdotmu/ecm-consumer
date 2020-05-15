package uk.gov.hmcts.reform.ethos.ecm.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.ecm.common.idam.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.ecm.common.idam.models.UserDetails;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

@Slf4j
@Component
public class UserService implements uk.gov.hmcts.ecm.common.service.UserService {

    public static final String OPENID_GRANT_TYPE = "password";
    public static final String OPENID_SCOPE = "openid";
    public static final String BEARER_AUTH_TYPE = "Bearer";

    private final IdamClient idamClient;
//    private final OAuth2Configuration oauth2Configuration;
//    private final RestTemplate restTemplate;
//
//    @Value("${idam.api.url.oidc}")
//    private String idamApiOIDCUrl;

    @Autowired
    public UserService(IdamClient idamClient) {
        this.idamClient = idamClient;
//        this.oauth2Configuration = oauth2Configuration;
//        this.restTemplate = restTemplate;
    }

//    public ApiAccessToken loginUser(String userName, String password) {
//        ResponseEntity<ApiAccessToken> responseEntity = idamApi.loginUser(userName, password);
//        return responseEntity != null && responseEntity.getBody() != null
//            ? responseEntity.getBody()
//            : null;
//    }

//    public String getAccessToken(String username, String password) {
//        TokenRequest tokenRequest =
//            new TokenRequest(
//                oauth2Configuration.getClientId(),
//                oauth2Configuration.getClientSecret(),
//                OPENID_GRANT_TYPE,
//                oauth2Configuration.getRedirectUri(),
//                username,
//                password,
//                OPENID_SCOPE,
//                null,
//                null
//            );
//        ResponseEntity<TokenResponse> responseEntity = idamApi.generateOpenIdToken(tokenRequest);
//        return responseEntity != null && responseEntity.getBody() != null
//            ? BEARER_AUTH_TYPE + " " + responseEntity.getBody().accessToken
//            : "";
//    }

//    public String getAccessToken(String username, String password) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        log.info("IdamAPI: " + idamApiOIDCUrl);
//        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(idamApiOIDCUrl,
//                                                                                  new HttpEntity<>(getTokenRequest(username, password), headers),
//                                                                                  TokenResponse.class);
//        return responseEntity.getBody() != null
//            ? BEARER_AUTH_TYPE + " " + responseEntity.getBody().accessToken
//            : "";
//    }
//
//    private MultiValueMap<String, String> getTokenRequest(String username, String password) {
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("client_id", oauth2Configuration.getClientId());
//        map.add("client_secret", oauth2Configuration.getClientSecret());
//        map.add("grant_type", OPENID_GRANT_TYPE);
//        map.add("redirect_uri", oauth2Configuration.getRedirectUri());
//        map.add("username", username);
//        map.add("password", password);
//        map.add("scope", OPENID_SCOPE);
//        map.add("refresh_token", null);
//        map.add("code", null);
//        return map;
//    }




    public String getAccessToken(String userName, String password) {
        return idamClient.getAccessToken(userName, password);
    }

    @Override
    public UserDetails getUserDetails(String authorisation) {
        UserInfo userInfo = idamClient.getUserInfo(authorisation);
        UserDetails userDetails = new UserDetails();
        userDetails.setUid(userInfo.getUid());
        userDetails.setRoles(userInfo.getRoles());
        userDetails.setName(userInfo.getName());
        userDetails.setLastName(userInfo.getFamilyName());
        userDetails.setFirstName(userInfo.getGivenName());
        return userDetails;
    }
}
