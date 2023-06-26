package com.example.dentistClinic.service;

import com.example.dentistClinic.domain.AbstractUser;
import com.example.dentistClinic.domain.UserAdmin;
import com.example.dentistClinic.domain.UserDentist;
import com.example.dentistClinic.domain.UserPatient;
import com.example.dentistClinic.email.EmailSender;
import com.example.dentistClinic.emailToken.ConfirmationToken;
import com.example.dentistClinic.emailToken.ConfirmationTokenService;
import com.example.dentistClinic.repository.UserAdminRepository;
import com.example.dentistClinic.repository.UserDentistRepository;
import com.example.dentistClinic.repository.UserPatientRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
@AllArgsConstructor
public class RegistryService {

    private UserPatientRepository userPatientRepository;
    private UserDentistRepository userDentistRepository;
    private UserAdminRepository userAdminRepository;
    private ConfirmationTokenService confirmationTokenService;
    private EmailSender emailSender;
    private final static Logger LOGGER = LoggerFactory.getLogger(RegistryService.class);



    public void saveUser(AbstractUser abstractUser) {
            String token = UUID.randomUUID().toString();
        if (abstractUser instanceof UserPatient) {
            UserPatient user = (UserPatient) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            userPatientRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://localhost:8080/confirmToken/" + token;
            emailSender.send(user.getUsername(), buildEmail(user.getName(), link));
        } else if (abstractUser instanceof UserAdmin) {
            UserAdmin user = (UserAdmin) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            userAdminRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://localhost:8080/confirmToken/" + token;
            emailSender.send(user.getUsername(), buildEmail(user.getName(), link));
        } else if (abstractUser instanceof UserDentist) {
            UserDentist user = (UserDentist) abstractUser;
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            userDentistRepository.save(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            String link = "http://localhost:8080/confirmToken/" + token;
            emailSender.send(user.getUsername(), buildEmail(user.getName(), link));
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }



    @Transactional
    public String confirmToken(String token) {
        LOGGER.info("The token arrived: " + token);
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(() ->
                new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        AbstractUser user = confirmationToken.getUser();
        confirmationTokenService.setConfirmedAt(token);
        if (user instanceof UserAdmin) {
            UserAdmin admin = (UserAdmin) user;
            userAdminRepository.enableUser(admin.getUsername());
        } else if (user instanceof UserPatient) {
            UserPatient patient = (UserPatient) user;
            userPatientRepository.enableUser(patient.getUsername());
        } else if (user instanceof UserDentist) {
            UserDentist dentist = (UserDentist) user;
            userDentistRepository.enableUser(dentist.getUsername());
        }

        String html = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <title>Email Confirmation</title>\n"
                + "    <style>\n"
                + "        body {\n"
                + "            font-family: Arial, sans-serif;\n"
                + "            background-color: #03a9f4;\n"
                + "            display: flex;\n"
                + "            justify-content: center;\n"
                + "            align-items: center;\n"
                + "            height: 100vh;\n"
                + "            flex-direction: column;\n"
                + "        }\n"
                + "        h1 {\n"
                + "            color: #383838;\n"
                + "            background-color: white;\n"
                + "            width: 50vw;\n"
                + "            height: 5vw;\n"
                + "            display: flex;\n"
                + "            flex-direction: column;\n"
                + "            justify-content: center;\n"
                + "            align-items: center;\n"
                + "            border-radius: 1vw;\n"
                + "        }\n"
                + "        p {\n"
                + "            color: #383838;\n"
                + "            background-color: white;\n"
                + "            width: 35vw;\n"
                + "            height: 3vw;\n"
                + "            display: flex;\n"
                + "            flex-direction: column;\n"
                + "            justify-content: center;\n"
                + "            align-items: center;\n"
                + "            border-radius: 0.5vw;\n"
                + "        }\n"                + "       "
                + " img {\n"
                + "            width: 20vw;\n"
                + "        }\n"
                + "        /* Otros estilos CSS */\n"
                + "        /* ... */\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <h1>Your account is confirmed!</h1>\n"
                + "    <p>Thank you for confirming your email.</p>\n"
                + "    <p>Your account is now active, you can login now! <a href=\"http://127.0.0.1:5173/login\">Click here to login</a></p>\n"
                + "    <img src=\"http://localhost:8000/images/image.svg\">\n"
                + "</body>\n"
                + "</html>";

        return html;
    }


    public String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public int enableUser(String email) {
        return userAdminRepository.enableUser(email);
    }
}
