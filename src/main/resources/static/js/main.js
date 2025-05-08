// Main JavaScript file for Healthcare Scheduler

document.addEventListener('DOMContentLoaded', function() {
    // Initialize components
    initializeTooltips();
    initializeFormValidation();
    initializeAppointmentCalendar();
    initializeAvailabilityManagement();

    // Check if user is logged in (token exists)
    checkAuthStatus();
});

// Authentication Functions
function checkAuthStatus() {
    const token = localStorage.getItem('auth_token');
    if (token) {
        // Update UI for logged-in user
        document.querySelectorAll('.logged-out-only').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.logged-in-only').forEach(el => el.style.display = 'block');

        // Get user info
        fetchUserInfo();
    } else {
        // Update UI for logged-out user
        document.querySelectorAll('.logged-in-only').forEach(el => el.style.display = 'none');
        document.querySelectorAll('.logged-out-only').forEach(el => el.style.display = 'block');
    }
}

function login(event) {
    if (event) event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Basic validation
    if (!email || !password) {
        showAlert('Please fill in all fields', 'danger');
        return;
    }

    // API request
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Login failed');
            }
            return response.json();
        })
        .then(data => {
            // Store token
            localStorage.setItem('auth_token', data.token);

            // Redirect based on user role
            if (data.roles.includes('ROLE_DOCTOR')) {
                window.location.href = '/doctor/dashboard';
            } else if (data.roles.includes('ROLE_PATIENT')) {
                window.location.href = '/patient/dashboard';
            } else {
                window.location.href = '/';
            }
        })
        .catch(error => {
            showAlert('Invalid credentials. Please try again.', 'danger');
            console.error('Login error:', error);
        });
}

function register(event) {
    if (event) event.preventDefault();

    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const role = document.querySelector('input[name="role"]:checked').value;

    // Basic validation
    if (!firstName || !lastName || !email || !password || !confirmPassword) {
        showAlert('Please fill in all fields', 'danger');
        return;
    }

    if (password !== confirmPassword) {
        showAlert('Passwords do not match', 'danger');
        return;
    }

    // API request
    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName,
            lastName,
            email,
            password,
            role
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Registration failed');
            }
            return response.json();
        })
        .then(data => {
            showAlert('Registration successful! Please log in.', 'success');

            // Redirect to login page after a delay
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        })
        .catch(error => {
            showAlert('Registration failed. Please try again.', 'danger');
            console.error('Registration error:', error);
        });
}

function logout() {
    // Clear local storage
    localStorage.removeItem('auth_token');

    // Redirect to home page
    window.location.href = '/';
}

function fetchUserInfo() {
    const token = localStorage.getItem('auth_token');

    if (!token) return;

    fetch('/api/auth/user-info', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch user info');
            }
            return response.json();
        })
        .then(data => {
            // Update UI with user info
            document.querySelectorAll('.user-name').forEach(el => {
                el.textContent = `${data.firstName} ${data.lastName}`;
            });

            // Store user info for later use
            localStorage.setItem('user_info', JSON.stringify(data));
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
            // If unauthorized, clear token
            if (error.message.includes('401')) {
                localStorage.removeItem('auth_token');
                checkAuthStatus();
            }
        });
}

// Appointment Functions
function initializeAppointmentCalendar() {
    const calendar = document.getElementById('appointment-calendar');
    if (!calendar) return;

    // Get current date
    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth();

    // Create calendar for current month
    renderCalendar(calendar, currentYear, currentMonth);

    // Set up navigation
    document.getElementById('prev-month')?.addEventListener('click', () => {
        let year = parseInt(calendar.dataset.year);
        let month = parseInt(calendar.dataset.month) - 1;

        if (month < 0) {
            month = 11;
            year--;
        }

        renderCalendar(calendar, year, month);
    });

    document.getElementById('next-month')?.addEventListener('click', () => {
        let year = parseInt(calendar.dataset.year);
        let month = parseInt(calendar.dataset.month) + 1;

        if (month > 11) {
            month = 0;
            year++;
        }

        renderCalendar(calendar, year, month);
    });
}

