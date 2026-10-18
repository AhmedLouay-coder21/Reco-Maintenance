import {
  MARQUEE_ITEMS, FEATURES,
  STATS
} from '../data/data.js'
import {
  Marquee, FeatureCard,
  StatCard
} from '../components/components.js'
import { api, isAuthenticated, isTokenValid } from '../api/auth.js'
import { showToast } from '../utils/alert.js'
import { addToCart, bindEvents, fetchCategories, goToProduct, loadCart, productCard, skeletonGrid } from './products.js'

export function HomeView() {
  setTimeout(() => {
    void initHomePage()
  }, 0)

  return `
    <!-- HERO -->
    <section class="hero min-h-screen flex flex-col items-center justify-center text-center px-6 relative z-10 pt-20">
      <p class="font-space text-[0.7rem] tracking-[0.3em] uppercase text-secondary mb-8 hero-anim-1">
        — Frequency 001 · Drop Season IV —
      </p>
      <h1 class="hero-title font-bebas text-[clamp(4rem,14vw,12rem)] mb-4 hero-anim-2">
        <span class="glow-word">RECO</span><br>
      </h1>
      <p class="text-[0.85rem] font-light tracking-[0.25em] uppercase text-muted mb-12 max-w-[500px] leading-relaxed hero-anim-3">
        Objects channeled from the space between thought and form.<br>For those who feel the frequency.
      </p>
      <div class="flex gap-5 hero-anim-4">
        <a href="/products" class="relative overflow-hidden px-10 py-4 border border-secondary bg-secondary/10 text-line-bright text-[0.72rem] font-semibold tracking-[0.2em] uppercase rounded-sm transition-all hover:shadow-[0_0_30px_rgba(155,89,247,0.3)] hover:text-white">
          Enter RECO →
        </a>
        <a href="#about" class="px-9 py-4 border border-secondary/20 text-[0.72rem] font-semibold tracking-[0.2em] uppercase rounded-sm transition-all hover:text-lavender hover:border-secondary/40 text-muted">
          Our Ritual
        </a>
      </div>
      <div class="absolute bottom-10 left-1/2 -translate-x-1/2 flex flex-col items-center gap-2 font-space text-[0.6rem] tracking-widest uppercase hero-anim-5 text-muted">
        <div class="scroll-line-anim w-[1px] h-12 bg-gradient-to-b from-secondary to-transparent"></div>
        <span>scroll</span>
      </div>
    </section>

    <!-- MARQUEE -->
    ${Marquee(MARQUEE_ITEMS)}

    <!-- FEATURES -->
    <div class="reveal max-w-[90%] mx-auto mt-20 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 border border-border relative z-10">
      ${FEATURES.map((f, i) => FeatureCard(f, i === FEATURES.length - 1)).join('')}
    </div>
    <hr>

    <!-- TOP PRODUCTS -->
    <section class="px-6 lg:px-20 py-16 relative z-10">
      <div class="reveal mb-10 flex flex-col gap-3">
        <div class="font-space text-[0.62rem] tracking-[0.3em] uppercase text-secondary">Top Transmissions</div>
        <h2 class="font-bebas text-4xl lg:text-6xl tracking-wider text-line-bright">
          Trending Products
        </h2>
      </div>
      <div id="top-products-container" class="reveal">
        ${skeletonGrid()}
      </div>
    </section>

    <!-- PRODUCT DETAIL DRAWER -->
    <div id="detail-overlay" class="fixed inset-0 z-[200] pointer-events-none">
      <!-- backdrop -->
      <div id="detail-backdrop" class="absolute inset-0 bg-main/0 backdrop-blur-none transition-all duration-500"></div>
      <!-- drawer -->
      <div id="detail-drawer"
           class="absolute top-0 right-0 h-full w-full max-w-[520px] bg-surface border-l border-border
                  translate-x-full transition-transform duration-500 ease-[cubic-bezier(.16,1,.3,1)]
                  flex flex-col overflow-hidden">
        <div id="detail-content" class="flex flex-col h-full overflow-y-auto"></div>
      </div>
    </div>

    <!-- CART TOAST -->
    <div id="cart-toast" class="fixed top-25 right-6 z-[300] pointer-events-none opacity-0 transition-all duration-500" style="transform: translateX(150%)">
      <div class="flex items-center gap-3 bg-surface border border-secondary/60 px-5 py-3 shadow-[0_0_30px_rgba(155,89,247,.25)]">
        <span class="text-secondary">⬡</span>
        <span id="cart-toast-msg" class="font-josefin text-[0.72rem] tracking-[0.15em] uppercase text-(--color-white)"></span>
      </div>
    </div>

    <!-- floating cart button -->
    <button id="cart-fab"
      class="fixed bottom-8 right-8 z-[150] w-14 h-14 rounded-full border-2 border-secondary bg-main/80 backdrop-blur-lg
             flex items-center justify-center text-xl text-line-bright
             shadow-[0_0_20px_rgba(155,89,247,.3)] hover:shadow-[0_0_40px_rgba(155,89,247,.5)] hover:scale-110
             transition-all duration-300 group">
      ⬡
      <span id="cart-badge"
        class="absolute -top-1 -right-1 min-w-[20px] h-5 rounded-full bg-secondary text-white font-bold text-[0.6rem] flex items-center justify-center px-1 hidden">
        0
      </span>
    </button>

    <!-- CART SIDEBAR -->
    <div id="cart-overlay" class="fixed inset-0 z-[200] pointer-events-none">
      <div id="cart-backdrop" class="absolute inset-0 bg-main/0 backdrop-blur-none transition-all duration-500"></div>
      <div id="cart-drawer"
           class="absolute top-0 right-0 h-full w-full max-w-[400px] bg-surface border-l border-border
                  translate-x-full transition-transform duration-500 ease-[cubic-bezier(.16,1,.3,1)]
                  flex flex-col overflow-hidden">
        <div id="cart-content" class="flex flex-col h-full"></div>
      </div>
    </div>
  
    <!-- STATS -->
    <div class="reveal max-w-[90%] mx-auto my-20 grid grid-cols-2 lg:grid-cols-4 border border-border relative z-10">
      ${STATS.map((s, i) => StatCard(s, i === STATS.length - 1)).join('')}
    </div>

    <!-- TESTIMONIALS -->
    <section class="bg-test-color px-6 lg:px-20 py-28 relative z-10" id="testi">
      <div class="reveal mb-12">
        <div class="font-space text-[0.62rem] tracking-[0.3em] uppercase text-secondary mb-3">Transmissions Received</div>
        <h2 class="font-bebas text-5xl lg:text-7xl tracking-wider">
          VOICES FROM <span class="text-line-bright drop-shadow-[0_0_30px_rgba(155,89,247,0.4)]">THE</span><br>OTHER SIDE
        </h2>
      </div>
      <div id="top-reviews-container" class="reveal border border-border overflow-hidden">
        <div class="p-10 text-center text-[0.72rem] tracking-[0.2em] uppercase text-muted">
          Loading top reviews...
        </div>
      </div>
    </section>
  `
}

