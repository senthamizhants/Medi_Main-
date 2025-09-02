
window.onload = function () {
    const flash = document.getElementById("flashMessage");
    if (flash) {
        setTimeout(() => {
            flash.style.opacity = "0"; // fade out
            setTimeout(() => flash.remove(), 500); // remove after fade
        }, 2000); // show for 2.5 seconds
    }
};
function validateForm() {
    const location = document.getElementById('locationId').value;
    const machine = document.getElementById('machineId').value;
    const parameterid = document.getElementById('parameterId').value;
    const parametername = document.getElementById('parameterName').value;

    if (location && machine && parameterid && parametername) {
        const table = document.getElementById('lisparameterTable').querySelector('tbody');
        const newRow = table.insertRow();
        newRow.innerHTML = `<td>${location}</td><td>${machine}</td><td>${parameterid}</td><td>${parametername}</td>`;
        document.getElementById('lisparametercodeform').reset();
        return false;
    }
    return false;
}


function clearForm() {
    const form = document.getElementById('lisparametercodeform');
    form.reset();

   
    document.querySelector('[name="id"]').value = '';
    document.getElementById("formAction").value = "save";

   
    document.getElementById("saveButton").style.display = "inline-block";
    document.getElementById("updateButton").style.display = "none";

    // Remove highlight
    document.querySelectorAll("#lisparameterTable tbody tr").forEach(row => {
        row.classList.remove("edited-row");
    });
}
function setFormAction(action) {
    document.getElementById("formAction").value = action;
}
function addRow() {
    const location = document.getElementById('location').value;
    const machine = document.getElementById('machinename').value;
    const parameterid = document.getElementById('parameterid').value;
    const testname = document.getElementById('parametername').value;

    if (location && machine && parameterid && parametername) {
        const table = document.getElementById('lisparameterTable').querySelector('tbody');
        const newRow = table.insertRow();
        newRow.innerHTML = `
            <td>${location}</td>
            <td>${machine}</td>
            <td>${parameterid}</td>
            <td>${parametername}</td>
            <td>
                <button class="action-btn edit-btn" onclick="editRow(this)">Edit</button>
                <button class="action-btn delete-btn" onclick="deleteRow(this)">Delete</button>
            </td>`;
        clearForm();
        searchTable(false);
        return false;
    }
    return false;
}

function deleteEntry(button) {
    const id = button.getAttribute("data-id");
    if (confirm("Are you sure you want to delete this record?")) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = `/lisparameterCode/delete/${id}`;
        document.body.appendChild(form);
        form.submit();
    }
}
function editRow(btn) {
  
    document.querySelectorAll("#lisparameterTable tbody tr").forEach(row => {
        row.classList.remove("edited-row");
    });

  
    const currentRow = btn.closest('tr');
    currentRow.classList.add("edited-row");

   
    document.querySelector('[name="id"]').value = btn.getAttribute("data-id");
    document.querySelector('[name="locationId"]').value = btn.getAttribute("data-location-id");
    document.querySelector('[name="machineId"]').value = btn.getAttribute("data-machine-id");
    document.querySelector('[name="parameterId"]').value = btn.getAttribute("data-param-id");
    document.querySelector('[name="parameterName"]').value = btn.getAttribute("data-param-name");
    
    const page = btn.getAttribute("data-page");
    document.getElementById("currentPageInput").value = page;

   
    document.getElementById("saveButton").style.display = "none";
    document.getElementById("updateButton").style.display = "inline-block";
}
function searchTable() {
    const input = document.getElementById("searchInput");
    const filter = input.value.trim().toUpperCase();
    const tableBody = document.getElementById("lisparameterTable").querySelector('tbody');
    const rows = tableBody.querySelectorAll("tr");

    rows.forEach(row => {
        const cells = row.querySelectorAll("td");
        let matchFound = false;

        cells.forEach(cell => {
            const cellText = cell.textContent || cell.innerText;
            if (cellText.toUpperCase().includes(filter)) {
                matchFound = true;
            }
        });

        if (filter === "") {
           
            row.style.display = "";
            row.style.backgroundColor = "";
        } else if (matchFound) {
            row.style.display = "";
            row.style.backgroundColor = "#ffff99"; 
        } else {
            row.style.display = "none";
        }
    });
}
function goBack() {
    window.location.href = "/home";  
}

document.addEventListener("DOMContentLoaded", function () {
    const parameterIdInput = document.getElementById("parameterId");
    const flashDiv = document.getElementById("flashMessage");
    const saveBtn = document.getElementById("saveButton");
    const updateBtn = document.getElementById("updateButton");

    parameterIdInput.addEventListener("blur", function () {
        const paramId = this.value.trim();

        if (paramId) {
            fetch(`/lisparameterCode/check-duplicate?parameterId=${encodeURIComponent(paramId)}`)
                .then(response => response.json())
                .then(isDuplicate => {
                    if (isDuplicate) {
                        
                        flashDiv.style.display = "inline-block";
                        flashDiv.style.backgroundColor = "#f8d7da"; 
                        flashDiv.style.color = "#721c24";
                        flashDiv.style.border = "1px solid #f5c6cb";
                        flashDiv.querySelector("span").textContent =
                            "⚠ This Parameter ID already exists.";

                        saveBtn.disabled = true;
                        updateBtn.disabled = true;
                    } else {
                        
                        flashDiv.style.display = "none";
                        flashDiv.querySelector("span").textContent = "";

                        saveBtn.disabled = false;
                        updateBtn.disabled = false;
                    }
                })
                .catch(err => {
                    console.error("Error checking duplicate:", err);
                });
        } else {
            flashDiv.style.display = "none";
            flashDiv.querySelector("span").textContent = "";
            saveBtn.disabled = false;
            updateBtn.disabled = false;
        }
    });

   
    parameterIdInput.addEventListener("input", function () {
        saveBtn.disabled = false;
        updateBtn.disabled = false;
        flashDiv.style.display = "none";
        flashDiv.querySelector("span").textContent = "";
    });
});