function renderCalendar(calendar, year, month) {
    if (!calendar) return;

    // Store current view
    calendar.dataset.year = year;
    calendar.dataset.month = month;

    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'];

    // Update header
    const calendarTitle = document.querySelector('.calendar-title');
    if (calendarTitle) {
        calendarTitle.textContent = `${monthNames[month]} ${year}`;
    }

    // Clear existing grid
    const calendarGrid = document.querySelector('.calendar-grid');
    if (!calendarGrid) return;

    calendarGrid.innerHTML = '';

    // Add day headers
    const dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    dayNames.forEach(day => {
        const dayHeader = document.createElement('div');
        dayHeader.className = 'calendar-day-header';
        dayHeader.textContent = day;
        calendarGrid.appendChild(dayHeader);
    });

    // Get first day of month and number of days
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    // Add empty cells for days before start of month
    for (let i = 0; i < firstDay; i++) {
        const emptyDay = document.createElement('div');
        emptyDay.className = 'calendar-day empty';
        calendarGrid.appendChild(emptyDay);
    }

    // Add days of month
    for (let day = 1; day <= daysInMonth; day++) {
        const dayCell = document.createElement('div');
        dayCell.className = 'calendar-day';

        const dayHeader = document.createElement('div');
        dayHeader.className = 'calendar-day-header';
        dayHeader.textContent = day;

        const dayContent = document.createElement('div');
        dayContent.className = 'calendar-day-content';

        dayCell.appendChild(dayHeader);
        dayCell.appendChild(dayContent);

        // Add click handler to select date
        dayCell.addEventListener('click', () => {
            const selectedDate = new Date(year, month, day);
            selectDate(selectedDate);
        });

        calendarGrid.appendChild(dayCell);
    }

    // After rendering, fetch appointments for this month
    fetchAppointmentsForMonth(year, month);
}

function selectDate(date) {
    // Format date for display
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    const formattedDate = date.toLocaleDateString('en-US', options);

    // Update selected date display
    const selectedDateEl = document.getElementById('selected-date');
    if (selectedDateEl) {
        selectedDateEl.textContent = formattedDate;
        selectedDateEl.dataset.date = date.toISOString().split('T')[0];
    }

    // Fetch available slots for this date
    fetchAvailableSlotsForDate(date);
}

function fetchAvailableSlotsForDate(date) {
    const token = localStorage.getItem('auth_token');
    if (!token) return;

    const formattedDate = date.toISOString().split('T')[0];
    const specialtyId = document.getElementById('specialty')?.value;
    const doctorId = document.getElementById('doctor')?.value;

    let url = `/api/availability-slots?date=${formattedDate}`;
    if (specialtyId) url += `&specialtyId=${specialtyId}`;
    if (doctorId) url += `&doctorId=${doctorId}`;

    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch available slots');
            }
            return response.json();
        })
        .then(data => {
            renderAvailableSlots(data, date);
        })
        .catch(error => {
            console.error('Error fetching available slots:', error);
            showAlert('Failed to load available appointment slots', 'danger');
        });
}

