package br.com.delivery.test.dto;

import java.time.LocalDate;

public class BillAddDTO extends BillDTO {
    private LocalDate dueDate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