async function initHomePage() {
  const tasks = [fetchCategories()]
  if (isAuthenticated() && isTokenValid()) {
    tasks.push(loadCart())
  }

  await Promise.all(tasks)

  const observer = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.isIntersecting) entry.target.classList.add('vis')
    })
  }, { threshold: 0.1 })

  document.querySelectorAll('.reveal').forEach(el => observer.observe(el))

  await loadTopProducts()
  await loadTopReviews()
  bindEvents()
}

async function loadTopReviews() {
  const container = document.getElementById('top-reviews-container')
  if (!container) return

  try {
    const reviews = await api('/reviews/top?limit=4')

    if (!Array.isArray(reviews) || !reviews.length) {
      container.innerHTML = `
        <div class="p-10 text-center text-[0.72rem] tracking-[0.2em] uppercase text-muted">
          No reviews available yet.
        </div>`
      return
    }

    container.innerHTML = `
      <div class="grid grid-cols-1 md:grid-cols-2 gap-px bg-border">
        ${reviews.map(review => `
          <article class="bg-main p-8">
            <p class="text-[0.9rem] font-light leading-relaxed text-lavender">${escapeHtml(review.comment || '')}</p>
            <p class="mt-5 text-[0.7rem] tracking-[0.15em] uppercase text-muted">
              — ${escapeHtml(review.username || 'Anonymous')}
            </p>
          </article>
        `).join('')}
      </div>`
  } catch (error) {
    container.innerHTML = `
      <div class="p-10 text-center text-[0.72rem] tracking-[0.2em] uppercase text-muted">
        Reviews are temporarily unavailable.
      </div>`
  }
}

