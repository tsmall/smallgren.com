# smallgren.com - Annual Christmas Card Website

This is the code for our annual "Christmas card" website.
We use this to share our yearly updates with family and friends.

## About

The website serves as our digital Christmas card, featuring:
- Annual stories and updates from our family
- Photo galleries from each year
- Minimal, hand-crafted HTML and CSS
- Lightweight JavaScript where needed

## Technical Implementation

This is a **completely static website** with no server-side processing:

- **Frontend**: Hand-written HTML, CSS, and minimal JavaScript
- **Structure**: Each year has its own directory under `stories/` with:
  - `index.html` - Main content for that year
  - Year-specific CSS files (e.g., `2024.css`)
  - Media assets in `media/` subdirectories (not committed)
- **Shared assets**: Common styles and scripts in `stories/public/`

## Image and Video Preparation

The project includes scripts for preparing media assets before uploading:

- **Image conversion**: Scripts in the `scripts/` directory handle:
  - Resizing and optimizing images
  - Creating web-friendly formats
  - Batch processing for entire year directories
- **Video encoding**: Scripts for converting and optimizing video files

## Development

### Prerequisites

No special prerequisites are needed for the static site itself.
The media preparation scripts require specific tools:

- **ImageMagick** (for `convert` command) - used by image conversion scripts
- **FFmpeg** - used by video encoding and poster generation scripts
- **Clojure** (with AWS SDK dependencies) - used by AWS management utilities
- Basic Unix shell tools (bash)

### Running Locally

Since this is a static site, simply:

1. Clone the repository
2. Start a local web server
3. Visit http://localhost:8000/stories/

```bash
# Using Python's built-in server (Python 3)
python3 -m http.server 8000
```

### Adding a New Year

To add content for a new year:

1. Create a new directory under `stories/` (e.g., `stories/2025/`)
2. Add your `index.html` file with the year's content
3. Create any year-specific CSS in a file named `2025.css`
4. Place media assets in `stories/2025/media/`
5. Use the preparation scripts to optimize images/videos before committing

## Media Preparation Scripts

The `scripts/` directory contains utilities for preparing media:

- `convert.sh` / `convert-year.sh` - Image conversion utilities
- `encode.sh` / `encode-year.sh` - Video encoding utilities
- `poster.sh` - Generate video poster frames
- `all-posters.sh` - Process posters for all videos

## License

Copyright © 2016-2025 Tom Small III.

Distributed under the [Eclipse Public License][epl], the same as Clojure.

<!-- References -->
[epl]: https://eclipse.org/org/documents/epl-v10.php
