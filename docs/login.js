const TOKEN_KEY = "access_token";
const USERS = [
  { usuario: "PablitoInPensante", contrasena: "DGGC1912", rol: "ADMIN" },
  { usuario: "Caja", contrasena: "123", rol: "CAJERO" },
];

const loginForm = document.getElementById("loginForm");
const loginMessage = document.getElementById("loginMessage");
const usuarioInput = document.getElementById("usuario");
const contrasenaInput = document.getElementById("contrasena");

function setMessage(type, text) {
  loginMessage.innerHTML = `<div class="message ${type}">${text}</div>`;
}

function base64Url(value) {
  return btoa(unescape(encodeURIComponent(JSON.stringify(value))))
    .replace(/=/g, "")
    .replace(/\+/g, "-")
    .replace(/\//g, "_");
}

function createSessionToken(user) {
  const header = { alg: "none", typ: "JWT" };
  const payload = {
    sub: user.usuario,
    rol: user.rol,
    iat: Math.floor(Date.now() / 1000),
  };
  return `${base64Url(header)}.${base64Url(payload)}.`;
}

function saveToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

window.addEventListener("DOMContentLoaded", () => {
  if (getToken()) {
    window.location.replace("pos.html");
  }
});

loginForm.addEventListener("submit", (event) => {
  event.preventDefault();
  loginMessage.innerHTML = "";

  const usuario = usuarioInput.value.trim();
  const contrasena = contrasenaInput.value.trim();

  const user = USERS.find(
    (candidate) => candidate.usuario === usuario && candidate.contrasena === contrasena
  );

  if (!user) {
    setMessage("error", "Credenciales incorrectas");
    return;
  }

  saveToken(createSessionToken(user));
  window.location.replace("pos.html");
});
