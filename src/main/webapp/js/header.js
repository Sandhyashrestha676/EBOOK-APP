document.addEventListener('DOMContentLoaded', function() {
    const header = document.querySelector('header');
    const logo = document.querySelector('.logo h1');

    // Ensure header is visible on page load
    header.style.display = 'block';

    window.addEventListener('scroll', function() {
        // Always ensure header is visible
        header.style.display = 'block';

        if (window.scrollY > 50) {
            header.classList.add('scrolled');
            logo.style.fontSize = '1.5em';
        } else {
            header.classList.remove('scrolled');
            logo.style.fontSize = '1.8em';
        }
    });
});
