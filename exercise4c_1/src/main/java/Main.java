import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);
	private static Connection connection;
	private static Session session;
	private static MessageProducer messageProducer;
	private static Destination destination; 
	
	public static void main(String[] args) {

		try {
			connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic("BIEDRO94.TOPIC");
			messageProducer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			
			
			long start = System.currentTimeMillis();
			
			for(int i=0; i<10000;i++){
				message.setText("test_"+i);
				message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
				messageProducer.send(message);
			}
			long stop = System.currentTimeMillis() - start;
			
			log.info("persistent {" + stop + "} milliseconds.\n");
			
			long start1 = System.currentTimeMillis();
			
			for(int i=0; i<10000;i++){
				message.setText("test_"+i);
				message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
				messageProducer.send(message);
			}
			
			long stop2 = System.currentTimeMillis() - start;
			log.info("non persistent {" + stop2 + "} milliseconds.\n");
			
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
