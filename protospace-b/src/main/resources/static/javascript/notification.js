document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("notificationModal");
  const list = document.getElementById("notificationList");
  const button = document.getElementById("notificationButton");
  const closeBtn = document.getElementById("closeModal");

  const csrfMeta = document.querySelector('meta[name="_csrf"]');
  const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');
 
  if (!csrfMeta || !csrfHeaderMeta) {
    console.warn("CSRFトークンのmetaタグが見つかりません");
    return;
  }
 
  // CSRFトークンとヘッダー名を取得
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
 
  if (!button || !modal || !list) {
    console.warn("通知機能の要素が見つかりません");
    return;
  }
 
  button.addEventListener("click", () => {
    console.log("通知ボタンがクリックされた");
    fetch("/api/notifications")
      .then(res => res.json())
      .then(data => {
        console.log("データ取得", data);
        list.innerHTML = "";
 
        data.forEach(n => {
          console.log("通知項目：", n);
          const li = document.createElement("li");
          li.classList.add("notification-item");
          li.textContent = `${n.commenterName} さんがあなたの投稿「${n.prototypeName}」にコメントしました`;
 
          li.addEventListener("click", () => {
            console.log("通知クリック：", n.id);
 
            fetch(`/api/notifications/${n.id}/read`, {
              method: "PATCH",
              headers: {
                [csrfHeader]: csrfToken,
                "Content-Type": "application/json"
              }
            })
              .then(res => {
                if (!res.ok) {
                  console.warn("既読更新に失敗しました");
                }
                modal.style.display = "none";
                window.location.href = `/prototypes/${n.prototypeId}/detail`;
              })
              .catch(err => {
                console.error("通知既読更新エラー:", err);
                // エラーでも遷移する
                modal.style.display = "none";
                window.location.href = `/prototypes/${n.prototypeId}/detail`;
              });
          });
 
          list.appendChild(li);
        });
 
        // 通知ボタンの位置調整
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
 
  if (closeBtn) {
    closeBtn.addEventListener("click", () => {
      modal.style.display = "none";
    });
  }
 
  window.addEventListener("click", (e) => {
    if (modal.style.display === "block" && !modal.contains(e.target) && e.target !== button) {
      modal.style.display = "none";
    }
  });
});