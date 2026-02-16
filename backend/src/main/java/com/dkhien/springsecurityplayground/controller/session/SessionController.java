package com.dkhien.springsecurityplayground.controller.session;

import com.dkhien.springsecurityplayground.api.session.SessionApi;
import com.dkhien.springsecurityplayground.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequiredArgsConstructor
public class SessionController implements SessionApi {
    private final AppUserService appUserService;

    @Override
    public ResponseEntity<String> sessionMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();
        var appUser = appUserService.findByUsername(name);
        return ResponseEntity.ok("(Session) Hello, " + appUser.getName() + "\nSession ID: " + sessionId);
    }

    @Override
    public ResponseEntity<String> sessionPublic() {
        return ResponseEntity.ok("This is a public endpoint. (Session)");
    }

    @Override
    public ResponseEntity<String> sessionAdmin() {
        return ResponseEntity.ok("This is an admin endpoint. (Session)");
    }
}
