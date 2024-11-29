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
    document.getElementById('deleteContainer').style.display = 'block';
    // Hide the delete button
    var deleteButton = document.getElementById("delete");
    if (deleteButton.style.display === "none") {
        deleteButton.style.display = "inline-block";
    } else {
        deleteButton.style.display = "none";
    }
    var editButton = document.getElementById("edit");
    if (editButton.style.display === "none") {
        editButton.style.display = "inline-block";
    } else {
        editButton.style.display = "none";
    }
}

function hideDelete() {
    document.getElementById('deleteContainer').style.display = 'none';
    var deleteButton = document.getElementById("delete");
    if (deleteButton.style.display === "inline-block") {
        deleteButton.style.display = "none";
    } else {
        deleteButton.style.display = "inline-block";
    }
    var editButton = document.getElementById("edit");
    if (editButton.style.display === "inline-block") {
        editButton.style.display = "none";
    } else {
        editButton.style.display = "inline-block";
    }
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
            const brands = data.brands; // Adjust this based on the structure of your JSON file
            const inputValue = "'" + input.value.trim() + "'";
            if (brands.includes(inputValue)) {
                input.classList.remove('invalid');
                input.classList.add('valid');
            } else {
                input.classList.remove('valid');
                input.classList.add('invalid');
            }
        })
        .catch(error => {
            console.error('Error loading brands:', error);
        });
}

function updatePrice() {
    fetch('/calculatePrice', {
        method: 'GET',
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('preis').innerText = data.price + ' €';
        })
        .catch(error => console.error('Error:', error));
}

function validateInput(input, regex) {
    if (regex.test(input.value)) {
        input.classList.remove('invalid');
        input.classList.add('valid');
    } else {
        input.classList.remove('valid');
        input.classList.add('invalid');
    }
}

kennzeichenInput.addEventListener('input', function () {
    checkKennzeichen(kennzeichenInput, regexKennzeichen);
});

herstellerInput.addEventListener('input', function () {
    validateInputHersteller(herstellerInput);
});

updatePreis.addEventListener('click', function () {
    updatePrice();
});

vornameInput.addEventListener('input', function () {
    validateInput(vornameInput, regexName);
});

nachnameInput.addEventListener('input', function () {
    validateInput(nachnameInput, regexName);
});

typInput.addEventListener('input', function () {
    validateInput(typInput, regexTyp);
});
nachnameInput.addEventListener('input', function () {
    validateInput(nachnameInput, regexName);
});
strasseInput.addEventListener('input', function () {
    validateInput(strasseInput, regexName);
});
hausnummerInput.addEventListener('input', function () {
    validateInput(hausnummerInput, regexHausnummer);
});
plzInput.addEventListener('input', function () {
    validateInput(plzInput, regexPLZ);
});
stadtInput.addEventListener('input', function () {
    validateInput(stadtInput, regexName);
});
bundeslandInput.addEventListener('input', function () {
    validateInput(bundeslandInput, regexName);
});

// Event Listener, um die Mindest- und Höchstwerte der anderen Felder basierend auf dem Beginn zu setzen
startInput.addEventListener('change', function () {
    const startDate = startInput.value;
    endInput.min = startDate;
    createInput.max = startDate;
});

function checkKennzeichen(input, regex) {
    if (regex.test(input.value)) {
        input.classList.remove('invalid');
        input.classList.add('valid');
        fetch('vertrage.json')
            .then(response => response.json())
            .then(data => {
                const kennzeichenExists = data.some(vertrag => vertrag.fahrzeug.amtlichesKennzeichen === input.value.trim());
                if (kennzeichenExists) {
                    input.classList.remove('valid');
                    input.classList.add('invalid');
                } else {
                    input.classList.remove('invalid');
                    input.classList.add('valid');
                }
            })
            .catch(error => {
                console.error('Error loading vertrage:', error);
            });
    } else {
        input.classList.remove('valid');
        input.classList.add('invalid');
    }
}

function toggleEdit() {
    var handledVertrag = document.getElementById("handledVertrag");
    var inputs = handledVertrag.querySelectorAll("input, label, #preiscalc, #kmh");
    inputs.forEach(function (input) {
        input.style.display = "inline-block";
    });
    var editContainer = document.getElementById("editContainer");
    editContainer.style.display = "inline-block";
    var editButton = document.getElementById("edit");
    editButton.style.display = "none";
    var deleteButton = document.getElementById("delete");
    deleteButton.style.display = "none";
}

function hideEdit() {
    var handledVertrag = document.getElementById("handledVertrag");
    var inputs = handledVertrag.querySelectorAll("#editContainer, input, label, #preiscalc, #kmh");
    inputs.forEach(function (input) {
        input.style.display = "none";
    });

    var editContainer = document.getElementById("editContainer");
    editContainer.style.display = "none";
    var deleteButton = document.getElementById("delete");
    deleteButton.style.display = "inline-block";
    var editButton = document.getElementById("edit");
    editButton.style.display = "inline-block";
}

// Initiale Einstellung der Mindest- und Höchstwerte basierend auf dem heutigen Datum
