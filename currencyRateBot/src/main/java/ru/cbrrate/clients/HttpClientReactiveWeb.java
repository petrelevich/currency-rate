package ru.cbrrate.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class HttpClientReactiveWeb implements HttpClientReactive {

    private final WebClient.Builder webBuilder;

    public HttpClientReactiveWeb(WebClient.Builder webBuilder) {
        this.webBuilder = webBuilder;
    }

    @Override
    public Mono<String> performRequest(String url) {
        log.info("http request, url:{}", url);
        var client = webBuilder.baseUrl(url).build();
        return client.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("Http request error, url:{}", url, error))
                .doOnNext(val -> log.info("val:{}", val));
    }
}
