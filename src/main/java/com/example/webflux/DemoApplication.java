package com.example.webflux;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Schedules;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws InterruptedException {
		//SpringApplication.run(DemoApplication.class, args);

		Map<String, Mono<Boolean>> map=new HashMap<>();
		map.put("A1",Mono.just(false));
		map.put("A2",Mono.just(false));
		map.put("A3",Mono.just(false));
		map.put("A4",Mono.just(false));
		new Thread("Kapil-Thread"){
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					int finalI = i;
					map.computeIfAbsent(("B"+i), ds->runAsync(Mono.just(finalI)));
					try {
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Random random=new Random(10);
			private <T> Mono<Boolean> runAsync(Mono<T> just) {
				if(random.nextInt()%2==0){
					return just.flatMap(this::fetch).thenReturn(true);
				}else{
					return just.flatMap(this::fetch).thenReturn(false);
				}
			}

			private <R, T> Mono<? extends R> fetch(T t) {
				try {
					Thread.sleep(3000);
					System.out.println(Thread.currentThread().getName()+" = Data Processor called"+t);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return (Mono<? extends R>) Mono.just(true);
			}

		}.start();


		System.out.println("Before traverse");

		Flux.fromIterable(map
				.values())
				.flatMap(v->Flux.merge(v)
						.collectList().doOnNext(System.out::println)).subscribe();

		/*Flux.fromIterable(map
				.values())
				.flatMap(m->m.subscribeOn(Schedulers.boundedElastic()))
				.then().block();*/
//		Flux.merge(map
//				.values())
////				.window(8)
//				.collectList().doOnNext(System.out::println).subscribe();
////				.delaySequence(Duration.ofSeconds(1L))
////				.parallel()
////				.runOn(Schedulers.parallel())
////				.subscribeOn(Schedulers.fromExecutor(Executors.newFixedThreadPool(11)))
////				.flatMap(v->{
////					v.collectList().doOnNext(System.out::println).subscribe();
////					System.out.println("KAPIL-"+v);
////					return Mono.just(v);
////				})
////				.then().block();




		/*Flux.zip(map.values(), v -> false).map(v -> {
			System.out.println("KAPIL-" + v);
			return v;
		}).subscribe();*/

		System.out.println("welcome");


		Thread.sleep(500000000);
	}

}
