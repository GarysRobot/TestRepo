<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        document.getElementById('loading').style.display = 'block';
        document.getElementById('success').style.display = 'none';

        setTimeout(() => {
            document.getElementById('loading').style.display = 'none';
            document.getElementById('success').style.display = 'block';
        }, 5000);
    });
</script>