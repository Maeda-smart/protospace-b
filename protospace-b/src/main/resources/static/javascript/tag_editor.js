validation = (value) => {
  if (value === "") throw new Error("Validation Error");
  return value;
};
before_send_hook = (value) => {
  return value;
};
delete_handler = (delete_button) => {
  delete_button.addEventListener("click", () => {
    delete_button.parentElement.remove();
  });
};
window.addEventListener("load", () => {
  console.log("loaded");
  const tags = document.querySelector(".tags");
  const edit_button = document.querySelector(".tag.edit-button");
  const edit_input = document.querySelector(".tag.edit-input");
  const add_button = document.querySelector(".add-button");
  const tag_input = document.getElementById("create-tag");
  document.querySelectorAll("span.delete-button").forEach(delete_button => { delete_handler(delete_button) });
  edit_button.addEventListener("click", () => {
    edit_button.classList.add("hidden");
    edit_input.classList.remove("hidden");
  });
  tag_input.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      add_button.click();
    }
  })
  add_button.addEventListener("click", (event) => {
    event.stopPropagation();
    let value = tag_input.value;
    if (typeof value !== "string") return;
    tag_input.value = "";
    try {
      value = validation(value);
    } catch (error) {
      edit_button.classList.remove("hidden");
      edit_input.classList.add("hidden");
    }

    const newTag = document.createElement("div");
    newTag.className = "tag";

    const tag_name_span = document.createElement("span");
    tag_name_span.textContent = value;
    newTag.appendChild(tag_name_span);

    const delete_span = document.createElement("span");
    delete_span.className = "delete-button";
    delete_span.textContent = "×";
    delete_handler(delete_span);
    newTag.appendChild(delete_span);

    const hidden_input = document.createElement("input");
    hidden_input.type = "text";
    hidden_input.name = "tags";
    hidden_input.className = "hidden";
    hidden_input.value = before_send_hook(value);
    newTag.appendChild(hidden_input);

    tags.insertBefore(newTag, edit_button);

    edit_button.classList.remove("hidden");
    edit_input.classList.add("hidden");
  });
});