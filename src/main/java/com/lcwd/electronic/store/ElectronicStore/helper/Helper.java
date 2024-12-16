package com.lcwd.electronic.store.ElectronicStore.helper;

import com.lcwd.electronic.store.ElectronicStore.dtos.ProductDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.Product;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <U, V> PagableResponse<V> getPagableResponse(Page<U> pageObject, Class<V> type) {
        // Means page of type User mila and humne return kiya page of type UserDto
        // U --> Entity (User)
        // V --> dto (UserDto)
        List<U> entity = pageObject.getContent();
        List<V> allUsersDtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

        PagableResponse<V> response = new PagableResponse<>();
        response.setContent(allUsersDtoList);
        response.setPageNumber(pageObject.getNumber());
//        response.setPageNumber(pageObject.getNumber() + 1);
        response.setPageSize(pageObject.getSize());
        response.setTotalElements(pageObject.getTotalElements());
        response.setTotalPages(pageObject.getTotalPages());
        response.setLastPage(pageObject.isLast());

        return response;
    }

    public static String generateSKU(Product product) {
        // Create a simple SKU format
        String categoryCode = product.getCategory() != null ?
                product.getCategory().getCategoryTitle().substring(0, 3).toUpperCase() : "GEN";

        String productNameCode = product.getProductName().substring(0,
                Math.min(product.getProductName().length(), 4)).toUpperCase();

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMdd"));

        // Generate unique SKU
        return String.format("%s-%s-%s-%d",
                categoryCode,
                productNameCode,
                timestamp,
                product.hashCode() % 1000
        );
    }
}
