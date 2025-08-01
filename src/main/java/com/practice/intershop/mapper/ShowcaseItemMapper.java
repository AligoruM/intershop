package com.practice.intershop.mapper;

import com.practice.intershop.dto.ShowcaseItemDto;
import com.practice.intershop.model.ShowcaseItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowcaseItemMapper {

    ShowcaseItemDto toDto(ShowcaseItem showcaseItem);

    List<ShowcaseItemDto> toDto(List<ShowcaseItem> showcaseItem);

}
