console.log("notification.js 読み込まれました");
 
document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("notificationModal");
  const list = document.getElementById("notificationList");
  const button = document.getElementById("notificationButton");
  const closeBtn = document.getElementById("closeModal");
 
  console.log("通知ボタン:", button);
 
  if (!button || !modal || !list) {
    console.warn("通知機能の要素が見つかりません");
    return;
  }
 
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
            window.location.href = `/prototypes/${n.prototypeId}/detail`;
          });
          list.appendChild(li);
        });
 
        // 🔽 通知ボタンの位置取得 → モーダル表示位置に反映
        const offsetX = -140;
        const offsetY = 20;
        const rect = button.getBoundingClientRect();
        modal.style.position = "absolute";
        modal.style.left = `${rect.left + offsetX}px`;
        modal.style.top = `${rect.bottom + window.scrollY + offsetY}px`;
        modal.style.display = "block";
      })
      .catch(err => {
        console.error("通知取得エラー:", err);
      });
  });
 
  // ✖ボタンで閉じる
  if (closeBtn) {
    closeBtn.addEventListener("click", () => {
      modal.style.display = "none";
    });
  }
 
  // 🔽 モーダル・ボタン以外クリックで閉じる
  window.addEventListener("click", (e) => {
    if (
      modal.style.display === "block" &&
      !modal.contains(e.target) &&
      e.target !== button
    ) {
      modal.style.display = "none";
    }
  });
});