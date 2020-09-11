package uk.gov.hmcts.ethos.ecm.consumer.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.gov.hmcts.ecm.common.client.CcdClient;
import uk.gov.hmcts.ecm.common.model.bulk.BulkData;
import uk.gov.hmcts.ecm.common.model.bulk.SubmitBulkEvent;
import uk.gov.hmcts.ecm.common.model.multiples.MultipleData;
import uk.gov.hmcts.ecm.common.model.multiples.SubmitMultipleEvent;
import uk.gov.hmcts.ecm.common.model.servicebus.UpdateCaseMsg;
import uk.gov.hmcts.ethos.ecm.consumer.helpers.Helper;
import uk.gov.hmcts.reform.ethos.ecm.consumer.domain.MultipleErrors;
import uk.gov.hmcts.reform.ethos.ecm.consumer.service.MultipleUpdateService;
import uk.gov.hmcts.reform.ethos.ecm.consumer.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static uk.gov.hmcts.reform.ethos.ecm.consumer.helpers.Constants.UNPROCESSABLE_STATE;

@RunWith(SpringJUnit4ClassRunner.class)
public class MultipleUpdateServiceTest {

    @InjectMocks
    private MultipleUpdateService multipleUpdateService;
    @Mock
    private CcdClient ccdClient;
    @Mock
    private UserService userService;

    private List<SubmitMultipleEvent> submitMultipleEvents;
    private SubmitMultipleEvent submitMultipleEvent;
    private UpdateCaseMsg updateCaseMsg;

    @Before
    public void setUp() {
        submitMultipleEvent = new SubmitMultipleEvent();
        MultipleData multipleData = new MultipleData();
        multipleData.setMultipleReference("4100001");
        submitMultipleEvent.setCaseData(multipleData);
        submitMultipleEvents = new ArrayList<>(Collections.singletonList(submitMultipleEvent));
        updateCaseMsg = Helper.generateUpdateCaseMsg();
    }

    @Test
    public void sendUpdateToMultipleLogic() throws IOException {
        when(userService.getAccessToken()).thenReturn("Token");
        when(ccdClient.retrieveMultipleCasesElasticSearchWithRetries(anyString(), anyString(), anyString())).thenReturn(submitMultipleEvents);

        //when(ccdClient.retrieveBulkCases(anyString(), anyString(), anyString())).thenReturn(submitBulkEvents);

        when(ccdClient.submitMultipleEventForCase(anyString(), any(), anyString(), anyString(), any(), anyString())).thenReturn(submitMultipleEvent);
        multipleUpdateService.sendUpdateToMultipleLogic(updateCaseMsg, new ArrayList<>());
    }

    @Test
    public void sendUpdateToMultipleLogicEmptyES() throws IOException {
        when(userService.getAccessToken()).thenReturn("Token");
        when(ccdClient.retrieveMultipleCasesElasticSearchWithRetries(anyString(), anyString(), anyString())).thenReturn(new ArrayList<>());

        //when(ccdClient.retrieveBulkCases(anyString(), anyString(), anyString())).thenReturn(new ArrayList<>());

        multipleUpdateService.sendUpdateToMultipleLogic(updateCaseMsg, new ArrayList<>());
    }

    @Test
    public void sendUpdateToMultipleLogicNullES() throws IOException {
        when(userService.getAccessToken()).thenReturn("Token");
        when(ccdClient.retrieveMultipleCasesElasticSearchWithRetries(anyString(), anyString(), anyString())).thenReturn(null);

        //when(ccdClient.retrieveBulkCases(anyString(), anyString(), anyString())).thenReturn(null);

        multipleUpdateService.sendUpdateToMultipleLogic(updateCaseMsg, new ArrayList<>());
    }

    @Test
    public void sendUpdateToMultipleLogicWithErrors() throws IOException {
        when(userService.getAccessToken()).thenReturn("Token");
        when(ccdClient.retrieveMultipleCasesElasticSearchWithRetries(anyString(), anyString(), anyString())).thenReturn(submitMultipleEvents);

        //when(ccdClient.retrieveBulkCases(anyString(), anyString(), anyString())).thenReturn(submitBulkEvents);

        when(ccdClient.submitMultipleEventForCase(anyString(), any(), anyString(), anyString(), any(), anyString())).thenReturn(submitMultipleEvent);
        MultipleErrors multipleErrors = new MultipleErrors();
        multipleErrors.setDescription(UNPROCESSABLE_STATE);
        multipleUpdateService.sendUpdateToMultipleLogic(updateCaseMsg, new ArrayList<>(Collections.singletonList(
            multipleErrors)));
    }

    @Test(expected = Exception.class)
    public void sendUpdateToMultipleLogicException() throws IOException {
        when(userService.getAccessToken()).thenReturn("Token");
        when(ccdClient.retrieveMultipleCasesElasticSearchWithRetries(anyString(), anyString(), anyString())).thenThrow(new Exception());

        //when(ccdClient.retrieveBulkCases(anyString(), anyString(), anyString())).thenThrow(new Exception());

        multipleUpdateService.sendUpdateToMultipleLogic(updateCaseMsg, new ArrayList<>());
    }

}
