package com.activemq.receiver;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender {

	// URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server
	// is on localhost
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	// default broker URL is : tcp://localhost:61616"
	private static String subject = "q4_ahenk1_2read"; // Queue Name.You can
														// create any/many queue
														// names as per your
														// requirement.

	public static void main(String[] args) throws JMSException {
		// Getting JMS connection from the server and starting it
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
//		Connection connection = connectionFactory.createConnection();
		Connection connection = connectionFactory.createConnection("admin", "anything");
		connection.start();

		// Creating a non transactional session to send/receive JMS message.
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);

		// Destination represents here our queue 'JCG_QUEUE' on the JMS server.
		// The queue will be created automatically on the server.
		Destination destination = session.createQueue(subject);

		// MessageProducer is used for sending messages to the queue.
		MessageProducer producer = session.createProducer(destination);

		// We will send a small text message saying 'Hello World!!!'
		TextMessage message = session.createTextMessage("{\"type\":\"RESPONSE_AGREEMENT\",\"parameterMap\":{\"path\":\"/sample-agreementtttttttt.txt\",\"password\":\"1\",\"port\":22,\"host\":\"192.168.1.110\",\"username\":\"lider\"},\"protocol\":\"SSH\",\"timestamp\":1525257599245}");
		
		            message = session.createTextMessage("{\"type\":\"EXECUTE_TASK\",\"fileServerConf\":null, \"task\":{ \"id\":21856, \"plugin\":{ \"id\":1951,\"taskPlugin\":true,\"name\":\"network-inventory\", \"version\":\"1.0.0\", \"description\":\"Lider Network Inventory Plugin\", \"active\":true, \"deleted\":false,\"machineOriented\":true, \"userOriented\":false, \"policyPlugin\":false, \"xBased\":false, \"createDate\":1464254876000, \"modifyDate\":1465283994000 }, \"commandClsId\":\"SCANNETWORK\", \"parameterMap\":{ \"timingTemplate\":\"3\", \"ipRange\":\"192.168.1.100-101\", \"executeOnAgent\":true }, \"deleted\":false, \"cronExpression\":null, \"createDate\":1465285670481, \"modifyDate\":null }, \"timestamp\":\"07-06-2016 10:47\"}");
//        			message = session.createTextMessage("{\"type\":\"EXECUTE_TASK\",\"task\":{\"id\":21856,\"plugin\":{\"id\":1951,\"name\":\"network-inventory\",\"version\":\"1.0.0\",\"description\":\"Lider Network Inventory Plugin\",\"active\":true,\"deleted\":false,\"machineOriented\":true,\"userOriented\":false,\"policyPlugin\":false,\"xBased\":false,\"createDate\":1464254876000,\"modifyDate\":1465283994000 },\"commandClsId\":\"SCANNETWORK\",\"parameterMap\":{\"timingTemplate\":\"3\",\"ipRange\":\"192.168.1.100-101\",\"executeOnAgent\":true },\"deleted\":false,\"cronExpression\":null,\"createDate\":1465285670481,\"modifyDate\":null },\"timestamp\":\"07-06-2016 10:47\"}");

		// Here we are sending our message!
		producer.send(message);

		System.out.println("sentttttt  '" + message.getText() + "'");
		connection.close();
	}
}
