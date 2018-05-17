package com.activemq.receiver;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver {

	// URL of the JMS server
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	// default broker URL is : tcp://localhost:61616"

	// Name of the queue we will receive messages from
	private static String subject = "q4_server_2read";

	public static void main(String[] args) throws JMSException {
		// Getting JMS connection from the server
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
//		Connection connection = connectionFactory.createConnection();
		Connection connection = connectionFactory.createConnection("admin", "anything");
		connection.start();

		// Creating session for seding messages
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Getting the queue 'JCG_QUEUE'
		Destination destination = session.createQueue(subject);

		// MessageConsumer is used for receiving (consuming) messages
		MessageConsumer consumer = session.createConsumer(destination);

		while (true) {
			System.out.println("whilee1");
			// Here we receive the message.
			Message message = consumer.receive();
			System.out.println("whilee2");
			// We will be using TestMessage in our example. MessageProducer sent
			// us a TextMessage
			// so we must cast to it to get access to its .getText() method.
			
			if (message instanceof TextMessage) {
				TextMessage textMessage = (TextMessage) message;
				System.out.println("Received message '" + textMessage.getText()
						+ "'");
     		}
			else{
				BytesMessage bm = (BytesMessage) message;
			    byte data[] = new byte[(int) bm.getBodyLength()];
			    bm.readBytes(data);
			    System.out.println("Received messageee '" + new String(data)	+ "'");
			}
			System.out.println("whilee3");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		connection.close();
	}
}
