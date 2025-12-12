document.addEventListener("DOMContentLoaded", () => {
  if (!('IntersectionObserver' in window)) {
    return;
  }

  const lazyVideoObserver = new IntersectionObserver((entries, observer) => {
    entries.forEach((video) => {
      if (!video.isIntersecting) {
        return
      }

      for (const source in video.target.children) {
        const videoSource = video.target.children[source];
        if (videoSource.tagName === "SOURCE") {
          videoSource.src = videoSource.dataset.src;
        }
      }

      video.target.load();
      video.target.classList.remove("lazy");
      observer.unobserve(video.target);

      console.debug("Lazy loaded a video:", video.target);
    });
  });

  const lazyVideos = document.querySelectorAll("video.lazy");
  lazyVideos.forEach((lazyVideo) => lazyVideoObserver.observe(lazyVideo));
});
