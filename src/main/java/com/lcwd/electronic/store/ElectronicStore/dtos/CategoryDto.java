package com.lcwd.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoryDto {

    private String categoryId;

    @NotBlank
    @Min(value = 4, message = "Title must be of minimum 4 characters !!")
    private String categoryTitle;

    @NotBlank(message = "Description required !!")
    private String categoryDescription;

    private String categoryCoverImage;
}
