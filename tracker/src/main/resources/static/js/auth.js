const BASE_URL = "http://localhost:8080/api";

function saveToken(token) {
  localStorage.setItem("accessToken", token);
  localStorage.setItem("tokenTimestamp", Date.now());
}

function isTokenExpired() {
  const timestamp = localStorage.getItem("tokenTimestamp");
  if (!timestamp) return true;
  const TEN_HOURS = 10 * 60 * 60 * 1000;
  return Date.now() - Number(timestamp) > TEN_HOURS;
}

function login() {
  const email = document.getElementById("login-email").value;
  const password = document.getElementById("login-password").value;

  fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  })
  .then(res => {
    if (!res.ok) throw new Error("Ошибка входа");
    return res.json();
  })
  .then(data => {
    saveToken(data.token);
    window.location.href = "dashboard.html";
  })
  .catch(err => alert(err.message));
}

function register() {
  const email = document.getElementById("register-email").value;
  const password = document.getElementById("register-password").value;

  fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password })
  })
  .then(res => {
    if (!res.ok) throw new Error("Ошибка регистрации");
    return res.json();
  })
  .then(() => {
    alert("Успешная регистрация! Теперь войдите.");
  })
  .catch(err => alert(err.message));
}