function renderAvailableSlots(slots, date) {
    const slotsContainer = document.getElementById('available-slots');
    if (!slotsContainer) return;

    // Clear existing slots
    slotsContainer.innerHTML = '';

    if (slots.length === 0) {
        slotsContainer.innerHTML = '<p>No available slots for this date. Please select another date.</p>';
        return;
    }

    // Sort slots by time
    slots.sort((a, b) => {
        return new Date(a.startTime) - new Date(b.startTime);
    });

    // Group by doctor
    const doctorSlots = {};
    slots.forEach(slot => {
        if (!doctorSlots[slot.doctorId]) {
            doctorSlots[slot.doctorId] = {
                doctorName: `${slot.doctorFirstName} ${slot.doctorLastName}`,
                specialty: slot.specialty,
                slots: []
            };
        }
        doctorSlots[slot.doctorId].slots.push(slot);
    });

    // Create slot elements
    for (const doctorId in doctorSlots) {
        const doctorInfo = doctorSlots[doctorId];

        const doctorHeader = document.createElement('div');
        doctorHeader.className = 'doctor-header';
        doctorHeader.innerHTML = `
            <h3>${doctorInfo.doctorName}</h3>
            <p>${doctorInfo.specialty}</p>
        `;
        slotsContainer.appendChild(doctorHeader);

        doctorInfo.slots.forEach(slot => {
            const slotEl = document.createElement('div');
            slotEl.className = 'appointment-slot appointment-slot-available';

            // Format times for display
            const startTime = new Date(slot.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            const endTime = new Date(slot.endTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

            slotEl.innerHTML = `
                <div class="slot-time">${startTime} - ${endTime}</div>
                <button class="btn book-appointment" data-slot-id="${slot.id}">Book</button>
            `;

            slotEl.querySelector('.book-appointment').addEventListener('click', () => {
                bookAppointment(slot.id);
            });

            slotsContainer.appendChild(slotEl);
        });
    }
}

function bookAppointment(slotId) {
    const token = localStorage.getItem('auth_token');
    if (!token) {
        showAlert('Please log in to book an appointment', 'warning');
        window.location.href = '/login';
        return;
    }

    // Optional: Get reason for visit
    const reason = prompt('Please enter the reason for your visit (optional):');

    fetch('/api/appointments', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            availabilitySlotId: slotId,
            reason: reason || 'Regular checkup'
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to book appointment');
            }
            return response.json();
        })
        .then(data => {
            showAlert('Appointment booked successfully!', 'success');

            // Reload available slots
            const selectedDateEl = document.getElementById('selected-date');
            if (selectedDateEl && selectedDateEl.dataset.date) {
                const date = new Date(selectedDateEl.dataset.date);
                fetchAvailableSlotsForDate(date);
            }

            // Optionally redirect to appointments page
            setTimeout(() => {
                window.location.href = '/appointment/list';
            }, 2000);
        })
        .catch(error => {
            console.error('Error booking appointment:', error);
            showAlert('Failed to book appointment. Please try again.', 'danger');
        });
}

