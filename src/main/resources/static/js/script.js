function confirmDelete(title) {
    return confirm(`Are you sure you want to delete the promotion "${title}"?`);
}
function searchPromotions() {
    const searchTerm = document.getElementById('searchInput').value;
    window.location.href = '/promotions/search?discountAmount=' + encodeURIComponent(searchTerm);
}