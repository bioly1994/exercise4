
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static Logger log = LoggerFactory.getLogger(Main.class);
	private static Connection connection;
	private static Session session;
	private static Topic topic;
	private static MessageConsumer messageConsumer;
	
	public static void main(String[] args) {
		
		try {
			connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
			connection.setClientID("1995");
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			topic = session.createTopic("BIOLY.TOPIC");
			messageConsumer = session.createDurableSubscriber(topic, "bioly");
			
			messageConsumer.setMessageListener(new MessageListener() {
				
				public void onMessage(Message message) {
					String text = "";
					
					try {
						text = ((TextMessage) message).getText();
						log.info(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			});
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
