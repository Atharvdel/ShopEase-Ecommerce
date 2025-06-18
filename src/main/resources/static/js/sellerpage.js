document.addEventListener("DOMContentLoaded", function () {
    const productContainer = document.getElementById("product-container");

    // Dummy data for products (Replace with actual backend fetch)
    let products = [
        { id: 1, name: "Laptop", price: "$999", image: "laptop.jpg" },
        { id: 2, name: "Smartphone", price: "$499", image: "phone.jpg" },
        { id: 3, name: "Headphones", price: "$199", image: "headphones.jpg" },
        { id: 4, name: "Keyboard", price: "$89", image: "keyboard.jpg" },
        { id: 5, name: "Mouse", price: "$49", image: "mouse.jpg" },
        { id: 6, name: "Monitor", price: "$299", image: "monitor.jpg" }
    ];

    function displayProducts() {
        productContainer.innerHTML = ""; // Clear existing products
        products.forEach((product, index) => {
            let productDiv = document.createElement("div");
            productDiv.classList.add("product");
            productDiv.innerHTML = `
                <img src="${product.image}" alt="${product.name}">
                <h3 class="product-name">${product.name}</h3>
                <p class="product-price">${product.price}</p>
                <button class="update-btn" onclick="updateProduct(${index})">Update</button>
                <button class="delete-btn" onclick="deleteProduct(${index})">Delete</button>
            `;
            productContainer.appendChild(productDiv);
        });
    }

    // Function to update a product
    window.updateProduct = function (index) {
        let newName = prompt("Enter new product name:", products[index].name);
        let newPrice = prompt("Enter new price:", products[index].price);
        if (newName && newPrice) {
            products[index].name = newName;
            products[index].price = newPrice;
            displayProducts(); // Refresh display
        }
    };

    // Function to delete a product
    window.deleteProduct = function (index) {
        if (confirm("Are you sure you want to delete this product?")) {
            products.splice(index, 1);
            displayProducts(); // Refresh display
        }
    };

    displayProducts(); // Initial render
});
