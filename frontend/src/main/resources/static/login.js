const TOKEN_KEY = "access_token";

const loginForm = document.getElementById("loginForm");
const loginMessage = document.getElementById("loginMessage");
const showLoginBtn = document.getElementById("showLoginBtn");
const showRegisterBtn = document.getElementById("showRegisterBtn");
const formTitle = document.getElementById("formTitle");
const formHint = document.getElementById("formHint");
const submitBtn = document.getElementById("submitBtn");
const usuarioInput = document.getElementById("usuario");
const contrasenaInput = document.getElementById("contrasena");

let authMode = "login";

function setMessage(type, text) {
    loginMessage.innerHTML = `<div class="message ${type}">${text}</div>`;
}

function saveToken(token) {
    localStorage.setItem(TOKEN_KEY, token);
}

function clearToken() {
    localStorage.removeItem(TOKEN_KEY);
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function setAuthMode(mode) {
    authMode = mode;
    loginMessage.innerHTML = "";
    loginForm.reset();

    const isRegister = mode === "register";
    showLoginBtn.classList.toggle("active", !isRegister);
    showRegisterBtn.classList.toggle("active", isRegister);
    formTitle.textContent = isRegister ? "Crear cuenta" : "Iniciar sesión";
    formHint.textContent = isRegister ? "Registra un usuario nuevo" : "Accede con tus credenciales";
    submitBtn.textContent = isRegister ? "Registrarse" : "Entrar";
    usuarioInput.placeholder = isRegister ? "Nuevo usuario" : "Usuario";
    contrasenaInput.placeholder = isRegister ? "Nueva contraseña" : "Contraseña";
}

async function validarToken() {
    const token = getToken();
    if (!token) return false;

    try {
        const response = await fetch("/productos?page=1&limit=1", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.ok) return true;

        clearToken();
        return false;
    } catch {
        clearToken();
        return false;
    }
}

window.addEventListener("DOMContentLoaded", async () => {
    const valido = await validarToken();
    if (valido) {
        window.location.replace("/pos.html");
    }
});

showLoginBtn.addEventListener("click", () => setAuthMode("login"));
showRegisterBtn.addEventListener("click", () => setAuthMode("register"));

loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    loginMessage.innerHTML = "";

    const usuario = usuarioInput.value.trim();
    const contrasena = contrasenaInput.value.trim();
    const endpoint = authMode === "register" ? "/register" : "/auth";

    try {
        clearToken();

        const response = await fetch(endpoint, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ usuario, contrasena })
        });

        const text = await response.text();
        let data = {};

        try {
            data = text ? JSON.parse(text) : {};
        } catch {
            data = {};
        }

        if (!response.ok) {
            setMessage("error", data.message || "No fue posible iniciar sesión");
            return;
        }

        if (!data.access_token) {
            setMessage("error", "El servidor no devolvió el token");
            return;
        }

        saveToken(data.access_token);
        window.location.replace("/pos.html");
    } catch (error) {
        setMessage("error", "No fue posible conectar con el servidor");
    }
});
