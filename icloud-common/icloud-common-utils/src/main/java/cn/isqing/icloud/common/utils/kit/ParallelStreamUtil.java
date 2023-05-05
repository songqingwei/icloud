package cn.isqing.icloud.common.utils.kit;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParallelStreamUtil {

    public static ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static <T, R> List<R> exec(List<T> input, Function<T, R> function, int timeoutSeconds) throws Exception {

        List<CompletableFuture<R>> futures = input.stream()
                .map(item -> CompletableFuture.<R>supplyAsync(() -> {
                    // 真正的执行逻辑
                    return function.apply(item);
                }, forkJoinPool))
                .collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        allFutures.get(timeoutSeconds, TimeUnit.SECONDS);
        List<R> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return result;
    }


    public static <T> void exec(List<T> input, Consumer<T> consumer, int timeoutSeconds) throws Exception {
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(input.stream()
                .map(item -> CompletableFuture.runAsync(() ->
                                consumer.accept(item)
                        , forkJoinPool)).toArray(CompletableFuture[]::new));
        allFutures.get(timeoutSeconds, TimeUnit.SECONDS);
    }
}
