delete_handler = (delete_button) => {
  delete_button.addEventListener("click", () => {
    delete_button.parentElement.remove();
    document.querySelector("button.search-button").click();
  });
};
window.addEventListener("load", () => {
  console.log("loaded");
  document.querySelectorAll("span.delete-button").forEach(delete_button => { delete_handler(delete_button) });
});
