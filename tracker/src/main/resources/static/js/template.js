const token = localStorage.getItem("accessToken");
const templateId = new URLSearchParams(window.location.search).get("templateId");

let deleteMode = false;

document.addEventListener("DOMContentLoaded", () => {
    if (!token) {
        window.location.href = "/index.html";
        return;
    }

    loadTemplateData();

    document.getElementById("add-transport-btn").addEventListener("click", () => {
        window.location.href = `addTransport.html?templateId=${templateId}`;
    });

    document.getElementById("delete-mode-btn").addEventListener("click", () => {
        deleteMode = !deleteMode;
        document.getElementById("delete-mode-btn").textContent = deleteMode ? "Отмена" : "Удалить транспорт";
    });

    document.getElementById("back-btn").addEventListener("click", () => {
        window.location.href = "/dashboard.html";
    });

    document.getElementById("refresh-btn").addEventListener("click", () => {
        loadTemplateData();
    });
});

function loadTemplateData() {
    fetch(`/api/templates`, {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(templates => {
            const currentTemplate = templates.find(t => t.id == templateId);
            if (!currentTemplate) throw new Error("Template not found");
            document.getElementById("template-title").textContent = currentTemplate.name;
        });

    fetch(`/api/transport/${templateId}`, {
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(transportList => {
            const listEl = document.getElementById("transport-list");
            listEl.innerHTML = "";

            fetch(`/api/schedule/${templateId}/time`, {
                headers: { "Authorization": `Bearer ${token}` }
            })
                .then(res => res.json())
                .then(scheduleList => {
                    transportList.forEach(transport => {
                        const match = scheduleList.find(s => s.id === transport.id);

                        const li = document.createElement("li");
                        li.classList.add("transport-item");

                        const arrivalSpanId = `arrival-${transport.id}-${transport.direction}`;
                        const userTimeSpanId = `user-time-${transport.id}-${transport.direction}`;

                        let userTime = "нет данных";
                        let arrivalTime = "нет данных";

                        if (match) {
                            if (match.userArrivalTime) {
                                userTime = new Date(match.userArrivalTime)
                                    .toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                            }
                            if (match.nextTransportArrivalTime) {
                                arrivalTime = new Date(match.nextTransportArrivalTime)
                                    .toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                            }
                        }

                        li.innerHTML = `
                            <strong>${transport.transportType.toUpperCase()} ${transport.transportNumber}</strong><br>
                            Остановка: ${transport.stopName}<br>
                            Направление: ${transport.direction === 0 ? "0" : "1"}<br>
                            Вы на остановке: <span id="${userTimeSpanId}">${userTime}</span><br>
                            Прибытие транспорта: <span id="${arrivalSpanId}">${arrivalTime}</span>
                        `;

                        if (deleteMode) {
                            li.style.cursor = "pointer";
                        }

                        li.addEventListener("click", () => {
                            if (deleteMode) {
                                deleteTransport(transport.id);
                            }
                        });

                        listEl.appendChild(li);
                    });
                })
                .catch(err => {
                    console.error("Ошибка загрузки расписания:", err);
                    transportList.forEach(transport => {
                        const li = document.createElement("li");
                        li.textContent = `Ошибка загрузки транспорта ${transport.transportNumber}`;
                        listEl.appendChild(li);
                    });
                });
        })
        .catch(err => {
            console.error("Ошибка загрузки транспорта:", err);
        });
}

function deleteTransport(transportId) {
    fetch(`/api/transport/${transportId}`, {
        method: "DELETE",
        headers: { "Authorization": `Bearer ${token}` }
    })
        .then(() => {
            loadTemplateData();
        })
        .catch(err => {
            console.error("Ошибка при удалении транспорта:", err);
        });
}
