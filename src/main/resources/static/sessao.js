(function () {
    "use strict";

    const fetchOriginal = window.fetch.bind(window);

    window.fetch = function (entrada, opcoes) {
        const init = opcoes ? { ...opcoes } : {};
        const cabecalhos = new Headers(init.headers || {});
        const metodo = String(init.method || "GET").toUpperCase();

        if (metodo !== "GET" && metodo !== "HEAD" && metodo !== "OPTIONS") {
            const token = tokenCsrf();
            if (token && !cabecalhos.has("X-XSRF-TOKEN")) {
                cabecalhos.set("X-XSRF-TOKEN", token);
            }
        }

        init.headers = cabecalhos;
        init.credentials = init.credentials || "same-origin";

        return fetchOriginal(entrada, init).then(function (resposta) {
            const caminho = window.location.pathname;
            const publico = caminho === "/login" || caminho === "/acompanhar";

            if (resposta.status === 401 && !publico) {
                localStorage.removeItem("usuarioLogado");
                window.location.replace("/login");
            }

            return resposta;
        });
    };

    window.tokenCsrf = tokenCsrf;

    window.mostrarAviso = function (mensagem, tipo) {
        const estilo = tipo === "erro" ? "erro" : "sucesso";
        garantirAreaAvisos();

        const aviso = document.createElement("div");
        aviso.className = "aviso aviso-" + estilo;
        aviso.setAttribute("role", "status");
        aviso.textContent = String(mensagem ?? "");

        document.getElementById("areaAvisos").appendChild(aviso);

        window.setTimeout(function () {
            aviso.classList.add("aviso-oculto");
            window.setTimeout(function () {
                aviso.remove();
            }, 280);
        }, 4200);
    };

    window.mostrarErro = function (mensagem) {
        window.mostrarAviso(mensagem, "erro");
    };

    function tokenCsrf() {
        const match = document.cookie.match(/(?:^|; )XSRF-TOKEN=([^;]+)/);
        return match ? decodeURIComponent(match[1]) : "";
    }

    function garantirAreaAvisos() {
        if (document.getElementById("areaAvisos")) {
            return;
        }

        const area = document.createElement("div");
        area.id = "areaAvisos";
        area.className = "area-avisos";
        document.body.appendChild(area);
    }
})();
