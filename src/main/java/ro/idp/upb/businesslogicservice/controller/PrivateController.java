package ro.idp.upb.businesslogicservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.idp.upb.businesslogicservice.config.SecurityUtils;
import ro.idp.upb.businesslogicservice.data.entity.User;
import ro.idp.upb.businesslogicservice.utils.UrlBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/private")
@Slf4j
public class PrivateController {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    private String getPrivate() throws AuthenticationException {
        User user = SecurityUtils.getCurrentUser();
        Map<String, Object> params = new HashMap<>();
        params.put("user-first-name", user.getFirstname());
        params.put("user-last-name", user.getLastname());
        params.put("user-email", user.getEmail());
        params.put("user-id", user.getId());
        params.put("user-role", user.getRole());
        return UrlBuilder.replacePlaceholdersInString("Welcome ${user-first-name} ${user-last-name}, ${user-email}, ${user-id}, ${user-role}",
                params);
    }
}
