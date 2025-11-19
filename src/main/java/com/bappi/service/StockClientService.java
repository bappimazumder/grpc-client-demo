package com.bappi.service;

import com.bappi.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;


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

    public void placeBulkOrder() {
        StreamObserver<OrderSummary> responseObserver = new StreamObserver<OrderSummary>() {

            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println("Received order summary: ");
                System.out.printf("Total orders: " + orderSummary.getTotalOrders());
                System.out.println(" Successful Orders: "+ orderSummary.getSuccessCount());
                System.out.println("Total Amount " +orderSummary.getTotalAmount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Error: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }

        };
        StreamObserver<StockOrder> requestObserver = stockTradingServiceStub.bulkStockOrder(responseObserver);

        // send multiple steam of stock order message/request
        try {
            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("1")
                    .setStockSymbol("Dff")
                    .setOrderType("RR")
                    .setPrice(150.5)
                    .setQuantity(10)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("2")
                    .setStockSymbol("Rff")
                    .setOrderType("Test")
                    .setPrice(2700.0)
                    .setQuantity(5)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("3")
                    .setStockSymbol("Cgg")
                    .setOrderType("Evv")
                    .setPrice(700.0)
                    .setQuantity(8)
                    .build());

            //done sending orders
            requestObserver.onCompleted();
        } catch (Exception ex) {
            requestObserver.onError(ex);
        }
    }
}
