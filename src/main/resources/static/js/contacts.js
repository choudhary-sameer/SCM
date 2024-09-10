console.log("Contacts.js");

const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    setTimeout(() => {
      contactModal.classList.add("scale-100");
    }, 50);
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};


const contactModal = new Modal(viewContactModal, options, instanceOptions);

function opneContactModal() {
    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

// function call to load data
async function loadContactData(id) {
    console.log(id);

    try {
        const data = await (await fetch(`${baseURL}`))
    }
    
}