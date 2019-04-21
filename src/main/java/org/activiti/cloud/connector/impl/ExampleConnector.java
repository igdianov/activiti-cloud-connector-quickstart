package org.activiti.cloud.connector.impl;

import org.activiti.cloud.api.process.model.IntegrationRequest;
import org.activiti.cloud.api.process.model.IntegrationResult;
import org.activiti.cloud.connectors.starter.channels.IntegrationResultSender;
import org.activiti.cloud.connectors.starter.configuration.ConnectorProperties;
import org.activiti.cloud.connectors.starter.model.IntegrationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@EnableBinding(ExampleConnectorChannels.class)
public class ExampleConnector {

    private final Logger logger = LoggerFactory.getLogger(ExampleConnector.class);

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ConnectorProperties connectorProperties;

    private final IntegrationResultSender integrationResultSender;

    public ExampleConnector(IntegrationResultSender integrationResultSender) {

        this.integrationResultSender = integrationResultSender;
    }

    @StreamListener(value = ExampleConnectorChannels.EXAMPLE_CONNECTOR_CONSUMER)
    public void performTask(IntegrationRequest event) throws InterruptedException {

        logger.info(">> Inside Example Cloud Connector: " + event.getIntegrationContext().getInBoundVariables());

        Map<String, Object> results = new HashMap<>();
        
        try {
        	String var1 = ExampleConnector.class.getSimpleName()+" was called for instance " + event.getIntegrationContext().getProcessInstanceId();

        	// @TODO: add your code here

        	// @TODO: set your results here, i.e.
        	results.put("var1", var1);
        } catch(Exception e) {	
        	results.put("exception", e);
        
        } finally {
        	Message<IntegrationResult> message = IntegrationResultBuilder.resultFor(event, connectorProperties)
														                 .withOutboundVariables(results)
														                 .buildMessage();
        	integrationResultSender.send(message);
        }

    }


}
