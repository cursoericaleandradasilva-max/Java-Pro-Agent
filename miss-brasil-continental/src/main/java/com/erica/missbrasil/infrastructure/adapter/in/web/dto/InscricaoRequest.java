package com.erica.missbrasil.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record InscricaoRequest(
    @NotBlank(message = "O nome completo é obrigatório")
    @Size(min = 3, max = 150, message = "O nome completo deve ter entre 3 e 150 caracteres")
    String nomeCompleto,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "O WhatsApp é obrigatório")
    @Size(min = 9, max = 25, message = "WhatsApp deve ter entre 9 e 25 caracteres")
    String whatsapp,

    @NotBlank(message = "O logradouro é obrigatório")
    String logradouro,

    String numero,
    String bairro,

    @NotBlank(message = "A cidade é obrigatória")
    String cidade,

    @NotBlank(message = "O estado (UF) é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 letras (Ex: SP, RJ, MG)")
    String estado,

    String cep,

    @NotNull(message = "A idade é obrigatória")
    @Min(value = 16, message = "A idade mínima é de 16 anos")
    @Max(value = 40, message = "A idade máxima é de 40 anos")
    Integer idade,

    @NotNull(message = "A altura é obrigatória")
    @DecimalMin(value = "1.40", message = "A altura mínima é 1.40m")
    @DecimalMax(value = "2.20", message = "A altura máxima é 2.20m")
    Double alturaMetros,

    @NotNull(message = "O peso é obrigatório")
    @DecimalMin(value = "35.0", message = "O peso mínimo é 35kg")
    @DecimalMax(value = "150.0", message = "O peso máximo é 150kg")
    Double pesoKg,

    @NotEmpty(message = "Ao menos uma foto de rosto ou corpo inteiro deve ser enviada")
    List<String> fotos
) {}
