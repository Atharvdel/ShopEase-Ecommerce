document.addEventListener("DOMContentLoaded", function () {
    // Add event listeners for filter interactions
    setupFilterInteractions();

    // Add functionality for "Add to Cart" buttons
    setupAddToCartButtons();

    // Handle product image loading errors
    handleImageErrors();
});

function setupFilterInteractions() {
    // Make category checkboxes submit the form when clicked
    const categoryCheckboxes = document.querySelectorAll('.filter-section input[type="checkbox"]');
    categoryCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function() {
            // Uncheck other checkboxes if this one is checked
            if (this.checked) {
                categoryCheckboxes.forEach(cb => {
                    if (cb !== this) cb.checked = false;
                });

                // Submit the form
                document.getElementById('categoryForm').submit();
            }
        });
    });

    // Make price and rating radio buttons submit their forms when clicked
    const radioButtons = document.querySelectorAll('.filter-section input[type="radio"]');
    radioButtons.forEach(radio => {
        radio.addEventListener('change', function() {
            this.closest('form').submit();
        });
    });
}

function setupAddToCartButtons() {
    const addToCartButtons = document.querySelectorAll('.add-to-cart');
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            // Optional: Add animation or feedback when adding to cart
            button.classList.add('added');
            setTimeout(() => {
                button.classList.remove('added');
            }, 1000);
        });
    });
}

function handleImageErrors() {
    const productImages = document.querySelectorAll('#product-container img');
    productImages.forEach(img => {
        img.onerror = function() {
            this.src = '/images/default-product.jpg';
        };
    });
}
