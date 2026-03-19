package com.example.springbootconcepts.mappers;

import com.example.springbootconcepts.domains.Image;
import com.example.springbootconcepts.dto.ImageInfo;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageInfo imageToImageInfo(Image image);
    Image imageInfoToImage(ImageInfo imageInfo);

}
