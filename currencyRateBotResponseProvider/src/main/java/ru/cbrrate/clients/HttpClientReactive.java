package ru.cbrrate.clients;

import reactor.core.publisher.Mono;

public interface HttpClientReactive {

    Mono<String> performRequest(String url);
}
