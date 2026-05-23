export function confirmDialog(message, onConfirm) {
    const id = 'confirm-overlay-' + Date.now();

    const overlay = document.createElement('div');
    overlay.id = id;
    overlay.className = 'fixed inset-0 z-[300] flex items-center justify-center bg-black/75 backdrop-blur-sm font-josefin';
    overlay.innerHTML = `
        <div class="relative w-[min(420px,95vw)] border border-border bg-surface2 p-7">
            <div class="absolute inset-x-0 top-0 h-[2px] bg-gradient-to-r from-transparent via-red-400 to-transparent opacity-60"></div>
            <div class="font-bebas text-[1.1rem] tracking-[0.2em] text-line-bright mb-2">Confirm Action</div>
            <div class="text-[0.78rem] text-muted leading-relaxed mb-6">${message}</div>
            <div class="flex justify-end gap-2.5">
                <button id="${id}-cancel" class="inline-flex items-center justify-center gap-2 border border-secondary/60 bg-secondary/10 text-line-bright text-[0.65rem] tracking-[0.18em] uppercase transition-all hover:bg-secondary/20 px-4 py-2">Cancel</button>
                <button id="${id}-confirm" class="inline-flex items-center justify-center gap-2 border border-red-400/70 text-red-300 bg-red-500/10 text-[0.65rem] tracking-[0.18em] uppercase transition-all hover:bg-red-500/20 px-4 py-2">Confirm</button>
            </div>
        </div>`;

    document.body.appendChild(overlay);

    const close = () => overlay.remove();

    overlay.addEventListener('click', e => { if (e.target === overlay) close(); });
    document.getElementById(`${id}-cancel`).addEventListener('click', close);
    document.getElementById(`${id}-confirm`).addEventListener('click', () => {
        close();
        onConfirm();
    });
}