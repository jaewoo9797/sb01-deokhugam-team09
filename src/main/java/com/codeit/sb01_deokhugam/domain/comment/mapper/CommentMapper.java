package com.codeit.sb01_deokhugam.domain.comment.mapper;


import com.codeit.sb01_deokhugam.domain.comment.dto.CommentDto;
import com.codeit.sb01_deokhugam.domain.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "comment.user.id", target = "userId")
    @Mapping(source = "userNickname", target = "userNickname")
    CommentDto toDto(Comment comment, String userNickname);

}
