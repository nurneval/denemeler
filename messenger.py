#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# Author: Volkan Şahin <volkansah.in> <bm.volkansahin@gmail.com>
# Author: İsmail BAŞARAN <ismail.basaran@tubitak.gov.tr> <basaran.ismaill@gmail.com>
import json

import time
import stomp
from base.scope import Scope
 
read_messages = []
connection_to_receive_message = None
connection_to_send_message = None
  
class Messenger(stomp.ConnectionListener):


    def __init__(self):
        scope = Scope().get_instance()
        self.logger = scope.get_logger()
        self.configuration_manager = scope.get_configuration_manager()
        self.event_manager = scope.get_event_manager()

        self.user_name =  str(self.configuration_manager.get('CONNECTION', 'ActiveMQ_username'))
        self.password =  str(self.configuration_manager.get('CONNECTION', 'ActiveMQ_password'))
        self.queue_name_to_read_from =  'q4_'+  str(self.configuration_manager.get('CONNECTION', 'uid')) +'_2read'
        self.queue_name_to_write_to =   'q4_server_2read'
        self.host_and_port_info = [ (str(self.configuration_manager.get('CONNECTION', 'ActiveMQ_server')),str(self.configuration_manager.get('CONNECTION', 'ActiveMQ_port')))]
        
      

    def connect_to_server_to_receive_messages(self):   
        try:
            global connection_to_receive_message

            connection_to_receive_message= stomp.Connection(host_and_ports=self.host_and_port_info)
            connection_to_receive_message.set_listener('my_listener', self)
            connection_to_receive_message.start()
            connection_to_receive_message.connect(self.user_name, self.password, wait=True)
            connection_to_receive_message.subscribe(destination=self.queue_name_to_read_from , id=1, ack='client-individual')
           
            time.sleep(1)
            
            self.logger.info('Connection for receiving messages is established successfully') 
            
        except Exception as e:
            self.logger.error('Connection for receiving messages is failed! Error Message: {0}'.format(str(e)))
            return False 
            
            
    def connect_to_server_to_send_messages(self):   
        try:
            global connection_to_send_message

            connection_to_send_message= stomp.Connection(host_and_ports=self.host_and_port_info)
            connection_to_send_message.start()
            connection_to_send_message.connect(self.user_name, self.password, wait=True)
           
            time.sleep(1)
            
            self.logger.info('Connection for sending messages is established successfully') 
                
        except Exception as e:
            self.logger.error('Connection for sending messages is failed! Error Message: {0}'.format(str(e)))
            return False 
            
    
    def connect_to_server(self):  
        self.logger.info('###-connect_to_server')

        self.connect_to_server_to_receive_messages()
        self.connect_to_server_to_send_messages()
              
            
    def on_message(self, headers, message):
        self.logger.info('---------->Received message: {0}'.format(message))

        global connection_to_receive_message
        connection_to_receive_message.ack(headers['message-id'], headers['subscription']) 
        
        try:
            j = json.loads(message)
            message_type = j['type']
            self.event_manager.fireEvent(message_type,str(message))
            self.logger.debug('Fired event is: {0}'.format(message_type))
        except Exception as e:
            self.logger.error('A problem occurred while keeping message. Error Message: {0}'.format(str(e)))
        
                
    def send_direct_message(self, msg):
        
        global connection_to_send_message

        try:
            self.logger.info('<<--------Sending message: {0}'.format(str(msg)))
          
            connection_to_send_message.send(body=str(msg), destination=self.queue_name_to_write_to) 

        except Exception as e:
            self.logger.error(
                'A problem occurred while sending direct message. Error Message: {0}'.format(str(e)))
 
