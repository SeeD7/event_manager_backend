package com.zeromus.eventmanager.model.mapper;

import lombok.Data;

@Data
public class PageWrapper<T> {
    private T value;
}