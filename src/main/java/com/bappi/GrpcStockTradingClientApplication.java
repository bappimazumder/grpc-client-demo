package com.bappi;

import com.bappi.service.StockClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrpcStockTradingClientApplication implements CommandLineRunner {

	private final StockClientService stockClientService;

	public GrpcStockTradingClientApplication(StockClientService stockClientService) {
		this.stockClientService = stockClientService;
	}

	public static void main(String[] args) {
		SpringApplication.run(GrpcStockTradingClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Unary Request
		 //System.out.println("Client Response from grpc : " + stockClientService.getStockPrice("Dff"));

		// server Streaming
		 // stockClientService.subscribeStock("Dff");

		// Client Streaming
		stockClientService.placeBulkOrder();
	}
}
