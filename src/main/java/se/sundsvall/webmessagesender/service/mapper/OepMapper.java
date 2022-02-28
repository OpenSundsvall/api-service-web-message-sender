package se.sundsvall.webmessagesender.service.mapper;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;

import static javax.xml.datatype.DatatypeFactory.newInstance;

import se.sundsvall.webmessagesender.generatedsources.oep.AddMessage;
import se.sundsvall.webmessagesender.generatedsources.oep.IntegrationMessage;

public class OepMapper {
	private OepMapper() {}
	
	public static AddMessage toAddMessage(String message, int flowInstanceId) throws DatatypeConfigurationException {
		IntegrationMessage integrationMessage = new IntegrationMessage();
		integrationMessage.setAdded(newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		integrationMessage.setMessage(message);
		
		AddMessage addMessage = new AddMessage();
		addMessage.setFlowInstanceID(flowInstanceId);
		addMessage.setMessage(integrationMessage);
		
		return addMessage;
	}
}
