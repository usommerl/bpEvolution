#!/bin/bash

TESTNAME=$1
PROBLEM=$2
G=$3

S=500
Q=1
PS='best'
R='mapped'
M='shift'
D='best-fit'
ES="tournament=3"

NUMBEROFRUNS=11

function execute {
  NUMBER=$(printf "%02d" $1)
  PREFIX='test_'
  EXTENSION='.dat'
  DATAFOLDER="evaluation/data/"
  O="${DATAFOLDER}${PREFIX}SCRIPTTEST_${ALGORITHM}_${PROBLEM}_${NUMBER}${EXTENSION}"
  #O="${DATAFOLDER}${PREFIX}${TESTNAME}_${ALGORITHM}_${PROBLEM}_${NUMBER}${EXTENSION}"
  sbt "run -s $S -g $G -d $D -q $Q -ps $PS -r $R -m $M -es $ES -o $O $PROBLEM"
}

BUILDFOLDER="../"
cd $BUILDFOLDER

if [ "$TESTNAME" == "decfuc" ]; then
    declare -a ALGORITHMS=('next-fit' 'first-fit' 'best-fit')
    for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
    do
       for ALGORITHM in ${ALGORITHMS[@]}
       do
         D=$ALGORITHM
         execute $TESTRUN
       done
    done
fi

if [ "$TESTNAME" == "mutations" ]; then
    declare -a ALGORITHMS=('none' 'inversion' 'shift' 'exchange' 'shift,exchange')
    for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
    do
       for ALGORITHM in ${ALGORITHMS[@]}
       do
         M=$ALGORITHM
         execute $TESTRUN
       done
    done
fi

if [ "$TESTNAME" == "recombinations" ]; then
    declare -a ALGORITHMS=('ordered' 'mapped' 'random')
    for TESTRUN in $(eval echo {1..$NUMBEROFRUNS})
    do
       for ALGORITHM in ${ALGORITHMS[@]}
       do
         R=$ALGORITHM
         execute $TESTRUN
       done
    done
fi
