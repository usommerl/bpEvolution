bpEvolution
===========

A customizable evolutionary algorithm to solve one-dimensional bin packing problems.

Usage
-----

    bpEvolution [options] <problem-id>

      -s <INTEGER> | --population-size <INTEGER>
            Size of the population. The default value is '500'
      -g <INTEGER> | --max-generations <INTEGER>
            Maximum number of generations. The default value is '1000'
      -d <next-fit|first-fit|best-fit> | --genotype-decoder <next-fit|first-fit|best-fit>
            Decoder heuristic which translates the genotype of a individual to its
            corresponding phenotype. The default value is 'best-fit'
      -q <1|2> | --quality-function <1|2>
            Quality function which evaluates the phenotype of a individual.
            The default value is '1'
      -ps <SELECTION> | --parent-selection <SELECTION>
            Algorithm which selects individuals for recombination.
            Valid SELECTION keywords are: best|probabilistic|tournament=<INTEGER>
      -r <ordered|mapped|random> | --recombination <ordered|mapped|random>
            Algorithm which recombines two parent individuals.
      -m <none|MUTATION{,MUTATION}> | --mutation <none|MUTATION{,MUTATION}>
            Algorithm which is used to mutate child individuals. If there is more than
            one MUTATION specified, the algorithms will be applied in the defined order.
            Valid MUTATION keywords are: inversion|shift|exhange. The default value is 'shift'
      -es <SELECTION> | --environment-selection <SELECTION>
            Algorithm which selects individuals for the next generation.
            Valid SELECTION keywords are: best|probabilistic|tournament=<INTEGER>
      -o <file> | --output <file>
            Writes the program output to a file. In addition to the command line output,
            detailed information about the bin occupancy of the overall best individual
            will be written to the file as well.
      <problem-id>
            Identifier of the E. Falkenauer problem instance (see: http://goo.gl/Noa4S).

Note
----
This project was part of a university course and is no longer actively maintained. You can download the paper which evaluates the algorithm [here](doc/documentation.pdf) (German language).
