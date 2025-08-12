package org.example.bookstoreapp.mapper;

import org.example.bookstoreapp.config.MapperConfig;
import org.example.bookstoreapp.dto.user.UserRegistrationRequestDto;
import org.example.bookstoreapp.dto.user.UsersResponseDto;
import org.example.bookstoreapp.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UsersResponseDto toDto(User user);

    User toModel (UserRegistrationRequestDto userRegistrationRequestDto);
}
