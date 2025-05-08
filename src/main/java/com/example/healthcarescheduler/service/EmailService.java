package com.example.healthcarescheduler.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.healthcarescheduler.model.Appointment;
import com.example.healthcarescheduler.model.Doctor;
import com.example.healthcarescheduler.model.Patient;
import com.example.healthcarescheduler.model.User;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
            logger.info("Simple email sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send simple email to: {}", to, e);
        }
    }

    @Async
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
            logger.info("HTML email sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to: {}", to, e);
        }
    }

    @Async
    public void sendAppointmentConfirmationToPatient(Appointment appointment, Patient patient, Doctor doctor) {
        try {
            String subject = "Appointment Confirmation - Healthcare Scheduler";

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("patientName", patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
            variables.put("doctorName", "Dr. " + doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName());
            variables.put("appointmentDateTime",
                    appointment.getStartTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
            variables.put("appointmentType", appointment.getAppointmentType().toString());
            variables.put("appointmentId", appointment.getId());

            context.setVariables(variables);
            String htmlContent = templateEngine.process("email/appointment-confirmation", context);

            sendHtmlMessage(patient.getUser().getEmail(), subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send appointment confirmation email to patient", e);
        }
    }

    @Async
    public void sendAppointmentNotificationToDoctor(Appointment appointment, Patient patient, Doctor doctor) {
        try {
            String subject = "New Appointment Scheduled - Healthcare Scheduler";

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("doctorName", "Dr. " + doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName());
            variables.put("patientName", patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
            variables.put("appointmentDateTime",
                    appointment.getStartTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
            variables.put("appointmentType", appointment.getAppointmentType().toString());
            variables.put("appointmentId", appointment.getId());

            context.setVariables(variables);
            String htmlContent = templateEngine.process("email/appointment-notification", context);

            sendHtmlMessage(doctor.getUser().getEmail(), subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send appointment notification email to doctor", e);
        }
    }

    @Async
    public void sendAppointmentReminder(Appointment appointment, Patient patient, Doctor doctor) {
        try {
            String subject = "Appointment Reminder - Healthcare Scheduler";

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("patientName", patient.getUser().getFirstName() + " " + patient.getUser().getLastName());
            variables.put("doctorName", "Dr. " + doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName());
            variables.put("appointmentDateTime",
                    appointment.getStartTime().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a")));
            variables.put("appointmentType", appointment.getAppointmentType().toString());

            context.setVariables(variables);
            String htmlContent = templateEngine.process("email/appointment-reminder", context);

            sendHtmlMessage(patient.getUser().getEmail(), subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send appointment reminder email", e);
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            String subject = "Password Reset Request - Healthcare Scheduler";

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", user.getFirstName() + " " + user.getLastName());
            variables.put("resetLink", "http://localhost:8080/healthcare/reset-password?token=" + resetToken);

            context.setVariables(variables);
            String htmlContent = templateEngine.process("email/password-reset", context);

            sendHtmlMessage(user.getEmail(), subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send password reset email", e);
        }
    }

    @Async
    public void sendVerificationEmail(User user, String verificationToken) {
        try {
            String subject = "Email Verification - Healthcare Scheduler";

            Context context = new Context();
            Map<String, Object> variables = new HashMap<>();
            variables.put("userName", user.getFirstName() + " " + user.getLastName());
            variables.put("verificationLink", "http://localhost:8080/healthcare/api/auth/verify/" + verificationToken);

            context.setVariables(variables);
            String htmlContent = templateEngine.process("email/email-verification", context);

            sendHtmlMessage(user.getEmail(), subject, htmlContent);
        } catch (Exception e) {
            logger.error("Failed to send verification email", e);
        }
    }
}