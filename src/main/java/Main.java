
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private static Connection connection;	
	private static MessageProducer producer;
	private static Session session;
	private static Destination destination;
	
	public static void main(String[] args) {		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("BIEDRO.QUEUE");
			producer = session.createProducer(destination);
			
			TextMessage message = session.createTextMessage();
			
			long startTime = System.currentTimeMillis();
			
			for(Integer i=0; i<10000;i++){
				message.setText("test_"+i.toString());
				message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
				producer.send(message);
			}
			
			long summarytime = System.currentTimeMillis()- startTime;
			log.info("10000 persistent messages sent in {" + summarytime + "} milliseconds.\n");			
					
			startTime = System.currentTimeMillis();
			for(Integer i=0; i<10000;i++){				
				message.setText("test_"+i.toString());
				message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				producer.send(message);
			}
			summarytime = System.currentTimeMillis()- startTime;
			log.info("10000 persistent messages sent in {" + summarytime + "} milliseconds.\n");	
			
			producer.close();
			session.close();
			connection.close();
			
			System.out.println("KONIEC");
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
