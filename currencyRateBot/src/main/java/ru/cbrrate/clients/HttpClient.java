package ru.cbrrate.clients;

public interface HttpClient {

    String performRequest(String url, String params);

    String performRequest(String url);
}
