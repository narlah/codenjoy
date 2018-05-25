package com.codenjoy.dojo.web.controller;
/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.dao.CodeForReward;
import com.codenjoy.dojo.services.dao.CodeReward;
import com.codenjoy.dojo.services.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/codeWinners")
public class CodeWinnersController {

    @Autowired
    private CodeForReward codeForReward;

    @Autowired
    private MailService mailService;


    @RequestMapping(params = "hasCode", method = RequestMethod.GET)
    public @ResponseBody
    String isRegistered(HttpServletRequest _request, @RequestParam("hasCode") String contact_email) {
        String hasCode = codeForReward.getCode(contact_email);
        if (!hasCode.equals("false")) {
            return "{\"alreadyHasCode\":" + "\"true\", " +
                    "\"code\":" + "\"" + hasCode + "\" " +
                    "}";
        } else {
            String newCode = codeForReward.insertCode(contact_email);
            sendEmailForCode(contact_email, newCode);
            return "{\"alreadyHasCode\":" + "\"false\", " +
                    "\"code\":" + "\"" + newCode + "\" " +
                    "}";

        }
    }

    @RequestMapping(params = "resendEmail", method = RequestMethod.GET)
    public @ResponseBody
    String resendEmail(HttpServletRequest _request, @RequestParam("resendEmail") String contact_email) {
        String code = codeForReward.getCode(contact_email);
        if (!code.equals("true")) {
            sendEmailForCode(contact_email, code);
            return "{\"mailSent\":" + "\"true\", " +
                    "\"code\":" + "\"" + code + "\" " +
                    "}";

        }
        return "{\"mailSent\":" + "\"false\", " +
                "\"reason\":" + "\"no_code\" " +
                "}";
    }

    private void sendEmailForCode(String contact_email, String code) {
        try {
            //String email = "Maria_Zharova@epam.com";
            String email = "mitrandir@gmail.com";
            mailService.sendEmail(email, "Codenjoy winner",
                    "You got rewarded \n Pass by our boot to receive your reward : \n Email " +
                            "\n email: " + contact_email +
                            "\n Your code" + code +
                            "\n Best regards Team Codenjoy<>"
            );
        } catch (MessagingException ignored) {
        }
    }


    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<CodeReward> all() {
        return codeForReward.getAll();
    }
}
