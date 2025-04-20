// Toggle mobile menu
document.addEventListener("DOMContentLoaded", () => {
  // Add to cart functionality
  const addToCartButtons = document.querySelectorAll(".add-to-cart")
  if (addToCartButtons) {
    addToCartButtons.forEach((button) => {
      button.addEventListener("click", function (e) {
        e.preventDefault()
        const form = this.closest("form")
        if (form) {
          form.submit()
        }
      })
    })
  }

  // Quantity validation
  const quantityInputs = document.querySelectorAll('input[type="number"]')
  if (quantityInputs) {
    quantityInputs.forEach((input) => {
      input.addEventListener("change", function () {
        const min = Number.parseInt(this.getAttribute("min"))
        const max = Number.parseInt(this.getAttribute("max"))
        const value = Number.parseInt(this.value)

        if (value < min) {
          this.value = min
        } else if (value > max) {
          this.value = max
        }
      })
    })
  }

  // Confirm delete
  const deleteButtons = document.querySelectorAll(".btn-danger")
  if (deleteButtons) {
    deleteButtons.forEach((button) => {
      button.addEventListener("click", (e) => {
        if (!confirm("Are you sure you want to delete this item?")) {
          e.preventDefault()
        }
      })
    })
  }

  // Payment form validation
  const cardNumberInput = document.getElementById("cardNumber")
  if (cardNumberInput) {
    // Allow only numbers in card number field
    cardNumberInput.addEventListener("input", function(e) {
      this.value = this.value.replace(/[^0-9]/g, '')
    })
  }

  const cvvInput = document.getElementById("cvv")
  if (cvvInput) {
    // Allow only numbers in CVV field
    cvvInput.addEventListener("input", function(e) {
      this.value = this.value.replace(/[^0-9]/g, '')
    })
  }

  const expiryDateInput = document.getElementById("expiryDate")
  if (expiryDateInput) {
    // Format expiry date as MM/YY
    expiryDateInput.addEventListener("input", function(e) {
      let value = this.value.replace(/[^0-9]/g, '')

      if (value.length > 2) {
        value = value.substring(0, 2) + '/' + value.substring(2, 4)
      }

      this.value = value
    })
  }
})
