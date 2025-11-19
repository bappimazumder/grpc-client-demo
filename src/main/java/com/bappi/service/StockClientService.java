package com.bappi.service;

import com.bappi.grpc.StockRequest;
import com.bappi.grpc.StockResponse;
import com.bappi.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class StockClientService {
   /* @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingServiceBlockingStub;

    public StockResponse getStockPrice(String symbol) {
        StockRequest request = StockRequest.newBuilder().setStockSymbol(symbol).build();
        return stockTradingServiceBlockingStub.getStockPrice(request);
    }*/

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;
    public void subscribeStock(String symbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(symbol)
                .build();

        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {

            @Override
            public void onNext(StockResponse stockResponse) {
                System.out.println("Received stock response: " + stockResponse.getStockSymbol()
                + " " + stockResponse.getPrice() + " Time: " + stockResponse.getTimestamp());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.printf("Error: %s\n", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }
        });

    }
}
