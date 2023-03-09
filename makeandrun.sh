#!/bin/sh

# Kinda shitty fix since for some reason runClient doesn't want to include the mod
# - build task
# - cp the mod from build/libs to run/mods
# - remove the .mixin folder from run/ (if not, runClient doesn't start at all)
# - runClient task

# EDIT:
# For some reason can't run gradle tasks 
# so this just removes the problematic folder and copies the mod

rm run/mods/*
rm -r run/.mixin.out
mv build/libs/* run/mods/
