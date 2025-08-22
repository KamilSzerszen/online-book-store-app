package org.example.bookstoreapp.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.bookstoreapp.model.Status;

@Data
public class UpdateOrderStatusDto {

    @NotNull
    private Status status;
}
