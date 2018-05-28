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

import com.codenjoy.dojo.services.dao.HackathonRegistration;
import com.codenjoy.dojo.services.mail.MailService;
import com.codenjoy.dojo.web.restParamClasses.HackathonRegistrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@Controller
@RequestMapping("/hackathonRegistration")
public class HackathonRegistrationController {

    @Autowired
    private HackathonRegistration hackathonRegistration;

    @Autowired
    private MailService mailService;

    public HackathonRegistrationController() {
    }

    @RequestMapping(params = "isRegistered", method = RequestMethod.GET)
    @ResponseBody
    public String isRegistered(@RequestParam("isRegistered") String contact_email) {
        return "{ \"registered\":" + hackathonRegistration.isRegistered(contact_email) + "}";
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<HackathonRegistrant> all() {
        return hackathonRegistration.getHackathonRegistrants();
    }

    @RequestMapping(params = "regDetailsFor", method = RequestMethod.GET)
    @ResponseBody
    public HackathonRegistrant regDetailsFor(@RequestParam("regDetailsFor") String email) {
        HackathonRegistrant hackathonRegistrant = hackathonRegistration.getRegistrant(email);
        return hackathonRegistrant;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String submitRegistration(HackathonRegistrant req) {
        try {
            HackathonRegistrant registrant = new HackathonRegistrant(
                    req.getContactEmail(),
                    req.getContactName(),
                    req.getContactPhone(),
                    req.getTeamName(),
                    req.getTeamDescription()
            );
            try {
                mailService.sendEmail("Hackathon registration",
                        "Somebody just registered for the hackathon with the following details:" +
                                "<br/>Contact email: " + registrant.getContactEmail() +
                                "<br/>Contact name: " + registrant.getContactName() +
                                "<br/>Contact phone: " + registrant.getContactPhone() +
                                "<br/>Team name: " + registrant.getTeamName() +
                                "<br/>Team description: " + registrant.getTeamDescription() +
                                "<br/><br/>EPAM Systems Team"
                );
            } catch (MessagingException e) {

            }

            //if (hackathonRegistration.isRegistered(req.getContactEmail())) TODO clarify what we do if we already have that registration
            hackathonRegistration.registerForHackathon(registrant);
            return "{\"created\":\"true\"}";
            //return raw;
        } catch (Exception e) {
            //return raw;
            return "{\"created\":\"false\", \"exception\":" + e.getMessage() + "}";
        }
    }

}
