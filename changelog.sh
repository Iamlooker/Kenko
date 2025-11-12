#
# Copyright (C) 2025 LooKeR & Contributors
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

VERSION=$1
# Extract changelog content for this version (Keep a Changelog format: ## [Version])
# Match from ## [VERSION] to the next ## [ version section
awk -v version="$VERSION" '
  /^## \[/ {
    if (match($0, "^## \\[" version "\\]")) {
      in_section = 1
      next
    } else if (in_section) {
      exit
    }
  }
  in_section {
    print
  }
' CHANGELOG.md