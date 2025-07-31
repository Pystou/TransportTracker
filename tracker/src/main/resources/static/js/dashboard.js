document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");
    if (!token) {
        window.location.href = "/index.html";
        return;
    }

    const headers = {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
    };

    const templateList = document.getElementById("templateList");
    const createBtn = document.getElementById("createTemplateBtn");
    const logoutBtn = document.getElementById("logoutBtn");

    // Загрузить шаблоны
    fetch("/api/templates", { headers })
        .then(res => res.json())
        .then(data => {
            templateList.innerHTML = "";
            data.forEach(template => {
                const li = document.createElement("li");
                li.textContent = template.name + " ";

                const openBtn = document.createElement("button");
                openBtn.textContent = "Открыть";
                openBtn.onclick = () => {
                    window.location.href = `/template.html?templateId=${template.id}`;
                };

                const deleteBtn = document.createElement("button");
                deleteBtn.textContent = "Удалить";
                deleteBtn.onclick = () => {
                    fetch(`/api/templates/${template.id}`, {
                        method: "DELETE",
                        headers
                    }).then(() => location.reload());
                };

                li.append(openBtn, deleteBtn);
                templateList.appendChild(li);
            });
        });

    // Создать шаблон
    createBtn.onclick = () => {
        const name = document.getElementById("newTemplateName").value.trim();
        if (name) {
            fetch("/api/templates", {
                method: "POST",
                headers,
                body: JSON.stringify({ name })
            }).then(() => location.reload());
        }
    };

    // Выход
    logoutBtn.onclick = () => {
        localStorage.removeItem("token");
        window.location.href = "/index.html";
    };
});
