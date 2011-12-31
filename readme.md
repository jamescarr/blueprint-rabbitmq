# RabbitMQ Publisher Buffering
In this branch I experimented with an idea of buffering messages that
couldn't be published because of a connection failure. 

I basically
extend RabbitTemplate with BufferedRabbitTemplate in the producer module
which simply adds messages to an instance of java.util.Queue when a
ConnectException occurs. THe message is wrapped in a special object of
type UnsentMessage that contains the routing key, exchange, and the
message. The ConnectException is not propagated up.

To send the unsent messages back out on reconnection I use
UnsetnMessageHandler (which is an instance of ConnectionListenr) which
has the java.util.Queue used in BufferedRabbitTemplate and an
AmqpTemplate instance as dependencies. On connection creation it
attempts to drain all the unsent messages from the unsent messages queue
and publish them all out on the provided AmqpTemplate.

## Drawbacks
The actual implementation in this example is an in memory
ConcurrentLinkedQueue, so failure is likely as the number of unpublished
messages grow. However it is completely possible to reimplement the
Queue as something that overflows to disk when a certain threshold is
reached.



