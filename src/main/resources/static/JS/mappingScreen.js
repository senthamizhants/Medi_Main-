window.addEventListener('DOMContentLoaded', () => {
    allowOnlyOneCheckbox('testTable', 'selectedTests');
    allowOnlyOneCheckbox('machineTable', 'selectedRows');

    const searchBtn = document.getElementById('searchBtn');
    if (searchBtn) searchBtn.addEventListener('click', searchTable);

    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchTable();
            }
        });
    }

    const testSearchBtn = document.getElementById('testSearchBtn');
    const testSearchInput = document.getElementById('testSearchInput');
    if (testSearchBtn && testSearchInput) {
        testSearchBtn.addEventListener('click', searchTestTable);
        testSearchInput.addEventListener('keypress', e => {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchTestTable();
            }
        });
    }

    // Fetch test data on machine change
    const machineSelect = document.getElementById('machine');
    if (machineSelect) {
        machineSelect.addEventListener('change', function () {
            const machineId = this.value;
            fetch(`/getTestsByMachine/${machineId}`)
                .then(response => response.text())
                .then(html => {
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(html, "text/html");
                    const newRows = doc.querySelector("tbody").innerHTML;
                    document.querySelector("#machineTable tbody").innerHTML = newRows;

                    // Re-apply checkbox logic after DOM update
                    allowOnlyOneCheckbox('machineTable', 'selectedRows');
                });
        });
    }
});

function validateForm() {
    const selectedTest = document.querySelector('input[name="selectedTests"]:checked');
    const selectedParam = document.querySelector('input[name="selectedParams"]:checked');

    if (!selectedTest || !selectedParam) {
        alert("Please select one test and one parameter.");
        return false;
    }

    document.getElementById("testId").value = selectedTest.getAttribute("data-test-id");
    document.getElementById("testName").value = selectedTest.getAttribute("data-test-name");
    document.getElementById("selectedParameterId").value = selectedParam.getAttribute("data-param-id");
    document.getElementById("selectedParameterName").value = selectedParam.getAttribute("data-param-name");

    return true;
}


function highlightRow(checkbox) {
    const row = checkbox.closest('tr');
    removeHighlightFromRows(row.closest('table').id);
    if (row) row.style.backgroundColor = '#e6f7ff';
}

function removeHighlightFromRows(tableId) {
    document.querySelectorAll(`#${tableId} tbody tr`).forEach(row => {
        row.style.backgroundColor = '';
    });
}

function searchTable() {
    const filter = document.getElementById("searchInput").value.toUpperCase();
    const rows = document.querySelectorAll("#machineTable tbody tr");

    rows.forEach(row => {
        const tds = row.querySelectorAll("td");
        let match = false;
        for (let i = 1; i < tds.length - 1; i++) {
            if (tds[i].innerText.toUpperCase().includes(filter)) {
                match = true;
                break;
            }
        }
        row.style.display = match ? "" : "none";
    });
}

function searchTestTable() {
    const filter = document.getElementById("testSearchInput").value.toUpperCase();
    const rows = document.querySelectorAll("#testTable tbody tr");

    rows.forEach(row => {
        const tds = row.querySelectorAll("td");
        let found = false;
        for (let j = 1; j < tds.length; j++) {
            if (tds[j].innerText.toUpperCase().includes(filter)) {
                found = true;
                break;
            }
        }
        row.style.display = found ? "" : "none";
    });
}
function deleteRow(button) {
   const row = button.closest("tr");
   const id = row.getAttribute("data-id");

   if (!id) {
     alert("No ID found on row.");
     return;
   }

   if (!confirm("Are you sure you want to delete this item?")) {
     return;
   }

   fetch(`/deleteParameter/${id}`, {
     method: "DELETE",
   })
   .then(response => {
     if (response.ok) {
       row.remove(); // Remove row from HTML
     } else {
       alert("Failed to delete.");
     }
   })
   .catch(error => {
     console.error("Delete failed:", error);
     alert("Error deleting item.");
   });
 }
 function goBack() {
    window.location.href = "/home";
}

function collectSelectedDataAndSave() {
    const selectedTest = document.querySelector('#testTable input[name="selectedTests"]:checked');
    const selectedParam = document.querySelector('#parameterTable input[name="selectedParams"]:checked');
	

    if (!selectedTest || !selectedParam) {
        alert("Select one test and one parameter.");
        return;
    }

    const locationId = document.getElementById("location").value;
    const machineId = document.getElementById("machine").value;
    const locationName = document.getElementById("location").selectedOptions[0].text;
    const machineName = document.getElementById("machine").selectedOptions[0].text;

    const testId = selectedTest.getAttribute("data-test-id");
    const testName = selectedTest.getAttribute("data-test-name");

    const lisParameterId = selectedParam.getAttribute("data-param-id");
    const lisParameterName = selectedParam.getAttribute("data-param-name");

    const payload = {
        locationId, locationName,
        machineId, machineName,
        testId, testName,
        lisParameterId, lisParameterName
    };

    fetch("/api/mapping/save", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(response => response.text())
    .then(data => alert(data))
    .catch(err => console.error("Error saving mapping:", err));
}


function handleTestSelection(checkbox) {
  const checkboxes = document.querySelectorAll("input[name='selectedTests']");

  checkboxes.forEach(cb => {
    if (cb !== checkbox) cb.checked = false; // uncheck all others
  });

  if (checkbox.checked) {
    const testId = checkbox.getAttribute("data-test-id");
    const testName = checkbox.getAttribute("data-test-name");

    document.getElementById("selectedTestId").value = testId;
    document.getElementById("selectedTestName").value = testName;
  } else {
    document.getElementById("selectedTestId").value = '';
    document.getElementById("selectedTestName").value = '';
  }
}
function handleParameterSelection(paramId, paramName) {
    document.getElementById('selectedParameterId').value = paramId;
    document.getElementById('selectedParameterName').value = paramName;
}

function handleParamSelection(checkbox) {
  const selected = [];
  const checkboxes = document.querySelectorAll("input[name='selectedParams']:checked");
  
  checkboxes.forEach(cb => {
    const paramId = cb.getAttribute("data-param-id");
    const paramName = cb.getAttribute("data-param-name");
    selected.push(`${paramName} (${paramId})`);
  });

  // Store as comma-separated (or JSON if needed)
  document.getElementById("selectedParameterId").value =
      Array.from(checkboxes).map(cb => cb.getAttribute("data-param-id")).join(",");
  document.getElementById("selectedParameterName").value =
      Array.from(checkboxes).map(cb => cb.getAttribute("data-param-name")).join(",");

  document.getElementById("displayParam").innerText = selected.join(", ");
}


/*function handleParamSelection(checkbox) {
  const checkboxes = document.querySelectorAll("input[name='selectedParams']");
  checkboxes.forEach(cb => {
    if (cb !== checkbox) cb.checked = false;
  });

  if (checkbox.checked) {
    const paramId = checkbox.getAttribute("data-param-id");
    const paramName = checkbox.getAttribute("data-param-name");

    document.getElementById("selectedParameterId").value = paramId;
    document.getElementById("selectedParameterName").value = paramName;
  } else {
    document.getElementById("selectedParameterId").value = '';
    document.getElementById("selectedParameterName").value = '';
  }
}*/