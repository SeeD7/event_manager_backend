package com.zeromus.eventmanager.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class WaitingListId implements Serializable {
    private Long event;
    private Long user;
}
