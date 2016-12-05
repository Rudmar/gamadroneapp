import os
import glob
import time
import random
import struct
from bluetooth import *

#while True:
#	print(read_speed())
#	time.sleep(1)

server_sock = BluetoothSocket(RFCOMM)
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

print port

uuid = "00001101-0000-1000-8000-00805f9b34fb"

advertise_service( server_sock, "RASPBERRY",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
#                   protocols = [ OBEX_UUID ] 
                    )
while True:

	try:    
		print "Waiting for connection on RFCOMM channel %d" % port
		client_sock, client_info = server_sock.accept()
		print "Accepted connection from ", client_info
	        messageReceived = client_sock.recv(1024)
	        print messageReceived
	        info = client_info
	       	if len(messageReceived) == 0:
	       		break
	       	else:
	        	print "received: %s from " % messageReceived, info
			if messageReceived == 'Esta mensagem foi enviada do meu APP GamaDrone\n':
				messageSent = 'Esta mensagem foi enviada da minha plataforma Ubuntu\n'
				send = struct.pack('!i', len(messageSent))
				client_sock.send(messageSent)
				print "sending: %s" % messageSent
			else:
				pass
				

	except IOError:
		pass

	except KeyboardInterrupt:
		print "Disconnected by KeyboardInterrupt"
		client_sock.close()
		server_sock.close()

	client_sock.close()