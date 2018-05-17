import time
import stomp

conn = None
class MyListener(stomp.ConnectionListener):
    def on_message(self, headers, message):
        print('MyListener:\nreceived a message "{}"\n'.format(message))
 
        global conn
        conn.ack(headers['message-id'], headers['subscription'])
        



class MyStatsListener(stomp.StatsListener):
    def on_disconnected(self):
        super(MyStatsListener, self).on_disconnected()
        print('MyStatsListener:\n{}\n'.format(self))



hosts = [('192.168.1.104', 61613)]


conn= stomp.Connection(host_and_ports=hosts)
conn.set_listener('my_listener', MyListener())
conn.set_listener('stats_listener', MyStatsListener())
conn.start()
 
conn.connect('admin', 'anything', wait=True)
 
conn.subscribe(destination='q4_server_2read', id=1, ack='client-individual')
conn.send(body="A Test message", destination='q4_server_2read')
while True:
            time.sleep(1)
            print('sleeeping ')
            conn.send(body="A Test message", destination='q4_server_2read')

#for message in read_messages:
    #conn.ack(message['id'], message['subscription'])
#conn.disconnect()

# Script output ...
#
# MyListener:
# received a message "A Test message"
#
# MyStatsListener:
# Connections: 1
# Messages sent: 5
# Messages received: 1
# Errors: 0
