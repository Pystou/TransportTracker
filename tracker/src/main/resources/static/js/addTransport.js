document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("accessToken");
  if (!token) return (window.location.href = "/index.html");

  const templateId = new URLSearchParams(window.location.search).get("templateId");
  if (!templateId) {
    alert("Не указан шаблон!");
    return (window.location.href = "/dashboard.html");
  }

  let selectedType = "";
  let selectedStop = null;

  const headers = {
    "Authorization": `Bearer ${token}`,
    "Content-Type": "application/json"
  };

  const transportButtons = document.querySelectorAll("#transportButtons button");
  const formSection = document.getElementById("formSection");
  const stopsSection = document.getElementById("stopsSection");
  const direction0Div = document.getElementById("direction0");
  const direction1Div = document.getElementById("direction1");
  const chosenStopDisplay = document.getElementById("chosenStop");

  const loadStopsBtn = document.getElementById("loadStopsBtn");
  const transportNumberInput = document.getElementById("transportNumber");
  const waitTimeInput = document.getElementById("waitTime");
  const waitTimeSection = document.getElementById("waitTimeSection");
  const submitBtn = document.getElementById("submitBtn");
  const cancelBtn = document.getElementById("cancelBtn");

  transportButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      selectedType = btn.dataset.type;
      formSection.style.display = "block";
      stopsSection.style.display = "none";
      waitTimeSection.style.display = "none";
    });
  });

  loadStopsBtn.addEventListener("click", () => {
    const number = transportNumberInput.value.trim();
    if (!selectedType || !number) return alert("Укажите тип и номер транспорта.");

    fetch(`/api/schedule/stops?type=${selectedType}&route=${encodeURIComponent(number)}`, { headers })
      .then(res => {
        if (!res.ok) throw new Error("Ошибка загрузки остановок");
        return res.json();
      })
      .then(stops => {
        direction0Div.innerHTML = "<h4>Направление 0</h4>";
        direction1Div.innerHTML = "<h4>Направление 1</h4>";
        stopsSection.style.display = "block";

        stops.forEach(stop => {
          const btn = document.createElement("button");
          btn.textContent = stop.name;
          btn.dataset.id = stop.stopId;
          btn.dataset.name = stop.name;
          btn.dataset.direction = stop.direction;
          btn.addEventListener("click", () => {
            selectedStop = stop;
            chosenStopDisplay.textContent = `Вы выбрали остановку: ${stop.name} (напр. ${stop.direction})`;
            waitTimeSection.style.display = "block";
          });

          if (stop.direction == 0) {
            direction0Div.appendChild(btn);
          } else {
            direction1Div.appendChild(btn);
          }
        });
      })
      .catch(err => {
        console.error(err);
        alert("Не удалось загрузить остановки.");
      });
  });

  submitBtn.addEventListener("click", () => {
    const number = transportNumberInput.value.trim();
    const waitTime = parseInt(waitTimeInput.value, 10);

    if (!selectedStop || !waitTime || !number) {
      return alert("Заполните все поля и выберите остановку.");
    }

    const body = {
      transportType: selectedType,
      transportNumber: number,
      stopId: selectedStop.stopId,
      stopName: selectedStop.name,
      direction: selectedStop.direction,
      userWaitTime: waitTime
    };

    fetch(`/api/transport/${templateId}`, {
      method: "POST",
      headers,
      body: JSON.stringify(body)
    })
    .then(res => {
      if (!res.ok) throw new Error("Ошибка при добавлении транспорта");
      window.location.href = `/template.html?templateId=${templateId}`;
    })
    .catch(err => {
      console.error(err);
      alert("Не удалось добавить транспорт.");
    });
  });

  cancelBtn.addEventListener("click", () => {
    window.location.href = `/template.html?templateId=${templateId}`;
  });
});
