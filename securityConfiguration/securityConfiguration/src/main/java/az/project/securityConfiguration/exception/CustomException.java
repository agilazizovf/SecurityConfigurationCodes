package az.project.securityConfiguration.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String internalMessage;
    private Integer status;
    private String type;
    private BindingResult result;

    public CustomException(String message, String internalMessage, String type, Integer status, BindingResult result) {
        super(message);
        this.internalMessage = internalMessage;
        this.type = type;
        this.status = status;
        this.result = result;

    }
}
