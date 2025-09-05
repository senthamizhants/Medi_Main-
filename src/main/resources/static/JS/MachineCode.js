window.onload = function () {
    const flash = document.getElementById("flashMessage");
    if (flash) {
        setTimeout(() => {
            flash.style.opacity = "0"; // fade out
            setTimeout(() => flash.remove(), 500); // remove after fade
        }, 2000);
    }

    // ✅ Real-time search input listener
    const searchInput = document.getElementById("searchInput");
    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const keyword = searchInput.value.trim();
            fetch(`/searchMachineCode?keyword=${encodeURIComponent(keyword)}`)
                .then(response => {
                    if (!response.ok) throw new Error("Search failed");
                    return response.text();
                })
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, "text/html");
                    const newTbody = doc.querySelector("tbody");
                    if (newTbody) {
                        document.querySelector("#machineTable tbody").innerHTML = newTbody.innerHTML;
                    }
                })
                .catch(error => {
                    console.error("Search error:", error);
                });
        });
    }
};
document.addEventListener("DOMContentLoaded", function() {
    const dropdown = document.getElementById("locationDropdown");
    const firstOption = dropdown.options[0];

    dropdown.addEventListener("focus", function() {
      if (firstOption) {
        firstOption.style.display = "none";  // hide "Select Location"
      }
    });
  });
  document.addEventListener("DOMContentLoaded", function() {
     const machineDropdown = document.getElementById("machineDropdown");
     const firstOption = machineDropdown.options[0];

     machineDropdown.addEventListener("focus", function() {
       if (firstOption) {
         firstOption.style.display = "none";  // hide "-- Select Machine --"
       }
     });
   });
function validateForm() {
    const location = document.getElementById('location').value;
    const machine = document.getElementById('machinename').value;
    const testid = document.getElementById('testid').value;
    const testname = document.getElementById('testname').value;

    if (location && machine && testid && testname) {
        const table = document.getElementById('machineTable').querySelector('tbody');
        const newRow = table.insertRow();
        newRow.innerHTML = `<td>${location}</td><td>${machine}</td><td>${testid}</td><td>${testname}</td>`;
        document.getElementById('machinecodeform').reset();
        return false;
    }
    return false;
}

function clearForm() {
    const form = document.getElementById('machinecodeform');
    form.reset();
    document.querySelector('[name="id"]').value = '';
    document.getElementById("saveButton").style.display = "inline-block";
    document.getElementById("updateButton").style.display = "none";

    document.querySelectorAll("#machineTable tbody tr").forEach(row => {
        row.classList.remove("edited-row");
    });
}

function setFormAction(action) {
    document.getElementById("formAction").value = action;
}

function addRow() {
    const location = document.getElementById('location').value;
    const machine = document.getElementById('machinename').value;
    const testid = document.getElementById('testid').value;
    const testname = document.getElementById('testname').value;

    if (location && machine && testid && testname) {
        const table = document.getElementById('machineTable').querySelector('tbody');
        const newRow = table.insertRow();
        newRow.innerHTML = `
            <td>${location}</td>
            <td>${machine}</td>
            <td>${testid}</td>
            <td>${testname}</td>
            <td>
                <button class="action-btn edit-btn" onclick="editRow(this)">Edit</button>
                <button class="action-btn delete-btn" onclick="deleteRow(this)">Delete</button>
            </td>`;
        clearForm();
        return false;
    }
    return false;
}

function confirmDelete(button) {
    const id = button.getAttribute("data-id");
    if (id && confirm("Are you sure you want to delete this record?")) {
        window.location.href = "/deleteMachineParam/" + id;
    }
}

function deleteRow(btn) {
    if (confirm("Are you sure you want to delete this record?")) {
        const row = btn.closest('tr');
        row.remove();
    }
}
function deleteEntry(button) {
    const id = button.getAttribute("data-id");
    if (!confirm("Are you sure you want to delete this entry?")) return;

    fetch(`/deleteMachineParameter/${id}`, {
        method: "DELETE"
    })
    .then(response => {
        if (response.ok) {
            // Remove the row from the table
            const row = button.closest("tr");
            if (row) row.remove();
        } else {
            alert("Failed to delete entry.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("An error occurred while deleting.");
    });
}
function editRow(btn) {
    document.querySelectorAll("#machineTable tbody tr").forEach(row => {
        row.classList.remove("edited-row");
    });

    const currentRow = btn.closest('tr');
    currentRow.classList.add("edited-row");

    document.querySelector('[name="id"]').value = btn.getAttribute("data-id");
    document.querySelector('[name="locationId"]').value = btn.getAttribute("data-location-id");
    document.querySelector('[name="machineId"]').value = btn.getAttribute("data-machine-id");
    document.querySelector('[name="testId"]').value = btn.getAttribute("data-test-id");
    document.querySelector('[name="testName"]').value = btn.getAttribute("data-test-name");

    const page = btn.getAttribute("data-page");
    document.getElementById("currentPageInput").value = page;

    document.getElementById("saveButton").style.display = "none";
    document.getElementById("updateButton").style.display = "inline-block";
}

function goBack() {
    window.location.href = "/home";
}


document.getElementById("locationDropdown").addEventListener("change", function() {
    const locationId = this.value;
    const machineDropdown = document.getElementById("machineDropdown");

    // Clear old options
    machineDropdown.innerHTML = '<option value="">-- Select Machine --</option>';

    if(locationId) {
        // Call backend to get machines
        fetch('/getMachines/' + locationId)
            .then(res => res.json())
            .then(data => {
                data.forEach(m => {
                    const opt = document.createElement("option");
                    opt.value = m.id;
                    opt.text = m.name;
                    machineDropdown.add(opt);
                });
            })
            .catch(err => console.error("Error loading machines:", err));
    }
});
