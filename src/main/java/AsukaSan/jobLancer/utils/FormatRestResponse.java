package AsukaSan.jobLancer.utils;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import AsukaSan.jobLancer.domain.RestResponse;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
       
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);
        // nếu body trả về không phải object thì return luôn
        if(body instanceof String){
            return body;
        }
        if(status >= 400){
            return body;
        }else{
            res.setMessage("API HAS SUCCESSFULLY CALLED");
            res.setData(body);
        }
        return res;
    }
    
}
