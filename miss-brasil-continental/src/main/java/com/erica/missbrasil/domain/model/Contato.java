package com.erica.missbrasil.domain.model;

import com.erica.missbrasil.domain.exception.RegraNegocioException;
import java.util.Objects;
import java.util.regex.Pattern;

public record Contato(
    String email,
    String whatsapp
) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern WHATSAPP_PATTERN = Pattern.compile("^\\+?[0-9\\s()\\-]{8,20}$");

    public Contato {
        Objects.requireNonNull(email, "E-mail é obrigatório");
        Objects.requireNonNull(whatsapp, "WhatsApp é obrigatório");

        String emailTrim = email.trim();
        String whatsappTrim = whatsapp.trim();

        if (emailTrim.isBlank() || !EMAIL_PATTERN.matcher(emailTrim).matches()) {
            throw new RegraNegocioException("E-mail informado é inválido");
        }
        if (whatsappTrim.isBlank() || !WHATSAPP_PATTERN.matcher(whatsappTrim).matches()) {
            throw new RegraNegocioException("WhatsApp informado é inválido");
        }

        email = emailTrim.toLowerCase();
        whatsapp = whatsappTrim;
    }
}