function escapeHtml(value) {
  return String(value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;')
}

async function loadTopProducts() {
  const container = document.getElementById('top-products-container')
  if (!container) return

  container.innerHTML = skeletonGrid()

  try {
    const params = new URLSearchParams({
      page: '0',
      limit: '4',
      sort: 'popularityScore',
      order: 'desc',
    })

    const data = await api(`/recommendations/popular`)
    const topProducts = data.recommendations || []

    if (!topProducts.length) {
      container.innerHTML = `
        <div class="border border-border bg-surface p-12 text-center">
          <div class="font-bebas text-4xl tracking-wider text-muted mb-2">NO TOP PRODUCTS</div>
          <p class="text-[0.72rem] tracking-[0.2em] uppercase text-muted">The catalog has no objects to highlight yet.</p>
        </div>`
      return
    }

    container.innerHTML = `
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-[1px] bg-border border border-border">
        ${topProducts.map(productCard).join('')}
      </div>`

    bindTopProductCards(container, topProducts)
  } catch (err) {
    container.innerHTML = `
      <div class="border border-red-400/20 bg-red-500/5 p-10 text-center">
        <div class="font-bebas text-4xl text-red-300 mb-2">SIGNAL LOST</div>
        <p class="text-[0.72rem] tracking-[0.2em] uppercase text-muted">${err?.message || 'Failed to load top products'}</p>
      </div>`
    showToast(err?.message || 'Failed to load top products', 'error')
  }
}

async function bindTopProductCards(container, topProducts) {
  const canTrack = isAuthenticated() && isTokenValid();
  let userId = null;

  if (canTrack) {
    try {
      const uid = await api(`/users/me`);
      userId = uid.id;
    } catch (_) {
      userId = null;
    }
  }

  container.querySelectorAll('[data-product-id]').forEach(card => {
    const id = Number(card.dataset.productId)
    const product = topProducts.find(item => item.productId === id)
    card.querySelector('.btn-cart')?.addEventListener('click', event => {
          if (userId) {
            api(`/users/${userId}/interactions`, {
              method: 'POST',
              body: JSON.stringify({
                productId: product.id || product.productId,
                interactionType: 'CART_ADD'
              })
            }).catch(() => {});
          }
      event.stopPropagation()
      addToCart(product)
    })

    card.querySelector('.card-click-area')?.addEventListener('click', () => {
          if (userId) {
            api(`/users/${userId}/interactions`, {
              method: 'POST',
              body: JSON.stringify({
                productId: product.id || product.productId,
                interactionType: 'CLICK'
              })
            }).catch(() => {});
          }

          goToProduct(id);
        });
  })

  requestAnimationFrame(() => {
    container.querySelectorAll('.product-reveal').forEach((element, index) => {
      setTimeout(() => element.classList.add('product-reveal--vis'), index * 40)
    })
  })
}