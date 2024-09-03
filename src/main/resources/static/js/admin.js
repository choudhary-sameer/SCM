console.log("Admin User");

document
  .querySelector("#img_file_input")
  .addEventListener("change", function (event) {
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function () {
      document.getElementById("upload_img_preview").src = reader.result;
    };
    reader.readAsDataURL(file);
  });
