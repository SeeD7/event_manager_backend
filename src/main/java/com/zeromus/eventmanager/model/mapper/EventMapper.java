package com.zeromus.eventmanager.model.mapper;

import com.zeromus.eventmanager.model.dto.EventCardDto;
import com.zeromus.eventmanager.model.dto.EventDto;
import com.zeromus.eventmanager.model.dto.EventFormDto;
import com.zeromus.eventmanager.model.dto.UserDto;
import com.zeromus.eventmanager.model.entity.Event;
import com.zeromus.eventmanager.model.entity.EventWaitingList;
import com.zeromus.eventmanager.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {
    @Mapping(target = "creator", source = "creator")
    @Mapping(target = "lastUpdater", source = "lastUpdater")
    @Mapping(target = "isCurrentUserRegistered", ignore = true)
    @Mapping(target = "isCurrentUserInWaitingList", ignore = true)
    EventDto toDto(Event event);

    EventCardDto toCardDto (Event event);

    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "lastUpdater", ignore = true)
    Event toEntity(EventDto dto);

    Event toEntity(EventFormDto dto);

    default String mapUserToString(User user) {
        if (user == null) {
            return null;
        }
        return user.getUsername();
    }

    default List<UserDto> mapWaitingList(List<EventWaitingList> ewl, UserMapper userMapper) {
        if (ewl == null) {
            return new ArrayList<>();
        }
        return ewl.stream().map(EventWaitingList::getUser).map(userMapper::toDto).toList();
    }

    default Long mapListOfUserToLong(List<User> list) {
        if (list == null) {
            return null;
        }
        return (long) list.size();
    }

    default Long mapEventWaitingListToLong(List<EventWaitingList> list) {
        if (list == null) {
            return null;
        }
        return (long) list.size();
    }
}
