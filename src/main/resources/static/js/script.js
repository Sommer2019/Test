function getTodayDate() {
    const today = new Date();
    return `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
}

function getMaxBirth() {
    const today = new Date();
    return `${today.getFullYear() - 100}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
}

function getMinBirth() {
    const today = new Date();
    return `${today.getFullYear() - 18}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
}

function toggleDelete() {
    const deleteContainer = document.getElementById('deleteContainer');
    const deleteButton = document.getElementById("delete");
    const editButton = document.getElementById("edit");
    deleteContainer.style.display = 'block';
    deleteButton.style.display = deleteButton.style.display === "none" ? "inline-block" : "none";
    editButton.style.display = editButton.style.display === "none" ? "inline-block" : "none";
}

function hideDelete() {
    const deleteContainer = document.getElementById('deleteContainer');
    const deleteButton = document.getElementById("delete");
    const editButton = document.getElementById("edit");
    deleteContainer.style.display = 'none';
    deleteButton.style.display = deleteButton.style.display === "inline-block" ? "none" : "inline-block";
    editButton.style.display = editButton.style.display === "inline-block" ? "none" : "inline-block";
}

const todayDate = getTodayDate();
const minbirth = getMinBirth();
const maxbirth = getMaxBirth();
const birthInput = document.getElementById('birth');
const startInput = document.getElementById('start');
const endInput = document.getElementById('end');
const createInput = document.getElementById('create');
const kennzeichenInput = document.getElementById('kennzeichen');
const herstellerInput = document.getElementById('hersteller');
const updatePreis = document.getElementById('preisedit');
const vornameInput = document.getElementById('vorname');
const nachnameInput = document.getElementById('nachname');
const typInput = document.getElementById('typ');
const strasseInput = document.getElementById('strasse');
const hausnummerInput = document.getElementById('hausnummer');
const plzInput = document.getElementById('plz');
const stadtInput = document.getElementById('stadt');
const bundeslandInput = document.getElementById('bundesland');
const regexKennzeichen = /^\p{Lu}{1,3}-\p{Lu}{1,2}\d{1,4}[EH]?$/u;
const regexName = /^[a-zA-Z0-9\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ'-]+$/;
const regexTyp = /^[a-zA-Z0-9\s-äöüÄÖÜçéèêáàâíìîóòôúùûñÑ]+$/;
const regexPLZ = /^\d{5}$/;
const regexHausnummer = /^\d+[a-zA-Z]?$/;

function validateInputHersteller(input) {
    fetch('brands.json')
        .then(response => response.json())
        .then(data => {
            const brands = data.brands;
            const inputValue = `'${input.value.trim()}'`;
            input.classList.toggle('invalid', !brands.includes(inputValue));
            input.classList.toggle('valid', brands.includes(inputValue));
        })
        .catch(error => console.error('Error loading brands:', error));
}

function updatePrice() {
    fetch('/calculatePrice', { method: 'GET' })
        .then(response => response.json())
        .then(data => document.getElementById('preis').innerText = `${data.price} €`)
        .catch(error => console.error('Error:', error));
}

function validateInput(input, regex) {
    input.classList.toggle('invalid', !regex.test(input.value));
    input.classList.toggle('valid', regex.test(input.value));
}

kennzeichenInput.addEventListener('input', () => checkKennzeichen(kennzeichenInput, regexKennzeichen));
herstellerInput.addEventListener('input', () => validateInputHersteller(herstellerInput));
updatePreis.addEventListener('click', updatePrice);
vornameInput.addEventListener('input', () => validateInput(vornameInput, regexName));
nachnameInput.addEventListener('input', () => validateInput(nachnameInput, regexName));
typInput.addEventListener('input', () => validateInput(typInput, regexTyp));
strasseInput.addEventListener('input', () => validateInput(strasseInput, regexName));
hausnummerInput.addEventListener('input', () => validateInput(hausnummerInput, regexHausnummer));
plzInput.addEventListener('input', () => validateInput(plzInput, regexPLZ));
stadtInput.addEventListener('input', () => validateInput(stadtInput, regexName));
bundeslandInput.addEventListener('input', () => validateInput(bundeslandInput, regexName));

startInput.addEventListener('change', () => {
    const startDate = startInput.value;
    endInput.min = startDate;
    createInput.max = startDate;
});

function checkKennzeichen(input, regex) {
    if (regex.test(input.value)) {
        fetch('vertrage.json')
            .then(response => response.json())
            .then(data => {
                const kennzeichenExists = data.some(vertrag => vertrag.fahrzeug.amtlichesKennzeichen === input.value.trim());
                input.classList.toggle('invalid', kennzeichenExists);
                input.classList.toggle('valid', !kennzeichenExists);
            })
            .catch(error => console.error('Error loading vertrage:', error));
    } else {
        input.classList.add('invalid');
        input.classList.remove('valid');
    }
}

function toggleEdit() {
    const handledVertrag = document.getElementById("handledVertrag");
    const inputs = handledVertrag.querySelectorAll("input, label, #preiscalc, #kmh");
    inputs.forEach(input => input.style.display = "inline-block");
    document.getElementById("editContainer").style.display = "inline-block";
    document.getElementById("edit").style.display = "none";
    document.getElementById("delete").style.display = "none";
}

function hideEdit() {
    const handledVertrag = document.getElementById("handledVertrag");
    const inputs = handledVertrag.querySelectorAll("#editContainer, input, label, #preiscalc, #kmh");
    inputs.forEach(input => input.style.display = "none");
    document.getElementById("editContainer").style.display = "none";
    document.getElementById("delete").style.display = "inline-block";
    document.getElementById("edit").style.display = "inline-block";
}