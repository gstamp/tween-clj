(ns tween-clj.core)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Transition types
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn transition-linear
  "Calculates a linear transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [pos] pos)

(defn transition-pow
  "Calculates a power transition.

  'x' the the power to raise to.
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ([pos] (transition-pow 6 pos))
  ([x pos] (Math/pow pos x)))

(defn transition-expo
  "Calculates a exponential transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [pos]
  (Math/pow 2 (* 8 (- pos 1))))

(defn transition-sine
  "Calculates a sineousidal transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [pos]
  (- 1 (Math/sin (/ (* (- 1 pos) Math/PI) 2))))

(defn transition-circ
  "Calculates a circular transition.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [pos]
  (- 1 (Math/sin (Math/acos pos))))

(defn transition-back
  "Calculates a transition that moves backwards before moving forwards.

  'x' controls the bounceback size.
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ([pos] (transition-back 1.618 pos))
  ([x pos] (* (Math/pow pos 2) (- (* (inc x) pos) x))))

(defn transition-bounce
  "Calculates a transition that looks somewhat like a bouncing ball.

  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [pos]
  (loop [a 0 b 1]
    (if (>= pos (/ (- 7 (* 4 a)) 11))
      (- (* b b)
         (Math/pow (/ (- 11 (* 6 a) (* 11 pos)) 4)
                   2))
      (recur (+ a b) (/ b 2)))))

(defn transition-elastic
  "Calculates a transition that looks somewhat like a bouncing ball.

  'x' controls the elasticity
  'pos' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  ([pos] (transition-elastic 1 pos))
  ([x pos] (*
            (Math/pow 2 (* 10 (dec pos)))
            (Math/cos (* 20 pos Math/PI (/ x 3))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Easing functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn ease-in
  "Eases into the transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [transition p]
  (transition p))

(defn ease-out
  "Eases out of a transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [transition p]
  (- 1 (transition (- 1 p) )))

(defn ease-in-out
  "Eases in to then out of of a transition where

  'transition' is a function defining the transition type and
  'p' is a number between 0 and 1 representing the position of the transition between
  starting (0) and finished (1)"
  [transition p]
  (if (<= p 0.5)
    (/ (transition (* 2 p))
       2)
    (/ (- 2 (transition (* 2 (- 1 p))))
       2)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Extra functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn constrain
  "Constrains a value to not exceed a maximum and minimum value."
  [amt low high]
  (if (< amt low) low
      (if (> amt high)
        high
        amt)))

(defn range-to-p
  "Converts the current position in a range into a p value for use in the transition functions.

  'start' is the starting value of the range
  'end' is the ending value of the range
  'current' is the current value between 'start' and 'end'"
  [start end current]
  (constrain 
   (/ (- current start) (- end start))
   0
   1))

(defn p-to-range
  "Converts a p value (between 0 & 1) back into a range between start and end.

  'start' is the starting value of the range
  'end' is the ending value of the range
  'p' is a value between 0 & 1."
  [start end p]
  (constrain
   (+ start
      (* p (- end start)))
   start
   end))