// Global variables
let currentRestaurant = null;
let currentOrders = [];
let currentMenuItems = [];
let currentEditingItem = null;

// API Base URL - adjust this to match your backend server
const API_BASE_URL = 'http://localhost:8080/api';

// Authentication token (in real app, this would be stored securely)
let authToken = localStorage.getItem('authToken') || '';

// DOM Elements
const navItems = document.querySelectorAll('.nav-item');
const contentSections = document.querySelectorAll('.content-section');
const pageTitle = document.getElementById('page-title');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    loadDashboardData();
});

// Initialize application
function initializeApp() {
    // Check if user is authenticated
    if (!authToken) {
        // In a real app, redirect to login page
        console.warn('No auth token found. User should be redirected to login.');
    }
    
    // Load initial data
    loadRestaurantProfile();
    loadMenuItems();
    loadOrders();
}

// Setup event listeners
function setupEventListeners() {
    // Navigation
    navItems.forEach(item => {
        item.addEventListener('click', handleNavigation);
    });
    
    // Modal controls
    const modal = document.getElementById('menu-modal');
    const addItemBtn = document.getElementById('add-item-btn');
    const closeBtn = document.querySelector('.close');
    const cancelBtn = document.getElementById('cancel-btn');
    
    addItemBtn.addEventListener('click', () => openMenuItemModal());
    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);
    
    // Click outside modal to close
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });
    
    // Forms
    document.getElementById('menu-item-form').addEventListener('submit', handleMenuItemSubmit);
    document.getElementById('restaurant-form').addEventListener('submit', handleRestaurantProfileSubmit);
    
    // Order status filter
    document.getElementById('status-filter').addEventListener('change', filterOrders);
    
    // Logout
    document.querySelector('.logout-btn').addEventListener('click', handleLogout);
}

// Navigation handler
function handleNavigation(e) {
    e.preventDefault();
    
    const targetSection = e.currentTarget.dataset.section;
    
    // Update active nav item
    navItems.forEach(item => item.classList.remove('active'));
    e.currentTarget.classList.add('active');
    
    // Show target section
    contentSections.forEach(section => section.classList.remove('active'));
    document.getElementById(targetSection).classList.add('active');
    
    // Update page title
    const titles = {
        'dashboard': 'Dashboard',
        'orders': 'Order Management',
        'menu': 'Menu Management',
        'profile': 'Restaurant Profile',
        'analytics': 'Analytics & Reports'
    };
    pageTitle.textContent = titles[targetSection] || 'Dashboard';
    
    // Load section-specific data
    switch(targetSection) {
        case 'dashboard':
            loadDashboardData();
            break;
        case 'orders':
            loadOrders();
            break;
        case 'menu':
            loadMenuItems();
            break;
        case 'profile':
            loadRestaurantProfile();
            break;
        case 'analytics':
            loadAnalytics();
            break;
    }
}

// API Helper functions
async function apiRequest(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${authToken}`,
            ...options.headers
        },
        ...options
    };
    
    try {
        const response = await fetch(url, config);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('API request failed:', error);
        showAlert('API request failed: ' + error.message, 'error');
        throw error;
    }
}

// Load dashboard data
async function loadDashboardData() {
    try {
        // Load orders for stats
        const orders = await apiRequest('/orders');
        const today = new Date().toDateString();
        
        const todayOrders = orders.filter(order => 
            new Date(order.createdAt).toDateString() === today
        );
        
        const pendingOrders = orders.filter(order => 
            order.status === 'PENDING' || order.status === 'ACCEPTED'
        );
        
        const todayRevenue = todayOrders.reduce((sum, order) => sum + order.totalAmount, 0);
        
        // Update stats
        document.getElementById('total-orders').textContent = todayOrders.length;
        document.getElementById('total-revenue').textContent = `RM ${todayRevenue.toFixed(2)}`;
        document.getElementById('pending-orders').textContent = pendingOrders.length;
        document.getElementById('menu-items').textContent = currentMenuItems.length;
        
        // Load recent orders
        loadRecentOrders(orders.slice(0, 10));
        
    } catch (error) {
        console.error('Failed to load dashboard data:', error);
    }
}

// Load recent orders for dashboard
function loadRecentOrders(orders) {
    const tbody = document.getElementById('recent-orders-tbody');
    tbody.innerHTML = '';
    
    orders.forEach(order => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>#${order.orderId}</td>
            <td>Customer ${order.customerId}</td>
            <td>${order.items ? order.items.length : 0} items</td>
            <td>RM ${order.totalAmount.toFixed(2)}</td>
            <td><span class="order-status status-${order.status.toLowerCase()}">${order.status}</span></td>
            <td>${formatDateTime(order.createdAt)}</td>
        `;
        tbody.appendChild(row);
    });
}

