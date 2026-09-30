// JavaScript do Painel Administrativo Oficial - Miss Brasil Continental
const ADMIN_AUTH = 'Basic ' + btoa('admin:miss2026admin');
let candidatasLista = [];
let candidataSelecionadaId = null;

document.addEventListener('DOMContentLoaded', () => {
    carregarTudo();
});

function carregarTudo() {
    carregarMetricas();
    carregarCandidatas();
}

async function carregarMetricas() {
    try {
        const response = await fetch('/api/v1/admin/candidatas/metricas', {
            headers: { 'Authorization': ADMIN_AUTH }
        });
        if (response.ok) {
            const data = await response.json();
            document.getElementById('kpiTotal').textContent = data.total;
            document.getElementById('kpiEmAnalise').textContent = data.emAnalise;
            document.getElementById('kpiAprovadas').textContent = data.aprovadas;
            document.getElementById('kpiReprovadas').textContent = data.reprovadas;
        }
    } catch (error) {
        console.error('Erro ao buscar métricas:', error);
    }
}

async function carregarCandidatas() {
    const statusFiltro = document.getElementById('selectStatusFiltro').value;
    const busca = document.getElementById('inputBusca').value.trim();
    const tbody = document.getElementById('tbodyCandidatas');

    tbody.innerHTML = '<tr><td colspan="8" class="loading-td">Carregando inscrições do banco de dados seguro...</td></tr>';

    try {
        let url = '/api/v1/admin/candidatas?';
        if (statusFiltro) url += `status=${statusFiltro}&`;
        if (busca) url += `busca=${encodeURIComponent(busca)}`;

        const response = await fetch(url, {
            headers: { 'Authorization': ADMIN_AUTH }
        });

        if (!response.ok) {
            tbody.innerHTML = '<tr><td colspan="8" class="loading-td" style="color: var(--danger);">Erro de autenticação ou permissão negada.</td></tr>';
            return;
        }

        candidatasLista = await response.json();
        renderizarTabela(candidatasLista);

    } catch (error) {
        console.error('Erro ao listar candidatas:', error);
        tbody.innerHTML = '<tr><td colspan="8" class="loading-td" style="color: var(--danger);">Falha de conexão com o servidor.</td></tr>';
    }
}

function renderizarTabela(candidatas) {
    const tbody = document.getElementById('tbodyCandidatas');
    tbody.innerHTML = '';

    if (candidatas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="loading-td">Nenhuma candidata encontrada com os filtros selecionados.</td></tr>';
        return;
    }

    candidatas.forEach(c => {
        const tr = document.createElement('tr');
        const fotoPrincipal = (c.fotos && c.fotos.length > 0) ? c.fotos[0] : 'data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="%23d4af37" viewBox="0 0 24 24"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>';

        let statusClass = 'EM_ANALISE';
        let statusLabel = 'Em Análise';
        if (c.status === 'APROVADA') {
            statusClass = 'APROVADA';
            statusLabel = 'Aprovada';
        } else if (c.status === 'REPROVADA') {
            statusClass = 'REPROVADA';
            statusLabel = 'Reprovada';
        }

        tr.innerHTML = `
            <td><img src="${fotoPrincipal}" alt="${c.nomeCompleto}" class="candidata-avatar"></td>
            <td><span class="candidata-protocolo">${c.protocolo}</span></td>
            <td>
                <span class="candidata-nome">${c.nomeCompleto}</span>
                <small style="color: var(--text-muted);">${c.idade} anos</small>
            </td>
            <td>
                <div>📧 ${c.email}</div>
                <div style="color: #34d399;">💬 ${c.whatsapp}</div>
            </td>
            <td>${c.cidade}/${c.estado}</td>
            <td>${c.alturaMetros}m / ${c.pesoKg}kg</td>
            <td><span class="badge-status ${statusClass}">${statusLabel}</span></td>
            <td>
                <button class="btn-view" onclick="abrirDossie('${c.id}')">Ver Dossiê</button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function filtrarStatus(status) {
    document.getElementById('selectStatusFiltro').value = status;
    
    // Atualiza classe ativa nos cards
    document.querySelectorAll('.kpi-card').forEach(card => {
        if (card.getAttribute('data-filter') === status) {
            card.classList.add('active');
        } else {
            card.classList.remove('active');
        }
    });

    carregarCandidatas();
}

function abrirDossie(id) {
    const candidata = candidatasLista.find(c => c.id === id);
    if (!candidata) return;

    candidataSelecionadaId = id;
    document.getElementById('dossieNome').textContent = candidata.nomeCompleto;
    document.getElementById('dossieProtocolo').textContent = `Protocolo: ${candidata.protocolo}`;
    document.getElementById('dossieEmail').textContent = candidata.email;
    document.getElementById('dossieWhatsapp').textContent = candidata.whatsapp;
    document.getElementById('dossieIdade').textContent = `${candidata.idade} anos`;
    document.getElementById('dossieMedidas').textContent = `Altura: ${candidata.alturaMetros}m | Peso: ${candidata.pesoKg}kg`;
    document.getElementById('dossieEndereco').textContent = `${candidata.logradouro}, ${candidata.numero} - ${candidata.bairro}, ${candidata.cidade}/${candidata.estado} (CEP: ${candidata.cep})`;
    document.getElementById('dossieData').textContent = new Date(candidata.cadastradaEm).toLocaleString('pt-BR');
    document.getElementById('parecerTexto').value = candidata.parecerAvaliador || '';

    // Galeria
    const galeria = document.getElementById('dossieGaleria');
    galeria.innerHTML = '';
    if (candidata.fotos && candidata.fotos.length > 0) {
        candidata.fotos.forEach(foto => {
            const img = document.createElement('img');
            img.src = foto;
            galeria.appendChild(img);
        });
    } else {
        galeria.innerHTML = '<p style="color: var(--text-muted);">Nenhuma foto cadastrada.</p>';
    }

    document.getElementById('modalDossie').style.display = 'flex';
}

function fecharModalDossie() {
    document.getElementById('modalDossie').style.display = 'none';
    candidataSelecionadaId = null;
}

async function atualizarStatusCandidata(novoStatus) {
    if (!candidataSelecionadaId) return;

    const parecer = document.getElementById('parecerTexto').value.trim();

    try {
        const response = await fetch(`/api/v1/admin/candidatas/${candidataSelecionadaId}/status`, {
            method: 'PATCH',
            headers: {
                'Authorization': ADMIN_AUTH,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                status: novoStatus,
                parecer: parecer
            })
        });

        if (!response.ok) {
            alert('Falha ao atualizar status da candidata.');
            return;
        }

        fecharModalDossie();
        carregarTudo();
        alert(`Status da candidata atualizado para ${novoStatus} com sucesso!`);

    } catch (error) {
        console.error('Erro ao atualizar status:', error);
        alert('Erro ao comunicar com o servidor.');
    }
}
