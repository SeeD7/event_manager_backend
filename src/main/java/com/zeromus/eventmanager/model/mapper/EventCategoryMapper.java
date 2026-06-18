package com.zeromus.eventmanager.model.mapper;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.entity.EventCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventCategoryMapper {
    EventCategoryDto toDto(EventCategory eventCategory);

    EventCategory toEntity(EventCategoryDto dto);
}
