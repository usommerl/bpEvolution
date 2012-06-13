#!/bin/bash


DATAFOLDER="/home/uwe/study/msc/4_semester/ealg/tests/data"
NUMBEROFFILES=$(ls -l1 ${1}* | wc -l)
LINES=""

for i in $(eval echo {1..$NUMBEROFFILES})
do
 n=$(eval printf "%02d" $i)
 LINE=$(awk '/# Total time:/ { sub(/# Total time: /,"",$0); sub(/ s/,"",$0); printf "%04d %s\n",$0,FILENAME}' ${1}${n}.dat)'\n'
 LINES=${LINES}${LINE}
done
echo -ne $LINES | sort
echo
idx=$(awk 'BEGIN { rounded = sprintf("%.0f",'$NUMBEROFFILES'/2); print rounded }')
MEDIAN=$(echo -ne $LINES | sort | sed -n "${idx}p")
echo "MEDIAN: "${MEDIAN}
