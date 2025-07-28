package site.ahzx.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import site.ahzx.config.FeignConfig;
import util.R;

import java.util.Map;

@FeignClient(name = "dit-system-service", contextId = "remoteUserService", path = "/internal",configuration = FeignConfig.class )
public interface RemoteUserFeign {
    @GetMapping("/getUserInfo")
    R<?> getUserInfo(@RequestParam("username") String username);
}
