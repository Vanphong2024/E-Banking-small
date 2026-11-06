const formatVnd = (value) => {
    const digits = value.replace(/\D/g, '').replace(/^0+(?=\d)/, '');
    return digits.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
};

(() => {
    const search = document.getElementById('customer-search');
    if (!search) return;
    const normalize = (value) => value.toLocaleLowerCase('vi-VN').normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').trim();
    const rows = Array.from(document.querySelectorAll('.customer-row'));
    // Index only customer fields; action labels and deposit inputs are excluded.
    const customers = rows.map((row) => ({
        row,
        text: normalize(Array.from(row.cells).slice(0, 5).map((cell) => cell.textContent).join(' '))
    }));
    const status = document.getElementById('customer-status');
    const previous = document.getElementById('customer-prev');
    const next = document.getElementById('customer-next');
    const pageSize = 10;
    let page = 1;

    function render() {
        const terms = normalize(search.value).split(/\s+/).filter(Boolean);
        const matches = customers.filter(({ row, text }) =>
            terms.every((term) => text.includes(term)) &&
            (status.value === 'all' || row.dataset.status === status.value));
        const pages = Math.max(1, Math.ceil(matches.length / pageSize));
        page = Math.min(page, pages);
        rows.forEach((row) => {
            row.hidden = true;
            row.querySelectorAll('details[open]').forEach((panel) => { panel.open = false; });
        });
        matches.slice((page - 1) * pageSize, page * pageSize).forEach(({ row }) => { row.hidden = false; });
        document.getElementById('customer-no-results').hidden = matches.length > 0 || rows.length === 0;
        const start = matches.length ? (page - 1) * pageSize + 1 : 0;
        document.getElementById('customer-result-count').textContent =
            `Hiển thị ${start}–${Math.min(page * pageSize, matches.length)} / ${matches.length} khách hàng (tổng ${rows.length})`;
        document.getElementById('customer-page').textContent = `Trang ${page} / ${pages}`;
        previous.disabled = page === 1;
        next.disabled = page === pages;
    }
    search.addEventListener('input', () => { page = 1; render(); });
    status.addEventListener('change', () => { page = 1; render(); });
    previous.addEventListener('click', () => { page--; render(); });
    next.addEventListener('click', () => { page++; render(); });
    document.getElementById('customer-tools').hidden = false;
    document.getElementById('customer-pagination').hidden = false;
    render();
})();

function formatVndInput(input) {
    const formatted = formatVnd(input.value);
    if (input.value !== formatted) {
        input.value = formatted;
    }
}

document.querySelectorAll('.deposit-form').forEach((form) => {
    const amountInput = form.elements.amount;

    form.addEventListener('submit', (event) => {
        const rawAmount = amountInput.value.replace(/\./g, '');
        const amount = Number(rawAmount);
        if (!window.confirm(`Xác nhận đã nhận và nạp ${amount.toLocaleString('vi-VN')} VNĐ cho ${form.dataset.customer}?`)) {
            event.preventDefault();
            amountInput.value = formatVnd(rawAmount);
            return;
        }
        amountInput.value = rawAmount;
        form.querySelector('button[type="submit"]').disabled = true;
    });
});

document.querySelectorAll('.auto-hide-notice').forEach((notice) => {
    window.setTimeout(() => {
        notice.classList.add('is-hidden');
        window.setTimeout(() => notice.remove(), 400);
    }, 3000);
});
