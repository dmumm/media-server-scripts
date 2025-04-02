#!/usr/bin/env python3
"""
filefind.py - Recursively finds all MKV files in a specified directory
Usage: python filefind.py [directory]
"""

import os
import sys

# Use command line argument for directory or default to current directory
search_dir = sys.argv[1] if len(sys.argv) > 1 else "."

for root, dirs, files in os.walk(search_dir):
    for file in files:
        p = os.path.join(root, file)
        if p.endswith('.mkv'):
            print(os.path.abspath(p))