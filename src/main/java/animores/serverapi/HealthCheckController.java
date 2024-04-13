package animores.serverapi;


import animores.serverapi.common.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public Response<String> healthCheck() {
        return Response.success("OK");
    }
}
