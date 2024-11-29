document.getElementById('calculatePrice').addEventListener('click', function () {
    var form = document.getElementById('myForm');
    var formData = new FormData(form);
    fetch('/createPreis', {method: 'POST', body: formData}).then(response => response.json()).then(data => {
        document.getElementById('preis').textContent = data.preis;
    }).catch(error => {
        console.error('Fehler:', error);
        alert('Es gab einen Fehler bei der Berechnung des Preises.');
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('myForm');
    const calculateButton = document.getElementById('calculateButton');
    const preisDiv = document.getElementById('preis');

    calculateButton.addEventListener('click', function () {
        const formData = new FormData(form);
        const jsonData = {};
        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch('/calculatePreis', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
            .then(response => response.json())
            .then(data => {
                preisDiv.textContent = data.preis;
            })
            .catch(error => console.error('Error:', error));
    });
});
startInput.value = todayDate;
startInput.min = todayDate;
birthInput.min = maxbirth;
birthInput.value = minbirth;
birthInput.max = minbirth;
endInput.min = todayDate;
endInput.value = todayDate;
endInput.min = todayDate;
createInput.value = todayDate;
createInput.max = todayDate;