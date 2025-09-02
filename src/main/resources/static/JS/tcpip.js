function toggleMode(mode){
  document.getElementById('serverForm').classList.toggle('hidden', mode !== 'server');
  document.getElementById('clientForm').classList.toggle('hidden', mode !== 'client');
}

async function pingIp(btn, formId, inputName){
  const form = document.getElementById(formId);
  const ip = form.querySelector(`[name="${inputName}"]`).value.trim();
  const statusEl = btn.parentElement.querySelector('[data-role="ping-status"]');
  statusEl.textContent = 'Pinging...';
  const body = new URLSearchParams({ ip });
  const res = await fetch('/api/ping', { method: 'POST', headers:{'Content-Type':'application/x-www-form-urlencoded'}, body });
  const data = await res.json();
  statusEl.textContent = data.reachable ? `Success (${data.rttMs} ms)` : `Failed`;
}

async function testServer(btn){
  const form = document.getElementById('serverForm');
  const saveBtn = form.querySelector('[data-role="save-btn"]');
  saveBtn.disabled = true;
  const text = form.querySelector('textarea[name="status"]');
  text.value = 'Starting server test...\n';
  const fd = new FormData(form);
  const res = await fetch('/api/server/test', { method: 'POST', body: new URLSearchParams(fd) });
  const data = await res.json();
  text.value += (data.log || '') + '\n';
  if (data.ok){
    saveBtn.disabled = false;
  }
}

async function testClient(btn){
  const form = document.getElementById('clientForm');
  const saveBtn = form.querySelector('[data-role="save-btn"]');
  saveBtn.disabled = true;
  const text = form.querySelector('textarea[name="status"]');
  text.value = 'Starting client test...\n';
  const fd = new FormData(form);
  const res = await fetch('/api/client/test', { method: 'POST', body: new URLSearchParams(fd) });
  const data = await res.json();
  text.value += (data.log || '') + '\n';
  if (data.ok){
    saveBtn.disabled = false;
  }
}

async function saveServer(e){
  e.preventDefault();
  const form = document.getElementById('serverForm');
  const saveStatus = form.querySelector('[data-role="save-status"]');
  const fd = new FormData(form);
  const res = await fetch('/api/server/save', { method: 'POST', body: new URLSearchParams(fd) });
  const data = await res.json();
  saveStatus.textContent = data.message || 'Saved';
  if (data.ok){
    form.reset();
    form.querySelector('[data-role="save-btn"]').disabled = true;
  }
}

async function saveClient(e){
  e.preventDefault();
  const form = document.getElementById('clientForm');
  const saveStatus = form.querySelector('[data-role="save-status"]');
  const fd = new FormData(form);
  const res = await fetch('/api/client/save', { method: 'POST', body: new URLSearchParams(fd) });
  const data = await res.json();
  saveStatus.textContent = data.message || 'Saved';
  if (data.ok){
    form.reset();
    form.querySelector('[data-role="save-btn"]').disabled = true;
  }
}