// Load restaurant profile
async function loadRestaurantProfile() {
    try {
        const restaurants = await apiRequest('/restaurants');
        if (restaurants.length > 0) {
            currentRestaurant = restaurants[0]; // Assuming first restaurant belongs to current user
            populateRestaurantForm(currentRestaurant);
            document.getElementById('restaurant-name').textContent = currentRestaurant.name;
        }
    } catch (error) {
        console.error('Failed to load restaurant profile:', error);
    }
}

// Populate restaurant form
function populateRestaurantForm(restaurant) {
    document.getElementById('restaurant-name-input').value = restaurant.name || '';
    document.getElementById('restaurant-description').value = restaurant.description || '';
    document.getElementById('opening-time').value = restaurant.openingTime || '';
    document.getElementById('closing-time').value = restaurant.closingTime || '';
    document.getElementById('delivery-radius').value = restaurant.deliveryRadius || '';
    document.getElementById('logo-url').value = restaurant.logoUrl || '';
    document.getElementById('is-active').checked = restaurant.isActive || false;
}

// Handle restaurant profile form submission
async function handleRestaurantProfileSubmit(e) {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('restaurant-name-input').value,
        description: document.getElementById('restaurant-description').value,
        openingTime: document.getElementById('opening-time').value,
        closingTime: document.getElementById('closing-time').value,
        deliveryRadius: parseInt(document.getElementById('delivery-radius').value),
        logoUrl: document.getElementById('logo-url').value,
        isActive: document.getElementById('is-active').checked
    };
    
    try {
        if (currentRestaurant && currentRestaurant.restaurantId) {
            // Update existing restaurant
            await apiRequest(`/restaurants/${currentRestaurant.restaurantId}`, {
                method: 'PUT',
                body: JSON.stringify(formData)
            });
        } else {
            // Create new restaurant
            const newRestaurant = await apiRequest('/restaurants', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            currentRestaurant = newRestaurant;
        }
        
        showAlert('Restaurant profile updated successfully!', 'success');
        document.getElementById('restaurant-name').textContent = formData.name;
        
    } catch (error) {
        console.error('Failed to update restaurant profile:', error);
        showAlert('Failed to update restaurant profile', 'error');
    }
}

// Load menu items
async function loadMenuItems() {
    try {
        const menuItems = await apiRequest('/fooditems');
        currentMenuItems = menuItems;
        displayMenuItems(menuItems);
    } catch (error) {
        console.error('Failed to load menu items:', error);
        showAlert('Failed to load menu items', 'error');
    }
}

// Display menu items
function displayMenuItems(items) {
    const menuGrid = document.getElementById('menu-grid');
    menuGrid.innerHTML = '';
    
    items.forEach(item => {
        const itemCard = createMenuItemCard(item);
        menuGrid.appendChild(itemCard);
    });
}

// Create menu item card
function createMenuItemCard(item) {
    const card = document.createElement('div');
    card.className = 'menu-item-card';
    card.innerHTML = `
        <img src="${item.imageUrl || 'https://via.placeholder.com/300x200?text=No+Image'}" 
             alt="${item.name}" class="menu-item-image" 
             onerror="this.src='https://via.placeholder.com/300x200?text=No+Image'">
        <div class="menu-item-content">
            <div class="menu-item-header">
                <h4 class="menu-item-name">${item.name}</h4>
                <span class="menu-item-price">RM ${item.price.toFixed(2)}</span>
            </div>
            <span class="menu-item-category">${item.category}</span>
            <p class="menu-item-description">${item.description || 'No description available'}</p>
            <div class="availability-toggle">
                <div class="toggle-switch ${item.isAvailable ? 'active' : ''}" 
                     onclick="toggleAvailability(${item.foodId})">
                    <div class="toggle-slider"></div>
                </div>
                <span>${item.isAvailable ? 'Available' : 'Unavailable'}</span>
            </div>
            <div class="menu-item-actions">
                <button class="btn btn-primary" onclick="editMenuItem(${item.foodId})">Edit</button>
                <button class="btn btn-danger" onclick="deleteMenuItem(${item.foodId})">Delete</button>
            </div>
        </div>
    `;
    return card;
}

