(ns tween-clj.core
  #?(:cljs (:require [tween-clj.math-interop :as Math])))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transition types
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn transition-linear
  "Calculates a linear transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [^double pos] pos)

(defn transition-pow
  "Calculates a power transition.

  'x' the the power to raise to.
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  (^double [^double pos] (transition-pow 6 pos))
  (^double [^double x ^double pos] (Math/pow pos x)))

(defn transition-expo
  "Calculates a exponential transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [^double pos]
  (Math/pow 2 (* 8 (- pos 1))))

(defn transition-sine
  "Calculates a sineousidal transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [^double pos]
  (- (double 1)
     (Math/sin (/ (* (- (double 1) pos)
                     Math/PI)
                  (double 2)))))

(defn transition-circ
  "Calculates a circular transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [^double pos]
  (- 1 (Math/sin (Math/acos pos))))

(defn transition-back
  "Calculates a transition that moves backwards before moving forwards.

  'x' controls the bounceback size.
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  (^double [^double pos] (transition-back (double 1.618) pos))
  (^double [^double x ^double pos] (* (Math/pow pos (double 2))
                              (- (* (inc x) pos) x))))

(defn transition-bounce
  "Calculates a transition that looks somewhat like a bouncing ball.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [^double pos]
  (loop [a (double 0) b (double 1)]
    (if (or (>= pos (/ (- 7 (* 4 a)) 11))
            (not (<= 0 pos 1.0 )))
      (- (* b b)
         (Math/pow (/ (- 11 (* 6 a) (* 11 pos)) 4)
                   2))
      (recur (+ a b) (/ b 2)))))

(defn transition-elastic
  "Calculates a transition that looks somewhat like a bouncing ball.

  'x' controls the elasticity
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  (^double [^double pos] (transition-elastic (double 1) pos))
  (^double [^double x ^double pos]
           (* (Math/pow (double 2) (* (double 10) (dec pos)))
              (Math/cos (/ (* (double 20) (dec pos) Math/PI x)
                           (double 3))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Easing functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ease-in
  "Eases into the transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [transition ^double p]
  (transition p))

(defn ease-out
  "Eases out of a transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [transition ^double p]
  (- (double 1) (transition (- (double 1) p) )))

(defn ease-in-out
  "Eases in to then out of of a transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ^double
  [transition ^double p]
  (if (<= p (double 0.5))
    (/ (transition (* (double 2) p))
       (double 2))
    (/ (- 2 (transition (* (double 2) (- (double 1) p))))
       (double 2))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Extra functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn constrain
  "Constrains a value to not exceed a maximum and minimum value."
  ^double
  [^double amt ^double low ^double high]
  (if (< amt low) low
      (if (> amt high)
        high
        amt)))

(defn range-to-p
  "Converts the current position in a range into a p value for use in the transition functions.

  'start' is the starting value of the range
  'end' is the ending value of the range
  'current' is the current value between 'start' and 'end'"
  ^double
  [^double start ^double end ^double current]
  (constrain 
   (/ (- current start) (- end start))
   (double 0)
   (double 1)))

(defn p-to-range
  "Converts a p value (between 0 & 1) back into a range between start and end.

  'start' is the starting value of the range
  'end' is the ending value of the range
  'p' is a value between 0 & 1."
  ^double
  [^double start ^double end ^double p]
  (constrain
   (+ start
      (* p (- end start)))
   start
   end))