function fetchAppointmentsForMonth(year, month) {
    const token = localStorage.getItem('auth_token');
    if (!token) return;

    // Get first and last day of month
    const firstDay = new Date(year, month, 1).toISOString().split('T')[0];
    const lastDay = new Date(year, month + 1, 0).toISOString().split('T')[0];

    fetch(`/api/appointments?startDate=${firstDay}&endDate=${lastDay}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch appointments');
            }
            return response.json();
        })
        .then(data => {
            renderAppointmentsOnCalendar(data);
        })
        .catch(error => {
            console.error('Error fetching appointments:', error);
        });
}

function renderAppointmentsOnCalendar(appointments) {
    // Group appointments by date
    const appointmentsByDate = {};

    appointments.forEach(appointment => {
        const date = new Date(appointment.startTime);
        const day = date.getDate();

        if (!appointmentsByDate[day]) {
            appointmentsByDate[day] = [];
        }

        appointmentsByDate[day].push(appointment);
    });

    // Add appointment events to calendar days
    const calendarDays = document.querySelectorAll('.calendar-day:not(.empty)');

    calendarDays.forEach((dayEl, index) => {
        const day = index + 1;
        const dayContent = dayEl.querySelector('.calendar-day-content');

        if (appointmentsByDate[day]) {
            appointmentsByDate[day].forEach(appointment => {
                const appointmentEl = document.createElement('div');
                appointmentEl.className = 'calendar-event';

                const time = new Date(appointment.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

                // Determine if user is patient or doctor
                const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}');
                let appointmentText = '';

                if (userInfo.roles && userInfo.roles.includes('ROLE_DOCTOR')) {
                    appointmentEl.textContent = `${time} - ${appointment.patientName || 'Patient'}`;
                } else {
                    appointmentEl.textContent = `${time} - Dr. ${appointment.doctorName || 'Doctor'}`;
                }

                appointmentEl.addEventListener('click', (e) => {
                    e.stopPropagation();
                    window.location.href = `/appointment/detail?id=${appointment.id}`;
                });

                dayContent.appendChild(appointmentEl);
            });
        }
    });
}

// Doctor Availability Management
function initializeAvailabilityManagement() {
    const availabilityForm = document.getElementById('availability-form');
    if (!availabilityForm) return;

    availabilityForm.addEventListener('submit', (e) => {
        e.preventDefault();
        saveAvailability();
    });

    // Load existing availability
    loadAvailability();
}

function loadAvailability() {
    const token = localStorage.getItem('auth_token');
    if (!token) return;

    fetch('/api/availability-slots/doctor', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch availability');
            }
            return response.json();
        })
        .then(data => {
            renderAvailability(data);
        })
        .catch(error => {
            console.error('Error fetching availability:', error);
            showAlert('Failed to load availability data', 'danger');
        });
}

function renderAvailability(slots) {
    const container = document.getElementById('availability-slots');
    if (!container) return;

    // Clear existing slots
    container.innerHTML = '';

    if (slots.length === 0) {
        container.innerHTML = '<p>No availability slots set. Add some below.</p>';
        return;
    }

    // Group by date
    const slotsByDate = {};
    slots.forEach(slot => {
        const date = new Date(slot.startTime).toISOString().split('T')[0];
        if (!slotsByDate[date]) {
            slotsByDate[date] = [];
        }
        slotsByDate[date].push(slot);
    });

    // Sort dates
    const sortedDates = Object.keys(slotsByDate).sort();

    // Create elements for each date
    sortedDates.forEach(date => {
        const dateHeader = document.createElement('h3');
        dateHeader.textContent = new Date(date).toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
        container.appendChild(dateHeader);

        // Sort slots by time
        slotsByDate[date].sort((a, b) => {
            return new Date(a.startTime) - new Date(b.startTime);
        });

        // Create slot elements
        slotsByDate[date].forEach(slot => {
            const slotEl = document.createElement('div');
            slotEl.className = 'appointment-slot';

            // Check if slot is booked
            if (slot.booked) {
                slotEl.classList.add('appointment-slot-booked');
            } else {
                slotEl.classList.add('appointment-slot-available');
            }

            // Format times for display
            const startTime = new Date(slot.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            const endTime = new Date(slot.endTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

            slotEl.innerHTML = `
                <div class="slot-time">${startTime} - ${endTime}</div>
                <div class="slot-status">${slot.booked ? 'Booked' : 'Available'}</div>
                <div class="slot-actions">
                    ${!slot.booked ? `<button class="btn btn-danger btn-sm delete-slot" data-slot-id="${slot.id}">Delete</button>` : ''}
                </div>
            `;

            // Add delete handler
            const deleteBtn = slotEl.querySelector('.delete-slot');
            if (deleteBtn) {
                deleteBtn.addEventListener('click', () => {
                    deleteAvailabilitySlot(slot.id);
                });
            }

            container.appendChild(slotEl);
        });
    });
}

function saveAvailability() {
    const token = localStorage.getItem('auth_token');
    if (!token) return;

    const date = document.getElementById('availability-date').value;
    const startTime = document.getElementById('availability-start-time').value;
    const endTime = document.getElementById('availability-end-time').value;

    // Basic validation
    if (!date || !startTime || !endTime) {
        showAlert('Please fill in all fields', 'danger');
        return;
    }

    // Create datetime strings
    const startDateTime = `${date}T${startTime}:00`;
    const endDateTime = `${date}T${endTime}:00`;

    // Validate times
    if (new Date(startDateTime) >= new Date(endDateTime)) {
        showAlert('End time must be after start time', 'danger');
        return;
    }

    fetch('/api/availability-slots', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            startTime: startDateTime,
            endTime: endDateTime
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to save availability');
            }
            return response.json();
        })
        .then(data => {
            showAlert('Availability saved successfully!', 'success');

            // Reset form
            document.getElementById('availability-form').reset();

            // Reload availability
            loadAvailability();
        })
        .catch(error => {
            console.error('Error saving availability:', error);
            showAlert('Failed to save availability. Please try again.', 'danger');
        });
}

function deleteAvailabilitySlot(slotId) {
    if (!confirm('Are you sure you want to delete this availability slot?')) {
        return;
    }

    const token = localStorage.getItem('auth_token');
    if (!token) return;

    fetch(`/api/availability-slots/${slotId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete availability slot');
            }
            return response.text();
        })
        .then(() => {
            showAlert('Availability slot deleted successfully!', 'success');
            loadAvailability();
        })
        .catch(error => {
            console.error('Error deleting availability slot:', error);
            showAlert('Failed to delete availability slot. Please try again.', 'danger');
        });
}

