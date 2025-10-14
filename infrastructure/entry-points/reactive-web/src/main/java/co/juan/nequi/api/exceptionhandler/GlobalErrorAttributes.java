package co.juan.nequi.api.exceptionhandler;

import co.juan.nequi.model.exceptions.BranchNotFoundException;
import co.juan.nequi.model.exceptions.FranchiseNotFoundException;
import co.juan.nequi.model.exceptions.ProductNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(request);

        int status = determineHttpStatus(error);
        errorAttributes.put("status", status);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
        errorAttributes.put("path", request.path());
        return errorAttributes;
    }

    private int determineHttpStatus(Throwable error) {
        if (error instanceof IllegalArgumentException || error instanceof ValidationException
                || error instanceof ServerWebInputException) {
            return HttpStatus.BAD_REQUEST.value();
        } else if (error instanceof FranchiseNotFoundException || error instanceof BranchNotFoundException
                || error instanceof ProductNotFoundException) {
            return HttpStatus.NOT_FOUND.value();
        } else if (error instanceof IllegalStateException) {
            return HttpStatus.CONFLICT.value();
        } else if (error instanceof RuntimeException) {
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
