package com.lcwd.electronic.store.ElectronicStore.helper;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.payload.PagableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

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
}
