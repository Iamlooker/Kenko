#!/bin/sh
# POSIX script to convert CHANGELOG.md to changelog.html
# Skips [Unreleased] section and generates a table of contents

set -e

CHANGELOG_MD="${1:-CHANGELOG.md}"
OUTPUT_HTML="${2:-changelog.html}"

# Check if input file exists
if [ ! -f "$CHANGELOG_MD" ]; then
    echo "Error: $CHANGELOG_MD not found" >&2
    exit 1
fi

# Function to escape HTML entities
escape_html() {
    printf '%s' "$1" | sed 's/&/\&amp;/g; s/</\&lt;/g; s/>/\&gt;/g; s/"/\&quot;/g'
}

# Function to convert inline markdown to HTML
convert_inline() {
    # Convert `code` to <code>code</code>
    printf '%s' "$1" | sed 's/`\([^`]*\)`/<code>\1<\/code>/g'
}

# Extract versions for TOC (skip Unreleased)
get_versions() {
    grep -E '^## \[[0-9]+\.[0-9]+\.[0-9]+\]' "$CHANGELOG_MD" | \
    sed 's/## \[\([^]]*\)\].*/\1/' | \
    while read -r version; do
        date=$(grep -F "## [$version]" "$CHANGELOG_MD" | sed 's/.*- \([0-9-]*\)$/\1/')
        printf '%s|%s\n' "$version" "$date"
    done
}

# Generate TOC HTML
generate_toc() {
    printf '            <nav class="changelog-toc">\n'
    printf '                <h3>Versions</h3>\n'
    printf '                <ul>\n'
    get_versions | while IFS='|' read -r version date; do
        printf '                    <li><a href="#%s"><span class="toc-version">%s</span> <span class="toc-date">%s</span></a></li>\n' "$version" "$version" "$date"
    done
    printf '                </ul>\n'
    printf '            </nav>\n'
}

# Generate changelog content HTML
generate_content() {
    in_unreleased=0
    in_version=0
    in_list=0
    current_type=""
    
    while IFS= read -r line || [ -n "$line" ]; do
        # Skip empty lines at start
        case "$line" in
            "# Changelog"*)
                continue
                ;;
            "All notable changes"*|"The format is based"*|"and this project adheres"*)
                continue
                ;;
            "## [Unreleased]"*)
                in_unreleased=1
                continue
                ;;
            "## ["*)
                # Close previous list if open
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                    in_list=0
                fi
                # Close previous version section
                if [ $in_version -eq 1 ]; then
                    printf '            </section>\n\n'
                fi
                
                in_unreleased=0
                in_version=1
                
                # Extract version and date
                version=$(printf '%s' "$line" | sed 's/## \[\([^]]*\)\].*/\1/')
                date=$(printf '%s' "$line" | sed 's/.*- \([0-9-]*\)$/\1/')
                
                printf '            <section class="changelog-version">\n'
                printf '                <h2 id="%s">%s <span class="version-date">%s</span></h2>\n' "$version" "$version" "$date"
                continue
                ;;
        esac
        
        # Skip if in unreleased section
        if [ $in_unreleased -eq 1 ]; then
            continue
        fi
        
        # Skip if not in a version section
        if [ $in_version -eq 0 ]; then
            continue
        fi
        
        # Handle section headers (### Added, ### Changed, etc.)
        case "$line" in
            "### Added"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type added">Added</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="added"
                continue
                ;;
            "### Changed"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type changed">Changed</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="changed"
                continue
                ;;
            "### Fixed"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type fixed">Fixed</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="fixed"
                continue
                ;;
            "### Removed"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type removed">Removed</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="removed"
                continue
                ;;
            "### Deprecated"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type deprecated">Deprecated</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="deprecated"
                continue
                ;;
            "### Security"*)
                if [ $in_list -eq 1 ]; then
                    printf '                </ul>\n'
                fi
                printf '                <h3 class="change-type security">Security</h3>\n'
                printf '                <ul>\n'
                in_list=1
                current_type="security"
                continue
                ;;
        esac
        
        # Handle list items
        case "$line" in
            "- "*)
                item=$(printf '%s' "$line" | sed 's/^- //')
                item_html=$(convert_inline "$item")
                printf '                    <li>%s</li>\n' "$item_html"
                ;;
        esac
        
    done < "$CHANGELOG_MD"
    
    # Close final list and section
    if [ $in_list -eq 1 ]; then
        printf '                </ul>\n'
    fi
    if [ $in_version -eq 1 ]; then
        printf '            </section>\n'
    fi
}

# Generate the full HTML file
cat > "$OUTPUT_HTML" << 'HEADER'
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Kenko Changelog - All notable changes to Kenko">
    <meta name="theme-color" content="#14140E">
    <title>Changelog - Kenko</title>
    <link rel="icon" type="image/png" href="assets/icon.png">
    <link rel="stylesheet" href="style.css">
</head>

<body>
    <!-- Top Navigation Bar -->
    <nav class="top-nav">
        <a href="index.html" class="nav-back">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M19 12H5M12 19l-7-7 7-7"/>
            </svg>
            <span>Back to Home</span>
        </a>
        <span class="nav-title">Changelog</span>
    </nav>

    <!-- Main Content with TOC -->
    <div class="changelog-layout">
HEADER

# Generate TOC
generate_toc >> "$OUTPUT_HTML"

cat >> "$OUTPUT_HTML" << 'CONTENT_START'

        <main class="changelog-content">
            <article class="changelog">
                <header class="changelog-header">
                    <h1>Changelog</h1>
                    <p class="changelog-intro">All notable changes to this project will be documented here.</p>
                </header>

CONTENT_START

# Generate changelog content
generate_content >> "$OUTPUT_HTML"

cat >> "$OUTPUT_HTML" << 'FOOTER'

            </article>
        </main>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-content">
            <p class="footer-copyright">
                Kenko © 2025 LooKeR & Contributors — Licensed under GPLv3
            </p>
        </div>
    </footer>
</body>

</html>
FOOTER

echo "Generated $OUTPUT_HTML from $CHANGELOG_MD"