// Toggle item availability
async function toggleAvailability(itemId) {
    try {
        const item = currentMenuItems.find(item => item.foodId === itemId);
        if (!item) return;
        
        const updatedItem = {
            ...item,
            isAvailable: !item.isAvailable
        };
        
        await apiRequest(`/fooditems/${itemId}`, {
            method: 'PUT',
            body: JSON.stringify(updatedItem)
        });
        
        // Update local data
        const index = currentMenuItems.findIndex(item => item.foodId === itemId);
        currentMenuItems[index] = updatedItem;
        
        // Refresh display
        displayMenuItems(currentMenuItems);
        showAlert('Item availability updated!', 'success');
        
    } catch (error) {
        console.error('Failed to toggle availability:', error);
        showAlert('Failed to update availability', 'error');
    }
}

// Open menu item modal
function openMenuItemModal(item = null) {
    currentEditingItem = item;
    const modal = document.getElementById('menu-modal');
    const form = document.getElementById('menu-item-form');
    const title = document.getElementById('modal-title');
    
    if (item) {
        title.textContent = 'Edit Menu Item';
        populateMenuItemForm(item);
    } else {
        title.textContent = 'Add New Menu Item';
        form.reset();
        document.getElementById('item-available').checked = true;
    }
    
    modal.style.display = 'block';
}

// Populate menu item form
function populateMenuItemForm(item) {
    document.getElementById('item-name').value = item.name;
    document.getElementById('item-description').value = item.description || '';
    document.getElementById('item-price').value = item.price;
    document.getElementById('item-category').value = item.category;
    document.getElementById('item-image').value = item.imageUrl || '';
    document.getElementById('item-available').checked = item.isAvailable;
}

