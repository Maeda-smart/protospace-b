document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("notificationModal");
  const list = document.getElementById("notificationList");
  const button = document.getElementById("notificationButton");
  const closeBtn = document.getElementById("closeModal");
 
  if (!button || !modal) return;
 
  button.addEventListener("click", () => {
    fetch("/api/notifications")
      .then(res => res.json())
      .then(data => {
        list.innerHTML = "";
        data.forEach(n => {
          const li = document.createElement("li");
          li.classList.add("notification-item");
          li.textContent = `${n.commenterName} さんがあなたの投稿「${n.prototypeName}」にコメントしました`;
          li.addEventListener("click", () => {
            modal.style.display = "none";
            window.location.href = `/prototypes/${n.prototypeId}`;
          });
          list.appendChild(li);
        });
        modal.style.display = "block";
      });
  });
 
  closeBtn.addEventListener("click", () => {
    modal.style.display = "none";
  });
 
  window.addEventListener("click", (e) => {
    if (e.target === modal) {
      modal.style.display = "none";
    }
  });
});