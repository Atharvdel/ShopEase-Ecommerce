// This function loads products from the server or uses the model data passed from Thymeleaf
function loadProducts() {
  // Check if we're using Thymeleaf data or need to fetch products
  const productsContainer = document.getElementById("products-section");
  const productCards = productsContainer.querySelectorAll(".product-card");
  const noProductsMessage = productsContainer.querySelector(".no-products");

  // If we already have product cards or a no-products message rendered by Thymeleaf, don't override them
  if (productCards.length > 0 || noProductsMessage) {
    console.log("Products or no-products message already loaded from server");
    return;
  }

  // Only fetch products if Thymeleaf hasn't already handled it
  fetch("/api/seller/products")
      .then(response => {
        if (!response.ok) {
          throw new Error("Failed to fetch products");
        }
        return response.json();
      })
      .then(products => {
        if (products.length === 0) {
          // No products found - display a simple message
          productsContainer.innerHTML = `
          <div class="no-products">
            <p>You haven't added any products yet.</p>
            <a href="/seller/products/add" class="add-product-btn">Add Your First Product</a>
          </div>
        `;
        } else {
          // Create product cards
          const productGrid = document.createElement("div");
          productGrid.className = "product-grid";

          products.forEach(product => {
            const card = createProductCard(product);
            productGrid.appendChild(card);
          });

          productsContainer.appendChild(productGrid);
        }
      })
      .catch(error => {
        // Instead of showing error, just show the "Add your first product" message
        console.error("Error loading products:", error);
        productsContainer.innerHTML = `
        <div class="no-products">
          <p>You haven't added any products yet.</p>
          <a href="/seller/products/add" class="add-product-btn">Add Your First Product</a>
        </div>
      `;
      });
}

function createProductCard(product) {
  const card = document.createElement("div");
  card.className = "product-card";
  card.setAttribute("data-product-id", product.id);

  // Use product.imageUrl if available, otherwise use a default image
  const imageUrl = product.imageUrl || "/images/default-product.jpg";

  card.innerHTML = `
    <img src="${imageUrl}" alt="${product.name}" onerror="this.src='/images/default-product.jpg'">
    <div class="product-info">
      <h3>${product.name}</h3>
      <p class="product-description">${product.description || ''}</p>
      <div class="product-price">₹${product.price}</div>
      <div class="product-actions">
        <a href="/seller/products/edit/${product.id}" class="edit-btn">Edit</a>
        <button onclick="deleteProduct(${product.id})" class="delete-btn">Delete</button>
      </div>
    </div>
  `;
  return card;
}

function deleteProduct(productId) {
  if (confirm("Are you sure you want to delete this product?")) {
    fetch(`/api/seller/products/delete/${productId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        // Add CSRF token if needed
        // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
      }
    })
        .then(response => {
          if (!response.ok) {
            throw new Error("Failed to delete product");
          }
          return response.json();
        })
        .then(data => {
          alert("Product deleted successfully");
          // Remove the product card from the DOM
          const productCard = document.querySelector(`.product-card[data-product-id="${productId}"]`);
          if (productCard) {
            productCard.remove();
          } else {
            // Reload the page to reflect changes
            window.location.reload();
          }
        })
        .catch(error => {
          console.error("Error deleting product:", error);
          alert("Failed to delete product. Please try again.");
        });
  }
}

// Initialize when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", function() {
  // Add event listeners for sidebar buttons
  const sidebarButtons = document.querySelectorAll(".sidebar button");
  sidebarButtons.forEach(button => {
    button.addEventListener("click", function() {
      // Remove active class from all buttons
      sidebarButtons.forEach(btn => btn.classList.remove("active"));
      // Add active class to clicked button
      this.classList.add("active");
    });
  });

  // Load products
  loadProducts();
});
