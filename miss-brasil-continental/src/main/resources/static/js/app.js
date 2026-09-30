// JavaScript de Inscrição Oficial - Miss Brasil Continental
let fotosBase64 = [];

document.addEventListener('DOMContentLoaded', () => {
    configurarUploadFotos();
    configurarFormInscricao();
    configurarConsultaProtocolo();
});

// Manipulação e conversão de fotos para Base64 seguro
function configurarUploadFotos() {
    const fotoInput = document.getElementById('fotoInput');
    const previewContainer = document.getElementById('previewContainer');

    fotoInput.addEventListener('change', async (e) => {
        const files = Array.from(e.target.files);
        if (fotosBase64.length + files.length > 3) {
            alert('Você pode enviar no máximo 3 fotos (Rosto e Corpo).');
            return;
        }

        for (const file of files) {
            if (!file.type.startsWith('image/')) {
                alert('Apenas arquivos de imagem são aceitos.');
                continue;
            }

            if (file.size > 5 * 1024 * 1024) {
                alert('A imagem não pode ultrapassar 5MB.');
                continue;
            }

            const base64 = await converterArquivoParaBase64(file);
            fotosBase64.push(base64);
        }

        renderizarPreviews();
        fotoInput.value = '';
    });
}

function converterArquivoParaBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
        reader.readAsDataURL(file);
    });
}

function renderizarPreviews() {
    const previewContainer = document.getElementById('previewContainer');
    previewContainer.innerHTML = '';

    fotosBase64.forEach((foto, index) => {
        const item = document.createElement('div');
        item.className = 'preview-item';
        item.innerHTML = `
            <img src="${foto}" alt="Foto ${index + 1}">
            <button type="button" class="btn-remove-photo" onclick="removerFoto(${index})">✕</button>
        `;
        previewContainer.appendChild(item);
    });
}

function removerFoto(index) {
    fotosBase64.splice(index, 1);
    renderizarPreviews();
}

// Envio do formulário de inscrição
function configurarFormInscricao() {
    const form = document.getElementById('formInscricao');
    const alertBox = document.getElementById('formAlert');
    const btnEnviar = document.getElementById('btnEnviar');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        if (fotosBase64.length === 0) {
            mostrarAlerta(alertBox, 'error', 'Por favor, anexe ao menos uma foto (rosto ou corpo inteiro).');
            return;
        }

        const payload = {
            nomeCompleto: document.getElementById('nomeCompleto').value.trim(),
            email: document.getElementById('email').value.trim(),
            whatsapp: document.getElementById('whatsapp').value.trim(),
            logradouro: document.getElementById('logradouro').value.trim(),
            numero: document.getElementById('numero').value.trim() || 'S/N',
            bairro: document.getElementById('bairro').value.trim() || 'Centro',
            cidade: document.getElementById('cidade').value.trim(),
            estado: document.getElementById('estado').value.trim().toUpperCase(),
            cep: document.getElementById('cep').value.trim() || '00000-000',
            idade: parseInt(document.getElementById('idade').value, 10),
            alturaMetros: parseFloat(document.getElementById('alturaMetros').value),
            pesoKg: parseFloat(document.getElementById('pesoKg').value),
            fotos: fotosBase64
        };

        btnEnviar.disabled = true;
        btnEnviar.querySelector('.btn-text').textContent = 'Enviando inscrição com segurança...';
        alertBox.style.display = 'none';

        try {
            const response = await fetch('/api/v1/candidatas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const erroData = await response.json().catch(() => null);
                let mensagemErro = 'Falha ao processar inscrição. Verifique os dados preenchidos.';
                if (erroData && erroData.detail) {
                    mensagemErro = erroData.detail;
                } else if (erroData && erroData.erros) {
                    mensagemErro = Object.values(erroData.erros).join(' | ');
                }
                throw new Error(mensagemErro);
            }

            const data = await response.json();
            form.reset();
            fotosBase64 = [];
            renderizarPreviews();

            // Abre modal com protocolo gerado
            document.getElementById('modalProtocolo').textContent = data.protocolo;
            document.getElementById('modalSucesso').style.display = 'flex';

        } catch (error) {
            mostrarAlerta(alertBox, 'error', error.message);
        } finally {
            btnEnviar.disabled = false;
            btnEnviar.querySelector('.btn-text').textContent = 'Confirmar e Enviar Inscrição';
        }
    });
}

// Consulta de Protocolo
function configurarConsultaProtocolo() {
    const btnConsultar = document.getElementById('btnConsultarProtocolo');
    const inputProtocolo = document.getElementById('inputProtocolo');
    const resultadoBox = document.getElementById('resultadoProtocolo');

    btnConsultar.addEventListener('click', async () => {
        const protocolo = inputProtocolo.value.trim().toUpperCase();
        if (!protocolo) {
            alert('Por favor, informe o número de protocolo.');
            return;
        }

        resultadoBox.style.display = 'block';
        resultadoBox.innerHTML = '<p>Consultando banco de dados oficial...</p>';

        try {
            const response = await fetch(`/api/v1/candidatas/protocolo/${encodeURIComponent(protocolo)}`);
            if (response.status === 404) {
                resultadoBox.innerHTML = `
                    <p style="color: var(--danger);">❌ Protocolo <strong>${protocolo}</strong> não encontrado. Verifique se digitou corretamente.</p>
                `;
                return;
            }

            const data = await response.json();
            let statusBadge = '';
            if (data.status === 'APROVADA') {
                statusBadge = '<span class="badge-status APROVADA" style="padding: 6px 16px; font-size: 0.9rem;">✅ APROVADA NA SELETIVA</span>';
            } else if (data.status === 'REPROVADA') {
                statusBadge = '<span class="badge-status REPROVADA" style="padding: 6px 16px; font-size: 0.9rem;">❌ NÃO SELECIONADA</span>';
            } else {
                statusBadge = '<span class="badge-status EM_ANALISE" style="padding: 6px 16px; font-size: 0.9rem;">⏳ EM ANÁLISE PELA COMISSÃO</span>';
            }

            resultadoBox.innerHTML = `
                <div style="text-align: left; padding: 10px;">
                    <h4 style="color: var(--gold-light); margin-bottom: 5px;">Candidata: ${data.nome}</h4>
                    <p style="color: var(--text-muted); font-size: 0.85rem; margin-bottom: 15px;">Protocolo: <code>${data.protocolo}</code></p>
                    <div style="margin-bottom: 15px;">Status Atual: ${statusBadge}</div>
                    <p style="font-size: 0.9rem; color: var(--text-light);">${data.mensagem}</p>
                </div>
            `;
        } catch (error) {
            resultadoBox.innerHTML = `<p style="color: var(--danger);">Erro de conexão ao consultar protocolo.</p>`;
        }
    });
}

function mostrarAlerta(elemento, tipo, mensagem) {
    elemento.className = `alert-box ${tipo}`;
    elemento.textContent = mensagem;
    elemento.style.display = 'block';
}
