package org.example.bookstoreapp.dto.book;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String[] title, String[] author, String[] isbn, BigDecimal minPrice, BigDecimal maxPrice) {

}
