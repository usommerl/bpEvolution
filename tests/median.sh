#!/bin/bash

NUMBEROFFILES=$(ls -l1 ${1}* | wc -l)

for i in $(eval echo {1..$NUMBEROFFILES})
do
  n=$(eval printf "%02d" $i)
  PATTERN=$(awk '/# theoretical optimum      :/ { sub(/# theoretical optimum      : /,"",$0); sub(/ +$/,"",$0);print $0"\\.[[:digit:]]{3,}"}' ${1}${n}.dat)
  RUNTIME=$(awk '/# Total time:/ { sub(/# Total time: /,"",$0); sub(/ s/,"",$0); printf "%04d %s\n",$0,FILENAME}' ${1}${n}.dat)
  GENERATION=$(awk "/$PATTERN/ "'{ printf "%05d",$1}' ${1}${n}.dat)
  if [ "$GENERATION" == "" ]; then GENERATION="XXXXX"; fi
  LINE="${GENERATION} ${RUNTIME}\n"
  if [ "$2" == "onlyruntime" ];then LINE="${RUNTIME}\n"; fi
  LINES=${LINES}${LINE}
done

echo -ne $LINES | sort
echo

idx=$(awk 'BEGIN { rounded = sprintf("%.0f",'$NUMBEROFFILES'/2); print rounded }')
MEDIAN=$(echo -ne $LINES | sort | sed -n "${idx}p")
echo "MEDIAN: "${MEDIAN}
