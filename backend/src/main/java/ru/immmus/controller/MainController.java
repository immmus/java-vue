package ru.immmus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.immmus.domain.User;
import ru.immmus.domain.Views;
import ru.immmus.dto.MessagePageDto;
import ru.immmus.service.MessageService;
import ru.immmus.service.UserService;
import ru.immmus.util.UserUtil;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
    private final MessageService messageService;
    private final UserService userService;
    @Value("${spring.profiles.active:prod}")
    private String profile;
    private final ObjectWriter messageWriter;
    private final ObjectWriter profileWriter;

    @Autowired
    public MainController(MessageService messageService, UserService userService, ObjectMapper mapper) {
        final ObjectMapper config = mapper.setConfig(mapper.getSerializationConfig());
        this.messageService = messageService;
        this.userService = userService;

        this.messageWriter = config.writerWithView(Views.FullMessage.class);
        this.profileWriter = config.writerWithView(Views.FullProfile.class);
    }

    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal OAuth2User oAuth2User
    ) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        User user = userService.getAuthUser(oAuth2User);
        if (!UserUtil.isBadUser(user)) {
            // Записываем пользователя как json для получения его на frontend'e
            final String serializedProfile = profileWriter.writeValueAsString(user);
            model.addAttribute("profile", serializedProfile);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
            MessagePageDto messagePageDto = messageService.findAllVisibleMessagesForUser(pageRequest, user);

            final String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());
            model.addAttribute("messages", messages);

            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
            model.addAttribute("profile", "null");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }


}
