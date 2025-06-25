window.addEventListener("load", () => {
  console.log("loaded");
  const edit_button = document.querySelector(".tag.edit-button");
  const edit_input = document.querySelector(".tag.edit-input");
  const add_button = document.querySelector(".add-button");
  const empty = document.querySelector(".tag.empty");
  edit_button.addEventListener("click", ()=>{
    edit_button.classList.add("hidden");
    edit_input.classList.remove("hidden");
  });
  add_button.addEventListener("click", (event)=>{
    event.stopPropagation();
    edit_button.classList.remove("hidden");
    edit_input.classList.add("hidden");
  });
});