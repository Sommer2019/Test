document.getElementById('calculateallPrice').addEventListener('click', function () {
    const form = document.getElementById('myForm');
    const formData = new FormData(form);
    fetch('/precalcPreis', {method: 'POST', body: formData})
        .then(response => response.json())
        .then(data => document.getElementById('preis').textContent = data.preis)
        .catch(error => {
            console.error('Fehler:', error);
            alert('Es gab einen Fehler bei der Berechnung des Preises.');
        });
});