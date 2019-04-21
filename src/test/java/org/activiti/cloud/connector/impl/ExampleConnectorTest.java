package org.activiti.cloud.connector.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.activiti.api.runtime.model.impl.IntegrationContextImpl;
import org.activiti.cloud.api.process.model.IntegrationResult;
import org.activiti.cloud.api.process.model.impl.IntegrationRequestImpl;
import org.activiti.cloud.connector.impl.ExampleConnectorChannels;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.support.StringObjectMapBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleConnectorTest {
	
    @Value("${spring.application.name}")
    private String appName;
	
    @Autowired
    private ExampleConnectorChannels channels;

    @Autowired
    private MessageCollector messageCollector;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private BinderAwareChannelResolver resolver;
    
    private BlockingQueue<Message<?>> messages;
	
    @Before
    public void setUp() {
    	// Resolve connector integration result destination for message collector
    	messages = messageCollector.forChannel(resolver.resolveDestination("integrationResult_"+appName));
    }
    
	@Test
	public void testContextLoads() {
		// success
	}
	
	@Test
	public void testPerformTask() throws InterruptedException, IOException {
	    // given
    	Map<String, Object> inboundVariables = new StringObjectMapBuilder()
    			.get();
    	
    	IntegrationContextImpl integrationContext = new IntegrationContextImpl();
    	integrationContext.setInBoundVariables(inboundVariables);
    	integrationContext.setProcessInstanceId(UUID.randomUUID().toString());
    	integrationContext.setBusinessKey("1");
    	
    	IntegrationRequestImpl integrationRequest = new IntegrationRequestImpl(integrationContext );
        integrationRequest.setServiceFullName(appName);
        
        // when
        channels.exampleConnectorConsumer().send(MessageBuilder.withPayload(objectMapper.writeValueAsString(integrationRequest))
				   .build());
        
        Message<?> received = messages.poll();

        // then
        IntegrationResult result = objectMapper.readValue(received.getPayload().toString(), IntegrationResult.class);
        
		assertThat(result.getIntegrationContext().getOutBoundVariables().get("var1")).asString()
																					 .contains(integrationContext.getProcessInstanceId());
	}	

}
