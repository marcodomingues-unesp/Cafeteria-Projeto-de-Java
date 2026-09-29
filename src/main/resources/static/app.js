
const API_URL = "/api/cafes";

// Elementos HTML
const cafeForm = document.getElementById("cafeForm");
const cafeId = document.getElementById("cafeId");
const nomeProduto = document.getElementById("nomeProduto");
const tamanhoProduto = document.getElementById("tamanhoProduto");
const tipoTorra = document.getElementById("tipoTorra");
const preco = document.getElementById("preco");

const tabelaCafes = document.getElementById("tabelaCafes");
const totalCafes = document.getElementById("totalCafes");
const mensagem = document.getElementById("mensagem");

const btnSalvar = document.getElementById("btnSalvar");
const btnLimpar = document.getElementById("btnLimpar");
const btnAtualizar = document.getElementById("btnAtualizar");
const btnBuscar = document.getElementById("btnBuscar");
const btnListar = document.getElementById("btnListar");

const buscarId = document.getElementById("buscarId");

let cafes = [];

// ==================== MENSAGENS ====================

function mostrarMensagem(texto, tipo = "success") {
    mensagem.textContent = texto;
    mensagem.className = `message ${tipo}`;
}

// ==================== REQUISIÇÃO HTTP ====================

async function requisicao(url, metodo = "GET", dados = null) {

    const configuracao = {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        }
    };

    if (dados !== null) {
        configuracao.body = JSON.stringify(dados);
    }

    const resposta = await fetch(url, configuracao);

    if (!resposta.ok) {
        let erro = `Erro HTTP ${resposta.status}`;

        try {
            const corpo = await resposta.json();
            erro = corpo.erro || corpo.message || erro;
        } catch {
            // A resposta pode não possuir conteúdo JSON.
        }

        throw new Error(erro);
    }

    if (resposta.status === 204) {
        return null;
    }

    return resposta.json();
}

// ==================== LISTAR CAFÉS - GET ====================

async function listarCafes() {

    tabelaCafes.innerHTML = `
        <tr>
            <td colspan="6" class="empty">Carregando...</td>
        </tr>
    `;

    try {

        cafes = await requisicao(API_URL);

        exibirCafes(cafes);

        mostrarMensagem("Lista de cafés atualizada.");

    } catch (erro) {

        tabelaCafes.innerHTML = `
            <tr>
                <td colspan="6" class="empty">
                    Não foi possível carregar os cafés.
                </td>
            </tr>
        `;

        totalCafes.textContent = "0";

        mostrarMensagem(erro.message, "error");
    }
}

// ==================== EXIBIR CAFÉS ====================

function exibirCafes(lista) {

    totalCafes.textContent = lista.length;

    tabelaCafes.innerHTML = "";

    if (lista.length === 0) {

        tabelaCafes.innerHTML = `
            <tr>
                <td colspan="6" class="empty">
                    Nenhum café cadastrado.
                </td>
            </tr>
        `;

        return;
    }

    lista.forEach(cafe => {

        const linha = document.createElement("tr");

        const valores = [
            cafe.id,
            cafe.nomeProduto,
            cafe.tamanhoProduto,
            cafe.tipoTorra,
            Number(cafe.preco).toLocaleString("pt-BR", {
                style: "currency",
                currency: "BRL"
            })
        ];

        valores.forEach(valor => {
            const coluna = document.createElement("td");
            coluna.textContent = valor;
            linha.appendChild(coluna);
        });

        const colunaAcoes = document.createElement("td");

        colunaAcoes.innerHTML = `
            <div class="action-buttons">
                <button
                    class="btn-edit"
                    data-action="edit"
                    data-id="${Number(cafe.id)}">
                    Editar
                </button>

                <button
                    class="btn-delete"
                    data-action="delete"
                    data-id="${Number(cafe.id)}">
                    Excluir
                </button>
            </div>
        `;

        linha.appendChild(colunaAcoes);

        tabelaCafes.appendChild(linha);
    });
}

// ==================== CADASTRAR / ATUALIZAR ====================

