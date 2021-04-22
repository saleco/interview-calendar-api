package com.github.saleco.interview.calendar.api.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public abstract class AbstractService {

    protected PageRequest getPageRequest(int pageNumber, int pageSize, String... properties) {
        return PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, properties);
    }
}
