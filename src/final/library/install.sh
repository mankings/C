#!/usr/bin/sh

cd dependencies/SDL2-2.0.22
sudo ./configure
sudo make
sudo make install

sudo apt update
sudo apt install libsdl2-dev
sudo apt install libsdl2-image-dev
sudo apt install libsdl2-mixer-dev
sudo apt install libsdl2-ttf-dev
sudo apt install cmake
