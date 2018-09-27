package com.stock.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.stock.app.beans.CustomerDetails;

@RestController
@RequestMapping("/StockManagement")
public class StockManagement {
	
	@Autowired
	private EurekaClient eurekaClient;

	@Value("CustomerService")	  
	private String customerServiceId;

	private int productStock = 0;
	
	
	@RequestMapping(value = "/stock/{productid}", method = RequestMethod.GET)
	public int getProductStock(@PathVariable String productid) {//REST Endpoint.
		
		
		if(productid.equalsIgnoreCase("ABC")){
			productStock = 5;			
		}else {
			productStock = 1;
		}
		fetchCustomerDetails();
		return productStock;
	}


	private void fetchCustomerDetails() {
		try {
			
			Application application = eurekaClient.getApplication(customerServiceId);
			InstanceInfo instanceInfo = application.getInstances().get(0);
			String url = "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/" + "API/CustomerDetails/123";
			System.out.println("Customer Service URL:" + url);
			
			RestTemplate restTemplate = new RestTemplate();
			CustomerDetails response = restTemplate.getForObject(url, CustomerDetails.class);
			System.out.println("RESPONSE " + response);
			System.out.println("Customer Name:"+ response.getName());
			System.out.println("Customer address:"+ response.getAddress());
			System.out.println("Customer contact no:"+ response.getContactNo());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
