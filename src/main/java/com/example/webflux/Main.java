package com.example.webflux;

import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Main main=new Main();
        main.start();
    }

    private void start() throws InterruptedException {
        Mono<Boolean> result = Mono.just(1).flatMap(Main::test1)
                .onErrorContinue(new BiConsumer<Throwable, Object>() {
                    @Override
                    public void accept(Throwable throwable, Object o) {
                        System.out.println("on1ERROR");
                        System.out.println(throwable.toString());
                    }
        }).thenReturn(false);
        System.out.println("WElcome="+result.block());

        Thread.sleep(500000);
    }

    private static <R> Mono<? extends R> test1(Integer integer) {
        System.out.println("called");
        try {
            //Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Mono<? extends R>)Mono.just(1);
    }

    public static Mono<Integer> test() {
        System.out.println("called");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Mono.just(1);
    }


}
