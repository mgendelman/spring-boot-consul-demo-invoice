package org.springdays.ny2017.demo.consul.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@EnableDiscoveryClient
public class InvoiceController {
	
	@Value("${lineitem.format}")
	private String lineItemFormat;

	@Value("${price}")
	private Double price;

	@Autowired
	private RestTemplate restTemplate;


	
		
	@RequestMapping("/lineitem/{quantity}")
	public String calculateLineItem(@PathVariable(value = "quantity") Integer quantity ) {
		double subtotal = price * quantity;
		double tax = restTemplate.getForObject("http://tax-service/tax/{subtotal}",Double.class, subtotal );
		double total = subtotal + tax;
		return String.format(this.lineItemFormat,  "product", quantity, price, subtotal,  tax, total);
		
	}
	
}
//= "%s %d $%,.2f $%,.2f $%,.2f $%,.2f";