// This will run when the page loads
window.onload = function () {
    fetch('/api/sellers')  // Adjust this URL to match your Spring Boot endpoint
        .then(response => response.json())
        .then(data => {
            const tbody = document.querySelector("#sellerTable tbody");

            data.forEach(seller => {
                const row = document.createElement("tr");

                const idCell = document.createElement("td");
                idCell.textContent = seller.sellerId;

                const nameCell = document.createElement("td");
                nameCell.textContent = seller.sellerName;

                row.appendChild(idCell);
                row.appendChild(nameCell);

                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching sellers:', error);
        });
};
