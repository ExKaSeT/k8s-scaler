package ru.sberbank.k8sscaler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessContainerDto {
    public static SuccessContainerDto success() {
        return new SuccessContainerDto(true, "Успешное выполнение запроса");
    }

    private boolean success;
    private String message;
}
