# Codaphela documents

MkDocs (Material theme) site source.

    .venv/bin/mkdocs serve     # live preview on http://127.0.0.1:8000
    .venv/bin/mkdocs build     # static site into site/, copy that to the web server

Recreate the venv with:

    python3 -m venv .venv
    .venv/bin/pip install mkdocs-material mkdocs-glightbox mkdocs-table-reader-plugin
