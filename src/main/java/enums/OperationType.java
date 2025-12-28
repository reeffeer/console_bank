package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType {

    DEPOSIT ("Внести"),
    WITHDRAW ("Снять"),
    TRANSFER ("Перевести");

    private String operationName;

    @Override
    public String toString() {
        return operationName;
    }
}
