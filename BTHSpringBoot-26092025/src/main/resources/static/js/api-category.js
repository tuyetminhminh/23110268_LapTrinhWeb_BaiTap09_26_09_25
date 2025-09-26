const API = '/api/categories';

async function loadCategories() {
  const res = await fetch(API);
  const json = await res.json();
  const rows = (json.body || []).map(c => `
    <tr>
      <td>${c.categoryId}</td>
      <td>${c.categoryName}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="editCat(${c.categoryId})">Edit</button>
        <button class="btn btn-sm btn-danger" onclick="delCat(${c.categoryId})">Delete</button>
      </td>
    </tr>`).join('');
  document.querySelector('#catTable tbody').innerHTML = rows;
}

async function createCategory() {
  const name = document.querySelector('#catName').value.trim();
  const res = await fetch(API, {
    method:'POST',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify({categoryName:name})
  });
  await loadCategories();
}

async function editCat(id) {
  const name = prompt('New name:');
  if(!name) return;
  await fetch(`${API}/${id}`, {
    method:'PUT',
    headers:{'Content-Type':'application/json'},
    body: JSON.stringify({categoryName:name})
  });
  await loadCategories();
}

async function delCat(id) {
  if(!confirm('Delete?')) return;
  await fetch(`${API}/${id}`, { method:'DELETE' });
  await loadCategories();
}

document.addEventListener('DOMContentLoaded', loadCategories);
