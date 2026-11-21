import { Nav, Footer } from "../components/components.js"
import { NAV_LINKS, FOOTER_LINKS } from "../data/data.js"

const ABOUT_STORIES = [
  {
    image: "/src/assets/Ahmad Louay.jpeg",
    eyebrow: "",
    title: "Ahmad Louay",
    description:
      "Assigned tasks: MR-01 — replace the mock reviews on the home page with real top reviews shown as plain text. MR-04 — replace the About Us link that currently leads nowhere. Frontend team task: MR-09 — escape review text on the product page.",
  },
  {
    image: "/src/assets/Ahmed Nasser.jpeg",
    eyebrow: "",
    title: "Ahmed Nasser",
    description:
      "Assigned task: MR-08 — add a production profile with SQL logging disabled and the CORS origin read from an environment variable.",
  },
  {
    image: "/src/assets/Abdelrahman Hany.jpeg",
    eyebrow: "",
    title: "Abdelrahman Hany",
    description:
      "Assigned tasks: MR-05 — add a top reviews endpoint and sample reviews. MR-06 — restrict CORS to one configured origin using app.cors.allowed-origin. Backend team tasks: MR-10 — replace the fixed admin password in the seed code. MR-11 — add Swagger tags and summaries to the controllers.",
  },
  {
    image: "/src/assets/Bahaa El Deen.png",
    eyebrow: "",
    title: "Bahaa El Deen",
    description:
      "Assigned task: MR-07 — add unit tests for MockPaymentGateway.",
  },
  {
    image: "/src/assets/Yussuf Hassan.jpeg",
    eyebrow: "",
    title: "Yussuf Hassan",
    description:
      "Assigned tasks: MR-02 — add a mobile menu to the navbar and make the main pages fit phone screens. MR-03 — show the cart button on the product description page.",
  },
]

export function AboutView() {
  const nav = document.getElementById("nav-slot")
  const footer = document.getElementById("footer-slot")
  const app = document.getElementById("app")

  if (nav) nav.innerHTML = Nav(NAV_LINKS)
  if (footer) footer.innerHTML = Footer(FOOTER_LINKS)

  app.innerHTML = `
    <main class="relative z-10 pt-32">
      <section class="px-6 lg:px-20 pb-20 max-w-[1100px] mx-auto">
        <div class="font-space text-[0.62rem] tracking-[0.3em] uppercase text-secondary mb-5">The RECO frequency</div>
        <h1 class="font-bebas text-[clamp(5rem,15vw,11rem)] leading-[0.82] tracking-wider text-line-bright">
          ABOUT<br><span class="text-(--color-white)">US</span>
        </h1>
        <div class="mt-10 max-w-[620px] border-l-2 border-secondary pl-6">
          <p class="text-lg lg:text-xl font-light leading-relaxed text-lavender">
            RECO is an immersive e-commerce platform that connects you with distinctive products through a seamless shopping experience. Explore curated collections, manage your cart, and discover personalized recommendations powered by our hybrid recommendation engine.
          </p>
        </div>
      </section>

      <section class="px-6 lg:px-20 pb-28 max-w-[1200px] mx-auto">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-px bg-border border border-border">
          ${ABOUT_STORIES.map(
            (story) => `
            <article class="group bg-surface overflow-hidden">
              <div class="aspect-[32/30] overflow-hidden bg-deep">
                <img src="${story.image}" alt="${story.title}" class="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105">
              </div>
              <div class="p-8 lg:p-10">
                <div class="font-space text-[0.6rem] tracking-[0.25em] uppercase text-secondary mb-4">${story.eyebrow}</div>
                <h2 class="font-bebas text-3xl tracking-wider text-line-bright mb-4">${story.title}</h2>
                <p class="text-[0.85rem] font-light leading-relaxed text-muted">${story.description}</p>
              </div>
            </article>
          `,
          ).join("")}
        </div>
      </section>

      <section class="px-6 lg:px-20 py-24 border-t border-border text-center">
        <div class="font-space text-[0.62rem] tracking-[0.3em] uppercase text-secondary mb-5">Open channel</div>
        <p class="font-bebas text-4xl lg:text-6xl tracking-wider text-line-bright">FIND YOUR FREQUENCY</p>
        <a href="/products" class="inline-block mt-8 px-8 py-4 border border-secondary text-[0.7rem] tracking-[0.2em] uppercase text-line-bright hover:bg-secondary/10 transition-all">
          Browse the collection →
        </a>
      </section>
    </main>
  `
}
