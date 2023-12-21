#!/usr/bin/env python3
import sys

for line in sys.stdin:

    ########## EDIT HERE ##########

    line = line.strip().split(',')

    if len(line) == 5:
        if line[1] == "Suwon":
            print(" ".join(line))

    ########## EDIT HERE ##########