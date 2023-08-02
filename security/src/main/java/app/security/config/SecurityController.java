package app.security.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;

    @GetMapping("/getUsername")
    public String getUsername(@RequestHeader(name="Authorization") String token){
        log.trace("getUsername method was called with token-{}",token);
        String result = jwtUtil.extractUsername(token);
        log.trace("getUsername method's with token-{} result is {}",token,result);
        return result;
    }
}
