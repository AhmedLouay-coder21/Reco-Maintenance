let toastTimer;

export function showToast(msg, type = 'success') {
    let toast = document.getElementById('_toast');
    
    if (!toast) {
        toast = document.createElement('div');
        toast.id = '_toast';
        toast.style.cssText = 'position:fixed; top:6.5rem; right:1.5rem; z-index:9999; pointer-events:none;';
        toast.innerHTML = `
            <div class="flex items-center gap-3 bg-surface border border-secondary/60 px-5 py-3 shadow-[0_0_30px_rgba(155,89,247,.25)]">
                <span class="text-secondary">⬡</span>
                <span id="_toast-msg" class="font-josefin text-[0.72rem] text-var(--color-text-dif) tracking-[0.15em] uppercase"></span>
            </div>`;
        document.body.appendChild(toast);
    }

    if (type === 'error') {
        toast.querySelector('div').style.borderColor = 'rgba(248,113,113,0.6)';
    } else {
        toast.querySelector('div').style.borderColor = 'green';
    }

    document.getElementById('_toast-msg').textContent = msg;
    toast.style.transition = 'transform 0.6s ease, opacity 0.6s ease';
    toast.style.transform = 'translateX(150%)';
    toast.style.opacity = '1';
    requestAnimationFrame(() => { toast.style.transform = 'translateX(0%)'; });

    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => {
        toast.style.transform = 'translateX(150%)';
        toast.style.opacity = '0';
    }, 2200);
}