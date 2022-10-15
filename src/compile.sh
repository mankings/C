#!/bin/bash

declare auxiliary

while getopts ":m:a:" opt; do
    case $opt in
        a) auxiliary+=("$OPTARG");;
        m) main=$OPTARG
    esac
done

echo $main
cp "$main" ./final/

for val in "${auxiliary[@]}"; do
    cp "$val" ./final
done

cd final/compiler
antlr4 PdrawGrammar.g4
antlr4-build
cat "../$(basename $main)" | antlr4-run

cd ../library
./compile.sh
mv ./build/PDraw ../Output