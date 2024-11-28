// Funktion, um das heutige Datum im Format YYYY-MM-DD zu erhalten
function getTodayDate() {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getMaxBirth() {
    const today = new Date();
    const year = today.getFullYear() - 100;
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

function getMinBirth() {
    const today = new Date();
    const year = today.getFullYear() - 18;
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
function toggleDelete() {
    document.getElementById('delete-container').style.display = 'block';
}

function hideDelete() {
    document.getElementById('delete-container').style.display = 'none';
}
// Setze das heutige Datum als Wert und Mindestdatum für das Datumseingabefeld
const todayDate = getTodayDate();
const minbirth = getMinBirth();
const maxbirth = getMaxBirth();
const birthInput = document.getElementById('birth')
const startInput = document.getElementById('start');
const endInput = document.getElementById('end');
const createInput = document.getElementById('create');
const kennzeichenInput = document.getElementById('kennzeichen');
const herstellerInput = document.getElementById('hersteller');
const vornameInput = document.getElementById('vorname');
const nachnameInput = document.getElementById('nachname');
const typInput = document.getElementById('typ');
const plzInput = document.getElementById('plz');
const hausnummerInput = document.getElementById('hausnummer');
const regexKennzeichen = /^\p{Lu}{1,3}-\p{Lu}{1,2}\d{1,4}[EH]?$/u;
const regexName = /^[a-zA-Z0-9\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$/;
const regexTyp = /^[a-zA-Z0-9\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ]+$/;
const regexPLZ = /^\d{5}$/;
const regexHausnummer = /^\d+[a-zA-Z]?$/;
document.getElementById('calculatePrice').addEventListener('click', function() {
var form = document.getElementById('myForm'); var formData = new FormData(form);
fetch('/createPreis', { method: 'POST', body: formData }) .then(response => response.json()) .then(data => {
document.getElementById('preis').textContent = data.preis; }) .catch(error => { console.error('Fehler:', error);
alert('Es gab einen Fehler bei der Berechnung des Preises.'); }); });
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
function validateInputHersteller(input, messageElement) {
    fetch('brands.json')
        .then(response => response.json())
        .then(data => {
            const brands = data.brands; // Adjust this based on the structure of your JSON file
            const inputValue = "'" + input.value.trim() + "'";
            if (brands.includes(inputValue)) {
                input.classList.remove('invalid');
                input.classList.add('valid');
                messageElement.textContent = '';
            } else {
                input.classList.remove('valid');
                input.classList.add('invalid');
                messageElement.textContent = 'Ungültige Eingabe';
            }
        })
        .catch(error => {
            console.error('Error loading brands:', error);
            messageElement.textContent = 'Error';
            messageElement.style.color = 'red';
        });
}

function validateInput(input, regex, messageElement) {
    if (regex.test(input.value)) {
        input.classList.remove('invalid');
        input.classList.add('valid');
        messageElement.textContent = '';
    } else {
        input.classList.remove('valid');
        input.classList.add('invalid');
        messageElement.textContent = 'Ungültige Eingabe';
    }
}

kennzeichenInput.addEventListener('input', function () {
    validateInput(kennzeichenInput, regexKennzeichen, document.getElementById('kennzeichen-message'));
});

herstellerInput.addEventListener('input', function () {
    validateInputHersteller(herstellerInput, document.getElementById('hersteller-message'));
});

vornameInput.addEventListener('input', function () {
    validateInput(vornameInput, regexName, document.getElementById('vorname-message'));
});

nachnameInput.addEventListener('input', function () {
    validateInput(nachnameInput, regexName, document.getElementById('nachname-message'));
});

typInput.addEventListener('input', function () {
    validateInput(typInput, regexTyp, document.getElementById('typ-message'));
});

plzInput.addEventListener('input', function () {
    validateInput(plzInput, regexPLZ, document.getElementById('plz-message'));
});

hausnummerInput.addEventListener('input', function () {
    validateInput(hausnummerInput, regexHausnummer, document.getElementById('hausnummer-message'));
});

// Event Listener, um die Mindest- und Höchstwerte der anderen Felder basierend auf dem Beginn zu setzen
startInput.addEventListener('change', function () {
    const startDate = startInput.value;
    endInput.min = startDate;
    createInput.max = startDate;
});

// Initiale Einstellung der Mindest- und Höchstwerte basierend auf dem heutigen Datum
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