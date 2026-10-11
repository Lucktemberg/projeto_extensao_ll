(function () {
    "use strict";

    window.textoSeguro = function (valor) {
        return String(valor ?? "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    };

    window.mensagemErro = async function (resposta, fallback) {
        const texto = await resposta.text();

        try {
            const json = JSON.parse(texto);
            return json.mensagem || json.erro || Object.values(json)[0] || fallback;
        } catch (erro) {
            return texto || fallback;
        }
    };

    window.tabelaVazia = function (tabela, colunas, texto) {
        tabela.innerHTML = `
            <tr>
                <td colspan="${colunas}" class="tabela-vazia">${textoSeguro(texto)}</td>
            </tr>
        `;
    };

    window.filtrarTabela = function (campo, tabelaId) {
        const termo = String(campo.value ?? "").trim().toLowerCase();
        const tabela = document.getElementById(tabelaId);

        if (!tabela) {
            return;
        }

        tabela.querySelectorAll("tr").forEach(linha => {
            if (linha.querySelector(".tabela-vazia")) {
                return;
            }

            linha.hidden = termo !== ""
                && !linha.textContent.toLowerCase().includes(termo);
        });
    };

    window.formatarCpf = function (cpf) {
        const digitos = String(cpf ?? "").replace(/\D/g, "");

        if (digitos.length !== 11) {
            return cpf ?? "";
        }

        return digitos.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
    };

    window.formatarTelefone = function (telefone) {
        const digitos = String(telefone ?? "").replace(/\D/g, "");

        if (digitos.length === 11) {
            return digitos.replace(/(\d{2})(\d{5})(\d{4})/, "($1) $2-$3");
        }

        if (digitos.length === 10) {
            return digitos.replace(/(\d{2})(\d{4})(\d{4})/, "($1) $2-$3");
        }

        return telefone ?? "";
    };

    window.mostrarFormulario = function () {
        const formulario = document.getElementById("formulario");

        if (formulario) {
            formulario.style.display = "block";
            formulario.scrollIntoView({ behavior: "smooth", block: "start" });
        }
    };

    iniciarInterface();

    async function iniciarInterface() {
        const caminhoAtual = window.location.pathname;
        let usuario = obterUsuario();

        if (!usuario) {
            usuario = await buscarUsuarioSessao();
            if (!usuario) {
                window.location.replace("/login");
                return;
            }
            localStorage.setItem("usuarioLogado", JSON.stringify(usuario));
        } else {
            buscarUsuarioSessao().then(atual => {
                if (!atual) {
                    return;
                }
                localStorage.setItem("usuarioLogado", JSON.stringify(atual));
                preencherPerfil(atual);
                aplicarPermissoes(
                    atual.perfil === "ADMIN",
                    Array.isArray(atual.permissoes) ? atual.permissoes : []
                );
            });
        }

        const permissoes = Array.isArray(usuario.permissoes)
            ? usuario.permissoes
            : [];

        const perfilAdministrador = usuario.perfil === "ADMIN";

        if (!podeAcessarPagina(
                caminhoAtual,
                perfilAdministrador,
                permissoes)) {
            window.location.replace("/menu");
            return;
        }

        preencherPerfil(usuario);
        aplicarPermissoes(perfilAdministrador, permissoes);
        destacarPaginaAtual(caminhoAtual);
        configurarSidebar();
        configurarTema();
        configurarRetorno(caminhoAtual);
    }

    window.sair = function () {
        fetch("/sair", { method: "POST" })
            .finally(() => {
                localStorage.removeItem("usuarioLogado");
                sessionStorage.removeItem("solidarioUltimaPagina");
                window.location.replace("/login");
            });
    };

    async function buscarUsuarioSessao() {
        try {
            const resposta = await fetch("/usuarios/me");
            if (!resposta.ok) {
                return null;
            }
            return await resposta.json();
        } catch (erro) {
            return null;
        }
    }

    window.voltarPagina = function () {
        const paginaAnterior = document.referrer;
        const veioDoSistema = paginaAnterior.startsWith(window.location.origin);
        const veioDoLogin = paginaAnterior.endsWith("/login");

        if (window.history.length > 1 && veioDoSistema && !veioDoLogin) {
            window.history.back();
            return;
        }

        window.location.href = "/menu";
    };

    function obterUsuario() {
        try {
            return JSON.parse(localStorage.getItem("usuarioLogado"));
        } catch (erro) {
            localStorage.removeItem("usuarioLogado");
            return null;
        }
    }

    function preencherPerfil(usuarioLogado) {
        const nome = usuarioLogado.nome || "Usuário";
        const nomeUsuario = document.getElementById("nomeUsuario");
        const perfilUsuario = document.getElementById("perfilUsuario");
        const avatarUsuario = document.getElementById("avatarUsuario");

        if (nomeUsuario) {
            nomeUsuario.textContent = nome;
        }

        if (perfilUsuario) {
            perfilUsuario.textContent =
                usuarioLogado.perfil === "ADMIN"
                    ? "Administrador"
                    : "Usuário";
        }

        if (avatarUsuario) {
            avatarUsuario.textContent = obterIniciais(nome);
        }
    }

    function obterIniciais(nome) {
        const partes = nome.trim().split(/\s+/).filter(Boolean);

        if (partes.length === 0) {
            return "U";
        }

        const primeira = partes[0].charAt(0);
        const ultima = partes.length > 1
            ? partes[partes.length - 1].charAt(0)
            : "";

        return (primeira + ultima).toUpperCase();
    }

    function aplicarPermissoes(administrador, permissoesUsuario) {
        document.querySelectorAll("[data-admin-only='true'], .admin-only")
            .forEach(elemento => {
                elemento.hidden = !administrador;
            });

        document.querySelectorAll("[data-permissao]")
            .forEach(elemento => {
                const permissao = elemento.dataset.permissao;
                elemento.hidden =
                    !administrador && !permissoesUsuario.includes(permissao);
            });
    }

    function podeAcessarPagina(caminho, administrador, permissoesUsuario) {
        if (administrador) {
            return true;
        }

        if (caminho === "/tela-usuarios") {
            return false;
        }

        const permissoesPorPagina = {
            "/tela-doacoes": "DOACAO",
            "/tela-campanhas": "DOACAO",
            "/tela-distribuicoes": "DISTRIBUICAO",
            "/tela-estoques": "ESTOQUE",
            "/tela-doadores": "DOADOR",
            "/tela-receptores": "RECEPTOR",
            "/tela-tipos-doacao": "TIPO_DOACAO",
            "/tela-tamanhos-roupa": "TAMANHOS"
        };

        const permissaoNecessaria = permissoesPorPagina[caminho];
        return !permissaoNecessaria
            || permissoesUsuario.includes(permissaoNecessaria);
    }

    function destacarPaginaAtual(caminho) {
        document.querySelectorAll(".links-navegacao a")
            .forEach(link => {
                const ativo = link.getAttribute("href") === caminho;
                link.classList.toggle("ativo", ativo);

                if (ativo) {
                    link.setAttribute("aria-current", "page");
                }
            });
    }

    function configurarRetorno(caminho) {
        const botaoRetorno = document.getElementById("botaoRetorno");

        if (botaoRetorno && caminho === "/menu") {
            botaoRetorno.hidden = true;
        }
    }

    function configurarTema() {
        const botao = document.getElementById("botaoTema");
        const tema = localStorage.getItem("solidarioTema") === "escuro" ? "escuro" : "claro";
        document.documentElement.setAttribute("data-tema", tema);

        atualizarRotuloTema();

        if (!botao) {
            return;
        }

        botao.addEventListener("click", function () {
            const escuro = document.documentElement.getAttribute("data-tema") !== "escuro";
            const tema = escuro ? "escuro" : "claro";
            document.documentElement.setAttribute("data-tema", tema);
            localStorage.setItem("solidarioTema", tema);
            atualizarRotuloTema();
        });
    }

    function atualizarRotuloTema() {
        const botao = document.getElementById("botaoTema");
        const rotulo = document.getElementById("rotuloTema");
        const escuro = document.documentElement.getAttribute("data-tema") === "escuro";

        if (rotulo) {
            rotulo.textContent = escuro ? "Modo claro" : "Modo escuro";
        }

        if (botao) {
            botao.setAttribute(
                "title",
                escuro ? "Usar modo claro" : "Usar modo escuro"
            );
            botao.setAttribute(
                "aria-label",
                escuro ? "Usar modo claro" : "Usar modo escuro"
            );
        }
    }

    function configurarSidebar() {
        const sidebar = document.getElementById("sidebar");
        const botaoRecolher = document.getElementById("botaoSidebar");
        const botaoMobile = document.getElementById("botaoMenuMobile");
        const fundo = document.getElementById("sidebarFundo");

        if (!sidebar) {
            return;
        }

        atualizarRotuloSidebar();

        if (botaoRecolher) {
            botaoRecolher.addEventListener("click", function () {
                const recolhida = document.body.classList.toggle("sidebar-recolhida");
                localStorage.setItem(
                    "solidarioSidebarRecolhida",
                    recolhida ? "1" : "0"
                );
                atualizarRotuloSidebar();
            });
        }

        if (botaoMobile) {
            botaoMobile.addEventListener("click", function () {
                alternarSidebarMobile(true);
            });
        }

        if (fundo) {
            fundo.addEventListener("click", function () {
                alternarSidebarMobile(false);
            });
        }

        window.addEventListener("resize", function () {
            if (window.innerWidth > 820) {
                alternarSidebarMobile(false);
            }
        });
    }

    function atualizarRotuloSidebar() {
        const botaoRecolher = document.getElementById("botaoSidebar");
        const recolhida = document.body.classList.contains("sidebar-recolhida");

        if (botaoRecolher) {
            botaoRecolher.setAttribute(
                "aria-label",
                recolhida ? "Expandir menu" : "Recolher menu"
            );
            botaoRecolher.setAttribute(
                "title",
                recolhida ? "Expandir menu" : "Recolher menu"
            );
        }
    }

    function alternarSidebarMobile(abrir) {
        const sidebar = document.getElementById("sidebar");
        const botaoMobile = document.getElementById("botaoMenuMobile");
        const fundo = document.getElementById("sidebarFundo");

        document.body.classList.toggle("sidebar-aberta", abrir);
        if (sidebar) {
            sidebar.classList.toggle("aberta", abrir);
        }
        if (botaoMobile) {
            botaoMobile.setAttribute("aria-expanded", String(abrir));
        }
        if (fundo) {
            fundo.hidden = !abrir;
        }
    }
})();
