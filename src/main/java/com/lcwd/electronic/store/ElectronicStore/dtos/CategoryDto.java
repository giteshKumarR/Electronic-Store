package com.lcwd.electronic.store.ElectronicStore.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CategoryDto {

    private String categoryId;

//    @Min(value = 4, message = "Minimum 4 characters required!!!") --> this is having some issue to avoid using this
    @NotBlank
    @Size(min = 4, max = 20, message = "Title must be of minimum 4 characters!!")
    private String categoryTitle;

    @NotBlank(message = "Description required !!")
    private String categoryDescription;

    private String categoryCoverImage;
}
