package org.example.bookstoreapp.dto;

import java.math.BigDecimal;

public record PriceParams(BigDecimal minPrice, BigDecimal maxPrice) {
}
