import { clearSession, getCashierName, getToken, getUserRole } from "./modules/auth.js";

const form = document.getElementById("user-form");
const message = document.getElementById("message");
const usersBody = document.getElementById("users-body");
const reloadBtn = document.getElementById("reload-btn");
const logoutBtn = document.getElementById("logout-btn");

function setMessage(type, text) {
  message.className = `message ${type}`;
  message.textContent = text;
}

function requireAdmin() {
  if (!getToken()) {
    window.location.replace("login.html");
    return false;
  }

  if (getUserRole() !== "ADMIN") {
    setMessage("error", "Solo los administradores pueden gestionar clientes.");
    usersBody.innerHTML = `<tr><td colspan="4">Acceso restringido.</td></tr>`;
    form.querySelectorAll("input, select, button").forEach((field) => field.disabled = true);
    return false;
  }

  return true;
}

async function fetchJson(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${getToken()}`,
      ...(options.headers ?? {}),
    },
  });

  const text = await response.text();
  let data = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    data = null;
  }

  if (!response.ok) {
    throw new Error(data?.message || `Error ${response.status}`);
  }

  return data;
}

function renderUsers(users) {
  const currentUser = getCashierName();

  if (!users.length) {
    usersBody.innerHTML = `<tr><td colspan="4">No hay clientes registrados.</td></tr>`;
    return;
  }

  usersBody.innerHTML = users.map((user) => {
    const roleClass = user.rol === "ADMIN" ? "admin" : "cajero";
    const canDelete = user.usuario !== currentUser;
    return `
      <tr>
        <td>${user.usuario}</td>
        <td><span class="role-badge ${roleClass}">${user.rol === "ADMIN" ? "Administrador" : "Cajero"}</span></td>
        <td><span class="state-badge">${user.estado}</span></td>
        <td>
          <button class="btn btn-danger" data-delete="${user.usuario}" ${canDelete ? "" : "disabled"}>
            Borrar
          </button>
        </td>
      </tr>
    `;
  }).join("");
}

async function loadUsers() {
  if (!requireAdmin()) return;

  try {
    const users = await fetchJson("/usuarios");
    renderUsers(users);
  } catch (error) {
    setMessage("error", error.message);
  }
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  if (!requireAdmin()) return;

  const payload = {
    usuario: document.getElementById("usuario").value.trim(),
    contrasena: document.getElementById("contrasena").value.trim(),
    rol: document.getElementById("rol").value,
  };

  try {
    await fetchJson("/usuarios", {
      method: "POST",
      body: JSON.stringify(payload),
    });
    form.reset();
    setMessage("success", "Cliente registrado correctamente.");
    await loadUsers();
  } catch (error) {
    setMessage("error", error.message);
  }
});

usersBody.addEventListener("click", async (event) => {
  const button = event.target.closest("[data-delete]");
  if (!button || !requireAdmin()) return;

  const usuario = button.dataset.delete;
  try {
    await fetchJson(`/usuarios/${encodeURIComponent(usuario)}`, { method: "DELETE" });
    setMessage("success", "Cliente eliminado correctamente.");
    await loadUsers();
  } catch (error) {
    setMessage("error", error.message);
  }
});

reloadBtn.addEventListener("click", loadUsers);
logoutBtn.addEventListener("click", () => {
  clearSession();
  window.location.replace("login.html");
});

loadUsers();
