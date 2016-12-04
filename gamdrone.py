import os
import glob
import time
from bluetooth import *

#while True:
#	print(read_temp())	
#	time.sleep(1)

server_sock = BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

print "TTT"

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
	print "Waiting for connection on RFCOMM channel %d" % port
	client_sock, client_info = server_sock.accept()
	print "VVV"
	print "XXX"
	print "Accepted connection from ", client_info

	try:
	        data = client_sock.recv(1024)
        	if len(data) == 0: break
	        print "received [%s]" % data

		if data == 'WTF!':
			data = 'WTF!'
		print "sending [%s]" % data

	except IOError:
		pass

	except KeyboardInterrupt:

		print "disconnected"

		client_sock.close()
		server_sock.close()
		print "all done"

		break
