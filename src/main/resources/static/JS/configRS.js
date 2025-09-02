function checkStatus() {
    const portName = document.getElementById("portSelect").value;

    if (!portName) {
        alert("Please select a port first");
        return;
    }

    fetch(`/checkPortStatus?portName=${encodeURIComponent(portName)}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("statusBox").value = data;
        })
        .catch(err => {
            document.getElementById("statusBox").value = "Error: " + err;
        });
}

document.getElementById('rs232Form').addEventListener('submit', function(event) {
    event.preventDefault(); // prevent default form submission

	fetch('/api/saveRs232Config', {
	    method: 'POST',
	    body: new FormData(document.getElementById('rs232Form'))
	})
	 .then(response => response.json())
	    .then(savedConfig => {
	        console.log("Saved:", savedConfig); // check if returned correctly
	        loadMachines(); // refresh dropdown
	    })
	    .catch(err => console.error(err));
	});