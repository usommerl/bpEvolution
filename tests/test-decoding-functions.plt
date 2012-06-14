set terminal epslatex size 13.7cm,8cm color colortex
#set terminal postscript eps size 3.5,2.62 enhanced color dl 3 font "Helvetica,11" 
#set encoding utf8
set output "../doc/figures/test-decoding-functions.tex"
unset log   
set style fill solid border -1
set ytic auto
set bmargin 4
set xrange [0:350]
set yrange [46.5:59]
set xlabel '\footnotesize{Generation}'
set ylabel '\shortstack{\footnotesize{Anzahl der ben\"otigten Beh\"alter} \\ \scriptsize{(Bestes Individuum der Population)}}'

#key settings
set key at 220,55.93 left top 
set key width -5 samplen 7 spacing 1.05
#set key box ls 1
# define axis
# remove border on top and right and set color to gray
set style line 100 lc rgb 'black' lt 1 lw 4
set border 3 back ls 100
set tics out nomirror scale 1
# define grid
set style line 101 lc rgb '#808080' lt 0 lw 1
set grid xtics back ls 101

# color definitions
set style line 1 lc rgb '#0060ad' pt 1 ps 1 lt 1 lw 4 # --- blue
set style line 2 lc rgb '#FFD700' pt 1 ps 1 lt 1 lw 4 # --- gold
set style line 3 lc rgb '#dd181f' pt 1 ps 1 lt 2 lw 4 # --- red
set style line 4 lc rgb '#000000' pt 1 ps 1 lt 1 lw 1 # --- min

set rmargin 2

min = 48

set label '\footnotesize{$k_{min}$}' at 315,47.4

plot './data/test_decfunc_next-fit_u120_00_05.dat' using 1:2 with lines ls 1 title '\footnotesize{\textit{Next-Fit}}', \
     './data/test_decfunc_first-fit_u120_00_04.dat' using 1:2 with lines ls 2 title '\footnotesize{\textit{First-Fit}}', \
     './data/test_decfunc_best-fit_u120_00_01.dat' using 1:2 with lines ls 3 title '\footnotesize{\textit{Best-Fit}}', \
     min notitle ls 4

