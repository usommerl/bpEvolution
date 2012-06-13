!/bin/bash

DOCDIR=$(pwd)
TESTDIR=../tests

cd $TESTDIR
for file in *.plt
do
	gnuplot "$file"
done
cd $DOCDIR