// Handle menu item form submission
async function handleMenuItemSubmit(e) {
    e.preventDefault();
    
    const formData = {
        name: document.getElementById('item-name').value,
        description: document.getElementById('item-description').value,
        price: parseFloat(document.getElementById('item-price').value),
        category: document.getElementById('item-category').value,
        imageUrl: document.getElementById('item-image').value,
        isAvailable: document.getElementById('item-available').checked,
        restaurantId: currentRestaurant ? currentRestaurant.restaurantId : 1 // Default to 1 if no restaurant
    };
    
    try {
        if (currentEditingItem) {
            // Update existing item
            await apiRequest(`/fooditems/${currentEditingItem.foodId}`, {
                method: 'PUT',
                body: JSON.stringify({...formData, foodId: currentEditingItem.foodId})
            });
            showAlert('Menu item updated successfully!', 'success');
        } else {
            // Create new item
            await apiRequest('/fooditems', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            showAlert('Menu item added successfully!', 'success');
        }
        
        closeModal();
        loadMenuItems(); // Refresh menu items
        
    } catch (error) {
        console.error('Failed to save menu item:', error);
        showAlert('Failed to save menu item', 'error');
    }
}

// Edit menu item
function editMenuItem(itemId) {
    const item = currentMenuItems.find(item => item.foodId === itemId);
    if (item) {
        openMenuItemModal(item);
    }
}

// Delete menu item
async function deleteMenuItem(itemId) {
    if (!confirm('Are you sure you want to delete this menu item?')) {
        return;
    }
    
    try {
        await apiRequest(`/fooditems/${itemId}`, {
            method: 'DELETE'
        });
        
        showAlert('Menu item deleted successfully!', 'success');
        loadMenuItems(); // Refresh menu items
        
    } catch (error) {
        console.error('Failed to delete menu item:', error);
        showAlert('Failed to delete menu item', 'error');
    }
}

// Close modal
function closeModal() {
    document.getElementById('menu-modal').style.display = 'none';
    currentEditingItem = null;
}

// Load orders
async function loadOrders() {
    try {
        const orders = await apiRequest('/orders');
        currentOrders = orders;
        displayOrders(orders);
    } catch (error) {
        console.error('Failed to load orders:', error);
        showAlert('Failed to load orders', 'error');
    }
}

// Display orders
function displayOrders(orders) {
    const ordersGrid = document.getElementById('orders-grid');
    ordersGrid.innerHTML = '';
    
    orders.forEach(order => {
        const orderCard = createOrderCard(order);
        ordersGrid.appendChild(orderCard);
    });
}

// Create order card
function createOrderCard(order) {
    const card = document.createElement('div');
    card.className = 'order-card';
    
    const statusActions = getStatusActions(order.status);
    
    card.innerHTML = `
        <div class="order-header">
            <span class="order-id">Order #${order.orderId}</span>
            <span class="order-status status-${order.status.toLowerCase()}">${order.status}</span>
        </div>
        <div class="order-details">
            <p><strong>Customer:</strong> Customer ${order.customerId}</p>
            <p><strong>Address:</strong> ${order.deliveryAddress}</p>
            <p><strong>Total:</strong> RM ${order.totalAmount.toFixed(2)}</p>
            <p><strong>Time:</strong> ${formatDateTime(order.createdAt)}</p>
        </div>
        <div class="order-items">
            <h5>Items:</h5>
            ${order.items ? order.items.map(item => `
                <div class="order-item">
                    <span>${item.name} x${item.quantity}</span>
                    <span>RM ${(item.price * item.quantity).toFixed(2)}</span>
                </div>
            `).join('') : '<p>No items loaded</p>'}
        </div>
        <div class="order-actions">
            ${statusActions.map(action => `
                <button class="btn btn-${action.class}" 
                        onclick="updateOrderStatus(${order.orderId}, '${action.status}')">
                    ${action.label}
                </button>
            `).join('')}
        </div>
    `;
    
    return card;
}

// Get available status actions based on current status
function getStatusActions(currentStatus) {
    const actions = [];
    
    switch(currentStatus) {
        case 'PENDING':
            actions.push({status: 'ACCEPTED', label: 'Accept', class: 'success'});
            actions.push({status: 'CANCELLED', label: 'Cancel', class: 'danger'});
            break;
        case 'ACCEPTED':
            actions.push({status: 'PREPARING', label: 'Start Preparing', class: 'warning'});
            actions.push({status: 'CANCELLED', label: 'Cancel', class: 'danger'});
            break;
        case 'PREPARING':
            actions.push({status: 'READY_FOR_DELIVERY', label: 'Ready for Delivery', class: 'success'});
            break;
        case 'READY_FOR_DELIVERY':
            actions.push({status: 'ON_THE_WAY', label: 'Out for Delivery', class: 'primary'});
            break;
        case 'ON_THE_WAY':
            actions.push({status: 'DELIVERED', label: 'Mark Delivered', class: 'success'});
            break;
    }
    
    return actions;
}

// Update order status
async function updateOrderStatus(orderId, newStatus) {
    try {
        const order = currentOrders.find(o => o.orderId === orderId);
        if (!order) return;
        
        const updatedOrder = {
            ...order,
            status: newStatus
        };
        
        await apiRequest(`/orders/${orderId}`, {
            method: 'PUT',
            body: JSON.stringify(updatedOrder)
        });
        
        showAlert(`Order status updated to ${newStatus}!`, 'success');
        loadOrders(); // Refresh orders
        
    } catch (error) {
        console.error('Failed to update order status:', error);
        showAlert('Failed to update order status', 'error');
    }
}

// Filter orders
function filterOrders() {
    const statusFilter = document.getElementById('status-filter').value;
    
    let filteredOrders = currentOrders;
    if (statusFilter) {
        filteredOrders = currentOrders.filter(order => order.status === statusFilter);
    }
    
    displayOrders(filteredOrders);
}

// Load analytics (placeholder)
function loadAnalytics() {
    // This would typically load chart data and render charts
    console.log('Loading analytics...');
    // For now, just show placeholder message
    showAlert('Analytics feature coming soon!', 'warning');
}

// Utility functions
function formatDateTime(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

function showAlert(message, type = 'info') {
    // Create alert element
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    
    // Insert at top of main content
    const mainContent = document.querySelector('.main-content');
    const header = document.querySelector('.header');
    mainContent.insertBefore(alert, header.nextSibling);
    
    // Remove after 5 seconds
    setTimeout(() => {
        if (alert.parentNode) {
            alert.parentNode.removeChild(alert);
        }
    }, 5000);
}

// Handle logout
function handleLogout() {
    if (confirm('Are you sure you want to logout?')) {
        localStorage.removeItem('authToken');
        // In a real app, redirect to login page
        window.location.href = 'login.html';
    }
}

// Make functions globally available for onclick handlers
window.toggleAvailability = toggleAvailability;
window.editMenuItem = editMenuItem;
window.deleteMenuItem = deleteMenuItem;
window.updateOrderStatus = updateOrderStatus;
