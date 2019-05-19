package com.ewolff.microservice.bonus;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("test")
public class OrderRestServiceStub {

	@RequestMapping(value = "/feed", method = RequestMethod.GET, produces="application/json")
	public String feed() {
		return "{" +
				"  \"updated\" : \"2019-04-20T15:28:50Z\"," +
				"  \"orders\" : [" +
				"    {\"id\": \"1\", \"link\": \"http://localhost:8080/order/1\", \"updated\": \"2019-04-20T15:27:58Z\"}"+
				"  ]" +
				"}";
	}

	@RequestMapping(value = "/order/1", method = RequestMethod.GET)
	public String order1() {
		return "{" +
				"  \"id\" : 1," +
				"  \"customer\" : {" +
				"    \"customerId\" : 1," +
				"    \"name\" : \"Wolff\"," +
				"    \"firstname\" : \"Eberhard\"," +
				"    \"email\" : \"eberhard.wolff@gmail.com\"," +
				"    \"street\" : \"Unter den Linden\"," +
				"    \"city\" : \"Berlin\"" +
				"  }," +
				"  \"deliveryService\": \"Hermes\","+
				"  \"updated\" : \"2019-04-20T15:42:12.351+0000\"," +
				"  \"shippingAddress\" : {" +
				"    \"street\" : \"Ohlauer Str. 43\"," +
				"    \"zip\" : \"10999\"," +
				"    \"city\" : \"Berlin\"" +
				"  }," +
				"  \"billingAddress\" : {" +
				"    \"street\" : \"Krischerstr. 100\"," +
				"    \"zip\" : \"40789\"," +
				"    \"city\" : \"Monheim am Rhein\"" +
				"  }," +
				"  \"orderLine\" : [ {" +
				"    \"count\" : 42," +
				"    \"item\" : {" +
				"      \"itemId\" : 1," +
				"      \"name\" : \"iPod\"," +
				"      \"price\" : 42.0" +
				"    }" +
				"  } ]," +
				"  \"numberOfLines\" : 1," +
				"  \"_links\" : {" +
				"    \"self\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }," +
				"    \"order\" : {" +
				"      \"href\" : \"http://localhost:8080/order/1\"" +
				"    }" +
				"  }" +
				"}";
	}

}