// Medical Records Functions
function fetchPatientRecords() {
    const token = localStorage.getItem('auth_token');
    if (!token) return;

    fetch('/api/medical-records', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch medical records');
            }
            return response.json();
        })
        .then(data => {
            renderMedicalRecords(data);
        })
        .catch(error => {
            console.error('Error fetching medical records:', error);
            showAlert('Failed to load medical records', 'danger');
        });
}

function renderMedicalRecords(records) {
    const container = document.getElementById('medical-records');
    if (!container) return;

    // Clear existing records
    container.innerHTML = '';

    if (records.length === 0) {
        container.innerHTML = '<p>No medical records found.</p>';
        return;
    }

    // Sort records by date (newest first)
    records.sort((a, b) => {
        return new Date(b.date) - new Date(a.date);
    });

    // Create record elements
    records.forEach(record => {
        const recordEl = document.createElement('div');
        recordEl.className = 'card mb-3';

        const formattedDate = new Date(record.date).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        recordEl.innerHTML = `
            <div class="card-header">
                <h5 class="card-title">${record.title}</h5>
                <div class="text-muted">Date: ${formattedDate}</div>
                <div class="text-muted">Doctor: Dr. ${record.doctorName}</div>
            </div>
            <div class="card-body">
                <p>${record.description}</p>
                <div class="mt-3">
                    <strong>Diagnosis:</strong> ${record.diagnosis}
                </div>
                <div class="mt-2">
                    <strong>Treatment:</strong> ${record.treatment}
                </div>
                <div class="mt-2">
                    <strong>Prescription:</strong> ${record.prescription || 'None'}
                </div>
            </div>
            <div class="card-footer">
                <button class="btn btn-primary view-record" data-record-id="${record.id}">View Details</button>
            </div>
        `;

        recordEl.querySelector('.view-record').addEventListener('click', () => {
            window.location.href = `/medical-record/detail?id=${record.id}`;
        });

        container.appendChild(recordEl);
    });
}

// Utility Functions
function initializeTooltips() {
    // If you have tooltip functionality, initialize it here
    const tooltips = document.querySelectorAll('[data-toggle="tooltip"]');
    tooltips.forEach(tooltip => {
        // Initialize tooltip
    });
}

function initializeFormValidation() {
    // Get all forms with the 'needs-validation' class
    const forms = document.querySelectorAll('.needs-validation');

    // Loop over them and prevent submission if invalid
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            form.classList.add('was-validated');
        }, false);
    });
}

function showAlert(message, type) {
    const alertsContainer = document.getElementById('alerts-container');
    if (!alertsContainer) return;

    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    `;

    alertsContainer.appendChild(alert);

    // Auto dismiss after 5 seconds
    setTimeout(() => {
        alert.classList.remove('show');
        setTimeout(() => {
            alertsContainer.removeChild(alert);
        }, 300);
    }, 5000);
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Login form submit
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', login);
    }

    // Register form submit
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', register);
    }

    // Logout button
    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', logout);
    }

    // Specialty filter (for appointment booking)
    const specialtySelect = document.getElementById('specialty');
    if (specialtySelect) {
        specialtySelect.addEventListener('change', () => {
            const selectedDateEl = document.getElementById('selected-date');
            if (selectedDateEl && selectedDateEl.dataset.date) {
                const date = new Date(selectedDateEl.dataset.date);
                fetchAvailableSlotsForDate(date);
            }
        });
    }

    // Doctor filter (for appointment booking)
    const doctorSelect = document.getElementById('doctor');
    if (doctorSelect) {
        doctorSelect.addEventListener('change', () => {
            const selectedDateEl = document.getElementById('selected-date');
            if (selectedDateEl && selectedDateEl.dataset.date) {
                const date = new Date(selectedDateEl.dataset.date);
                fetchAvailableSlotsForDate(date);
            }
        });
    }

    // Medical records page
    if (document.getElementById('medical-records')) {
        fetchPatientRecords();
    }
});