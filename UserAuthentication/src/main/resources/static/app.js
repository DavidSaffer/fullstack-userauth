function apiRequest(url, method, body) {
    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }).then(response => response.json());
}

function displayMessage(message) {
    const messageArea = document.getElementById('messageArea');
    messageArea.textContent = message;
    messageArea.classList.remove('hidden');
    setTimeout(() => messageArea.classList.add('hidden'), 5000); // Hide message after 5 seconds
}

function login() {
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;
    apiRequest('http://localhost:8080/api/auth/login', 'POST', { username, password })
        .then(data => {
            if (data.success) {
                displayMessage('Login successful');
                showHomePage();
            } else {
                displayMessage('Login Failed: ' + data.data);
            }
        })
        .catch(error => {
            console.error('Login failed', error);
            displayMessage('Login failed: ' + error.message);
        });
}

function signup() {
    const username = document.getElementById('signupUsername').value;
    const password = document.getElementById('signupPassword').value;
    const email = document.getElementById('signupEmail').value;
    const phone = document.getElementById('signupPhone').value;
    apiRequest('http://localhost:8080/api/auth/signup', 'POST', { username, password, email, phone })
        .then(data => {
            if (data.success) {
                displayMessage('Signup successful');
                showHomePage();
            } else {
                displayMessage('Signup failed: ' + data.data);
            }
        })
        .catch(error => {
            console.error('Signup failed', error);
            displayMessage('Signup failed: ' + error.message);
        });
}

function showHomePage() {
    document.getElementById('loginForm').classList.add('hidden');
    document.getElementById('signupForm').classList.add('hidden');
    document.getElementById('homePage').classList.remove('hidden');
}

function logout() {
    document.getElementById('loginForm').classList.remove('hidden');
    document.getElementById('signupForm').classList.remove('hidden');
    document.getElementById('homePage').classList.add('hidden');
}