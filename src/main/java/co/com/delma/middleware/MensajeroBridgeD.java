package co.com.delma.middleware;
import io.vertx.amqpbridge.AmqpBridge;
import io.vertx.amqpbridge.AmqpConstants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;

import co.com.delma.middleware.Runner;

/*En esta clase se trata de implementar con la libreria AmqpBridge
 * Por alguna razón desconocida no funciona y nos envía el error
 * Bridge startup failed: io.vertx.core.VertxException: Disconnected
 */

public class MensajeroBridgeD extends AbstractVerticle  {
	  
	private int count = 1;
	  
	public static void main(String[] args) {
		Runner.runExample(MensajeroBridgeD.class);
		
		System.out.print("ya");
		
}
	
	 @Override
	  public void start() throws Exception {
	    AmqpBridge bridge = AmqpBridge.create(vertx);

	    // Start the bridge, then use the event loop thread to process things thereafter.
	    bridge.start("localhost", 5672, res -> {
	    	if(!res.succeeded()) {
		        System.out.println("Bridge startup failed: " + res.cause());
		        return;
		      }

	      // Set up a producer using the bridge, send a message with it.
	      MessageProducer<JsonObject> producer = bridge.createProducer("myAmqpAddress");

	      // Schedule sending of a message every second
	      System.out.println("Producer created, scheduling sends.");
	      vertx.setPeriodic(1000, v -> {
	        JsonObject amqpMsgPayload = new JsonObject();
	        amqpMsgPayload.put(AmqpConstants.BODY, "myStringContent" + count);

	        producer.send(amqpMsgPayload);

	        System.out.println("Sent message: " + count++);
	      });
	    });
	  }
	
	
}