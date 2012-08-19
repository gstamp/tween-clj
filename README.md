# tween-clj

A Clojure library designed to tween a value between two points.

Inbetweening or tweening is the process of generating intermediate
points between two points.

[![Build Status](https://secure.travis-ci.org/gstamp/tween-clj.png?branch=master)](http://travis-ci.org/gstamp/tween-clj)

## Usage

Simply add this to your leiningen deps: [tween-clj "0.3.0"]

The tweening interface defines two main concepts.  Transition
functions and easing functions.

The transition function defines how a value transitions between two
states. There are several transition functions to choose from.

The easing function defines how the transition proceeds.  You can ease
into a transition (with ease-in) or out of a transition (with
ease-out) or even both with east-in-out.

An example:

```clojure
(map (partial ease-in transition-elastic) 
     [0 0.1 0.2 0.5 0.9 1])
```

Available transition types:

- transition-linear
- transition-pow
- transition-expo
- transition-sine
- transition-circ
- transition-back
- transition-bounce
- transition-elastic

## License

Copyright © 2012 Glen Stampoultzis

Distributed under the Eclipse Public License, the same as Clojure.
