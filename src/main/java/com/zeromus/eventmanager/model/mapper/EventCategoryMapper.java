package com.zeromus.eventmanager.model.mapper;

import com.zeromus.eventmanager.model.dto.EventCategoryDto;
import com.zeromus.eventmanager.model.dto.EventCategoryLightDto;
import com.zeromus.eventmanager.model.entity.EventCategory;
import com.zeromus.eventmanager.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventCategoryMapper {

    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "lastUpdater", source = "lastUpdater")
    EventCategoryDto toDto(EventCategory eventCategory);

    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "lastUpdater", ignore = true)
    EventCategory toEntity(EventCategoryDto dto);

    EventCategoryLightDto toLightDto(EventCategory eventCategory);

    default String mapUserToString(User user) {
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }
}