cafeForm.addEventListener("submit", async (evento) => {

    evento.preventDefault();

    const id = cafeId.value;

    const cafe = {
        nomeProduto: nomeProduto.value.trim(),
        tamanhoProduto: tamanhoProduto.value,
        tipoTorra: tipoTorra.value,
        preco: Number(preco.value)
    };

    if (
        !cafe.nomeProduto ||
        !cafe.tamanhoProduto ||
        !cafe.tipoTorra ||
        !Number.isFinite(cafe.preco) ||
        cafe.preco <= 0
    ) {
        mostrarMensagem("Preencha todos os campos corretamente.", "error");
        return;
    }

    btnSalvar.disabled = true;

    try {

        if (id) {

            // PUT - Atualiza um café existente.

            await requisicao(
                `${API_URL}/${encodeURIComponent(id)}`,
                "PUT",
                cafe
            );

            mostrarMensagem("Café atualizado com sucesso!");

        } else {

            // POST - Cadastra um novo café.

            const salvo = await requisicao(
                API_URL,
                "POST",
                cafe
            );

            mostrarMensagem(
                `Café cadastrado com sucesso! ID: ${salvo.id}`
            );
        }

        limparFormulario();
        await listarCafes();

    } catch (erro) {

        mostrarMensagem(erro.message, "error");

    } finally {

        btnSalvar.disabled = false;
    }
});

// ==================== BUSCAR POR ID - GET ====================

async function buscarCafePorId() {

    const id = buscarId.value.trim();

    if (!id || !Number.isInteger(Number(id)) || Number(id) <= 0) {
        mostrarMensagem("Informe um ID válido.", "error");
        return;
    }

    try {

        const cafe = await requisicao(
            `${API_URL}/${encodeURIComponent(id)}`
        );

        cafes = [cafe];

        exibirCafes(cafes);

        mostrarMensagem(`Café encontrado: ${cafe.nomeProduto}`);

    } catch (erro) {

        mostrarMensagem(erro.message, "error");

        tabelaCafes.innerHTML = `
            <tr>
                <td colspan="6" class="empty">
                    Café não encontrado.
                </td>
            </tr>
        `;

        totalCafes.textContent = "0";
    }
}

// ==================== EDITAR CAFÉ ====================

function editarCafe(id) {

    const cafe = cafes.find(item => item.id === id);

    if (!cafe) {
        mostrarMensagem("Café não encontrado na lista.", "error");
        return;
    }

    cafeId.value = cafe.id;
    nomeProduto.value = cafe.nomeProduto;
    tamanhoProduto.value = cafe.tamanhoProduto;
    tipoTorra.value = cafe.tipoTorra;
    preco.value = cafe.preco;

    document.getElementById("formTitle").textContent =
        "Alterar café";

    btnSalvar.textContent = "Salvar alterações";

    mostrarMensagem(
        `Editando o café de ID ${cafe.id}.`
    );

    document.querySelector(".form-panel").scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

// ==================== EXCLUIR CAFÉ - DELETE ====================

async function excluirCafe(id) {

    const cafe = cafes.find(item => item.id === id);

    const nome = cafe ? cafe.nomeProduto : `ID ${id}`;

    if (!confirm(`Deseja realmente excluir "${nome}"?`)) {
        return;
    }

    try {

        await requisicao(
            `${API_URL}/${encodeURIComponent(id)}`,
            "DELETE"
        );

        mostrarMensagem("Café excluído com sucesso!");

        await listarCafes();

        if (Number(cafeId.value) === id) {
            limparFormulario();
        }

    } catch (erro) {

        mostrarMensagem(erro.message, "error");
    }
}

// ==================== EVENTOS DA TABELA ====================

tabelaCafes.addEventListener("click", (evento) => {

    const botao = evento.target.closest("button[data-action]");

    if (!botao) {
        return;
    }

    const id = Number(botao.dataset.id);

    if (!Number.isInteger(id) || id <= 0) {
        return;
    }

    if (botao.dataset.action === "edit") {
        editarCafe(id);
    }

    if (botao.dataset.action === "delete") {
        excluirCafe(id);
    }
});

// ==================== LIMPAR FORMULÁRIO ====================

function limparFormulario() {

    cafeForm.reset();

    cafeId.value = "";

    document.getElementById("formTitle").textContent =
        "Cadastrar café";

    btnSalvar.textContent = "Cadastrar café";
}

// ==================== BOTÕES ====================

btnLimpar.addEventListener("click", () => {

    limparFormulario();

    mostrarMensagem("Formulário limpo.");
});

btnAtualizar.addEventListener("click", listarCafes);

btnBuscar.addEventListener("click", buscarCafePorId);

btnListar.addEventListener("click", () => {

    buscarId.value = "";

    listarCafes();
});

buscarId.addEventListener("keydown", evento => {

    if (evento.key === "Enter") {
        buscarCafePorId();
    }
});

// ==================== INICIALIZAÇÃO ====================

// Carrega os registros assim que a página é aberta.

document.addEventListener("DOMContentLoaded", listarCafes);
