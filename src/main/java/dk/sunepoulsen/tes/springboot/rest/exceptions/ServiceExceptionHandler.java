package dk.sunepoulsen.tes.springboot.rest.exceptions;

import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
    private static Logger log = LoggerFactory.getLogger( ServiceExceptionHandler.class );

    public ServiceExceptionHandler() {
    }

    @Hidden
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ExceptionHandler( ApiBadRequestException.class )
    @ResponseBody
    public ServiceErrorModel handleBadRequest(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    @ExceptionHandler( ApiUnauthorizedException.class )
    @ResponseBody
    public ServiceErrorModel handleUnauthorized(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.FORBIDDEN )
    @ExceptionHandler( ApiForbiddenException.class )
    @ResponseBody
    public ServiceErrorModel handleForbidden(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.NOT_FOUND )
    @ExceptionHandler( ApiNotFoundException.class )
    @ResponseBody
    public ServiceErrorModel handleNotFound(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.CONFLICT )
    @ExceptionHandler( ApiConflictException.class )
    @ResponseBody
    public ServiceErrorModel handleConflict(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.NOT_IMPLEMENTED )
    @ExceptionHandler( UnsupportedOperationException.class )
    @ResponseBody
    public ServiceErrorModel handleUnsupportedOperation(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @Hidden
    @ResponseStatus( HttpStatus.BAD_GATEWAY )
    @ExceptionHandler( ApiBadGatewayException.class )
    @ResponseBody
    public ServiceErrorModel handleBadGateway(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    @ExceptionHandler( ApiInternalServerException.class )
    @ResponseBody
    public ServiceErrorModel handleInternalServerError(HttpServletRequest request, Exception ex ) {
        return handleCheckedException( ex );
    }

    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    @ExceptionHandler( RuntimeException.class )
    @ResponseBody
    public ServiceErrorModel handleRuntimeException(HttpServletRequest request, Exception ex ) {
        ServiceErrorModel body = null;
        try {
            body = new ServiceErrorModel();
            body.setMessage( "Unable to process request" );

            return body;
        }
        catch( Exception e ) {
            log.info( e.getMessage(), e );
            return null;
        }
        finally {
            log.info( ex.getMessage(), ex );
            logResponseBody( body );
        }
    }

    private ServiceErrorModel handleCheckedException( Exception ex ) {
        ServiceErrorModel body = null;
        try {
            return body = extractErrorBody( ex );
        }
        catch( Exception e ) {
            log.info( e.getMessage(), e );
            return null;
        }
        finally {
            log.info( ex.getMessage() );
            log.debug( "Exception", ex );
            logResponseBody( body );
        }
    }

    private ServiceErrorModel extractErrorBody( Exception ex ) {
        if( ex instanceof ApiException) {
            return ((ApiException)ex).getServiceError();
        }

        ServiceErrorModel serviceError = new ServiceErrorModel();
        serviceError.setMessage(ex.getMessage());

        return serviceError;
    }

    private static void logResponseBody( ServiceErrorModel body ) {
        log.info( "Returned body: {}", body.toString() );
    }
}
