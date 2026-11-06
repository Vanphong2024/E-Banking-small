(() => {
    const script = document.currentScript;
    const statusUrl = script.dataset.statusUrl;
    const loginUrl = script.dataset.loginUrl;
    let checking = false;

    async function checkSession() {
        if (checking) return;
        checking = true;
        try {
            const response = await fetch(statusUrl, { cache: 'no-store', credentials: 'same-origin' });
            if (!response.ok) return;
            const status = await response.text();
            if (status === 'replaced' || status === 'expired') {
                window.location.replace(loginUrl + (status === 'replaced' ? '?sessionReplaced' : '?sessionExpired'));
            }
        } catch (error) {
            // Retry on the next check when connectivity returns.
        } finally {
            checking = false;
        }
    }

    checkSession();
    window.setInterval(checkSession, 3000);
    window.addEventListener('pageshow', checkSession);
    window.addEventListener('focus', checkSession);
    document.addEventListener('visibilitychange', () => {
        if (!document.hidden) checkSession();
    });
})();
