package com.erica.missbrasil.application.port.in;

import java.util.List;

public record InscreverCandidataCommand(
    String nomeCompleto,
    String email,
    String whatsapp,
    String logradouro,
    String numero,
    String bairro,
    String cidade,
    String estado,
    String cep,
    int idade,
    double alturaMetros,
    double pesoKg,
    List<String> fotos
) {}
