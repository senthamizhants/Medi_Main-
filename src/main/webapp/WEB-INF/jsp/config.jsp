<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Server / Client Configuration</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: white;
            padding: 5px;
        }
        .container {
            max-width: 950px;
            margin: auto;
            background: white;
            padding: 9px 15px; 
            border-radius: 10px;
            box-shadow: 0 0 12px rgba(0, 0, 0, 0.2);
        }
        h2 {
             margin-top: 05;
            text-align: left;
             font-size: 16px;
              border-bottom: 2px solid #000;
    margin-bottom: 15px;
    padding-bottom: 8px;
        }
        .tabs {
            text-align: left;
            margin-bottom: 20px;
        }
        
        .tabs input {
            margin: 0 10px;
        }
        
        .form-section {
            display: none;
        }
        
        .form-group {
            margin-bottom: 08px;
            display: flex;
          
            align-items: center;
        }
        
        .form-group label {
            display: inline-block;
    width: 120px;        /* ✅ Fixed width for all labels */
    font-weight: bold;
    text-align: left;    /* ✅ Align text left */
    margin-right: 10px;
        }
       
        .form-group input, .form-group select {                 
    padding: 6px;
    min-width: 180px;    /* ✅ Ensure input boxes are consistent */
    box-sizing: border-box;
        }
        
        .form-group.inline {
            flex-direction: row;
        }
        
        .form-group.inline input {
            flex: 1.5;
            margin-right: 8px;
            max-width: 150px;
        }
        
        .form-group.inline button {
            flex: 0 0 auto; 
            margin: 5px;
            padding: 6px 14px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer; 
             font-size: 12px;            /* Optional: Adjust font size */
    height: auto;               /* Ensure height comes from padding */
    width: auto;                /* Or set fixed width like 150px */           
        }
        .buttons {
            text-align: left;
            margin-top: 10px;
        }      
        .buttons button {
            margin: 5px;
            padding: 6px 08px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .buttons button:hover {
            background-color: #0056b3;
        }
        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 5px;
            font-weight: bold;
        }
        .success {
            background-color: #e6ffe6;
            color: #2d7a2d;
        }
        .error {
            background-color: #ffe6e6;
            color: #a10000;
        }
        
        .form-row {
    display: flex;
    justify-content: space-between;
  
    gap: 10px; /* space between two fields */
}

.form-row .form-group1 {
    flex: 1; /* Each takes equal width */
}

fieldset {
    border: 1px solid #000; /* ✅ Blue border */
    padding: 2px 10px 2px 10px; /* Top | Right | Bottom | Left */
    border-radius: 4px;
    width: 100%; /* ✅ Make it full width inside form */
    box-sizing: border-box;
}
        
        
    </style>
 <script>
 function toggleMode(mode) {
     document.getElementById("serverForm").style.display = (mode === 'server') ? 'block' : 'none';
     document.getElementById("clientForm").style.display = (mode === 'client') ? 'block' : 'none';
 }

 function validateForm(sectionId, fieldNames) {
     for (const name of fieldNames) {
         const el = document.querySelector(`#${sectionId} [name="${name}"]`);
         if (!el || !el.value.trim()) {
             alert("Please fill all fields.");
             el.focus();
             return false;
         }
     }
     return true;
 }

 function validateServerForm() {
     return validateForm('serverForm', ['ip', 'port', 'serverName', 'machineId', 'dbUser', 'dbPass', 'dbType', 'protocol', 'clientIp']);
 }

 function validateClientForm() {
     return validateForm('clientForm', ['clientHostIp', 'clientPort', 'clientMachineName', 'clientMachineId', 'clientUser', 'clientPass', 'clientDbType', 'clientProtocol', 'pingIp']);
 }
 
 function refreshClientDropdown() {
     fetch('/api/clients')
         .then(response => response.json())
         .then(clients => {
             const select = document.getElementById('targetIpSelect');
             select.innerHTML = ''; // clear existing options
             if (clients.length === 0) {
                 const option = document.createElement('option');
                 option.text = 'No clients connected';
                 option.disabled = true;
                 select.add(option);
                 return;
             }
             clients.forEach(ip => {
                 const option = document.createElement('option');
                 option.value = ip;
                 option.text = ip;
                 select.add(option);
             });
         })
         .catch(error => {
             console.error('Error fetching clients:', error);
         });
 }

 // Refresh every 3 seconds
 setInterval(refreshClientDropdown, 3000);

 // Initial load
 document.addEventListener('DOMContentLoaded', refreshClientDropdown);
 

    </script>
