#!/bin/bash

for file in "./files/"*.txt; do
    start_char=$(basename "$file" .txt | head -c 1 | tr '[:upper:]' '[:lower:]')
    mv "$file" "./$start_char"
done