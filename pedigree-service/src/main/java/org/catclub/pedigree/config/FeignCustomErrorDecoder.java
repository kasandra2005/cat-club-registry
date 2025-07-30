package org.catclub.pedigree.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.catclub.shared.exception.FeignClientException;

public class FeignCustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new FeignClientException("Bad request through Feign", response.status());
            case 404 -> new FeignClientException("Resource not found", response.status());
            case 500 -> new FeignClientException("Server error through Feign", response.status());
            default -> new FeignClientException("Feign client error", response.status());
        };
    }
}