</head>
<body onload="toggleMode('${mode}')">
<div class="container">
<fieldset>
    <h2>Server / Client Configurator</h2>

    <div class="tabs">
        <label><input type="radio" name="mode" value="server" onclick="toggleMode('server')" ${mode == 'server' ? 'checked' : ''}/> Server</label>
        <label><input type="radio" name="mode" value="client" onclick="toggleMode('client')" ${mode == 'client' ? 'checked' : ''}/> Client</label>
    </div>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>
    <c:if test="${not empty pingStatus}">
        <div class="message">${pingStatus}</div>
    </c:if>
    <c:if test="${not empty dbStatus}">
        <div class="message">${dbStatus}</div>
    </c:if>

    <!-- SERVER FORM -->
    <div class="form-section" id="serverForm">
        <form action="/server/test-connection" method="post" onsubmit="return validateServerForm()">
          
           <div class="form-row">           
            <div class="form-group"><label>IP:</label><input type="text" name="ip" value="${ip}"/></div>
            <div class="form-group"><label>Port:</label><input type="text" name="port" value="${port}"/></div>
            </div>
            
           <div class="form-row">
            <div class="form-group"><label>Machine Name:</label><input type="text" name="serverName" value="${serverName}"/></div>
            <div class="form-group"><label>Machine ID:</label><input type="text" name="machineId" value="${machineId}"/></div>
           </div>
           
           <div class="form-row">
            <div class="form-group"><label>DB Username:</label><input type="text" name="dbUser" value="${dbUser}"/></div>
            <div class="form-group"><label>DB Password:</label><input type="password" name="dbPass" value="${dbPass}"/></div>
           </div>
          <div class="form-row">
            <div class="form-group"><label>DB Type:</label>
                <select name="dbType">
                    <option ${dbType == 'MySQL' ? 'selected' : ''}>MySQL</option>
                    <option ${dbType == 'PostgreSQL' ? 'selected' : ''}>PostgreSQL</option>
                    <option ${dbType == 'Oracle' ? 'selected' : ''}>Oracle</option>
                </select>
            </div>
            <div class="form-group"><label>Protocol:</label>
                <select name="protocol">
                    <option ${protocol == 'TCP' ? 'selected' : ''}>TCP</option>
                    <option ${protocol == 'UDP' ? 'selected' : ''}>UDP</option>
                    <option ${protocol == 'HL7' ? 'selected' : ''}>HL7</option>
                </select>
            </div></div>
             <div class="form-row">
            <div class="form-group inline buttons">
                <label >Client IP:</label>
                <input style="max-width:180px;" type="text" name="clientIp" value="${clientIp}"/>
                <button formaction="/server/ping-client" formmethod="post" onclick="return validateServerForm()">Ping Client</button>
            </div>
            <div class="buttons">
                <button style="margin-left:30px;" type="submit">Test Connection</button>
                <input type="text" readonly value="${message}" style="margin-left:10px; width: 150px; height: 25px;" placeholder="Status will appear here..."/>
            </div>
            </div>
        </form>
	 <div class="form-row">
				<form action="/server/send-file" method="post">
    				<div class="form-group inline">
        				<label>File Paths:</label>
        				<input type="text" name="filePaths" placeholder="e.g. C:/send1.txt, C:/send2.txt" />
        				<div class="buttons">
        				
        				<button type="submit">📤 Send to Client</button>
    				</div></div>
        				<div class="form-group inline">
        				<label>Target Client IP:</label>
        				<select name="targetIp" id="targetIpSelect">
            				<option>Loading...</option>
        				</select>
    				</div>				
    				    				
				</form>

        <form action="/server/save-config" method="post">
    				<input type="hidden" name="ip" value="${ip}" />
    				<input type="hidden" name="port" value="${port}" />
    				<input type="hidden" name="serverName" value="${serverName}" />
    				<input type="hidden" name="machineId" value="${machineId}" />
    				<input type="hidden" name="dbUser" value="${dbUser}" />
    				<input type="hidden" name="dbPass" value="${dbPass}" />
    				<input type="hidden" name="dbType" value="${dbType}" />
    				<input type="hidden" name="protocol" value="${protocol}" />
    				<input type="hidden" name="clientIp" value="${clientIp}" />
      <div class="buttons">
                <button type="submit">Save Configuration</button>
            </div>
        </form>
        </div>
    </div>

    <!-- CLIENT FORM -->
    <div class="form-section" id="clientForm">
        <form action="/client/test-connection" method="post" onsubmit="return validateClientForm()">
           
            <div class="form-row">
            <div class="form-group"><label>Host IP:</label><input type="text" name="clientHostIp" value="${clientHostIp}"/></div>
            <div class="form-group"><label>Port:</label><input type="text" name="clientPort" value="${clientPort}"/></div>
           </div>
            <div class="form-row">
            <div class="form-group"><label>Machine Name:</label><input type="text" name="clientMachineName" value="${clientMachineName}"/></div>
            <div class="form-group"><label>Machine ID:</label><input type="text" name="clientMachineId" value="${clientMachineId}"/></div>
           </div>
             <div class="form-row">
            <div class="form-group"><label>DB Username:</label><input type="text" name="clientUser" value="${clientUser}"/></div>
            <div class="form-group"><label>DB Password:</label><input type="password" name="clientPass" value="${clientPass}"/></div>
            </div>
            
            <div class="form-group"><label>DB Type:</label>
                <select name="clientDbType">
                    <option ${clientDbType == 'MySQL' ? 'selected' : ''}>MySQL</option>
                    <option ${clientDbType == 'PostgreSQL' ? 'selected' : ''}>PostgreSQL</option>
                    <option ${clientDbType == 'Oracle' ? 'selected' : ''}>Oracle</option>
                </select>
            </div>
            <div class="form-group"><label>Protocol:</label>
                <select name="clientProtocol">
                    <option ${clientProtocol == 'TCP' ? 'selected' : ''}>TCP</option>
                    <option ${clientProtocol == 'UDP' ? 'selected' : ''}>UDP</option>
                    <option ${clientProtocol == 'HL7' ? 'selected' : ''}>HL7</option>
                </select>
            </div>
                <div class="form-row">
           <div class="form-group inline buttons">
                <label >Server IP:</label>
                <input style="max-width:180px;" type="text" name="pingIp" value="${pingIp}"/>
                <button  formaction="/client/ping-server" formmethod="post" onclick="return validateClientForm()">Ping Server</button>
            </div>
            <div class="buttons">
                <button style="margin-left:40px;" type="submit">Test Connection</button>
                <input type="text" readonly value="${message}" style="margin-left:10px; width: 180px; height: 25px;" placeholder="Status will appear here..."/>
            </div>
            </div>
        </form>
		    <div class="form-row">
			<form action="/client/send-files" method="post">
    			<div class="form-group inline">
        			<label>File Paths:</label>
        			<input type="text" name="filePaths" placeholder="e.g. C:/file1.txt, C:/file2.txt" />
        			<button type="submit">📤 Send to Server</button>
    			</div>
    			   			
		  </form>


        <form action="/client/save-config" method="post">          
    				<input type="hidden" name="clientHostIp" value="${clientHostIp}" />
    				<input type="hidden" name="clientPort" value="${clientPort}" />
    				<input type="hidden" name="clientMachineName" value="${clientMachineName}" />
    				<input type="hidden" name="clientMachineId" value="${clientMachineId}" />
    				<input type="hidden" name="clientUser" value="${clientUser}" />
    				<input type="hidden" name="clientPass" value="${clientPass}" />
    				<input type="hidden" name="clientDbType" value="${clientDbType}" />
    				<input type="hidden" name="clientProtocol" value="${clientProtocol}" />
    				<input type="hidden" name="pingIp" value="${pingIp}" />
            <div class="buttons">
                <button type="submit">Save Configuration</button>
            </div>
            </div>
        </form>
    </div>
    </fieldset>
</div>
</body>
</html>
