(ns tween-clj-examples.core
  (:use [quil.core]
        [tween-clj.core :exclude [constrain]])
  (import [java.util Date]))

;; Defines the current tweening movement.
(defrecord Movement [from         ; tween from this [x,y]
                     to           ; tween to this [x,y]
                     start-time   ; the time the tween started
                     end-time     ; the time it's scheduled to finish
                     transition-func  ; the transition function use to
                                      ; update.
                     ])

;; Where we start from in the animation
(def starting-position [20 20])
;; The current movement state or nil if no animation in progress
(def movement-state (atom nil))
;; The current position of the ball
(def current-position (atom starting-position))
;; A full list of transition functions keyed by the name
(def transition-names {"transition-linear"  transition-linear
                       "transition-pow"     transition-pow
                       "transition-expo"    transition-expo
                       "transition-sine"    transition-sine
                       "transition-circ"    transition-circ
                       "transition-back"    transition-back
                       "transition-bounce"  transition-bounce
                       "transition-elastic" transition-elastic})
;; Just the transition functions
(def transitions (vals transition-names))
;; A full list of easing functions keyed by the name
(def easing-names {"ease-in"     ease-in
                   "ease-out"    ease-out
                   "ease-in-out" ease-in-out})
;; Just the easing functions
(def easings (vals easing-names))

;; The offset position for the start of the table animations
(def height-offset 50)

;; Setup the drawing surface
(defn setup []
  (smooth)
  (frame-rate 60)
  (no-stroke))

(defn calc-new-pos
  "Calculate the new position for the ball.  Destructures the Movement record
   and uses this information to calculate a new [x,y] as a result."
  [{:keys [from to start-time end-time current-time transition-func]} ]
  (let [p      (range-to-p start-time end-time current-time)
        coords (partition 2 (interleave from to))]
    (map (fn [[p1 p2]]
           (p-to-range p1 p2 (transition-func p)))
         coords)))

(defn cell-width
  "The width of each cell in the transition table"
  [] (/ (width) (count easings)))
(defn cell-height
  "The height of each cell in the transition table"
  [] (/ (- (height) height-offset) (count transitions)))

(defn col-for-x
  "Given an x position will calculate the column in the transition table
   or will return nil if x is not in the correct range"
  [x]
  (let [col (int (/ x (cell-width)))]
    (if (or (>= col (count easings)) (< col 0))
      nil
      col)))

(defn row-for-y
  "Given a y position will calculate the row in the transition table or
   will return nil if y is not in the correct range"
  [y]
  (let [row (int (/ (- y height-offset) (cell-height)))]
    (if (or (>= row (count transitions) (< row 0)))
      nil
      row)
    )
  )

(defn draw
  "Called by quil to deaw a new frame."
  []
  (let [box-background-color           (color 255 255 0)
        box-background-highlight-color (color 100 100 0)
        box-border-color               (color 255 50 50)
        box-stroke-weight              5
        text-color                     (color 100 0 40)
        window-background              (color 0 0 0)
        font-size-px                   17]

    ;; paint in the background color
    (background window-background)

    ;; set some default styling
    (fill box-background-color)
    (stroke-weight box-stroke-weight)
    (stroke box-border-color)
    (text-size font-size-px)

    ;; Draw the table of transition and easing functions
    (with-translation [0 height-offset]
      (doseq [[col [e-name e]] (partition 2 (interleave (range) easing-names))
              [row [t-name t]] (partition 2 (interleave (range) transition-names))]

        ;; If the mouse is over a particular cell we heightlight it.
        (if (and (= col (col-for-x (mouse-x))) 
                 (= row (row-for-y (mouse-y))))
          (fill box-background-highlight-color)
          (fill box-background-color))
        
        (rect (* col (cell-width)) (* row (cell-height))
              (cell-width)
              (cell-height))
        (with-translation [20 35]
          (fill text-color)
          (text (str t-name) (* col (cell-width)) (* row (cell-height)))
          (with-translation [0 17]
            (text (str e-name) (* col (cell-width)) (* row (cell-height)))
            ))))

    (if @movement-state
      (do
        (reset! current-position
                (calc-new-pos (assoc @movement-state :current-time (.getTime (Date.)))))
        (if (> (.getTime (Date.)) (:end-time @movement-state))
          (reset! movement-state nil))))

    (ellipse (first @current-position) (second @current-position) 30 30)))

(defn click
  "Called when the mouse is clicked"
  []
  
  ;; Create a new animation with the currently selected transition and
  ;; easing function.
  (reset! movement-state (Movement. starting-position
                                    [(- (width) 20) 20]
                                    (.getTime (Date.))
                                    (+ 2000 (.getTime (Date.)))
                                    (partial (nth easings (col-for-x (mouse-x)))
                                             (nth transitions (row-for-y (mouse-y))))
                                    )))

;; Create the sketch
(defn -main [& args]
  (defsketch example                  ;;Define a new sketch named example
    :title "Transition example"       ;;Set the title of the sketch
    :setup setup                      ;;Specify the setup fn
    :draw draw                        ;;Specify the draw fn
    :size [800 600]
    :mouse-clicked click
    ))

(comment
  (sketch-stop example)
  (sketch-start example)
  (sketch-close example)
  )


